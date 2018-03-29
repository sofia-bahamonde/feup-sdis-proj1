package messages;

import java.net.DatagramPacket;

import common.Utils;
import peer.Peer;

public class MsgHandler implements Runnable{

	DatagramPacket packet;
	
	public MsgHandler(DatagramPacket packet){
		this.packet = packet;
	}
	
	@Override
	public void run() {
		
		String[] header = Utils.parseHeader(packet);
		
		Integer server_id = Integer.parseInt(header[2]);
		
		// if message comes from self ignore it 
		if(server_id == Peer.getServerID()) {
			System.out.println("Packet Ignored");
			return;
		}
		
		String operation = header[0];
		
		switch(operation){
		case "PUTCHUNK":
			System.out.println("PUTCHUNK RECEIVED");
		break;
			
		}
		
	}

}
