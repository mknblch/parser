package de.mknblch.lfp.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author martinknobloch
 */
public class Table<R, C, V> {

    private static final class Key<R, C> {

        private final R row;
        private final C column;

        private Key(R row, C column) {
            this.row = row;
            this.column = column;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Key<?, ?> key = (Key<?, ?>) o;
            if (row != null ? !row.equals(key.row) : key.row != null) return false;
            return !(column != null ? !column.equals(key.column) : key.column != null);
        }

        @Override
        public int hashCode() {
            int result = row != null ? row.hashCode() : 0;
            result = 31 * result + (column != null ? column.hashCode() : 0);
            return result;
        }

        public R getRow() {
            return row;
        }

        public C getColumn() {
            return column;
        }
    }

    private final Map<Key<R, C>, V> map = new HashMap<>();

    public V get(R row, C column) {
        return map.get(new Key<>(row, column));
    }

    public V put(R row, C column, V value) {
        return map.put(new Key<>(row, column), value);
    }

    public Set<R> getRows() {
        return map.keySet().stream()
                .map(Key::getRow)
                .collect(Collectors.toSet());
    }

    public Set<C> getColumns() {
        return map.keySet().stream()
                .map(Key::getColumn)
                .collect(Collectors.toSet());
    }
}
