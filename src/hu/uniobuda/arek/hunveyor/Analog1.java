package hu.uniobuda.arek.hunveyor;

import hu.uniobuda.arek.hunveyor.propertyreader.HunveyorProperty;

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

		analog1aI2cAddress = Integer.parseInt(HunveyorProperty.getProperty("analog1a.address"));
		analog1bI2cAddress = Integer.parseInt(HunveyorProperty.getProperty("analog1b.address"));

		dustOffCommand = Integer.parseInt(HunveyorProperty.getProperty("analog1.dust.off"));
		dustOnCommand = Integer.parseInt(HunveyorProperty.getProperty("analog1.dust.on"));
		pressureOffCommand = Integer.parseInt(HunveyorProperty.getProperty("analog1.pressure.off"));
		pressureOnCommand = Integer.parseInt(HunveyorProperty.getProperty("analog1.pressure.on"));
		gasOffCommand = Integer.parseInt(HunveyorProperty.getProperty("analog1.gas.off"));
		gasOnCommand = Integer.parseInt(HunveyorProperty.getProperty("analog1.gas.on"));

		noiseCommand = Integer.parseInt(HunveyorProperty.getProperty("analog1.getnoise"));
		readRedCommand = Integer.parseInt(HunveyorProperty.getProperty("analog1.getred"));
		readYellowCommand = Integer.parseInt(HunveyorProperty.getProperty("analog1.getyellow"));
		readGreenCommand = Integer.parseInt(HunveyorProperty.getProperty("analog1.getgreen"));
		readUVCommand = Integer.parseInt(HunveyorProperty.getProperty("analog1.getuv"));
		readHumidityCommand = Integer.parseInt(HunveyorProperty.getProperty("analog1.gethumidity"));
		readPressureCommand = Integer.parseInt(HunveyorProperty.getProperty("analog1.getpressure"));
		readGasCommand = Integer.parseInt(HunveyorProperty.getProperty("analog1.getgas"));
		readLightCommand = Integer.parseInt(HunveyorProperty.getProperty("analog1.getlight"));

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
