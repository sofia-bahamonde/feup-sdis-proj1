import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class Client {
	
  private static int port;

  public static void main(String[] args) throws IOException{

    valid_arguments(args);
    
    DatagramSocket socket = new DatagramSocket();
	byte[] buffer = new byte[1024];
	
	buffer = "yoyoyoyoo   yoy".getBytes();
	
	
	InetAddress address = InetAddress.getByName(args[0]);
	
	DatagramPacket packet = new DatagramPacket(buffer, buffer.length,address,port);
	socket.send(packet);
	
	// get response
	packet= new DatagramPacket(buffer,buffer.length);
	socket.receive(packet);
	
	// display response
	String received= new String(packet.getData());
	System.out.println("Echoed Message:" + received);
	socket.close();



  }

  private static boolean valid_arguments(String[] args){
	  
	  if(args.length != 4) {
		  System.out.println("ERROR - invalid number of arguments!");
		  return false;
	  }
	  
	  port = Integer.parseInt(args[1]);
	  
	  
	  if(!args[2].equals("register") && !args[2].equals("lookup")) {
		  System.out.println("ERROR - invalid operation!");
		  return false;
	  }
	  
	  /*
	  System.out.println(args[3]);
	  
	  String[] operands = args[3].split(",");
	  
	  System.out.println(operands);
	  
	
	  if(args[3][0].length != 8) {
		  System.out.println("ERROR - invalid operation!");
		  return false;
	  }
	  
	  if(args[2].equals("lookup")) {
		  
		  for(int i = 0; i <)
	  }*/
	  
	  return true;

  }


}
