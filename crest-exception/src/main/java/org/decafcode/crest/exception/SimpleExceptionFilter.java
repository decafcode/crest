package org.decafcode.crest.exception;

import java.io.IOException;
import javax.inject.Singleton;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

@Singleton
public class SimpleExceptionFilter implements Filter {
    private ServletContext ctx;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ctx = filterConfig.getServletContext();
    }

    @Override
    public void doFilter(
            ServletRequest req_,
            ServletResponse resp_,
            FilterChain chain) throws IOException, ServletException {
        HttpServletResponse resp = (HttpServletResponse) resp_;

        try {
            chain.doFilter(req_, resp_);
        } catch (HttpStatusException e) {
            e.extraHeaders().entrySet().forEach(kvp -> resp.setHeader(
                    kvp.getKey(),
                    kvp.getValue()));
            resp.setStatus(e.statusCode());
            resp.setContentType("text/plain;charset=utf-8");
            resp.getWriter().print(e.getMessage());
        } catch (Throwable t) {
            ctx.log("Uncaught exception", t);

            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.setContentType("text/plain;charset=utf-8");
            resp.getWriter().print("Internal Server Error");
        }
    }

    @Override
    public void destroy() {
    }
}
