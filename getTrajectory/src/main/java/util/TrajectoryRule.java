package util;

import java.io.*;
import java.util.ArrayList;

public class TrajectoryRule {
    public void TrajectoryWriter(ArrayList<String> trajectory) {
        String fileName = "result.txt";
        File result = new File(fileName);
        try {
            if (!result.exists()) {
                result.createNewFile();
            }
            writeByOutputStreamWrite(fileName, trajectory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用OutputStreamWrite向文件写入内容
     *
     * @throws IOException
     */
    public void writeByOutputStreamWrite(String fileName, ArrayList<String> trajectory) {
        OutputStreamWriter os = null;
        FileOutputStream fos = null;
        try {
            try {
                fos = new FileOutputStream(fileName);
                os = new OutputStreamWriter(fos, "UTF-8");
                for (int i = 0; i < trajectory.size(); i++)
                    os.write(trajectory.get(i) + "\t");
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (os != null) {
                    os.close();
                    os = null;
                }
                if (fos != null) {
                    fos.close();
                    fos = null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
