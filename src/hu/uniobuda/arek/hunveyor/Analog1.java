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
	private Logger logger;
	static {
		instance = new Analog1();
	}

	public static Analog1 getInstance() {
		if (instance == null) {
			instance = new Analog1();
		}
		return instance;
	}

	private Analog1() {
		logger = LoggerFactory.getLogger(Analog1.class);
		final Properties properties = new Properties();
		
		try {
			properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("res/i2c.properties"));
		} catch (IOException e) {
			logger.error("Could not parse the property file");
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

	private static float ReadI2CData(int address, int data) {

		I2CBus i2cBus;
		try {
			i2cBus = I2CFactory.getInstance(I2CBus.BUS_1);
			I2CDevice temp = i2cBus.getDevice(address);
			temp.write((byte) data);
			// unsigned char
			byte[] buffer = new byte[2];
			boolean read_done = true;
			do {

				try {
					temp.read(buffer, 0, 2);
					read_done = true;
				} catch (Exception e) {
					read_done = false;
				}
			} while (!read_done);
			Thread.sleep(50);
			int val = ((buffer[1] << 8) & 0x0000ff00) | (buffer[0] & 0x000000ff);
			return (float) val / 1024;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return 0xFF;
		} catch (InterruptedException e2) {
			e2.printStackTrace();
			return 0xFA;
		}

	}

	public float ReadNoise() {
		return ReadI2CData(analog1aI2cAddress, noiseCommand);
	}

	public void SwitchDust(boolean status) {
		if (status) {
			ReadI2CData(analog1bI2cAddress, dustOnCommand);
		} else {
			ReadI2CData(analog1bI2cAddress, dustOffCommand);
		}
	}

	public void SwitchPressure(boolean status) {
		if (status) {
			ReadI2CData(analog1bI2cAddress, pressureOnCommand);
		} else {
			ReadI2CData(analog1bI2cAddress, pressureOffCommand);
		}
	}

	public void SwitchGas(boolean status) {
		if (status) {
			ReadI2CData(analog1bI2cAddress, gasOnCommand);
		} else {
			ReadI2CData(analog1bI2cAddress, gasOffCommand);
		}
	}

	public float ReadLight() {
		return ReadI2CData(analog1aI2cAddress, readLightCommand);
	}

	public float ReadGas() {
		return ReadI2CData(analog1bI2cAddress, readGasCommand);
	}

	public float readPressure() {
		return ReadI2CData(analog1bI2cAddress, readPressureCommand);
	}

	public float readHumidity() {
		return ReadI2CData(analog1bI2cAddress, readHumidityCommand);
	}

	public float readUV() {
		return ReadI2CDataAverage(analog1aI2cAddress, readUVCommand);
	}

	public float ReadGreen() {
		return ReadI2CDataAverage(analog1aI2cAddress, readGreenCommand);
	}

	public float ReadYellow() {
		return ReadI2CDataAverage(analog1aI2cAddress, readYellowCommand);
	}

	public float ReadRed() {
		return ReadI2CDataAverage(analog1aI2cAddress, readRedCommand);
	}

	private static float ReadI2CDataAverage(int address, int command) {
		float[] values = new float[40];

		for (int i = 0; i < 40; i++) {
			values[i] = ReadI2CData(address, command);
		}
		float min = values[0], max = values[0];
		for (int i = 0; i < 40; i++) {
			if (min > values[i])
				min = values[i];
			if (max < values[i])
				max = values[i];
		}
		return (max - min) / 2;

	}
}
