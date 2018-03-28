package common;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import peer.Peer;

public class Utils{
	
	public static String getFileID(File file) throws NoSuchAlgorithmException {
		String file_id = file.getName() + file.lastModified() + Peer.getServerID();
		
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest(file_id.getBytes(StandardCharsets.UTF_8));
		
		String hash_str =  Base64.getEncoder().encodeToString(hash);
		
		return hash_str;
	}
}