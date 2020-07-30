package cn.stevekung.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter(filterName = "ayUserFilter", urlPatterns="/*")
public class AyUserFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(AyUserFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("====filter=======init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("====filter=======doFilter");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        log.info("====filter=======destroy");
    }
}
