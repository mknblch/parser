package de.mknblch.lfp.common;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by mknblch on 15.12.2015.
 */
public class Bag<K, T> {

    private final Map<K, Set<T>> bag = new HashMap<>();

    public Set<Map.Entry<K, Set<T>>> entrySet() {
        return bag.entrySet();
    }

    public List<Set<T>> values() {
        return bag.values().stream().collect(Collectors.toList());
    }

    public Map<K, Set<T>> getMap() {
        return bag;
    }

    public Set<T> get(K key) {
        return bag.get(key);
    }

    public boolean put(K key, T element) {
        return getOrCreate(key).add(element);
    }

    public boolean putAll(K key, Collection<T> elements) {
        if (null == elements) {
            return false;
        }
        return getOrCreate(key).addAll(elements);
    }

    public void clear() {
        bag.clear();
    }

    public boolean replaceIf(Predicate<T> predicate, Set<T> replacement) {
        boolean changed = false;
        for (Set<T> ts : bag.values()) {
            for (Iterator<T> it = ts.iterator(); it.hasNext(); ) {
                if (predicate.test(it.next())) {
                    it.remove();
                    ts.addAll(replacement);
                    changed = true;
                    break;
                }
            }
        }
        return changed;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();

        for (K k : bag.keySet()) {
            builder.append(k).append("->{ ");
            for (T t : bag.get(k)) {
                builder.append(t).append(" ");
            }
            builder.append("} ");
        }
        return builder.toString();
    }

    private Set<T> getOrCreate(K key) {
        Set<T> set = get(key);
        if (null == set) {
            set = new HashSet<>();
            bag.put(key, set);
        }
        return set;
    }
}
