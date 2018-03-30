package messages;

import java.net.DatagramPacket;

import common.Utils;
import peer.Chunk;
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
		
		// chunk info from header
		String file_id=header[3];
		int chunk_no = Integer.parseInt(header[4]);
		int rep_degree= Integer.parseInt(header[5]);
		
		// chunk data from body
		byte[] chunk_data =Utils.parseBody(packet);
		
		// save chunk
		Chunk chunk = new Chunk(chunk_no,file_id,chunk_data, rep_degree);
		chunk.save();
		
		System.out.println("PUTCHUNK RECEIVED");
		
	}

}
