package WebServer;

import java.io.IOException;
import java.net.ServerSocket;

public class FrontEnd {
	private static final int NUM_THREADS = 4;
    private static Thread[] threads = new Thread[NUM_THREADS];
    private static ServerSocket myServerSocket;

	public static void main(String args[]) {
        init();
        runLoop();
	}

    private static void runLoop() {
        while(true) {
            for(int i = 0; i < NUM_THREADS; ++i) {
                if(!threads[i].isAlive()) {
                    threads[i].run();
                } else {
                    System.out.print(".");
                }
            }
        }
    }

    public static ServerSocket getMyServerSocket() {
        return myServerSocket;
    }

    private static void init() {
        initServerSocket();
        initThreadPool();
    }

    private static void initServerSocket() {
        try {
            myServerSocket = new ServerSocket(8080);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initThreadPool() {
        for(int i = 0; i < NUM_THREADS; ++i) {
            threads[i] = new Thread(new Listener());
        }
    }
}
