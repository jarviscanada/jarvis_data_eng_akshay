package ca.jrvs.apps.practice;

import static org.junit.jupiter.api.Assertions.*;

class RegexExcImpTest {

    @org.junit.jupiter.api.Test
    void matchJpeg() {
        RegexExcImp imp = new RegexExcImp();
        System.out.println(imp.matchJpeg("hello.jpeg"));
        assertTrue(imp.matchJpeg("hello.jpeg"));
    }

    @org.junit.jupiter.api.Test
    void matchIp() {
    }

    @org.junit.jupiter.api.Test
    void isEmptyLine() {
    }
}