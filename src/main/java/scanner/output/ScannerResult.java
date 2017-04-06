package scanner.output;

import org.apache.commons.lang3.StringUtils;
import utils.Constants;

import java.io.PrintWriter;
import java.util.Collection;

public interface ScannerResult {

    /**
     * Add link to store
     * @param from link
     * @param to link
     * @return false - if store is filled, otherwise - true
     */
    boolean addLink(String from, String to);

    /**
     * @return all links setInLinks store
     */
    Collection<String> getAllLinks();

    /**
     * @param page link
     * @return all links having a link to this page
     */
    Collection<String> setInLinks(String page);

    /**
     * @param page link
     * @return all links on this page
     */
    Collection<String> setOutLinks(String page);

    /**
     *
     * @param printWriter
     */
    void printAsMatrix(PrintWriter printWriter);

    /**
     *
     * @return SparseMatrixOutput if useSparse is true and by other hand @return ArrayScannerOutput
     */
    static ScannerResult newInstance() {
        final String maxSize = Constants.get(Constants.SCANNER_SIZE);
        boolean useSparse = !StringUtils.isEmpty(Constants.get(Constants.SCANNER_SPARSE));
        final int size = StringUtils.isNumeric(maxSize) ? Integer.valueOf(maxSize) : Constants.DEFAULT_SIZE;
        return useSparse ? new SparseMatrixOutput(size) : new ArrayScannerOutput(size);
    }
}
