package WebServer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;

public class FileCache {

	private static ConcurrentHashMap<String, String> fileHashMap = new ConcurrentHashMap<String, String>();

	public static String getFile(String filePath) { // TODO LRU? Expiration?
		return fileHashMap.computeIfAbsent(filePath, path -> readFileToString(path) );
	}

	private static String readFileToString(String filePath) {
		try {
			return new String(Files.readAllBytes(Paths.get(filePath)));
		} catch (IOException e) {
			return "Error opening file";
		}
	}
}
