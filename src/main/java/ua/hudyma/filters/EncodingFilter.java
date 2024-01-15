package ua.hudyma.filters;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
/** Filter for proper jsp-pages encoding output
 * @author yurko hudyma
 * 
 */

@WebFilter(filterName = "EncodingFilter")
public class EncodingFilter implements Filter {
    public static final Logger LOG= Logger.getLogger(EncodingFilter.class);

    private String encoding = "UTF-8";

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws ServletException, IOException {
        req.setCharacterEncoding(encoding);
        chain.doFilter(req, resp);
        HttpServletRequest request = (HttpServletRequest) req;
        String URI = request.getRequestURI();
        LOG.info(URI);

    }
    @Override
    public void init(FilterConfig config) throws ServletException {
        String encodingParam = config.getInitParameter("encoding");
        if (encodingParam != null) {
            encoding = encodingParam;
        }
    }
}
