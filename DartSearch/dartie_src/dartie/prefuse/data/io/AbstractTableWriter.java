package dartie.prefuse.data.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import dartie.prefuse.data.Table;


/**
 * Abstract base class implementation of the TableWriter interface. Provides
 * implementations for all but the
 * {@link dartie.prefuse.data.io.TableWriter#writeTable(Table, java.io.OutputStream)}
 * method.
 *  
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public abstract class AbstractTableWriter implements TableWriter {

    /**
     * @see dartie.prefuse.data.io.TableWriter#writeTable(prefuse.data.Table, java.lang.String)
     */
    public void writeTable(Table table, String filename) throws DataIOException
    {
        writeTable(table, new File(filename));
    }

    /**
     * @see dartie.prefuse.data.io.TableWriter#writeTable(prefuse.data.Table, java.io.File)
     */
    public void writeTable(Table table, File f) throws DataIOException {
        try {
            writeTable(table, new FileOutputStream(f));
        } catch ( FileNotFoundException e ) {
            throw new DataIOException(e);
        }
    }

} // end of abstract class AbstractTableReader
