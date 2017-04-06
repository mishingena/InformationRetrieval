package scanner;

import scanner.output.ScannerResult;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.stream.Collectors;

public class MultyThreadScanner implements ScannerInterface{

    private final ForkJoinPool mService;

    MultyThreadScanner(int threadCount) {
        mService = new ForkJoinPool(threadCount);
    }

    @Override
    public ScannerResult work(String startUrl) {
        final ScannerResult result = ScannerResult.newInstance();
        mService.invoke(new Worker(result, new ConcurrentHashMap<>(), startUrl));
        mService.shutdown();
        return result;
    }

    private static class Worker extends RecursiveAction {

        final ScannerResult result;
        final Map<String, Boolean> visitUrl;
        final String from;

        private Worker(ScannerResult result, Map<String, Boolean> visitUrl, String from) {
            this.result = result;
            this.visitUrl = visitUrl;
            this.from = from;
        }


        @Override
        public void compute() {
            addIfAbsent(visitUrl, from, Boolean.TRUE);
            final List<Worker> workers = ScannerInterface.parse(from)
                    .filter(url -> result.addLink(from, url))
                    .filter(u -> addIfAbsent(visitUrl, u, Boolean.TRUE))
                    .map(url -> new Worker(result, visitUrl, url)).collect(Collectors.toList());
            invokeAll(workers);
        }

        static <K, V> boolean addIfAbsent(Map<K, V> map, K key, V value) {
            if (map.containsKey(key)) {
                return false;
            }
            map.put(key, value);
            return true;
        }

    }
}
