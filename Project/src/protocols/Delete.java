package protocols;

import java.io.File;
import java.security.NoSuchAlgorithmException;

import common.Utils;
import peer.Peer;

public class Delete implements Runnable {
	private File file;

	public Delete(String file_path) {
		this.file = new File(file_path); 
	}

	@Override
	public void run() {
		String file_id="";
		
		try {
			file_id = Utils.getFileID(file);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Peer.getMsgForwarder().sendDELETE(file_id);
		
	}

}
