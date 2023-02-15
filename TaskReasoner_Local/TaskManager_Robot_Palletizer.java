package demo;

import taskManager.TaskManager_Robot;

public class TaskManager_Robot_Palletizer {
	public static void main(String[] args) {	String brokerAddress;
	String robotID;
	if(args.length == 0) {
//		brokerAddress = "tcp://172.16.165.141:61112";
		brokerAddress = "tcp://192.168.100.10:62112";
//		brokerAddress = "tcp://127.0.0.1:62112";
		robotID = "Palletizer";	
	} else {
		robotID = args[0];
		brokerAddress = args[1];
	}
	
	TaskManager_Robot tm = new TaskManager_Robot(robotID, brokerAddress);
		
	}
}
