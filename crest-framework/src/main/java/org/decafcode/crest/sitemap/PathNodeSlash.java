package org.decafcode.crest.sitemap;

import com.google.common.collect.ImmutableList;
import java.util.Objects;
import java.util.Optional;

final class PathNodeSlash implements PathNode {
    private final Optional<PathInstantiator> terminal;
    private final Optional<PathNode> nonterminal;

    PathNodeSlash(
            Optional<PathInstantiator> terminal,
            Optional<PathNode> nonterminal) {
        this.terminal = Objects.requireNonNull(terminal, "terminal");
        this.nonterminal = Objects.requireNonNull(nonterminal, "nonterminal");
    }

    @Override
    public Optional<PathBuilder> traverse(
            ImmutableList<String> tokens,
            int pos) {
        if (pos >= tokens.size()) {
            return terminal.map(PathBuilder::new);
        }

        String head = tokens.get(pos);

        if (!head.equals("/")) {
            throw new IllegalStateException();
        }

        return nonterminal.flatMap(next -> next.traverse(tokens, pos + 1));
    }
}
