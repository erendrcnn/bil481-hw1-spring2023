package input;

import java.util.ArrayList;
import java.util.List;

public class test1 {
    static void main(String[] args) {
        m1();
    }

    static void m1() {
        m2();
        test2.m2();
    }
}

public class test2 {
    static void m2() {
        test3.m3();
    }
}

public class test3 {
    static void m3() {
        test4.m4();
    }
}

public class test4 {
    static void m4() {
        test5.m5();
    }
}

public class test5 {
    static void m5() {
        test6.m6();
        test6.m7();
    }
}

public class test6 {
    static void m6() {
        test1.m1();
    }

    static void m9() {
        test1.m1();
        test6.m9();
    }
}
