package com.derekpage.RAMTimingCalculator.html;

import com.derekpage.RAMTimingCalculator.logging.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * This utility class exists solely for loading the HTML files used in the various tooltips of the application. It utilizes
 * the Class.getResource() method of loading HTML files to allow loading them when running the application in JAR format.
 */
public class HTMLLoader {

    /**
     * @param fileName - name of the HTML file to load.
     * @return the HTML content of the file if found, empty string otherwise.
     *
     * This method attempts to read and return the HTML content of the specified file, from the application's "html"
     * package.  This works in both loose file and JAR archive form.  Any Exceptions generated in the process are
     * suppressed and an empty string is simply returned in that case.
     */
    public static String loadHTMLHelpFile(String fileName) {
        if (fileName == null || fileName.isEmpty())
            throw new IllegalArgumentException("Parameter 'fileName' is required!");

        //Get the file path via the class loader (required for loading when running from a JAR file).
        //If not found then abort.
        URL filePathURL = HTMLLoader.class.getResource(fileName);
        if (filePathURL == null)
            return "";

        //Use the URL getContent() method to acquire an initialized InputStream with the content loaded.
        //Return empty string if it comes back null.
        Object inStream = null;
        try {
            inStream = filePathURL.getContent();
            if (inStream == null)
                return "";

        //If error then just return empty string.
        } catch (IOException e) {
            return "";
        }

        //Sanity check - ensure is an InputStream (should always be the case).
        if (!(inStream instanceof InputStream))
            return "";

        //Get the contents of the HTML file line-by-line until EOF.
        String currLine = null;
        StringBuilder sb = new StringBuilder();
        try {

            //Initialize a BufferedReader from the provided InputStream and read in the char data.
            BufferedReader br = new BufferedReader(new InputStreamReader((InputStream) inStream));
            while ((currLine = br.readLine()) != null) {
                sb.append(currLine);
            }

        //Suppress any exceptions.
        } catch (IOException e) {
            Logger.warn("IOException while loading '" + fileName + "' - " + e.getMessage());
        }

        //Return combined contents if loaded, empty string otherwise.
        return (sb.length() > 0) ? sb.toString() : "";
    }
}
