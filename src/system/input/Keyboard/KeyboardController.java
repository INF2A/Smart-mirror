package system.input.Keyboard;

import system.AbstractInputHandler;

/**
 * Created by Erwin on 5/15/2017.
 */
public class KeyboardController extends AbstractInputHandler {

    public String text;

    private Keyboard keyboard;
    private Keyboard.Key currentKey;
    int keynumber;
    private Keyboardview keyboardview;

    public void KeyBoardController()
    {
        keynumber = 0;
    }

    public void setKeyboard(Keyboard keyboard)
    {
        this.keyboard = keyboard;
        this.currentKey = keyboard.getKeys().get(0);
    }

    public void setKeyboardview(Keyboardview keyboardview)
    {
        this.keyboardview = keyboardview;
        keyboardview.setKeys(keyboard.getKeys());
        keyboardview.setCurrentKey(currentKey);
    }

    public Keyboardview getKeyboardview()
    {
        keynumber = 0;
        text = "";
        setCurrentKey();
        return this.keyboardview;
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
        this.setCurrentKey();
        this.update();
    }

    @Override
    public void onLeftButton()
    {
        keynumber--;;
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
        text += currentKey.text;
    }

    public void update()
    {
        System.out.println(keynumber + ", key: " + currentKey.label);
        keyboardview.repaint();
        keyboardview.revalidate();
    }

}
