package com.smartmirror.simulator;

import com.smartmirror.core.view.AbstractApplication;
import com.smartmirror.simulator.input.keyboard.KeyboardController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by Erwin on 5/24/2017.
 */
public class Simulator {
    JFrame frame;
    JPanel container;

    InputHandler inputHandler = new InputHandler();
    KeyboardController kbc = new KeyboardController();

    public Simulator(AbstractApplication application) {
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

        addApplication(application);
        test_AttachButtonSimulator();
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

    // refreshes the screen
    private void update()
    {
        container.revalidate();
        container.repaint();
    }


    private void addApplication(AbstractApplication window)
    {
        inputHandler.attachWindow(window);

        window.INTERNAL_addExitActionListener(e -> System.exit(0));
        window.INTERNAL_addKeyBoardRequestActionListener(e -> openKeyboard());
        window.INTERNAL_addKeyboardCloseHandleActionListener(e -> closeKeyboard());
        container.add(window.INTERNAL_getScreen(), BorderLayout.CENTER);
        window.INTERNAL_init();
        update();
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


}
