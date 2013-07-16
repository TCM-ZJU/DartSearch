/*
 * Created on 2005-12-31
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.exception.lucene;

import edu.zju.tcmsearch.exception.query.TcmRuntimeException;

public class SerializeLuceneException  extends TcmRuntimeException {
    



    /**
     * 
     */
    private static final long serialVersionUID = 6259568789106732074L;

    public SerializeLuceneException(String msg) {
      super(msg);
    }
    
    public SerializeLuceneException(String msg, Throwable ex) {
      super(msg,ex);
    }
}
