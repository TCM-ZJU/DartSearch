package edu.zju.tcmsearch.web.controller.query;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import edu.zju.tcmsearch.util.GBK2Big5.GB2Big5;
import edu.zju.tcmsearch.util.web.MultiLinguaUtil;

public class ErrorInfoController implements Controller {

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		String errMsg= request.getParameter("errorMessage");
		Map model = new HashMap();
		model.put("errorInfoMsg", null==errMsg ? "内部错误":errMsg);
		
		String ViewName = "dartcore/errorInfo";
		model.put("GB2Big5",GB2Big5.getInstance());
		return new ModelAndView(ViewName,model);
	}

}
