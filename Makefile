
run: clean
	javac *.java
	cat input/test.java | java CallGraphListener | dot -Tpng -o output/test.png

run1: clean
	javac *.java
	cat input/test1.java | java CallGraphListener | dot -Tpng -o output/test1.png

run2: clean
	javac *.java
	cat input/test2.java | java CallGraphListener | dot -Tpng -o output/test2.png

run3: clean
	javac *.java
	cat input/test3.java | java CallGraphListener | dot -Tpng -o output/test3.png

gui: clean
	javac *.java
	cat input/test.java | java org.antlr.v4.gui.TestRig Java8 compilationUnit -gui

tree: clean
	javac *.java
	cat input/test.java | java org.antlr.v4.gui.TestRig Java8 compilationUnit -tree

clean:
	rm -f *.class