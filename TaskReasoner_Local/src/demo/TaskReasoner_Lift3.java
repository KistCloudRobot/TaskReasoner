package demo;

import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.robot.intelligent.taskReasoner.TaskReasoner;

public class TaskReasoner_Lift3 {
	
	public static void main(String[] args) {
		String role = "carrier";
		String brokerAddress = "172.16.165.143";
		int port = 61114;
		TaskReasoner reasoner = new TaskReasoner(role, brokerAddress, port);
	}
}
