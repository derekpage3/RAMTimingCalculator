package com.derekpage.RAMTimingCalculator.data;

/**
 * This Structure is used by the calculator to define the list of timings that will be displayed, as well as its
 * minimum, maximum and default values.
 */
public class TimingDefinitionStruct {

    //For each timing we capture its name and a description, the minimum/maximum allowed value, and the default
    //value to set when loading the calculator source values.
    private String timingName;
    private String description;
    private int minValue = -1;  //negative indicates no default.
    private int defaultValue = -1;  //negative indicates no default.
    private int maxValue = -1;  //negative indicates no default.

    public TimingDefinitionStruct(String timingName, String description, int minValue, int defaultValue, int maxValue) {
        this.timingName = timingName;
        this.description = description;
        this.minValue = minValue;
        this.defaultValue = defaultValue;
        this.maxValue = maxValue;
    }

    //Getter/setter

    public String getTimingName() {
        return timingName;
    }

    public void setTimingName(String timingName) {
        this.timingName = timingName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(int defaultValue) {
        this.defaultValue = defaultValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }
}
