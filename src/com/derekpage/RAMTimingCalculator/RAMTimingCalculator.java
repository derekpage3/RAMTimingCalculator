package com.derekpage.RAMTimingCalculator;

import com.derekpage.RAMTimingCalculator.action.AboutMenuAction;
import com.derekpage.RAMTimingCalculator.action.CalculateAction;
import com.derekpage.RAMTimingCalculator.action.OpenInstructionsAction;
import com.derekpage.RAMTimingCalculator.action.SpinnerMouseWheelModel;
import com.derekpage.RAMTimingCalculator.data.TimingDefinitionStruct;
import com.derekpage.RAMTimingCalculator.html.HTMLLoader;
import com.derekpage.RAMTimingCalculator.img.ImageLoader;
import com.derekpage.RAMTimingCalculator.logging.Logger;
import com.derekpage.RAMTimingCalculator.ui.JHighlightSpinner;
import com.derekpage.RAMTimingCalculator.ui.MainFrame;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Entry point class for the application.
 */
public class RAMTimingCalculator {

    //The top-level application "main" frame.
    private JFrame mf = null;

    //Text fields and associated labels for the source and target speed for conversion.
    protected JSpinner spSourceSpeed = null;
    protected JSpinner spTargetSpeed = null;

    //Keeps a reference to a sole instance of the CalculateAction.
    protected Action _calculateActionInstance = null;

    //Map of all the input fields for the source timing selections.
    private final Map<String, JSpinner> sourceTimings = new HashMap<>(AppConstants.RAM_TIMINGS.length);

    //Maps for the target values set on calculations.
    private final Map<String, JTextField> sourceTimingNS = new HashMap<>(AppConstants.RAM_TIMINGS.length);
    private final Map<String, JTextField> targetTimingsMin = new HashMap<>(AppConstants.RAM_TIMINGS.length);
    private final Map<String, JTextField> targetTimingsCalc = new HashMap<>(AppConstants.RAM_TIMINGS.length);
    private final Map<String, JTextField> targetTimingsMax = new HashMap<>(AppConstants.RAM_TIMINGS.length);

    /**
     * @param args:
     *            LogLevel=<Logger level>
     *
     * NOTE: Core application class constructor takes care of initialization.
     */
    public static void main(String[] args) {
        long st = System.currentTimeMillis();

        //Set logging level if command line argument set for it.
        for (String arg : args) {
            arg = arg.toUpperCase();

            //Initialize log level if specified.
            if (arg.startsWith(AppConstants.LOG_LEVEL)) {
                Logger.setLogLevelByName(arg.substring(9));
            }
        }

        //Init app
        new RAMTimingCalculator();
        Logger.debug("App started in " + (System.currentTimeMillis() - st) + " ms");
    }

    /**
     * Sole initializer of the application.
     */
    public RAMTimingCalculator() {

        //Build the top-level frame.
        JFrame mf = new MainFrame(AppConstants.APP_NAME);
        this.mf = mf;

        //Initialize app panels
        this.initializeAppIconImage();
        this.initializeMenuBar();
        this.initializeTopPanel();
        this.initializeTimingsPanel();
        this.initializeBottomPanel();

        //Pack, center and display app.
        //TODO now can you make it save and restore it's last position? ;)
        this.packAndCenterApp();
        mf.setVisible(true);
    }

    /// getter/setter ///

    public JSpinner getSpSourceSpeed() {
        return spSourceSpeed;
    }

    public JSpinner getSpTargetSpeed() {
        return spTargetSpeed;
    }

    public Map<String, JSpinner> getSourceTimings() {
        return sourceTimings;
    }

    public Map<String, JTextField> getSourceTimingNS() {
        return sourceTimingNS;
    }

    public Map<String, JTextField> getTargetTimingsMin() {
        return targetTimingsMin;
    }

    public Map<String, JTextField> getTargetTimingsCalc() {
        return targetTimingsCalc;
    }

    public Map<String, JTextField> getTargetTimingsMax() {
        return targetTimingsMax;
    }

    /////////////////////////// UI INITIALIZATION METHODS //////////////////////////
    //                                                                            //
    // This section contains all the methods responsible for initializing the     //
    // UI of the application.                                                     //
    //                                                                            //
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Initializes the top panel where the instruction text and source/target speed settings are located.
     */
    private void initializeTopPanel() {

        //Initialize single panel to contain the top header segment.
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setAutoscrolls(false);
        topPanel.setFocusable(false);

        //Initialize a second panel to contain the source and target speed components.
        JPanel pnlSpeedSelections = new JPanel();
        pnlSpeedSelections.setLayout(new GridLayout(1, 4));
        pnlSpeedSelections.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        topPanel.add(pnlSpeedSelections, BorderLayout.NORTH);

        //Border for speed label padding.
        Border lblBorder = this.getSpeedSelectionLabelBorder();

        //Add the Source Speed selection
        //TODO try to get the Enter key to trigger the Calculate Action.
        JLabel lblSrcSpeed = new JLabel("Source Speed (MT/s):");
        lblSrcSpeed.setBorder(lblBorder);
        lblSrcSpeed.setHorizontalAlignment(SwingConstants.RIGHT);
        pnlSpeedSelections.add(lblSrcSpeed);
        pnlSpeedSelections.add(this.spSourceSpeed = this.createJHighlightSpinner(getSpeedSelectionModel(AppConstants.DEFAULT_SOURCE_SPEED)));
        this.spSourceSpeed.addMouseWheelListener(new SpinnerMouseWheelModel());


        //Increase the source speed label Font by 1 point.
        //Mainly doing this to force the source speed spinner box to be larger.
        lblSrcSpeed.setFont(new Font(lblSrcSpeed.getFont().getFontName(), lblSrcSpeed.getFont().getStyle(), (lblSrcSpeed.getFont().getSize() + 2)));

        //Add the Target Speed selection
        JLabel lblTargetSpeed = new JLabel("Target Speed (MT/s):");
        lblTargetSpeed.setBorder(lblBorder);
        lblTargetSpeed.setHorizontalAlignment(SwingConstants.RIGHT);
        pnlSpeedSelections.add(lblTargetSpeed);
        pnlSpeedSelections.add(this.spTargetSpeed = this.createJHighlightSpinner(getSpeedSelectionModel(AppConstants.DEFAULT_TARGET_SPEED)));
        this.spTargetSpeed.addMouseWheelListener(new SpinnerMouseWheelModel());

        //Increase the target speed label Font by 1 point.
        //Mainly doing this to force the target speed spinner box to be larger.
        lblTargetSpeed.setFont(lblSrcSpeed.getFont());

        //Separator for the top panel.
        topPanel.add(new JSeparator(JSeparator.HORIZONTAL), BorderLayout.SOUTH);

        //Add top pane to the top segment of the main window.
        this.mf.getContentPane().add(topPanel, BorderLayout.NORTH);
    }

    private void initializeTimingsPanel() {

        //Build new panel with a Grid layout and add to the parent frame
        //timing fields will be captured in 6.
        JPanel pnlTimingEntry = new JPanel();
        pnlTimingEntry.setLayout(new GridLayout(0, 6));
        this.mf.getContentPane().add(pnlTimingEntry, BorderLayout.CENTER);

        //Font that will be used for the header labels.
        Font hdrFont = null;

        //Build header labels.
        JLabel lblTimingName = this.createHeaderLabel("Timing", "hdrTiming", "The name of the memory timing (in AMD terminology)");
        JLabel lblTimingValue = this.createHeaderLabel("Value", "hdrValue", "The value for the timing at the source speed");
        JLabel lblTimingNS = this.createHeaderLabel("Cycle Time", "hdrNS", "The time (in nanoseconds) the timing value equates to at the source speed");
        JLabel lblLowerTiming = this.createHeaderLabel("Lower", "hdrLower", "The lowest integer value for this timing based on the calculated conversion; this may work, depending on the timing but is not guaranteed.");
        JLabel lblTargetTimingCalc = this.createHeaderLabel("Calc", "hdrCalc", "The scaled timing value based on the source to target speed conversion.");
        JLabel lblHigherTiming = this.createHeaderLabel("Higher", "hdrHigher", "The highest integer value for this timing based on the calculated conversion; this SHOULD be stable for most timings as it will always equate to the source cycle time or more.");

        //Create the header font and set on header labels. For this we take whatever the default size is that the
        //system sets and then add a few point sizes to make it stand out.
        hdrFont = new Font(lblTimingName.getFont().getFontName(),
                lblTimingName.getFont().getStyle(),
                (lblTimingName.getFont().getSize() + 2));
        lblTimingName.setFont(hdrFont);
        lblTimingValue.setFont(hdrFont);
        lblTimingNS.setFont(hdrFont);
        lblLowerTiming.setFont(hdrFont);
        lblTargetTimingCalc.setFont(hdrFont);
        lblHigherTiming.setFont(hdrFont);

        //Add header labels to the panel to start the grid.
        pnlTimingEntry.add(lblTimingName);
        pnlTimingEntry.add(lblTimingValue);
        pnlTimingEntry.add(lblTimingNS);
        pnlTimingEntry.add(lblLowerTiming);
        pnlTimingEntry.add(lblTargetTimingCalc);
        pnlTimingEntry.add(lblHigherTiming);

        //Add a label/text field combo for each supported timing.
        for (TimingDefinitionStruct tds : AppConstants.RAM_TIMINGS) {
            this.createTimingRow(pnlTimingEntry, tds);
        }
    }

    /**
     * Initializes the bottom panel which contains the Action buttons.
     */
    private void initializeBottomPanel() {

        //Build new panel to contain the buttons.
        JPanel pnlActionButtons = new JPanel();
        pnlActionButtons.setLayout(new BorderLayout());
        this.mf.getContentPane().add(pnlActionButtons, BorderLayout.SOUTH);

        //Add horizontal rule to separate from the timings panel.
        pnlActionButtons.add(new JSeparator(JSeparator.HORIZONTAL), BorderLayout.NORTH);

        //Add button for doing calculation
        //NOTE: Have to wrap it into an inner panel to keep the top level panel's BorderLayout from
        //making the button fill the entire SOUTH panel.
        JButton btnCalculate = new JButton("Calculate");
        JPanel jp = new JPanel();
        jp.add(btnCalculate);
        pnlActionButtons.add(jp, BorderLayout.SOUTH);
        btnCalculate.addActionListener(this.getCalculateAction());
    }

    /**
     * Tries to load the application icon image to set on the top-level frame.
     */
    private void initializeAppIconImage() {

        //Initialize the application's Icon image or the top level frame.
        //NOTE: this is a rather "bass ackwards" way of doing this, creating an icon just to get an image.
        //Why does Swing do it this way?
        //this.mf.setIconImage(new ImageIcon(AppConstants.ICON_IMAGE_NAME).getImage());
        this.mf.setIconImage(ImageLoader.loadAppImageFile(AppConstants.ICON_IMAGE_NAME));
    }

    private void initializeMenuBar() {

        //Build simple Help menu with About MenuItem for now. We'll expand this later.
        JMenuBar jmb = new JMenuBar();
        jmb.setBorder(BorderFactory.createLineBorder(this.mf.getBackground()));

        //Add the Help menu.
        JMenu jm = new JMenu("Help", false);
        jmb.add(jm);

        //Add menu item for instructions.
        JMenuItem jmi = new JMenuItem(new OpenInstructionsAction("Instructions"));
        jm.add(jmi);

        //Add simple "About" application popup.
        JMenuItem helpItem = new JMenuItem(new AboutMenuAction("About", this.mf));
        jm.add(helpItem);
        this.mf.setJMenuBar(jmb);
    }

    /**
     * Initializer that packs the main frame after all components are added and attempts to center it on the screen.
     */
    private void packAndCenterApp() {

        //Pack to preferred size.
        this.mf.pack();

        //Get the Graphics configuration for the display.
        GraphicsConfiguration gConfig = this.mf.getGraphicsConfiguration();
        if (gConfig != null) {
            Rectangle screenDimensions = gConfig.getBounds();
            if (screenDimensions != null) {
                int appWidth = this.mf.getWidth();
                int appHeight = this.mf.getHeight();

                //Attempt to vertically and horizontally center the app by subtracting its width/height from the width/height of the screen
                if (appHeight > 0 && appWidth > 0) {
                    Rectangle newBounds = new Rectangle(this.mf.getBounds());
                    newBounds.setLocation(((screenDimensions.width - appWidth) / 2), ((screenDimensions.height - appHeight) / 2));
                    this.mf.setBounds(newBounds);
                }
            }
        }
    }

    /**
     * @param lblText     the text the label will display
     * @param lblName     the name property of the created JLabel
     * @param toolTipText (optional) Tooltip text to display on hover over the label.
     * @return JLabel
     * <p>
     * Returns a standardized JLabel for the headers of the table.
     */
    private JLabel createHeaderLabel(String lblText, String lblName, String toolTipText) {
        if (lblText == null)
            throw new IllegalArgumentException("Parameter 'lblText' is required!");
        if (lblName == null)
            throw new IllegalArgumentException("Parameter 'lblName' is required!");

        //Build and return label.
        JLabel lbl = new JLabel(lblText);
        lbl.setName(lblName);
        lbl.setBorder(this.getTimingFieldLabelBorder());
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setBorder(BorderFactory.createEtchedBorder());
        lbl.setFocusable(false);
        if (toolTipText != null)
            lbl.setToolTipText(toolTipText);
        return lbl;
    }

    /**
     * @param parent JPanel to add the created components to.
     * @param tds    The TimingDefinitionStruct to add a row for.
     *               <p>
     *               Used to build a standard "row" in the timings panel.
     */
    private void createTimingRow(JPanel parent, TimingDefinitionStruct tds) {
        if (parent == null)
            throw new IllegalArgumentException("Parameter 'parent' is required!");
        if (tds == null)
            throw new IllegalArgumentException("Parameter 'tds' is required!");
        String currTimingName = tds.getTimingName();

        //timing label alignment is based on LTR orientation.
        boolean ltr = parent.getComponentOrientation().isLeftToRight();

        //1. Build Label for the timing value.
        JLabel lbl = new JLabel(currTimingName);
        lbl.setBorder(BorderFactory.createEtchedBorder());
        lbl.setHorizontalTextPosition(ltr ? SwingConstants.RIGHT : SwingConstants.LEFT);
        lbl.setHorizontalAlignment(ltr ? SwingConstants.RIGHT : SwingConstants.LEFT);
        lbl.setVerticalTextPosition(SwingConstants.CENTER);
        lbl.setText(currTimingName);
        lbl.setName("lbl" + currTimingName);
        lbl.setFocusable(false);

        //1a. Get ToolTip HTML for current timing if present.
        //String ttext = HTMLLoader.loadFromFile(RAMTimingCalculator.AppConstants.HELP_FILE_REL_PATH + tds.getTimingName() + ".html");
        String ttext = HTMLLoader.loadHTMLHelpFile(tds.getTimingName() + ".html");
        if (!ttext.isEmpty()) {
            lbl.setToolTipText(ttext);
        }

        //2. Build input spinner for the source timing value entry.
        Logger.debug("Timing: " + tds.getTimingName() + ", def: " + tds.getDefaultValue() + ", min: " + tds.getMinValue() + ", max: " + tds.getMaxValue());
        JSpinner js = this.createJHighlightSpinner(new SpinnerNumberModel(
                tds.getDefaultValue(),
                tds.getMinValue(),
                tds.getMaxValue(),
                1
        ));

        js.setName("sp" + currTimingName);
        js.setPreferredSize(new Dimension(80, -1));  //shrink the JSpinners to a more reasonable width.
        js.addMouseWheelListener(new SpinnerMouseWheelModel());
        lbl.setLabelFor(js);
        this.sourceTimings.put(currTimingName, js);

        //3. Build the textbox where the cycles-to-nanosecond conversion will be set.
        JTextField txtNS = new JTextField();
        txtNS.setColumns(5);
        txtNS.setName("txt" + currTimingName + "_ns");
        txtNS.setEditable(false);
        txtNS.setFocusable(false);
        txtNS.setHorizontalAlignment(JTextField.CENTER);
        this.sourceTimingNS.put(currTimingName, txtNS);

        //4. Build text field for minimum target timing value.
        JTextField txtMinVal = new JTextField();
        txtMinVal.setColumns(3);
        txtMinVal.setName("txtMinVal" + currTimingName);
        txtMinVal.setEditable(false);
        txtMinVal.setFocusable(false);
        txtMinVal.setHorizontalAlignment(JTextField.CENTER);
        this.targetTimingsMin.put(currTimingName, txtMinVal);

        //5. Build text field for the target cycle time.
        JTextField txtNSVal = new JTextField();
        txtNSVal.setColumns(3);
        txtNSVal.setName("txtNSVal" + currTimingName);
        txtNSVal.setEditable(false);
        txtNSVal.setFocusable(false);
        txtNSVal.setHorizontalAlignment(JTextField.CENTER);
        this.targetTimingsCalc.put(currTimingName, txtNSVal);

        //6. Build text field for the maximum target value.
        JTextField txtMaxVal = new JTextField();
        txtMaxVal.setColumns(3);
        txtMaxVal.setName("txtMaxVal" + currTimingName);
        txtMaxVal.setEditable(false);
        txtMaxVal.setFocusable(false);
        txtMaxVal.setHorizontalAlignment(JTextField.CENTER);
        this.targetTimingsMax.put(currTimingName, txtMaxVal);

        //Add inputs of current row to the panel.
        parent.add(lbl);
        parent.add(js);
        parent.add(txtNS);
        parent.add(txtMinVal);
        parent.add(txtNSVal);
        parent.add(txtMaxVal);
    }

    private JSpinner createJHighlightSpinner(SpinnerModel model) {

        //Build a highligh spinner with the provided model and the configured highlight color.
        JHighlightSpinner js = new JHighlightSpinner(
                model,
                new Color(AppConstants.SPINNER_HIGHLIGHT_COLOR));

        //Add a MouseWheelListener to allow the spinner to be mousewheel scrolled.
        js.addMouseWheelListener(new SpinnerMouseWheelModel());
        return js;
    }

    private synchronized Action getCalculateAction() {
        if (this._calculateActionInstance == null)
            this._calculateActionInstance = new CalculateAction(this);
        return this._calculateActionInstance;
    }

    /**
     * @return javax.swing.Border
     * <p>
     * Returns an empty border that provides the standard padding for labels created for timing text fields.
     */
    private Border getTimingFieldLabelBorder() {
        return BorderFactory.createEmptyBorder(
                AppConstants.TIMING_LBL_BORDER_TOP,
                AppConstants.TIMING_LBL_BORDER_LEFT,
                AppConstants.TIMING_LBL_BORDER_BOTTOM,
                AppConstants.TIMING_LBL_BORDER_RIGHT);
    }

    /**
     * @return javax.swing.Border
     * <p>
     * Returns an empty border that provides the standard padding for labels created for timing text fields.
     */
    private Border getSpeedSelectionLabelBorder() {
        return BorderFactory.createEmptyBorder(
                AppConstants.SPEED_LBL_BORDER_TOP,
                AppConstants.SPEED_LBL_BORDER_LEFT,
                AppConstants.SPEED_LBL_BORDER_BOTTOM,
                AppConstants.SPEED_LBL_BORDER_RIGHT);
    }


    //////////////////////////////// STATIC ELEMENTS ///////////////////////////////
    //                                                                            //
    // This section contains all static objects defined by the main application   //
    // class.                                                                     //
    //                                                                            //
    ////////////////////////////////////////////////////////////////////////////////

    //Version number string for reference in logging.
    public static final String version = "0.0.1";

    /**
     * @param defaultSpeed to starting value to set in the spinner
     * @return SpinnerModel
     * <p>
     * Utility method to build SpinnerModels for the speed selections.
     */
    public static SpinnerModel getSpeedSelectionModel(int defaultSpeed) {
        return new SpinnerNumberModel(defaultSpeed,
                AppConstants.SPEED_SELECT_MIN_VAL,
                AppConstants.SPEED_SELECT_MAX_VAL,
                AppConstants.SPEED_SELECT_STEP_VAL);
    }

    /**
     * @param js JSpinner with the value to parse to long
     * @return numeric value in the text field if valid.
     * @throws NumberFormatException if non-nil value is not an integer.
     *                               <p>
     *                               Convenience function that performs the boiler-plate operation of obtaining the text value from a JTextfield and
     *                               parsing it to a long value for calculations.
     */
    public static Long getLongValueFromTextField(JSpinner js) throws NumberFormatException {
        if (js == null)
            throw new IllegalArgumentException("Parameter 'js' is required!");

        //Return null if no value set.
        Object srcVal = js.getValue();
        if (srcVal == null)
            return null;

        //Parse to a long value and return.
        return ((Integer) srcVal).longValue();
    }

    /**
     * @param message - the message to show on screen
     * @param title   - the text to show on the header of the error popup window
     *                <p>
     *                Global method for triggering an error message popup.
     */
    public static void triggerErrorDialog(String message, String title) {
        if (message == null)
            throw new RuntimeException("Parameter 'message' is required!");
        if (title == null || title.isEmpty())
            title = "Error";
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * This static class defines the app constants that govern the applications behavior and other configuration
     * options; it is explicitly made public to allow outside package classes to utilize it as needed.
     */
    public static class AppConstants {

        //name of the Icon image to try to load.
        public static final String ICON_IMAGE_NAME = "icon-memory-calc.png";

        //names for app initializer parameters
        public static final String LOG_LEVEL = "LOGLEVEL";

        //Color used as the "highlight" color for the JHighlightSpinners.
        public static final int SPINNER_HIGHLIGHT_COLOR = 0x00FFFF80;

        //Application display name.
        public static final String APP_NAME = "DDR Memory Timing Calculator";

        //border widths for labels created for the timing fields
        public static final int SPEED_LBL_BORDER_TOP = 5;
        public static final int SPEED_LBL_BORDER_LEFT = 5;
        public static final int SPEED_LBL_BORDER_BOTTOM = 5;
        public static final int SPEED_LBL_BORDER_RIGHT = 5;

        //border widths for labels created for the source and target speed selections.
        public static final int TIMING_LBL_BORDER_TOP = 2;
        public static final int TIMING_LBL_BORDER_LEFT = 5;
        public static final int TIMING_LBL_BORDER_BOTTOM = 3;
        public static final int TIMING_LBL_BORDER_RIGHT = 5;

        //The increments for the SpinnerModel to use for the source and target speed selections
        public static final int SPEED_SELECT_MIN_VAL = 100;
        public static final int SPEED_SELECT_STEP_VAL = 100;
        public static final int SPEED_SELECT_MAX_VAL = 16000;

        //Default source and target speed values.
        //NOTE: Using common values from AM5.
        public static final int DEFAULT_SOURCE_SPEED = 6000;
        public static final int DEFAULT_TARGET_SPEED = 6200;

        //URL to the instruction document.
        public static final String APP_INSTRUCTIONS_URL = "https://github.com/derekpage3/RAMTimingCalculator";  //TODO point to instructions.

        //Ordered list of timings managed by the calculator.
        //NOTE: Min/max values for timings were obtained from an ASRock X670E Taichi board, based on the lowest and
        //highest values the UEFI would allow setting; no doubt this varies on othe boards, between Intel/AMD and
        //especially DDR generations.
        public static final TimingDefinitionStruct[] RAM_TIMINGS = {
                new TimingDefinitionStruct("tCL", "", 22, 32, 64),
                new TimingDefinitionStruct("tRCD", "", 8, 38, 62),
                new TimingDefinitionStruct("tRP", "", 8, 38, 62),
                new TimingDefinitionStruct("tRAS", "", 24, 96, 128),
                new TimingDefinitionStruct("tRC", "", 32, 74, 255),
                new TimingDefinitionStruct("tWR", "", 42, 78, 96),
                new TimingDefinitionStruct("tREFI", "", 50, 50000, 65535),
                new TimingDefinitionStruct("tRFC", "", 50, 500, 2047),
                new TimingDefinitionStruct("tRFC2", "", 50, 385, 2047),
                new TimingDefinitionStruct("tRFCsb", "", 50, 300, 2047),
                new TimingDefinitionStruct("tRTP", "", 5, 14, 31),
                new TimingDefinitionStruct("tRRDL", "", 4, 10, 32),
                new TimingDefinitionStruct("tRRDS", "", 1, 6, 20),
                new TimingDefinitionStruct("tFAW", "", 12, 22, 80),
                new TimingDefinitionStruct("tWTRL", "", 8, 16, 48),
                new TimingDefinitionStruct("tWTRS", "", 1, 6, 16),
                new TimingDefinitionStruct("tRDRDSCL", "", 1, 5, 15),
                new TimingDefinitionStruct("tRDRDSC", "", 1, 1, 15),
                new TimingDefinitionStruct("tRDRDSD", "", 1, 7, 15),
                new TimingDefinitionStruct("tRDRDDD", "", 1, 7, 15),
                new TimingDefinitionStruct("tWRWRSCL", "", 1, 6, 63),
                new TimingDefinitionStruct("tWRWRSC", "", 1, 1, 15),
                new TimingDefinitionStruct("tWRWRSD", "", 1, 7, 15),
                new TimingDefinitionStruct("tWRWRDD", "", 1, 7, 15),
                new TimingDefinitionStruct("tWRRD", "", 1, 3, 15),
                new TimingDefinitionStruct("tRDWR", "", 1, 15, 31),
                new TimingDefinitionStruct("tCWL", "", 1, 30, Integer.MAX_VALUE),
                new TimingDefinitionStruct("tCKE", "", 0, 0, Integer.MAX_VALUE),
                new TimingDefinitionStruct("tMOD", "", 1, 42, Integer.MAX_VALUE),
                new TimingDefinitionStruct("tMODPDA", "", 1, 32, Integer.MAX_VALUE),
                new TimingDefinitionStruct("tSTAG", "", 1, 7, Integer.MAX_VALUE),
                new TimingDefinitionStruct("tMRD", "", 1, 42, Integer.MAX_VALUE),
                new TimingDefinitionStruct("tMRDPDA", "", 1, 32, Integer.MAX_VALUE),
                new TimingDefinitionStruct("tPHYWRD", "", 1, 6, Integer.MAX_VALUE),
                new TimingDefinitionStruct("tPHYWRL", "", 1, 17, Integer.MAX_VALUE),
                new TimingDefinitionStruct("tPHYRDL", "", 1, 35, Integer.MAX_VALUE),
                new TimingDefinitionStruct("tRDPRE", "", 1, 3, Integer.MAX_VALUE),
                new TimingDefinitionStruct("tWRPRE", "", 1, 3, Integer.MAX_VALUE)
        };
    }
}
