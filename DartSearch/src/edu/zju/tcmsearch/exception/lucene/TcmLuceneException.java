/*
 * Created on 2005-12-16
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.exception.lucene;

import edu.zju.tcmsearch.exception.query.TcmRuntimeException;

public class TcmLuceneException extends TcmRuntimeException {
    

    /**
     * 
     */
    private static final long serialVersionUID = 3179966661703193740L;

    public TcmLuceneException(String msg) {
      super(msg);
    }
    
    public TcmLuceneException(String msg, Throwable ex) {
      super(msg,ex);
    }
}
