package protocols;

import java.io.File;
import java.io.IOException;

import peer.FileManager;
import peer.Peer;

public class Backup implements Runnable{
	
	private File file;
	private int rep_degree;
	
	public Backup(String file_path, int rep_degree) {
		this.file = new File(file_path);
		this.rep_degree = rep_degree;
	}

	@Override
	public void run() {

		try {
			byte[] data = FileManager.loadFile(file);
			System.out.println(data.length);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	
		
		Peer.getController().sendPUTCHUNK();
	}
	
}