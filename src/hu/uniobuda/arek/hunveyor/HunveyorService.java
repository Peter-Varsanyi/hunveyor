package hu.uniobuda.arek.hunveyor;

import javax.jws.WebService;

@WebService(serviceName = "Hunveyor")
public class HunveyorService {

	Analog1 analog1;
	public HunveyorService() {
		analog1 = Analog1.getInstance();
	}
	public void MoveCamera(int cam, int direction, int degree) {
		try {
			ParallelPort.MoveMotor(cam, direction, degree);
		} catch (Exception e) {
			// pass
		}
	}

	public String Screenshot(int num) {
		return Camera.Screenshot(num);
	}

	public String Hello() {
		return "works";
	}

	public float GetSound() {
		return analog1.ReadNoise();
	}

	public void MoveUp() {
	}

	public void MoveDown() {
	}

	public float GetGas() {
		return analog1.ReadGas();
	}

	public void LedOn() {
		ParallelPort.Led(true);
		ParallelPort.Led(true);
	}

	public void LedOff() {
		ParallelPort.Led(false);
		ParallelPort.Led(false);
	}

	public float GetLight() {
		return analog1.ReadLight();
	}

	public float GetHumidity() {
		return analog1.readHumidity();
	}

	public float GetPressure() {
		return analog1.readPressure();
	}

	public float GetUV() {
		return analog1.readUV();
	}

	public float GetGreen() {
		return analog1.ReadGreen();
	}

	public float GetYellow() {
		return analog1.ReadYellow();
	}

	public float GetRed() {
		return analog1.ReadRed();
	}

	public void SetPressure(boolean status) {
		analog1.SwitchPressure(status);
	}

	public void SetDust(boolean status) {
		analog1.SwitchDust(status);
	}

	public void SetGas(boolean status) {
		analog1.SwitchGas(status);
	}

	public float GetTemperature(int address) {
		float temp = -60;
		for (int tries = 0; tries < 5 && temp == -60; tries++)
			temp = Temperature.ReadTemperature(address);
		return temp;
	}
}
