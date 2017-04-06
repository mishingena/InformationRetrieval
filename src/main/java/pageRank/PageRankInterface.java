package pageRank;

import org.apache.commons.lang3.StringUtils;
import pageRank.model.PageRankModel;
import scanner.output.ScannerResult;
import utils.Constants;

import java.util.Collection;

public interface PageRankInterface {

    double DAMPING_FACTOR = 0.85;
    double EPS = 0.000001;

    /**
     * @param result tree links
     * @return page ranks of tree links
     */
    Collection<PageRankModel> calculate(ScannerResult result);

    static PageRankInterface newInstance() {
        final String property = Constants.get(Constants.PAGERANK_THREAD);
        boolean useMulti = StringUtils.isNumeric(property) && Integer.valueOf(property) > 1;
        return useMulti ? new MultiThreadPageRank(Integer.valueOf(property)) : new SingleThreadPageRank();
    }
}
