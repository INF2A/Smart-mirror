package system.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Erwin on 5/11/2017.
 */
public class Keyboard {
    int 	KEYCODE_ALT;
    int 	KEYCODE_CANCEL;
    int 	KEYCODE_DELETE;
    int 	KEYCODE_DONE;
    int 	KEYCODE_MODE_CHANGE;
    int 	KEYCODE_SHIFT;

    boolean shifted;


    private List<Key> keys = new ArrayList<Key>();

    private String qwerty = "qwertyuiopasdfghjklzxcvbnm";
    private String numbers = "1234567890";

    public Keyboard()
    {
        for (int i = 0; i < qwerty.length(); i++) {
            Keyboard.Key key = new Keyboard.Key(qwerty.substring(i, i+1),
                    qwerty.substring(i, i+1) , 50, 50, 5 + ( 50 * i), 5 + (50));
            addKey(key);
        }
    }

    public boolean 	isShifted(){
        return shifted;
    }
    public void setShifted(boolean shiftState)
    {
        shifted = shiftState;
    }
    public void addKey(Keyboard.Key key)
    {
        keys.add(key);
    }
    public List<Keyboard.Key> getKeys()
    {
        return keys;
    }


    public static class Key {
        int     x;      //top left
        int     y;      //top left
        int     width;
        int 	height;
        int 	gap;

        boolean modifier;   // shift or alt (1 shift ! )

        boolean sticky;     // KEEP CAPS ON?
        boolean on;         // if sticky, is sticky on?

        String  label;      // what to display
        String  text;       // output
        boolean repeatable; // long press repeat?


        public Key(String text, String label, int width, int height, int x, int y){
            this.text = text;
            this.label = label;
            this.width = width;
            this.height = height;
            this.x = x;
            this.y = y;
            this.gap = 0;
        }
    }


}
