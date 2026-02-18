package com.derekpage.RAMTimingCalculator.ui;

import javax.swing.*;
import java.awt.*;

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

        //Default to EXIT_ON_CLOSE
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
    }
}
