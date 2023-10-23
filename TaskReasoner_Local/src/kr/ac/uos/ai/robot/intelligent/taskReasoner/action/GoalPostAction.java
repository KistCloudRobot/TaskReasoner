package kr.ac.uos.ai.robot.intelligent.taskReasoner.action;

import kr.ac.uos.ai.arbi.agent.logger.action.ActionBody;
import kr.ac.uos.ai.robot.intelligent.taskReasoner.TaskReasoner;
import kr.ac.uos.ai.robot.intelligent.taskReasoner.action.argument.GoalPostArgument;
import kr.ac.uos.ai.robot.intelligent.taskReasoner.message.GLMessageManager;

public class GoalPostAction implements ActionBody {

	private TaskReasoner reasoner;
	
	public GoalPostAction(TaskReasoner	reasoner) {
		this.reasoner = reasoner;
	}
	
	@Override
	public Object execute(Object o) {
		GoalPostArgument argument = (GoalPostArgument)o;
//		reasoner.sendToTM("PostGoal", argument.toGenerallizedList());
		
		return null;
	}
}
