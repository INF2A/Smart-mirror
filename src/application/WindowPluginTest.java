package application;

import com.smartmirror.core.view.AbstractApplication;

import javax.swing.*;

/**
 * Created by Erwin on 5/16/2017.
 */
public class WindowPluginTest extends AbstractApplication implements Runnable {

    private JLabel label;
    private int n;
    private boolean running = false;

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

    @Override
    public void init() {
        n = 0;
        label = new JLabel("amount: ");
        SYSTEM_Screen.add(label);
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
            gameLogicThread.join();
            System.out.println(Thread.currentThread().getName() + " - Join - Alive: " + Thread.currentThread().isAlive());
            SYSTEM_closeScreen();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}