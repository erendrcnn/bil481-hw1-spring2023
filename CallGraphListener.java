import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.util.*;
// METIN EREN DURUCAN - 201101038 - BIL 481 - HOMEWORK 1
public class CallGraphListener extends Java8BaseListener {
    // This methods are declared as a set to avoid duplicates in the graph.
    private static Set<String> declaredMethods = null;                          // declared methods
    private static Set<String> allMethods = null;                               // declared + referenced
    // This methods are declared as a map to link the methods to their classes and packages.
    private Map<String, Set<String>> methodCalls = new LinkedHashMap<>();       // caller -> callees
    private Map<String, String> methodToClass = new LinkedHashMap<>();          // method -> class
    private Map<String, String> methodToPackage = new LinkedHashMap<>();        // method -> package
    // These variables are used to collect the current method, class and package.
    private String currentMethod = null;                                        // current method
    private String currentClass = null;                                         // current class
    private String currentPackage = "";                                         // current package

    // Collect current package when entering the package declaration
    @Override
    public void enterPackageDeclaration(Java8Parser.PackageDeclarationContext ctx) {
        if (ctx.Identifier() == null) // no package (default package)
            return; 

        currentPackage = ctx.Identifier().stream().map(ParseTree::getText).reduce((a, b) -> a + "." + b).orElse("unknown");
        // identifier() is a list of identifiers
        // stream() converts the list to a stream
        // map() applies the getText() method to each element of the stream
        // reduce() combines the elements of the stream into a single string
        // orElse() returns "unknown" if the stream is empty
    }

    // Collect current class when entering the class declaration
    @Override
    public void enterClassDeclaration(Java8Parser.ClassDeclarationContext ctx) {
        currentClass = ctx.normalClassDeclaration().Identifier().getText();
        // normalClassDeclaration() is a class declaration without modifiers
        // Identifier() is the name of the class
        // getText() returns the text of the identifier
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
        // methodDeclarator() is the name of the method
        // Identifier() is the name of the method
        // getText() returns the text of the identifier
        String method = currentPackage + "/" + currentClass + "/" + methodName;
        currentMethod = method;                         // set current method
        methodToClass.put(method, currentClass);        // add method to class
        methodToPackage.put(method, currentPackage);    // add method to package
    }

    // Collect method references
    // {IGNORE THIS METHOD}
    // This method does not work for method calls in the same class
    // It is only for reference
    /* 
    @Override
    public void enterMethodReference(Java8Parser.MethodReferenceContext ctx) {
        // System.out.println("Method reference: " + ctx.getText());
        String methodName = ctx.Identifier().getText();
        String method = currentPackage + "/" + currentClass + "/" + methodName;
        if (currentMethod != null) {
            methodCalls.computeIfAbsent(currentMethod, k -> new HashSet<>()).add(method);
        }
    }
    */

    // This is for method calls in the same class
    @Override
    public void enterA(Java8Parser.AContext ctx) {
        String methodName = ctx.methodName().getText();
        String method = currentPackage + "/" + currentClass + "/" + methodName;
        // System.out.println("Method reference (S) : " + method);
        if (currentMethod != null) {
            methodCalls.computeIfAbsent(currentMethod, k -> new HashSet<>()).add(method);
            // computeIfAbsent() is used to avoid null pointer exceptions
            // If the key is not present, it is added to the map with the value returned by the lambda expression
            // If the key is present, the value is not changed
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
        ANTLRInputStream input = new ANTLRInputStream(System.in);   // read from standard input
        Java8Lexer lexer = new Java8Lexer(input);                   // create a lexer that feeds off of input CharStream
        CommonTokenStream tokens = new CommonTokenStream(lexer);    // create a buffer of tokens pulled from the lexer
        Java8Parser parser = new Java8Parser(tokens);               // create a parser that feeds off the tokens buffer
        ParseTree tree = parser.compilationUnit();                  // begin parsing at compilationUnit rule
        ParseTreeWalker walker = new ParseTreeWalker();             // create standard walker
        CallGraphListener listener = new CallGraphListener();       // create listener
        walker.walk(listener, tree);                                // initiate walk of tree with listener

        StringBuilder buf = new StringBuilder();
        buf.append("digraph G {\n");
        buf.append("node [shape=circle, style=filled];\n");     // set node style
        
        // Collect declared methods and all methods
        declaredMethods = listener.methodToClass.keySet();          // collect all declared methods
        allMethods = new LinkedHashSet<>(declaredMethods);          // create a new set with the declared methods
        listener.methodCalls.values().forEach(allMethods::addAll);  // add all methods called by declared methods

        // Add nodes to the graph
        for (String method : allMethods) {
            String nodeName = method;
            if (declaredMethods.contains(method)) {                 // if the method is declared
                buf.append("\"").append(nodeName).append("\" [fillcolor=green];\n");
            } else {                                                // if the method is not declared
                buf.append("\"").append(nodeName).append("\" [fillcolor=white];\n");
            }
        }

        // Add edges to the graph
        for (Map.Entry<String, Set<String>> entry : listener.methodCalls.entrySet()) {
            String caller = entry.getKey();                        // get the caller method
            for (String callee : entry.getValue()) {               // for each method called by the caller
                buf.append("\"").append(caller).append("\" -> \"").append(callee).append("\";\n");
            }
        }

        buf.append("}\n");
        System.out.println(buf.toString());                         // print the graph
    }
}