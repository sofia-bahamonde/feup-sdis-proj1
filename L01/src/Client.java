import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class Client {
	
  public static void main(String[] args) throws IOException{
	  
    if(!valid_arguments(args)) return;
    
	int port = Integer.parseInt(args[1]);
	InetAddress address = InetAddress.getByName(args[0]);
	DatagramSocket socket = new DatagramSocket();

	byte[] s_buffer = new byte[1024];
	byte[] r_buffer = new byte[1024];
	
	// send request
	System.out.println("Sending Request");
	String request = create_request(args);
	s_buffer = request.getBytes();
	DatagramPacket sendPacket = new DatagramPacket(s_buffer, s_buffer.length, address, port);
	socket.send(sendPacket);
	
	// receive response
	DatagramPacket receivePacket = new DatagramPacket(r_buffer, r_buffer.length);
	socket.receive(receivePacket);
	String modifiedSentence = new String(receivePacket.getData());
	
	// print response
	System.out.println("RESPONSE:" + modifiedSentence);
	socket.close();
	
	System.out.println("Connection End");
	
	return;


  }
  
  private static String create_request(String[] args) {
	  String request= "";
	  
	  request += args[2].toUpperCase() + " ";
	  
	  for(int i =3; i <args.length; i++)
		  request += args[i] + " ";
		  
	  request = request.substring(0, request.length() - 1);
	  
	  return request;
  }

  private static boolean valid_arguments(String[] args){
	  
	  if(args[2].equals("register")){
		  if(args.length != 5) {
		  System.out.println("ERROR - Invalid number of operands");
		  return false;
		  }
	  }else if(args[2].equals("lookup")) {
		  if(args.length != 4) {
		  System.out.println("ERROR - Invalid number of operands");
		  return false;
	  	}
	  }else {
		  System.out.println("ERROR - Invalid operation");
		  return false;
	  }
	  
	  if(!valid_plate(args[3])) {
		  System.out.println("ERROR - Invalid plate");
		  return false;
	  }
	  
	
	  return true;

  }

private static boolean valid_plate(String plate) {
	if(plate.length() != 8) return false;
	
	if(plate.charAt(2) != '-' || plate.charAt(5) != '-' )return false;
	
	return true;
}


}
