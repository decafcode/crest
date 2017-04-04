package org.decafcode.crest.framework;

import java.net.URI;
import java.util.Map;
import javax.servlet.ServletException;

public interface SiteMap {
    ResourcePath readPath(String uriPath, Map<String, String> queryParams)
            throws ServletException;

    URI writePath(ResourcePath path);
}
