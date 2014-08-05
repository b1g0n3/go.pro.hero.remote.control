package go.pro.utils;

public class CommandBuildingUtils {
	
	private static String cameraURL = "http://10.5.5.9/";
	
	public static String generateCommandURL(String commandName, String commandValue, String password, String cameraBacpac) {
		if(commandValue == null) {
			return cameraURL + cameraBacpac + "/" + commandName + "?t=" + password;
		} else  {
			return cameraURL + cameraBacpac + "/" + commandName + "?t=" + password + "&p=%" + commandValue;
		}
	}
}
