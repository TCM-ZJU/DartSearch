package edu.zju.tcmsearch.util.GBK2Big5;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class Simple2TraditionFilter implements Filter {

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
        G2B = GB2Big5.getInstance();
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		try {
			response.resetBuffer();
			chain.doFilter(request,response);
			if(response.isCommitted()){
			String encoding = response.getCharacterEncoding();
			response.getOutputStream().println(encoding);
			String s = G2B.simple2traditional("中华人民共和国","gbk");
			response.getOutputStream().println(s);
			}
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void destroy() {

	}
	
	private GB2Big5 G2B;

}
