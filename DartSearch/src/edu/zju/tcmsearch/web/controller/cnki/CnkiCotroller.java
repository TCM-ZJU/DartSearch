package edu.zju.tcmsearch.web.controller.cnki;

import java.io.Writer;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

import edu.zju.tcmsearch.util.web.ParameterValueDecoder;

public class CnkiCotroller implements Controller {
	public static final String FieldValue="FieldValue";
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		
		String value = request.getParameter(FieldValue);
		System.out.println(value);
		//System.out.print(ParameterValueDecoder.guessEncoding(value));
		value = URLEncoder.encode(value,"utf-8");
		return new ModelAndView(new RedirectView(request.getContextPath()+"/exclude/cnkiQuery.htm?"+FieldValue+"="+value));
		//return null;
	}

}
