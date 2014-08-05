package go.pro.actions;

import go.pro.constants.CommandConstants;
import go.pro.constants.ModeConstants;
import go.pro.constants.VideoResolutionConstants;
import go.pro.utils.CommandBuildingUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class RecordVideo {

	public static void RecordVideoStream(String password, int quality, long maxDurationInMillis) {
		String status = RetrieveCameraStatus.RetrieveStatus(password);
		if(status == null) {
			return;
		}
		HttpURLConnection conn = null;
		FileWriter fr = null;
		try {
			if(!status.substring(2, 4).equals("00")) {
				conn = (HttpURLConnection) (new URL(CommandBuildingUtils.generateCommandURL(ModeConstants.MODE_COMMAND_CAMERA, ModeConstants.MODE_VIDEO, password, "camera"))).openConnection();
				conn.getResponseCode();
				conn.disconnect();
			}
			fr = new FileWriter ("result.txt", true);
			fr.write("-------------" + (new Date(System.currentTimeMillis())) + "\n");
			
			if(quality == VideoResolutionConstants.VIDEO_QUALITY_LOW) {
				conn = (HttpURLConnection) (new URL(CommandBuildingUtils.generateCommandURL(VideoResolutionConstants.HERO3_BLACK_RESOLUTION_COMMAND_NEW_CAMERA, VideoResolutionConstants.HERO3_BLACK_RESOLUTION_WVGA_240_NEW, password, "camera"))).openConnection();
			} else {
				conn = (HttpURLConnection) (new URL(CommandBuildingUtils.generateCommandURL(VideoResolutionConstants.HERO3_BLACK_RESOLUTION_COMMAND_NEW_CAMERA, VideoResolutionConstants.HERO3_BLACK_RESOLUTION_720p_120_NEW, password, "camera"))).openConnection();
			}
			conn.getResponseCode();
			conn.disconnect();			
			Thread.sleep(1000);
			
			conn = (HttpURLConnection) (new URL(CommandBuildingUtils.generateCommandURL(CommandConstants.SHUTTER_COMMAND_BACPAC, CommandConstants.SHUTTER_SHUTTER, password, "bacpac"))).openConnection();
			long start = 100000;
			if(conn.getResponseCode() == 200) {
				start = System.currentTimeMillis();
				conn.disconnect();
				fr.write(String.valueOf(start) + ": ");
				fr.write(RetrieveCameraStatus.RetrieveStatus(password) + "\n");	
				fr.flush();
			}
			Thread.sleep(5000);
			while((System.currentTimeMillis() - start) < maxDurationInMillis){
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
			conn = (HttpURLConnection) (new URL(CommandBuildingUtils.generateCommandURL(CommandConstants.SHUTTER_COMMAND_BACPAC, CommandConstants.SHUTTER_STOP, password, "bacpac"))).openConnection();
			conn.getResponseCode();
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
				if(conn != null) {
					conn.disconnect();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
