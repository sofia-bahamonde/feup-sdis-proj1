package peer;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public class Chunk implements Serializable{

	private static final long serialVersionUID = 1L;

	public static int MAX_SIZE = 64*1000;
	
	private int chunk_no;
	private String file_id;
	private byte[] data;
	private int rep_degree;
	private String id;
	private int actual_rep_degree;
	
	
	public Chunk(int chunk_no,String file_id, byte[] data, int rep_degree ) {
		this.chunk_no =chunk_no;
		this.file_id = file_id;
		this.data = data;
		this.rep_degree= rep_degree;
		this.id=chunk_no + "_" +file_id;
		this.actual_rep_degree=0;
	}
	

	public int getChunkNo() {
		return chunk_no;
	}
	
	public String getFileId() {
		return file_id;
	}
	public byte[] getData() {
		return data;
	}
	public int getRepDegree() {
		return rep_degree;
	}
	public String getID() {
		return id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;

		Chunk chunk = (Chunk) obj;
		return (chunk_no == chunk.getChunkNo() && file_id.equals(chunk.getFileId()));
		
	}
	
	public void backup() {
		long wait_time =1;
		int putchunk_sent=0;
		int stored =0;
		
		
		do {
			Peer.getMC().startSave(this.id);
			Peer.getMsgForwarder().sendPUTCHUNK(this);
			putchunk_sent++;
			
			try {
				TimeUnit.SECONDS.sleep(wait_time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			stored = Peer.getMC().getSaves(this.id);
			
			wait_time *=2;
			
		}while(stored<rep_degree && putchunk_sent !=5);
		
	
		Peer.getMC().stopSave(this.id);
		
	}


	public int getActualRepDegree() {
		return actual_rep_degree;
	}


	public void setActualRepDegree(int saves) {
		actual_rep_degree = 1+saves;
		
	}
	



}