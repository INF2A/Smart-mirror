package com.smartmirror.core.input;

import com.sun.glass.events.KeyEvent;

/**
 * Created by Erwin on 5/15/2017.
 *
 * AbstractInputHandler is a holder for the interfaces: IKeyHandler, IMotionHandler and ISpeechHandler

 * Every implemented method is set to null so that the deriving class can chose what input to support.
 */
public abstract class AbstractInputHandler implements IKeyHandler, IMotionHandler, ISpeechHandler {

    @Override
    public void onButtonPressed(){}
    @Override
    public void onButtonReleased(){}
    @Override
    public void onLeftButton(){}
    @Override
    public void onRightButton(){}
    @Override
    public void onBackButton(){}
    @Override
    public void onMenuButton(){}

}
