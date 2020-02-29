package de.mkammerer.aluhut.util;

import java.util.Iterator;

public final class IterableUtil {
    private IterableUtil() {
    }

    /**
     * Picks the nth element from the given iterable
     *
     * @param iterable iterable to pick elements from
     * @param n        nth element
     * @param <T>      type of iterable elements
     * @return nth element
     * @throws java.util.NoSuchElementException if n is too big
     */
    public static <T> T nthElement(Iterable<T> iterable, int n) {
        Iterator<T> iterator = iterable.iterator();
        for (int i = 0; i < n; i++) {
            iterator.next();
        }

        return iterator.next();
    }
}
