        
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
        
public class Peer implements InterfaceRMI {
        
    public Peer() {}

    public String backup() {
        return "Hello, world!";
    }
        
    public static void main(String args[]) {
    	
    	double version = Double.parseDouble(args[0]);
        double server_id = Integer.parseInt(args[1]);
        String peer_ap = args[2];
    	
        System.out.println("version : " + version);
        System.out.println("server_id : " + server_id);
        System.out.println("acces point : " + peer_ap);
        
        try {
            Peer obj = new Peer();
            InterfaceRMI stub = (InterfaceRMI) UnicastRemoteObject.exportObject(obj, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(peer_ap, stub);

            System.err.println("Peer ready");
        } catch (Exception e) {
            System.err.println("Peer exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
