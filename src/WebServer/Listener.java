package WebServer;

import java.io.*;
import java.net.Socket;


public class Listener implements Runnable {

    private Socket mySocket;
    private InputStream myIS;
    private OutputStream myOS;

	@Override
	public void run() {
        try {
            initSocket();   // TODO: Change name of method?
            String filePath = getFilePath();
            String response = FileCache.getFile(filePath);
            myOS.write(response.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                mySocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private String getFilePath() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(myIS));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                if(line.contains("GET")) {
                    String filePath = System.getProperty("user.dir") + line.split(" ")[1];
                    return filePath;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "blah.txt";
    }


    private void initSocket() {
        try {
            mySocket = FrontEnd.getMyServerSocket().accept();
            myIS = mySocket.getInputStream();
            myOS = mySocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
