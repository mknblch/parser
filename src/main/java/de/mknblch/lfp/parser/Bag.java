package de.mknblch.lfp.parser;

import com.sun.org.apache.bcel.internal.generic.RET;

import java.util.*;
import java.util.function.Predicate;

/**
 * Created by mknblch on 15.12.2015.
 */
public class Bag<K, T> {

    private final Map<K, Set<T>> bag = new HashMap<>();

    public Set<Map.Entry<K, Set<T>>> entrySet() {
        return bag.entrySet();
    }

    public Set<T> get(K key) {
        return bag.get(key);
    }

    public Map<K, Set<T>> getMap() {
        return bag;
    }

    public void put(K key, T... elements) {
        Set<T> set = get(key);
        if (null == set) {
            set = new HashSet<>();
            bag.put(key, set);
        }
        Collections.addAll(set, elements);
    }

    public void put(K key, Collection<T> elements) {
        Set<T> set = get(key);
        if (null == set) {
            set = new HashSet<>();
            bag.put(key, set);
        }
        set.addAll(elements);
    }

    public boolean replace(K key, T needle, T replacement) {
        final Set<T> set = get(key);
        if (null != set && set.remove(needle)) {
            set.add(replacement);
            return true;
        }
        return false;
    }

    public boolean replace(K key, T needle, Collection<T> replacement) {
        final Set<T> set = get(key);
        if (null != set && set.remove(needle)) {
            set.addAll(replacement);
            return true;
        }
        return false;
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
}
