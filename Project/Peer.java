        
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
        
public class Peer implements InterfaceRMI {
        
 
    public String backup() {
        return "BACKUP";
    }
        
    public static void main(String args[]) throws IOException {
    	
    	// variable initialization
    	double version = Double.parseDouble(args[0]);
        double server_id = Integer.parseInt(args[1]);
        String peer_ap = args[2];
        
        System.out.println("version : " + version);
        System.out.println("server_id : " + server_id);
        System.out.println("access point : " + peer_ap);
        
        // communication channels initialization
        Channel MC = new Channel(args[3], args[4]);
        
        System.out.println();
        System.out.println("MC : " + args[3] + " " + args[4]);
        System.out.println();
        
        if(server_id==2) MC.sendMessage();
        
        new Thread(MC).start();
        
  
        
        
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
