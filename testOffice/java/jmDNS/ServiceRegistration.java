package jmDNS;

import java.io.IOException;
import java.net.InetAddress;
import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;

public class ServiceRegistration {

	public static JmDNS jmdns;

	public static void main(String[] args) throws InterruptedException {
	}

	public void jmdnsRegister(int climatePort, int lightPort, int utilityPort) throws InterruptedException {

		try {
			// Creating a jmDNS instance
			jmdns = JmDNS.create(InetAddress.getLocalHost());

			// Register all of the services
			System.out.println("Service Registration");
			ServiceInfo serviceUtility = ServiceInfo.create("_http._tcp.local.", "utility", 50098, "path=index.html");
			ServiceInfo serviceClimate = ServiceInfo.create("_http._tcp.local.", "climate", 50099, "path=index.html");
			ServiceInfo serviceLight = ServiceInfo.create("_http._tcp.local.", "light", 50097, "path=index.html");

			jmdns.registerService(serviceUtility);
			jmdns.registerService(serviceClimate);
			jmdns.registerService(serviceLight);
			System.out.println("Service Registration Underway! ");

			// Simulates a short delay
			Thread.sleep(10000);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	// Unregisters all of the services
	public void unRegister() {
		System.out.println("Unregistering!");
		jmdns.unregisterAllServices();
	}
}