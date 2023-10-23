package demo;

import kr.ac.uos.ai.robot.intelligent.taskReasoner.TaskReasoner;

public class TaskReasoner_Docker {
	public static void main(String[] args) {
		String role = System.getenv("AGENT_ID");
		String brokerAddress = System.getenv("BROKER_ADDRESS");
		String stringPort = System.getenv("BROKER_PORT");
		int port;
		
		if (stringPort != null) {
			port = Integer.parseInt(stringPort);
		} else {
			brokerAddress = "127.0.0.1";
			port = 61316;
			role = "agent://www.mcarbi.com/Local1";
		}
		
		TaskReasoner taskReasoner = new TaskReasoner(role, brokerAddress, port);
		
//		taskReasoner.send("agent://www.arbi.com/TaskReasoner", "(context (rackAt \"rack01\" 1 1))");
//		taskReasoner.onData("test", "(context (rackAt \"rack04\" 4 4))");
//		taskReasoner.onData("test", "(receivedCargo \"http://www.arbi.com/ontologies/arbi.owl#bin_01\" \"http://www.arbi.com/ontologies/arbi.owl#station1\" \"agent://www.mcarbi.com/SemanticMapManager1\")");
	}
}

