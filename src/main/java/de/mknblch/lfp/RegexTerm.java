package de.mknblch.lfp;

import java.util.regex.Pattern;

/**
 * Created by mknblch on 04.12.2015.
 */
public class RegexTerm implements Term {

    public final String id;
    public final Pattern regex;

    public RegexTerm(String id, Pattern regex) {
        this.id = id;
        this.regex = regex;
    }

    @Override
    public String getIdentifier() {
        return id;
    }
}
