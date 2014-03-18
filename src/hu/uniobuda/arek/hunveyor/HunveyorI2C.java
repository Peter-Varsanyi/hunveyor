package hu.uniobuda.arek.hunveyor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

public class HunveyorI2C {
	static float ReadI2CData(int address, int data) {

		I2CBus i2cBus;
		try {
			i2cBus = I2CFactory.getInstance(I2CBus.BUS_1);
			I2CDevice temp = i2cBus.getDevice(address);
			temp.write((byte) data);
			byte[] buffer = new byte[2];
			boolean readDone = true;
			do {

				try {
					temp.read(buffer, 0, 2);
					readDone = true;
				} catch (Exception e) {
					readDone = false;
				}
			} while (!readDone);
			Thread.sleep(50);
			int val = ((buffer[1] << 8) & 0x0000ff00) | (buffer[0] & 0x000000ff);
			Object[] args = { val, String.format("%02x", val), address };
			HunLogger.logger.debug("Read {}(0x{}) from {}", args);
			return (float) val / 1024;
		} catch (IOException e1) {
			e1.printStackTrace();
			return 0xFF;
		} catch (InterruptedException e2) {
			e2.printStackTrace();
			return 0xFA;
		}

	}

	static float ReadI2CDataAverage(int address, int command) {
		List<Float> values = new ArrayList<Float>() ;

		for (int i = 0; i < 40; i++) {
			values.add(ReadI2CData(address, command));
		}
		return (Collections.max(values) - Collections.min(values)) / 2;

	}
}
