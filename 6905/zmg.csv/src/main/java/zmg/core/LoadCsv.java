package zmg.core;

import zmg.db.Query;
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
        int batchNum = 10000, num = 0;
        long startTime = System.currentTimeMillis();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ArrayList<Object[]> paramList = new ArrayList<Object[]>();
        try {
            FileReader fileReader = new FileReader(new File("J:\\behavior_log.csv.tar\\behavior_log.csv"));
            CSVFormat format = CSVFormat.DEFAULT.withSkipHeaderRecord();
            CSVParser records = format.parse(fileReader);
            Iterator<CSVRecord> results = records.iterator();
            int startIndex = 0;
            CSVRecord tempCsvRecord = null;
            int size = 0;
            Object[] params = null;
            while (results.hasNext()) {
                if (startIndex <= 53410000) {
                    startIndex++;
                    System.out.println("忽略：" + startIndex);
                    continue;
                }
                tempCsvRecord = results.next();
                size = tempCsvRecord.size();
                params = new Object[size];
                for (int j = 0; j < size - 1; j++) {
                    params[j] = tempCsvRecord.get(j);
                }
                paramList.add(params);
                params = null;
                // 一万条插入一次
                if (startIndex % batchNum == 0) {
                    System.out.println(startIndex);
                    executeQuery(paramList);
                    System.out.println(" 第" + startIndex + "条！");
                    paramList.clear();
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
        String sql = "INSERT INTO behavior_log(user,time_stamp,btag,cate,brand) VALUES "
                + "(?,?,?,?,?) ";
        Query.executeBatchDML(sql, paramList);
        paramList.clear();
    }
}
