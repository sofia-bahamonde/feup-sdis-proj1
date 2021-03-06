package protocols;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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
			String file_id="";
			
			try {
				file_id = Utils.getFileID(file);
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	
			ArrayList<Chunk> chunks = new ArrayList<Chunk>();
			Peer.getMDR().startSave(file_id);
			
			int i=0;
			boolean stop = false;
								 
			do {
				
				Peer.getMsgForwarder().sendGETCHUNK(i,file_id);
				
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();

					
				}
				

				Chunk chunk = Peer.getMDR().getSave(file_id).get(i);
				
				if(chunk == null) {
					System.out.println("RESTORE:: fail");
					System.out.println("Chunks were lost");
					return;
				}
				
				if(chunk.getData().length != Chunk.MAX_SIZE)
					stop= true;
				
				chunks.add(chunk);
				i++;
				
			}while(!stop);
			
			Peer.getMDR().stopSave(file_id);
			
			
		
			// concatenate chunks into file data
			byte [] file_data = new byte[0];
			byte[] tmp = new byte[0];
			
			for(int j =0; j <chunks.size(); j++) {
				
				byte [] chunk_data = chunks.get(j).getData();
				
				
				tmp = new byte[file_data.length + chunk_data.length];
				System.arraycopy(file_data, 0, tmp, 0, file_data.length);
				System.arraycopy(chunk_data, 0, tmp, file_data.length, chunk_data.length);
				
				file_data = tmp;
			}
			
		
			// save into restores folder
			File folder = new File(Peer.RESTORES);

			 
			if (!(folder.exists() && folder.isDirectory()))
				folder.mkdir();

			FileOutputStream out;
			try {
				out = new FileOutputStream(Peer.RESTORES +file.getName());
				out.write(file_data);
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		
		
	}
	}


