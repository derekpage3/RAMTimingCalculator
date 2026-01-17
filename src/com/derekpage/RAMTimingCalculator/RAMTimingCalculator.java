package com.derekpage.RAMTimingCalculator;

import com.derekpage.RAMTimingCalculator.action.AboutMenuAction;
import com.derekpage.RAMTimingCalculator.action.CalculateAction;
import com.derekpage.RAMTimingCalculator.action.OpenInstructionsAction;
import com.derekpage.RAMTimingCalculator.data.TimingDefinitionStruct;
import com.derekpage.RAMTimingCalculator.html.HTMLLoader;
import com.derekpage.RAMTimingCalculator.img.ImageLoader;
import com.derekpage.RAMTimingCalculator.logging.Logger;

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

    //Map of all the input fields for the source timing selections.
    private final Map<String, JSpinner> sourceTimings = new HashMap<>(AppConstants.RAM_TIMINGS.length);

    //Maps for the target values set on calculations.
    private final Map<String, JTextField> sourceTimingNS = new HashMap<>(AppConstants.RAM_TIMINGS.length);
    private final Map<String, JTextField> targetTimingsMin = new HashMap<>(AppConstants.RAM_TIMINGS.length);
    private final Map<String, JTextField> targetTimingsCalc = new HashMap<>(AppConstants.RAM_TIMINGS.length);
    private final Map<String, JTextField> targetTimingsMax = new HashMap<>(AppConstants.RAM_TIMINGS.length);

    public RAMTimingCalculator() {

        //Build the top-level frame.
        JFrame mf = new JFrame();
        this.mf = mf;
        mf.setTitle(AppConstants.APP_NAME);
        mf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mf.setResizable(false);

        //Initialize app panels
        this.initializeAppIconImage();
        this.initializeMenuBar();
        this.initializeTopPanel();
        this.initializeTimingsPanel();
        this.initializeBottomPanel();
            //TODO get the window to open up centered on the screen.
        //Pack and show.
        mf.pack();
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
        pnlSpeedSelections.add(this.spSourceSpeed = new JSpinner(getSpeedSelectionModel(AppConstants.DEFAULT_SOURCE_SPEED)));

        //Increase the source speed label Font by 1 point.
        //Mainly doing this to force the source speed spinner box to be larger.
        lblSrcSpeed.setFont(new Font(lblSrcSpeed.getFont().getFontName(), lblSrcSpeed.getFont().getStyle(), (lblSrcSpeed.getFont().getSize()+2)));

        //Add the Target Speed selection
        JLabel lblTargetSpeed = new JLabel("Target Speed (MT/s):");
        lblTargetSpeed.setBorder(lblBorder);
        lblTargetSpeed.setHorizontalAlignment(SwingConstants.RIGHT);
        pnlSpeedSelections.add(lblTargetSpeed);
        pnlSpeedSelections.add(this.spTargetSpeed = new JSpinner(getSpeedSelectionModel(AppConstants.DEFAULT_TARGET_SPEED)));

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

        //Border for padding on all labels
        Border lblBorder = this.getTimingFieldLabelBorder();

        //Font that will be used for the header labels.
        Font hdrFont = null;

        //Timing field label header.
        JLabel lblTimingName = new JLabel("Timing");
        lblTimingName.setName("hdrTiming");
        lblTimingName.setBorder(lblBorder);
        lblTimingName.setHorizontalAlignment(SwingConstants.CENTER);
        lblTimingName.setBorder(BorderFactory.createEtchedBorder());
        lblTimingName.setFocusable(false);
        lblTimingName.setToolTipText("The name of the memory timing (in AMD terminology)");

        //Value selection header.
        JLabel lblTimingValue = new JLabel("Value");
        lblTimingValue.setName("hdrValue");
        lblTimingValue.setBorder(lblBorder);
        lblTimingValue.setHorizontalAlignment(SwingConstants.CENTER);
        lblTimingValue.setBorder(BorderFactory.createEtchedBorder());
        lblTimingValue.setFocusable(false);
        lblTimingValue.setToolTipText("The value for the timing at the source speed");

        //Source timing NS conversion header.
        JLabel lblTimingNS = new JLabel("Cycle Time");
        lblTimingNS.setName("hdrNS");
        lblTimingNS.setBorder(lblBorder);
        lblTimingNS.setHorizontalAlignment(SwingConstants.CENTER);
        lblTimingNS.setBorder(BorderFactory.createEtchedBorder());
        lblTimingNS.setFocusable(false);
        lblTimingNS.setToolTipText("The time (in nanoseconds) the timing value equates to at the source speed");

        //Lower target timing header.
        JLabel lblLowerTiming = new JLabel("Lower");
        lblLowerTiming.setName("hdrLower");
        lblLowerTiming.setBorder(lblBorder);
        lblLowerTiming.setHorizontalAlignment(SwingConstants.CENTER);
        lblLowerTiming.setBorder(BorderFactory.createEtchedBorder());
        lblLowerTiming.setFocusable(false);
        lblLowerTiming.setToolTipText("The lowest integer value for this timing based on the calculated conversion; this may work, depending on the timing but is not guaranteed.");

        //Calculated target timing value.
        JLabel lblTargetTimingCalc = new JLabel("Calc");
        lblTargetTimingCalc.setName("hdrCalc");
        lblTargetTimingCalc.setBorder(lblBorder);
        lblTargetTimingCalc.setHorizontalAlignment(SwingConstants.CENTER);
        lblTargetTimingCalc.setBorder(BorderFactory.createEtchedBorder());
        lblTargetTimingCalc.setFocusable(false);
        lblTargetTimingCalc.setToolTipText("The scaled timing value based on the source to target speed conversion.");

        //Higher target timing header.
        JLabel lblHigherTiming = new JLabel("Higher");
        lblHigherTiming.setName("hdrHigher");
        lblHigherTiming.setBorder(lblBorder);
        lblHigherTiming.setHorizontalAlignment(SwingConstants.CENTER);
        lblHigherTiming.setBorder(BorderFactory.createEtchedBorder());
        lblHigherTiming.setFocusable(false);
        lblHigherTiming.setToolTipText("The highest integer value for this timing based on the calculated conversion; this SHOULD be stable for most timings as it will always equate to the source cycle time or more.");

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
            String currTimingName = tds.getTimingName();

            //1. Build Label for the timing value.
            JLabel lbl = new JLabel(currTimingName);
            lbl.setBorder(BorderFactory.createEtchedBorder());
            lbl.setHorizontalTextPosition(SwingConstants.RIGHT);
            lbl.setHorizontalAlignment(SwingConstants.RIGHT);
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
            JSpinner js = new JSpinner(
                    new SpinnerNumberModel(tds.getDefaultValue(), 0, tds.getMaxValue(), 1)
            );
            js.setName("sp" + currTimingName);
            js.setPreferredSize(new Dimension(80, -1));  //shrink the JSpinners to a more reasonable width.
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
            pnlTimingEntry.add(lbl);
            pnlTimingEntry.add(js);
            pnlTimingEntry.add(txtNS);
            pnlTimingEntry.add(txtMinVal);
            pnlTimingEntry.add(txtNSVal);
            pnlTimingEntry.add(txtMaxVal);
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
        btnCalculate.addActionListener(new CalculateAction(this));
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
     * @return javax.swing.Border
     *
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
     *
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
    // class.
    //                                                                            //
    ////////////////////////////////////////////////////////////////////////////////

    //Version number string for reference in logging.
    public static final String version = "0.0.0";

    /**
     * @param args there are none.
     *
     * Core application class constructor takes care of initialization.
     */
    public static void main(String[] args) {
        long st = System.currentTimeMillis();

        //Set logging level if command line argument set for it.
        if (args.length > 0)
            Logger.setLogLevelByName(args[0]);

        //Init app
        new RAMTimingCalculator();
        Logger.debug("App started in " + (System.currentTimeMillis() - st) + " ms");
    }

    /**
     * @param defaultSpeed to starting value to set in the spinner
     * @return SpinnerModel
     *
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
     *
     * Convenience function that performs the boiler-plate operation of obtaining the text value from a JTextfield and
     * parsing it to a long value for calculations.
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
     * @param title - the text to show on the header of the error popup window
     *
     * Global method for triggering an error message popup.
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
                new TimingDefinitionStruct("tCKE", "", 1, 0, Integer.MAX_VALUE),
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
