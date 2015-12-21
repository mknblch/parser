package de.mknblch.lfp;

import de.mknblch.lfp.ast.ASTParser;
import de.mknblch.lfp.ast.Node;
import de.mknblch.lfp.common.Table;
import de.mknblch.lfp.grammar.Grammar;
import de.mknblch.lfp.grammar.Rule;
import de.mknblch.lfp.lexer.Lexer;
import de.mknblch.lfp.lexer.SyntaxException;
import de.mknblch.lfp.lexer.Token;
import de.mknblch.lfp.parser.GrammarException;
import de.mknblch.lfp.parser.ll1.LL1ParseTableBuilder;
import de.mknblch.lfp.parser.ParseException;

import java.util.List;

/**
 * Created by mknblch on 21.12.2015.
 */
public class TestParser {

    private final Grammar grammar;
    private final Table<String, String, Rule> parseTable;
    private final Lexer lexer;
    private final ASTParser astParser;

    public TestParser(Grammar grammar) throws GrammarException {
        this.grammar = grammar;
        parseTable = new LL1ParseTableBuilder(grammar).build();
        astParser = new ASTParser(grammar, parseTable);
        lexer = new Lexer(grammar);

        System.out.println(grammar);
    }


    public Node parse(String input) throws SyntaxException, ParseException {

        final List<Token> tokens = lexer.tokenize(input);

        astParser.parse(tokens);

        return astParser.getAst();
    }
}
