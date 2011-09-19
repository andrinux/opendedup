package org.opendedup.util;

import java.io.IOException;

import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PatternLayout;
import org.opendedup.sdfs.Main;

public class SDFSLogger {

	private static Logger log = Logger.getLogger("sdfs");
	static {
		RollingFileAppender app = null;
		try {

			app = new RollingFileAppender(new PatternLayout(
					"%d [%t] %p %c %x - %m%n"), Main.logPath, true);
			app.setMaxBackupIndex(2);
			app.setMaxFileSize("10MB");
		} catch (IOException e) {
			System.out.println("Unable to initialize logger");
			e.printStackTrace();
		}
		BasicConfigurator.configure(app);
		log.setLevel(Level.INFO);
	}

	public static Logger getLog() {
		return log;
	}

	public static void setLevel(int level) {
		if (level == 0)
			log.setLevel(Level.DEBUG);
		else
			log.setLevel(Level.INFO);
	}
}