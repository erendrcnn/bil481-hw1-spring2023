# A basic soruce code graph generator. 

This is a Java program that listens to a Java file's Abstract Syntax Tree (AST) and constructs a call graph of the methods in that file.

The program uses the ANTLRv4 parser generator to parse the Java file and generate the AST. It then walks the tree and listens for certain events in the tree traversal, such as entering or exiting a package declaration, class declaration, method declaration, and method reference.

For each method declaration, the program constructs a unique identifier for the method based on its package, class, and name. It also tracks the package and class of the current method being traversed.

When the program encounters a method reference, it constructs a unique identifier for the method being called based on the package, class, and name of the method. It then stores the relationship between the current method being traversed and the called method in a Map.

The program also collects the set of declared methods and all methods in the Java file. It then constructs a directed graph using the Graphviz language, where nodes represent methods and edges represent method calls. Declared methods are colored in green and all other methods are colored in white.

The resulting call graph is printed to standard output in the Graphviz language, which can be used to generate a visual representation of the call graph using the Graphviz software.

Note: The way the source code is translated into dot language takes place according to the order of the methods called in the given input.

# Example run:

```
javac *.java
cat input/test.java  | java CallGraphListener | dot -Tpng -o output/test.png
```

The output is:

<img src="https://user-images.githubusercontent.com/70805475/232507319-03f4c7e8-6a7a-4e12-86ed-67f49c4ddd13.png" height="500">

The numbers above correspond to the distance of each method name to the given word. 3 suggestions are returned as requested.


# Example run2:

```
javac *.java
cat input/test1.java | java CallGraphListener | dot -Tpng -o output/test1.png
```

The output is:

<img src="https://user-images.githubusercontent.com/70805475/232507354-289a46a9-b473-41c8-ac2d-8f8762c81bc8.png" height="500">

# Example run3:

```
javac *.java
cat input/test2.java | java CallGraphListener | dot -Tpng -o output/test2.png
```

The output is:

<img src="https://user-images.githubusercontent.com/70805475/232507382-4e3b67f9-2d53-43c9-9937-a1f873fea9e3.png" height="750">

# Example run4:

```
javac *.java
cat input/test3.java | java CallGraphListener | dot -Tpng -o output/test3.png
```

The output is:

<img src="https://user-images.githubusercontent.com/70805475/232507410-0d47433c-babf-449b-b7df-57f3df6e4c50.png" height="1000">

# Details
You can also use the methods in this way to print them as text:

Input:
```
cat input/test.java | java CallGraphListener
```

Output:
```
digraph G {
node [shape=circle, style=filled];
"com.acme/A/m1" [fillcolor=green];
"com.acme/A/m2" [fillcolor=green];
"com.acme/B/m1" [fillcolor=green];
"com.acme/B/m2" [fillcolor=green];
"com.acme/C/m1" [fillcolor=green];
"com.acme/C/m3" [fillcolor=white];
"com.acme/A/m1" -> "com.acme/B/m2";
"com.acme/A/m1" -> "com.acme/A/m2";
"com.acme/A/m2" -> "com.acme/B/m1";
"com.acme/B/m1" -> "com.acme/A/m1";
"com.acme/B/m2" -> "com.acme/C/m3";
}
```

(An alternative output control)
Using this output, a visual graph can be obtained from the http://www.webgraphviz.com/ website.
