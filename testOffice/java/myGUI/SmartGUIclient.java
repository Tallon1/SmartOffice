package myGUI;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import grpc.services.climate.ClimateClient;
import grpc.services.climate.ClimateServiceGrpc;
import grpc.services.climate.CoLevelRequest;
import grpc.services.climate.ExtractionResponse;
import grpc.services.climate.HvacRequest;
import grpc.services.climate.HvacResponse;
import grpc.services.climate.SwitchRequest;
import grpc.services.climate.SwitchResponse;
import grpc.services.light.IntensityRequest;
import grpc.services.light.IntensityResponse;
import grpc.services.light.LightServiceGrpc;
import grpc.services.light.LightsRequest;
import grpc.services.light.LightsResponse;
import grpc.services.utility.CameraRequest;
import grpc.services.utility.CameraResponse;
import grpc.services.utility.DevicesRequest;
import grpc.services.utility.DevicesResponse;
import grpc.services.utility.PrinterRequest;
import grpc.services.utility.PrinterResponse;
import grpc.services.utility.UtilityServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import jmDNS.ServiceRegistration;
import java.awt.Font;
import javax.swing.JTextField;
import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;
import javax.swing.ImageIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.swing.JToggleButton;
import javax.swing.JSlider;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.Color;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import java.awt.SystemColor;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.LineBorder;

public class SmartGUIclient {

	private JFrame frame;
	private static ClimateServiceGrpc.ClimateServiceBlockingStub cblockingStub;
	private static ClimateServiceGrpc.ClimateServiceStub casyncStub;
	private static LightServiceGrpc.LightServiceBlockingStub lblockingStub;
	private static LightServiceGrpc.LightServiceStub lasyncStub;
	private static UtilityServiceGrpc.UtilityServiceBlockingStub ublockingStub;
	private static UtilityServiceGrpc.UtilityServiceStub uasyncStub;
	private static int climatePort = 50099;
	private static int lightPort = 50097;
	private static int utilityPort = 50098;
	public JLabel lightDataId;
	public JLabel lightDataStatus;
	public JLabel lightDataIntensity;
	public static int randomCo = (int) (Math.random() * 100 + 1);
	private static final int MIN = 1;
	private static final int MAX = 5;
	private static final int DEFAULT = 1;
	public static JSpinner HTemp;
	public static JTextField messages;

	public static class Listener implements ServiceListener {
		public void serviceAdded(ServiceEvent serviceEvent) {
			System.out.println("Service added: " + serviceEvent.getInfo());
		}

		public void serviceRemoved(ServiceEvent serviceEvent) {
			System.out.println("Service removed: " + serviceEvent.getInfo());
		}

		public void serviceResolved(ServiceEvent serviceEvent) {
			System.out.println("Service resolved: " + serviceEvent.getInfo());
			// Ports for connections will be designated according to each event
			if (serviceEvent.getName().equals("climate")) {
				climatePort = serviceEvent.getInfo().getPort();
			} else if (serviceEvent.getName().equals("light")) {
				lightPort = serviceEvent.getInfo().getPort();
			} else if (serviceEvent.getName().equals("utility")) {
				utilityPort = serviceEvent.getInfo().getPort();
			}
		}
	}

	// Launch the app
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() { // check line
			public void run() {// check line
				try {
					SmartGUIclient window = new SmartGUIclient();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// Create the app
	public SmartGUIclient() throws InterruptedException, IOException {

		initialize();
		ServiceRegistration reg = new ServiceRegistration();
		reg.jmdnsRegister(climatePort, lightPort, utilityPort);

		try {
			JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost());
			jmdns.addServiceListener("_http._tcp.local.", new SmartGUIclient.Listener());
			jmdns.addServiceListener("_http._tcp.local.", new SmartGUIclient.Listener());
			jmdns.addServiceListener("_http._tcp.local.", new SmartGUIclient.Listener());
		} catch (UnknownHostException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		ManagedChannel climatechannel = ManagedChannelBuilder.forAddress("localhost", 50099).usePlaintext().build();
		cblockingStub = ClimateServiceGrpc.newBlockingStub(climatechannel);
		casyncStub = ClimateServiceGrpc.newStub(climatechannel);

		ManagedChannel lightchannel = ManagedChannelBuilder.forAddress("localhost", 50097).usePlaintext().build();
		lblockingStub = LightServiceGrpc.newBlockingStub(lightchannel);
		lasyncStub = LightServiceGrpc.newStub(lightchannel);

		ManagedChannel utilitychannel = ManagedChannelBuilder.forAddress("localhost", 50098).usePlaintext().build();
		ublockingStub = UtilityServiceGrpc.newBlockingStub(utilitychannel);
		uasyncStub = UtilityServiceGrpc.newStub(utilitychannel);
	}

	// Initialize the frame
	private void initialize() {

		// Frame, labels & separators
		frame = new JFrame("Smart Office Dashboard");
		frame.getContentPane().setForeground(new Color(255, 0, 0));
		frame.setBounds(100, 100, 406, 590);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().setBackground(new Color(153, 255, 255));

		JLabel TitleLabel = new JLabel("Tallon Smart Office Solutions");
		TitleLabel.setForeground(SystemColor.desktop);
		TitleLabel.setFont(new Font("Microsoft Tai Le", Font.BOLD | Font.ITALIC, 26));
		TitleLabel.setBounds(15, 0, 367, 52);
		frame.getContentPane().add(TitleLabel);

		JLabel dashWelcome = new JLabel("Welcome to your Dashboard!");
		dashWelcome.setForeground(SystemColor.desktop);
		dashWelcome.setBounds(91, 51, 216, 16);
		frame.getContentPane().add(dashWelcome);
		dashWelcome.setFont(new Font("Tahoma", Font.BOLD, 15));

		JLabel logo = new JLabel("");
		logo.setBounds(2, 390, 196, 128);
		frame.getContentPane().add(logo);
		ImageIcon img = new ImageIcon(this.getClass().getResource("/logo.png"));
		logo.setIcon(img);

		JLabel controls = new JLabel("CONTROLS");
		controls.setForeground(SystemColor.desktop);
		controls.setBounds(145, 196, 108, 25);
		frame.getContentPane().add(controls);
		controls.setFont(new Font("Tahoma", Font.BOLD, 18));

		JLabel switches = new JLabel("SWITCHES");
		switches.setForeground(SystemColor.desktop);
		switches.setBounds(150, 90, 114, 25);
		frame.getContentPane().add(switches);
		switches.setFont(new Font("Tahoma", Font.BOLD, 18));

		JLabel monitor = new JLabel("MONITOR");
		monitor.setForeground(SystemColor.desktop);
		monitor.setFont(new Font("Tahoma", Font.BOLD, 18));
		monitor.setBounds(46, 304, 100, 25);
		frame.getContentPane().add(monitor);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBackground(SystemColor.desktop);
		separator_1.setBounds(4, 77, 380, 3);
		frame.getContentPane().add(separator_1);

		JSeparator separator_2 = new JSeparator();
		separator_2.setBackground(SystemColor.desktop);
		separator_2.setBounds(4, 184, 380, 2);
		frame.getContentPane().add(separator_2);

		JSeparator separator_2_1 = new JSeparator();
		separator_2_1.setBackground(SystemColor.desktop);
		separator_2_1.setBounds(4, 291, 380, 2);
		frame.getContentPane().add(separator_2_1);

		JSeparator separator_3 = new JSeparator();
		separator_3.setBackground(SystemColor.desktop);
		separator_3.setOrientation(SwingConstants.VERTICAL);
		separator_3.setBounds(196, 296, 2, 218);
		frame.getContentPane().add(separator_3);

		messages = new JTextField();
		messages.setBorder(new LineBorder(SystemColor.desktop));
		messages.setFont(new Font("Tahoma", Font.BOLD, 12));
		messages.setBackground(new Color(153, 255, 255));
		messages.setBounds(0, 518, 388, 26);
		frame.getContentPane().add(messages);
		messages.setColumns(10);

		// Climate buttons & labels
		JLabel hvacLabel = new JLabel("HVAC");
		hvacLabel.setForeground(SystemColor.desktop);
		hvacLabel.setBounds(177, 127, 41, 14);
		frame.getContentPane().add(hvacLabel);
		hvacLabel.setFont(new Font("Tahoma", Font.BOLD, 14));

		JLabel coLabel = new JLabel("CO Level");
		coLabel.setForeground(SystemColor.desktop);
		coLabel.setBounds(15, 341, 79, 21);
		frame.getContentPane().add(coLabel);
		coLabel.setFont(new Font("Tahoma", Font.BOLD, 14));

		JLabel tempLabel = new JLabel("Temperature");
		tempLabel.setForeground(SystemColor.desktop);
		tempLabel.setBounds(268, 233, 99, 25);
		frame.getContentPane().add(tempLabel);
		tempLabel.setFont(new Font("Tahoma", Font.BOLD, 14));

		JButton coButton = new JButton("Check");
		coButton.setFont(new Font("Tahoma", Font.BOLD, 13));
		coButton.setBounds(104, 339, 75, 23);
		coButton.setBorderPainted(false);
		coButton.setBackground(SystemColor.activeCaption);
		frame.getContentPane().add(coButton);
		coButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkCO();
			}
		});

		final JToggleButton hvacOn = new JToggleButton("Off");
		hvacOn.setFont(new Font("Tahoma", Font.BOLD, 13));
		hvacOn.setBounds(165, 151, 65, 23);
		frame.getContentPane().add(hvacOn);
		hvacOn.setSelected(true);
		hvacOn.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (hvacOn.isSelected()) {
					hvacOn.setText("Off");
					hvacOnOff(true);
				} else {
					hvacOn.setText("On");
					hvacOnOff(false);
				}
			}
		});

		final JSpinner HTemp = new JSpinner();
		HTemp.setFont(new Font("Tahoma", Font.PLAIN, 13));
		HTemp.setModel(new SpinnerNumberModel(15, 15, 35, 1));
		HTemp.setBounds(264, 258, 43, 22);
		frame.getContentPane().add(HTemp);

		JButton btnNewButton = new JButton("OK");
		btnNewButton.setBorder(new LineBorder(SystemColor.desktop));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HvacTemperature(HTemp.getValue());
			}
		});
		
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 10));
		coButton.setBorderPainted(false);
		coButton.setBackground(SystemColor.activeCaption);
		btnNewButton.setBounds(317, 258, 46, 23);
		frame.getContentPane().add(btnNewButton);

		// Utility buttons & labels
		JLabel deviceLabel = new JLabel("Devices");
		deviceLabel.setForeground(SystemColor.desktop);
		deviceLabel.setBounds(37, 127, 93, 14);
		frame.getContentPane().add(deviceLabel);
		deviceLabel.setFont(new Font("Tahoma", Font.BOLD, 14));

		JLabel VisitLabel = new JLabel("Visitor List");
		VisitLabel.setForeground(SystemColor.desktop);
		VisitLabel.setBounds(15, 379, 79, 21);
		frame.getContentPane().add(VisitLabel);
		VisitLabel.setFont(new Font("Tahoma", Font.BOLD, 14));

		JLabel cameraLabel = new JLabel("Cameras");
		cameraLabel.setForeground(SystemColor.desktop);
		cameraLabel.setBounds(296, 127, 65, 14);
		frame.getContentPane().add(cameraLabel);
		cameraLabel.setFont(new Font("Tahoma", Font.BOLD, 14));

		final JToggleButton deviceOnOff = new JToggleButton("Off");
		deviceOnOff.setFont(new Font("Tahoma", Font.BOLD, 13));
		deviceOnOff.setBounds(32, 151, 62, 23);
		frame.getContentPane().add(deviceOnOff);
		deviceOnOff.setSelected(true);
		deviceOnOff.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (deviceOnOff.isSelected()) {
					deviceOnOff.setText("Off");
					switchDevices(true);
				} else {
					deviceOnOff.setText("On");
					switchDevices(false);
				}
			}
		});

		JButton printLabel = new JButton("Print");
		printLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		printLabel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				printList();
			}
		});
		
		printLabel.setBorderPainted(false);
		printLabel.setBackground(SystemColor.activeCaption);
		printLabel.setBounds(104, 379, 75, 23);
		frame.getContentPane().add(printLabel);

		final JToggleButton cameraOnOff = new JToggleButton("Off");
		cameraOnOff.setFont(new Font("Tahoma", Font.BOLD, 13));
		cameraOnOff.setBounds(297, 151, 59, 23);
		frame.getContentPane().add(cameraOnOff);
		cameraOnOff.setSelected(true);
		cameraOnOff.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (cameraOnOff.isSelected()) {
					cameraOnOff.setText("Off");
					switchCameraOn(true);
				} else {
					cameraOnOff.setText("On");
					switchCameraOn(false);
				}
			}
		});

		// Light buttons & labels
		JLabel lightMainLabel = new JLabel("LIGHTS");
		lightMainLabel.setForeground(SystemColor.desktop);
		lightMainLabel.setBounds(257, 304, 85, 22);
		frame.getContentPane().add(lightMainLabel);
		lightMainLabel.setFont(new Font("Tahoma", Font.BOLD, 18));

		JLabel intensityLabel = new JLabel("Brightness");
		intensityLabel.setForeground(SystemColor.desktop);
		intensityLabel.setBounds(23, 222, 98, 25);
		frame.getContentPane().add(intensityLabel);
		intensityLabel.setFont(new Font("Tahoma", Font.BOLD, 14));

		JLabel office1light = new JLabel("Office 1");
		office1light.setForeground(SystemColor.desktop);
		office1light.setBounds(215, 341, 59, 21);
		frame.getContentPane().add(office1light);
		office1light.setFont(new Font("Tahoma", Font.BOLD, 14));

		JLabel office2light = new JLabel("Office 2");
		office2light.setForeground(SystemColor.desktop);
		office2light.setFont(new Font("Tahoma", Font.BOLD, 14));
		office2light.setBounds(215, 415, 59, 21);
		frame.getContentPane().add(office2light);

		JLabel receptionlight = new JLabel("Reception");
		receptionlight.setForeground(SystemColor.desktop);
		receptionlight.setFont(new Font("Tahoma", Font.BOLD, 14));
		receptionlight.setBounds(215, 485, 75, 21);
		frame.getContentPane().add(receptionlight);

		final JToggleButton of1lightbutton = new JToggleButton("Off");
		of1lightbutton.setFont(new Font("Tahoma", Font.BOLD, 13));
		of1lightbutton.setBounds(317, 341, 59, 23);
		frame.getContentPane().add(of1lightbutton);
		of1lightbutton.setSelected(true);
		of1lightbutton.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (of1lightbutton.isSelected()) {
					of1lightbutton.setText("Off");
					LightsOnOff(true);
				} else {
					of1lightbutton.setText("On");
					LightsOnOff(false);
				}
			}
		});

		final JToggleButton of2lightbutton = new JToggleButton("Off");
		of2lightbutton.setFont(new Font("Tahoma", Font.BOLD, 13));
		of2lightbutton.setSelected(true);
		of2lightbutton.setBounds(317, 415, 59, 23);
		frame.getContentPane().add(of2lightbutton);
		of2lightbutton.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (of2lightbutton.isSelected()) {
					of2lightbutton.setText("Off");
					LightsOnOff(true);
				} else {
					of2lightbutton.setText("On");
					LightsOnOff(false);
				}
			}
		});

		final JToggleButton reclightbutton = new JToggleButton("Off");
		reclightbutton.setFont(new Font("Tahoma", Font.BOLD, 13));
		reclightbutton.setSelected(true);
		reclightbutton.setBounds(317, 485, 59, 23);
		frame.getContentPane().add(reclightbutton);
		reclightbutton.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (reclightbutton.isSelected()) {
					reclightbutton.setText("Off");
					LightsOnOff(true);
				} else {
					reclightbutton.setText("On");
					LightsOnOff(false);
				}
			}
		});

		final JSlider sliderbritness = new JSlider(JSlider.HORIZONTAL, MIN, MAX, DEFAULT);
		sliderbritness.setFont(new Font("Tahoma", Font.BOLD, 10));
		sliderbritness.setBackground(new Color(153, 255, 255));
		sliderbritness.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				lightIntensity(sliderbritness.getValue());
			}
		});
		sliderbritness.setBounds(20, 247, 200, 39);
		frame.getContentPane().add(sliderbritness);
		sliderbritness.setMajorTickSpacing(1);
		sliderbritness.setPaintTicks(true);
		sliderbritness.setPaintLabels(true);
	}

	// gRPC & HVAC Services
	public static void hvacOnOff(boolean OnOffH) {

		SwitchRequest request = SwitchRequest.newBuilder().setPower(OnOffH).build();
		SwitchResponse response;

		try {
			response = cblockingStub.hvacOnOff(request);
		} catch (StatusRuntimeException e) {
			System.out.println("RPC HVAC failed:" + e.getStatus());
			return;
		}
		if (response.getPower()) {
			messages.setText("HVAC off");
		} else {
			messages.setText("HVAC on");
		}
	}

	public static void HvacTemperature(Object value) {
		final int newTemp = (Integer) value;

		HvacRequest request = HvacRequest.newBuilder().setTemp(newTemp).build();
		System.out.println("Requesting temperature change to " + newTemp + "°C");

		StreamObserver<HvacResponse> responseObserver = new StreamObserver<HvacResponse>() {
			public void onNext(HvacResponse value) {
				messages.setText("Changing temperature to: " + value.getTemp() + "°C");
			}

			public void onError(Throwable t) {
				t.printStackTrace();

			}

			public void onCompleted() {
				messages.setText("Room reached the selected temperature: " + newTemp + "°C");
			}
		};

		casyncStub.hvacTemperature(request, responseObserver);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void checkCO() {
		int CoNow = ClimateClient.randomCo;

		CoLevelRequest request = CoLevelRequest.newBuilder().setLevel(CoNow).build();

		ExtractionResponse response;
		try {
			response = cblockingStub.checkCO(request);
		} catch (StatusRuntimeException e) {
			System.out.println("RPC CO level failed:" + e.getStatus());
			return;
		}
		if (CoNow > 40) {
			messages.setText("CO level is: " + response.getLevel() + " now");
			messages.setText("High levels of CO, HVAC turned on");
		} else {
			messages.setText("CO level: " + response.getLevel());
			messages.setText("CO level Stable");
		}

	}

	// Light Services
	public void LightsOnOff(boolean onOffLights) {

		LightsRequest request = LightsRequest.newBuilder().setSwitch(onOffLights).build();

		LightsResponse response;
		try {
			response = lblockingStub.lightsOnOff(request);
		} catch (StatusRuntimeException e) {
			System.out.println("RPC light switch failed:" + e.getStatus());
			return;
		}

		Boolean statusLight = response.getSwitch();
		if (statusLight) {
			messages.setText("Lights off");
		} else {
			messages.setText("Lights on");
		}
	}

	public void lightIntensity(int bright) {
		final CountDownLatch finishLatch = new CountDownLatch(1);

		StreamObserver<IntensityRequest> requestObserver = lasyncStub.withDeadlineAfter(2, TimeUnit.SECONDS)
				.lightIntensity(new StreamObserver<IntensityResponse>() {
					public void onNext(IntensityResponse value) {
						messages.setText("Lights intensity changed: " + value.getIntensity());
					}

					public void onError(Throwable t) {
						t.printStackTrace();
						finishLatch.countDown();
					}

					public void onCompleted() {
						finishLatch.countDown();
					}
				});

		try {
			requestObserver.onNext(IntensityRequest.newBuilder().setIntensity(bright).build());
			messages.setText("Lighting adjustment completed");
			Thread.sleep(1500);
		} catch (RuntimeException e) {
			requestObserver.onError(e);
			throw e;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		requestObserver.onCompleted();
	}

	// Device Services
	public static void switchDevices(boolean onOffDevices) {

		DevicesRequest request = DevicesRequest.newBuilder().setDevices(onOffDevices).build();

		DevicesResponse response;
		try {
			response = ublockingStub.switchDevices(request);
		} catch (StatusRuntimeException e) {
			System.out.println("RPC devices failed:" + e.getStatus());
			return;
		}
		if (response.getDevices()) {
			messages.setText("Devices off");
		} else {
			messages.setText("Devices on");
		}
	}

	public static void switchCameraOn(boolean onOffCamera) {

		CameraRequest request = CameraRequest.newBuilder().setCamera(onOffCamera).build();

		CameraResponse response;
		try {
			response = ublockingStub.switchCameraOn(request);
		} catch (StatusRuntimeException e) {
			System.out.println("RPC camera failed:" + e.getStatus());
			return;
		}
		if (response.getCamera()) {
			messages.setText("Camera off");
		} else {
			messages.setText("Camera on");
		}
	}
	
	// Prints list of visitors
	public static void printList() {

		final ArrayList<String> listOfVisits = new ArrayList<String>();

		StreamObserver<PrinterResponse> responseObserver = new StreamObserver<PrinterResponse>() {
			public void onNext(PrinterResponse value) {
				listOfVisits.add(value.getPList());
			}

			public void onError(Throwable t) {
				System.out.println("gRPC printer failed: " + t.getMessage());
				t.printStackTrace();
			}

			public void onCompleted() {
				System.out.println("List completed");
				messages.setText("List completed");
			}
		};

		StreamObserver<PrinterRequest> requestObserver = uasyncStub.printList(responseObserver);
		try {
			requestObserver.onNext(PrinterRequest.newBuilder().setPList("Karl Tallon").build());
			requestObserver.onNext(PrinterRequest.newBuilder().setPList("Caroline Byrne").build());
			requestObserver.onNext(PrinterRequest.newBuilder().setPList("Matthew Dwyer").build());
			requestObserver.onNext(PrinterRequest.newBuilder().setPList("Athanasios Staikopoulos").build());
			requestObserver.onNext(PrinterRequest.newBuilder().setPList("Magnum PI").build());

			Thread.sleep(1000);
		} catch (RuntimeException e) {
			System.out.println("RPC printer failed: " + e.getMessage());
			requestObserver.onError(e);
			throw e;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		requestObserver.onCompleted();
		messages.setText("\nVisitors List: " + listOfVisits.size());
		for (String visits : listOfVisits) {
			messages.setText("Printing Visitors List: ");
			JOptionPane.showMessageDialog(null, visits);
			System.out.println("Printing Visitors List: " + visits);
		}
	}
}