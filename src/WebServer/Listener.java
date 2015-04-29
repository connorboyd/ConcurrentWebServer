package WebServer;

import java.io.*;
import java.net.Socket;


public class Listener implements Runnable {

    private Socket mySocket;
    private InputStream myIS;
    private OutputStream myOS;

    public Listener() {
    }

    public void setMySocket(Socket sock) {
        this.mySocket = sock;
        try {
            this.myIS = mySocket.getInputStream();
            this.myOS = mySocket.getOutputStream();
        } catch (IOException e) {
            cleanUp();
        }
    }

	@Override
	public void run() {
        try {
            String filePath = getFilePath();
            byte[] response = FileCache.getFile(filePath);
            String headers = "HTTP/1.1 ";
            String statusCode = (response != null) ? "200 OK" : "404 Not Found";

            myOS.write(headers.getBytes());
            myOS.write(statusCode.getBytes());
            myOS.write('\n');
            myOS.write('\n');
            if (response != null) {
                myOS.write(response);
            }
        } catch (IOException e) {
            //e.printStackTrace();
        } finally {
            cleanUp();
        }
    }

    private void cleanUp() {
        try {
            mySocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mySocket = null; // Cleanup variables to be garbage collected
        myIS = null;
        myOS = null;
        FrontEnd.threadEnd(this);
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
}
