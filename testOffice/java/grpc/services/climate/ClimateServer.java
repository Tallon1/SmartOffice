package grpc.services.climate;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import grpc.services.climate.ClimateServiceGrpc.ClimateServiceImplBase;
import grpc.services.utility.UtilityServer;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

// Server Side Implementation
public class ClimateServer extends ClimateServiceImplBase {

	private static final Logger logger = Logger.getLogger(UtilityServer.class.getName());

	public static void main(String[] args) throws IOException, InterruptedException {

		System.out.println("Starting gRPC Climate Server");

		try {
			// Defines port
			int PORT = 50099;

			// Creating a jmDNS instance
			JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost());

			// Adding the service listener
			ServiceInfo serviceClimate = ServiceInfo.create("_http._tcp.local.", "climate", 50099, "path=index.html");
			jmdns.registerService(serviceClimate);

			// Collecting the reference to the server
			ClimateServer climateServer = new ClimateServer();
			Server server = ServerBuilder.forPort(PORT).addService(climateServer).build().start();

			logger.info("Climate server started, listening on " + PORT);
			server.awaitTermination();
		} catch (UnknownHostException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	// gRPC services

	// Switch for HVAC
	@Override
	public void hvacOnOff(SwitchRequest request, StreamObserver<SwitchResponse> responseObserver) {

		// Notification of method invocation
		System.out.println("Receiving request to turn on/off HVAC!");

		// If true, devices will turn off otherwise, they'll turn on
		boolean OnOffH = request.getPower();
		if (request.getPower()) {
			System.out.println("Setting HVAC off!");
		} else {
			System.out.println("Setting HVAC on!");
		}

		SwitchResponse response = SwitchResponse.newBuilder().setPower(OnOffH).build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}

	// Controls for temperature
	@Override
	public void hvacTemperature(HvacRequest request, StreamObserver<HvacResponse> responseObserver) {
		int newTemp = request.getTemp();

		System.out.println("Receiving request to change temperature to: " + newTemp + "�C");
		try {
			// Requests changes of temperature
			HvacResponse response = HvacResponse.newBuilder().setTemp(newTemp + 3).build();
			HvacResponse response1 = HvacResponse.newBuilder().setTemp(newTemp - 1).build();
			HvacResponse response2 = HvacResponse.newBuilder().setTemp(newTemp).build();

			try {
				// Response delayed to simulate the temp actually changing
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			responseObserver.onNext(response);

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			responseObserver.onNext(response1);

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			responseObserver.onNext(response2);

		} catch (Error e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		responseObserver.onCompleted();
		System.out.println("Room reached the selected temperature: " + newTemp + "�C");
	}

	// Checks CO levels in the room
	@Override
	public void checkCO(CoLevelRequest request, StreamObserver<ExtractionResponse> responseObserver) {

		System.out.println("Checking CO levels in the office!");

		// Variable to hold CO level
		int CoLv = request.getLevel();

		// If CO level is over 40, the extractor is turned on
		if (CoLv > 40) {
			responseObserver.onNext(ExtractionResponse.newBuilder().setLevel(CoLv).build());
			System.out.println("CO level is: " + CoLv);
			System.out.println("High level of CO, extractor is on!");
		} else {
			responseObserver.onNext(ExtractionResponse.newBuilder().setLevel(CoLv).build());
			System.out.println("Co level: " + CoLv);
			System.out.println("CO level OK!");
		}
		responseObserver.onCompleted();
	}
}