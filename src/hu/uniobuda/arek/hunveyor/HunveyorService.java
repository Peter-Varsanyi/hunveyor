package hu.uniobuda.arek.hunveyor;

import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebService(serviceName = "Hunveyor")
public class HunveyorService {

	Analog1 analog1;
	private final Logger logger;

	public HunveyorService() {
		analog1 = Analog1.getInstance();
		logger = LoggerFactory.getLogger(HunveyorService.class);
	}

	public void moveCamera(int cam, int direction, int degree) {
		try {
			ParallelPort.moveMotor(cam, direction, degree);
		} catch (Exception e) {
			logger.info("Exception: " + e);
		}
	}

	public String screenshot(int num) {
		return Camera.Screenshot(num);
	}

	public String hello() {
		return "works";
	}

	public float getSound() {
		return analog1.readNoise();
	}

	public void moveUp() {
	}

	public void moveDown() {
	}

	public float getGas() {
		return analog1.readGas();
	}

	public void ledOn() {
		ParallelPort.setLed(true);
		ParallelPort.setLed(true);
	}

	public void ledOff() {
		ParallelPort.setLed(false);
		ParallelPort.setLed(false);
	}

	public float getLight() {
		return analog1.readLight();
	}

	public float getHumidity() {
		return analog1.readHumidity();
	}

	public float getPressure() {
		return analog1.readPressure();
	}

	public float getUV() {
		return analog1.readUV();
	}

	public float getGreen() {
		return analog1.readGreen();
	}

	public float getYellow() {
		return analog1.readYellow();
	}

	public float getRed() {
		return analog1.readRed();
	}

	public void setPressure(boolean status) {
		analog1.setPressure(status);
	}

	public void setDust(boolean status) {
		analog1.setDust(status);
	}

	public void setGas(boolean status) {
		analog1.setGas(status);
	}

	public float getTemperature(int address) {
		float temp = -60;
		for (int tries = 0; tries < 5 && temp == -60; tries++) {
			temp = Temperature.ReadTemperature(address);
		}

		return temp;
	}
}
