package demo;

import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.robot.intelligent.taskReasoner.TaskReasoner;

public class TaskReasoner_Lift1 {
	
	public static void main(String[] args) {
		String role = "carrier";
		String brokerAddress = "172.16.165.158";
		int port = 61116;
		TaskReasoner reasoner = new TaskReasoner(role, brokerAddress, port);
	}
}
