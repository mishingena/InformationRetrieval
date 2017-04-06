package scanner;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import scanner.output.ScannerResult;
import utils.Constants;

import java.util.Objects;
import java.util.stream.Stream;

import static utils.Constants.ANCHOR;
import static utils.Constants.SCANNER_THREAD;

public interface ScannerInterface {
    
    /**
     * @param startUrl where to start
     * @return tree of links since startUrl
     */
    ScannerResult work(String startUrl);

    static ScannerInterface newInstance() {
        final String crawlerThreads = Constants.get(SCANNER_THREAD);
        boolean useMulti = StringUtils.isNumeric(crawlerThreads) && Integer.valueOf(crawlerThreads) > 1;
        return useMulti ? new MultyThreadScanner(Integer.valueOf(crawlerThreads)) : new SingleThreadScanner();
    }

    static Stream<String> parse(String page) {
        try {
            return Jsoup.connect(page).get()
                    .body()
                    .select("a")
                    .stream()
                    .map(l -> l.attr("abs:href"))
                    .map(l -> l.contains(ANCHOR) ? l.substring(0, l.indexOf(ANCHOR)) : l)
                    .filter(Objects::nonNull).filter(s -> !s.isEmpty());
        } catch (Exception e) {
            return Stream.empty();
        }

    }

}
