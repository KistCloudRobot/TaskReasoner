package demo;

import kr.ac.uos.ai.robot.intelligent.taskReasoner.TaskReasoner;

public class TaskReasoner_Docker {
	public static void main(String[] args) {
		String role = System.getenv("ROLE");
		String brokerAddress = System.getenv("BROKER_ADDRESS");
		String stringPort = System.getenv("BROKER_PORT");
		int port = Integer.parseInt(stringPort);
		
		TaskReasoner taskReasoner = new TaskReasoner(role, brokerAddress, port);
	}
}
