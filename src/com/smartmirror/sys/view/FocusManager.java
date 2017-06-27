package com.smartmirror.sys.view;

import com.smartmirror.core.view.IFocusManager;

import javax.swing.*;

import java.awt.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Erwin on 6/3/2017.
 *
 * This class defines the methods which a FocusManager should implement.
 * It is the contract between Applications and the MainSystem.
 *
 * The focus manager is designed to hold JComponents.
 * Applications can use the focus manager to notify the system what components
 * are selectable. This is necessary so that the system can cycle through
 * the components and highlight them when needed.
 *
 * Default functionality is to cycle through the component list in the order they
 * are added.
 * By not using the focus manager, the components will not be cycled and it
 * will be necessary to define a custom focus for the input.
 */
public class FocusManager implements IFocusManager {

    List<JComponent> componentList = new ArrayList<>();
    TYPE selectedObjectType = TYPE.EMPTY;

    int _selected;
    int _current;
    int _previous;

    /**
     * Adds a new component which can be focused on by the system
     * @param component to add
     */
    @Override
    public void addComponent(JComponent component) {
        componentList.add(component);
    }

    /**
     * Adds the list of components which can be focused on by the system
     * @param components to add
     */
    @Override
    public void addComponents(List<JComponent> components) {
        componentList.addAll(components);
    }

    /**
     * Inserts a component before the given component.
     * Makes the focus manager focus on the inserted component before
     * the given component
     * @param component The component to add
     * @param before The component to add the new component before
     */
    @Override
    public void insertBefore(JComponent component, JComponent before){
        int pos = componentList.indexOf(before);
        if(pos != -1) componentList.add(componentList.indexOf(before), component);
    }

    /**
     * Inserts a component after the given component.
     * Makes the focus manager focus on the inserted component after
     * the given component
     * @param component The component to add
     * @param after The component to add the new component after
     */
    @Override
    public void insertAfter(JComponent component, JComponent after){
        int pos = componentList.indexOf(after);
        if(pos != -1) {
            // check if it is the last component of the list
            if(pos == componentList.size() -1) componentList.add(component);
            else componentList.add(pos + 1, component);
        }
    }

    /**
     * Sets the first focusable component
     * This does not override the first component. Instead
     * all components will move up 1 spot.
     * @param component the component to set as first
     */
    @Override
    public void insertFirst(JComponent component){
        componentList.add(0, component);
    }

    /**
     * Replaces a component with a new component
     * @param replace the component to replace
     * @param with the component that takes the place
     */
    @Override
    public void replaceComponent(JComponent replace, JComponent with){
        if(componentList.size() > 0) {
            int pos = componentList.indexOf(replace);
            if(pos != -1) {
                componentList.remove(pos);
                componentList.add(pos, with);
            }
        }
    }

    /**
     * Removes a component from the Focus Manager
     * @param component the component to remove
     */
    @Override
    public void removeComponent(JComponent component) {
        componentList.remove(component);
    }

    /**
     * Removes all components known by the focus manager
     * from the given list
     * @param components The components to remove
     */
    @Override
    public void removeComponents(List<JComponent> components) {
        componentList.removeAll(components);
    }

    /**
     * Gives back the number of components known by the Focus Manager
     * @return integer value of how many components are known
     */
    @Override
    public int getNumberOfComponents() {
        return componentList.size();
    }

    /**
     * Returns the list of components in the order that
     * they will be navigated through
     * @return Ordered List of JComponents.
     */
    @Override
    public List<JComponent> getComponentList(){
        return componentList;
    }

    /** Tells the focus manager to go to the first component;
     * If there are no components it will return null
     * @return The first JComponent if available otherwise null
     */
    @Override
    public JComponent Start() {
        if(componentList.size() > 0) {
            _current = 0;
            _previous = componentList.size() - 1;
            update();
            return componentList.get(_current);
        }
        return null;
    }

    /**
     * Tells the focus manager to go to the next component if available;
     * If there are no components it will return null
     * @return The next JComponent if available otherwise null
     */
    @Override
    public JComponent Next() {
        if(componentList.size() > 0) {
            if (_current < componentList.size() - 1) {
                _previous = _current;
                _current++;
                update();
                return componentList.get(_current);
            }
        }
        return null;
    }


    /** Tells the focus manager to go to the previous component if available;
     * If there are no components it will return null
     * @return The previous JComponent if available otherwise null
     */
    @Override
    public JComponent Previous() {
        if(componentList.size() > 0) {
            if (_current > 0) {
                _previous = _current;
                _current--;
                update();
                return componentList.get(_current);
            }
        }
        return null;
    }


    /** Tells the focus manager to give back the current component;
     * If there are no components it will return null
     * @return The current JComponent if available otherwise null
     */
    @Override
    public JComponent Current() {
        if(componentList.size() > 0) {
            return componentList.get(_current);
        }
        return null;
    }

    /**
     * Tells the focus manager to make the current highlighted
     * component the selected component
     */
    @Override
    public void Select() {
        if(componentList.size() > 0) {
            _selected = _current;
            if (componentList.get(_selected) instanceof JButton) {
                selectedObjectType = TYPE.BUTTON;
            } else if (componentList.get(_selected) instanceof JTextField) {
                selectedObjectType = TYPE.TEXT_INPUT;
            } else if (componentList.get(_selected) instanceof JPanel) {
                selectedObjectType = TYPE.PANEL;
            } else  if (componentList.get(_selected) instanceof JRadioButton) {
                selectedObjectType = TYPE.RADIOBUTTON;
            } else {
                selectedObjectType = TYPE.EMPTY;
            }
        }
    }

    /**
     * Tells the focus manager to return the selected component
     *
     * @return the selected object
     */
    @Override
    public Object Selected() {
        return componentList.get(_selected);
    }


    /** Tells the focus manager to set the selected component to
     * the start component and clears the highlights
     */
    @Override
    public void Reset() {
        _selected = 0;
        _current = 0;
        _previous = 0;
        for(JComponent c : componentList) {
            c.setBorder(null);
            c.updateUI();
        }
    }


    @Override
    public TYPE getSelectedComponentType() {
        return selectedObjectType;
    }

    private void update() {
//        if(componentList.get(_current) instanceof JRadioButton)
//        {
//            JRadioButton temp = (JRadioButton)componentList.get(_previous);
//            ((JRadioButton) componentList.get(_current)).setText(((JRadioButton) componentList.get(_current)).getText() + "<");
//        }
        componentList.get(_current).setBorder(BorderFactory.createLineBorder(Color.WHITE, 4));
        componentList.get(_previous).setBorder(null);
        componentList.get(_previous).updateUI();
        System.out.println( componentList.get(_current).toString());
    }



}
