package hu.uniobuda.arek.hunveyor.propertyreader;

import hu.uniobuda.arek.hunveyor.HunLogger;

import java.io.IOException;
import java.util.Properties;

public class HunveyorProperty {

	static HunveyorProperty instance = null;
	private static Properties properties = null;
	static {
		instance = new HunveyorProperty();
		properties = new Properties();
		try {
			properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("res/i2c.properties"));
		} catch (IOException e) {
			HunLogger.logger.error("Could not parse the property file, ioerror.");
		}
	}

	public static String getPropertyWithFormat(String format, int key) {
		return getProperty(String.format(format, key));
	}
	public static String getProperty(String key) {
		return properties.getProperty(key,"");
	}

	private HunveyorProperty() {
		super();
	}

	public HunveyorProperty getInstance() {
		if (instance == null) {
			instance = new HunveyorProperty();
		}
		return instance;
	}

}
