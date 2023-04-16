javac *.java
cat input/test.java | java CallGraphListener | dot -Tpng -o output/test.png
cat input/test1.java | java CallGraphListener | dot -Tpng -o output/test1.png
cat input/test2.java | java CallGraphListener | dot -Tpng -o output/test2.png
cat input/test3.java | java CallGraphListener | dot -Tpng -o output/test3.png