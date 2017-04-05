package org.decafcode.crest.framework;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Singleton
public class DispatchServlet extends HttpServlet {
    private final RequestReader reader;
    private final ResponseWriter writer;
    private final SiteMap siteMap;
    private final HandlerProvider provider;

    @Inject
    DispatchServlet(
            RequestReader reader,
            ResponseWriter writer,
            SiteMap siteMap,
            HandlerProvider provider) {
        this.reader = Objects.requireNonNull(reader, "reader");
        this.writer = Objects.requireNonNull(writer, "writer");
        this.siteMap = Objects.requireNonNull(siteMap, "siteMap");
        this.provider = Objects.requireNonNull(provider, "provider");
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void service(
            HttpServletRequest httpReq,
            HttpServletResponse httpResp)
                    throws ServletException, IOException {
        String uri = httpReq.getRequestURI();

        Enumeration<String> paramIter = httpReq.getParameterNames();
        Map<String, String> paramMap = new HashMap<>();

        while (paramIter.hasMoreElements()) {
            String key = paramIter.nextElement();
            String val = httpReq.getParameter(key);

            paramMap.put(key, val);
        }

        ResourcePath path = siteMap.readPath(uri, paramMap);
        Class<? extends ResourcePath> pathClass = path.pathClass();
        Request req = reader.read(httpReq);
        Response resp = null;

        // Erase the XHandler<?> generics. Little bit of sleight-of-hand
        // necessary to force the type system to work.

        switch (httpReq.getMethod()) {
        case "OPTIONS":
            resp = options(pathClass);

            break;

        case "GET":
            GetHandler getHandler = provider.getGetHandler(pathClass);

            if (getHandler != null) {
                resp = getHandler.get(path, req);
            }

            break;

        case "PUT":
            PutHandler putHandler = provider.getPutHandler(pathClass);

            if (putHandler != null) {
                resp = putHandler.put(path, req);
            }

            break;

        case "POST":
            PostHandler postHandler = provider.getPostHandler(pathClass);

            if (postHandler != null) {
                resp = postHandler.post(path, req);
            }

            break;

        case "DELETE":
            DeleteHandler deleteHandler = provider.getDeleteHandler(pathClass);

            if (deleteHandler != null) {
                resp = deleteHandler.delete(path, req);
            }

            break;
        }

        if (resp != null) {
            writer.writeResponse(resp, httpResp);
        } else {
            httpResp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }

    private OptionsResponse options(Class<? extends ResourcePath> pathClass) {
        Set<String> methods = new HashSet<>();

        methods.add("OPTIONS");

        if (provider.hasGetHandler(pathClass)) {
            methods.add("GET");
        }

        if (provider.hasPutHandler(pathClass)) {
            methods.add("PUT");
        }

        if (provider.hasPostHandler(pathClass)) {
            methods.add("POST");
        }

        if (provider.hasDeleteHandler(pathClass)) {
            methods.add("DELETE");
        }

        return OptionsResponse.builder()
                .addMethods(methods)
                .build();
    }
}
