package protocols;

import java.io.File;

public class Backup implements Runnable{
	
	private File file;
	private int rep_degree;
	
	public Backup(String file_path, int rep_degree) {
		this.file = new File(file_path);
		this.rep_degree = rep_degree;
	}

	@Override
	public void run() {
		
		
	}
	
}