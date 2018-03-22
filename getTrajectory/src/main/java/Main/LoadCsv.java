package Main;

import cqupt.db.Query;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

public class LoadCsv {

    public static void main(String[] args) {
        int batchNum = 100000, num = 0;
        long startTime = System.currentTimeMillis();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ArrayList<Object[]> paramList = new ArrayList<Object[]>();
        try {
            FileReader fileReader = new FileReader(new File("J:\\2017-09-03.csv"));
            CSVFormat format = CSVFormat.DEFAULT.withSkipHeaderRecord();
            CSVParser records = format.parse(fileReader);
            Iterator<CSVRecord> results = records.iterator();
            // startIndex用于跳过第一条数据
            int startIndex = 0;
            CSVRecord tempCsvRecord = null;
            int size = 0;
            Object[] params = null;
            while (results.hasNext()) {
                System.out.println("startIndex:" + startIndex);
                // 跳过第一、二条
                if (startIndex == 0) {
                    startIndex++;
                    tempCsvRecord = results.next();
                    continue;
                }
                tempCsvRecord = results.next();
                size = tempCsvRecord.size();
                params = new Object[size];
                for (int j = 0; j < size - 1; j++) {
                    params[j] = tempCsvRecord.get(j).replaceAll("'", "");
                }
                paramList.add(params);
                startIndex++;
                // 十万条插入一次
                if (startIndex % batchNum == 0) {
                    executeQuery(paramList);
                    num++;
                }
            }
            executeQuery(paramList);
            System.out.println("共插入：" + num * batchNum + paramList.size() + "条数据！");
            System.out.println("计数器：" + startIndex);
            System.out.println("耗时： " + (System.currentTimeMillis() - startTime) / 1000 + " 秒！");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void executeQuery(ArrayList<Object[]> paramList) {
        String sql = "INSERT INTO table `903`(license_no,pass_time,pass_port,device,direction,license_type,license_color," +
                "vehicle,vehicle_len,vehicle_type,speed,feature_image,pass_port_name,direction_name,lane,lane_name," +
                "vehicle_body_color,vehicle_logo_type,data_source,lat,lng,etc1,etc2,etc3) VALUES (?,?,?,?,?) ";
        Query.executeBatchDML(sql, paramList);
        paramList.clear();
    }
}
