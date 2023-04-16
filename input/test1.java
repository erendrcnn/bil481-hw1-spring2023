package com.mycode;

public class A {
    static void m1() {
        m2();
        B.m2();
    }

    static void m2() {
        B.m1();
    }
}

class B {
    static void m1() {
        A.m1();
    }
    static void m2() {
        C.m3();
    }
}

class C {
    static void m1() {}
}

class D {
    static void m3() {
        C.m3();
    }
}

class E {
    static void m4() {
        C.m1();
    }

    static void m5() {
        A.m1();
    }
}

class F {
    static void m6() {
        A.m1();
    }

    static void m7() {
        A.m2();
    }

    static void m8() {
        B.m1();
    }
}