package com.derekpage.RAMTimingCalculator.ui;

import javax.swing.*;
import java.awt.*;

/**
 * This subclass of JFrame exists to fix certain out of box issues with the core JFrame class (mainly sizing issues).
 */
public class MainFrame extends JFrame {

    //Internal constant that represents the expected with the Iconify/Deiconify/Close buttons of the top-level frame
    //Used for calculating minimum window sizing on initialization.  NOTE: This was derived in Win10 on a 1440p monitor
    //at 125% DPI, and probably doesn't scale correctly at other resolutions/DPI settings.
    private static final int MIN_MAX_CLOSE_WIDTH = 175;



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
     * This method defines all the customizations and overriddien behaviors we do on JFrame upon initialization.
     */
    protected void init() {
        //TODO
    }

    @Override
    public void setMinimumSize(Dimension minimumSize) {
        super.setMinimumSize(minimumSize);
    }
}
