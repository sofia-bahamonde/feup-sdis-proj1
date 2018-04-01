package channel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import peer.Chunk;

public class MDRChannel extends Channel{
	private Hashtable<String,ArrayList<Chunk>> restoring;

	public MDRChannel(String address, String port) throws IOException {
		super(address, port);
		restoring=new Hashtable<String,ArrayList<Chunk>>();
	}
	
	public void startSave(String file_id) {
		restoring.put(file_id, new ArrayList<Chunk>());
	}

	public void stopSave(String file_id) {
		restoring.remove(file_id);
	}
	
	
	public Chunk getSave(String file_id, int i) {
		if (restoring.containsKey(file_id))
			return restoring.get(file_id).get(i);
		return null;
	}
	
	public void save(String file_id, Chunk chunk) {
		if (restoring.containsKey(file_id))
				restoring.get(file_id).add(chunk);
	}
	
	public boolean isSaving(String chunk_id) {
		return restoring.containsKey(chunk_id);
	}
	

}
