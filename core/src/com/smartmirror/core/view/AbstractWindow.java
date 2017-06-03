package com.smartmirror.core.view;
import com.smartmirror.core.input.AbstractInputHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

/**
 * Created by Erwin on 5/16/2017.
 *
 * The AbstractWindow class gives input and drawing capabilities to
 * the deriving classes.
 *
 * The abstractInputHolder methods are called from within the system,
 * this gives the ability to define what action to take when an input
 * is registered.
 *
 * Everything marked as 'SYSTEM' can be safely called in a deriving class.
 * This is supposed to give extra functionality.
 *
 * Everything marked as 'INTERNAL' is not to be called by a deriving class
 *
 */
public abstract class AbstractWindow extends AbstractInputHandler {

    public IFocusManager focusManager;

    @Deprecated
    public ArrayList<JComponent> focusComponents = new ArrayList<>();
    @Deprecated
    public int currentComponent = 0;
    @Deprecated
    public int previousComponent = 0;
    @Deprecated
    public Object INTERNAL_currentFocusedObject;


    boolean INTERNAL_keyboardActive = false;
    JButton INTERNAL_KeyboardRequestHandle = new JButton(); // The handle used by the system for opening the keyboard
    JButton INTERNAL_KeyboardCloseHandle = new JButton();   // The handle used by the system for closing the keyboard

    public JPanel SYSTEM_Screen = new JPanel();  // The container that holds the application'


    public void setFocusManager(IFocusManager fm) {
        if(focusManager == null) {
            focusManager = fm;
            focusManager.addComponent(SYSTEM_Screen);
        }
    }

    @Deprecated
    protected void INTERNAL_init()
    {
//        for (int i = 1; i < focusComponents.size(); i++) {
//            focusComponents.get(i).addFocusListener(this);
//        }
    }

    /**
     *  This method updates the screen when input is detected
     */
    public void update() {
        if(INTERNAL_keyboardActive && focusManager.Selected() == null) INTERNAL_closeKeyboard();
        SYSTEM_Screen.repaint();
        SYSTEM_Screen.revalidate();
    }

    /**
     * This Method may be called to request an onscreen keyboard
     *
     * It calls an internal JButton which is connected
     * with the system through an ActionListener
     */
    public void SYSTEM_requestKeyboard()
    {
        if(!INTERNAL_keyboardActive) {
            INTERNAL_keyboardActive = true;
            INTERNAL_KeyboardRequestHandle.doClick();
        }
    }

    public boolean INTERNAL_isKeyboardActive()
    {
        return INTERNAL_keyboardActive;
    }

    public void INTERNAL_closeKeyboard()
    {
        INTERNAL_keyboardActive = false;
        INTERNAL_KeyboardCloseHandle.doClick();
    }

    /**
     * This is an INTERNAL method used by the system to get
     * get a view to display on the system. It contains the
     * container on which the app runs.
     *
     * @return The JPanel where the app resides on
     */
    public JPanel INTERNAL_getScreen()
    {
        return SYSTEM_Screen;
    }

    /**
     * This is an INTERNAL method used by the system to check
     * if the SYSTEM_requestKeyboard method is called.
     *
     * It will add a listener to the jbutton INTERNAL_KeyboardRequestHandle
     * by the system to know when to open the keyboard for input
     *
     * @param al The ActionListener for the keyboardRequestHandle
     */
    public void INTERNAL_addKeyBoardRequestActionListener(ActionListener al)
    {
        INTERNAL_KeyboardRequestHandle.addActionListener(al);
    }

    /**
     * This is an INTERNAL method used by the system to check
     * if the INTERNAL_closeKeyboard method is called.
     *
     * It will add a listener to the jbutton INTERNAL_KeyboardCloseHandle
     * by the system to know when to close the keyboard for input
     *
     * @param al The ActionListener for the keyboardCloseHandle
     */
    public void INTERNAL_addKeyboardCloseHandleActionListener(ActionListener al)
    {
        INTERNAL_KeyboardCloseHandle.addActionListener(al);
    }


    @Override
    public void onLeftButton() {
        focusManager.Previous();
        this.update();
    }

    @Override
    public void onRightButton() {
        focusManager.Next();
        this.update();
    }

    @Override
    public void onBackButton() {
        focusManager.Start();
        this.update();
    }

    @Override
    public void onMenuButton() {
        focusManager.Select();
        switch(focusManager.getSelectedComponentType()){
            case TEXT_INPUT:
                SYSTEM_requestKeyboard();
                break;
            case BUTTON:
                ((JButton)focusManager.Selected()).doClick();
                break;
            default:
                break;
        }
        this.update();
    }
}
