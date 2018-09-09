package some.packagename;

public class ClassWithInstructions {

    public void doSmth_01() {
        int i = 0;
        i++;
        // test simple invokations
        System.out.println("Hello, world!" + i);
    }

    public void doSmth_02() {
        int i = 0;
        // some comment
        i++;
        // another comment in order to increase line numbers count
        System.out.println("Hello, world!" + i);
    }

    public void doSmth_03() {
        int i = 0;
        i+=2;
        System.out.println("Hello, world!" + i);
    }

    public void doSmth_04() {
        int i = 0;
        i++;
        System.out.println("Hello, world1" + i);
    }

    public void doSmth_05() {
        //test array instantiations
        byte[][] b = new byte[10][10];
        // test jumps
        for (int i = 0; i < 10; i++) {
            System.out.println("x");
        }
    }

    public void doSmth_06() {
        byte[][] b = new byte[10][10];
        for (int i = 0; i < 10; i++) {
            System.out.println("x");
        }
    }

    public void doSmth_07() {
        int[][] b = new int[10][10];
        for (int i = 0; i < 10; i++) {
            System.out.println("x");
        }
    }

    public void doSmth_08(Runnable r1, Runnable r2, int... args) {
        //tableswitch
        switch (args[0]) {
            case 1: r1.run();
            break;
            case 2: r2.run();
            break;
        }
    }

    public void doSmth_09(Runnable r1, Runnable r2, int... args) {
        //tableswitch
        switch (args[0]) {
            case 1: r1.run();
                break;
            case 2: r2.run();
                break;
        }
    }

    public void doSmth_10(Runnable r1, Runnable r2, int... args) {
        //tableswitch
        switch (args[0]) {
            case 2: r2.run();
                break;
            case 1: r1.run();
                break;
        }
    }
}
