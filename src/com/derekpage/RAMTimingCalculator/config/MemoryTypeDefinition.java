package com.derekpage.RAMTimingCalculator.config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class represents the configuration options for a memory type, as defined in the app config and used for the
 * configuration of the application.
 */
public class MemoryTypeDefinition {

    //Key names in the JSON config file for memory types.
    static final String JSON_CONF_MEMORY_TYPE_NAME_KEY = "name";
    static final String JSON_CONF_MEMORY_TYPE_DEFAULT_SOURCE_SPEED_KEY = "defaultSourceSpeed";
    static final String JSON_CONF_MEMORY_TYPE_DEFAULT_TARGET_SPEED_KEY = "defaultTargetspeed";
    static final String JSON_CONF_MEMORY_TYPE_TIMINGS_ARRAY_KEY = "timings";

    //Simple name that defines the represented memory technology (i.e., "DDR5").
    private String name;

    //The default speed selection for the "source" speed when selecting this memory technology in the app.
    private Long defaultSourceSpeed;

    //The default speed selection for the "target" speed when selecting this memory technology in the app.
    private Long defaultTargetSpeed;

    //Array of the individual timings applicable to this memory type.
    private MemoryTimingDefinition[] memoryTimings;

    /**
     * @param name The "name" value as provided in the app config file.
     * @param defaultSourceSpeed the "defaultSourceSpeed" value as provided in the app config file.
     * @param defaultTargetSpeed the "defaultTargetSpeed" value as provided in the app config file.
     *
     * The sole initializer for this class - all values must be specified.
     */
    private MemoryTypeDefinition(String name, Long defaultSourceSpeed, Long defaultTargetSpeed) {
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("Parameter 'name' is required!");
        }
        if (defaultSourceSpeed == null) {
            throw new IllegalArgumentException("Parameter 'defaultSourceSpeed' is required!");
        } else if (defaultSourceSpeed < 0L) {
            throw new IllegalArgumentException("Parameter 'defaultSourceSpeed' value (" + defaultSourceSpeed + ") less than 0");
        }
        if (defaultTargetSpeed == null) {
            throw new IllegalArgumentException("Parameter 'defaultTargetSpeed' is required!");
        } else if (defaultTargetSpeed < 0L) {
            throw new IllegalArgumentException("Parameter 'defaultTargetSpeed' value (" + defaultTargetSpeed + ") less than 0");
        }

        this.name = name;
        this.defaultSourceSpeed = defaultSourceSpeed;
        this.defaultTargetSpeed = defaultTargetSpeed;
    }

    /// getter/setter ///

    String getName() {
        return name;
    }

    private Long getDefaultSourceSpeed() {
        return defaultSourceSpeed;
    }

    private Long getDefaultTargetSpeed() {
        return defaultTargetSpeed;
    }

    private MemoryTimingDefinition[] getMemoryTimings() {
        return memoryTimings;
    }

    private void setMemoryTimings(MemoryTimingDefinition[] memoryTimings) {
        this.memoryTimings = memoryTimings;
    }

    JSONObject toJSON() {
        JSONObject jso = new JSONObject();
        jso.put(JSON_CONF_MEMORY_TYPE_NAME_KEY, this.getName());
        if (this.getDefaultSourceSpeed() != null)
            jso.put(JSON_CONF_MEMORY_TYPE_DEFAULT_SOURCE_SPEED_KEY, this.getDefaultSourceSpeed());
        if (this.getDefaultTargetSpeed() != null)
            jso.put(JSON_CONF_MEMORY_TYPE_DEFAULT_TARGET_SPEED_KEY, this.getDefaultTargetSpeed());

        //Initialize memory timings array.
        JSONObject[] jsa = new JSONObject[this.getMemoryTimings().length];
        for (int i = 0; i < this.getMemoryTimings().length; i++)
            jsa[i] = this.getMemoryTimings()[i].toJSON();
        jso.put(JSON_CONF_MEMORY_TYPE_TIMINGS_ARRAY_KEY, jsa);
        return jso;
    }

    /**
     * @param jso the JSONObject containing the attributes to initialize a MemoryTypeDefinition from.
     * @return MemoryTypeDefinition
     *
     * Convenience method for validating a JSONObject from the app config file and initializing a MemoryTypeDefinition
     * object from it.
     */
    static MemoryTypeDefinition fromJSON(JSONObject jso) {
        if (jso == null)
            throw new RuntimeException("Parameter 'jso' is required!");

        //Get name for the provided memory type.
        if (!jso.has(JSON_CONF_MEMORY_TYPE_NAME_KEY))
            throw new RuntimeException("Attribute '" + JSON_CONF_MEMORY_TYPE_NAME_KEY + "' missing!");
        String currMemoryTypeStr = jso.getString(JSON_CONF_MEMORY_TYPE_NAME_KEY);  //TODO exception handling
        if (currMemoryTypeStr.trim().isEmpty()) {
            throw new RuntimeException("Attribute '" + JSON_CONF_MEMORY_TYPE_NAME_KEY + "' is nil!");
        }

        //Get default source speed if defined.
        Long currDefaultSourceSpeed = null;
        if (jso.has(JSON_CONF_MEMORY_TYPE_DEFAULT_SOURCE_SPEED_KEY)) {
            currDefaultSourceSpeed = jso.getLong(JSON_CONF_MEMORY_TYPE_DEFAULT_SOURCE_SPEED_KEY);
            if (currDefaultSourceSpeed < 0L) {
                throw new RuntimeException("Attribute '" + JSON_CONF_MEMORY_TYPE_DEFAULT_SOURCE_SPEED_KEY
                        + "' value less than zero (" + currDefaultSourceSpeed + ")");
            }
        }

        //Get default target speed if defined.
        Long currDefaultTargetSpeed = null;
        if (jso.has(JSON_CONF_MEMORY_TYPE_DEFAULT_TARGET_SPEED_KEY)) {
            currDefaultTargetSpeed = jso.getLong(JSON_CONF_MEMORY_TYPE_DEFAULT_TARGET_SPEED_KEY);
            if (currDefaultTargetSpeed < 0L) {
                throw new RuntimeException("Attribute '" + JSON_CONF_MEMORY_TYPE_DEFAULT_TARGET_SPEED_KEY
                        + "' value less than zero (" + currDefaultTargetSpeed + ")");
            }
        }

        //Get the individual timings for the current memory type.
        if (!jso.has(JSON_CONF_MEMORY_TYPE_TIMINGS_ARRAY_KEY))
            throw new RuntimeException("Attribute '" + JSON_CONF_MEMORY_TYPE_TIMINGS_ARRAY_KEY + "' missing!");

        //Build new MemoryTypeDefinition
        MemoryTypeDefinition mtd = new MemoryTypeDefinition(currMemoryTypeStr, currDefaultSourceSpeed, currDefaultTargetSpeed);

        /// Processing memory timings. ///

        //Get the JSON array for the timings.
        JSONArray timingsArrayJSON = null;
        try {
            timingsArrayJSON = jso.getJSONArray(JSON_CONF_MEMORY_TYPE_TIMINGS_ARRAY_KEY);
        } catch (JSONException e) {
            throw new RuntimeException("Error while parsing the '" + JSON_CONF_MEMORY_TYPE_TIMINGS_ARRAY_KEY
                    + "' attribute: '" + e.getMessage() + "'", e);  //TODO change to a proper exception class.
        }

        //Process each timing defined for this memory type.
        MemoryTimingDefinition[] currMemoryTimings = new MemoryTimingDefinition[timingsArrayJSON.length()];
        for (int i = 0; i < timingsArrayJSON.length(); i++) {

            JSONObject jsot = null;
            try {
                jsot = timingsArrayJSON.getJSONObject(i);
            } catch (JSONException e) {
                throw new RuntimeException("Error while getting '" + JSON_CONF_MEMORY_TYPE_TIMINGS_ARRAY_KEY
                        + "'[" + i + "]: " + e.getMessage() + "'", e);  //TODO change to a proper exception class.
            }

            //Build MemoryTimingDefinition for the current config object.
            currMemoryTimings[i] = MemoryTimingDefinition.fromJSON(jsot);
        }

        //Add timings to the MemoryTypeDefinition and return
        mtd.setMemoryTimings(currMemoryTimings);
        return mtd;
    }
}
