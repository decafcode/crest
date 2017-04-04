package org.decafcode.crest.sitemap;

import org.decafcode.crest.framework.ResourcePath;

interface PathSegment {
    String emit(ResourcePath path);
}
