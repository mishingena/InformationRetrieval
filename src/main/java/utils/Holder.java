package utils;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Holder {

    private final int maxSize;

    private final Map<String, Integer> indexMap;
    private final AtomicInteger freeIndex = new AtomicInteger();

    public Holder(int maxSize) {
        this.maxSize = maxSize;
        indexMap = new ConcurrentHashMap<>(maxSize, 1F);
    }

    public int getIndex(String id) {
        return indexMap.computeIfAbsent(id, k -> freeIndex.getAndIncrement());
    }

    public boolean contains(String id) {
        return indexMap.containsKey(id);
    }

    public boolean hasNext() {
        return freeIndex.get() < maxSize;
    }

    public Collection<String> ids() {
        return indexMap.keySet();
    }

    public Map<Integer, String> reverse() {
        Map<Integer, String> reverseMap = new ConcurrentHashMap<>(maxSize, 1F);
        indexMap.forEach((k, v) -> reverseMap.put(v, k));
        return reverseMap;
    }
}
