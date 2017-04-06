package scanner;

import scanner.output.ScannerResult;

import java.util.HashSet;
import java.util.Set;

public class SingleThreadScanner implements ScannerInterface {

    public ScannerResult work(String startUrl) {
        final ScannerResult result = ScannerResult.newInstance();
        work(startUrl, result, new HashSet<>());
        return result;
    }

    private void work(String from, final ScannerResult result, final Set<String> visitUrl) {
        visitUrl.add(from);
        ScannerInterface.parse(from)
                .filter(url -> result.addLink(from, url))
                .filter(visitUrl::add)
                .forEach(url -> work(url, result, visitUrl));
    }
}
