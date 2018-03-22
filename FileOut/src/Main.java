
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * 测试向文件中写文件
 *
 * @author rey
 */
public class Main {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        String sContent = "市场调研公司Forrester Research（以下简称“Forrester”）的分析师分析师莎拉·罗特曼-埃普斯（Sarah Rotman Epps）周四发布报告称，今年美国市场平板电脑销量将达到350万台。到2013年，美国平板电脑销售总量将超越台式机。";
        String sDestFile = "myWrite.txt";
        File destFile = new File(sDestFile);
        if (!destFile.exists()) {
            destFile.createNewFile();
        }

// 1.向文件写入内容
// writeByFileWrite(sDestFile, sContent);

// 2.FileOutputStream向文件写入内容
// writeByFileWrite(sDestFile, sContent);

// 2.OutputStreamWriter向文件写入内容
        writeByOutputStreamWrite(sDestFile, sContent);
    }

    /**
     * 用FileWrite向文件写入内容
     *
     * @throws IOException
     */
    public static void writeByFileWrite(String _sDestFile, String _sContent)
            throws IOException {
        FileWriter fw = null;
        try {
            fw = new FileWriter(_sDestFile);
            fw.write(_sContent);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (fw != null) {
                fw.close();
                fw = null;
            }
        }
    }

    /**
     * 用FileOutputStream向文件写入内容
     *
     * @throws IOException
     */
    public static void writeByFileOutputStream(String _sDestFile,
                                               String _sContent) throws IOException {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(_sDestFile);
            fos.write(_sContent.getBytes());
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (fos != null) {
                fos.close();
                fos = null;
            }
        }
    }

    /**
     * 用OutputStreamWrite向文件写入内容
     *
     * @throws IOException
     */
    public static void writeByOutputStreamWrite(String _sDestFile, String _sContent) throws IOException {
        OutputStreamWriter os = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(_sDestFile);
            os = new OutputStreamWriter(fos, "UTF-8");
            os.write(_sContent);
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
    }

}