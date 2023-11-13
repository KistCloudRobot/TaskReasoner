package kr.ac.uos.ai.robot.intelligent.taskReasoner.utility;

import java.util.UUID;

public class TaskGenerator {
	
	
	
	public String generatePotenitTasks(String station1, String station2, String station3, String station4) {
		StringBuilder builder = new StringBuilder();
		
		builder.append("(TaskRequest (PersonCall \"");
		builder.append(UUID.randomUUID() + "\" (commands ");
		if (station1.equals("exist")) {
			builder.append("(command \"http://www.arbi.com/ontologies/arbi.owl#station1\" \"Storing\")");
		} else if (station1.equals("empty")) {
			builder.append("(command \"http://www.arbi.com/ontologies/arbi.owl#station1\" \"PrepareStoring\")");
		} else if (station1.equals("using")) {
			System.out.println("station1 is still using");
		}
		
		if (station2.equals("exist")) {
			builder.append("(command \"http://www.arbi.com/ontologies/arbi.owl#station2\" \"Storing\")");
		} else if (station2.equals("empty")) {
			builder.append("(command \"http://www.arbi.com/ontologies/arbi.owl#station2\" \"PrepareStoring\")");
		} else if (station2.equals("using")) {
			System.out.println("station2 is still using");
		}
		
		if (station3.equals("exist")) {
			builder.append("(command \"http://www.arbi.com/ontologies/arbi.owl#station3\" \"Unstoring\")");
		} else if (station3.equals("empty")) {
			builder.append("(command \"http://www.arbi.com/ontologies/arbi.owl#station3\" \"PrepareUnstoring\")");
		} else if (station3.equals("using")) {
			System.out.println("station3 is still using");
		}
		
		if (station4.equals("exist")) {
			builder.append("(command \"http://www.arbi.com/ontologies/arbi.owl#station4\" \"Unstoring\")");
		} else if (station4.equals("empty")) {
			builder.append("(command \"http://www.arbi.com/ontologies/arbi.owl#station4\" \"PrepareUnstoring\")");
		} else if (station4.equals("using")) {
			System.out.println("station4 is still using");
		}
		builder.append(")))");
		return builder.toString();
	}
	
	
	public String generateIsaacTasks(String station1, String station2, String station3,String station4) {
		StringBuilder builder = new StringBuilder();
		
		builder.append("(TaskRequest (PersonCall \"");
		builder.append(UUID.randomUUID() + "\" (commands ");
		if (station1.equals("exist")) {
			builder.append("(command \"http://www.arbi.com/ontologies/arbi.owl#station1001\" \"Storing\")");
		} else if (station1.equals("empty")) {
			builder.append("(command \"http://www.arbi.com/ontologies/arbi.owl#station1001\" \"PrepareStoring\")");
		} else if (station1.equals("using")) {
			System.out.println("station1 is still using");
		}
		
		if (station4.equals("exist")) {
			builder.append("(command \"http://www.arbi.com/ontologies/arbi.owl#station1004\" \"Unstoring\")");
		} else if (station4.equals("empty")) {
			builder.append("(command \"http://www.arbi.com/ontologies/arbi.owl#station1004\" \"PrepareUnstoring\")");
		} else if (station4.equals("using")) {
			System.out.println("station1 is still using");
		}
		
		if (station2.equals("exist")) {
			builder.append("(command \"http://www.arbi.com/ontologies/arbi.owl#station1002\" \"Storing\")");
		} else if (station3.equals("exist")) {
			builder.append("(command \"http://www.arbi.com/ontologies/arbi.owl#station1003\" \"Unstoring\")");
		} else if (station2.equals("empty")) {
			builder.append("(command \"http://www.arbi.com/ontologies/arbi.owl#station1002\" \"PrepareStoring\")");
		} else if (station3.equals("empty")) {
			builder.append("(command \"http://www.arbi.com/ontologies/arbi.owl#station1003\" \"PrepareUnstoring\")");
		}

		builder.append(")))");
		return builder.toString();
	}
	
	public String generateIsaacStoringTasks(String station1) {
		StringBuilder builder = new StringBuilder();
		
		builder.append("(TaskRequest (PersonCall \"");
		builder.append(UUID.randomUUID() + "\" (commands ");
		if (station1.equals("exist")) {
			builder.append("(command \"http://www.arbi.com/ontologies/arbi.owl#station1001\" \"Storing\")");
		} else if (station1.equals("empty")) {
			builder.append("(command \"http://www.arbi.com/ontologies/arbi.owl#station1001\" \"PrepareStoring\")");
		} else if (station1.equals("using")) {
			System.out.println("station1 is still using");
		}

		builder.append(")))");
		return builder.toString();
	}

	public String generateIsaacUnstoringTasks(String station4) {
		StringBuilder builder = new StringBuilder();
		
		builder.append("(TaskRequest (PersonCall \"");
		builder.append(UUID.randomUUID() + "\" (commands ");
		if (station4.equals("exist")) {
			builder.append("(command \"http://www.arbi.com/ontologies/arbi.owl#station1004\" \"Unstoring\")");
		} else if (station4.equals("empty")) {
			builder.append("(command \"http://www.arbi.com/ontologies/arbi.owl#station1004\" \"PrepareUnstoring\")");
		} else if (station4.equals("using")) {
			System.out.println("station1 is still using");
		}
		

				
		builder.append(")))");
		return builder.toString();
	}
}
