package UnitTest;

import com.pi4j.platform.PlatformAlreadyAssignedException;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        try {
            new GpioListenExample(args);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (PlatformAlreadyAssignedException e) {
            e.printStackTrace();
        }
    }
}
