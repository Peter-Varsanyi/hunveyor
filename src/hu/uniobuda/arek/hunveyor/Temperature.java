package hu.uniobuda.arek.hunveyor;

import java.io.IOException;
import java.math.BigInteger;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

public class Temperature {
	private static final int SIGNBIT = 7;
	private static final int SLEEP_TIME = 50;
	private static final int GET_COMMAND = 0xAA;
	private static final int START_COMMAND = 0x51;
	private static final int SIGNBIT_ADD = 256;
	private static final float INTERRUPTED_FLAG = 2.0f;
	private static final double LSB_MULTIPLY = 0.00625;

	public static float ReadTemperature(int address) {
		try {
			I2CBus i2cBus;
			i2cBus = I2CFactory.getInstance(I2CBus.BUS_1);
			I2CDevice temp = i2cBus.getDevice(address);
			temp.write((byte) START_COMMAND);
			Thread.sleep(SLEEP_TIME);
			temp.write((byte) GET_COMMAND);

			byte[] buffer = new byte[2];
			temp.read(buffer, 0, 2);
			byte msb = buffer[0];
			byte lsb = buffer[1];

			if (BigInteger.valueOf(msb).testBit(SIGNBIT)) { // sign bit
				msb -= SIGNBIT_ADD;
			}
			Object[] args = {String.format("%02x", msb), String.format("%02x", lsb), address };
			HunLogger.logger.debug("Read MSB: {} {} from {}", args);

			return (float) (msb + lsb * LSB_MULTIPLY);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1.0f;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -INTERRUPTED_FLAG;
		}
	}

}
