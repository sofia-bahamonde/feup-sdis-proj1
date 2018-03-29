package peer;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;

public class Messages{
	Double version;
	
    public static byte CR = 0xD;
    public static byte LF = 0xA;
    private static String CRLF = "" + (char) CR + (char) LF;
	
	public Messages(double version) {
		this.version = version;
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
	
	public byte[] createMessage(byte[] header, byte[]body) {
		
		byte[] msg = new byte[header.length + body.length];
		System.arraycopy(header, 0, msg, 0, header.length);
		System.arraycopy(body, 0, msg, header.length, body.length);
		
		return msg;
	}
	

	
	public static String[] parseHeader(DatagramPacket packet) {
		 
	 	ByteArrayInputStream stream = new ByteArrayInputStream(packet.getData());
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

		String header = "";
		try {
			header = reader.readLine();
			System.out.println(header);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return header.split("[ ]+");

 }
}