package protocols;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import common.Utils;
import peer.Chunk;
import peer.Peer;

public class Restore implements Runnable {

	private File file;
	
	public Restore(String file_path) {
		file = new File(file_path);
	}

	@Override
	public void run() {
		byte[] file_data;
		try {
			file_data = Utils.loadFileBytes(file);
			String file_id = Utils.getFileID(file);
			
			// gets number of chunks
			int chunks_num= file_data.length / (Chunk.MAX_SIZE) +1;
								 
			for(int i =0; i < chunks_num; i++) {
				
				Peer.getMsgForwarder().sendGETCHUNK(i,file_id);

			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
