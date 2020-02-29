package de.mkammerer.aluhut.util;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IterableUtilTest {
    @Test
    void nthElement() {
        Set<String> set = new LinkedHashSet<>();
        set.add("one");
        set.add("two");
        set.add("three");

        assertThat(IterableUtil.nthElement(set, 0)).isEqualTo("one");
        assertThat(IterableUtil.nthElement(set, 1)).isEqualTo("two");
        assertThat(IterableUtil.nthElement(set, 2)).isEqualTo("three");

        assertThatThrownBy(() -> IterableUtil.nthElement(set, 3)).isInstanceOf(NoSuchElementException.class);
    }
}