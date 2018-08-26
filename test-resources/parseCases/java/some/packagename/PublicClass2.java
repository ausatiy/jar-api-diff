package some.packagename;

public class PublicClass2 {
    // different field types
    public int f1;
    public static int f2;
    public Integer f3;
    protected Integer f4;
    Integer f5;
    private Integer f6;

    // different methods
    // void methods without arguments and internal code
    private void m0_1() {

    }
    void m0_2() {

    }
    protected void m0_3() {

    }

    public void m0_4() {

    }
    public  static void m0_5() {

    }
    public final void m0_6() {

    }


    // Methods with byte-code
    public void m1_1(String arg1) {
        System.out.println(arg1);
    }
    public void m1_2(String arg2) {
        System.out.println(arg2);
    }

    public void m1_3(String arg2) {
        System.out.println(arg2);
        System.out.println(arg2);
    }




}
