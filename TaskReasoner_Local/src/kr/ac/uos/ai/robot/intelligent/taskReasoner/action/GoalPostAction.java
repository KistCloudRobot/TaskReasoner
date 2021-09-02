package kr.ac.uos.ai.robot.intelligent.taskReasoner.action;

import kr.ac.uos.ai.arbi.agent.logger.ActionBody;
import kr.ac.uos.ai.robot.intelligent.taskReasoner.TaskReasoner_Local;
import kr.ac.uos.ai.robot.intelligent.taskReasoner.action.argument.GoalPostArgument;
import kr.ac.uos.ai.robot.intelligent.taskReasoner.message.GLMessageManager;

public class GoalPostAction implements ActionBody {

	private TaskReasoner_Local reasoner;
	
	public GoalPostAction(TaskReasoner_Local	reasoner) {
		this.reasoner = reasoner;
	}
	
	@Override
	public Object execute(Object o) {
		GoalPostArgument argument = (GoalPostArgument)o;
		reasoner.sendToTM("postGoal", argument.getGoalName(), argument.getArguments());
		
		// TODO Auto-generated method stub
		return null;
	}
}
