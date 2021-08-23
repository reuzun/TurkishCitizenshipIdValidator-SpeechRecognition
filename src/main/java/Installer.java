import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author reuzun
 */
public class Installer {

    final private String baseLocation = System.getProperty("user.home") + File.separator + ".SpeechIdValidator";
    private Map<String, String> map = new HashMap<>();

    public void install(String name) throws IOException {

        new File(baseLocation).mkdirs();

        String content;
        BufferedInputStream stream = new BufferedInputStream(Main.class.getResourceAsStream(name));
        byte[] data = stream.readAllBytes();

        Path location = Paths.get(baseLocation + File.separator + name);

        map.put(name, location.toString());
        try {
            Files.write(location, data, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
        }catch (FileAlreadyExistsException e){}
    }

    public String getLocation(String name){
        return map.get(name);
    }

}
