package scanner.output;

import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class SparseMatrixOutput implements ScannerResult {
    private final transient Map<String, Integer> mMap;
    private final AtomicInteger mFreeInt ;
    private final List<AtomicInteger> mValues;
    private final List<Integer> mRows;
    private final List<Integer> mLines;
    private final int mSize;
    private Map<Integer, String> mReverse;


    SparseMatrixOutput(int mSize) {
        mFreeInt = new AtomicInteger();
        this.mMap = new ConcurrentHashMap<>(mSize, 1F);
        this.mValues = new CopyOnWriteArrayList<>();
        this.mRows = new CopyOnWriteArrayList<>();
        this.mLines = new CopyOnWriteArrayList<>();
        this.mSize = mSize;
    }

    @Override
    public boolean addLink(String from, String to) {
        if ((!mMap.containsKey(from) || !mMap.containsKey(to)) && mFreeInt.get() >= mSize) {
            return false;
        }

        final AtomicBoolean hasKeys = new AtomicBoolean(true);

        final int fromIndex = mMap.computeIfAbsent(from, k -> {
            hasKeys.set(false);
            return mFreeInt.getAndIncrement();
        });
        final int toIndex = mMap.computeIfAbsent(to, k -> {
            hasKeys.set(false);
            return mFreeInt.getAndIncrement();
        });

        int findIndex = -1;

        if (hasKeys.get()) {
            for (int i = 0; i < mRows.size(); i++) {
                if (mRows.get(i).equals(fromIndex) && mLines.get(i).equals(toIndex)) {
                    findIndex = i;
                    break;
                }
            }
        }

        if (findIndex != -1) {
            mValues.get(findIndex).incrementAndGet();
        } else {
            synchronized (this) { //there may be mistakes
                mRows.add(fromIndex);
                mLines.add(toIndex);
                mValues.add(new AtomicInteger(1));
            }
        }

        return true;
    }

    @Override
    public Collection<String> getAllLinks() {
        return mMap.keySet();
    }

    @Override
    public Collection<String> setInLinks(String page) {
        final int col = mMap.get(page);
        final Collection<String> result = new ArrayList<>();
        for (int i = 0; i < mLines.size(); i++) {
            if (col == mLines.get(i)) {
                result.add(reverse().get(mRows.get(i)));
            }
        }
        return result;
    }

    @Override
    public Collection<String> setOutLinks(String page) {
        final int row = mMap.get(page);
        final Collection<String> result = new ArrayList<>();
        for (int i = 0; i < mRows.size(); i++) {
            if (row == mRows.get(i)) {
                result.add(reverse().get(mLines.get(i)));
            }
        }
        return result;
    }

    @Override
    public void printAsMatrix(PrintWriter printWriter) {
        mMap.forEach((k, v) -> printWriter.println(v + " : " + k));
        printWriter.println();

        int[][] matrix = new int[mSize][mSize];

        for (int i = 0; i < mValues.size(); i++) {
            matrix[mRows.get(i)][mLines.get(i)] = mValues.get(i).intValue();
        }

        for (int[] links : matrix) {
            printWriter.println(Arrays.toString(links));
        }

        printWriter.println();
    }

    private Map<Integer, String> reverse() {
        if (mReverse == null) {
            mReverse = new ConcurrentHashMap<>(mSize, 1F);
            mMap.forEach((k, v) -> mReverse.put(v, k));
        }
        return mReverse;
    }
}
