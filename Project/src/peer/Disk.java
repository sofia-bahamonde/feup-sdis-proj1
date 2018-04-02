package peer;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class Disk implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private long CAPACITY = 8*1000000; // 8MBytes
	
	private long free_mem;
	private long used_mem;
	
	private ArrayList<Chunk> chunks_stored;
	
	public Disk() {
		free_mem = CAPACITY;
		used_mem =0;
		chunks_stored = new ArrayList<Chunk>();
	}
	
	
	public long getCapacity() {
		return CAPACITY/1000; // capacity in KBytes
	}

	public long getUsedMem() {
		return used_mem/1000; // used memory in KBytes
	}
	
	public boolean storeChunk(Chunk chunk) {
		byte [] chunk_data = chunk.getData();
		
		// check if there is enough memory
		long tmp = free_mem -chunk_data.length;
		
		if(tmp <0) {
			System.out.println("Chunk can not be stored.");
			System.out.println("Not enough free memory.");
			return false;
		}
		
		// update memory status
		free_mem -= chunk_data.length;
		used_mem += chunk_data.length;
		
		
		// store in folder
		File folder = new File(Peer.CHUNKS);

		if (!(folder.exists() && folder.isDirectory()))
			folder.mkdir();

		FileOutputStream out;
		try {
			out = new FileOutputStream(Peer.CHUNKS + chunk.getID());
			out.write(chunk.getData());
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		// store in database
		chunks_stored.add(chunk);
		
		return true;
		
		
	}
	
	public boolean isStored(Chunk chunk) {
		return chunks_stored.contains(chunk);
	}


	public ArrayList<Chunk> getStoredChunks() {
		return chunks_stored;
	}


	public void incRepDegree(String chunk_id, int saves) {
		
		
		for(int i=0; i< chunks_stored.size();i++) {
			if(chunks_stored.get(i).getID().equals(chunk_id)) {
				chunks_stored.get(i).incRepDegree(saves);
			}
		}
	
	}


	public long getFreeMem() {
		return free_mem/1000;
	}


	public boolean hasChunks() {
		return chunks_stored.size() >0;
	}


	
}