package kr.ac.uos.ai.robot.intelligent.taskReasoner.action;

import kr.ac.uos.ai.arbi.agent.logger.ActionBody;
import kr.ac.uos.ai.robot.intelligent.taskReasoner.TaskReasoner_Local;
import kr.ac.uos.ai.robot.intelligent.taskReasoner.action.argument.GoalPostArgument;

public class GoalUnpostAction implements ActionBody{

	private TaskReasoner_Local reasoner;
	
	public GoalUnpostAction(TaskReasoner_Local resoner) {
		this.reasoner = resoner;
		
		// TODO Auto-generated constructor stub
	}
	@Override
	public Object execute(Object o) {
		GoalPostArgument argument = (GoalPostArgument) o;
		
		reasoner.sendToTM("unpostGoal", argument.getGoalName());
		// TODO Auto-generated method stub
		return null;
	}

}
