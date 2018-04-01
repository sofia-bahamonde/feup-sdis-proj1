package protocols;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

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
		try {
			String file_id = Utils.getFileID(file);
			
			// TODO: Store file info
			int chunks_num= (int) (file.length() / (Chunk.MAX_SIZE) +1);
			
			Peer.getMDR().startSave(file_id);
								 
			for(int i =0; i < chunks_num; i++) {
				
				Peer.getMsgForwarder().sendGETCHUNK(i,file_id);
				
				
				try {
					TimeUnit.MILLISECONDS.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				Chunk chunk = Peer.getMDR().getSave(file_id,i);
				
				chunk.restore();
			}
			
			Peer.getMDR().stopSave(file_id);
			
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
