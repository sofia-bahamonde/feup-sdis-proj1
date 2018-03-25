
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class TestApp {

    private TestApp() {}

    public static void main(String[] args) {
    	
    	String peer_ap = args[0];
        //String operation = args[1].toUpperCase();

        try {
            Registry registry = LocateRegistry.getRegistry("localhost");
            InterfaceRMI stub = (InterfaceRMI) registry.lookup(peer_ap);
            String response = stub.backup();
            System.out.println("response: " + response);
        } catch (Exception e) {
            System.err.println("Error:: Connection with access point '" + peer_ap + "' failed");
            return;
        }
    }
}
