package com.derekpage.RAMTimingCalculator.img;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * This utility class exists solely for loading the images files used in the various parts of the application. It utilizes
 * the Class.getResource() method of loading files to allow loading them when running the application in JAR format.
 */
public class ImageLoader {

    /**
     * @param fileName - name of the image file to load.
     * @return Image object of the image file if found, null otherwise.
     *
     * This method attempts to read and create an Image object from the specified file, from the application's "img"
     * package.  This works in both loose file and JAR archive form.  Any Exceptions generated in the process are
     * suppressed and null is simply returned in that case.
     *
     * NOTE: this is a rather "bass ackwards" way of doing this, creating an icon object just to get the image. Why
     * does Swing not provide a similar way of just getting the Image itself?
     */
    public static Image loadAppImageFile(String fileName) {
        if (fileName == null || fileName.isEmpty())
            throw new IllegalArgumentException("Parameter 'fileName' is required!");

        //Get the file path via the class loader (required for loading when running from a JAR file).
        //If not found then abort.
        URL filePathURL = ImageLoader.class.getResource(fileName);
        if (filePathURL == null)
            return null;

        //Attempt to build an ImageIcon and return the contained image if found, null otherwise.
        return new ImageIcon(filePathURL).getImage();
    }
}
