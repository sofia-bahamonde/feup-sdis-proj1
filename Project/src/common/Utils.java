package common;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


import messages.MsgForwarder;
import peer.Peer;

public class Utils{
	
	public static byte[] loadFileBytes(File file) throws IOException  {
		FileInputStream file_is = new FileInputStream(file);

		byte[] data = new byte[(int) file.length()];

		file_is.read(data);
		file_is.close();

		return data;
	}
	

	public static String getFileID(File file) throws NoSuchAlgorithmException {
		String file_id = file.getName() + file.lastModified() + Peer.getServerID();
		
		// sha-256
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest(file_id.getBytes(StandardCharsets.UTF_8));
		
		// byte[] to hex string
		char[] hexArray = "0123456789ABCDEF".toCharArray();
		char[] hexChars = new char[hash.length * 2];
	    for ( int j = 0; j < hash.length; j++ ) {
	        int v = hash[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
		
		
	}
	
	public static String[] parseHeader(DatagramPacket packet) {
		 
	 	ByteArrayInputStream stream = new ByteArrayInputStream(packet.getData());
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

		String header = "";
		try {
			header = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return header.split(" ");

 }
	
	public static byte[] parseBody(DatagramPacket packet) {
		
		 
		ByteArrayInputStream stream = new ByteArrayInputStream(packet.getData());
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

		String header="";
		
		
		try {
			header += reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int body_idx = header.length()+2*MsgForwarder.CRLF.length();
		
		byte[] body = Arrays.copyOfRange(packet.getData(),body_idx ,
				packet.getLength());

		
		return body;
 }
	
	
	 
}