package go.pro.settings;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class Settings {
	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

	public static void main(String[] args) throws IOException {
		String commandURL = "http://10.5.5.9/camera/se?t=bmw760bmw760";
		 URL url = new URL(commandURL);
         URLConnection con = url.openConnection();
         System.out.println("Received a : " + con.getClass().getName());
         con.setDoInput(true);
         con.setDoOutput(true);
         con.setUseCaches(false);
         con.setRequestProperty("CONTENT_LENGTH", "5"); // Not checked
         con.setRequestProperty("Stupid", "Nonsense");
         System.out.println("Getting an input stream...");
         InputStream is = con.getInputStream();
         byte[] buff = new byte[31];
         int i = 0;
         while ((i = is.read(buff)) != -1) 
         {
             System.out.println("line: " + buff);
         }
         char[] hexChars = new char[buff.length * 2];
         for ( int j = 0; j < buff.length; j++ ) {
             int v = buff[j] & 0xFF;
             hexChars[j * 2] = hexArray[v >>> 4];
             hexChars[j * 2 + 1] = hexArray[v & 0x0F];
         }
         System.out.println(new String(hexChars));
	}

}
