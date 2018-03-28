package peer;

public class Chunk{
	
	public static int MAX_SIZE = 64*1000;
	
	private int chunk_no;
	private String file_id;
	private byte[] data;
	private int rep_degree;
	
	public Chunk(int chunk_no,String file_id, byte[] data, int rep_degree ) {
		this.chunk_no =chunk_no;
		this.file_id = file_id;
		this.data = data;
		this.rep_degree= rep_degree;
	}
	
	public void backup() {
		Peer.getMsgForwarder().sendPUTCHUNK(this);
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




}