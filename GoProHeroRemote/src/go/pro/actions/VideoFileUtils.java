package go.pro.actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class VideoFileUtils {
	public static List<String> retrieveMP4FileNames(String urlStr) {
		String line;
		String fileName;
		List<String> mp4Names = new ArrayList<String>();
		HttpURLConnection conn = null;
		InputStreamReader rdr = null;
		BufferedReader in = null;
		URL url;
		try {
			url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			if(conn.getResponseCode() == 200) {
				rdr = new InputStreamReader(conn.getInputStream());
				in = new BufferedReader(rdr);
				while((line = in.readLine())!=null){
					if(line.startsWith("    <a class=\"link\"")) {
						fileName = line.substring(26, 38);
						if(fileName.endsWith(".MP4")){
							mp4Names.add(fileName);
						}
					}
				}
				in.close();
				rdr.close();
				conn.disconnect();
			}
			return mp4Names;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ConnectException e) {
			System.out.println("Could not establish connection!");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(in != null) {
					in.close();
				}
				if(rdr != null) {
					rdr.close();
				} 
				if(conn != null) {
					conn.disconnect();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static String retrieveMP4FilePath(String fileName, String fileDir) {
		return (fileDir + fileName);
	}
}
