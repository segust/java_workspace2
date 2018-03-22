package Main;

import db.Query;
import db.QueryCallBack;
import util.TimeCorrect;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;

public class createInfoCollection {


    public static void main(String args[]) {
        createInfoCollection create = new createInfoCollection();
        create.create();
    }


    //未做套牌车处理
    private int count = 0;

    private void create() {

        HashSet<String> tableSet = util.TableSet.getTableSet();
        for (final String tableName : tableSet) {
            if (!tableName.equals("info_collection")) {


                String SQL = "select * from `" + tableName + "` order by license_no,pass_time desc";
                System.out.println("receiving data from `" + tableName + "`\n");


                final ArrayList<Object[]> paramList = new ArrayList<Object[]>();
                Query.executeQueryTemplate(SQL, null, new QueryCallBack() {
                    public Object doExecute(ResultSet tabledetails) {
                        String lastLicense_no = "";
                        String lastPass_port = "";

                        try {
                            System.out.println("operating data from `" + tableName + "`\n");
                            while (tabledetails.next()) {
                                String license_no = tabledetails.getString("license_no");
                                Timestamp pass_time = tabledetails.getTimestamp("pass_time");
                                String pass_port = tabledetails.getString("pass_port");
                                String device = tabledetails.getString("device");
                                String direction = tabledetails.getString("direction");
                                String license_type = tabledetails.getString("license_type");
                                String license_color = tabledetails.getString("license_color");
                                String vehicle = tabledetails.getString("vehicle");
                                String vehicle_len = tabledetails.getString("vehicle_len");
                                String vehicle_type = tabledetails.getString("vehicle_type");
                                String speed = tabledetails.getString("speed");
                                String feature_image = tabledetails.getString("feature_image");
                                String panorama_image = tabledetails.getString("panorama_image");
                                String pass_port_name = tabledetails.getString("pass_port_name");
                                String direction_name = tabledetails.getString("direction_name");
                                String lane = tabledetails.getString("lane");
                                String lane_name = tabledetails.getString("lane_name");
                                String vehicle_body_color = tabledetails.getString("vehicle_body_color");
                                String vehicle_logo_type = tabledetails.getString("vehicle_logo_type");
                                String data_source = tabledetails.getString("data_source");
                                String lat = tabledetails.getString("lat");
                                String lng = tabledetails.getString("lng");
                                String etc1 = tabledetails.getString("etc1");
                                String etc2 = tabledetails.getString("etc2");
                                String etc3 = tabledetails.getString("etc3");

                                TimeCorrect timec = new TimeCorrect(pass_time);
                                Timestamp new_pass_time = timec.getRightTime();

                                //出去车牌号为“未知”的数据
                                if (!license_no.equals("未知")) {
                                    //若与上一次车牌号相同
                                    if (license_no.equals(lastLicense_no)) {
                                        //若与上一次的卡口名称不同
                                        if (!pass_port.equals(lastPass_port)) {
                                            Object[] params = new Object[25];
                                            params[0] = license_no;
                                            params[1] = new_pass_time;
                                            params[2] = pass_port;
                                            params[3] = device;
                                            params[4] = direction;
                                            params[5] = license_type;
                                            params[6] = license_color;
                                            params[7] = vehicle;
                                            params[8] = vehicle_len;
                                            params[9] = vehicle_type;
                                            params[10] = speed;
                                            params[11] = feature_image;
                                            params[12] = panorama_image;
                                            params[13] = pass_port_name;
                                            params[14] = direction_name;
                                            params[15] = lane;
                                            params[16] = lane_name;
                                            params[17] = vehicle_body_color;
                                            params[18] = vehicle_logo_type;
                                            params[19] = data_source;
                                            params[20] = lat;
                                            params[21] = lng;
                                            params[22] = etc1;
                                            params[23] = etc2;
                                            params[24] = etc3;
                                            paramList.add(params);
                                            lastLicense_no = license_no;
                                            lastPass_port = pass_port;
                                        }
                                        //若与上一次车牌号不同
                                    } else {
                                        Object[] params = new Object[25];
                                        params[0] = license_no;
                                        params[1] = new_pass_time;
                                        params[2] = pass_port;
                                        params[3] = device;
                                        params[4] = direction;
                                        params[5] = license_type;
                                        params[6] = license_color;
                                        params[7] = vehicle;
                                        params[8] = vehicle_len;
                                        params[9] = vehicle_type;
                                        params[10] = speed;
                                        params[11] = feature_image;
                                        params[12] = panorama_image;
                                        params[13] = pass_port_name;
                                        params[14] = direction_name;
                                        params[15] = lane;
                                        params[16] = lane_name;
                                        params[17] = vehicle_body_color;
                                        params[18] = vehicle_logo_type;
                                        params[19] = data_source;
                                        params[20] = lat;
                                        params[21] = lng;
                                        params[22] = etc1;
                                        params[23] = etc2;
                                        params[24] = etc3;
                                        paramList.add(params);
                                        lastLicense_no = license_no;
                                        lastPass_port = pass_port;
                                    }
                                    //批量导入
                                    if (paramList.size() % 10000 == 0) {
                                        count++;
                                        String sql = "insert into `132`(license_no,pass_time,pass_port,device,direction,license_type,license_color,vehicle,vehicle_len,vehicle_type,speed,feature_image,panorama_image,pass_port_name,direction_name,lane,lane_name,vehicle_body_color,vehicle_logo_type,data_source,lat,lng,etc1,etc2,etc3) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                                        Query.executeBatchDML(sql, paramList);
                                        System.out.println(String.valueOf(count) + "*" + String.valueOf(3000 * count) + "items has been loaded.");
                                        paramList.clear();
                                    }
                                }
                            }
                            //余量导入
                            String sql = "insert into `132`(license_no,pass_time,pass_port,device,direction,license_type,license_color,vehicle,vehicle_len,vehicle_type,speed,feature_image,panorama_image,pass_port_name,direction_name,lane,lane_name,vehicle_body_color,vehicle_logo_type,data_source,lat,lng,etc1,etc2,etc3) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                            Query.executeBatchDML(sql, paramList);
                            paramList.clear();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                });
            }
        }
    }
}

