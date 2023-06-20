package kr.ac.uos.ai.robot.intelligent.taskReasoner;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.agent.logger.LoggerManager;
import kr.ac.uos.ai.arbi.ltm.DataSource;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.model.parser.ParseException;
import kr.ac.uos.ai.robot.intelligent.taskReasoner.action.TaskReasonerAction;
import kr.ac.uos.ai.robot.intelligent.taskReasoner.action.argument.ContextArgument;
import kr.ac.uos.ai.robot.intelligent.taskReasoner.message.GLMessageManager;
import kr.ac.uos.ai.robot.intelligent.taskReasoner.message.JsonMessageManager;
import kr.ac.uos.ai.robot.intelligent.taskReasoner.message.RecievedMessage;
import kr.ac.uos.ai.robot.intelligent.taskReasoner.policy.PolicyHandler;
import kr.ac.uos.ai.robot.intelligent.taskReasoner.service.ServiceModelGenerator;
import kr.ac.uos.ai.robot.intelligent.taskReasoner.utility.UtilityCalculator;
import uos.ai.jam.Interpreter;
import uos.ai.jam.JAM;

public class TaskReasoner extends ArbiAgent {

	public static String ENV_JMS_BROKER;
	public static String ENV_AGENT_NAME;
	public static String ENV_ROBOT_NAME;
	public static final String ARBI_PREFIX = "www.arbi.com/";

	private static String brokerURI = "tcp://172.16.165.141:61316";
	private static String TASKREASONER_ADDRESS = "www.arbi.com/TaskReasoner";
	private static String TASKMANAGER_ADDRESS  = "www.arbi.com/TaskManager";

	private static final String agentURIPrefix = "agent://";
	private static final String dsURIPrefix = "ds://";

	private Interpreter interpreter;
	private GLMessageManager glMessageManager;
	private BlockingQueue<RecievedMessage> messageQueue;
	private DataSource ds;
	private PlanLoader planLoader;
	private PolicyHandler policyHandler;
	private ServiceModelGenerator serviceModelGenerator;
	private TaskReasonerAction taskReasonerAction;
	private LoggerManager loggerManager;
	private JsonMessageManager jsonMessageManager;
	private UtilityCalculator utilityCalculator;

	private int workflowID;
//
//	public TaskReasoner_Local() {
//
//		initAddress();
//		// config();
//		interpreter = JAM.parse(new String[] { "TaskReasonerLocalPlan/boot.jam" });
//
//		ds = new TaskReasonerDataSource(this);
//
//		messageQueue = new LinkedBlockingQueue<RecievedMessage>();
//		glMessageManager = new GLMessageManager(interpreter, ds);
//		planLoader = new PlanLoader(interpreter);
//		serviceModelGenerator = new ServiceModelGenerator(this);
//		policyHandler = new PolicyHandler(this, interpreter);
//		jsonMessageManager = new JsonMessageManager(policyHandler);
//		// server = new Server(this);
//		utilityCalculator = new UtilityCalculator(interpreter);
//
//		ArbiAgentExecutor.execute(ENV_JMS_BROKER, agentURIPrefix + TASKREASONER_ADDRESS, this, brokerType);
//
//		loggerManager = LoggerManager.getInstance();
//
//		taskReasonerAction = new TaskReasonerAction(this, interpreter, loggerManager);
//		
//		workflowID = 0;
//		init();
//	}

	public TaskReasoner(String role, String brokerAddress, int port) {
		ENV_JMS_BROKER = brokerAddress;
		interpreter = JAM.parse(new String[] { "./plan/boot.jam" });

		ds = new TaskReasonerDataSource(this);

		ds.connect(ENV_JMS_BROKER, port, dsURIPrefix + TASKREASONER_ADDRESS, BrokerType.ACTIVEMQ);
		messageQueue = new LinkedBlockingQueue<RecievedMessage>();
		glMessageManager = new GLMessageManager(interpreter, ds);
		planLoader = new PlanLoader(interpreter);
		serviceModelGenerator = new ServiceModelGenerator(this);
		policyHandler = new PolicyHandler(this, interpreter);
		jsonMessageManager = new JsonMessageManager(policyHandler);
		utilityCalculator = new UtilityCalculator(interpreter);

		ArbiAgentExecutor.execute(ENV_JMS_BROKER,port , agentURIPrefix + TASKREASONER_ADDRESS, this, BrokerType.ACTIVEMQ);

		loggerManager = LoggerManager.getInstance();

		taskReasonerAction = new TaskReasonerAction(this, interpreter, loggerManager);
		
		workflowID = 0;
		glMessageManager.assertFact("AssignedRole",role);
		init();
		
	}

	public void initAddress() {
		//String ip = System.getenv("JMS_BROKER");
		//ENV_JMS_BROKER = "tcp://" + ip ;
		//ENV_AGENT_NAME = System.getenv("AGENT");
		//ENV_ROBOT_NAME = System.getenv("ROBOT");
		
		ENV_JMS_BROKER = "tcp://172.16.165.141:61313";


	}

	private void init() {

		glMessageManager.assertFact("GLMessageManager", glMessageManager);
		glMessageManager.assertFact("PolicyHandler", policyHandler);
		glMessageManager.assertFact("ServiceModelGenerator", serviceModelGenerator);
		glMessageManager.assertFact("LoggerManager", taskReasonerAction);
		glMessageManager.assertFact("TaskReasoner", this);
		glMessageManager.assertFact("JsonMessageManager", jsonMessageManager);
		glMessageManager.assertFact("UtilityCalculator", utilityCalculator);

		Thread t = new Thread() {
			public void run() {
				interpreter.run();
			}
		};

		t.run();
	}

	public int getUtility(String roleName) {
		return 0;
	}

	public Boolean sleepAwhile(int mileSecond) {
		try {
			Thread.sleep(mileSecond);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}

	@Override
	public void onStart() {
		System.out.println("====onStart====");
		// goal and context is wrapped
		// String subscriveGoal = "(rule (fact (goal $goal $precondition
		// $postcondition)) --> (notify (goal $goal $precondition $postcondition)))";
		// ds.subscribe(subscriveGoal);
		// (goal (goalName ))

		String subscriveContext = "(rule (fact (context (PersonCall $callID $location))) --> (notify (context (PersonCall $callID $location))))";
		System.out.println(ds.subscribe(subscriveContext));
		// String subscriveContext = "(rule (fact (context $context)) --> (notify
		// (context $context)))";
		// System.out.println(ds.subscribe(subscriveContext));

		System.out.println("reasoner boot complete!");

	}

	@Override
	public void onNotify(String sender, String notification) {
		System.out.println("Notyfied from " + sender + ". Message is " + notification);
		RecievedMessage msg = new RecievedMessage(sender, notification);
		try {
			messageQueue.put(msg);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String onRequest(String sender, String request) {
		return null;
	}

	@Override
	public void onData(String sender, String data) {
		try {
			//System.out.println("data  = " + data);
			RecievedMessage message = new RecievedMessage(sender, data);

			messageQueue.put(message);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	public boolean dequeueMessage() {

		if (messageQueue.isEmpty())
			return false;
		else {
			try {
				RecievedMessage message = messageQueue.take();
				GeneralizedList gl = null;
				String data = message.getMessage();
				String sender = message.getSender();

				gl = GLFactory.newGLFromGLString(data);

				//System.out.println("message from " + sender  + " dequeued : " + gl.toString());

				if (gl.getName().equals("context")) {

					glMessageManager.assertGL(gl.getExpression(0).asGeneralizedList());
				} else if (gl.getName().equals("GoalCompleted")) {
					System.out.println("completed goal name : " + gl.getExpression(0).asValue().stringValue());
					glMessageManager.assertGL(gl);
				} else if(gl.getExpression(0).isGeneralizedList()) {
					System.out.println(gl.toString());
				} else {
					glMessageManager.assertFact("RecievedMessage", sender, data);
				}
			} catch (InterruptedException | ParseException e) {
				e.printStackTrace();
			}

			return true;
		}
	}

	public boolean sendToTM(String type, String gl) {
		System.out.println("send to tm : " + type + ", " + gl);
		this.send(agentURIPrefix + TASKMANAGER_ADDRESS, "(" + type + " " + gl + ")");

		return true;
	}
	
	public void sleep(int count) {
		try {
			Thread.sleep(count);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public String getWorklflowID(String serviceName) {
		workflowID += workflowID;
		if (serviceName.startsWith("\"")) {
			serviceName = serviceName.substring(1,serviceName.length()-1);
		}
		String id = serviceName + workflowID;
		return id;
	}
	
	public void parsePlan(String string) {
		planLoader.parsePlan(string);
	}

	public void loadPlan(String string) {
		planLoader.loadPlan(string);
	}

	public void loadPlanPackage(String string) {
		planLoader.loadPlanPackage(string);
	}

	public void assertFact(String name, Object... args) {
		glMessageManager.assertFact(name, args);
	}

	public GLMessageManager getGlMessageManager() {
		return glMessageManager;
	}

	public PolicyHandler getPolicyHandler() {
		return policyHandler;
	}

	public ServiceModelGenerator getServiceModelGenerator() {
		return serviceModelGenerator;
	}

	public void receivedPolicyMessage(String str) {
		jsonMessageManager.updateLMPolicyValue(str);
	}

	public void putUtilityFunction(String serviceName, String stringFunction) {
		utilityCalculator.putUtilityFunction(serviceName, stringFunction);
	}
	
	public static void main(String[] args) {

		String brokerAddress = "";
		String robotID;
		int port;
		if(args.length == 0) {
			brokerAddress = "172.16.165.141";
//			brokerAddress = "192.168.100.10";
			robotID = "Local";	
			port = 61316;
		} else {
			robotID = args[0];
			brokerAddress = args[1];
			port = Integer.parseInt(args[2]);
		}
		
		TaskReasoner agent = new TaskReasoner(robotID, brokerAddress,port);
	}

	
}
