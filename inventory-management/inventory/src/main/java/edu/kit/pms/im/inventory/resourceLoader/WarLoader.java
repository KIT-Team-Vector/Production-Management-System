package edu.kit.pms.im.inventory.resourceLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

public class WarLoader {
	
	public File getWarFile() {
		// Get the current working directory
        String jarPath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();

        // Navigate to the parent directory
        File parentDirectory = new File(jarPath).getParentFile();

        // Check if the warFiles folder exists in the parent directory
        File warFilesFolder = new File(parentDirectory, "warFiles");
        if (!warFilesFolder.exists() || !warFilesFolder.isDirectory()) {
            throw new IllegalStateException("warFiles folder not found in the parent directory.");
        }

        // Construct the path to the WAR file
        File warFile = new File(warFilesFolder, "rest-service.war");

        // Check if the WAR file exists
        if (!warFile.exists() || !warFile.isFile()) {
            throw new IllegalStateException("rest-service.war file not found in the warFiles folder.");
        }

        return warFile;
    }
}
