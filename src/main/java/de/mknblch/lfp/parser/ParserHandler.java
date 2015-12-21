package de.mknblch.lfp.parser;

import de.mknblch.lfp.common.Table;
import de.mknblch.lfp.grammar.Grammar;
import de.mknblch.lfp.grammar.Rule;
import de.mknblch.lfp.lexer.SyntaxException;
import de.mknblch.lfp.lexer.Token;

import java.util.List;

/**
 * Created by mknblch on 21.12.2015.
 */
public interface ParserHandler {

    void onInitialize();

    void onToken(Token token);

    void onRule(Rule rule);

    void onDone();
}
