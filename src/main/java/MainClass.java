import pageRank.PageRankInterface;
import pageRank.model.PageRankModel;
import scanner.ScannerInterface;
import scanner.output.ScannerResult;
import utils.Constants;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Comparator;
import java.util.Scanner;
import java.util.stream.IntStream;

public class MainClass {

    private static String mUrl = "http://www.kpfu.ru/";

    public static void main(String[] args) {
        System.out.println("To view all commands print: -help");
        System.out.println("Enter url:");

        Scanner scanner = new Scanner(System.in);
        mUrl = scanner.nextLine();
        if (mUrl.contains("-help")) {
            getHelp();
        }else {
            getResults();
        }
    }

    private static void getResults() {
        String[] array = mUrl.split("\\s");
        for (String value : array) {
            System.out.println("value: " + value);
        }
        System.out.println("Calculations started...\n");
        final String url = array[0];

        if (array.length > 1) {
            IntStream.range(1, array.length)
                    .mapToObj(i -> array[i])
                    .map(command -> command.split("="))
                    .forEach(command -> {
                        switch (command[0]) {
                            case Constants.SCANNER_SPARSE:
                                Constants.put(Constants.SCANNER_SPARSE, Boolean.TRUE);
                                break;
                            case Constants.SCANNER_SIZE:
                                Constants.put(Constants.SCANNER_SPARSE, command[1]);
                                break;
                            case Constants.SCANNER_THREAD:
                                Constants.put(Constants.SCANNER_THREAD, command[1]);
                                break;
                            case Constants.PAGERANK_THREAD:
                                Constants.put(Constants.PAGERANK_THREAD, command[1]);
                                break;
                            default:
                                throw new IllegalArgumentException("Not found argument " + command[0]);
                        }
                    });
        }
        final long start = System.currentTimeMillis();
        final ScannerResult scannerResult = ScannerInterface.newInstance().work(url);
        final Collection<PageRankModel> result = PageRankInterface.newInstance().calculate(scannerResult);
        final long end = System.currentTimeMillis();
        showResults(start, scannerResult, result, end);
    }

    private static void showResults(long start, ScannerResult scannerResult, Collection<PageRankModel> result, long end) {
        System.out.println("---------------------------MATRIX RESULT---------------------------\n");
        scannerResult.printAsMatrix(new PrintWriter(System.out));
        System.out.println("\n---------------------------PAGE RANK---------------------------\n");
        result.stream().sorted(Comparator.comparing(PageRankModel::getRank))
                .forEach(r -> System.out.println(r.page + " : " + r.rank));
        System.out.println("\n time of calculations " + (end - start) + "ms");
    }

    private static void getHelp() {
        System.out.println("---------------------------HELP---------------------------\n");
        System.out.println("Avaliable commands:");
        System.out.println("scan_thread - Scanner threads (by default 1)\n" +
                "scan_size - Scanner size (default 100)\n" +
                "scan_sparse - use Scanner based on sparsed matrix\n" +
                "pagerank_thread - PageRank threads (default 1)\n");
        Scanner sc = new Scanner(System.in);
        mUrl = sc.nextLine();
        getResults();
    }
}
