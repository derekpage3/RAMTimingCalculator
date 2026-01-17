package com.derekpage.RAMTimingCalculator.action;

import com.derekpage.RAMTimingCalculator.RAMTimingCalculator;
import com.derekpage.RAMTimingCalculator.data.TimingDefinitionStruct;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;

/**
 * Action used by the Calculate button to perform the calculations. It contains a reference to the main
 * RAMTimingCalculator instance for easy access to all the fields.
 */
public class CalculateAction extends AbstractAction {
    private RAMTimingCalculator _converter = null;

    public CalculateAction(RAMTimingCalculator _converter) {
        super();
        this._converter = _converter;
    }

    public CalculateAction(String name, RAMTimingCalculator _converter) {
        super(name);
        this._converter = _converter;
    }

    public CalculateAction(String name, Icon icon, RAMTimingCalculator _converter) {
        super(name, icon);
        this._converter = _converter;
    }

    @Override
    /**
     * Mathematical formula for calculating a memory timing in nanoseconds, where:
     *
     * DDR = Effective DDR data bus speed in MT/s
     * MEMCLK = Clock speed of the DRAM = (DDR / 2) in MHz
     * CT = Cycle Time = (1 / MEMCLK) s
     * TIMING = the integer value for the timing in question (in clock cycles).
     * TimingS = timing in seconds
     * TimingNS = timing in nanoseconds
     *
     * FORMULA:
     *  TimingS = TIMING * (1 / (MEMCLK x 1,000,000))
     *  TimingNS = TimingS * 1,000,000,000
     *
     * EXAMPLE (DDR5)
     * --------------
     *
     * TIMING (tCL) = 26 cycles
     * DDR = 6,000 MT/s
     * MEMCLK = 3,000 MHz = 3,000,000,000 Hz
     * CT = (1 / 3,000,000,000 Hz) = 0.000000000333333333333333333333 s = 0.333333333333333333333 ns
     *                                                                        _
     * TimingS = (26 cycles * (1 / (3,000 x 1,000,000)) s/cycle) = 0.00000000867 s
     *                         _                               _
     * TimingNS = (0.000000008667 s * 1,000,000,000 ns/s) = 8.667 ns
     *
     * NOTES ON CODED FORM
     * -------------------
     *
     * For simplicity, we would normally code the formula to convert the time values to seconds and then perform the
     * calculations in that unit before converting it back to the desired unit (nanoseconds in this case); however, in
     * order to maximize the precision and accuracy of the calculation we want to minimize the number of floating point
     * operations performed.  As such, we've translated the formula to only perform a floating point operation in the
     * last step of the calculation. Further, because cycle times for DDR are regularly in fractions of nanoseconds,
     * we convert the formula to capture all the intermediate values in picoseconds, giving us enough resolution to keep
     * everything in an integer format until the last step, where we use a double-precision floating point to do the
     * final step.  Therefore, the formula used is:
     *
     * DDR = Effective DDR speed in MT/s
     * MEMCLK = Clock speed of the DRAM = (DDR / 2) in MHz
     * CT = Cycle Time = (1 / MEMCLK) s
     * TIMING = the integer value for the timing in question (in clock cycles).
     * TimingS = timing in seconds
     * TimingNS = timing in nanoseconds
     *
     * TimingNS = TIMING cycles * (1 / MEMCLK * 1,000,000,000) cycles/s
     *
     * NOTE: We could simplify this for ourselves greatly by just using the BigMath SDK classes, but this works for now.
     * TODO finish optimizing this.
     */
    public strictfp void actionPerformed(ActionEvent e) {

        //Top-level try for any errors not handled individually.
        try {

            //Get the source speed value
            Long srcSpeedLong = null;
            try {
                srcSpeedLong = RAMTimingCalculator.getLongValueFromTextField(this._converter.getSpSourceSpeed());

                //Abort if not source speed set
                if (srcSpeedLong == null)
                    throw new RuntimeException("No value set for source speed!");

            } catch (NumberFormatException nfe) {
                throw new RuntimeException("Value set on source speed field is not a valid integer!", nfe);
            }

            //Get the target speed value
            Long targetSpeedLong = null;
            try {
                targetSpeedLong = RAMTimingCalculator.getLongValueFromTextField(this._converter.getSpTargetSpeed());

                //Abort if not source speed set
                if (targetSpeedLong == null)
                    throw new RuntimeException("No value set for target speed!");

            } catch (NumberFormatException nfe) {
                throw new RuntimeException("Value set on target speed field is not a valid integer!", nfe);
            }

            //Convert speeds from MT/s to into transfers/second
            long sourceSpeedTransfersPerSecond = srcSpeedLong * 1000000L;
            long targetSpeedTransfersPerSecond = targetSpeedLong * 1000000L;

            //Perform calculations for each timing.
            for (TimingDefinitionStruct rt : RAMTimingCalculator.AppConstants.RAM_TIMINGS) {

                /// Get source and target text fields ///

                //Get the input field for the current source timing value.
                JSpinner spSourceTiming = this._converter.getSourceTimings().get(rt.getTimingName());
                if (spSourceTiming == null)
                    throw new RuntimeException("CANT FIND SOURCE TEXT FIELD FOR TIMING '" + rt.getTimingName() + "'!");

                //Get the TextField for the current source timing NS value and clear it for re-calculation.
                JTextField txtSourceTimingNS = this._converter.getSourceTimingNS().get(rt.getTimingName());
                if (txtSourceTimingNS == null)
                    throw new RuntimeException("CANT FIND SOURCE NS TEXT FIELD FOR TIMING '" + rt.getTimingName() + "'!");
                txtSourceTimingNS.setText("");

                //Get the TextField for the target lower timing value and clear it for re-calculation.
                JTextField txtTargetTimingLower = this._converter.getTargetTimingsMin().get(rt.getTimingName());
                if (txtTargetTimingLower == null)
                    throw new RuntimeException("CANT FIND TARGET LOWER TEXT FIELD FOR TIMING '" + rt.getTimingName() + "'!");
                txtTargetTimingLower.setText("");

                //Get the TextField for the target higher timing value and clear it for re-calculation.
                JTextField txtTargetTimingHigher = this._converter.getTargetTimingsMax().get(rt.getTimingName());
                if (txtTargetTimingHigher == null)
                    throw new RuntimeException("CANT FIND TARGET HIGHER TEXT FIELD FOR TIMING '" + rt.getTimingName() + "'!");
                txtTargetTimingHigher.setText("");

                //Get numeric value set on the timing field
                Long sourceTimingVal = null;
                try {
                    sourceTimingVal = RAMTimingCalculator.getLongValueFromTextField(spSourceTiming);

                    //Abort if no source timing set.
                    if (sourceTimingVal == null)
                        throw new RuntimeException("No value set for source timing " + rt.getTimingName() + "!");

                } catch (NumberFormatException nfe) {
                    throw new RuntimeException("Value set on timing field '" + rt.getTimingName() + "' is not a valid integer!", nfe);
                }

                /// Perform calculations ///

                //1. Calculate the source cycle time set it on the source cycle time textbox.
                //NOTE: We halve the sourceSpeedTransfersPerSecond value because the actual cycle time is half of the DDR speed.
                double sourceTimingValNS = ((double) sourceTimingVal * (1.0D / (((double) sourceSpeedTransfersPerSecond) / 2.0D))) * 1000000000D;
                txtSourceTimingNS.setText(new DecimalFormat("0.00 ns").format(sourceTimingValNS));

                //2. Calculate the scaled (decimal) timing value for the current timing at the target speed.
                Double targetTimingCalc = (targetSpeedTransfersPerSecond * sourceTimingVal) / ((double) sourceSpeedTransfersPerSecond);
                this._converter.getTargetTimingsCalc().get(rt.getTimingName()).setText(new DecimalFormat("0.00").format(targetTimingCalc));

                //3. Calculate and display the lowest integer and highest integer timing for the calculated value.
                this._converter.getTargetTimingsMin().get(rt.getTimingName()).setText("" + ((long) Math.floor(targetTimingCalc)));
                this._converter.getTargetTimingsMax().get(rt.getTimingName()).setText("" + ((long) Math.ceil(targetTimingCalc)));
            }

        //Trigger the application's error popup on any errors.
        } catch (Exception ex) {
            RAMTimingCalculator.triggerErrorDialog(ex.getMessage(), "");
        }
    }
}