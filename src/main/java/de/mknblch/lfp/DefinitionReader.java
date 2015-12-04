package de.mknblch.lfp;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mknblch on 04.12.2015.
 */
public class DefinitionReader {

    public static final String REGEX_PREFIX = "%";

    private final Pattern LINEPATTERN = Pattern.compile("(([!:%])\\w+)\\s*=\\s*(.+)");


    private final InputStream iStream;
    private final Scanner scanner;
    private final Map<String, Term> terms = new HashMap<>();


    public DefinitionReader(InputStream iStream) {
        this.iStream = iStream;
        scanner = new Scanner(iStream);
        scanner.useDelimiter(Pattern.compile("(\r?\n)+"));
    }

    public void preProcess() {

        while (scanner.hasNext()) {

            final String line = scanner.next();
            final Matcher matcher = LINEPATTERN.matcher(line);

            if (!matcher.matches()) {
                continue;
            }

            switch (matcher.group(2)) {
                case "%":
                    terms.put(
                            matcher.group(1),
                            new RegexTerm(
                                    matcher.group(1),
                                    Pattern.compile(matcher.group(2))));
                    break;

                case ":":
//                case "!":
            }

        }


    }




    public static DefinitionReader load(Path path) throws IOException {
        final InputStream iStream = Files.newInputStream(path, StandardOpenOption.READ);
        return new DefinitionReader(iStream);
    }

    public static DefinitionReader loadResource(String resource) {
        final InputStream iStream = Thread
                .currentThread()
                .getContextClassLoader()
                .getResourceAsStream(resource);

        return new DefinitionReader(iStream);
    }
}
