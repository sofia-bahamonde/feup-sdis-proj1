package messages;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Random;

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
		
		int server_id = Integer.parseInt(header[2]);
		
		
		// if message comes from self ignore it 
		if(server_id == Peer.getServerID()) return;
		
		String operation = header[0];
		
		switch(operation){
		case "PUTCHUNK":
			handlePUTCHUNK();
			break;
		case "STORED":
			handleSTRORED();
			break;
		case "DELETE":
			handleDELETE();
			break;
		case "GETCHUNK":
			handleGETCHUNK();
			break;
		case "CHUNK":
			handleCHUNK();
			break;
			
		}
		
	}

	private void handleCHUNK() {
		System.out.println("CHUNK RECEIVED");
		
		// parsing message
		String file_id = header[3];
		int chunk_no = Integer.parseInt(header[4]);
		int rep_degree= Integer.parseInt(header[5]);
		byte[] chunk_data =Utils.parseBody(packet);
		
		if(Peer.getMDR().isSaving(file_id)){
			Chunk chunk = new Chunk(chunk_no,file_id,chunk_data, rep_degree);
			Peer.getMDR().save(file_id, chunk);
		}
			
	}

	private void handleGETCHUNK() {
		System.out.println("GETCHUNK RECEIVED");
		
		String file_id = header[3];
		int chunk_no = Integer.parseInt(header[4]);
	
		
		File file = new File(Peer.CHUNKS +  chunk_no + "_"+ file_id);
		
		Peer.getMDR().startSave(file_id);
		
		if(file.exists() && file.isFile()) {
			try {
				byte[] chunk_data = Utils.loadFileBytes(file);
				
				Chunk chunk =new Chunk(chunk_no, file_id,chunk_data,0);
				
				// wait a random delay
				Random rand = new Random();
				int  n = rand.nextInt(400) + 1;
				
				
				try {
					Thread.sleep(n);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} 
				
				ArrayList<Chunk> chunks =Peer.getMDR().getSave(file_id);
				
				
				if(chunks != null)
				if(!chunks.contains(new Chunk(chunk_no, file_id, new byte[0], 0)))
						Peer.getMsgForwarder().sendCHUNK(chunk);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		Peer.getMDR().stopSave(file_id);
		
		
	}

	private void handleDELETE() {
		System.out.println("DELETE RECEIVED");
		
		String file_id = header[3];

		chunkFilter filter = new chunkFilter(file_id);
	    File dir = new File(Peer.CHUNKS);
	    
	    String[] list = dir.list(filter);
	     
	     if (list.length == 0) return;

		    
	     for (String file_name : list){
	    	 File file = new File(Peer.CHUNKS + file_name);
	    	 file.delete();
	     }
		
	}

	private void handleSTRORED() {
		System.out.println("STORED RECEIVED");
		
		int peer_id = Integer.parseInt(header[2]);
		String file_id=header[3];
		int chunk_no = Integer.parseInt(header[4]);
		
		String chunk_id = chunk_no + "_" +file_id; 
		
		Peer.getMC().save(chunk_id,peer_id );
		
		if(Peer.getDisk().isStored(new Chunk(chunk_no,file_id, new byte[0],0))) 
			Peer.getDisk().incRepDegree(chunk_id,Peer.getMC().getSaves(chunk_id));
		
		
	}

	private void handlePUTCHUNK() {
		System.out.println("PUTCHUNK RECEIVED");
		
		// chunk info from header
		String file_id=header[3];
		int chunk_no = Integer.parseInt(header[4]);
		int rep_degree = Integer.parseInt(header[5]);
		
		// chunk data from body
		byte[] chunk_data =Utils.parseBody(packet);
		
		
		// create chunk 
		Chunk chunk = new Chunk(chunk_no,file_id,chunk_data,rep_degree );
		
		// stored chunk if not stored already
		if(!Peer.getDisk().isStored(chunk)) {
			Peer.getDisk().storeChunk(chunk);
		}
		
		// start saving STORED messages
		Peer.getMC().startSave(chunk.getID());
		
		// wait a random delay
		Random rand = new Random();
		int  n = rand.nextInt(400) + 1;
		
		try {
			Thread.sleep(n);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
		
		// send STORED message
		Peer.getMsgForwarder().sendSTORED(chunk);
		
		
		
	}
	
	public class chunkFilter implements FilenameFilter {
		
	       private String file_id;
		
	       public chunkFilter(String file_id) {
	         this.file_id = file_id;             
	       }
		       
	       public boolean accept(File dir, String name) {
	         return (name.endsWith(file_id));
	       }
	    }
	

}
