package pageRank;

import pageRank.model.PageRankModel;
import scanner.output.ScannerResult;
import utils.Holder;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;


public class SingleThreadPageRank extends AbstractPageRank implements PageRankInterface {

    @Override
    public Collection<PageRankModel> calculate(ScannerResult result) {
        final int size = result.getAllLinks().size();
        double[] pageRanks = init(size);
        final Holder holder = new Holder(size);
        double[] prevRanks;
        do {
            prevRanks = Arrays.copyOf(pageRanks, pageRanks.length);
            final double[] finalPrevRanks = prevRanks;
            for (String link : result.getAllLinks()) {
                final double[] newRank = new double[]{(1 - DAMPING_FACTOR) / size};
                final int index = holder.getIndex(link);
                result.setOutLinks(link).forEach(out -> {
                    int in = result.setInLinks(out).size();
                    newRank[0] += DAMPING_FACTOR * finalPrevRanks[index] * 1D / (in == 0 ? 1 : in);
                });
                pageRanks[index] = newRank[0];
            }
            pageRanks = normalize(pageRanks);
        } while (diff(pageRanks, prevRanks) > EPS);

        double[] finalPageRanks = pageRanks;
        return holder.ids().stream()
                .map(id -> new PageRankModel(id, finalPageRanks[holder.getIndex(id)]))
                .collect(Collectors.toList());
    }

}
