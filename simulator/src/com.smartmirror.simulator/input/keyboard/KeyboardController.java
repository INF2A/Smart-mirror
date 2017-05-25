package com.smartmirror.simulator.input.keyboard;

import com.smartmirror.core.input.AbstractInputHandler;
import com.smartmirror.simulator.input.keyboard.Keyboard;
import com.smartmirror.simulator.input.keyboard.Keyboardview;

import java.awt.*;

/**
 * Created by Erwin on 5/15/2017.
 */
public class KeyboardController extends AbstractInputHandler {

    public String text;

    private int screenHeight;
    private int screenWidth;

    private Keyboard keyboard;
    private Keyboard.Key currentKey;
    int keynumber;
    private Keyboardview keyboardview;

    public KeyboardController()
    {
        setKeyboard(new Keyboard());
        setKeyboardview(new Keyboardview());
        keynumber = 0;
    }

    public void setKeyboard(Keyboard keyboard)
    {
        this.keyboard = keyboard;
    }

    public void setKeyBoardDimensions(int width, int height) {
        keyboardview.setPreferredSize(new Dimension(width, height / 4));
        keyboard.setKeysDimensions(width, (height / 4));
        this.currentKey = keyboard.getKeys().get(0);
    }

    public void setKeyboardview(Keyboardview keyboardview)
    {
        this.keyboardview = keyboardview;
        keyboardview.setKeys(keyboard.getKeys());
        keyboardview.setCurrentKey(currentKey);
    }

    public Keyboard getKeyboard() {
        return keyboard;
    }

    public Keyboardview getKeyboardview()
    {
        keynumber = 0;
        text = "";
        setCurrentKey();
        return this.keyboardview;
    }

    public Keyboard.KEYCODE getKeyCodeCurrentKey() {
        return currentKey.code;
    }

    private void setCurrentKey()
    {
        currentKey = keyboard.getKeys().get(keynumber);
        keyboardview.setCurrentKey(currentKey);
    }

    @Override
    public void onRightButton()
    {
        keynumber++;
        if(keynumber > keyboard.getKeys().size() - 1) keynumber = 0;
        this.setCurrentKey();
        this.update();
    }

    @Override
    public void onLeftButton()
    {
        keynumber--;;
        if(keynumber < 0) keynumber = keyboard.getKeys().size() - 1;
        this.setCurrentKey();
        this.update();
    }

    @Override
    public void onBackButton()
    {
        //return focus
    }

    @Override
    public void onMenuButton()
    {
        if(currentKey.modifier) {
            if(currentKey.text.equals("SHIFT")) {
                if(keyboard.isShifted()) keyboard.setShifted(false);
                else keyboard.setShifted(true);
                setCurrentKey();
            } else if(currentKey.text.equals("?123")) {
                // switch to weird mode
            } else if(currentKey.text.equals("DELETE")){
                text = text.substring(0, text.length() - 1);
            }
        } else {
            text += currentKey.text;
        }
        this.update();
    }

    public void update()
    {
        System.out.println(keynumber + ", key: " + currentKey.label);
        keyboardview.repaint();
        keyboardview.revalidate();
    }

}
