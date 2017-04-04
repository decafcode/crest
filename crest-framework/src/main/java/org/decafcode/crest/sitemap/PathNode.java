package org.decafcode.crest.sitemap;

import com.google.common.collect.ImmutableList;
import java.util.Optional;

interface PathNode {
    Optional<PathBuilder> traverse(ImmutableList<String> tokens, int pos);
}
