package com.derekpage.RAMTimingCalculator.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;

/**
 * An extension of the JSpinner class that changes the cell color when the spinner gains focus.
 */
public class JHighlightSpinner extends JSpinner {

    //Background color to set when the spinner gets focus.
    private Color highlightColor = null;

    //Keeps track of the original background color set by the runtime L&F.
    private Color originalBackgroundColor = null;

    //Keeps track of the underlying text field used as the Editor for the JSpinner component.
    private JFormattedTextField jtf = null;

    public JHighlightSpinner(Color highlightColor) {
        super();
        this.init(highlightColor);
    }

    public JHighlightSpinner(SpinnerModel model, Color highlightColor) {
        super(model);
        this.init(highlightColor);
    }

    /**
     * @param highlightColor the color to use for background when the spinner has focus.
     *
     * Initializer for custom functionality.
     */
    protected void init(Color highlightColor) {
        if (highlightColor == null)
            throw new IllegalArgumentException("Parameter 'highlightColor' is required!");
        this.highlightColor = highlightColor;

        //Get the editor.  Should never be null.
        JComponent editor = this.getEditor();
        if (editor == null)
            return;

        //Should always be an instance of JSpinner.DefaultEditor, but abort if not so
        //NOTE: Not likely but it's a safety in case future versions of the JDK were to change it or reclass it.
        if (!(editor instanceof JSpinner.DefaultEditor))
            return;

        //Get the underlying text field for the spinner's editor.
        JFormattedTextField jtf = ((JSpinner.DefaultEditor) editor).getTextField();
        if (jtf == null)
            return;

        //Capture the editor component and its original background color of the text field set by the L&F.
        this.originalBackgroundColor = jtf.getBackground();
        this.jtf = jtf;
        jtf.addFocusListener(new JHighlightSpinner.HighlightFocusListener(this));


        /*EEL listener = EEL.getInstance();
        listener.showDialog();
        this.addFocusListener(listener);
        this.addAncestorListener(listener);
        this.addChangeListener(listener);
        this.addVetoableChangeListener(listener);
        this.addComponentListener(listener);
        this.addContainerListener(listener);
        this.addKeyListener(listener);
        this.addInputMethodListener(listener);
        this.addPropertyChangeListener(listener);
        this.addHierarchyBoundsListener(listener);
        this.addHierarchyListener(listener);
        this.addMouseListener(listener);
        this.addMouseWheelListener(listener);
        this.addMouseMotionListener(listener);*/
    }

    /// getter/setter ///

    public Color getHighlightColor() {
        return highlightColor;
    }

    public void setHighlightColor(Color highlightColor) {
        this.highlightColor = highlightColor;
    }

    public Color getOriginalBackgroundColor() {
        return originalBackgroundColor;
    }

    public void setOriginalBackgroundColor(Color originalBackgroundColor) {
        this.originalBackgroundColor = originalBackgroundColor;
    }

    public JFormattedTextField getJtf() {
        return jtf;
    }

    public void setJtf(JFormattedTextField jtf) {
        this.jtf = jtf;
    }


    /// Event listener registration methods for the editor. ///

    public synchronized void addEditorKeyListener(KeyListener l) {
        this.jtf.addKeyListener(l);
    }


    public synchronized void removeEditorKeyListener(KeyListener l) {
        this.jtf.removeKeyListener(l);
    }


    public synchronized KeyListener[] getEditorKeyListeners() {
        return this.jtf.getKeyListeners();
    }


    /// Internal support FocusListener for custom functionality. ///

    /**
     * Implements the functionality needed to change to highlight color on focus and revert on losing focus.
     */
    protected static class HighlightFocusListener implements FocusListener {

        //Keep reference to the JHighlightSpinner we're bound to.
        private JHighlightSpinner _spinner = null;

        public HighlightFocusListener(JHighlightSpinner spinner) {
            if (spinner == null)
                throw new IllegalArgumentException("Parameter 'spinner' is required!");
            this._spinner = spinner;
        }

        @Override
        public void focusGained(FocusEvent e) {
            this._spinner.getJtf().setBackground(this._spinner.getHighlightColor());
        }

        @Override
        public void focusLost(FocusEvent e) {
            this._spinner.getJtf().setBackground(this._spinner.getOriginalBackgroundColor());
        }
    }
}
