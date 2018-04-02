package protocols;

import java.util.ArrayList;
import java.util.Arrays;

import peer.Chunk;
import peer.Peer;

public class Reclaim implements Runnable {
	
	private int reclaimed_space;

	public Reclaim(int space) {
		this.reclaimed_space =space;
	}

	@Override
	public void run() {
		long used_mem = Peer.getDisk().getUsedMem();
		
		if(used_mem <=reclaimed_space) {
			Peer.getDisk().reclaimSpace(reclaimed_space);
			return;
		}
		
		long reclaimed_mem = (used_mem - reclaimed_space)*1000;
		
		
		
		@SuppressWarnings("unchecked")
		ArrayList<Chunk> chunks = (ArrayList<Chunk>) Peer.getDisk().getStoredChunks().clone();
		
		
		int i =0;
		
		
		do {
			Chunk chunk = chunks.get(i);
			
			Peer.getMsgForwarder().sendDELETED(chunk.getChunkNo(), chunk.getFileId());
			
			Peer.getDisk().deleteChunk(chunk);
			
			
			reclaimed_mem -= chunk.getData().length;
			i++;
			
		}while(reclaimed_mem >0);
		
		
		
	}

}
