package edu.progmatic.messageapp.filters;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;
import java.util.Arrays;

@Component
class LoggerFilter implements Filter {

        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
                throws IOException, ServletException {

            servletRequest.getParameterMap().forEach((k, v) -> System.out.println(k + ": " + Arrays.toString(v)));

            //((HttpServletResponse) servletResponse).addHeader("MyHeader", "Header value");

            filterChain.doFilter(servletRequest, servletResponse);
        }


}
