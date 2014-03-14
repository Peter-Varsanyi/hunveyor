package hu.uniobuda.arek.hunveyor;
import java.math.BigInteger;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;


public class ParallelPort {
	final static GpioController gpio;

	final static GpioPinDigitalOutput[] gpio_data_pins;

	final static GpioPinDigitalOutput toggle_bit;
	final static GpioPinDigitalOutput[] gpio_port_pins;
	static {
		gpio = GpioFactory.getInstance();
		gpio_data_pins = new GpioPinDigitalOutput[] {
				gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, "0 bit",
						PinState.LOW),
				gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "1 bit",
						PinState.LOW),
				gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "2 bit",
						PinState.LOW),
				gpio.provisionDigitalOutputPin(RaspiPin.GPIO_12, "3 bit",
						PinState.LOW),
				gpio.provisionDigitalOutputPin(RaspiPin.GPIO_13, "4 bit",
						PinState.LOW),
				gpio.provisionDigitalOutputPin(RaspiPin.GPIO_14, "5 bit",
						PinState.LOW),
				gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "6 bit",
						PinState.LOW),
				gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, "7 bit",
						PinState.LOW) };
		toggle_bit = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_11,
				"toggle bit", PinState.HIGH);
		gpio_port_pins = new GpioPinDigitalOutput[] {
				gpio.provisionDigitalOutputPin(RaspiPin.GPIO_10, "0 bit",
						PinState.LOW),
				gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, "1 bit",
						PinState.LOW) };
//
	}
	public static void ResetPins(GpioPinDigitalOutput[] pins) {

		for (GpioPinDigitalOutput pin : pins) {
			pin.low();
		}
	}

	public static void ResetParallelPort() throws InterruptedException {
		PutData(0, 0);
		Thread.sleep(50);
		PutData(0, 1);
		Thread.sleep(50);
		PutData(0, 2);
		Thread.sleep(50);
		PutData(0, 3);
		Thread.sleep(50);
	}

	public static boolean StartPosition(int motor) {
		boolean pos = false;
		// if
		return pos;
	}

	public static void MoveMotor(int port, int direction, int degree)
			throws InterruptedException {
		int moves_req = (int) (degree * 4.3138);
		int moves = 0;
		int[] pattern = { 1, 3, 2, 6, 4, 12, 8, 9 };
		int i = 0;
		while (moves < moves_req) {
			if (direction == 0) {
				i++;
				if (i > 7) i = 0;
			} else {
				
				i--;
				if (i < 0) i=7;
			}
			moves++;
			PutData(pattern[i], port);
			Thread.sleep(50);
		}

	}
  public static void Led(boolean status) {
    System.out.println("Status: "+status);
    if (status) ParallelPort.PutData(64,1);
    else ParallelPort.PutData(0,1);
  }
	public static void PutData(int data, int port) {

		// gpio.provisionDigitalOutputPin(RaspiPin.GPIO_10, PinState.HIGH);
		toggle_bit.high();
		switch (port) {
		case 1:
			gpio_port_pins[0].low();
			gpio_port_pins[1].low();
			break;
		case 2:
			gpio_port_pins[0].low();
			gpio_port_pins[1].high();
			break;
		case 3:
			gpio_port_pins[0].high();
			gpio_port_pins[1].high();
			break;
		}
		ResetPins(gpio_data_pins);
    System.out.println("data: "+data);
		for (int i = 0; i < 8; i++) {
			if (BigInteger.valueOf(data).testBit(i)) {
				gpio_data_pins[i].high();
        System.out.println(i+". bit: 1");
			} else {
				gpio_data_pins[i].low();
        System.out.println(i+". bit: 0");
      }

		}
		// int[] gpiopins = {4,17,22,10,9,11,23,24,25,8};
		toggle_bit.low();
	}

}
