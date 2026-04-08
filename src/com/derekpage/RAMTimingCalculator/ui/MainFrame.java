package com.derekpage.RAMTimingCalculator.ui;

import com.derekpage.RAMTimingCalculator.config.AppConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * This subclass of JFrame simply exists to encapsulate default JFrame options we always want to apply.
 */
public class MainFrame extends JFrame {

    /// CONSTRUCTORS ///

    public MainFrame() throws HeadlessException {
        super();
        this.init();
    }

    public MainFrame(GraphicsConfiguration gc) {
        super(gc);
        this.init();
    }

    public MainFrame(String title) throws HeadlessException {
        super(title);
        this.init();
    }

    public MainFrame(String title, GraphicsConfiguration gc) {
        super(title, gc);
        this.init();
    }

    /**
     * This method defines all the customizations and overridden behaviors we do on JFrame upon initialization.
     */
    protected void init() {

        //App main frame will DISPOSE_ON_CLOSE so we can run exit processing.
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);

        //Save current state to the config file on app exit.
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosed(WindowEvent e) {
                AppConfig.getInstance().saveToConfFile();
                super.windowClosed(e);
            }
        });
    }
}
