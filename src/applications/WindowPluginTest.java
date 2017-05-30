package applications;

import com.smartmirror.core.view.AbstractUserApplication;

import javax.swing.*;

/**
 * Created by Erwin on 5/16/2017.
 */
public class WindowPluginTest extends AbstractUserApplication implements Runnable {

    private JLabel label;
    private int n;

    private volatile boolean running = false;

    Thread gameLogicThread;

    /**
     * Starts a new thread to run our game logic in.
     * This is to make sure we are not in the EDT that is responsible
     * for repainting the JFrame and JPanels.
     * This way our close button will remain responsive.
     */
    public synchronized void start() {
        if (running) return;
        running = true;
        gameLogicThread = new Thread(this);
        gameLogicThread.setName("Game Thread");
        System.out.println(Thread.currentThread().getName() + " - Start -  Alive: " + Thread.currentThread().isAlive());
        gameLogicThread.start();
    }

    /**
     * Will be called everytime when the app starts
     */
    @Override
    public void init() {
        n = 0;
        label = new JLabel("amount: ");
        System.out.println("new okug");
        JButton killApplication = new JButton("destroy me");
        killApplication.addActionListener(e -> SYSTEM_destroy());

        SYSTEM_Widget.add(label);
        SYSTEM_Screen.add(label);
        SYSTEM_Screen.add(killApplication);
        focusComponents.add(killApplication);
        start();
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " - Run - Alive: " + Thread.currentThread().isAlive());
        while (running) {
            try {
                gameLogicThread.sleep(17);
                label.setText("Amount: " + n++);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onBackButton() {
        super.onBackButton();
        try {
            running = false;
            System.out.println(Thread.currentThread().getName() + " - Join - Alive: " + Thread.currentThread().isAlive());
            SYSTEM_destroy();
            gameLogicThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}