package peer;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import channel.Channel;
import channel.MDRChannel;
import common.InterfaceRMI;
import messages.MsgForwarder;
import protocols.Backup;
import protocols.Delete;
import protocols.Restore;

public class Peer implements InterfaceRMI {
	
	private static double version;
    private static int server_id;
    private static String peer_ap;
    
    private static Channel MC;
    private static Channel MDB;
    private static MDRChannel MDR;
    
    private static MsgForwarder msg_forwarder;
    
	public static String CHUNKS  = "CHUNKS_";
	public static String RESTORES  = "RESTORES_";
	public static String DISK = "disk_";
	
	private static Disk disk;

	
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
        MC = new Channel(args[3], args[4]);
        MDB = new Channel(args[5], args[6]);
        MDR = new MDRChannel(args[7], args[8]);
        
        // start channel threads
        new Thread(MC).start();
        new Thread(MDB).start();
        new Thread(MDR).start();

        msg_forwarder = new MsgForwarder(version);
        
        CHUNKS += server_id + "/";
        RESTORES += server_id + "/";
        DISK += server_id + ".data";
        
        loadDisk();
        
        
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


	private static void loadDisk() throws IOException {

		FileInputStream stream;
		try {
		
			stream = new FileInputStream(DISK);
			ObjectInputStream in = new ObjectInputStream(stream);
			disk = (Disk) in.readObject();
			in.close();
			
		} catch (FileNotFoundException e) {
			disk = new Disk();
			saveDisk();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	public static void saveDisk() throws IOException {
		FileOutputStream stream = new FileOutputStream(Peer.DISK);

		ObjectOutputStream out = new ObjectOutputStream(stream);

		out.writeObject(disk);

		out.close();
		
	}

	public static Channel getMDB() {
		return MDB;
	}
	
	public static MDRChannel getMDR() {
		return MDR;
	}
	
	public static Channel getMC() {
		return MC;
	}

	
	public static MsgForwarder getMsgForwarder(){
		return msg_forwarder;
	}

	public static int getServerID() {
		return server_id;
	}

	
	@Override
	public void backup(String file_path, int rep_degree) throws RemoteException {
		Backup inititator = new Backup(file_path,rep_degree);
		new Thread(inititator).start();
	}

	@Override
	public void delete(String file_path) throws RemoteException {
		Delete initiator = new Delete(file_path);
		new Thread(initiator).start();
	}

	@Override
	public void restore(String file_path) throws RemoteException {
		Restore initiator = new Restore(file_path);
		new Thread(initiator).start();
		
	}

	@Override
	public void state() throws RemoteException {
		System.out.println("\nPEER STATE");
		System.out.println("Disk memory capacity: " + disk.getFreeMem()+ " KBytes");
		System.out.println("Disk memory used: " + disk.getUsedMem()+ " KBytes");
		
		if(disk.hasChunks()) {
			ArrayList<Chunk> chunks = disk.getStoredChunks();
			
			System.out.println("\nCHUNKS");
			for(int i=0; i < chunks.size(); i++) {
				System.out.println("ID: "+ chunks.get(i).getID());
				System.out.println("SIZE: "+ chunks.get(i).getData().length);
				System.out.println("REP DEGREE: " + chunks.get(i).getRepDegree()); 
			}
		}
		
	}

	public static Disk getDisk() {
		return disk;
	}




}
