package cn.edu.cqupt.service.warehouse_management;

import java.util.ArrayList;

import cn.edu.cqupt.beans.Device;
import cn.edu.cqupt.dao.DeviceDao;

public class DeviceService {
	private DeviceDao deviceDao=new DeviceDao();
	
	public boolean addDevice(Device device){
		return deviceDao.addDevice(device);
	}
	
	public boolean updateDevice(Device device,long deviceId) {
		return deviceDao.updateDevice(device, deviceId);
	}
	
	public boolean deleteDevice(long deviceId) {
		return deviceDao.deleteDevice(deviceId);
	}
	
	public ArrayList<Device> searchDeviceByPage(int curPageNum, int pageSize){
		return deviceDao.searchDeviceByPage(curPageNum, pageSize);
	}
	
	public long getDeviceSum(){
		return deviceDao.getDeviceSum();
	}
	
	public ArrayList<String> getDeviceType(){
		return deviceDao.getDeviceType();
	}
	
	public ArrayList<Device> searchDeviceByCondition(String deviceStartTime,String deviceEndTime,String deviceType,String deviceStatus,int curPageNum,int pageSize) {
		ArrayList<Device> curDeviceList=deviceDao.searchDeviceInfoByCondition(deviceStartTime, deviceEndTime, deviceType, deviceStatus, curPageNum, pageSize);
		return curDeviceList;
	}
	
	public long getDeviceSumByCondition(String deviceStartTime,String deviceEndTime,String deviceType,String deviceStatus){
		long deviceSum=0;
		deviceSum=deviceDao.getDeviceSumByCondition(deviceStartTime, deviceEndTime, deviceType, deviceStatus);
		return deviceSum;
	}
	
	public Device getDeviceById(long deviceId){
		Device device=new Device();
		device=deviceDao.getDeviceById(deviceId);
		return device;
	}

	
	/*public static void main(String[] args) {
		ArrayList<Device> curdeviceList=new DeviceService().searchDeviceByCondition("", "", "空调", 0, 10);
		for (Device device : curdeviceList) {
			System.out.println(device.getDeviceName());
		}
	}*/
}
