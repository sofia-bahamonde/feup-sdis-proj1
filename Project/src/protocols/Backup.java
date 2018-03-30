package protocols;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import common.Utils;
import peer.Chunk;

public class Backup implements Runnable{
	
	private File file;
	private int rep_degree;
	
	public Backup(String file_path, int rep_degree) {
		this.file = new File(file_path);
		this.rep_degree = rep_degree;
	}

	@Override
	public void run() {

		try {
			byte[] file_data = Utils.loadFileBytes(file);
			String file_id = Utils.getFileID(file);
			
			// gets number of chunks
			int chunks_num= file_data.length / (Chunk.MAX_SIZE) +1;
								 
			for(int i =0; i < chunks_num; i++) {
				
				// gets chunk data
				byte[] data;
				
				if(i == chunks_num-1) {
					if(file_data.length % Chunk.MAX_SIZE ==0) {
						data= new byte[0];
					}else {
						data= Arrays.copyOfRange(file_data, i*Chunk.MAX_SIZE, file_data.length %Chunk.MAX_SIZE);
					}
				}else {
					data= Arrays.copyOfRange(file_data, i*Chunk.MAX_SIZE, (i+1)*Chunk.MAX_SIZE);
				}
				
				// creates chunk 
				Chunk chunk=new Chunk(i,file_id,data,rep_degree);
				
				// chunk backup
				chunk.backup();
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
	
	}
	
}