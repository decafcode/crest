package org.decafcode.crest.sitemap;

import com.google.common.collect.ImmutableMap;
import org.decafcode.crest.framework.ResourcePath;
import javax.servlet.ServletException;

interface PathInstantiator {
    ResourcePath create(
            ImmutableMap<String, String> pathParams,
            ImmutableMap<String, String> queryParams)
                    throws ReflectiveOperationException, ServletException;
}
