package demo;

import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.robot.intelligent.taskReasoner.TaskReasoner;

public class TaskReasoner_Local {
	
	public static void main(String[] args) {
		String role = "logisticManager";
		String brokerAddress = "172.16.165.158";
		int port = 61316;
		TaskReasoner reasoner = new TaskReasoner(role, brokerAddress, port);
	}
}
