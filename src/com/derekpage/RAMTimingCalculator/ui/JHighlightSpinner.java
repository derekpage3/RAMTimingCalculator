package com.derekpage.RAMTimingCalculator.ui;

import com.derekpage.RAMTimingCalculator.logging.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * An extension of the JSpinner class that changes the cell color when the spinner gains focus.
 */
public class JHighlightSpinner extends JSpinner {

    //Background color to set when the spinner gets focus.
    private Color highlightColor = null;

    //Keeps track of the original background color set by the runtime L&F.
    private Color originalBackgroundColor = null;

    public JHighlightSpinner(Color highlightColor) {
        super();
        this.init(highlightColor);
    }

    public JHighlightSpinner(SpinnerModel model, Color highlightColor) {
        super(model);
        this.init(highlightColor);
        /*EEL listener = EEL.getInstance();
//        listener.showDialog();
        this.addFocusListener(listener);
        this.addAncestorListener(listener);
        this.addChangeListener(listener);
        this.addVetoableChangeListener(listener);
        this.addComponentListener(listener);
        this.addContainerListener(listener);
        this.addKeyListener(listener);
        this.addInputMethodListener(listener);
        this.addPropertyChangeListener(listener);
        Logger.debug(this.getComponentCount());*/
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
        this.originalBackgroundColor = this.getBackground();
        this.addFocusListener(new JHighlightSpinner.HighlightFocusListener(this));
        //TODO make this shit work.
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

    /// Internal support FocusListner for custom functionality. ///

    /**
     * Implements the functionality needed to change to highlight color on focus and revert on losing focus.
     */
    protected static class HighlightFocusListener implements FocusListener {

        //Keep reference to the JHighlighSpinner we're bound to.
        private JHighlightSpinner _spinner = null;

        public HighlightFocusListener(JHighlightSpinner spinner) {
            if (spinner == null)
                throw new IllegalArgumentException("Parameter 'spinner' is required!");
            this._spinner = spinner;
        }

        @Override
        public void focusGained(FocusEvent e) {
            Logger.debug("Gained focus");
//TODO            this._spinner.setBackground(this._spinner.getHighlightColor());
        }

        @Override
        public void focusLost(FocusEvent e) {
            Logger.debug("Lost focus");
//TODO            this._spinner.setBackground(this._spinner.getOriginalBackgroundColor());
        }
    }
}
