package com.smartmirror.sys;

import com.smartmirror.core.view.AbstractWidget;

import java.awt.*;

/**
 * Created by basva on 26-6-2017.
 */
public class Font {
    public enum FontSize
    {
        H1,
        H2,
        H3,
        H4,
        H5,
        H6,
        H7
    }

    public static java.awt.Font applyFontSize(FontSize fontSize)
    {
        java.awt.Font font = null;

        switch (fontSize)
        {
            case H1:
                font = new java.awt.Font("Dialog", java.awt.Font.PLAIN, Toolkit.getDefaultToolkit().getScreenResolution());
                break;
            case H2:
                font = new java.awt.Font("Dialog", java.awt.Font.PLAIN, Toolkit.getDefaultToolkit().getScreenResolution() / 2);
                break;
            case H3:
                font = new java.awt.Font("Dialog", java.awt.Font.PLAIN, Toolkit.getDefaultToolkit().getScreenResolution() / 3);
                break;
            case H4:
                font = new java.awt.Font("Dialog", java.awt.Font.PLAIN, Toolkit.getDefaultToolkit().getScreenResolution() / 4);
                break;
            case H5:
                font = new java.awt.Font("Dialog", java.awt.Font.PLAIN, Toolkit.getDefaultToolkit().getScreenResolution() / 5);
                break;
            case H6:
                font = new java.awt.Font("Dialog", java.awt.Font.PLAIN, Toolkit.getDefaultToolkit().getScreenResolution() / 6);
                break;
            case H7:
                font = new java.awt.Font("Dialog", java.awt.Font.PLAIN, Toolkit.getDefaultToolkit().getScreenResolution() / 7);
                break;
        }

        return font;
    }
}
