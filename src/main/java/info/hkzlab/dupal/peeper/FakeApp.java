package info.hkzlab.dupal.peeper;

public class FakeApp {
    // See here on why this exists: https://stackoverflow.com/questions/52653836/maven-shade-javafx-runtime-components-are-missing

    public static void main(String[] args) throws Exception {
        App.main(args);
    }
}
