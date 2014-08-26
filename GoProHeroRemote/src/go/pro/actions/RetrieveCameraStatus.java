package go.pro.actions;

import go.pro.utils.CommandBuildingUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RetrieveCameraStatus {
	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	
	public static String RetrieveStatus(String password){
		HttpURLConnection con = null;
		try {
			URL url = new URL(CommandBuildingUtils.generateCommandURL("se", null, password, "camera"));
			con =(HttpURLConnection) url.openConnection();
	        //check if camera is powered on and if it's not - send command to power on
	        if(con.getResponseCode() != 200) {
	        	 con.disconnect();
	        	 return null;
	        } else {
	        	byte[] buff = new byte[31];
	        	InputStream is = con.getInputStream();
		        while (is.read(buff) != -1) {
		        	System.out.println("Retrieved status at" + System.currentTimeMillis());
			    }
		        con.disconnect();
		        char[] hexChars = new char[buff.length * 2];
		        for ( int j = 0; j < buff.length; j++ ) {
		            int v = buff[j] & 0xFF;
		            hexChars[j * 2] = hexArray[v >>> 4];
		            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		        }
		        return String.valueOf(hexChars);
	        }
		} catch (IOException e) {
			if(con != null) {
				con.disconnect();
			}
			return null;
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}
	}
}
