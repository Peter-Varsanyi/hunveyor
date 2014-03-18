package hu.uniobuda.arek.hunveyor;

import hu.uniobuda.arek.hunveyor.propertyreader.HunveyorProperty;

import java.math.BigInteger;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiGpioProvider;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.impl.PinImpl;

public class ParallelPort {
	private static final int SLEEP_TIME = 50;
	private static final int DATA_BITS_COUNT = 8;
	private static final int LED_OFF;
	private static final int LED_ON;
	private static final double DEGREE_TO_STEPS;
	private static final int PATTERN_MIN_VALUE = 0;
	private static final int PATTERN_MAX_VALUE = 7;
	private static final String FORMAT_DATA_ADDR = "parallelport.bit.%d.address";
	private static final String FORMAT_DATA_NAME = "parallelport.bit.%d.name";
	private static final String FORMAT_PORT_ADDR = "parallelport.port.bit.%d.address";
	private static final String FORMAT_PORT_NAME = "parallelport.port.bit.%d.name";

	private final static GpioController GPIO;

	private final static Map<Integer, GpioPinDigitalOutput> GPIO_DATA_PINS = new HashMap<Integer, GpioPinDigitalOutput>();

	private final static GpioPinDigitalOutput ENABLE_BIT;
	private final static Map<Integer, GpioPinDigitalOutput> GPIO_PORT_PINS = new HashMap<Integer, GpioPinDigitalOutput>();
	static {

		GPIO = GpioFactory.getInstance();

		for (int i = 0; i < DATA_BITS_COUNT; i++) {
			Pin pin = new PinImpl(RaspiGpioProvider.NAME, Integer.parseInt(HunveyorProperty.getPropertyWithFormat(
					FORMAT_DATA_ADDR, i)), HunveyorProperty.getPropertyWithFormat(FORMAT_DATA_NAME, i), EnumSet.of(
					PinMode.DIGITAL_INPUT, PinMode.DIGITAL_OUTPUT), PinPullResistance.all());
			GPIO_DATA_PINS.put(i, GPIO.provisionDigitalOutputPin(pin, PinState.LOW));
		}
		for (int i = 0; i < 2; i++) {
			Pin pin = new PinImpl(RaspiGpioProvider.NAME, Integer.parseInt(HunveyorProperty.getPropertyWithFormat(
					FORMAT_PORT_ADDR, i)), HunveyorProperty.getPropertyWithFormat(FORMAT_PORT_NAME, i), EnumSet.of(
					PinMode.DIGITAL_INPUT, PinMode.DIGITAL_OUTPUT), PinPullResistance.all());
			GPIO_PORT_PINS.put(i, GPIO.provisionDigitalOutputPin(pin, PinState.LOW));
		}
		ENABLE_BIT = GPIO.provisionDigitalOutputPin(RaspiPin.GPIO_11, "enable bit", PinState.HIGH);

		DEGREE_TO_STEPS = Double.valueOf(HunveyorProperty.getProperty("parallelport.degree_to_steps"));
		LED_ON = Integer.valueOf(HunveyorProperty.getProperty("parallelport.led.on"));
		LED_OFF = Integer.valueOf(HunveyorProperty.getProperty("parallelport.led.off"));
	}

	public static void resetPins(Map<Integer, GpioPinDigitalOutput> pins) {

		for (GpioPinDigitalOutput pin : pins.values()) {
			pin.low();
		}
	}

	public static void resetParallelPort() throws InterruptedException {
		putData(0, 0);
		Thread.sleep(SLEEP_TIME);
		putData(0, 1);
		Thread.sleep(SLEEP_TIME);
		putData(0, 2);
		Thread.sleep(SLEEP_TIME);
		putData(0, 3);
		Thread.sleep(SLEEP_TIME);
	}

	public static boolean startPosition(int motor) {
		boolean pos = false;
		return pos;
	}

	public static void moveMotor(int port, int direction, int degree) throws InterruptedException {
		int movesReq = (int) (degree * DEGREE_TO_STEPS);
		int moves = 0;
		final int[] pattern = { 1, 3, 2, 6, 4, 12, 8, 9 };
		int currentPattern = 0;
		while (moves < movesReq) {
			if (direction == 0) {
				currentPattern++;
				if (currentPattern > PATTERN_MAX_VALUE) {
					currentPattern = PATTERN_MIN_VALUE;
				}
			} else {
				currentPattern--;
				if (currentPattern < PATTERN_MIN_VALUE) {
					currentPattern = PATTERN_MAX_VALUE;
				}
			}
			moves++;
			putData(pattern[currentPattern], port);
			Thread.sleep(SLEEP_TIME);
		}

	}

	public static void setLed(final boolean status) {
		HunLogger.logger.debug("Led status: {}", status);
		if (status) {
			ParallelPort.putData(LED_ON, 1);
		} else {
			ParallelPort.putData(LED_OFF, 1);
		}
	}

	public static void putData(int data, int port) {

		ENABLE_BIT.high();
		switch (port) {
		case 1:
			GPIO_PORT_PINS.get(0).low();
			GPIO_PORT_PINS.get(1).low();
			break;
		case 2:
			GPIO_PORT_PINS.get(0).low();
			GPIO_PORT_PINS.get(1).high();
			break;
		case 3:
			GPIO_PORT_PINS.get(0).high();
			GPIO_PORT_PINS.get(1).high();
			break;
		default:
		}
		resetPins(GPIO_DATA_PINS);
		HunLogger.logger.debug("Data: {} on port: {}", data, port);
		for (int i = 0; i < DATA_BITS_COUNT; i++) {
			if (BigInteger.valueOf(data).testBit(i)) {
				GPIO_DATA_PINS.get(i).high();
				HunLogger.logger.debug("{} bit is high", i);
			} else {
				GPIO_DATA_PINS.get(i).low();
				HunLogger.logger.debug("{} bit is low", i);
			}

		}
		ENABLE_BIT.low();
	}

}
