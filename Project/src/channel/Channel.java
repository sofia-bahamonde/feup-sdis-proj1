package channel;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashSet;
import java.util.Hashtable;

import messages.MsgHandler;
import peer.Chunk;


public class Channel implements Runnable{
	
	public MulticastSocket mcast_socket;
	public InetAddress mcast_addr;
	public int mcast_port;
	
	private Hashtable<String,HashSet<Integer>> logs;
	
	public Channel(String address, String port) throws IOException{
		
		this.mcast_addr = InetAddress.getByName(address);
		this.mcast_port = Integer.parseInt(port);
		
		mcast_socket = new MulticastSocket(mcast_port);
		mcast_socket.setTimeToLive(1);
		mcast_socket.joinGroup(mcast_addr);
		
		logs=new Hashtable<String,HashSet<Integer>>();
				
	}

	public void run(){

		byte[] buf = new byte[1024];
		boolean done = false;
		
		// receive messages
		while (!done) {
			try {
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				mcast_socket.receive(packet);
				new Thread(new MsgHandler(packet)).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// close socket
		mcast_socket.close();
		
	}

	public synchronized void sendMessage(byte[] msg) {

	        DatagramPacket packet = new DatagramPacket(msg, msg.length, mcast_addr, mcast_port);

	        try {
	            mcast_socket.send(packet);
	        } catch (IOException e) {
	        	e.printStackTrace();
	        }
	}


	public void startSave(Chunk chunk) {
		logs.put(chunk.getID(), new HashSet<Integer>());
	}
	
	public int getSaves(Chunk chunk) {
		return logs.get(chunk.getID()).size();
	}
	
	public void stopSave(Chunk chunk) {
		logs.remove(chunk.getID());
	}
	
	public void save(Chunk chunk, int peer_id) {
		if (logs.containsKey(chunk.getID()))
				logs.get(chunk.getID()).add(peer_id);
	}
	
	
	

	
	
}