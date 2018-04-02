package protocols;

import java.util.ArrayList;


import peer.Chunk;
import peer.Peer;

public class Reclaim implements Runnable {
	
	private int reclaimed_space;

	public Reclaim(int space) {
		this.reclaimed_space =space;
	}

	@Override
	public void run() {
		int used_mem = (int) Peer.getDisk().getUsedMem();
		
		System.out.println(used_mem);
		
		if(used_mem <=reclaimed_space) {
			Peer.getDisk().reclaimSpace(reclaimed_space,used_mem);
			return;
		}
		
		long reclaimed_mem = (used_mem - reclaimed_space)*1000;
		
		
		
		ArrayList<Chunk> chunks =  Peer.getDisk().getStoredChunks();
		
		
		int i =0;
		
		
		do {
			Chunk chunk = chunks.get(i);
			
			Peer.getMsgForwarder().sendREMOVED(chunk.getChunkNo(), chunk.getFileId());
			
			Peer.getDisk().deleteChunk(chunk);
			
			
			reclaimed_mem -= chunk.getData().length;
			i++;
			
		}while(reclaimed_mem >0 && i<chunks.size());
		
		
		Peer.getDisk().reclaimSpace(reclaimed_space,1);
	}

}
