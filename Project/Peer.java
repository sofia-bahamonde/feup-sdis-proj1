        
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
        
public class Peer implements InterfaceRMI {
	
	static double version;
    static double server_id;
    static String peer_ap;
    
    Channel MC;
    Channel MDB;
    Channel MDR;
        
 
    public String backup() {
        return "BACKUP";
    }
        
    public static void main(String args[]) throws IOException {
    	
    	initialize(args);
    	

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

	private static void initialize(String args[]) throws IOException {

		// variable initialization
    	version = Double.parseDouble(args[0]);
        server_id = Integer.parseInt(args[1]);
        peer_ap = args[2];
        
        // communication channels initialization
        Channel MC = new Channel(args[3], args[4]);
        Channel MDB = new Channel(args[5], args[6]);
        Channel MDR = new Channel(args[7], args[8]);
        
        // start channel threads
        new Thread(MC).start();
        new Thread(MDB).start();
        new Thread(MDR).start();
        
        
        // print main info 
        System.out.println("version : " + version);
        System.out.println("server_id : " + server_id);
        System.out.println("access point : " + peer_ap);
        
        System.out.println();
        
        System.out.println("MC  : " + args[3] + " " + args[4]);
        System.out.println("MDB : " + args[5] + " " + args[6]);
        System.out.println("MDR : " + args[7] + " " + args[8]);
        
        System.out.println();
		
	}
}
