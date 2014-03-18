package hu.uniobuda.arek.hunveyor;

import javax.jws.WebService;

@WebService(serviceName = "Hunveyor")
public class HunveyorService {

	private static final int WRONG_TEMP_VALUE = -60;
	private static final int MAX_TRIES = 5;
	private final Analog1 analog1;
	public HunveyorService() {
		analog1 = Analog1.getInstance();
	}

	public void moveCamera(final int cam, final int direction, final int degree) {
		try {
			ParallelPort.moveMotor(cam, direction, degree);
		} catch (InterruptedException e) {
			HunLogger.logger.info("Exception: " + e);
		}
	}

	public String screenshot(final int num) {
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

	public void setPressure(final boolean status) {
		analog1.setPressure(status);
	}

	public void setDust(final boolean status) {
		analog1.setDust(status);
	}

	public void setGas(final boolean status) {
		analog1.setGas(status);
	}

	public float getTemperature(final int address) {
		float temp = WRONG_TEMP_VALUE;
		for (int tries = 0; tries < MAX_TRIES && temp == WRONG_TEMP_VALUE; tries++) {
			temp = Temperature.ReadTemperature(address);
		}

		return temp;
	}
}
