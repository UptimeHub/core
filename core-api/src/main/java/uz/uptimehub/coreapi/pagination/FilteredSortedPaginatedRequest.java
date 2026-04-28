package uz.uptimehub.coreapi.pagination;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.swing.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FilteredSortedPaginatedRequest<T extends Filter, E extends RuntimeException>{

    private final T filter;
    private final Pageable pageable;

    public FilteredSortedPaginatedRequest(T filter, Pageable incomingPageable, Supplier<E> exceptionProvider) {
        this.filter = filter;

        if (isSortRuleValid(incomingPageable)) {
            throw exceptionProvider.get();
        }

        this.pageable = PageRequest.of(
                incomingPageable.getPageNumber(),
                incomingPageable.getPageSize(),
                addUniqueSort(overrideSortProperties(incomingPageable.getSort()))
        );
    }

    public Pageable getPageable() {
        return pageable;
    }

    public T getFilter() {
        return filter;
    }

    private boolean isSortRuleValid(Pageable pageable) {
        Set<String> allowedSortProps = Arrays.stream(filter.getClass().getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.toSet());

        List<String> provided = StreamSupport.stream(pageable.getSort().spliterator(), false)
                .map(Sort.Order::getProperty)
                .toList();

        boolean allValid = allowedSortProps.containsAll(provided);
        if (!allValid) {
            List<String> invalid = provided.stream()
                    .filter(p -> !allowedSortProps.contains(p))
                    .toList();
            System.err.println("Unsupported sort keys: " + invalid);
        }
        return allValid;
    }

    private Sort addUniqueSort(Sort sort) {
        String idFieldName = filter.getClass().isAnnotationPresent(IdPropertyOverride.class)
                ? filter.getClass().getAnnotation(IdPropertyOverride.class).value()
                : "id";

        boolean hasIdSort = StreamSupport.stream(sort.spliterator(), false)
                .anyMatch(o -> o.getProperty().equals(idFieldName));

        if (hasIdSort) return sort;

        return sort.and(Sort.by(Sort.Order.asc(idFieldName)));
    }

    private Sort overrideSortProperties(Sort sort) {
        Map<String, String> overrides = Arrays.stream(filter.getClass().getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(SortPropertyOverride.class))
                .collect(Collectors.toMap(
                        Field::getName,
                        f -> f.getAnnotation(SortPropertyOverride.class).value()
                ));

        if (overrides.isEmpty() || sort.isUnsorted())
            return sort;

        return StreamSupport.stream(sort.spliterator(), false)
                .map(order -> new Sort.Order(order.getDirection(), overrides.getOrDefault(order.getProperty(), order.getProperty())))
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        Sort::by
                ));
    }
}
