package go.pro.actions;

import go.pro.constants.CommandConstants;
import go.pro.constants.CommonConstants;
import go.pro.constants.ModeConstants;
import go.pro.constants.VideoResolutionConstants;
import go.pro.utils.CommandBuildingUtils;

import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RecordVideo {
	
	public static boolean changeToVideoMode(String password) {
		String status = RetrieveCameraInfo.RetrieveStatus(password);
		if(status == null) {
			//Camera is turned off/phone not connected to camer via Wi-Fi
			return false;
		}
		
		HttpURLConnection conn = null;
		try {
			//Check if camera is in video mode and send command to change to video mode if it is not
			if(!status.substring(2, 4).equals("00")) {
				conn = (HttpURLConnection) (new URL(CommandBuildingUtils.generateCommandURL(ModeConstants.MODE_COMMAND_CAMERA, ModeConstants.MODE_VIDEO, password, "camera"))).openConnection();
				if(conn.getResponseCode() == 200) {
					conn.disconnect();
					return true;
				} else {
					conn.disconnect();
					return false;
				}
			} else {
				return true;
			}
		} catch (MalformedURLException e) {
			if(conn != null) {
				conn.disconnect();
			}
			e.printStackTrace();
		} catch (ConnectException e) {
			System.out.println("Could not establish connection!");
		}  catch (IOException e) {
			if(conn != null) {
				conn.disconnect();
			}
			e.printStackTrace();
		} 
		return false;
	}
	
	public static boolean changeVideoQuality(String password, int quality, int cameraModel) {
		String status = RetrieveCameraInfo.RetrieveStatus(password);
		if(status == null) {
			//Camera is turned off/phone not connected to camer via Wi-Fi
			return false;
		}
		
		HttpURLConnection conn = null;
		try {
			//Check if camera is in video mode and send command to change to video mode if it is not
			if(cameraModel == CommonConstants.CAMERA_HERO_2_3_SILVER) {
				if(quality == VideoResolutionConstants.VIDEO_QUALITY_LOW) {
					conn = (HttpURLConnection) (new URL(CommandBuildingUtils.generateCommandURL(VideoResolutionConstants.HERO23_RESOLUTION_COMMAND_CAMERA, VideoResolutionConstants.HERO23_RESOLUTION_WVGA_60, password, "camera"))).openConnection();
				} else {
					conn = (HttpURLConnection) (new URL(CommandBuildingUtils.generateCommandURL(VideoResolutionConstants.HERO23_RESOLUTION_COMMAND_CAMERA, VideoResolutionConstants.HERO23_RESOLUTION_720p_30, password, "camera"))).openConnection();
				}
			} else if(cameraModel == CommonConstants.CAMERA_HERO_3_BLACK) {
				if(quality == VideoResolutionConstants.VIDEO_QUALITY_LOW) {
					conn = (HttpURLConnection) (new URL(CommandBuildingUtils.generateCommandURL(VideoResolutionConstants.HERO3_BLACK_RESOLUTION_COMMAND_NEW_CAMERA, VideoResolutionConstants.HERO3_BLACK_RESOLUTION_WVGA_240_NEW, password, "camera"))).openConnection();
				} else {
					conn = (HttpURLConnection) (new URL(CommandBuildingUtils.generateCommandURL(VideoResolutionConstants.HERO3_BLACK_RESOLUTION_COMMAND_NEW_CAMERA, VideoResolutionConstants.HERO3_BLACK_RESOLUTION_720p_120_NEW, password, "camera"))).openConnection();
				}
			} else {
				conn = (HttpURLConnection) (new URL(CommandBuildingUtils.generateCommandURL(VideoResolutionConstants.HERO3_PLUS_BLACK_RESOLUTION_COMMAND_WITHOUT_FPS_CAMERA, VideoResolutionConstants.HERO3_PLUS_BLACK_RESOLUTION_720p_WITHOUT_FPS, password, "camera"))).openConnection();
			}
			if(conn.getResponseCode() == 200) {
				conn.disconnect();
				return true;
			} else {
				conn.disconnect();
				return false;
			}
		} catch (MalformedURLException e) {
			if(conn != null) {
				conn.disconnect();
			}
			e.printStackTrace();
		} catch (ConnectException e) {
			System.out.println("Could not establish connection!");
		} catch (IOException e) {
			if(conn != null) {
				conn.disconnect();
			}
			e.printStackTrace();
		}
		return false;
	}

	public static boolean startRecordVideo(String password) {
		String status = RetrieveCameraInfo.RetrieveStatus(password);
		if(status == null) {
			//Camera is turned off/phone not connected to camer via Wi-Fi
			return false;
		} else if (status.charAt(59) == '1' && status.substring(2, 4).equals("00")) {
			//Camera is recording
			return true;
		}
		
		HttpURLConnection conn = null;
		try {				
			//Start recording
			conn = (HttpURLConnection) (new URL(CommandBuildingUtils.generateCommandURL(CommandConstants.SHUTTER_COMMAND_BACPAC, CommandConstants.SHUTTER_SHUTTER, password, "bacpac"))).openConnection();
			if(conn.getResponseCode() == 200) {
				conn.disconnect();
				return true;
			} else {
				conn.disconnect();
				return false;
			}
		} catch (MalformedURLException e) {
			if(conn != null) {
				conn.disconnect();
			}
			e.printStackTrace();
		} catch (ConnectException e) {
			System.out.println("Could not establish connection!");
		} catch (IOException e) {
			if(conn != null) {
				conn.disconnect();
			}
			e.printStackTrace();
		} finally {
			if(conn != null) {
				conn.disconnect();
			}
		}
		return false;
	}
	
	public static boolean stopRecordVideo(String password) {
		String status = RetrieveCameraInfo.RetrieveStatus(password);
		if(status == null) {
			//Camera is turned off/phone not connected to camer via Wi-Fi
			return false;
		} else if (status.charAt(59) == '0') {
			//Camera is stopped
			return true;
		}
		
		HttpURLConnection conn = null;
		try {		
			//Start recording
			conn = (HttpURLConnection) (new URL(CommandBuildingUtils.generateCommandURL(CommandConstants.SHUTTER_COMMAND_BACPAC, CommandConstants.SHUTTER_SHUTTER, password, "bacpac"))).openConnection();
			if(conn.getResponseCode() == 200) {
				conn.disconnect();
				return true;
			} else {
				conn.disconnect();
				return false;
			}
		} catch (MalformedURLException e) {
			if(conn != null) {
				conn.disconnect();
			}
			e.printStackTrace();
		} catch (ConnectException e) {
			System.out.println("Could not establish connection!");
		} catch (IOException e) {
			if(conn != null) {
				conn.disconnect();
			}
			e.printStackTrace();
		} finally {
			if(conn != null) {
				conn.disconnect();
			}
		}
		return false;
	}
}
