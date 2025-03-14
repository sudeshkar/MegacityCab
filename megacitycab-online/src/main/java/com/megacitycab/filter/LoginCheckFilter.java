package com.megacitycab.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.megacitycab.utils.SessionUtils;


public class LoginCheckFilter extends HttpFilter implements Filter {
	private static final long serialVersionUID = 1L;
	private String loginPage = "/index.jsp";
 
	@Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String configuredLoginPage = filterConfig.getInitParameter("loginPage");
        if (configuredLoginPage != null) {
            loginPage = configuredLoginPage;
        }
    }
	@Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();

         
        if (requestURI.endsWith(loginPage) || requestURI.contains("/index.jsp")) {
            chain.doFilter(request, response);
            return;
        }

         
        if (!SessionUtils.isUserLoggedIn(httpRequest)) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + loginPage);
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
         
    }

 

}
