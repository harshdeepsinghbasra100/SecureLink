package client;

import java.awt.*;
import javax.swing.*;
import utils.LoggerUtil;

public class VPNClientUI extends JFrame {

    private JTextField ipField, portField, messageField;
    private JTextArea outputArea, logsArea;
    private JButton connectBtn, sendBtn, disconnectBtn;

    private JLabel statusLabel, msgCountLabel;
    private JLabel connectionStatusLabel, serverIPLabel, lastActivityLabel;

    private VPNClient client;
    private int messageCount = 0;
    private String selectedIP = "127.0.0.1";
    private int selectedPort = 5000;
    private String ipType = "Localhost";

    public VPNClientUI() {
        setTitle("Java VPN Client");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        client = new VPNClient();

        // ================= TABS =================
        JTabbedPane tabs = new JTabbedPane();

        JPanel connectionPanel = new JPanel(new BorderLayout());
        JPanel logsPanel = new JPanel(new BorderLayout());
        JPanel settingsPanel = new JPanel();
        JPanel dashboardPanel = new JPanel();

        // ================= TOP PANEL =================
        JPanel topPanel = new JPanel();

        ipField = new JTextField("127.0.0.1", 10);
        portField = new JTextField("5000", 5);

        connectBtn = new JButton("Connect");
        disconnectBtn = new JButton("Disconnect");

        statusLabel = new JLabel("Disconnected ❌");

        connectBtn.setBackground(Color.GREEN);
        disconnectBtn.setBackground(Color.RED);

        topPanel.add(new JLabel("IP:"));
        topPanel.add(ipField);
        topPanel.add(new JLabel("Port:"));
        topPanel.add(portField);
        topPanel.add(connectBtn);
        topPanel.add(disconnectBtn);
        topPanel.add(statusLabel);

        connectionPanel.add(topPanel, BorderLayout.NORTH);

        // ================= OUTPUT =================
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setBackground(Color.BLACK);
        outputArea.setForeground(Color.GREEN);

        connectionPanel.add(new JScrollPane(outputArea), BorderLayout.CENTER);

        // ================= BOTTOM =================
        JPanel bottomPanel = new JPanel();

        messageField = new JTextField(25);
        sendBtn = new JButton("Send");
        sendBtn.setBackground(Color.CYAN);

        bottomPanel.add(messageField);
        bottomPanel.add(sendBtn);

        connectionPanel.add(bottomPanel, BorderLayout.SOUTH);

        // ================= LOGS PANEL =================
        logsArea = new JTextArea();
        logsArea.setEditable(false);
        logsArea.setBackground(Color.BLACK);
        logsArea.setForeground(Color.GREEN);
        logsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        logsPanel.add(new JScrollPane(logsArea), BorderLayout.CENTER);

        // ================= DASHBOARD =================
        dashboardPanel.setLayout(new GridLayout(4, 1));

        msgCountLabel = new JLabel("Messages Sent: 0");
        connectionStatusLabel = new JLabel("Status: Disconnected");
        serverIPLabel = new JLabel("Server IP: -");
        lastActivityLabel = new JLabel("Last Activity: -");

        dashboardPanel.add(msgCountLabel);
        dashboardPanel.add(connectionStatusLabel);
        dashboardPanel.add(serverIPLabel);
        dashboardPanel.add(lastActivityLabel);

        // ================= SETTINGS =================
        // ================= SETTINGS =================
        settingsPanel.setLayout(new GridLayout(6, 2));

        // Radio buttons
        JRadioButton localRadio = new JRadioButton("Localhost", true);
        JRadioButton networkRadio = new JRadioButton("Same Network");

        ButtonGroup group = new ButtonGroup();
        group.add(localRadio);
        group.add(networkRadio);

        // Fields
        JTextField networkIPField = new JTextField("192.168.1.1", 10);
        JTextField portFieldSetting = new JTextField("5000", 5);

        // Disable IP field initially (localhost)
        networkIPField.setEnabled(false);

        // Toggle enable/disable
        localRadio.addActionListener(e -> networkIPField.setEnabled(false));
        networkRadio.addActionListener(e -> networkIPField.setEnabled(true));

        // Save button
        JButton saveBtn = new JButton("Save Settings");

        // Layout
        settingsPanel.add(new JLabel("IP Type:"));
        settingsPanel.add(new JLabel(""));

        settingsPanel.add(localRadio);
        settingsPanel.add(networkRadio);

        settingsPanel.add(new JLabel("Network IP:"));
        settingsPanel.add(networkIPField);

        settingsPanel.add(new JLabel("Port:"));
        settingsPanel.add(portFieldSetting);

        settingsPanel.add(new JLabel(""));
        settingsPanel.add(saveBtn);

// Save logic
saveBtn.addActionListener(e -> {

    if (localRadio.isSelected()) {
        ipType = "Localhost";
        selectedIP = "127.0.0.1";
    } else {
        ipType = "Network";
        selectedIP = networkIPField.getText();
    }

    selectedPort = Integer.parseInt(portFieldSetting.getText());

    // Update main UI fields
    ipField.setText(selectedIP);
    portField.setText(String.valueOf(selectedPort));

    JOptionPane.showMessageDialog(this, "Settings Saved!");
});

        // ================= ADD TABS =================
        tabs.add("Connection", connectionPanel);
        tabs.add("Logs", logsPanel);
        tabs.add("Dashboard", dashboardPanel);
        tabs.add("Settings", settingsPanel);

        add(tabs);

        // ================= BUTTON ACTIONS =================
        connectBtn.addActionListener(e -> connectToServer());
        sendBtn.addActionListener(e -> sendMessage());
        disconnectBtn.addActionListener(e -> disconnect());

        // ================= MESSAGE LISTENER =================
        client.setMessageListener(message -> {
            outputArea.append("Server: " + message + "\n");
            log("RECEIVED", message);
        });

        setVisible(true);
    }

    // ================= LOG METHOD =================
    private void log(String type, String message) {
        String logMsg = "[" + type + "] " + message + "\n";
        logsArea.append(logMsg);
    }

    private void connectToServer() {
        String ip = selectedIP;
        int port = selectedPort;

        client.connect(ip, port);
        client.receiveMessage();

        statusLabel.setText("Connected 🟢");
        outputArea.append("Connected to server\n");

        connectionStatusLabel.setText("Status: Connected");
        serverIPLabel.setText("Server IP: " + ip);
        lastActivityLabel.setText("Last Activity: Connected");

        log("INFO", "Connected to server");
        LoggerUtil.log("Connected to server");
    }

    
    private void sendMessage() {
        String msg = messageField.getText();

        if (msg.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Message cannot be empty");
            return;
        }

        client.sendMessage(msg);
        outputArea.append("You: " + msg + "\n");

        log("SENT", msg);
        LoggerUtil.log("Sent: " + msg);

        // Intrusion detection (improved)
        String lower = msg.toLowerCase();
        if (lower.contains("hack") || lower.contains("attack") || lower.contains("malware") || lower.contains("malicious")) {
            log("ALERT", "Suspicious message detected: " + msg);
            LoggerUtil.log("⚠️ Suspicious message detected: " + msg);
        }

        messageCount++;
        msgCountLabel.setText("Messages Sent: " + messageCount);
        lastActivityLabel.setText("Last Activity: Sent message");

        messageField.setText("");
    }

    private void disconnect() {
        client.disconnect();

        statusLabel.setText("Disconnected 🔴");
        outputArea.append("Disconnected\n");

        connectionStatusLabel.setText("Status: Disconnected");
        lastActivityLabel.setText("Last Activity: Disconnected");

        log("INFO", "Disconnected from server");
        LoggerUtil.log("Disconnected from server");
    }
}