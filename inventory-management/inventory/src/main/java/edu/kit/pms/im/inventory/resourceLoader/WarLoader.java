package edu.kit.pms.im.inventory.resourceLoader;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class WarLoader {
	
	public File getWarFile() {
		//ClassName>.class.getClassLoader()
		ClassLoader classLoader = getClass().getClassLoader();
		URL warURL = null;
		try {
			warURL = WarLoader.class.getResource("rest-service.war");
		if (warURL == null) {
			throw new IllegalArgumentException("file not found!");
		} else {
			return new File(warURL.toURI());
		}
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
