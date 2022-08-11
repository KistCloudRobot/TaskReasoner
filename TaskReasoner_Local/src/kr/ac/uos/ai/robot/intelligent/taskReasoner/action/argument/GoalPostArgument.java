package kr.ac.uos.ai.robot.intelligent.taskReasoner.action.argument;

import org.json.simple.JSONObject;

public class GoalPostArgument {
	private String goalName;
	private String serviceName;
	private Object[] arguments;
	
	public GoalPostArgument() {
	}
	
	public String getGoalName() {
		return goalName;
	}

	public String getServiceName() {
		return serviceName;
	}
	public void setGoalName(String goalName) {
		this.goalName = goalName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public void setArguments(Object[] arguments) {
		this.arguments = arguments;
	}
	public Object[] getArguments() {
		return arguments;
	}
	
	@Override
	public String toString() {
		JSONObject json = new JSONObject();
		
		json.put("ServiceName", serviceName);
		json.put("GoalName", goalName);
		
		return json.toJSONString();
	}
	
	public String toGenerallizedList() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("(" + goalName);
		for(Object o : arguments) {
			builder.append(" " + o.toString());
		}
		builder.append(")");
		return builder.toString();
	}
	
}
