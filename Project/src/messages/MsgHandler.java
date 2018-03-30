package messages;

import java.net.DatagramPacket;

import common.Utils;
import peer.Peer;

public class MsgHandler implements Runnable{

	DatagramPacket packet;
	
	String[] header;
	
	public MsgHandler(DatagramPacket packet){
		this.packet = packet;
	}
	
	@Override
	public void run() {
		
		header = Utils.parseHeader(packet);
		
		Integer server_id = Integer.parseInt(header[2]);
		
		// if message comes from self ignore it 
		if(server_id == Peer.getServerID()) {
			System.out.println("Packet Ignored");
			return;
		}
		
		String operation = header[0];
		
		switch(operation){
		case "PUTCHUNK":
			handlePUCHUNK();
		break;
			
		}
		
	}

	private void handlePUCHUNK() {
		Utils.parseBody(packet);
		System.out.println("PUTCHUNK RECEIVED");
		
	}

}
