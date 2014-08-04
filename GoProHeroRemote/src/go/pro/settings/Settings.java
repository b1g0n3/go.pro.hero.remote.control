package go.pro.settings;

import go.pro.actions.RetrieveCameraStatus;
import go.pro.constants.CommandConstants;
import go.pro.constants.ModeConstants;

import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class Settings {
	

	public static void main(String[] args){
		String password = "bmw760bmw760";
		String status = RetrieveCameraStatus.RetrieveStatus(password);
		if(status == null) {
			return;
		}
		String ip = "http://10.5.5.9/";
		HttpURLConnection conn = null;
		FileWriter fr = null;
		try {
			if(status.substring(2, 4) != "00") {
				conn = (HttpURLConnection) (new URL(ip + "camera/" + ModeConstants.MODE_COMMAND_CAMERA 
						+ "?t=" + password + "&p=%" + ModeConstants.MODE_VIDEO)).openConnection();
				System.out.println(conn.getResponseCode());
				conn.disconnect();
			}
			fr = new FileWriter ("result.txt", true);
			fr.write("-------------" + (new Date(System.currentTimeMillis())) + "\n");
			conn = (HttpURLConnection) (new URL(ip + "bacpac/" + CommandConstants.SHUTTER_COMMAND_BACPAC 
					+ "?t=" + password + "&p=%" + CommandConstants.SHUTTER_SHUTTER)).openConnection();
			long start = System.currentTimeMillis();
			if(conn.getResponseCode() == 200) {
				conn.disconnect();
				fr.write(String.valueOf(start) + ": ");
				fr.write(RetrieveCameraStatus.RetrieveStatus(password) + "\n");	
				fr.flush();
			}	
			Thread.sleep(2000);
			while((System.currentTimeMillis() - start) < 60000){
				if(RetrieveCameraStatus.RetrieveStatus(password).charAt(59) == '1'){
					System.out.println((System.currentTimeMillis() - start)/1000);
					fr.write(String.valueOf(System.currentTimeMillis()) + ": ");
					fr.write(RetrieveCameraStatus.RetrieveStatus(password) + "\n");	
					fr.flush();
					Thread.sleep(2000);
				} else {
					break;
				}
			}
			conn = (HttpURLConnection) (new URL(ip + "bacpac/" + CommandConstants.SHUTTER_COMMAND_BACPAC 
					+ "?t=" + password + "&p=%" + CommandConstants.SHUTTER_STOP)).openConnection();
			conn.disconnect();
			Thread.sleep(1000);
			fr.write("End ");
			fr.write(RetrieveCameraStatus.RetrieveStatus(password) + "\n");
			fr.flush();
		} catch (MalformedURLException e) {
			if(conn != null) {
				conn.disconnect();
			}
			e.printStackTrace();
		} catch (IOException e) {
			if(conn != null) {
				conn.disconnect();
			}
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			try {
				if(fr != null) {
					fr.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
