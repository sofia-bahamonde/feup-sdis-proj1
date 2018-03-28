package peer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class FileManager {
	
	public static byte[] loadFileBytes(File file) throws IOException  {
		FileInputStream file_is = new FileInputStream(file);

		byte[] data = new byte[(int) file.length()];

		file_is.read(data);
		file_is.close();

		return data;
	}
	
}