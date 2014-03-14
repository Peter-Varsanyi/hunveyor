package hu.uniobuda.arek.hunveyor;

/**
 * Created by gwelican on 2014.02.03..
 */

import java.io.IOException;
import java.math.BigInteger;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

public class HunveyorMain {

	final static GpioController gpio;

	final static GpioPinDigitalOutput[] gpioDataPins;

	final static GpioPinDigitalOutput toggleBit;
	final static GpioPinDigitalOutput[] gpioPortPins;
	static {
		gpio = GpioFactory.getInstance();
		gpioDataPins = new GpioPinDigitalOutput[] {
				gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, "0 bit", PinState.LOW),
				gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "1 bit", PinState.LOW),
				gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "2 bit", PinState.LOW),
				gpio.provisionDigitalOutputPin(RaspiPin.GPIO_12, "3 bit", PinState.LOW),
				gpio.provisionDigitalOutputPin(RaspiPin.GPIO_13, "4 bit", PinState.LOW),
				gpio.provisionDigitalOutputPin(RaspiPin.GPIO_14, "5 bit", PinState.LOW),
				gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "6 bit", PinState.LOW),
				gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, "7 bit", PinState.LOW) };
		toggleBit = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_11, "toggle bit", PinState.HIGH);
		gpioPortPins = new GpioPinDigitalOutput[] {
				gpio.provisionDigitalOutputPin(RaspiPin.GPIO_10, "0 bit", PinState.LOW),
				gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, "1 bit", PinState.LOW) };

	}

	public static void readWindDirection(int address) throws IOException {
		I2CBus i2cBus = I2CFactory.getInstance(I2CBus.BUS_1);
		I2CDevice temp = i2cBus.getDevice(address);
		byte[] buffer = new byte[2];
		temp.read(buffer, 0, 2);
		for (byte b : buffer) {
			System.out.println("Address: " + address + " Data : " + b + " hex 0x" + Integer.toHexString((b & 0xff)));
		}

	}

	public static void printProgBar(int percent) {
		StringBuilder bar = new StringBuilder("[");

		for (int i = 0; i < 50; i++) {
			if (i < (percent / 2)) {
				bar.append("=");
			} else if (i == (percent / 2)) {
				bar.append(">");
			} else {
				bar.append(" ");
			}
		}

		bar.append("]   ").append(percent).append("%     ");
		System.out.print("\r" + bar.toString());
	}

	public static void ResetPins(GpioPinDigitalOutput[] pins) {

		for (GpioPinDigitalOutput pin : pins) {
			pin.low();
		}
	}

	public static void ResetParallelPort() throws InterruptedException {
		ParallelPort(0, 0);
		Thread.sleep(50);
		ParallelPort(0, 1);
		Thread.sleep(50);
		ParallelPort(0, 2);
		Thread.sleep(50);
		ParallelPort(0, 3);
		Thread.sleep(50);
	}

	public static boolean StartPosition(int motor) {
		boolean pos = false;
		// if
		return pos;
	}

	public static void MoveMotor(int port, int direction, int degree) throws InterruptedException {
		int moves_req = (int) (degree * 2.3138);
		int moves = 0;
		int[] pattern = { 1, 3, 2, 6, 4, 12, 8, 9 };
		int i = 0;
		while (moves < moves_req) {
			if (direction == 1) {
				i++;
				if (i > 7)
					i = 0;
			} else {

				i--;
				if (i < 0)
					i = 7;
			}
			moves++;
			ParallelPort(pattern[i], port);
			Thread.sleep(50);
		}

	}

	public static void ParallelPort(int data, int port) {

		toggleBit.high();
		switch (port) {
		case 1:
			gpioPortPins[0].low();
			gpioPortPins[0].high();
			break;
		case 2:
			gpioPortPins[0].low();
			gpioPortPins[1].high();
			break;
		case 3:
			gpioPortPins[0].high();
			gpioPortPins[1].high();
			break;
		}
		ResetPins(gpioDataPins);
		for (int i = 0; i < 8; i++) {
			if (BigInteger.valueOf(data).testBit(i)) {
				gpioDataPins[i].high();
			}
		}
		toggleBit.low();
	}

	public static void main(String[] args) throws IOException, InterruptedException {

		ResetParallelPort();
		while(true) {}
//		MoveMotor(2, 0, 40);
//		Thread.sleep(50);
//		ResetParallelPort();


	}

}