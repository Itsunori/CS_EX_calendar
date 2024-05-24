package backend;

import java.util.concurrent.*;

public class Cache<K, V> {
    private final ConcurrentMap<K, V> cacheMap = new ConcurrentHashMap<>();
    private final ConcurrentMap<K, Long> expiryMap = new ConcurrentHashMap<>();
    private final ScheduledExecutorService cleaner = Executors.newScheduledThreadPool(1);

    public Cache(long expiryDuration, TimeUnit timeUnit) {
        cleaner.scheduleAtFixedRate(() -> {
            long now = System.currentTimeMillis();
            for (K key : expiryMap.keySet()) {
                if (now > expiryMap.get(key)) {
                    cacheMap.remove(key);
                    expiryMap.remove(key);
                }
            }
        }, expiryDuration, expiryDuration, timeUnit);
    }

    public void put(K key, V value, long duration, TimeUnit timeUnit) {
        cacheMap.put(key, value);
        expiryMap.put(key, System.currentTimeMillis() + timeUnit.toMillis(duration));
    }

    public V get(K key) {
        if (expiryMap.containsKey(key) && System.currentTimeMillis() > expiryMap.get(key)) {
            cacheMap.remove(key);
            expiryMap.remove(key);
        }
        return cacheMap.get(key);
    }

    public void remove(K key) {
        cacheMap.remove(key);
        expiryMap.remove(key);
    }
}