package model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cqupt.util.getHoliday;

import cqupt.util.Rate;

public class RegularPoints {

    ResultSet rs = null;
    List<String> timeList = new ArrayList<String>();
    Map<String, Integer> holidayMap = new HashMap<String, Integer>();


    public RegularPoints(ResultSet rs) {
        this.rs = rs;
        this.holidayMap = getHoliday.getMap();
    }

    @SuppressWarnings("deprecation")
    public void getRegularPoints() {
        try {

            String currentlocation;
            Timestamp currenttime = null;
            BigDecimal currentlat;
            BigDecimal currentlng;
            BigDecimal currentrate = null;

            String lastlocation;
            Timestamp lasttime = null;
            BigDecimal lastlat;
            BigDecimal lastlng;

            Map<String, Integer> workdaystayMap = new HashMap<String, Integer>();
            Map<String, Integer> weekendstayMap = new HashMap<String, Integer>();
            Map<String, Integer> holidaystayMap = new HashMap<String, Integer>();

            Map<String, Integer> workdaypassMap = new HashMap<String, Integer>();
            Map<String, Integer> weekendpassMap = new HashMap<String, Integer>();
            Map<String, Integer> holidaypassMap = new HashMap<String, Integer>();


            if (rs.next()) {

                String templetregion = "?" + " 至 " + rs.getString("pass_port_name");
                BigDecimal templetrate = new BigDecimal("999999999999");

                lastlocation = rs.getString("pass_port_name");
                lasttime = rs.getTimestamp("pass_time");
                lastlat = new BigDecimal(rs.getString("lat"));
                lastlng = new BigDecimal(rs.getString("lng"));
                Timestamp starttime = lasttime;

                while (rs.next()) {
                    currentlocation = rs.getString("pass_port_name");
                    currenttime = rs.getTimestamp("pass_time");
                    if (!rs.getString("lat").equals("0") && !rs.getString("lat").equals("")) {
                        currentlat = new BigDecimal(rs.getString("lat"));
                        currentlng = new BigDecimal(rs.getString("lng"));
                    } else {
                        continue;
                    }
                    if (currenttime.getMonth() == lasttime.getMonth()) {


                        if (currenttime.getTime() != lasttime.getTime())
                            currentrate = Rate.getRate(currentlat, currentlng, lastlat, lastlng, currenttime, lasttime);

                        if (currenttime.getDate() != lasttime.getDate()) {

                            Timestamp endtime = lasttime;
                            String time = starttime.toString() + " - " + endtime.toString();
                            timeList.add(time);
                            starttime = currenttime;

                            if (holidayMap.get(String.valueOf((lasttime.getMonth() + 1) * 100 + lasttime.getDate())).intValue() == 2) {
                                if (!holidaypassMap.containsKey(lastlocation)) {
                                    holidaypassMap.put(lastlocation, 1);
                                } else {
                                    int n = holidaypassMap.get(lastlocation);
                                    holidaypassMap.put(lastlocation, ++n);
                                }
                                if (!holidaystayMap.containsKey(templetregion)) {
                                    holidaystayMap.put(templetregion, 1);
                                } else {
                                    int n = holidaystayMap.get(templetregion);
                                    holidaystayMap.put(templetregion, ++n);
                                }
                                templetrate = new BigDecimal("99999999999");

                            } else if (holidayMap.get(String.valueOf((currenttime.getMonth() + 1) * 100 + currenttime.getDate())).intValue() == 1) {

                                if (!weekendpassMap.containsKey(lastlocation)) {
                                    weekendpassMap.put(lastlocation, 1);
                                } else {
                                    int n = weekendpassMap.get(lastlocation);
                                    weekendpassMap.put(lastlocation, ++n);
                                }
                                if (!weekendstayMap.containsKey(templetregion)) {
                                    weekendstayMap.put(templetregion, 1);
                                } else {
                                    int n = weekendstayMap.get(templetregion);
                                    weekendstayMap.put(templetregion, ++n);
                                }
                                templetrate = new BigDecimal("9999999999");

                            } else {

                                if (!workdaypassMap.containsKey(lastlocation)) {
                                    workdaypassMap.put(lastlocation, 1);
                                } else {
                                    int n = workdaypassMap.get(lastlocation);
                                    workdaypassMap.put(lastlocation, ++n);
                                }

                                if (!workdaystayMap.containsKey(templetregion)) {
                                    workdaystayMap.put(templetregion, 1);
                                } else {
                                    int n = workdaystayMap.get(templetregion);
                                    workdaystayMap.put(templetregion, ++n);
                                }
                                templetrate = new BigDecimal("99999999999");

                            }
                        }


                        if (holidayMap.get(String.valueOf((lasttime.getMonth() + 1) * 100 + lasttime.getDate())).intValue() == 2) {
                            if (!holidaypassMap.containsKey(lastlocation)) {
                                holidaypassMap.put(lastlocation, 1);
                            } else {
                                int n = holidaypassMap.get(lastlocation);
                                holidaypassMap.put(lastlocation, ++n);
                            }
                        } else if (holidayMap.get(String.valueOf((lasttime.getMonth() + 1) * 100 + lasttime.getDate())).intValue() == 1) {
                            if (!weekendpassMap.containsKey(lastlocation)) {
                                weekendpassMap.put(lastlocation, 1);
                            } else {
                                int n = weekendpassMap.get(lastlocation);
                                weekendpassMap.put(lastlocation, ++n);
                            }
                        } else {

                            if (!workdaypassMap.containsKey(lastlocation)) {
                                workdaypassMap.put(lastlocation, 1);
                            } else {
                                int n = workdaypassMap.get(lastlocation);
                                workdaypassMap.put(lastlocation, ++n);
                            }
                        }
                        if (currentrate.compareTo(templetrate) == -1) {
                            templetregion = lastlocation + " 至 " + currentlocation;
                            templetrate = currentrate;
                        }
                        lastlocation = currentlocation;
                        lasttime = currenttime;
                        lastlat = currentlat;
                        lastlng = currentlng;

                        if (rs.isLast()) {
                            System.out.println(currenttime.getMonth() + 1 + "月数据：");

                            templetregion = "?" + " 至 " + rs.getString("pass_port_name");
                            templetrate = new BigDecimal("999999999999");
                            starttime = currenttime;
                            System.out.println("工作日常留地点：\n");
                            List<Entry<String, Integer>> list1 = new ArrayList<Entry<String, Integer>>(workdaystayMap.entrySet());
                            Collections.sort(list1, new Comparator<Map.Entry<String, Integer>>() {
                                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                                    return (o2.getValue() - o1.getValue());
                                }
                            });
                            for (int i = 0; i < list1.size() && i < 5; i++)
                                System.out.print(list1.get(i).getKey() + ":" + list1.get(i).getValue() + "\t");
                            System.out.println();
                            System.out.println();
                            System.out.println("周末常留地点：\n");
                            List<Entry<String, Integer>> list2 = new ArrayList<Entry<String, Integer>>(weekendstayMap.entrySet());

                            Collections.sort(list2, new Comparator<Map.Entry<String, Integer>>() {
                                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                                    return (o2.getValue() - o1.getValue());
                                }
                            });
                            for (int i = 0; i < list2.size() && i < 5; i++)
                                System.out.print(list2.get(i).getKey() + ":" + list2.get(i).getValue() + "\t");
                            System.out.println();
                            System.out.println();
                            System.out.println("节假日常留地点：\n");
                            List<Entry<String, Integer>> list3 = new ArrayList<Entry<String, Integer>>(holidaystayMap.entrySet());

                            Collections.sort(list3, new Comparator<Map.Entry<String, Integer>>() {
                                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                                    return (o2.getValue() - o1.getValue());
                                }
                            });
                            for (int i = 0; i < list3.size() && i < 5; i++)
                                System.out.print(list3.get(i).getKey() + ":" + list3.get(i).getValue() + "\t");
                            System.out.println();
                            System.out.println();
                            System.out.println("工作日常经过地点：\n");
                            List<Entry<String, Integer>> list4 = new ArrayList<Entry<String, Integer>>(workdaypassMap.entrySet());

                            Collections.sort(list4, new Comparator<Map.Entry<String, Integer>>() {
                                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                                    return (o2.getValue() - o1.getValue());
                                }
                            });
                            for (int i = 0; i < list4.size() && i < 5; i++)
                                System.out.print(list4.get(i).getKey() + ":" + list4.get(i).getValue() + "\t");
                            System.out.println();
                            System.out.println();
                            System.out.println("周末常经过地点：\n");
                            List<Entry<String, Integer>> list5 = new ArrayList<Entry<String, Integer>>(weekendpassMap.entrySet());

                            Collections.sort(list5, new Comparator<Map.Entry<String, Integer>>() {
                                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                                    return (o2.getValue() - o1.getValue());
                                }
                            });

                            for (int i = 0; i < list5.size() && i < 5; i++)
                                System.out.print(list5.get(i).getKey() + ":" + list5.get(i).getValue() + "\t");
                            System.out.println();
                            System.out.println();
                            System.out.println("节假日常经过地点：\n");
                            List<Entry<String, Integer>> list6 = new ArrayList<Entry<String, Integer>>(holidaypassMap.entrySet());
                            Collections.sort(list6, new Comparator<Map.Entry<String, Integer>>() {
                                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                                    return (o2.getValue() - o1.getValue());
                                }
                            });
                            for (int i = 0; i < list6.size() && i < 5; i++)
                                System.out.print(list6.get(i).getKey() + ":" + list6.get(i).getValue() + "\t");
                            System.out.println();
                            System.out.println();
                            System.out.println("回家及上班时间：\n");
                            for (String time : timeList)
                                System.out.println(time);

                        }
                    } else {
                        timeList.add(starttime + " - " + lasttime.toString());
                        System.out.println(lasttime.getMonth() + 1 + "月数据：");
                        lastlocation = currentlocation;
                        lasttime = currenttime;
                        lastlat = currentlat;
                        lastlng = currentlng;

                        templetregion = "?" + " 至 " + rs.getString("pass_port_name");
                        templetrate = new BigDecimal("999999999999");
                        starttime = currenttime;
                        System.out.println("工作日常留地点：\n");
                        List<Entry<String, Integer>> list1 = new ArrayList<Entry<String, Integer>>(workdaystayMap.entrySet());
                        Collections.sort(list1, new Comparator<Map.Entry<String, Integer>>() {
                            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                                return (o2.getValue() - o1.getValue());
                            }
                        });
                        for (int i = 0; i < list1.size() && i < 5; i++)
                            System.out.print(list1.get(i).getKey() + ":" + list1.get(i).getValue() + "\t");
                        System.out.println();
                        System.out.println();
                        System.out.println("周末常留地点：\n");
                        List<Entry<String, Integer>> list2 = new ArrayList<Entry<String, Integer>>(weekendstayMap.entrySet());

                        Collections.sort(list2, new Comparator<Map.Entry<String, Integer>>() {
                            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                                return (o2.getValue() - o1.getValue());
                            }
                        });
                        for (int i = 0; i < list2.size() && i < 5; i++)
                            System.out.print(list2.get(i).getKey() + ":" + list2.get(i).getValue() + "\t");
                        System.out.println();
                        System.out.println();
                        System.out.println("节假日常留地点：\n");
                        List<Entry<String, Integer>> list3 = new ArrayList<Entry<String, Integer>>(holidaystayMap.entrySet());

                        Collections.sort(list3, new Comparator<Map.Entry<String, Integer>>() {
                            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                                return (o2.getValue() - o1.getValue());
                            }
                        });
                        for (int i = 0; i < list3.size() && i < 5; i++)
                            System.out.print(list3.get(i).getKey() + ":" + list3.get(i).getValue() + "\t");
                        System.out.println();
                        System.out.println();
                        System.out.println("工作日常经过地点：\n");
                        List<Entry<String, Integer>> list4 = new ArrayList<Entry<String, Integer>>(workdaypassMap.entrySet());

                        Collections.sort(list4, new Comparator<Map.Entry<String, Integer>>() {
                            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                                return (o2.getValue() - o1.getValue());
                            }
                        });
                        for (int i = 0; i < list4.size() && i < 5; i++)
                            System.out.print(list4.get(i).getKey() + ":" + list4.get(i).getValue() + "\t");
                        System.out.println();
                        System.out.println();
                        System.out.println("周末常经过地点：\n");
                        List<Entry<String, Integer>> list5 = new ArrayList<Entry<String, Integer>>(weekendpassMap.entrySet());

                        Collections.sort(list5, new Comparator<Map.Entry<String, Integer>>() {
                            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                                return (o2.getValue() - o1.getValue());
                            }
                        });

                        for (int i = 0; i < list5.size() && i < 5; i++)
                            System.out.print(list5.get(i).getKey() + ":" + list5.get(i).getValue() + "\t");
                        System.out.println();
                        System.out.println();
                        System.out.println("节假日常经过地点：\n");
                        List<Entry<String, Integer>> list6 = new ArrayList<Entry<String, Integer>>(holidaypassMap.entrySet());
                        Collections.sort(list6, new Comparator<Map.Entry<String, Integer>>() {
                            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                                return (o2.getValue() - o1.getValue());
                            }
                        });
                        for (int i = 0; i < list6.size() && i < 5; i++)
                            System.out.print(list6.get(i).getKey() + ":" + list6.get(i).getValue() + "\t");
                        System.out.println();
                        System.out.println();
                        System.out.println("回家及上班时间：\n");
                        for (String time : timeList)
                            System.out.println(time);

                        timeList = new ArrayList<String>();

                        workdaystayMap = new HashMap<String, Integer>();
                        weekendstayMap = new HashMap<String, Integer>();
                        holidaystayMap = new HashMap<String, Integer>();

                        workdaypassMap = new HashMap<String, Integer>();
                        weekendpassMap = new HashMap<String, Integer>();
                        holidaypassMap = new HashMap<String, Integer>();


                    }
                }
            } else {
                System.out.println("车牌号输入有误或无该车牌号信息！");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}