import java.io.File;
import java.util.*;

public class getMap {

    HashMap<String, String> loadmap = new HashMap<String, String>();
    String[] fileName;

    public static String[] getFileName(String path) {
        File file = new File(path);
        String[] fileName = file.list();
        return fileName;
    }

    public HashMap<String, String> getLoadmap() {
        fileName = getFileName("H:\\04");

        int flag = 401;

        for (String filename : fileName) {
            loadmap.put(String.valueOf(flag), filename);
            System.out.print(String.valueOf(flag) + ":" + filename + "\t");
            flag++;
        }
        return loadmap;
    }

    public static void main(String args[]) {
        getMap mp = new getMap();
        HashMap<String, String> map = mp.getLoadmap();
    }

}
