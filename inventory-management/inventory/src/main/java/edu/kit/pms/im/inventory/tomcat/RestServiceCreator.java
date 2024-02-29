package edu.kit.pms.im.inventory.tomcat;


import java.io.IOException;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.scan.StandardJarScanner;
import edu.kit.pms.im.inventory.resourceLoader.WarLoader;

public class RestServiceCreator {
	private static final String BASE_DIR = "temp";
	private static final String APP_BASE = ".";
	private static final String CONTEXT_PATH = "rest-service"; 

	public Runnable createRestService(ClassLoader mainClassLoader) {
		return new Runnable() {
			@Override
			public void run() {
				try {
					// Create an instance of Tomcat
					Tomcat tomcat = new Tomcat();
					// Set the port number
					tomcat.setPort(8080);
					// specify workspace of tomcat instance
					tomcat.setBaseDir(BASE_DIR);
					// root is set to host
					tomcat.getHost().setAppBase(APP_BASE);
					tomcat.getConnector();
					// host url + contextPath => path to service
					String contextPath = CONTEXT_PATH;
					// load web service war
					WarLoader loader = new WarLoader();
					// feed tomcat server with jakarta web service and context path
					Context ctx = tomcat.addWebapp(contextPath, loader.getWarFile().getCanonicalPath());
					// add dependecies of main application to jakarta web service with main class
					// loader (shared dependencies)
					ctx.setParentClassLoader(mainClassLoader);
					// disable all scanns, all tomcat dependecies are already included in the
					// classpath of the jar
					StandardJarScanner jarScanner = (StandardJarScanner) ctx.getJarScanner();
					jarScanner.setScanAllDirectories(false);
					jarScanner.setScanClassPath(false);

					// Graceful shut down
					Runtime.getRuntime().addShutdownHook(new Thread(() -> {
						try {
							tomcat.stop();
						} catch (LifecycleException e) {
							e.printStackTrace();
						}
					}));
					// Start Tomcat
					tomcat.start();
					tomcat.getServer().await();
				} catch (LifecycleException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		};
	}
}
