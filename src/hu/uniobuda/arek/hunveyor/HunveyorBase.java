package hu.uniobuda.arek.hunveyor;

import javax.xml.ws.Endpoint;

public class HunveyorBase {

	public static void main(String[] args) {
		String serviceAddress = "http://0.0.0.0:443/hunveyor";
		HunveyorService service = new HunveyorService();
    System.out.println("Serving on: "+serviceAddress);
    try {
      ParallelPort.ResetParallelPort();
    } catch(Exception e) { }
		@SuppressWarnings("unused")
		Endpoint ep = Endpoint.publish(serviceAddress,service);


	}

}
