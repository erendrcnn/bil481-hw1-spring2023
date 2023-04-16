import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.net.SocketTimeoutException;
import java.util.*;

public class CallGraphListener extends Java8BaseListener {
    private static Set<String> declaredMethods = null;
    private static Set<String> allMethods = null;
    private Map<String, Set<String>> methodCalls = new HashMap<>();
    private Map<String, String> methodToClass = new HashMap<>();
    private Map<String, String> methodToPackage = new HashMap<>();
    private String currentMethod = null;
    private String currentClass = null;
    private String currentPackage = null;

    // Collect current package when entering the package declaration
    @Override
    public void enterPackageDeclaration(Java8Parser.PackageDeclarationContext ctx) {
        currentPackage = ctx.Identifier().stream().map(ParseTree::getText).reduce((a, b) -> a + "." + b).orElse("unknown");
    }

    // Collect current class when entering the class declaration
    @Override
    public void enterClassDeclaration(Java8Parser.ClassDeclarationContext ctx) {
        currentClass = ctx.normalClassDeclaration().Identifier().getText();
    }

    // Reset current class when exiting the class declaration
    @Override
    public void exitClassDeclaration(Java8Parser.ClassDeclarationContext ctx) {
        currentClass = null;
    }

    // Collect methods and their classes
    @Override
    public void enterMethodHeader(Java8Parser.MethodHeaderContext ctx) {
        String methodName = ctx.methodDeclarator().Identifier().getText();
        String method = currentPackage + "/" + currentClass + "/" + methodName;
        currentMethod = method;
        methodToClass.put(method, currentClass);
        methodToPackage.put(method, currentPackage);
    }

    // Collect method references
    // IGNORE THIS METHOD
    @Override
    public void enterMethodReference(Java8Parser.MethodReferenceContext ctx) {
        // System.out.println("Method reference: " + ctx.getText());
        String methodName = ctx.Identifier().getText();
        String method = currentPackage + "/" + currentClass + "/" + methodName;
        if (currentMethod != null) {
            methodCalls.computeIfAbsent(currentMethod, k -> new HashSet<>()).add(method);
        }
    }

    // This is for method calls in the same class
    @Override
    public void enterA(Java8Parser.AContext ctx) {
        String methodName = ctx.methodName().getText();
        String method = currentPackage + "/" + currentClass + "/" + methodName;
        // System.out.println("Method reference (S) : " + method);
        if (currentMethod != null) {
            methodCalls.computeIfAbsent(currentMethod, k -> new HashSet<>()).add(method);
        }
    }
    
    // This is for method calls in other classes
    @Override
    public void enterB(Java8Parser.BContext ctx) {
        String methodName = ctx.Identifier().getText();
        String currClass = ctx.typeName().getText();
        String method = currentPackage + "/" + currClass + "/" + methodName;
        // System.out.println("Method reference (D) : " + method);
        if (currentMethod != null) {
            methodCalls.computeIfAbsent(currentMethod, k -> new HashSet<>()).add(method);
        }
    }

    // Reset current method when exiting the method declaration
    @Override
    public void exitMethodDeclaration(Java8Parser.MethodDeclarationContext ctx) {
        currentMethod = null;
    }

    public static void main(String[] args) throws Exception {
        ANTLRInputStream input = new ANTLRInputStream(System.in);
        Java8Lexer lexer = new Java8Lexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Java8Parser parser = new Java8Parser(tokens);
        ParseTree tree = parser.compilationUnit();
        ParseTreeWalker walker = new ParseTreeWalker();
        CallGraphListener listener = new CallGraphListener();
        walker.walk(listener, tree);

        StringBuilder buf = new StringBuilder();
        buf.append("digraph G {\n");
        buf.append("node [shape=circle, style=filled];\n");
        
        // Collect declared methods and all methods
        declaredMethods = listener.methodToClass.keySet();
        allMethods = new HashSet<>(declaredMethods);
        listener.methodCalls.values().forEach(allMethods::addAll);

        // Add nodes to the graph
        for (String method : allMethods) {
            String nodeName = method;
            if (declaredMethods.contains(method)) {
                buf.append("\"").append(nodeName).append("\" [fillcolor=green];\n");
            } else {
                buf.append("\"").append(nodeName).append("\" [fillcolor=white];\n");
            }
        }

        // Add edges to the graph
        for (Map.Entry<String, Set<String>> entry : listener.methodCalls.entrySet()) {
            String caller = entry.getKey();
            for (String callee : entry.getValue()) {
                buf.append("\"").append(caller).append("\" -> \"").append(callee).append("\";\n");
            }
        }

        buf.append("}\n");
        System.out.println(buf.toString());
    }
}