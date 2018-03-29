package peer;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

public class Database implements Serializable{

	private static final long serialVersionUID = 1L;
	HashMap<String,Chunk> database;
	
	public Database() {
		database= new HashMap<String,Chunk>();
	}
	

	public  void addChunk(Chunk chunk) {
	
		database.put(chunk.getFileId(), chunk);
		Peer.saveDatabase();
	}
	
}