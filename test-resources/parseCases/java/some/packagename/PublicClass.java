package some.packagename;

import java.io.Serializable;

public class PublicClass implements Serializable {

    public void doSmth1() {
        System.out.println("DoSmth");
    }

    private void doSmth2() {
        System.out.println("DoSmth");
    }

    protected static void doSmth3() {
        System.out.println("DoSmth");
    }
}
