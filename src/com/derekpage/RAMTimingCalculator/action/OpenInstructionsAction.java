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

    //Reference to the RAMTimingCalculator for using utility functions.
    private RAMTimingCalculator _converter = null;

    public OpenInstructionsAction(RAMTimingCalculator _converter) {
        super();
        this.init(_converter);
    }

    public OpenInstructionsAction(RAMTimingCalculator _converter, String name) {
        super(name);
        this.init(_converter);
    }

    public OpenInstructionsAction(RAMTimingCalculator _converter, String name, Icon icon) {
        super(name, icon);
        this.init(_converter);
    }

    protected void init(RAMTimingCalculator _converter) {
        if (_converter == null)
            throw new IllegalArgumentException("Parameter '_converter' is required!");
        this._converter = _converter;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        //Attempt to open the "how to" page on the user's default browser.
        try {
            URI appURI = new URI(RAMTimingCalculator.AppConstants.APP_INSTRUCTIONS_URL);
            Desktop desk = Desktop.getDesktop();
            desk.browse(appURI);
        } catch (Exception ex) {
            this._converter.triggerErrorDialog("ERROR: " + ex.getMessage(), "ERROR");
        }
    }
}
