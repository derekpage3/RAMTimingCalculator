package com.derekpage.RAMTimingCalculator.config;

import com.derekpage.RAMTimingCalculator.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This inner class represents the configuration options for a single memory timing associated with the parent
 * memory type definition, as defined in the app config and used for the configuration of the application.
 */
public class MemoryTimingDefinition {

    //Keys for the attribute names in the config JSON for a memory timing definition
    private static final String JSON_CONF_MEMORY_TIMING_NAME_KEY = "name";
    private static final String JSON_CONF_MEMORY_TIMING_DESCR_KEY = "description";
    private static final String JSON_CONF_MEMORY_TIMING_MIN_VAL_KEY = "min";
    private static final String JSON_CONF_MEMORY_TIMING_DEFAULT_VAL_KEY = "defaultValue";
    private static final String JSON_CONF_MEMORY_TIMING_MAX_VAL_KEY = "max";
    private static final String JSON_CONF_MEMORY_TIMING_STEP_INTERVAL_KEY = "stepInterval";

    //Simple name that defines the memory timing in the scope of the parent memory technology (i.e., "tCL").
    private String name;

    //Description of the memory timing (used for hover text)
    private String description;

    //Lowest value this timing can be set to.
    private Long minValue;

    //The default value to set for this timing if the default source speed of the parent memory type is used.
    private Long defaultValue;

    //Maximum value this timing can be set to.
    private Long maxValue;

    //The default step interval to use on the Spinner for this memory timing. Usually 1, but may differ for some
    //timings that have a large min/max range (ex. tREFI).
    private Long stepInterval;

    /**
     * @param name         The "name" value as provided in the app config file for this timing and the parent memory technology.
     * @param description  Description of the memory timing (used for hover text)
     * @param minValue     Lowest value this timing can be set to.
     * @param defaultValue The default value to set for this timing if the default source speed of the parent memory type is used.
     * @param maxValue     Maximum value this timing can be set to.
     * @param stepInterval //The default step interval to use on the Spinner for this memory timing.
     */
    private MemoryTimingDefinition(String name, String description, Long minValue, Long defaultValue, Long maxValue, Long stepInterval) {
        this.name = name;
        this.description = description;
        this.minValue = minValue;
        this.defaultValue = defaultValue;
        this.maxValue = maxValue;
        this.stepInterval = stepInterval;
    }

    /// Getter/setter ///

    private String getName() {
        return name;
    }

    private void setName(String name) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Parameter 'name' can't be nil!");
        this.name = name;
    }

    private String getDescription() {
        return description;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    private Long getMinValue() {
        return minValue;
    }

    private void setMinValue(Long minValue) {
        if (minValue == null)
            throw new IllegalArgumentException("Parameter 'minValue' can't be null!");
        if (minValue < 0L)
            throw new IllegalArgumentException("Parameter 'minValue' can't be less than 0!");
        this.minValue = minValue;
    }

    private Long getDefaultValue() {
        return defaultValue;
    }

    private void setDefaultValue(Long defaultValue) {
        if (defaultValue != null && (defaultValue < 0L))
            throw new IllegalArgumentException("Parameter 'defaultValue' can't be less than 0!");
        this.defaultValue = defaultValue;
    }

    private Long getMaxValue() {
        return maxValue;
    }

    private void setMaxValue(Long maxValue) {
        if (maxValue == null)
            throw new IllegalArgumentException("Parameter 'maxValue' can't be null!");
        if (maxValue < 0L)
            throw new IllegalArgumentException("Parameter 'maxValue' can't be less than 0!");
        this.maxValue = maxValue;
    }

    private Long getStepInterval() {
        return stepInterval;
    }

    private void setStepInterval(Long stepInterval) {

        //Defaults to 1 if not set.
        if (stepInterval == null)
            this.stepInterval = 1L;

        //Otherwise, validate before allowing change.
        else {
            if (stepInterval < 0L)
                throw new IllegalArgumentException("Parameter 'stepInterval' can't be less than 0!");
            this.stepInterval = stepInterval;
        }
    }

    /**
     * @return JSONObject
     *
     * Convenience method for converting a MemoryTimingDefintion object into its JSON equivalent.
     * Used for saving settings back to the app config file.
     */
    JSONObject toJSON() {
        JSONObject jso = new JSONObject();
        jso.put(JSON_CONF_MEMORY_TIMING_NAME_KEY, this.getName());
        if (this.getDescription() != null)
            jso.put(JSON_CONF_MEMORY_TIMING_DESCR_KEY, this.getDescription());
        jso.put(JSON_CONF_MEMORY_TIMING_MIN_VAL_KEY, this.getMinValue());
        if (this.getDefaultValue() != null)
            jso.put(JSON_CONF_MEMORY_TIMING_DEFAULT_VAL_KEY, this.getDefaultValue());
        jso.put(JSON_CONF_MEMORY_TIMING_MAX_VAL_KEY, this.getMaxValue());
        if (this.getStepInterval() != null)
            jso.put(JSON_CONF_MEMORY_TIMING_STEP_INTERVAL_KEY, this.getStepInterval());
        return jso;
    }

    /**
     * @param jso the JSONObject containing the attributes to initialize the MemoryTimingDefinition from.
     * @return MemoryTimingDefinition
     *
     * Convenience method for validating a JSONObject from the app config file for a specific memory timing on a
     * memory type configuration, and initializing a MemoryTimingDefinition object from it.
     */
    static MemoryTimingDefinition fromJSON(JSONObject jso) {
        if (jso == null)
            throw new RuntimeException("Parameter 'jso' is required!");

        //Get name for the provided memory type.
        if (!jso.has(JSON_CONF_MEMORY_TIMING_NAME_KEY))
            throw new RuntimeException("Attribute '" + JSON_CONF_MEMORY_TIMING_NAME_KEY + "' missing!");
        String currMemoryTimingName = jso.getString(JSON_CONF_MEMORY_TIMING_NAME_KEY);  //TODO exception handling
        if (currMemoryTimingName.trim().isEmpty()) {
            throw new RuntimeException("Attribute '" + JSON_CONF_MEMORY_TIMING_NAME_KEY + "' is nil!");
        }

        //Get description if provided.
        String descr = null;
        if (jso.has(JSON_CONF_MEMORY_TIMING_DESCR_KEY)) {
            try {
                descr = jso.getString(JSON_CONF_MEMORY_TIMING_DESCR_KEY);
            } catch (JSONException e) {
                throw new RuntimeException("JSON error while parsing timing description: '" + e.getMessage() + "'");  //TODO change to a proper exception class.
            }
        }

        //Get minimum value. Error if < 0.
        Long minVal = null;
        try {
            minVal = jso.getLong(JSON_CONF_MEMORY_TIMING_MIN_VAL_KEY);
        } catch (JSONException e) {
            throw new RuntimeException("JSON error while parsing timing min value: '" + e.getMessage() + "'");  //TODO change to a proper exception class.
        }
        if (minVal < 0)
            throw new RuntimeException("Parameter 'min' value (" + minVal + ") less than 0");

        //Get maximum value. Error if < 0.
        Long maxVal = null;
        try {
            maxVal = jso.getLong(JSON_CONF_MEMORY_TIMING_MAX_VAL_KEY);
        } catch (JSONException e) {
            throw new RuntimeException("JSON error while parsing timing max value: '" + e.getMessage() + "'");  //TODO change to a proper exception class.
        }
        if (maxVal < 0)
            throw new RuntimeException("Parameter 'max' value (" + maxVal + ") less than 0");

        //Error if min > max
        if (minVal > maxVal)
            throw new RuntimeException("Parameter 'max' value (" + maxVal + ") greater than 'min' value (" + minVal + ")");

        //Get default value if set. Error if < 0.
        Long defVal = null;
        if (jso.has(JSON_CONF_MEMORY_TIMING_DEFAULT_VAL_KEY)) {
            try {
                defVal = jso.getLong(JSON_CONF_MEMORY_TIMING_DEFAULT_VAL_KEY);
            } catch (JSONException e) {
                throw new RuntimeException("JSON error while parsing default value: '" + e.getMessage() + "'");  //TODO change to a proper exception class.
            }
            if (defVal < 0)
                throw new RuntimeException("Parameter 'defaultValue' value (" + defVal + ") less than 0");
        }

        //Get Step Interval if defined (default: 1)
        Long stepInterval = 1L;
        if (jso.has(JSON_CONF_MEMORY_TIMING_STEP_INTERVAL_KEY)) {
            try {
                stepInterval = jso.getLong(JSON_CONF_MEMORY_TIMING_STEP_INTERVAL_KEY);
            } catch (JSONException e) {
                throw new RuntimeException("JSON error while parsing stepInterval value: '" + e.getMessage() + "'");  //TODO change to a proper exception class.
            }
            if (stepInterval < 1)
                throw new RuntimeException("Parameter 'stepInterval' value (" + defVal + ") less than 1");
        }

        //If we get here then validation passed; build and return a MemoryTimingDefinition.
        Logger.debug("name: " + currMemoryTimingName
                + ", description: " + descr
                + ", min: " + minVal
                + ", defaultValue: " + defVal
                + ", max: " + maxVal
                + ", stepInterval: " + stepInterval);
        return new MemoryTimingDefinition(currMemoryTimingName, descr, minVal, defVal, maxVal, stepInterval);
    }
}