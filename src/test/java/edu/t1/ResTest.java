package edu.t1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class ResTest {


    /**
     * "Test with null object parameters!"
     */
    @Test()
    public void testWithNullObject()
    {
        try {
            Res obj = new Res();
            Res.setDefaultValues(obj, null, null); // или нужные аргументы
        } catch (Exception e) {
        }
        Assertions.assertTrue(true);
    }


    /**
     * Test with using full functionality of reset, using test classes
     */
    @Test
    public void testListOfClassesWithDefault() {
        A a = new A();
        B b = new B();

        Res.setDefaultValues(a, null, null);
        Res.setDefaultValues(b, null, null);

        A expectedA = new A();
        expectedA.t = 1d;
        expectedA.str = "Test";

        B expectedB = new B();
        expectedB.t = 1d;
        expectedB.str = "Test";
        expectedB.b = true;

        Assertions.assertEquals(expectedA, a);
        Assertions.assertEquals(expectedB, b);
    }

    // Test with test class without default of annotation
    @Test()
    public void testListOfClassesWithoutDefault() {
        Object[] actual = new Object[]{new C()};
        Object[] expected = new Object[]{new C()};
        try {
            Res obj = new Res();
            Res.setDefaultValues(obj, null, null); // или нужные аргументы
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Assertions.assertArrayEquals(actual, expected);

    }
}

class Config{
    Boolean b =true;
    String str ="Test";
    Double t= 1d;
}

@Default(Config.class)
class A {
    Boolean b =true;
    Integer i =3;
    String str ="TT";
    Double t;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        A a = (A) o;
        return Objects.equals(i, a.i) && Objects.equals(b, a.b) && Objects.equals(t, a.t) && Objects.equals(str, a.str);
    }
    @Override
    public int hashCode() {
        return Objects.hash(i, b, t, str);
    }
    @Override
    public String toString() {
        return "edu.t1.A{" +
                "i=" + i +
                ", b=" + b +
                ", str='" + str + '\'' +
                ", t=" + t +
                '}';
    }
}

@Default(Config.class)
class B {
    Integer i = 5;
    Boolean b = false;
    Double t =7.9;
    String str ="RR";

    @Override
    public String toString() {
        return "edu.t1.B{" +
                "i=" + i +
                ", b=" + b +
                ", t=" + t +
                ", str='" + str + '\'' +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        B b1 = (B) o;
        return Objects.equals(i, b1.i) && Objects.equals(b, b1.b) && Objects.equals(t, b1.t) && Objects.equals(str, b1.str);
    }
    @Override
    public int hashCode() {
        return Objects.hash(i, b, t, str);
    }
}

class C {
    Integer i =3;
    Boolean b =true;
    String str ="TT";
    Double t;

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        C a = (C) o;
        return Objects.equals(i, a.i) && Objects.equals(b, a.b) && Objects.equals(t, a.t) && Objects.equals(str, a.str);
    }
    @Override
    public int hashCode() {
        return Objects.hash(i, b, t, str);
    }
    @Override
    public String toString() {
        return "edu.t1.C{" +
                "i=" + i +
                ", b=" + b +
                ", str='" + str + '\'' +
                ", t=" + t +
                '}';
    }
}