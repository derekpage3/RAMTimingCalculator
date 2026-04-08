package com.derekpage.RAMTimingCalculator.config;

import com.derekpage.RAMTimingCalculator.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

/**
 * This singleton class is used by the app to capture the app configuration, reading from the file on open and
 * updating any changes upon close.
 */
public class AppConfig {

    //Relative file path to the app config file.
    private static final String CONFIG_FILE_PATH = "./tConfig.json";

    //Key names in the JSON config file.
    static final String JSON_CONF_MEMORY_TYPES_KEY = "memoryTypes";
    static final String JSON_CONF_DEFAULT_MEMORY_TYPE_KEY = "defaultMemoryTypeSelection";

    //singleton instance of this class.
    private static AppConfig _instance = null;

    //Memory types configuration.
    private MemoryTypeDefinition[] memoryTypes;
    private MemoryTypeDefinition defaultMemoryTypeSelection;

    //File handle to the config file. Used for saving any changes in the app back to the config file.
    private File confFileHandle = null;

    /**
     * @return singleton instance of this class.
     */
    public static synchronized AppConfig getInstance() {
        if (AppConfig._instance == null) {
            AppConfig._instance = new AppConfig();
        }
        return AppConfig._instance;
    }

    /**
     * Singleton
     */
    private AppConfig() {
        this.loadFromConfigFile();
    }

    /// Getters ///

    public MemoryTypeDefinition[] getMemoryTypes() {
        return memoryTypes;
    }

    public MemoryTypeDefinition getDefaultMemoryTypeSelection() {
        return defaultMemoryTypeSelection;
    }

    /**
     * This synchronized method initializes the configuration from the config file.
     */
    private synchronized void loadFromConfigFile() {

        //TODO handle creating file when it doesn't exist.

        //Abort if config file not found.
        File confFile = new File(AppConfig.CONFIG_FILE_PATH);
        if (!confFile.exists())
            throw new RuntimeException("Config file '" + AppConfig.CONFIG_FILE_PATH + "' not found!");

        //Error if not file
        if (!confFile.isFile())
            throw new RuntimeException("'" + AppConfig.CONFIG_FILE_PATH + "' is not a file!");

        //Error if not readable.
        if (!confFile.canRead())
            throw new RuntimeException("Config file '" + AppConfig.CONFIG_FILE_PATH + "' is not readable!");

        //Capture validated file for future writing.
        this.confFileHandle = confFile;

        //Get file contents
        StringBuilder sb = new StringBuilder();
        BufferedReader confReader = null;
        Logger.info("Reading app config file.");
        try {
            String currLine = null;
            confReader = new BufferedReader(new FileReader(confFile));
            while ((currLine = confReader.readLine()) != null) {
                sb.append(currLine);
            }
        } catch (IOException e) {
            throw new RuntimeException("CONFIG FILE READ FAILED!", e);
        } finally {

            //Close reader if we successfully opened it.
            try {
                if (confReader != null)
                    confReader.close();
            } catch (IOException e) {
                Logger.warn("Failed to close config file");
            }
        }
        Logger.info("Config file read complete. Content size: " + sb.length());

        //Error if no content.
        if (sb.length() < 1) {
            throw new RuntimeException("Config file '" + AppConfig.CONFIG_FILE_PATH + "' has no content!");
        }
        if (Logger.isLogLevelTraceEnabled()) {
            Logger.trace("CONFIG FILE CONTENTS: " + sb.toString());
        }

        //Build JSON object from conf file.
        JSONObject confJSON = null;
        try {
            if (Logger.isLogLevelDebugEnabled())
                Logger.debug("Parsing config file contents to JSON");
            confJSON = new JSONObject(sb.toString());
        } catch (JSONException e) {
            throw new RuntimeException("Config file content is not a valid JSON object!", e);
        }
        if (Logger.isLogLevelDebugEnabled())
            Logger.debug("JSON parsing completed");

        /// If we get here then we have a JSON object in the conf file; parse and validate it. ///

        //1. Get default memory selection if present.
        Logger.info("Reading default memory type selection.");
        String defaultMemoryTypeSelectionString = null;
        try {
            defaultMemoryTypeSelectionString = confJSON.getString(JSON_CONF_DEFAULT_MEMORY_TYPE_KEY);
        } catch (JSONException e) {
            throw new RuntimeException("JSON error while reading default memory type!: '" + e.getMessage() + "'");  //TODO change to a proper exception class.
        }
        Logger.info("Default memory type selection value is '" + defaultMemoryTypeSelectionString + "'");

        //2. Get memory types defined in the conf file. Error if empty.
        Logger.info("Retrieving memory types definitions.");
        JSONArray memoryTypesJSON = null;
        try {
            memoryTypesJSON = confJSON.getJSONArray(JSON_CONF_MEMORY_TYPES_KEY);
        } catch (JSONException e) {
            throw new RuntimeException("JSON error while reading memory types!: '" + e.getMessage() + "'");  //TODO change to a proper exception class.
        }
        if (memoryTypesJSON == null || memoryTypesJSON.isEmpty()) {
            throw new RuntimeException("Config file has empty '" + JSON_CONF_MEMORY_TYPES_KEY + "' element.");  //TODO change to a proper exception class.
        }
        Logger.info("Number of memoryType configurations found: " + memoryTypesJSON.length());

        //Process each memory type defined in the config file.
        Logger.info("Processing memory type configurations.");
        MemoryTypeDefinition[] currentMemoryTypeConfigs = new MemoryTypeDefinition[memoryTypesJSON.length()];
        for (int i = 0; i < memoryTypesJSON.length(); i++) {

            //Get current memory type.
            JSONObject memoryTypeConfigJSON = null;
            try {
                Logger.info("Getting memoryType[" + i + "]");
                memoryTypeConfigJSON = memoryTypesJSON.getJSONObject(i);
                if (Logger.isLogLevelDebugEnabled())
                    Logger.debug("memoryType[" + i + "] JSON = " + memoryTypeConfigJSON);
            } catch (JSONException e) {
                throw new RuntimeException("Error while processing memoryType[" + i + "]: '" + e.getMessage() + "'");  //TODO change to a proper exception class.
            }

            //Build MemoryTypeDefintion for the current memory type.
            MemoryTypeDefinition currMemoryType = null;
            try {
                Logger.info("Building MemoryTypeDefinition for memoryType[" + i + "]");
                currMemoryType = MemoryTypeDefinition.fromJSON(memoryTypeConfigJSON);
            } catch (RuntimeException e) {
                throw new RuntimeException("Error while processing memoryType[" + i + "]: '" + e.getMessage() + "'");  //TODO change to a proper exception class.
            }
            Logger.info("Finished building MemoryTypeDefinition for memoryType[" + i + "]");
            currentMemoryTypeConfigs[i] = currMemoryType;

            //Set current MemoryTypeDefintion as the default one if configured as such in the config file.
            if (defaultMemoryTypeSelectionString != null && defaultMemoryTypeSelectionString.equals(currMemoryType.getName())) {
                Logger.info("Setting MemoryTypeConfig '" + currMemoryType.getName() + "' as default, per config file.");
                this.defaultMemoryTypeSelection = currMemoryType;
            }
        }
        if (Logger.isLogLevelDebugEnabled())
            Logger.debug("Finished processing memory type configurations.");

        //Capture all configured memory types into the Config object.
        this.memoryTypes = currentMemoryTypeConfigs;
    }

    /**
     * This synchronized method flushes the current configuration back to the config file.
     */
    public synchronized void saveToConfFile() {
        //TODO implement custom JSONWriter that preserves formatting.

        //Abort if we don't have a handle to the config file.
        if (this.confFileHandle == null) {
            Logger.error("No active handle to config file; aborting save process.");
            return;
        }

        //Abort if the file no longer exists (if we have an active file handle then it existed on start but doesn't now).
        if (!this.confFileHandle.exists()) {
            Logger.warn("Config file '" + AppConfig.CONFIG_FILE_PATH + "' not found; aborting save process.");
            return;
        }

        //Error if not file (this should NEVER happen).
        if (!this.confFileHandle.isFile()) {
            Logger.fatal("'" + AppConfig.CONFIG_FILE_PATH + "' is not a file; aborting save process.");
            return;
        }

        //Error if not editable.
        if (!this.confFileHandle.canWrite()) {
            Logger.error("Config file '" + AppConfig.CONFIG_FILE_PATH + "' is not writeable; aborting save process");
            return;
        }

        /// If we get here then the file can be written; build JSON payload to write to file. ///

        //Build top-level config file object.
        JSONObject conf = new JSONObject();
        if (this.getDefaultMemoryTypeSelection() != null)
            conf.put(JSON_CONF_DEFAULT_MEMORY_TYPE_KEY, this.getDefaultMemoryTypeSelection().getName());
        else
            conf.put(JSON_CONF_DEFAULT_MEMORY_TYPE_KEY, "");

        //Initialize memory types array.
        JSONObject[] memoryTypes = new JSONObject[this.getMemoryTypes().length];
        conf.put(JSON_CONF_MEMORY_TYPES_KEY, memoryTypes);

        //Re-add memory type configurations from current config.
        for (int i = 0; i < this.getMemoryTypes().length; i++) {
            memoryTypes[i] = this.getMemoryTypes()[i].toJSON();
        }
        JSONArray jsa = new JSONArray();
        jsa.putAll(memoryTypes);
        conf.put(JSON_CONF_MEMORY_TYPES_KEY, jsa);
        if (Logger.isLogLevelTraceEnabled())
            Logger.trace("JSON result: " + conf.toString());

        //Open config file for writing.
        BufferedWriter bf = null;
        try {
            bf = new BufferedWriter(new FileWriter(this.confFileHandle));
        } catch (IOException e) {
            Logger.error("IOException while opening config file '" + e.getMessage() + "'; current config will not be saved.");
            return;
        }

        //Update config in the config file.
        try {
            bf.write(conf.toString());
            bf.flush();
        } catch (IOException e) {
            Logger.error("IOException while writing updated config to file '" + e.getMessage() + "'");
        }

        //Close conf file out.
        try {
            bf.close();
        } catch (IOException e) {
            Logger.warn("IOException while closing config file '" + e.getMessage() + "'");
        }
    }
}
