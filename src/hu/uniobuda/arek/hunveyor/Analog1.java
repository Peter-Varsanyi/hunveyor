package hu.uniobuda.arek.hunveyor;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by gwelican on 2014.02.03..
 */
public class Analog1 {

	private final int readRedCommand;
	private final int readYellowCommand;
	private final int readGreenCommand;
	private final int readUVCommand;
	private final int readHumidityCommand;
	private final int readPressureCommand;
	private final int readGasCommand;
	private final int readLightCommand;
	private final int gasOffCommand;
	private final int gasOnCommand;
	private final int pressureOffCommand;
	private final int pressureOnCommand;
	private final int dustOffCommand;
	private final int dustOnCommand;
	private final int noiseCommand;
	private final int analog1bI2cAddress;
	private final int analog1aI2cAddress;
	private static Analog1 instance = null;

	public static Analog1 getInstance() {
		if (instance == null) {
			instance = new Analog1();
		}
		return instance;
	}

	private Analog1() {
		
		final Properties properties = new Properties();

		try {
			properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("res/i2c.properties"));
		} catch (IOException e) {
			HunLogger.logger.error("Could not parse the property file");
		}
		analog1aI2cAddress = Integer.parseInt(properties.getProperty("analog1a.address"));
		analog1bI2cAddress = Integer.parseInt(properties.getProperty("analog1b.address"));

		dustOffCommand = Integer.parseInt(properties.getProperty("analog1.dust.off"));
		dustOnCommand = Integer.parseInt(properties.getProperty("analog1.dust.on"));
		pressureOffCommand = Integer.parseInt(properties.getProperty("analog1.pressure.off"));
		pressureOnCommand = Integer.parseInt(properties.getProperty("analog1.pressure.on"));
		gasOffCommand = Integer.parseInt(properties.getProperty("analog1.gas.off"));
		gasOnCommand = Integer.parseInt(properties.getProperty("analog1.gas.on"));

		noiseCommand = Integer.parseInt(properties.getProperty("analog1.getnoise"));
		readRedCommand = Integer.parseInt(properties.getProperty("analog1.getred"));
		readYellowCommand = Integer.parseInt(properties.getProperty("analog1.getyellow"));
		readGreenCommand = Integer.parseInt(properties.getProperty("analog1.getgreen"));
		readUVCommand = Integer.parseInt(properties.getProperty("analog1.getuv"));
		readHumidityCommand = Integer.parseInt(properties.getProperty("analog1.gethumidity"));
		readPressureCommand = Integer.parseInt(properties.getProperty("analog1.getpressure"));
		readGasCommand = Integer.parseInt(properties.getProperty("analog1.getgas"));
		readLightCommand = Integer.parseInt(properties.getProperty("analog1.getlight"));

	}


	public float readNoise() {
		return HunveyorI2C.ReadI2CData(analog1aI2cAddress, noiseCommand);
	}

	public void setDust(boolean status) {
		if (status) {
			HunveyorI2C.ReadI2CData(analog1bI2cAddress, dustOnCommand);
		} else {
			HunveyorI2C.ReadI2CData(analog1bI2cAddress, dustOffCommand);
		}
	}

	public void setPressure(boolean status) {
		if (status) {
			HunveyorI2C.ReadI2CData(analog1bI2cAddress, pressureOnCommand);
		} else {
			HunveyorI2C.ReadI2CData(analog1bI2cAddress, pressureOffCommand);
		}
	}

	public void setGas(boolean status) {
		if (status) {
			HunveyorI2C.ReadI2CData(analog1bI2cAddress, gasOnCommand);
		} else {
			HunveyorI2C.ReadI2CData(analog1bI2cAddress, gasOffCommand);
		}
	}

	public float readLight() {
		return HunveyorI2C.ReadI2CData(analog1aI2cAddress, readLightCommand);
	}

	public float readGas() {
		return HunveyorI2C.ReadI2CData(analog1bI2cAddress, readGasCommand);
	}

	public float readPressure() {
		return HunveyorI2C.ReadI2CData(analog1bI2cAddress, readPressureCommand);
	}

	public float readHumidity() {
		return HunveyorI2C.ReadI2CData(analog1bI2cAddress, readHumidityCommand);
	}

	public float readUV() {
		return HunveyorI2C.ReadI2CDataAverage(analog1aI2cAddress, readUVCommand);
	}

	public float readGreen() {
		return HunveyorI2C.ReadI2CDataAverage(analog1aI2cAddress, readGreenCommand);
	}

	public float readYellow() {
		return HunveyorI2C.ReadI2CDataAverage(analog1aI2cAddress, readYellowCommand);
	}

	public float readRed() {
		return HunveyorI2C.ReadI2CDataAverage(analog1aI2cAddress, readRedCommand);
	}

}
