package edu.zju.tcmsearch.exception.query;
/*
 * 浙江大学网格实验室
 * @author 谢骋超 
 * 2005年
 */
public class TcmRuntimeException extends RuntimeException{
    /**
	 * 
	 */
	private static final long serialVersionUID = 5084264984915009719L;

	public TcmRuntimeException(String msg) {
      super(msg);
    }
    
    public TcmRuntimeException(String msg, Throwable ex) {
      super(msg,ex);
    }
}
