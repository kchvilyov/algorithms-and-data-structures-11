import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.stream.Stream;

public class ResTest {

    /**
     * "Test with null object parameters!"
     */
    @Test()
    public void testWithNullObject()
    {
        try {
            Res obj = new Res();
            obj.reset(null);
        } catch (Exception e) {
        }
        Assertions.assertTrue(true);
    }


    /**
     * Test with using full functionality of reset, using test classes
     */
    @Test()
    public void testListOfClassesWithDefault() {

        A a = new A();
        B b = new B();

        A res_a = new A();
        res_a.t = 1d;
        res_a.str = "Test";

        B res_b = new B();
        res_b.b=true;
        res_b.t = 1d;
        res_b.str = "Test";

        Object[] actual = new Object[]{a, b};
        Object[] expected = new Object[]{res_a, res_b};
        try {
            Res obj = new Res();
            obj.reset(actual);
        } catch (Exception e) {
        }
        Assertions.assertArrayEquals(actual, expected);

    }
    // Test with test class without default of annotation
    @Test()
    public void testListOfClassesWithoutDefault() {
        Object[] actual = new Object[]{new C()};
        Object[] expected = new Object[]{new C()};
        try {
            Res obj = new Res();
            obj.reset(actual);
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