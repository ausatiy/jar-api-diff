package demo.methods;

import java.io.IOException;

public class ClassWithMethods {

    //Signatures of these methods have changed
    public String signatureChanged_1() {
        return "";
    }

    protected final void signatureChanged_2() {
    }

    //Added method
    public void added() {
        System.out.println("Hello2");// add some code in order to be different with new method
    }
    // Methods with different arguments are always detected as different. (May be improved in future)
    public void removed_2(byte i) {
    }

    //Renamed methods
    //These methods should be mapped properly
    // renamedFrom_1 -> renamedTo_2
    // renamedFrom_2 -> renamedTo_1
    // because byte-code is different
    public void renameTo_2() {
        for (int i = 0; i < 10; i++) {
            System.out.println(i);
        }
    }
    public void renameTo_1() {
        for (int i = 0; i < 11; i++) {
            System.out.println(i);
        }
    }

    // rename should not be detected: different internal code
    public void renameTo_3() {
        int i = 0;
        i++;
        System.out.println(i);
    }
}
