package ru.otus.cacheengine.cachehw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.WeakReference;
import java.util.*;

public class MyCache<K, V> implements HwCache<K, V> {
    //Надо реализовать эти методы
    private static final Logger logger = LoggerFactory.getLogger(MyCache.class);

    private final Map<K, V> storage = new WeakHashMap<>();
    private final List<WeakReference<HwListener<K, V>>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        storage.put(key, value);
        notifyListeners(key, value, "put");
    }

    @Override
    public void remove(K key) {
        storage.remove(key);
        notifyListeners(key, null, "remove");
    }

    @Override
    public V get(K key) {
        notifyListeners(key, null, "get");
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
            if (wl != null && listener.equals(wl.get())) {
                href = wl;
            }
        }
        listeners.remove(href);
        logger.info("removed listener: {}, listeners now: {}", listener.hashCode(), listeners.size());
    }

    private void notifyListeners(K key, V value, String action) {
        listeners.removeIf(obj -> Objects.isNull(obj) || Objects.isNull(obj.get()));
        try {
            listeners.forEach(l -> l.get().notify(key, value, action));
        } catch (Exception e){
            logger.info("notifyListeners threw an exception, {}", e);
        }
    }
}
