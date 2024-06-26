package com.gepardec.wor.lord.util;

import org.openrewrite.Parser;
import org.openrewrite.java.JavaParser;

import java.nio.file.Path;
import java.util.List;

public class ParserUtil {

    public static JavaParser.Builder<?, ?> createParserWithRuntimeClasspath() {
        List<Path> classpath = JavaParser.runtimeClasspath();
        return JavaParser
                .fromJavaVersion()
                .classpath(classpath);
    }
}
