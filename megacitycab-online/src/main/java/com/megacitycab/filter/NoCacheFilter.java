package com.megacitycab.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletResponse;


public class NoCacheFilter extends HttpFilter implements Filter {
	private static final long serialVersionUID = 1L;


	public void destroy() {
	 
	}
 
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        httpResponse.setHeader("Pragma", "no-cache");  
        httpResponse.setDateHeader("Expires", 0);  
        chain.doFilter(request, response);
	}

	 
	public void init(FilterConfig fConfig) throws ServletException {
		 
	}

}
