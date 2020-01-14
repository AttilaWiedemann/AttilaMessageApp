package edu.progmatic.messageapp.filters;


import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@Component
public class LogFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //servletRequest.getParameterMap().values().forEach(x -> System.out.println(Arrays.toString(x)));

        //servletRequest.getParameterMap().forEach((k, v) -> System.out.println(k + ": " + Arrays.toString(v))); Jana-féle megoldás

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
