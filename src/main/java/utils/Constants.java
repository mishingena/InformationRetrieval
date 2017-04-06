package utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Constants {

    public static final String SCANNER_THREAD = "scan_thread";
    public static final String SCANNER_SIZE = "scan_size";
    public static final String SCANNER_SPARSE = "scan_sparse";
    public static final String PAGERANK_THREAD = "pagerank_thread";
    public static final String ANCHOR = "-";

    public static final int DEFAULT_SIZE = 100;

    private final static Map<String, String> values = new ConcurrentHashMap<>();

    public static String get(String key) {
        return values.get(key);
    }

    public static <T> void put(String key, T value) {
        values.put(key, value.toString());
    }
}
