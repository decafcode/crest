package org.decafcode.crest.sitemap;

import org.decafcode.crest.framework.ResourcePath;
import org.decafcode.crest.framework.SiteMap;
import java.net.URI;
import java.util.Collection;
import java.util.function.Function;

public final class SiteMapBuilder {
    private final ForwardSiteMapBuilder fwdBuilder
            = new ForwardSiteMapBuilder();

    private final ReverseSiteMapBuilder revBuilder
            = new ReverseSiteMapBuilder();

    public void rootUri(URI rootUri) {
        revBuilder.rootUri(rootUri);
    }

    public <T> void addReader(Class<T> paramType, Function<String, T> reader) {
        fwdBuilder.addReader(paramType, reader);
    }

    public void add(Class<? extends ResourcePath> pathClass) {
        fwdBuilder.addPathClass(pathClass);
        revBuilder.addPathClass(pathClass);
    }

    public void addAll(Collection<Class<? extends ResourcePath>> values) {
        values.forEach(this::add);
    }

    public SiteMap build() {
        return new SiteMapImpl(fwdBuilder.build(), revBuilder.build());
    }
}
