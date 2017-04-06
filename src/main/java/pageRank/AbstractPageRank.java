package pageRank;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealVector;

abstract class AbstractPageRank implements PageRankInterface {

    double[] normalize(double[] ranks) {
        final RealVector realVector = MatrixUtils.createRealVector(ranks);
        double distance = realVector.getNorm();

        for (int i = 0; i < ranks.length; i++) {
            ranks[i] = ranks[i] / distance;
        }

        return ranks;
    }

    double diff(double[] ranks, double[] prevRanks) {
        RealVector m1 = MatrixUtils.createRealVector(ranks);
        RealVector m2 = MatrixUtils.createRealVector(prevRanks);
        return m1.subtract(m2).getNorm();
    }

    double[] init(int size) {
        final double[] doubles = new double[size];
        for (int i = 0; i < doubles.length; i++) {
            doubles[i] = 1D / size;
        }
        return doubles;
    }
}
