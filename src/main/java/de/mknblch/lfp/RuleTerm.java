package de.mknblch.lfp;

import java.util.HashSet;

/**
 * Created by mknblch on 04.12.2015.
 */
public class RuleTerm implements Term {

    private final String id;
    private final HashSet<Term> rules = new HashSet<Term>();

    public RuleTerm(String id) {
        this.id = id;
    }

    public HashSet<Term> getRules() {
        return rules;
    }

    @Override
    public String getIdentifier() {
        return id;
    }
}
