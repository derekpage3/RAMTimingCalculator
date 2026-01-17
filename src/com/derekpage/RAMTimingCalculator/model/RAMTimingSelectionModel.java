package com.derekpage.RAMTimingCalculator.model;

import com.derekpage.RAMTimingCalculator.data.RAMTimingInterface;
import com.derekpage.RAMTimingCalculator.data.StandardRAMTiming;

import javax.swing.*;

public class RAMTimingSelectionModel extends SpinnerNumberModel {

    //Backing RAMTimingInterface object that underpins the model.
    private RAMTimingInterface timing = null;

    /// Constructors, overridden to take a RAMTimingInterface as an additional input ///

    public RAMTimingSelectionModel(RAMTimingInterface timing) {
        this.init(timing);
    }

    public RAMTimingSelectionModel(Number value, Comparable minimum, Comparable maximum, Number stepSize, RAMTimingInterface timing) {
        super(value, minimum, maximum, stepSize);
        this.init(timing);
    }

    public RAMTimingSelectionModel(int value, int minimum, int maximum, int stepSize, RAMTimingInterface timing) {
        super(value, minimum, maximum, stepSize);
        this.init(timing);
    }

    public RAMTimingSelectionModel(double value, double minimum, double maximum, double stepSize, RAMTimingInterface timing) {
        super(value, minimum, maximum, stepSize);
        this.init(timing);
    }

    /// initializer for RAMTimingInterface value provided ///

    /**
     * @param timing the DDR RAM Timing object that will underpin the model.
     */
    protected void init(RAMTimingInterface timing) {
        if (timing == null)
            throw new IllegalArgumentException("Parameter 'timing' is required!");
        this.timing = timing;
    }

    @Override
    public void setMinimum(Comparable minimum) {

        //If non-null and not an integer type, reject.
        if (minimum != null && (!((minimum instanceof Integer) || (minimum instanceof Long))))
            throw new IllegalArgumentException("Parameter 'minimum' must be null, or a Long or Integer!");

        //If null was sent, then set to the default minimum value.
        if (minimum == null) {
            this.timing.setMinValue(StandardRAMTiming.getDefaultMinimumValue());

        //Otherwise, validate the integer value provided.
        } else {
            Integer min = (int) minimum;
            if (min < 0)
                throw new IllegalArgumentException("Parameter 'minimum' must be null or a positive integer!");
            this.timing.setMinValue(min);
        }

        //If we get here then we passed validation. call superclass function to handle state change events.
        super.setMinimum(this.timing.getMinValue());
    }

    @Override
    public Comparable getMinimum() {
        return this.timing.getMinValue();
    }

    @Override
    public void setMaximum(Comparable maximum) {

        //If non-null and not an integer type, reject.
        if (maximum != null && (!((maximum instanceof Integer) || (maximum instanceof Long))))
            throw new IllegalArgumentException("Parameter 'maximum' must be null, or a Long or Integer!");

        //If null was sent, then set to the default maximum value.
        if (maximum == null) {
            this.timing.setMaxValue(StandardRAMTiming.getDefaultMaximumValue());

            //Otherwise, validate the integer value provided.
        } else {
            Integer max = (int) maximum;
            if (max < 0)
                throw new IllegalArgumentException("Parameter 'maximum' must be null or a positive integer!");
            this.timing.setMaxValue(max);
        }

        //If we get here then we passed validation. call superclass function to handle state change events.
        super.setMaximum(this.timing.getMaxValue());
    }

    @Override
    public Comparable getMaximum() {
        return this.timing.getMaxValue();
    }

    @Override
    public Object getValue() {
        return super.getValue();
    }
}
