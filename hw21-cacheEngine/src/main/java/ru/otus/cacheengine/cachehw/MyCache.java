package ru.otus.cacheengine.cachehw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.WeakReference;
import java.util.*;

public class MyCache<K, V> implements HwCache<K, V> {
    //Надо реализовать эти методы
    private static final Logger logger = LoggerFactory.getLogger(MyCache.class);

    Map<K, V> storage = new WeakHashMap<>();
    List<WeakReference<HwListener<K, V>>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        storage.put(key, value);
        listeners.stream()
                .filter(Objects::nonNull)
                .forEach(l -> Objects.requireNonNull(l.get()).notify(key, value, "put"));
    }

    @Override
    public void remove(K key) {
        storage.remove(key);
        listeners.stream()
                .filter(Objects::nonNull)
                .forEach(l -> Objects.requireNonNull(l.get()).notify(key, null, "remove"));
    }

    @Override
    public V get(K key) {
        listeners.stream()
                .filter(Objects::nonNull)
                .forEach(l -> Objects.requireNonNull(l.get()).notify(key, null, "get"));
        return storage.get(key);
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(new WeakReference<>(listener));
        logger.info("added listener: {}, listeners now: {}", listener.hashCode(), listeners.size());
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        WeakReference<HwListener<K, V>> href = null;
        for (WeakReference<HwListener<K, V>> wl : listeners) {
            if (listener.equals(wl.get())) {
                href = wl;
            }
        }
        listeners.remove(href);
        logger.info("removed listener: {}, listeners now: {}", listener.hashCode(), listeners.size());
    }
}
