package hu.uniobuda.arek.hunveyor;

import javax.xml.ws.Endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HunveyorBase {

	public static void main(String[] args) {
		final String serviceAddress = "http://0.0.0.0:443/hunveyor";
		final HunveyorService service = new HunveyorService();
		Logger logger = LoggerFactory.getLogger(Analog1.class);
		logger.info("Serving on: " + serviceAddress);
		try {
			ParallelPort.resetParallelPort();
		} catch (Exception e) {
			logger.info("Exception: " + e);
		}
		@SuppressWarnings({ "unused", "restriction" })
		final Endpoint ep = Endpoint.publish(serviceAddress, service);

	}

}
