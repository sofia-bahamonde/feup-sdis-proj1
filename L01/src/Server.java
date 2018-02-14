import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;

public class Server {
	
  private static HashMap<String, String> data_base;

  public static void main(String[] args) throws IOException{
	  
	  if(args.length!=1) {
		  System.out.println("ERROR - Invalid arguments");
		  System.out.println("Usage - Server <port_number>");
		  return;
	  }
	  
	  data_base = new HashMap<String, String>();
	  
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
		String response = handleRequest(request);
		
		// send response
		s_buffer = response.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(s_buffer, s_buffer.length, adress, s_port);
		socket.send(sendPacket);
		}
      
      /*
      System.out.println("Ending Connection");
      socket.close();
      System.out.println("Server Terminated");
      */

  }

private static String handleRequest(String request) {
	String[] array = request.split(" ");
	
	String request_type = array[0];
	String plate = array[1];

	
	if(request_type.equals("REQUEST")) {
		if(!data_base.containsKey(plate)) {
			String name = "";
			
			for(int i =2; i < array.length; i++) 
				name += array[i] + " ";
			
			name = name.substring(0, name.length() - 1);
			
			data_base.put(plate, name);
			return Integer.toString(data_base.size());
			
		}else return "-1";
			
	}else if(request_type.equals("LOOKUP")) {
		if(data_base.containsKey(plate)) {
			return data_base.get()
		}else return "NOT_FOUND";
	}
	
	return null;
}


}
