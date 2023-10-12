package test;

import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.ltm.DataSource;

public class CMTest extends ArbiAgent {
	private static final String brokerURI 				= "127.0.0.1";
	private static final int		brokerPORT 				= 61316;
	private static final String	agentURIPrefix			= "agent://";
	private static final String	dsURIPrefix				= "ds://";
	private static final String TR_URI					= "agent://www.arbi.com/TaskReasoner";
	private static final String TM_URI					= "agent://www.arbi.com/TaskManager";
	private static final String CM_URI				 	= "www.arbi.com/contextManager";
	private static final String KM_URI 					= "agent://www.arbi.com/knowledgeManager";
	static DataSource dataSource = new DataSource();

	public CMTest() {
	}
	
	@Override
	public void onStart() {
		dataSource.connect(brokerURI, brokerPORT, dsURIPrefix+CM_URI, BrokerType.ACTIVEMQ);
	}
	
	@Override
	public void send(String receiver, String data) {
		// TODO Auto-generated method stub
		super.send(receiver, data);
	}

	public static void main(String[] args) throws InterruptedException {
		ArbiAgent cm = new CMTest();	
		ArbiAgentExecutor.execute(brokerURI, brokerPORT,agentURIPrefix+CM_URI, cm, BrokerType.ACTIVEMQ);
		

		System.out.println("CMTest start");
		dataSource.assertFact("(context (rackAt \"rack01\" 1 1))");
		Thread.sleep(1000);
		dataSource.assertFact("(context (rackAt \"rack04\" 4 4))");
		Thread.sleep(1000);
		cm.send(TR_URI, "(receivedCargo \"http://www.arbi.com/ontologies/arbi.owl#bin_01\" \"http://www.arbi.com/ontologies/arbi.owl#station1\" \"agent://www.mcarbi.com/SemanticMapManager1\")");
		Thread.sleep(2000);
		cm.send(TR_URI, "(TaskCreated \"test1\" \"111\" \"http://www.arbi.com/ontologies/arbi.owl#station1\" \"agent://www.mcarbi.com/SemanticMapManager1\")");
		Thread.sleep(2000);
		cm.send(TR_URI, "(receivedCargo \"http://www.arbi.com/ontologies/arbi.owl#bin_01\" \"http://www.arbi.com/ontologies/arbi.owl#station2\" \"agent://www.mcarbi.com/SemanticMapManager1\")");
		Thread.sleep(2000);
		cm.send(TR_URI, "(TaskCompleted \"test1\" \"111\" \"http://www.arbi.com/ontologies/arbi.owl#station1\")");
		Thread.sleep(2000);
		cm.send(TR_URI, "(receivedCargo \"http://www.arbi.com/ontologies/arbi.owl#bin_01\" \"http://www.arbi.com/ontologies/arbi.owl#station2\" \"agent://www.mcarbi.com/SemanticMapManager1\")");
		/*
		cm.send(TR_URI, "(StudyMethod \"audio\")");
		Thread.sleep(1000);
		cm.send(TR_URI, "(StudyMethod \"video\")");
		Thread.sleep(1000);		
		cm.send(TR_URI, "(StudyTool \"tablet\")");
		Thread.sleep(1000);
		cm.send(TR_URI, "(retract (StudyMethod \"audio\"))");
		cm.send(TR_URI, "(StudentRespnseTime \"3\")");
		cm.send(TR_URI, "(StudentHeartbeat \"50\")");
		Thread.sleep(1000);

		cm.send(TR_URI, "(StudentEmotion \"happy\")");
		*/
	}
	
}
