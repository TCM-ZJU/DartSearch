package dartie.prefuse.data.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import dartie.prefuse.data.Graph;


/**
 * Abstract base class implementation of the GraphWriter interface. Provides
 * implementations for all but the
 * {@link dartie.prefuse.data.io.GraphWriter#writeGraph(Graph, OutputStream)}
 * method.
 *  
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public abstract class AbstractGraphWriter implements GraphWriter {

    /**
     * @see dartie.prefuse.data.io.GraphWriter#writeGraph(prefuse.data.Graph, java.lang.String)
     */
    public void writeGraph(Graph graph, String filename) throws DataIOException
    {
        writeGraph(graph, new File(filename));
    }

    /**
     * @see dartie.prefuse.data.io.GraphWriter#writeGraph(prefuse.data.Graph, java.io.File)
     */
    public void writeGraph(Graph graph, File f) throws DataIOException {
        try {
            writeGraph(graph, new FileOutputStream(f));
        } catch ( FileNotFoundException e ) {
            throw new DataIOException(e);
        }
    }

} // end of abstract class AbstractGraphReader
