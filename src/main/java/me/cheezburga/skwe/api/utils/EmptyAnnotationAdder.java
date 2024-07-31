package me.cheezburga.skwe.api.utils;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.printer.lexicalpreservation.LexicalPreservingPrinter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

public class EmptyAnnotationAdder {

    private static final Map<String, String> ANNOTATIONS_MAP = new LinkedHashMap<>();
    private static final Set<String> TARGET_CLASSES = Set.of("SkWEEffect", "SimpleExpression", "Expression", "PropertyExpression", "SimplePropertyExpression", "Condition", "PropertyCondition");
    private static final Set<String> PREFIXES = Set.of("Eff", "Expr", "Cond");

    static {
        ANNOTATIONS_MAP.put("Name", "");
        ANNOTATIONS_MAP.put("Description", "");
        ANNOTATIONS_MAP.put("Examples", "");
        ANNOTATIONS_MAP.put("Since", "");
        ANNOTATIONS_MAP.put("RequiredPlugins", "WorldEdit");
    }

    private static final String ANNOTATION_PACKAGE = "ch.njol.skript.doc";

    public static void main(String[] args) throws IOException {
        if (args.length < 1)
            System.exit(1);
        String sourceRoot = args[0];

        try (Stream<Path> paths = Files.walk(Paths.get(sourceRoot))) {
            paths.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(EmptyAnnotationAdder::processFile);
        }
    }

    private static void processFile(Path path) {
        try {
            JavaParser parser = new JavaParser();
            ParseResult<CompilationUnit> parseResult = parser.parse(path);
            Optional<CompilationUnit> optionalCU = parseResult.getResult();

            if (optionalCU.isEmpty())
                return;

            CompilationUnit cu = optionalCU.get();
            LexicalPreservingPrinter.setup(cu);
            boolean modified = false;

            for (ClassOrInterfaceDeclaration c : cu.findAll(ClassOrInterfaceDeclaration.class)) {
                if (c.getExtendedTypes().stream().map(NodeWithSimpleName::getNameAsString).anyMatch(TARGET_CLASSES::contains)
                        && PREFIXES.stream().anyMatch(prefix -> c.getNameAsString().startsWith(prefix))) {
                    if (addAnnotations(c)) {
                        modified = true;
                    }
                }
            }

            if (modified) {
                addImports(cu);
                Files.write(path, LexicalPreservingPrinter.print(cu).getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean addAnnotations(ClassOrInterfaceDeclaration c) {
        boolean modified = false;

        for (Map.Entry<String, String> entry : ANNOTATIONS_MAP.entrySet()) {
            String annotationClass = entry.getKey();
            String defaultValue = entry.getValue();
            if (addAnnotation(c, annotationClass, defaultValue)) {
                modified = true;
            }
        }

        return modified;
    }

    private static boolean addAnnotation(ClassOrInterfaceDeclaration c, String name, String defaultValue) {
        if (c.getAnnotations().stream().noneMatch(a -> a.getNameAsString().equals(name))) {
            SingleMemberAnnotationExpr annotation = new SingleMemberAnnotationExpr();
            annotation.setName(name);
            annotation.setMemberValue(new StringLiteralExpr(defaultValue));
            c.addAnnotation(annotation);
            return true;
        }
        return false;
    }

    private static void addImports(CompilationUnit cu) {
        Set<String> existingImports = new TreeSet<>(Comparator.naturalOrder());

        for (ImportDeclaration importDecl : cu.getImports()) {
            existingImports.add(importDecl.getNameAsString());
        }

        List<ImportDeclaration> importsToAdd = new ArrayList<>();
        for (String annotationClass : ANNOTATIONS_MAP.keySet()) {
            String fullAnnotationName = ANNOTATION_PACKAGE + "." + annotationClass;
            if (!existingImports.contains(fullAnnotationName)) {
                importsToAdd.add(new ImportDeclaration(fullAnnotationName, false, false));
            }
        }

        if (!importsToAdd.isEmpty()) {
            existingImports.addAll(importsToAdd.stream().map(ImportDeclaration::getNameAsString).toList());
            List<ImportDeclaration> sortedImports = existingImports.stream()
                    .map(name -> new ImportDeclaration(name, false, false))
                    .toList();
            NodeList<ImportDeclaration> sortedImportsNodeList = new NodeList<>(sortedImports);
            cu.setImports(sortedImportsNodeList);
        }
    }
}