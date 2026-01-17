package com.derekpage.RAMTimingCalculator.action;

import com.derekpage.RAMTimingCalculator.RAMTimingCalculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URI;

/**
 * Action for the menu item to open the "how to use" instructions.
 */
public class OpenInstructionsAction extends AbstractAction {

    public OpenInstructionsAction() {
        super();
    }

    public OpenInstructionsAction(String name) {
        super(name);
    }

    public OpenInstructionsAction(String name, Icon icon) {
        super(name, icon);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        //Attempt to open the "how to" page on the user's default browser.
        try {
            URI appURI = new URI(RAMTimingCalculator.AppConstants.APP_INSTRUCTIONS_URL);
            Desktop desk = Desktop.getDesktop();
            desk.browse(appURI);
        } catch (Exception ex) {
            RAMTimingCalculator.triggerErrorDialog("ERROR: " + ex.getMessage(), "ERROR");
        }
    }
}
