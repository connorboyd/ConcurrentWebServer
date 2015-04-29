package WebServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class FrontEnd {
	private static final int NUM_THREADS = 20;
    private static ServerSocket myServerSocket;
    private static BlockingQueue<Listener> runnableQueue = new LinkedBlockingQueue<Listener>();

	public static void main(String args[]) {
        init();
        runLoop();
	}

    public static void threadEnd(Listener listen) {
        try {
            runnableQueue.put(listen);
        } catch (InterruptedException e) {
            threadEnd(listen); // Keep trying to add Listener to queue
        }
    }

    private static void runLoop() {
        //ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        //ExecutorService executor = Executors.newSingleThreadExecutor();
        ExecutorService executor = Executors.newCachedThreadPool();
        //ExecutorService executor = Executors.newWorkStealingPool();
        while(true) {
            Socket newSocket;
            try {
                newSocket = myServerSocket.accept();
            } catch (IOException e) {
                System.err.println("Error accepting socket");
                continue;
            }
            Listener listen;
            try {
                listen = runnableQueue.take();
            } catch (InterruptedException e) {
                continue;
            }
            listen.setMySocket(newSocket);
            executor.execute(listen);
        }
    }

    private static void init() {
        initServerSocket();
        fillQueue();
    }

    private static void fillQueue() {
        for(int i = 0; i < NUM_THREADS; ++i) {
            try {
                runnableQueue.put(new Listener());
            } catch (InterruptedException e) {
                --i; // Thread put failed - try again
            }
        }

    }

    private static void initServerSocket() {
        try {
            myServerSocket = new ServerSocket(8080);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
