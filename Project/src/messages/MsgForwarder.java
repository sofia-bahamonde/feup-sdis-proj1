package messages;


import java.util.Arrays;

import peer.Chunk;
import peer.Peer;

public class MsgForwarder{
	Double version;
	
    public static byte CR = 0xD;
    public static byte LF = 0xA;
    public static String CRLF = "" + (char) CR + (char) LF;
	
	public MsgForwarder(double version) {
		this.version = version;
	}
	
	public byte[] createMessage(byte[] header, byte[]body) {
		
		byte[] msg = new byte[header.length + body.length];
		System.arraycopy(header, 0, msg, 0, header.length);
		System.arraycopy(body, 0, msg, header.length, body.length);
		
		return msg;
	}
	
	public void sendPUTCHUNK(Chunk chunk){
		String header = "PUTCHUNK"  
						+ " " + version 
						+ " " + Peer.getServerID()
						+ " " + chunk.getFileId()
						+ " " + chunk.getChunkNo()
						+ " " + chunk.getRepDegree()
						+ " " + CRLF + CRLF;

		
		byte[] msg = createMessage(header.getBytes(),chunk.getData());
		Peer.getMDB().sendMessage(msg);
						
	}
	
	public void sendCHUNK(Chunk chunk) {
		String header = "CHUNK"  
				+ " " + version 
				+ " " + Peer.getServerID()
				+ " " + chunk.getFileId()
				+ " " + chunk.getChunkNo()
				+ " " + chunk.getRepDegree()
				+ " " + CRLF + CRLF;


		byte[] msg = createMessage(header.getBytes(),chunk.getData());
		Peer.getMDR().sendMessage(msg);
				
	}
	
	public void sendSTORED(Chunk chunk) {
		String header = "STORED"
						+ " " + version 
						+ " " + Peer.getServerID()
						+ " " + chunk.getFileId()
						+ " " + chunk.getChunkNo()
						+ " " + CRLF + CRLF;
		
		Peer.getMC().sendMessage(header.getBytes());
	}
	
	public void sendDELETE(String file_id) {
		String header = "DELETE"
						+ " " + version
						+ " " + Peer.getServerID()
						+ " " + file_id
						+ " " + CRLF + CRLF;
		
		Peer.getMC().sendMessage(header.getBytes());
	}

	public void sendGETCHUNK(int chunk_no, String file_id) {
		String header = "GETCHUNK"
				+ " " + version 
				+ " " + Peer.getServerID()
				+ " " + file_id
				+ " " + chunk_no
				+ " " + CRLF + CRLF;
		

			Peer.getMC().sendMessage(header.getBytes());
		
	}



	

	

	
}