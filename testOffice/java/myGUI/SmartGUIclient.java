package myGUI;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import jmDNS.ServiceRegistration;

public class SmartGUIclient {

	private JFrame frame;
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
	}

	// Initialize the frame
	private void initialize() {

		// Frame, labels & separators
		frame = new JFrame("Smart Office Dashboard");
		frame.getContentPane().setForeground(new Color(255, 228, 225));
		frame.setBounds(100, 100, 406, 590);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().setBackground(SystemColor.inactiveCaptionBorder);

		JLabel TitleLabel = new JLabel("Tallon Smart Office Solutions");
		TitleLabel.setForeground(SystemColor.desktop);
		TitleLabel.setFont(new Font("Microsoft Tai Le", Font.BOLD | Font.ITALIC, 26));
		TitleLabel.setBounds(15, 0, 385, 52);
		frame.getContentPane().add(TitleLabel);

		JLabel dashWelcome = new JLabel("Welcome to your Dashboard!");
		dashWelcome.setForeground(SystemColor.desktop);
		dashWelcome.setBounds(100, 50, 218, 16);
		frame.getContentPane().add(dashWelcome);
		dashWelcome.setFont(new Font("Tahoma", Font.BOLD, 15));

		JLabel logo = new JLabel("");
		logo.setBounds(2, 390, 166, 128);
		frame.getContentPane().add(logo);
		ImageIcon img = new ImageIcon(this.getClass().getResource("/logo.png"));
		logo.setIcon(img);

		JLabel controls = new JLabel("CONTROLS");
		controls.setForeground(SystemColor.desktop);
		controls.setBounds(158, 198, 100, 25);
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
		monitor.setBounds(46, 304, 105, 25);
		frame.getContentPane().add(monitor);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 77, 395, 2);
		frame.getContentPane().add(separator_1);

		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(10, 184, 377, 2);
		frame.getContentPane().add(separator_2);

		JSeparator separator_2_1 = new JSeparator();
		separator_2_1.setBounds(10, 291, 377, 2);
		frame.getContentPane().add(separator_2_1);

		JSeparator separator_3 = new JSeparator();
		separator_3.setOrientation(SwingConstants.VERTICAL);
		separator_3.setBounds(194, 306, 2, 212);
		frame.getContentPane().add(separator_3);

		messages = new JTextField();
		messages.setBackground(SystemColor.inactiveCaptionBorder);
		messages.setBounds(2, 518, 385, 25);
		frame.getContentPane().add(messages);
		messages.setColumns(10);

		// Climate buttons and labels
		JLabel hvacLabel = new JLabel("HVAC");
		hvacLabel.setForeground(SystemColor.desktop);
		hvacLabel.setBounds(179, 126, 41, 14);
		frame.getContentPane().add(hvacLabel);
		hvacLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JLabel coLabel = new JLabel("CO level");
		coLabel.setForeground(SystemColor.desktop);
		coLabel.setBounds(32, 341, 65, 14);
		frame.getContentPane().add(coLabel);
		coLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JLabel tempLabel = new JLabel("Temperature");
		tempLabel.setForeground(SystemColor.desktop);
		tempLabel.setBounds(277, 233, 79, 14);
		frame.getContentPane().add(tempLabel);
		tempLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JButton coButton = new JButton("Check");
		coButton.setFont(new Font("Tahoma", Font.BOLD, 11));
		coButton.setBounds(91, 339, 75, 23);
		coButton.setBorderPainted(false);
		coButton.setBackground(SystemColor.activeCaption);
		frame.getContentPane().add(coButton);
		coButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkCO();
			}
		});

		final JToggleButton hvacOn = new JToggleButton("Off");
		hvacOn.setFont(new Font("Tahoma", Font.BOLD, 11));
		hvacOn.setBounds(166, 151, 65, 23);
		frame.getContentPane().add(hvacOn);
		hvacOn.setSelected(true);
		hvacOn.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (hvacOn.isSelected()) {
					hvacOn.setText("Off");
				} else {
					hvacOn.setText("On");
				}
			}
		});

		final JSpinner HTemp = new JSpinner();
		HTemp.setModel(new SpinnerNumberModel(15, 15, 35, 1));
		HTemp.setBounds(264, 258, 43, 22);
		frame.getContentPane().add(HTemp);

		JButton btnNewButton = new JButton("OK");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HvacTemperature(HTemp.getValue());
			}
		});
		
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 8));
		coButton.setBorderPainted(false);
		coButton.setBackground(SystemColor.activeCaption);
		btnNewButton.setBounds(317, 258, 46, 23);
		frame.getContentPane().add(btnNewButton);

		// Utility buttons and labels
		JLabel deviceLabel = new JLabel("Devices");
		deviceLabel.setForeground(SystemColor.desktop);
		deviceLabel.setBounds(44, 126, 93, 14);
		frame.getContentPane().add(deviceLabel);
		deviceLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JLabel VisitLabel = new JLabel("Visit list");
		VisitLabel.setForeground(SystemColor.desktop);
		VisitLabel.setBounds(32, 381, 59, 14);
		frame.getContentPane().add(VisitLabel);
		VisitLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JLabel cameraLabel = new JLabel("Cameras");
		cameraLabel.setForeground(SystemColor.desktop);
		cameraLabel.setBounds(304, 126, 59, 14);
		frame.getContentPane().add(cameraLabel);
		cameraLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));

		final JToggleButton deviceOnOff = new JToggleButton("Off");
		deviceOnOff.setFont(new Font("Tahoma", Font.BOLD, 11));
		deviceOnOff.setBounds(32, 150, 62, 23);
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
		printLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		printLabel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				printList();
			}
		});
		
		printLabel.setBorderPainted(false);
		printLabel.setBackground(SystemColor.activeCaption);
		printLabel.setBounds(91, 379, 75, 23);
		frame.getContentPane().add(printLabel);

		final JToggleButton cameraOnOff = new JToggleButton("Off");
		cameraOnOff.setFont(new Font("Tahoma", Font.BOLD, 11));
		cameraOnOff.setBounds(304, 151, 59, 23);
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

		// Light buttons and labels
		JLabel lightMainLabel = new JLabel("LIGHTS");
		lightMainLabel.setForeground(SystemColor.desktop);
		lightMainLabel.setBounds(257, 304, 85, 22);
		frame.getContentPane().add(lightMainLabel);
		lightMainLabel.setFont(new Font("Tahoma", Font.BOLD, 18));

		JLabel intensityLabel = new JLabel("Brightness");
		intensityLabel.setForeground(SystemColor.desktop);
		intensityLabel.setBounds(32, 222, 75, 14);
		frame.getContentPane().add(intensityLabel);
		intensityLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JLabel office1light = new JLabel("Office 1");
		office1light.setForeground(SystemColor.desktop);
		office1light.setBounds(206, 364, 59, 14);
		frame.getContentPane().add(office1light);
		office1light.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JLabel office2light = new JLabel("Office 2");
		office2light.setForeground(SystemColor.desktop);
		office2light.setFont(new Font("Tahoma", Font.PLAIN, 14));
		office2light.setBounds(206, 415, 59, 14);
		frame.getContentPane().add(office2light);

		JLabel receptionlight = new JLabel("Reception");
		receptionlight.setForeground(SystemColor.desktop);
		receptionlight.setFont(new Font("Tahoma", Font.PLAIN, 14));
		receptionlight.setBounds(206, 465, 75, 14);
		frame.getContentPane().add(receptionlight);

		final JToggleButton of1lightbutton = new JToggleButton("Off");
		of1lightbutton.setFont(new Font("Tahoma", Font.BOLD, 11));
		of1lightbutton.setBounds(297, 362, 59, 23);
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
		of2lightbutton.setFont(new Font("Tahoma", Font.BOLD, 11));
		of2lightbutton.setSelected(true);
		of2lightbutton.setBounds(297, 413, 59, 23);
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
		reclightbutton.setFont(new Font("Tahoma", Font.BOLD, 11));
		reclightbutton.setSelected(true);
		reclightbutton.setBounds(297, 463, 59, 23);
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
	public static void hvacOnOff() {

	}

	public static void HvacTemperature(Object value) {

	}

	public void checkCO() {

	}

	// Light Services
	public void LightsOnOff(boolean onOffLights) {

	}

	public void lightIntensity(int bright) {
		
	}

	// Device Services
	public static void switchDevices(boolean onOffDevices) {

	}

	public static void switchCameraOn(boolean onOffCamera) {

	}

	public static void printList() {

	}
}