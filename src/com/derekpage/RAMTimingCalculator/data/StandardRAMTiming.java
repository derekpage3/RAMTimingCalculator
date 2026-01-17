package com.derekpage.RAMTimingCalculator.data;

/**
 * This data structure represents a typical DDR RAM timing, which is specified in terms of clock cycles to wait for the
 * operation it represents to complete, i.e., waiting longer means the RAM has more time to complete the operation,
 * which typically means greater stability.  All current versions of DDR timings work this way, but defining this as
 * an interface with a single class sets up the application for future versions of DDR that might introduce new timings
 * that are specified differently and might require a different scaling algorithm than the standard one.
 */
public class StandardRAMTiming implements RAMTimingInterface {

    //For each timing we capture its name and a description, the minimum/maximum allowed value, and the default
    //value to set when loading the calculator source values.
    private String timingName;
    private String description;
    private int minValue;
    private int defaultValue;
    private int maxValue;

    /**
     * @param timingName acronym of the timing
     * @param description description of the details of what the timing's function is.
     * @param minValue minimum value the timing is allowed to be set to.
     * @param defaultValue the default value to set on the timing's textbox on startup.
     * @param maxValue maximum value the timing is allowed to be set to.
     *
     * Initialize a RAMTiming object with the key values for selection.
     * If no minimum value is provided, it will be set to 0.
     * If no default value is provided. it will be set to the value halfway between minimum and maximum.
     * If no maximum value is provided, it will be set to Integer.MAX_VALUE.
     */
    public StandardRAMTiming(String timingName, String description, Integer minValue, Integer defaultValue, Integer maxValue) {
        if (timingName == null || timingName.isEmpty())
            throw new IllegalArgumentException("Parameter 'timingName' is required!");
        this.timingName = timingName;
        this.description = description;

        //Default minimum to 0 if not set, but disallow negative numbers.
        this.minValue = (minValue == null) ? StandardRAMTiming.getDefaultMinimumValue() : minValue;
        if (this.minValue < 0)
            throw new IllegalArgumentException("Parameter 'minValue' cannot be a negative number");

        //Default maximum to max possible value if not set.
        this.maxValue = (maxValue == null) ? StandardRAMTiming.getDefaultMaximumValue() : maxValue;
        if (this.maxValue < 0)
            throw new IllegalArgumentException("Parameter 'maxValue' cannot be a negative number");

        //Minimum cannot be greater than the maximum value.
        if (this.minValue > this.maxValue)
            throw new IllegalArgumentException("Parameter 'minValue' cannot be > than parameter 'maxValue'!");

        //Set default value to ((Max - Min) / 2) if not provided.
        this.defaultValue = defaultValue;
        if (defaultValue == null) {
            this.setDefaultValue((this.getMaxValue() - this.getMinValue()) / 2);
        }
    }

    /// Getter/setter ///

    @Override
    public String getTimingName() {
        return timingName;
    }

    @Override
    public void setTimingName(String timingName) {
        this.timingName = timingName;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int getMinValue() {
        return minValue;
    }

    @Override
    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    @Override
    public int getDefaultValue() {
        return defaultValue;
    }

    @Override
    public void setDefaultValue(int defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public int getMaxValue() {
        return maxValue;
    }

    @Override
    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    /// Calculation methods ///

    /**
     * @param memorySpeedMHz The DDR speed in MT/s
     * @return the clock cycle time
     */
/*
    @Override
    //TODO validate the math on this
    public double calculateCycleTimeAtClockSpeed(long memorySpeedMHz) {
        if (memorySpeedMHz < 0)
            throw new IllegalArgumentException("Parameter 'memorySpeedMHz' must be a positive integer!");

        //Return calculated cycle time value for the current timing.
        return ((double) this.getCurrentValue()) / ((double) (memorySpeedMHz * 1000000000L));  //TODO move to constant.
    }*/


    //TODO validate the math on this
/*TODO
@Override
   public double calculateTimingAtClockSpeed(long sourceSpeedMHz, long targetSpeedMHz) {
        if (sourceSpeedMHz < 0)
            throw new IllegalArgumentException("Parameter 'sourceSpeedMHz' must be a positive integer!");
        if (targetSpeedMHz < 0)
            throw new IllegalArgumentException("Parameter 'targetSpeedMHz' must be a positive integer!");

        //Return converted memory timing value.  This is a simple calculation of dividing the target speed by the
        //source speed and multiplying that fraction by the current value of the timing, as increases in speed mean
        //a standard RAM timing needs to go UP to match the same amount of time required by that operation.
        //NOTE: The logic has been refactored to do the floating point calculation last to minimize FP math rounding errors.
        return ((targetSpeedMHz * 1000000L) * this.getCurrentValue()) / ((double) (sourceSpeedMHz * 1000000L));
    }*/

    /**
     * @return the default minimum to set when caller doesn't provide one.
     */
    public static int getDefaultMinimumValue() {
        return 0;
    }

    public static int getDefaultMaximumValue() {
        return Integer.MAX_VALUE;
    }
}
