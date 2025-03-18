import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// Device Interface
interface Device {
    void executeCommand(String command);
    String getStatus();
    double getEnergyConsumption();
}

// Smart Light Class
class SmartLight implements Device {
    private boolean isOn;
    private String location;
    private double energyUsage;

    public SmartLight(String location) {
        this.location = location;
        this.isOn = false;
        this.energyUsage = 0.0;
    }

    @Override
    public void executeCommand(String command) {
        if (command.equalsIgnoreCase("ON")) {
            isOn = true;
            energyUsage += 5.0;
        } else if (command.equalsIgnoreCase("OFF")) {
            isOn = false;
        }
    }

    @Override
    public String getStatus() {
        return "Light [" + location + "] is " + (isOn ? "ON" : "OFF");
    }

    @Override
    public double getEnergyConsumption() {
        return energyUsage;
    }
}

// Smart AC Class
class SmartAC implements Device {
    private boolean isOn;
    private double temperature;
    private double energyUsage;

    public SmartAC() {
        this.isOn = false;
        this.temperature = 24.0;
        this.energyUsage = 0.0;
    }

    @Override
    public void executeCommand(String command) {
        if (command.equalsIgnoreCase("ON")) {
            isOn = true;
            energyUsage += 50.0;
        } else if (command.equalsIgnoreCase("OFF")) {
            isOn = false;
        } else if (command.startsWith("SET_TEMP")) {
            try {
                double newTemp = Double.parseDouble(command.split(" ")[1]);
                this.temperature = newTemp;
            } catch (Exception e) {
                System.out.println("Invalid temperature command.");
            }
        }
    }

    @Override
    public String getStatus() {
        return "Smart AC is " + (isOn ? "ON at " + temperature + "°C" : "OFF");
    }

    @Override
    public double getEnergyConsumption() {
        return energyUsage;
    }
}

// Smart Door Lock Class
class SmartDoorLock implements Device {
    private boolean isLocked;

    public SmartDoorLock() {
        this.isLocked = true;
    }

    @Override
    public void executeCommand(String command) {
        if (command.equalsIgnoreCase("LOCK")) {
            isLocked = true;
        } else if (command.equalsIgnoreCase("UNLOCK")) {
            isLocked = false;
        }
    }

    @Override
    public String getStatus() {
        return "Door is " + (isLocked ? "LOCKED" : "UNLOCKED");
    }

    @Override
    public double getEnergyConsumption() {
        return 0.5;
    }
}

// Smart Home Controller
class SmartHomeController {
    private List<Device> devices;
    private double totalEnergyConsumption;

    public SmartHomeController() {
        devices = new ArrayList<>();
        totalEnergyConsumption = 0.0;
    }

    public void addDevice(Device device) {
        devices.add(device);
    }

    public void executeCommand(String command) {
        for (Device device : devices) {
            device.executeCommand(command);
        }
        totalEnergyConsumption = devices.stream().mapToDouble(Device::getEnergyConsumption).sum();
    }

    public String getAllStatus() {
        StringBuilder sb = new StringBuilder();
        for (Device device : devices) {
            sb.append(device.getStatus()).append("\n");
        }
        sb.append("Total Energy Used: ").append(totalEnergyConsumption).append("W\n");
        return sb.toString();
    }
}

// GUI for Smart Home Simulator
public class SmartHomeSimulator extends JFrame {
    private SmartHomeController controller;
    private JTextArea statusArea;

    public SmartHomeSimulator() {
        controller = new SmartHomeController();

        // Adding devices
        controller.addDevice(new SmartLight("Living Room"));
        controller.addDevice(new SmartAC());
        controller.addDevice(new SmartDoorLock());

        // GUI Setup
        setTitle("Smart Home Manager");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Status Display
        statusArea = new JTextArea(10, 30);
        statusArea.setEditable(false);
        add(new JScrollPane(statusArea), BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        JButton btnOn = new JButton("Turn ON Devices");
        JButton btnOff = new JButton("Turn OFF Devices");
        JButton btnLock = new JButton("Lock Door");
        JButton btnUnlock = new JButton("Unlock Door");
        JButton btnCheck = new JButton("Check Status");

        buttonPanel.add(btnOn);
        buttonPanel.add(btnOff);
        buttonPanel.add(btnLock);
        buttonPanel.add(btnUnlock);
        buttonPanel.add(btnCheck);

        // Button Actions
        btnOn.addActionListener(e -> {
            controller.executeCommand("ON");
            updateStatus();
        });

        btnOff.addActionListener(e -> {
            controller.executeCommand("OFF");
            updateStatus();
        });

        btnLock.addActionListener(e -> {
            controller.executeCommand("LOCK");
            updateStatus();
        });

        btnUnlock.addActionListener(e -> {
            controller.executeCommand("UNLOCK");
            updateStatus();
        });

        btnCheck.addActionListener(e -> updateStatus());

        updateStatus();
    }

    private void updateStatus() {
        statusArea.setText(controller.getAllStatus());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SmartHomeSimulator().setVisible(true));
    }
}
