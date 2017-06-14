package widgets;

import com.smartmirror.core.view.AbstractWidget;

import javax.swing.*;
import java.awt.*;

/**
 * Default widget used for application that have no widget setup
 */
public class DefaultWidget extends AbstractWidget {
    public DefaultWidget()
    {
        setPreferredSize(new Dimension(50,50));
        ClassLoader classLoader = getClass().getClassLoader();
        ImageIcon image = new ImageIcon(classLoader.getResource("img/Logo-Definitief-Icon.png"));
        JLabel img = new JLabel(image);
        add(img);
        location = location.CENTER;
    }

    @Override
    public void init() {

    }
}
