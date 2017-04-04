package org.decafcode.crest.sitemap;

import java.util.Optional;

final class PathNodeBuilder {
    private Optional<PathInstantiator> terminal = Optional.empty();
    private Optional<PathNodeBuilderState> state = Optional.empty();

    PathNodeBuilder addBranch(String branch) {
        if (!state.isPresent()) {
            state = Optional.of(new PathNodeBuilderBranchState());
        }

        return state.get().addBranch(branch);
    }

    PathNodeBuilder addCapture(String value) {
        if (!state.isPresent()) {
            state = Optional.of(new PathNodeBuilderCaptureState(value));
        }

        return state.get().addCapture(value);
    }

    PathNodeBuilder addSlash() {
        if (!state.isPresent()) {
            state = Optional.of(new PathNodeBuilderSlashState());
        }

        return state.get().addSlash();
    }

    void addTerminal(PathInstantiator value) {
        if (terminal.isPresent()) {
            throw new RouteConflictException();
        }

        terminal = Optional.of(value);
    }

    PathNode build() {
        return state.orElseGet(PathNodeBuilderSlashState::new).build(terminal);
    }
}
