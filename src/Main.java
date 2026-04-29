import client.VPNClient;

public class Main {

    public static void main(String[] args) {

        VPNClient client = new VPNClient();

        client.connect("127.0.0.1", 5000);

        client.receiveMessage();

        client.sendMessage("Hello VPN Server!");
        client.sendMessage("This is secure test");

    }
}