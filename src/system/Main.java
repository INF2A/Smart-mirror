package system;

import application.Clock;
import com.pi4j.platform.PlatformAlreadyAssignedException;
import application.WindowPluginTest;
import system.view.AbstractWindow;
import system.input.Keyboard.Keyboard;
import system.input.Keyboard.KeyboardController;
import system.input.Keyboard.Keyboardview;
import system.view.SystemWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Erwin on 5/15/2017.
 */
public class Main{

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Main();
                } catch (PlatformAlreadyAssignedException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    JFrame frame;
    JPanel container;

    SystemWindow systemWindow;
    AbstractWindow currentWindow;

    final KeyboardController kbc = new KeyboardController();
    final InputHandler inputHandler = new InputHandler();
    //   final GpioListener gpio = new GpioListener(inputHandler);

    Map<String, AbstractApplication> apps;

    public Main() throws PlatformAlreadyAssignedException, InterruptedException {
        apps = new HashMap<>();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame = new JFrame();
        frame.setLayout(new BorderLayout());
        //frame.setUndecorated(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        container = new JPanel();
        container.setLayout(new BorderLayout());
        //  container.setSize(screenSize);
        frame.add(container, BorderLayout.CENTER);
        frame.setVisible(true);

        System.out.println(frame.getWidth() + " " + frame.getHeight());
        kbc.setKeyBoardDimensions(frame.getWidth(), frame.getHeight());
        inputHandler.attachKeyboardController(kbc);

        systemWindow = new SystemWindow();


        AbstractApplication clock = new Clock();
        AbstractApplication a = new WindowPluginTest("app1 test1");
        AbstractApplication b = new WindowPluginTest("This is app 2, test2");
        apps.put("test1", a);
        apps.put("test2", b);
        apps.put("clock", clock);
        systemWindow.addApplication("test1", a);
        systemWindow.addApplication("test2", b);
        systemWindow.addApplication("clock", clock);


        startSystemWindow();
        test_AttachButtonSimulator();
    }

    /// TEMPORARY
    // simulates the remote buttons
    private void test_AttachButtonSimulator() {
        Object rightKey = new Object();
        Object leftKey = new Object();
        Object backKey = new Object();
        Object menuKey = new Object();
        //  Object buttonPressed = new Object();
        //  Object buttonReleased = new Object();

        container.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("UP"), rightKey);
        container.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DOWN"), leftKey);
        container.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), backKey);
        container.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), menuKey);
        //  container.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("pressed"), buttonPressed);
        //  container.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released"), buttonReleased);

        container.getActionMap().put(rightKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputHandler.onRightButton();
                System.out.println("right -> ");
            }
        });
        container.getActionMap().put(leftKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputHandler.onLeftButton();
                System.out.println("left <- ");
            }
        });
        container.getActionMap().put(backKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputHandler.onBackButton();
                System.out.println("Back");
            }
        });
        container.getActionMap().put(menuKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputHandler.onMenuButton();
                System.out.println("Menu");
            }
        });
    }


    private void setCurrentWindow(AbstractWindow window)
    {
        currentWindow = window;
        inputHandler.attachWindow(window);

        if(window instanceof AbstractApplication)
        {
            ((AbstractApplication)window).INTERNAL_addExitActionListener(e -> changeToSystemWindow());
        }
        window.INTERNAL_addKeyBoardRequestActionListener(e -> openKeyboard());
        window.INTERNAL_addKeyboardCloseHandleActionListener(e -> closeKeyboard());

        container.removeAll();
        container.add(window.INTERNAL_getScreen(), BorderLayout.CENTER);
        window.INTERNAL_init();
        update();
    }

    private void openKeyboard() {
        //kbc.setKeyBoardDimensions(frame.getWidth(), frame.getHeight());
        container.add(inputHandler.kbc.getKeyboardview(), BorderLayout.SOUTH);
        update();
    }

    private void closeKeyboard() {
        container.remove(inputHandler.kbc.getKeyboardview());
        update();
    }


    private void startSystemWindow() {
        currentWindow = systemWindow;

        systemWindow.INTERNAL_addWindowChangeListener(e -> setCurrentWindow(systemWindow.selectedApp));

        container.add(systemWindow.INTERNAL_getScreen(), BorderLayout.CENTER);
        inputHandler.attachWindow(systemWindow);
        update();
    }
    private void changeToSystemWindow()
    {
        container.remove(currentWindow.INTERNAL_getScreen());
        startSystemWindow();
    }

    // refreshes the screen
    private void update()
    {
        container.revalidate();
        container.repaint();
    }



//
//    http://stackoverflow.com/questions/10698049/how-to-dynamically-load-a-jar-with-common-abstract-class
//
//    //PluginA.java
//package byv;
//
//import byv.BasicPlugin;
//
//    public class PluginA extends BasicPlugin {
//        @Override
//        public int test(int a) {
//            return a + a;
//        }
//    }


//    // Avoid Class.newInstance, for it is evil.
//    Constructor<? extends Runnable> ctor = runClass.getConstructor();
//    Runnable doRun = ctor.newInstance();

    private void loadTest() throws Exception {
        URL url = new File("PluginA.jar").toURI().toURL();
        URLClassLoader ClassLoader = URLClassLoader.newInstance(new URL[] {url});

        Class<?> clazz = Class.forName("byv.PluginA", true, ClassLoader);
        // plugins.put("clock", clazz);

        //   AbstractWindow window = (AbstractWindow) plugins.get("clock").newInstance();
        //  setCurrentWindow(window);
    }



}
