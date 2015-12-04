package de.mknblch.lfp;

/**
 * Created by mknblch on 04.12.2015.
 */
public class UnknownTerm implements Term {
    private final String id;

    public UnknownTerm(String id) {
        this.id = id;
    }

    @Override
    public String getIdentifier() {
        return id;
    }
}
