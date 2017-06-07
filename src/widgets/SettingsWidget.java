package widgets;

import com.smartmirror.core.view.AbstractWidget;

import javax.swing.*;
import java.awt.*;

/**
 * System Widget for displaying settings on screen
 */
public class SettingsWidget extends AbstractWidget {

    public SettingsWidget()
    {
        setPreferredSize(new Dimension(25,25));
        ClassLoader classLoader = getClass().getClassLoader();
        ImageIcon image = new ImageIcon(classLoader.getResource("img/settings.png"));
        JLabel img = new JLabel(image);
        add(img);
        location = location.TOP;
    }

    @Override
    public void init() {

    }
}
