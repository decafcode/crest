package org.decafcode.crest.sitemap;

import org.decafcode.crest.framework.ResourcePath;
import org.decafcode.crest.framework.SiteMap;
import java.net.URI;
import java.util.Map;
import java.util.Objects;
import javax.servlet.ServletException;

final class SiteMapImpl implements SiteMap {
    private final ForwardSiteMap fwd;
    private final ReverseSiteMap rev;

    SiteMapImpl(ForwardSiteMap fwd, ReverseSiteMap rev) {
        this.fwd = Objects.requireNonNull(fwd, "fwd");
        this.rev = Objects.requireNonNull(rev, "rev");
    }

    @Override
    public ResourcePath readPath(
            String uriPath,
            Map<String, String> queryParams) throws ServletException {
        return fwd.readPath(uriPath, queryParams);
    }

    @Override
    public URI writePath(ResourcePath path) {
        return rev.write(path);
    }
}
