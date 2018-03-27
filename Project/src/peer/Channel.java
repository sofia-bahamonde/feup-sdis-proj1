package peer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;


public class Channel implements Runnable{
	
	public MulticastSocket mcast_socket;
	public InetAddress mcast_addr;
	public int mcast_port;
	
	public Channel(String address, String port) throws IOException{
		
		this.mcast_addr = InetAddress.getByName(address);
		this.mcast_port = Integer.parseInt(port);
		
		mcast_socket = new MulticastSocket(mcast_port);
		mcast_socket.setTimeToLive(1);
		mcast_socket.joinGroup(mcast_addr);
		
				
	}

	public void run(){

		byte[] buf = new byte[1024];
		boolean done = false;
		
		// receive messages
		while (!done) {
			try {
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				mcast_socket.receive(packet);
				String msg = new String(buf, 0, buf.length);
				System.out.println(msg);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// close socket
		mcast_socket.close();
		
	}
	
	 public synchronized void sendMessage(String msg) {

		 	
	        DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.getBytes().length, mcast_addr, mcast_port);

	        try {
	            mcast_socket.send(packet);
	        } catch (IOException e) {
	        	e.printStackTrace();
	        }
	}
	
}