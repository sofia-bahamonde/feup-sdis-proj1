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
        
        String file_path;
        
        switch(sub_protocol){
        	case "BACKUP":
	        	if(args.length != 4) {
	        		System.out.println("Error: Invalid arguements");
	        		System.out.println("Usage: TestApp <peer_ap> BACKUP <file_path> <rep_degree>");
	        	}
	        	
	        	file_path = args[2];
	        	int rep_degree = Integer.parseInt(args[3]);
	        	
	        	if(rep_degree >9) {
	        		System.out.println("Error: Invalid replication degree");
	        		System.out.println("Must be an integer lower than 10");
	        	}
	        	

	            System.out.println("file path: " + file_path);
	            System.out.println("replication degree: " + rep_degree);

	        	
	        	try {
	               stub.backup(file_path, rep_degree);
	               System.out.println("\nSent");
	            } catch (RemoteException e) {
	            	 System.err.println("Backup exception: " + e.toString());
	                 e.printStackTrace();
	            }
	        	break;
        	case "DELETE":
        		if(args.length != 3) {
	        		System.out.println("Error: Invalid arguements");
	        		System.out.println("Usage: TestApp <peer_ap> DELETE <file_path>");
	        	}
        		
        		file_path = args[2];
        		
        		try {
 	               stub.delete(file_path);
 	               System.out.println("\nSent");
 	            } catch (RemoteException e) {
 	            	 System.err.println("Delete exception: " + e.toString());
 	                 e.printStackTrace();
 	            }
 	        	break;
        	case "RESTORE":
        		if(args.length != 3) {
	        		System.out.println("Error: Invalid arguements");
	        		System.out.println("Usage: TestApp <peer_ap> RESTORE <file_path>");
	        	}
        		
        		file_path = args[2];
        		
        		try {
 	               stub.restore(file_path);
 	               System.out.println("\nSent");
 	            } catch (RemoteException e) {
 	            	 System.err.println("Delete exception: " + e.toString());
 	                 e.printStackTrace();
 	            }
 	        	break;
        	
        	
        	default:
        		System.out.println("Error: Invalid subprotocol");
        		System.out.println("Subprotocols - BACKUP RESTORE DELETE RECLAIM STATE");
        
        }
    }
}
