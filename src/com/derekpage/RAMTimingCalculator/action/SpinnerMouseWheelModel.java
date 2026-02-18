package com.derekpage.RAMTimingCalculator.action;

import javax.swing.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * Event listener to handle scrolling JSpinner components.
 */
public class SpinnerMouseWheelModel implements MouseWheelListener {

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

        //Sanity check - abort if not a JSpinner for some reason.
        if (!(e.getSource() instanceof JSpinner))
            return;
        JSpinner spinner = (JSpinner) e.getSource();

        //Determine adjustment amount.  Sanity check - abort if not Integer.
        Object adjustValue = (e.getWheelRotation() < 0) ? spinner.getModel().getNextValue() : spinner.getModel().getPreviousValue();
        if (!(adjustValue instanceof Integer))
            return;

        //Adjust model accordingly.
        spinner.getModel().setValue(adjustValue);
    }
}
