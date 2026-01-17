package com.derekpage.RAMTimingCalculator.data;

/**
 * This interface defines the standard API methods for all RAM Timing objects that the application expects to work with
 * to perform the scaling calculations.
 */
public interface RAMTimingInterface {

    //Getter/setters for the standard attributes.
    String getTimingName();
    void setTimingName(String timingName);
    String getDescription();
    void setDescription(String description);
    int getMinValue();
    void setMinValue(int minValue);
    int getDefaultValue();
    void setDefaultValue(int defaultValue);
    int getMaxValue();
    void setMaxValue(int maxValue);

    //Calculation methods used by the calculator.
//TODO    public double calculateCycleTimeAtClockSpeed(long datarate);
//TODO    public double calculateTimingAtClockSpeed(long sourceSpeedMHz, long targetSpeedMHz);
}
