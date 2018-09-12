package demo.methods;

import java.io.IOException;

public class ClassWithMethods {

    //Signatures of these methods will be changed
    public void signatureChanged_1() {

    }

    public final void signatureChanged_2() throws IOException {
    }

    //Removed method
    public void removed_1() {
        System.out.println("Hello");// add some code in order to be different with new method
    }
    // Mathods with different arguments are always detected as different. (May be improved in future)
    public void removed_2(int i) {
    }

    //Renamed methods
    public void renameFrom_1() {
        for (int i = 0; i < 10; i++) {
            System.out.println(i);
        }
    }
    public void renameFrom_2() {
        for (int i = 0; i < 11; i++) {
            System.out.println(i);
        }
    }

    // rename should not be detected: different internal code
    public void renameFrom_3() {
        int i = 0;
        i++;
        for (i = 0; i < 11; i++) {
            System.out.println(i);
        }
    }


}
