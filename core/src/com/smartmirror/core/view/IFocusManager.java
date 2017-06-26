package com.smartmirror.core.view;

import javax.swing.JComponent;
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
public interface IFocusManager {

    /**
     * Stores what type the selected component is
     * - EMTPY = There is no object selected
     * - TEXT_INPUT = Object where text can be entered
     * - BUTTON = Object which can be pressed
     */
    enum TYPE {
        EMPTY,
        TEXT_INPUT,
        BUTTON,
        RADIOBUTTON,
        PANEL;
    }

    /**
     * Adds a new component which can be focused on by the system
     * @param component to add
     */
    void addComponent(JComponent component);

    /**
     * Adds the list of components which can be focused on by the system
     * @param components to add
     */
    void addComponents(List<JComponent> components);

    /**
     * Inserts a component before the given component.
     * Makes the focus manager focus on the inserted component before
     * the given component
     * @param component The component to add
     * @param before The component to add the new component before
     */
    void insertBefore(JComponent component, JComponent before);

    /**
     * Inserts a component after the given component.
     * Makes the focus manager focus on the inserted component after
     * the given component
     * @param component The component to add
     * @param after The component to add the new component after
     */
    void insertAfter(JComponent component, JComponent after);

    /**
     * Sets the first focusable component
     * This does not override the first component. Instead
     * all components will move up 1 spot.
     * @param component the component to set as first
     */
    void insertFirst(JComponent component);

    /**
     * Replaces a component with a new component
     * @param replace the component to replace
     * @param with the component that takes the place
     */
    void replaceComponent(JComponent replace, JComponent with);

    /**
     * Removes a component from the Focus Manager
      * @param component the component to remove
     */
    void removeComponent(JComponent component);

    /**
     * Removes all components known by the focus manager
     * from the given list
     * @param components The components to remove
     */
    void removeComponents(List<JComponent> components);

    /**
     * Gives back the number of components known by the Focus Manager
     * @return integer value of how many components are known
     */
    int getNumberOfComponents();

    /**
     * Returns the list of components in the order that
     * they will be navigated through
     * @return Ordered List of JComponents.
     */
    List<JComponent> getComponentList();

    /** Tells the focus manager to go to the first component;
     * If there are no components it will return null
     * @return The first JComponent if available otherwise null
     */
    JComponent Start();

    /**
     * Tells the focus manager to go to the next component if available;
     * If there are no components it will return null
     * @return The next JComponent if available otherwise null
     */
    JComponent Next();


    /** Tells the focus manager to go to the previous component if available;
     * If there are no components it will return null
     * @return The previous JComponent if available otherwise null
     */
    JComponent Previous();

    /** Tells the focus manager to give back the currently highlighted
     * component. If there are no components it will return null.
     *
     * Note - This is not the focused component. Focused compontents
     * get the focus by requesting it. Example: Current().requestFocus();
     *
     * @return The current JComponent if available otherwise null
     */
    JComponent Current();

    /**
     * Tells the focus manager to make the current highlighted
     * component the selected component
     *
     */
    void Select();

    /**
     * Tells the focus manager to return the selected component
     *
     * @return the selected object
     */
    Object Selected();

    /** Tells the focus manager to set the current component to
     * the start component and clears the display
     */
    void Reset();


    /**
     * Gives back the type of the selectedComponent
     * @return the TYPE of component
     */
    TYPE getSelectedComponentType();

}
