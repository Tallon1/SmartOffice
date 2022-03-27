package grpc.services.light;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

// Client Side Implementation
public class LightClient {

	private static LightServiceGrpc.LightServiceBlockingStub lblockingStub;
	private static LightServiceGrpc.LightServiceStub lasyncStub;

	// Adds a listener for discovery
	public static class Listener implements ServiceListener {
		// Service resolution
		public void serviceAdded(ServiceEvent serviceEvent) {
			System.out.println("Service added: " + serviceEvent.getInfo());
		}

		// Removed service
		public void serviceRemoved(ServiceEvent serviceEvent) {
			System.out.println("Service removed: " + serviceEvent.getInfo());
		}

		// Service resolved.
		public void serviceResolved(ServiceEvent serviceEvent) {
			System.out.println("Service resolved: " + serviceEvent.getInfo());
			ServiceInfo info = serviceEvent.getInfo();
			final int Port = serviceEvent.getInfo().getPort();
			String address = info.getHostAddresses()[0];
		}
	}

	public static void main(String[] args) throws Exception {

		// gRPC channels
		ManagedChannel lightchannel = ManagedChannelBuilder.forAddress("localhost", 50097).usePlaintext().build();

		// Stubs - generated through proto
		lblockingStub = LightServiceGrpc.newBlockingStub(lightchannel);
		lasyncStub = LightServiceGrpc.newStub(lightchannel);

		try {
			// Creates a jmDNS instance
			JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost());

			// Adds a service listener
			jmdns.addServiceListener("_http._tcp.local.", new Listener());
		} catch (UnknownHostException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		lighting();
		LightsOnOff();
		lightIntensity();
	}
	
	// gRPC services

	// Lights Services
	public static void lighting() {

		Empty emp = Empty.newBuilder().build();

		LightingResponse response;
		try {
			// Collects lights information
			response = lblockingStub.lighting(emp);
			String intens = String.valueOf(response.getIntensity());
			System.out.println("LightID: " + response.getLightId());
			System.out.println("Light status: " + response.getStatus());
			System.out.println("Brightness: " + intens);
		} catch (io.grpc.StatusRuntimeException e) {
			System.out.println("Light client failed:" + e.getStatus());
			return;
		}
	}

	// Lights Controls
	public static void LightsOnOff() {
		LightsRequest request = LightsRequest.newBuilder().setSwitch(false).build();

		// If true, lights are off otherwise, turn lights on
		LightsResponse response = lblockingStub.lightsOnOff(request);
		if (response.getSwitch()) {
			System.out.println("Lights off!");
		} else {
			System.out.println("Lights on!");
		}
	}

	// Lighting intensity
	public static void lightIntensity() {
		StreamObserver<IntensityResponse> responseObserver = new StreamObserver<IntensityResponse>() {

			public void onNext(IntensityResponse intens) {
				System.out.println("Brightness has been set to level " + intens.getIntensity());
			}

			public void onError(Throwable t) {
				t.printStackTrace();
			}

			public void onCompleted() {
				System.out.println("Lights adjustment completed");
			}
		};

		StreamObserver<IntensityRequest> requestObserver = lasyncStub.lightIntensity(responseObserver);
		try {
			// Simulates a few requests from client to change temperature
			requestObserver.onNext(IntensityRequest.newBuilder().setIntensity(1).build());
			System.out.println("Lights brightness changed to: 1");
			requestObserver.onNext(IntensityRequest.newBuilder().setIntensity(3).build());
			System.out.println("Lights brightness changed to: 3");
			requestObserver.onNext(IntensityRequest.newBuilder().setIntensity(2).build());
			System.out.println("Lights brightness changed to: 2");
			requestObserver.onNext(IntensityRequest.newBuilder().setIntensity(5).build());
			System.out.println("Lights brightness changed to: 5");

			Thread.sleep(2000);
			// Catching errors
		} catch (RuntimeException e) {
			requestObserver.onError(e);
			throw e;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		requestObserver.onCompleted();
	}
}