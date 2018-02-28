import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;


public class Client {
	
	public static int mcast_port;
	public static int srvc_port;
	private static InetAddress mcast_addr;
	private static InetAddress srvc_addr;

	public static void main(String[] args) throws IOException {

		if (args.length > 5 || args.length < 4) {
			System.out.println("ERROR - Invalid arguments");
			System.out.println("Usage - Client <mcast_addr> <mcast_port> <oper> <opnd>*");
			return;
		}

		mcast_addr = InetAddress.getByName(args[0]);
		mcast_port = Integer.parseInt(args[1]);
		
		// join multicast group 
		MulticastSocket mcast_socket = new MulticastSocket(mcast_port);
		mcast_socket.joinGroup(mcast_addr);

		// learn address
		byte[] buf = new byte[1024];
		DatagramPacket mcast_packet = new DatagramPacket(buf, buf.length);
		mcast_socket.receive(mcast_packet);
		String msg = new String(buf, 0, buf.length).trim();
		
		
		// get address
		String[] array = msg.split("\\s");
		srvc_addr = InetAddress.getByName(array[0]);
		srvc_port = Integer.parseInt(array[1]);
		System.out.println("multicast: " + mcast_addr + " " + mcast_port + " : " +srvc_addr + " " + srvc_port);
		
		// initialize datagram socket
		DatagramSocket socket = new DatagramSocket();

		byte[] s_buffer = new byte[1024];
		byte[] r_buffer = new byte[1024];

		// send request
		System.out.println("Sending Request");
		String request = create_request(args);
		s_buffer = request.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(s_buffer, s_buffer.length, srvc_addr, srvc_port);
		socket.send(sendPacket);

		// receive response
		DatagramPacket receivePacket = new DatagramPacket(r_buffer, r_buffer.length);
		socket.receive(receivePacket);
		String response = new String(receivePacket.getData());

		// print response
		System.out.println(request + " :: " + response);

		// end connection
		socket.close();
		System.out.println("Connection End");
		return;

	}

	private static String create_request(String[] args) {
		String request = "";

		request += args[2].toUpperCase() + " ";

		for (int i = 3; i < args.length; i++)
			request += args[i] + " ";

		request = request.substring(0, request.length() - 1);

		return request;
	}

}
