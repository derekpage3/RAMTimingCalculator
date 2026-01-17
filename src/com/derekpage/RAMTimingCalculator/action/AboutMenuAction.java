package com.derekpage.RAMTimingCalculator.action;

import com.derekpage.RAMTimingCalculator.RAMTimingCalculator;
import com.derekpage.RAMTimingCalculator.html.HTMLLoader;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Action class for the Help/About menu popup.
 */
public class AboutMenuAction extends AbstractAction {

    //reference to the top-level application JFrame.
    private JFrame mf = null;

    //The help title and HTML content of the About popup.
    private String helpPaneTitle = null;
    private String helpPaneHTML = null;

    //reference to the Help Menu popup.

    /// Constructors ///

    public AboutMenuAction(JFrame mainFrame) {
        super();
        this.init(mainFrame);
    }

    public AboutMenuAction(String name, JFrame mainFrame) {
        super(name);
        this.init(mainFrame);
    }

    public AboutMenuAction(String name, Icon icon, JFrame mainFrame) {
        super(name, icon);
        this.init(mainFrame);
    }

    /**
     * @param mainFrame the top level JFrame of the application.
     *
     * Called by constructors to capture the top-level application JFrame, for use in showing the About popup.
     */
    protected void init(JFrame mainFrame) {

        //Capture the main top-level frame that the application will be using.
        if (mainFrame == null)
            throw new IllegalArgumentException("Parameter 'mainFrame' is required!");
        this.mf = mainFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        //Lazy-load the HTML contents of the About popup on first click
        if (this.helpPaneTitle == null) {
            this.helpPaneTitle = "About";
            //this.helpPaneHTML = HTMLLoader.loadFromFile(RAMTimingCalculator.AppConstants.HELP_FILE_REL_PATH + "\\HELP.html");
            this.helpPaneHTML = HTMLLoader.loadHTMLHelpFile("HELP.html");
            if (!this.helpPaneHTML.isEmpty()) {
                this.helpPaneHTML = this.helpPaneHTML.replaceAll("\\$\\{version\\}", RAMTimingCalculator.version);
            }
            //TODO rework this to make the link clickable.
        }

        //Standard INFO JOptionPane popup.
        JOptionPane.showMessageDialog(this.mf, this.helpPaneHTML, this.helpPaneTitle, JOptionPane.INFORMATION_MESSAGE);
    }
}
