package edu.zju.tcmsearch.secure.service;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.apache.log4j.Logger;

public class HttpSessionListenerProxy implements HttpSessionListener {

	protected static Logger logger = Logger.getLogger(HttpSessionListenerProxy.class);
	
	public void sessionCreated(HttpSessionEvent source) {
		// TODO Auto-generated method stub
		if(null!=realListener){
			logger.debug("Proxy SessionCreated Event");
			realListener.sessionCreated(source);
		}

	}

	public void sessionDestroyed(HttpSessionEvent source) {
		// TODO Auto-generated method stub
		if(null!=realListener){
			logger.debug("Proxy SessionDestroyed Event");
			realListener.sessionDestroyed(source);
		}

	}
	
	public void setListener(HttpSessionListener listener) {
		realListener = listener;
	}

	private static HttpSessionListener realListener=null;

}
