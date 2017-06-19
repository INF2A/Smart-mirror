package com.smartmirror.sys.applications;

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
    synchronized void start() {
        if (running) return;
        running = true;
        gameLogicThread = new Thread(this);
        gameLogicThread.setName("Game Thread");
        System.out.println(Thread.currentThread().getName() + " - Start -  Alive: " + Thread.currentThread().isAlive());
        gameLogicThread.start();
    }

    synchronized void stop() {
        try {
            running = false;
            System.out.println(Thread.currentThread().getName() + " - Join - Alive: " + Thread.currentThread().isAlive());
            gameLogicThread.join();
            SYSTEM_closeScreen();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Will be called one time on start
     * Use this to load all that is necessary,
     * Do not start threads in this method
     */
    @Override
    protected void setup() {
        label = new JLabel("amount: ");
        System.out.println("new okug");
        JButton closeAppliction = new JButton("close");
        closeAppliction.addActionListener(e -> stop());

        SYSTEM_Widget.add(label);
        SYSTEM_Screen.add(label);
        SYSTEM_Screen.add(closeAppliction);
        focusManager.addComponent(closeAppliction);
    }

    /**
     * Will be called every time when the app openes
     * The app will have the full focus once this
     * is called. Use this to start threads.
     */
    @Override
    public void init() {
        System.out.println("PluginTest -- sort of game");
        n = 0;
        start();
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " - Run - Alive: " + Thread.currentThread().isAlive());
        while (running) {
            try {
                //17
                gameLogicThread.sleep(600);
                System.out.println(Thread.currentThread().getName() + " - Running,,, - Alive: " + Thread.currentThread().isAlive());
                label.setText("Amount: " + n++);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onBackButton() {
        super.onBackButton();
        stop();
    }
}