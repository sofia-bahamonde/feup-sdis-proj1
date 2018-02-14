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
	  
	  int port = Integer.parseInt(args[0]);
	  
	  DatagramSocket socket = new DatagramSocket(port);
	  
      byte[] buffer = new byte[1024];
    
  
      while(true){
    	  	System.out.println("Receiving");
    	  	
            DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
            
            socket.receive(receivePacket);
            String request = new String(receivePacket.getData());
            System.out.println("RECEIVED: " + request);
            
            InetAddress IPAddress = receivePacket.getAddress();
            
            String response = request.toUpperCase();
            buffer = response.getBytes();
            
            DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, IPAddress, port);
            socket.send(sendPacket);
         }
      
      /*
      System.out.println("Ending Connection");
      socket.close();
      System.out.println("Server Terminated");
      */

  }


}
