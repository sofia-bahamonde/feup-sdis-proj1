package test;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import common.InterfaceRMI;

public class TestApp {
	
	static InterfaceRMI stub;
	
    private TestApp() {}

    public static void main(String[] args) {
    	
    	// main variables initialization
    	String peer_ap = args[0];
        String sub_protocol = args[1].toUpperCase();
        
        System.out.println("access point: " + peer_ap);
        System.out.println("subprotocol: " + sub_protocol);

        // establishing connection
        try {
            Registry registry = LocateRegistry.getRegistry("localhost");
            stub = (InterfaceRMI) registry.lookup(peer_ap);

        } catch (Exception e) {
        	System.err.println("RMI exception: " + e.toString());
            e.printStackTrace();
        }
        
        switch(sub_protocol){
        	case "BACKUP":
	        	if(args.length != 4) {
	        		System.out.println("Error: Invalid arguements");
	        		System.out.println("Usage: TestApp <peer_ap> <sub_protocol> <file_path> <rep_degree>");
	        	}
	        	
	        	String file_path = args[2];
	        	int rep_degree = Integer.parseInt(args[3]);
	        	
	        	if(rep_degree >9) {
	        		System.out.println("Error: Invalid replication degree");
	        		System.out.println("Must be an integer lower than 10");
	        	}
	        	
	        	try {
	               stub.backup(file_path, rep_degree);
	            } catch (RemoteException e) {
	            	 System.err.println("Backup exception: " + e.toString());
	                 e.printStackTrace();
	            }
	        	break;
            
            
        	
        	
        	
        	default:
        		System.out.println("Error: Invalid subprotocol");
        		System.out.println("Subprotocols - BACKUP RESTORE DELETE RECLAIM STATE");
        
        }
    }
}
