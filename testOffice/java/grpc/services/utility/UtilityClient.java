package grpc.services.utility;

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

// Client Implementation
public class UtilityClient {

	private static UtilityServiceGrpc.UtilityServiceBlockingStub ublockingStub;
	private static UtilityServiceGrpc.UtilityServiceStub uasyncStub;

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

		// Service resolved
		public void serviceResolved(ServiceEvent serviceEvent) {
			System.out.println("Service resolved: " + serviceEvent.getInfo());
			ServiceInfo info = serviceEvent.getInfo();
			final int Port = serviceEvent.getInfo().getPort();
			String address = info.getHostAddresses()[0];
		}
	}

	public static void main(String[] args) throws Exception {

		// gRPC channels
		ManagedChannel utilitychannel = ManagedChannelBuilder.forAddress("localhost", 50098).usePlaintext().build();

		// Stubs - generated through proto
		ublockingStub = UtilityServiceGrpc.newBlockingStub(utilitychannel);
		uasyncStub = UtilityServiceGrpc.newStub(utilitychannel);

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

		switchDevices();
		switchCameraOn();
		printList();
	}

	// gRPC services

	// Switches for devices
	public static void switchDevices() {

		DevicesRequest request = DevicesRequest.newBuilder().setDevices(false).build();

		// Notification of method invocation
		DevicesResponse response = ublockingStub.switchDevices(request);
		if (response.getDevices()) {
			System.out.println("Devices off!");
		} else {
			System.out.println("Devices on!");
		}
	}

	// Switch for cameras
	public static void switchCameraOn() {

		CameraRequest request = CameraRequest.newBuilder().setCamera(false).build();

		// If true, cameras are off otherwise, they're on
		CameraResponse response = ublockingStub.switchCameraOn(request);
		if (response.getCamera()) {
			System.out.println("Camera off!");
		} else {
			System.out.println("Camera on!");
		}
	}

	// Prints the visitor list
	public static void printList() {

		StreamObserver<PrinterResponse> responseObserver = new StreamObserver<PrinterResponse>() {

			public void onNext(PrinterResponse value) {
				System.out.println("Printing visit list: " + value.getPList());
			}

			public void onError(Throwable t) {
			}

			public void onCompleted() {
			}

		};

		StreamObserver<PrinterRequest> requestObserver = uasyncStub.printList(responseObserver);
		try {
			// Visitors list
			requestObserver.onNext(PrinterRequest.newBuilder().setPList("Karl Tallon/nCaroline Byrne").build());
			requestObserver.onNext(PrinterRequest.newBuilder().setPList("Caroline Byrne").build());
			requestObserver.onNext(PrinterRequest.newBuilder().setPList("Matthew Dwyer").build());
			requestObserver.onNext(PrinterRequest.newBuilder().setPList("Athanasios Staikopoulos").build());
			requestObserver.onNext(PrinterRequest.newBuilder().setPList("Magnum PI").build());

			Thread.sleep(2000);
		} catch (RuntimeException e) {
			requestObserver.onError(e);
			throw e;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		requestObserver.onCompleted();
	}
}