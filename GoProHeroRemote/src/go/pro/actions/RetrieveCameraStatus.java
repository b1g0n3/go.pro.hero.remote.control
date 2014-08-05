package go.pro.actions;

import go.pro.constants.CommandConstants;
import go.pro.utils.CommandBuildingUtils;

import java.io.FileWriter;
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
	        con.setRequestMethod("GET");
	        //check if camera is powered on and if it's not - send command to power on
	        if(con.getResponseCode() != 200) {
	        	 con.disconnect();
	        	 int i = 0;
	        	 while(i < 6) {
	        		 con = (HttpURLConnection) (new URL(CommandBuildingUtils.generateCommandURL(CommandConstants.POWER_COMMAND_BACPAC, CommandConstants.POWER_ON, password, "bacpac"))).openConnection();
	        		 con.setRequestMethod("GET");
	        		 con.getResponseCode();
	        		 con.disconnect();
	        		 Thread.sleep(5000);
	        		 con =(HttpURLConnection) url.openConnection();
	        		 con.setRequestMethod("GET");
	        		 if(con.getResponseCode() == 200) {
	        			 break;
	        		 }
	        		 con.disconnect();
	        		 i++;
	        	 }
	        	 if(i >= 6){
	        		 System.out.println("Connection could not be established!");
	            	 return null;
	        	 }
	         }
	 		FileWriter fr = new FileWriter ("test.txt", true);
	 		FileWriter frStr = new FileWriter ("teststr.txt", true);
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
	        fr.write(hexChars);
	        fr.write("\n");
	        StringBuilder sb = new StringBuilder();
	        for ( int j = 0; j < hexChars.length-1; j+=2 ) {
	        	sb.append(hexChars[j]);
	        	sb.append(hexChars[j+1]);
	        	sb.append("|");
	        }
	        frStr.write(sb.toString());
	        frStr.write("\n");
	        fr.close();
	        frStr.close();
			return String.valueOf(hexChars);
		} catch (InterruptedException e) {
			if(con != null) {
				con.disconnect();
			}
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			System.out.println("Connection could not be established!");
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
