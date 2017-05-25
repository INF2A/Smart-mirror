package com.smartmirror.sys.input.keyboard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Erwin on 5/11/2017.
 */
public class Keyboard {
    public enum KEYCODE {
        KEY,
        SHIFT,
        ENTER;
    }

    boolean shifted = false;
    private List<Key> keys;

    private String qwerty = "qwertyuiopasdfghjklzxcvbnm";
    private String mode1 = "1234567890@#$%&-+()*\"':;!?";

    private int totalWidth;
    private int totalHeight;

    public Keyboard()
    {
        keys = new ArrayList<>();
    }

    /**
     * Check if the keyboard is shifter or not (upper- or lower-case)
     * @return boolean saying if it is set or not
     */
    public boolean 	isShifted(){
        return shifted;
    }

    /**
     * Will set the keyboard display lowercase letters
     * if set to false, and will show uppercase letters
     * if set to true;
     * @param shiftState the state to set the shift to
     */
    public void setShifted(boolean shiftState)
    {
        shifted = shiftState;
        if(shiftState) qwerty = qwerty.toUpperCase();
        else qwerty = qwerty.toLowerCase();
        setupKeys();
    }

    /**
     * Returns a list of Key's the keyboard uses.
     * @return a list of Keys
     */
    public List<Keyboard.Key> getKeys()
    {
        return keys;
    }

    /**
     * Used to setup the width and height of the keyboard;
     * This will call the setupKeys function to set the
     * proper width and height of each keys based on the
     * provided width and height.
     *
     * @param totalWidth the width the keyboard should take
     * @param totalHeight the height the keyboard should take
     */
    public void setKeysDimensions(int totalWidth, int totalHeight)
    {
        this.totalWidth = totalWidth;
        this.totalHeight = totalHeight;
        setupKeys();
    }

    /**
     * Will create the keys used by the keyboard.
     * width and height of the keys as well as positioning is based
     * on the totalWidth and totalHeight parameters that are defined
     * by the setKeyDimensions function
     *
     * This function and the layout is currently highly depended on the private field "qwerty"
     */
    private void setupKeys()
    {
        keys.clear();

        int border = 20;
        int width = (totalWidth - (border * 3)) / 10;
        int height = (totalHeight - (border)) / 4;

        String text = "";
        String label = "";
        int row = 0;
        int x = border;
        int y = border;

        for (int i = 0; i < qwerty.length(); i++) {
            row = i < 10 ? 1 : i < 19 ? 2 : i < 28 ? 3 : 4;
            x = border + (row == 1 ? (width * i) : row == 2 ? ((width * (i - 10)) + (width / 2)) : ((width * (i - 19)) + (width / 2) + width));
            y = border + (height * (row-1));
            text = qwerty.substring(i, i + 1);
            label = qwerty.substring(i, i + 1);

            addKey(text, label, width, height, x, y, false, KEYCODE.KEY);
            System.out.println("Key: " + text + ", x: " + x + ", y: " + y);
            x = border;
            y = border;
        }
        // row 3 - add SHIFT DELETE
        addKeyAtIndex(19, "SHIFT", "SHIFT", (width + (width / 2)), height, x, (y + (height * 2)), true, KEYCODE.SHIFT);
        addKey("DELETE", "DELETE", (width + (width / 2)), height, (x + ((width * 8) + (width / 2))), (y + (height * 2)), true, KEYCODE.KEY);

        // row 4 - add ?123 . SPACE , ENTER
        addKey("?123", "?123", (width + (width / 2)), height, x, (y + (height * 3)), true, KEYCODE.KEY);
        addKey(".", ".", width, height, (x + ((width * 2) - (width / 2))), (y + (height * 3)), false, KEYCODE.KEY);
        addKey(" ", "SPACE", (width * 5), height, (x + ((width * 3) - (width / 2))), (y + (height * 3)), false, KEYCODE.KEY);
        addKey(",", ",", width, height, (x + ((width * 8) - (width / 2))), (y + (height * 3)), false, KEYCODE.KEY);
        addKey("ENTER", "ENTER", (width + (width / 2)), height, (x + ((width * 9) - (width / 2))), (y + (height * 3)), true, KEYCODE.ENTER);
    }

    /**
     * Adds a key to the keyboard
     * @param text the text it will output when its selected
     * @param label what the key will display when viewed
     * @param width the width of the key
     * @param height the height of the key
     * @param x the x position of the key
     * @param y the y position of the key
     * @param modifier if the key is used to modify the keyboard and not used for outputting text
     */
    private void addKey(String text, String label, int width, int height, int x, int y, boolean modifier, KEYCODE code){
        keys.add(new Key(text, label, width, height, x, y, modifier, code));
    }

    /**
     * Adds a key to the keyboard at the specified index
     * @param index the index to insert the key at
     * @param text the text it will output when its selected
     * @param label what the key will display when viewed
     * @param width the width of the key
     * @param height the height of the key
     * @param x the x position of the key
     * @param y the y position of the key
     * @param modifier if the key is used to modify the keyboard and not used for outputting text
     */
    private void addKeyAtIndex(int index, String text, String label, int width, int height, int x, int y, boolean modifier, KEYCODE code){
        keys.add(index, new Key(text, label, width, height, x, y, modifier, code));
    }

    /**
     * Defines an class of which a keyboard consists.
     * This class will determine what are used by the keyboard.
     */
    public static class Key {
        int     x;      //top left
        int     y;      //top left
        int     width;
        int 	height;

        boolean modifier;
        String  label;      // what to display
        String  text;       // output

        KEYCODE code;

        public Key(){}

        public Key(String text, String label, int width, int height, int x, int y, boolean modifier, KEYCODE code){
            this.text = text;
            this.label = label;
            this.width = width;
            this.height = height;
            this.x = x;
            this.y = y;
            this.modifier = modifier;
            this.code = code;
        }
    }


}
