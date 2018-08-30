package some.packagename;

import java.io.*;
import java.util.*;

public abstract class ClassWithMethods {

    public static void staticMethod() {

    }

    public static final void staticFinalMethod() {

    }

    public abstract void abstractMethod();

    public final void finalMethod() {

    }

    public String methodWithArguments(int a, Object b, String c, Integer d, List<String> e) throws IOException, RuntimeException {
        return null;
    }

    public String[] methodWithArrayArguments(int[] a, Object[] b, String[] c, Integer[] d, List<String> e) throws IOException, RuntimeException {
        return null;
    }


}
