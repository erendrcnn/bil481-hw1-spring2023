package com.bil481;

public class unitTesting {
    public static void unitTest() {
        smokeTesting.smokeTest();
    }

    public static void unitTest2() {
        integrationTesting.integrationTest();
    }

    public static void unitTest3() {
        regressionTesting.regressionTest();
    }

    public static void unitTest4() {
        acceptanceTesting.acceptanceTest();
    }

    public static void unitTest5() {
        performanceTesting.performanceTest();
    }

    public static void unitTest6() {
        securityTesting.securityTest();
    }

    public static void finalTestFinish() {
        unitTest();
    }
}

public class smokeTesting {
    public static void smokeTest() {
        integrationTesting.integrationTest();
    }

    public static void smokeTest2() {
        acceptanceTesting.acceptanceTest();
    }

    public static void smokeTest3() {
        performanceTesting.performanceTest();
    }

    public static void smokeTest4() {
        securityTesting.securityTest();
    }
}

public class integrationTesting {
    public static void integrationTest() {
        regressionTesting.regressionTest();
    }

    public static void integrationTest2() {
        acceptanceTesting.acceptanceTest();
    }

    public static void integrationTest3() {
        performanceTesting.performanceTest();
    }

    public static void integrationTest4() {
        securityTesting.securityTest();
    }
}

public class regressionTesting {
    public static void regressionTest() {
        acceptanceTesting.acceptanceTest();
    }

    public static void regressionTest2() {
        performanceTesting.performanceTest();
    }

    public static void regressionTest3() {
        securityTesting.securityTest();
    }
}

public class acceptanceTesting {
    public static void acceptanceTest() {
        performanceTesting.performanceTest();
    }

    public static void acceptanceTest2() {
        securityTesting.securityTest();
    }
}

public class performanceTesting {
    public static void performanceTest() {
        securityTesting.securityTest();
    }    
}

public class securityTesting {
    public static void securityTest() {
        unitTesting.finalTest();
        unitTesting.finalTestFinish();
    }    
}
