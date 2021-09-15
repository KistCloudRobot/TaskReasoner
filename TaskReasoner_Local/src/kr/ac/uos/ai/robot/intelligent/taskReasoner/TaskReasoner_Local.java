package kr.ac.uos.ai.robot.intelligent.taskReasoner;


import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

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
import kr.ac.uos.ai.robot.intelligent.taskReasoner.server.Server;
import kr.ac.uos.ai.robot.intelligent.taskReasoner.service.ServiceModelGenerator;
import kr.ac.uos.ai.robot.intelligent.taskReasoner.utility.UtilityCalculator;
import uos.ai.jam.Interpreter;
import uos.ai.jam.JAM;

public class TaskReasoner_Local extends ArbiAgent {

	public static String ENV_JMS_BROKER;
	public static String ENV_AGENT_NAME;
	public static String ENV_ROBOT_NAME;
	public static final String ARBI_PREFIX = "www.arbi.com/";
	
	private static String brokerURI = "tcp://172.16.165.204:8000";
	private static String TASKREASONER_ADDRESS;
	private static int brokerType = 2;
	private static String TASKMANAGER_ADDRESS;

	
	private static final String	agentURIPrefix			= "agent://";
	private static final String	dsURIPrefix				= "ds://";
	
	private Interpreter									interpreter;
	private GLMessageManager							glMessageManager;
	private BlockingQueue<RecievedMessage>				messageQueue;
	private DataSource									ds;
	private PlanLoader 									planLoader;
	private PolicyHandler								policyHandler;
	private ServiceModelGenerator						serviceModelGenerator;
	private TaskReasonerAction							taskReasonerAction;
	private LoggerManager 								loggerManager;
	private JsonMessageManager							jsonMessageManager;
	private UtilityCalculator							utilityCalculator;
	

	private int logisticManagerUtility;

	private int StoringManagerUtility;
	public TaskReasoner_Local() {

		initAddress();
		//config();
		interpreter = JAM.parse(new String[] {"plan/boot.jam"} );
		
		ds = new TaskReasonerDataSource(this);
		
		messageQueue= new LinkedBlockingQueue<RecievedMessage>();
		glMessageManager = new GLMessageManager(interpreter, ds);
		planLoader = new PlanLoader(interpreter);
		serviceModelGenerator = new ServiceModelGenerator(this);
		policyHandler = new PolicyHandler(this,interpreter);
		jsonMessageManager = new JsonMessageManager(policyHandler);
		//server = new Server(this);
		utilityCalculator = new UtilityCalculator(interpreter);
		
		ArbiAgentExecutor.execute(brokerURI, agentURIPrefix+TASKREASONER_ADDRESS, this, brokerType);

		loggerManager = LoggerManager.getInstance();
		
		taskReasonerAction = new TaskReasonerAction(this, interpreter, loggerManager);
		logisticManagerUtility = 100;
		StoringManagerUtility = 99; 
		init();
	}


	public void initAddress() {
		ENV_JMS_BROKER = System.getenv("JMS_BROKER");
		ENV_AGENT_NAME = System.getenv("AGENT");
		ENV_ROBOT_NAME = System.getenv("ROBOT");
		
		TASKMANAGER_ADDRESS = agentURIPrefix + ARBI_PREFIX + ENV_AGENT_NAME + "/TaskManager";
		TASKREASONER_ADDRESS = ARBI_PREFIX + ENV_AGENT_NAME + "/TaskReasoner";
		
	}
	private void config() {

		try {
			File file = new File("configuration/TaskReasonerConfiguration.xml");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(file);
			XPathFactory xPathFactory = XPathFactory.newInstance();
			XPath xPath = xPathFactory.newXPath();
			
			XPathExpression _brokerURI = xPath.compile("//ServerURL");
			Node n = (Node) _brokerURI.evaluate(doc, XPathConstants.NODE);
			brokerURI = n.getTextContent();
			
			XPathExpression _myURI = xPath.compile("//AgentName");
			n = (Node) _myURI.evaluate(doc, XPathConstants.NODE);
			TASKREASONER_ADDRESS = n.getTextContent();
			
			XPathExpression _brokerType = xPath.compile("//BrokerType");
			n = (Node) _brokerType.evaluate(doc, XPathConstants.NODE);
			if (n.getTextContent().equals("ZeroMQ")) {
				brokerType = 2;
			} else if (n.getTextContent().equals("Apollo")) {
				brokerType = 1;
			}
			
			XPathExpression _TM_URI = xPath.compile("//TaskManagerName");
			n = (Node) _TM_URI.evaluate(doc, XPathConstants.NODE);
			TASKMANAGER_ADDRESS = n.getTextContent();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		if(roleName.equals("LogisticManager")) {
			logisticManagerUtility -= 2;
			System.out.println("utility : " + logisticManagerUtility);
			return logisticManagerUtility;
		} else if (roleName.equals("StoringCarrier")) {

			StoringManagerUtility -= 2;
			System.out.println("utility : " + StoringManagerUtility);
			return StoringManagerUtility;
		}
		
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
		ds.connect(brokerURI, dsURIPrefix+TASKREASONER_ADDRESS, 2);
		//goal and context is wrapped
		//String subscriveGoal = "(rule (fact (goal $goal $precondition $postcondition)) --> (notify (goal $goal $precondition $postcondition)))";
		//ds.subscribe(subscriveGoal);
		// (goal (goalName ))
				
		String subscriveContext = "(rule (fact (context (PersonCall $callID $location $cmd))) --> (notify (context (PersonCall $callID $location $cmd))))";
		System.out.println(ds.subscribe(subscriveContext));
		//String subscriveContext = "(rule (fact (context $context)) --> (notify (context $context)))";
		//System.out.println(ds.subscribe(subscriveContext));
			
		
		
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
			System.out.println("data  = " + data);
			RecievedMessage message = new RecievedMessage(sender, data);

			messageQueue.put(message);
		} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		ArbiAgent agent = new TaskReasoner_Local();
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

				System.out.println("message dequeued : " + gl.toString());

				if (gl.getName().equals("context")) {

					glMessageManager.assertContext(gl.getExpression(0).asGeneralizedList());
				} else if (gl.getName().equals("goalComplete")) {
					glMessageManager.assertContext(gl.getExpression(0).asGeneralizedList());
					glMessageManager.assertFact("GoalCompleted",gl.getExpression(0).asGeneralizedList().getName());
				} else {
					glMessageManager.assertFact("RecievedMessage", sender, data);
				}

			} catch (InterruptedException | ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return true;
		}
	}
			
	public boolean sendToTM(String type, String gl) {
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("send to tm : " + type + ", " + gl);
		this.send(agentURIPrefix + TASKMANAGER_ADDRESS, "(" + type + " " + gl+ ")");
		
		return true;
	}
	
	public void parsePlan(String string) {
		planLoader.parsePlan(string);
	}
	
	public void loadPlan(String string ) {
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
}
