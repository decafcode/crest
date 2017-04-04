package org.decafcode.crest.sitemap;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.Objects;
import java.util.Optional;

final class PathNodeBranch implements PathNode {
    private final Optional<PathInstantiator> terminal;
    private final ImmutableMap<String, PathNode> nonterminals;

    PathNodeBranch(
            Optional<PathInstantiator> terminal,
            ImmutableMap<String, PathNode> nonterminals) {
        this.terminal = Objects.requireNonNull(terminal, "terminal");
        this.nonterminals = Objects.requireNonNull(nonterminals,
                "nonterminals");
    }

    @Override
    public Optional<PathBuilder> traverse(
            ImmutableList<String> tokens,
            int pos) {
        if (pos >= tokens.size()) {
            return terminal.map(PathBuilder::new);
        }

        String head = tokens.get(pos);
        Optional<PathNode> nonterminal
                = Optional.ofNullable(nonterminals.get(head));

        return nonterminal.flatMap(next -> next.traverse(tokens, pos + 1));
    }
}
