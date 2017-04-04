package org.decafcode.crest.sitemap;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

final class PathNodeBuilderBranchState implements PathNodeBuilderState {
    private final Map<String, PathNodeBuilder> branches = new HashMap<>();

    @Override
    public PathNodeBuilder addBranch(String branch) {
        PathNodeBuilder result = branches.get(branch);

        if (result != null) {
            return result;
        }

        result = new PathNodeBuilder();
        branches.put(branch, result);

        return result;
    }

    @Override
    public PathNodeBuilder addCapture(String value) {
        throw new RouteConflictException();
    }

    @Override
    public PathNodeBuilder addSlash() {
        throw new RouteConflictException();
    }

    @Override
    public PathNode build(Optional<PathInstantiator> terminal) {
        ImmutableMap.Builder<String, PathNode> mb = ImmutableMap.builder();

        for (Map.Entry<String, PathNodeBuilder> kvp : branches.entrySet()) {
            mb.put(kvp.getKey(), kvp.getValue().build());
        }

        return new PathNodeBranch(terminal, mb.build());
    }
}
