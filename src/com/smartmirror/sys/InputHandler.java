package com.smartmirror.sys;

import com.smartmirror.core.input.AbstractInputHandler;
import com.smartmirror.core.view.AbstractWindow;
import com.smartmirror.sys.input.keyboard.Keyboard;
import com.smartmirror.sys.input.keyboard.KeyboardController;
import javax.swing.*;


/**
 * Created by Erwin on 5/16/2017.
 *
 * This class defines how input will be handled
 * It wel determine what to do with the key events, motion events and speech events.
 * It also holds an AbstractWindow to propagate input events when they are fired by the system.
 *
 * Currently it will only handle key inputs
 */
public class InputHandler extends AbstractInputHandler {

    AbstractWindow window;  // The window to propagate input generated events to
    KeyboardController kbc;

    public void attachKeyboardController(KeyboardController kbc)
    {
        this.kbc = kbc;
    }

    /**
     * Setter for attaching a window to the inputHandler
     * @param window The window to set
     */
    public void attachWindow(AbstractWindow window)
    {
        this.window = window;
    }

    @Override
    public void onButtonPressed()
    {
        if(window.INTERNAL_isKeyboardActive()) kbc.onButtonPressed();
        else window.onButtonPressed();
    }

    @Override
    public void onButtonReleased()
    {
        if(window.INTERNAL_isKeyboardActive()) kbc.onButtonReleased();
        else window.onButtonReleased();
    }

    @Override
    public void onLeftButton()
    {
        if(window.INTERNAL_isKeyboardActive()) kbc.onLeftButton();
        else window.onLeftButton();
    }

    @Override
    public void onRightButton()
    {
        if(window.INTERNAL_isKeyboardActive()) kbc.onRightButton();
        else {
//            JTextField b = (JTextField)window.INTERNAL_currentFocusedObject;

            window.onRightButton();
        }
    }

    @Override
    public void onBackButton()
    {
        if(window.INTERNAL_isKeyboardActive()) window.INTERNAL_closeKeyboard();
        else window.onBackButton();
    }

    @Override
    public void onMenuButton()
    {
        if(window.INTERNAL_isKeyboardActive())
        {
            kbc.onMenuButton();
            JTextField b = (JTextField) window.focusManager.Selected();
            b.setText(kbc.text);
            if(kbc.getKeyCodeCurrentKey().equals(Keyboard.KEYCODE.ENTER)) {
                window.INTERNAL_closeKeyboard();
                window.focusManager.Next();
                window.focusManager.Select();
            }
        }
        else window.onMenuButton();
    }

}
