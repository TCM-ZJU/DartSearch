/*
 * Created on 2005-12-20
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StopWatch;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class TimeStatInterceptor implements HandlerInterceptor{
    ThreadLocal swThreadLocal=new ThreadLocal();

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        StopWatch stopWatch=new StopWatch();
        stopWatch.start("index searching");
        swThreadLocal.set(stopWatch);
        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        StopWatch stopWatch=(StopWatch)swThreadLocal.get();
        stopWatch.stop();
        modelAndView.getModel().put("stopWatch",stopWatch);
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // TODO Auto-generated method stub
        
    }


}
