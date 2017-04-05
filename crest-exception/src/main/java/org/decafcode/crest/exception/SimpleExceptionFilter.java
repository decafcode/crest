package org.decafcode.crest.exception;

import java.io.IOException;
import javax.inject.Singleton;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

@Singleton
public class SimpleExceptionFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(
            ServletRequest req_,
            ServletResponse resp_,
            FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(req_, resp_);
        } catch (HttpStatusException e) {
            HttpServletResponse resp = (HttpServletResponse) resp_;

            e.extraHeaders().entrySet().forEach(kvp -> resp.setHeader(
                    kvp.getKey(),
                    kvp.getValue()));
            resp.setStatus(e.statusCode());
            resp.setContentType("text/plain;charset=utf-8");
            resp.getWriter().print(e.getMessage());
        }
    }

    @Override
    public void destroy() {
    }
}
