package system.input.key;

/**
 * Created by Erwin on 5/15/2017.
 *
 * Holds the methods that will be automatically fired when the corresponding key is pressed
 */
public interface IKeyHandler {
    void onButtonPressed();
    void onButtonReleased();

    void onLeftButton();
    void onRightButton();
    void onBackButton();
    void onMenuButton();
}
