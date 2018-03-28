package peer;


public class MsgManager{
	Double version;
	
    public static byte CR = 0xD;
    public static byte LF = 0xA;
    private static String CRLF = "" + (char) CR + (char) LF;
	
	public MsgManager(double version) {
		this.version = version;
	}
	
	public void sendPUTCHUNK(){
		String header = "PUTCHUNK"  
						+ " " + version 
						+ " " + CRLF;
		
		Peer.getMDB().sendMessage(header);
						
		
	}
}