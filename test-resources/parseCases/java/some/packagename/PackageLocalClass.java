package some.packagename;

final class PackageLocalClass {

    private class PrivateClass {

    }

    public class InnerClass {
        private abstract class SecondLevelInnerClass {
            private final class ThirdLevelInnerClass {

            }
        }
    }

}

abstract class AbstractClass {

}

