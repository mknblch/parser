package de.mknblch.lfp.parser;

import de.mknblch.lfp.common.Table;
import de.mknblch.lfp.grammar.Grammar;
import de.mknblch.lfp.grammar.GrammarLoader;
import de.mknblch.lfp.grammar.GrammarLoaderException;
import de.mknblch.lfp.grammar.Production;
import de.mknblch.lfp.parser.ll1.LL1ParseTableBuilder;
import de.mknblch.lfp.parser.ll1.Parser;

import java.nio.file.Path;

/**
 * @author mknblch
 */
public class ParserBuilder {

    private Grammar grammar;
    private Table<String, String, Production> parseTable;

    public ParserBuilder withResource(String resource) throws GrammarLoaderException, GrammarException {
        this.grammar = GrammarLoader.loadResource(resource);
        this.parseTable = parseTable();
        return this;
    }

    public ParserBuilder withPath(Path path) throws GrammarLoaderException, GrammarException {
        this.grammar = GrammarLoader.load(path);
        this.parseTable = parseTable();
        return this;
    }

    public Parser build() throws GrammarException {
        return new Parser(grammar, parseTable());
    }

    private Table<String, String, Production> parseTable() throws GrammarException {
        return new LL1ParseTableBuilder(grammar).build();
    }
}
