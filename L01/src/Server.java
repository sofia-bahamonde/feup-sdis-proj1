import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Server {

  public static void main(String[] args) throws IOException{
	  
	  if(args.length!=1) {
		  System.out.println("ERROR - Invalid arguments");
		  System.out.println("Usage - Server <port_number>");
		  return;
	  }
	  
	  int r_port = Integer.parseInt(args[0]);
	  DatagramSocket socket = new DatagramSocket(r_port);
	  
	  byte[] r_buffer = new byte[1024];
      byte[] s_buffer = new byte[1024];
      
      while (true) {
    	// receive request 
		DatagramPacket receivePacket = new DatagramPacket(r_buffer, r_buffer.length);
		socket.receive(receivePacket);
		String request = new String(receivePacket.getData());
		System.out.println("RECEIVED: " + request);
		
		// handle request
		InetAddress adress = receivePacket.getAddress();
		int s_port = receivePacket.getPort();
		String capitalizedSentence = request.toUpperCase();
		
		// send response
		s_buffer = capitalizedSentence.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(s_buffer, s_buffer.length, adress, s_port);
		socket.send(sendPacket);
		}
      
      /*
      System.out.println("Ending Connection");
      socket.close();
      System.out.println("Server Terminated");
      */

  }


}
