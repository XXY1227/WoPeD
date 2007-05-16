package org.woped.quantana.simulation;


import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import org.woped.core.config.ConfigurationManager;
import org.woped.core.utilities.LoggerManager;
import org.woped.quantana.Constants;
import org.woped.quantana.graph.Arc;
import org.woped.quantana.graph.Node;
import org.woped.quantana.graph.WorkflowNetGraph;
import org.woped.quantana.resourcealloc.Resource;
import org.woped.quantana.resourcealloc.ResourceAllocation;
import org.woped.quantana.resourcealloc.ResourceUtilization;


public class Simulator {
	
	public static final int QD_FIFO	= 1;
	public static final int QD_LIFO = 2;
	
	public static final int STOP_NONE			= 0;
	public static final int STOP_CASE_DRIVEN	= 1;
	public static final int STOP_TIME_DRIVEN	= 2;
	public static final int STOP_BOTH			= 3;
	
	public static final int RES_USED		= 1;
	public static final int RES_NOT_USED	= 2;
	
	public static final Logger protocol = Logger.getLogger("org.woped.quantana.simulation");
	
	private ByteArrayOutputStream out;

	private String protocolPath;
	private String protocolName;
	
//	private QuantitativeSimulationDialog dlgSim;
	private WorkflowNetGraph process;
	private ResourceAllocation resAlloc;
	private ResourceUtilization resUtil;
	private int useResAlloc;
	private CaseGenerator caseGenerator;
	private SeedGenerator seedGenerator;
	private int numRuns;
	private double clock;
	private int maxNumCasesInSystem = 0;
	private int caseCount = 0;
	private int finishedCases = 0;
	private double avgCasesInSystem = 0.0;
	private double timeOfLastEvent = 0.0;
	private double timeOfLastCaseNumChange = 0.0;
	private double throughPut = 0.0;
	private double caseBusy = 0.0;
	private double caseWait = 0.0;
	private int typeOfDistForCases = 0;
	private int typeOfDistForServer = 0;
	private double caseParam1 = 0.0;
	private double caseParam2 = 0.0;
	private int queueDiscipline = 0;
	private int stopRule = 0;
	private double lambda = 50.0;
	private double timeOfPeriod = 480.0;
	private Random fstServChoice = new Random(new Date().getTime());
	private int[][] fstServList;
	
	private SimEvent nextEvent = null;
	private HashMap<String, Server> serverList = new HashMap<String, Server>();
	private PriorityQueue<SimEvent> eventList = new PriorityQueue<SimEvent>();
	private HashMap<Integer, Case> caseList	 = new HashMap<Integer, Case>();
	
	public Simulator(//QuantitativeSimulationDialog qsd, 
			WorkflowNetGraph wfpn, ResourceUtilization ru, SimParameters sp){
//		dlgSim = qsd;
		process = wfpn;
		resUtil = ru;
		resAlloc = ru.getResAlloc();
		
		this.numRuns = sp.getRuns();
		this.typeOfDistForCases = sp.getDistCases();
		this.typeOfDistForServer = sp.getDistServ();
		this.caseParam1 = sp.getCPara1();
		this.caseParam2 = sp.getCPara2();
		this.queueDiscipline = sp.getQueue();
		this.stopRule = sp.getStop();
		this.lambda = sp.getLambda();
		this.timeOfPeriod = sp.getTimeOfPeriod();
		this.useResAlloc = sp.getResUse();
		
		getFstServList();
		seedGenerator = new SeedGenerator();
	}
	
	public void start() {
		
		initProtocol();
		protocol.info(clckS() + "Simulation wird gestartet!");
		protocol.info(clckS() + "Verwendung Ressourcenmodell: " + printResModUsed());
		protocol.info(clckS() + "Warteschlangendisziplin: " + printQueueDiscipline());
		
		generateServerList();
		protocol.info(clckS() + "Server-Liste wurde erzeugt.");
		
		protocol.info(clckS() + "Anzahl der Simulationsl�ufe: " + numRuns);
		for (int i = 0; i < numRuns; i++){
			protocol.info(clckS() + "**************************************************");
			protocol.info(clckS() + "Lauf # " + (i + 1) + ": ");
			
			protocol.info(clckS() + "Initialisierung des Simulators: ");
			init(i + 1);
			
			protocol.info(clckS() + "Start: ");
			while (!shouldStopNow()){
				protocol.info(clckS() + "Anzahl abgearbeiteter Cases ist " + finishedCases);
				
				timing();
				
				if (nextEvent != null){
//					LoggerManager.info(Constants.QUANTANA_LOGGER, printEventList() + "::" + nextEvent.getName());
					protocol.info(clckS() + nextEvent.getEventTypeName());
					nextEvent.invoke();
				}
				else
					break;
			}
			
			LoggerManager.info(Constants.QUANTANA_LOGGER, "Report wird erzeugt.");
			protocol.info(clckS() + "Simulationsbericht wird generiert.");
			generateReport();
		}
	}
	
	private void init(int run){

		protocol.info(clckS() + "Simulationsuhr wird auf 00:00 gesetzt.");
		clock = 0.0;
		
		// s�mtliche Counter zur�cksetzen
		protocol.info(clckS() + "Statistikz�hler werden zur�ckgesetzt.");
		avgCasesInSystem = 0.0;
		caseCount = 0;
		finishedCases = 0;
		maxNumCasesInSystem = 0;
		//numCasesInSystem = 0;
		throughPut = 0.0;
		timeOfLastCaseNumChange = 0.0;
		timeOfLastEvent = 0.0;
		
		caseList.clear();
		
		nextEvent = null;
		
		// alle Server zur�cksetzen
		protocol.info(clckS() + "Server werden zur�ckgesetzt.");
		for (Server s : serverList.values()){
			s.reset();
		}
		
		// alle Ressourcen befreien
		protocol.info(clckS() + "Ressourcen werden befreit.");
		for (Resource r : resUtil.getUsedResources().values()){
			resUtil.freeResource(r);
		}
		
		protocol.info(clckS() + "Ereignisliste wird initialisiert.");
		initEventList();
		
		LoggerManager.info(Constants.QUANTANA_LOGGER, "Simulation run # " + run + " initialized.");
	}
	
	private void timing(){
		// nextEvent bestimmen
		protocol.info(clckS() + "N�chstes Ereignis wird bestimmt:");
		if (!(eventList.isEmpty()))
			nextEvent = eventList.remove();
		else
			nextEvent = null;
		
		protocol.info(clckS() + "Inhalt der Ereignisliste: " + printEventList());
		
		// Systemuhr setzen
		protocol.info(clckS() + "Simulationsuhr wird auf Ereigniszeitpunkt gesetzt.");
		if (nextEvent != null) clock = nextEvent.getTime();
	}
	
	private void generateReport(){
		protocol.info(clckS() + "Simulation beendet.");
		
		try {
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		((Handler)((protocol.getHandlers())[0])).close();
		
		LoggerManager.info(Constants.QUANTANA_LOGGER, "Protokoll wurde erstellt.");
//		SimOutputDialog sod = new SimOutputDialog(null, true, this);
//		sod.setVisible(true);
//		dlgSim.validate();
	}
	
	private void generateServerList(){
		Node[] nodes = process.getNodeArray();
		for (int i = 0; i < nodes.length; i++){
			String id = nodes[i].getId();
			String name = nodes[i].getName();
			double t = nodes[i].getTime();
			if (process.isTransition(id)){
				Server s = new Server(this, id, name, new ProbabilityDistribution(typeOfDistForServer, t, 1.0, seedGenerator.nextSeed()));
				s.setStatus(Server.STATUS_IDLE);
				String nid = name + " (" + id + ")";
				s.setRole(resAlloc.getRole(nid));
				s.setGroup(resAlloc.getGroup(nid));
				serverList.put(id, s);
			}
		}
		
		for (Server s : serverList.values()){
			String id = s.getId();
			int idx = process.getNodeIdx(id);
			Node n = (process.getNodeArray())[idx];
			for (Arc a : n.getSuccessor()){
				String id2 = a.getTarget().getId();
				int idx2 = process.getNodeIdx(id2);
				Node m = (process.getNodeArray())[idx2];
				for (Arc b : m.getSuccessor()){
					s.getSuccessor().add(new SuccServer(serverList.get(b.getTarget().getId()), a.getProbability() * b.getProbability()));
				}
			}
		}
		
		LoggerManager.info(Constants.QUANTANA_LOGGER, "List of servers for quantitative simulation generated.");
	}
	
	private void initEventList(){
		eventList.clear();
		
		caseGenerator = new CaseGenerator(new ProbabilityDistribution(typeOfDistForCases, caseParam1, caseParam2, seedGenerator.nextSeed()), this);
		
		BirthEvent be = new BirthEvent(this, clock);
		eventList.add(be);
		protocol.info(clckS() + "BIRTH_EVENT \"" + be.getName() + "\" wurde erzeugt.");
	}

	public HashMap<String, Server> getServerList() {
		return serverList;
	}

	public void setServerList(HashMap<String, Server> serverList) {
		this.serverList = serverList;
	}
	
	/*private void printServerList(){
		String text = "";
		for (Server s : serverList.values()){
			text += "\n" + s + " --> ";
			for (SuccServer t : s.getSuccessor()){
				text += t.getServer() + "(" + t.getProbability() + "), ";
			}
		}
		JOptionPane.showMessageDialog(null, text);
	}*/
	
	private boolean isCaseNumReached(){
		return finishedCases >= lambda;
	}
	
	private boolean isTimeRunOut(){
		return clock >= timeOfPeriod;
	}
	
	private boolean shouldStopNow(){
		switch (stopRule){
		case Simulator.STOP_CASE_DRIVEN:
			return isCaseNumReached();
		case Simulator.STOP_TIME_DRIVEN:
			return isTimeRunOut();
		case Simulator.STOP_BOTH:
			return isCaseNumReached() || isTimeRunOut();
		default:
			return true;
		}
	}
	
	public Server getStartServer(){
		Node start = process.getStartPlace();
		int succs = start.getSuccessor().size();
		int rnd = fstServChoice.nextInt(100);
		int idx = -1;
		for (int i = 0; i < succs; i++){
			if (i == 0){
				if (rnd < (fstServList[i][2] * 100)){
					idx = i;
				}
			} else {
				if ((rnd >= fstServList[i-1][2]) && (rnd < fstServList[i][2])){
					idx = i;
				}
			}
		}
		
		return serverList.get(start.getSuccessor().get(idx).getTarget().getId());
	}
	
	private void getFstServList(){
		Node start = process.getStartPlace();
		int succs = start.getSuccessor().size();
		fstServList = new int[succs][3];
		
		switch (succs){
		case 1:
			fstServList[0][0] = 1;
			fstServList[0][1] = 1;
			fstServList[0][2] = 1;
			break;
		default:
			for (int i = 0; i < succs; i++){
				fstServList[i][0] = i;
				fstServList[i][1] = (Double.valueOf(start.getSuccessor().get(i).getProbability() * 100)).intValue();
				if (i > 0){
					fstServList[i][2] = fstServList[i][1] + fstServList[i-1][2];
				} else {
					fstServList[i][2] = fstServList[i][1];
				}
			}
		}
	}

	public double getClock() {
		return clock;
	}

	public void setClock(double clock) {
		this.clock = clock;
	}
	
	public int getMaxNumCasesInSystem() {
		return maxNumCasesInSystem;
	}

	public void setMaxNumCasesInSystem(int maxNumCasesInSystem) {
		this.maxNumCasesInSystem = maxNumCasesInSystem;
	}

	public int getQueueDiscipline() {
		return queueDiscipline;
	}

	public void setQueueDiscipline(int queueDiscipline) {
		this.queueDiscipline = queueDiscipline;
	}

	public ResourceAllocation getResAlloc() {
		return resAlloc;
	}

	public void setResAlloc(ResourceAllocation resAlloc) {
		this.resAlloc = resAlloc;
	}

	public ResourceUtilization getResUtil() {
		return resUtil;
	}

	public void setResUtil(ResourceUtilization resUtil) {
		this.resUtil = resUtil;
	}

	public int getUseResAlloc() {
		return useResAlloc;
	}

	public void setUseResAlloc(int useResAlloc) {
		this.useResAlloc = useResAlloc;
	}

	public PriorityQueue<SimEvent> getEventList() {
		return eventList;
	}

	public void setEventList(PriorityQueue<SimEvent> eventList) {
		this.eventList = eventList;
	}

	public double getAvgCasesInSystem() {
		return avgCasesInSystem;
	}

	public void setAvgCasesInSystem(double avgCasesInSystem) {
		this.avgCasesInSystem = avgCasesInSystem;
	}

	public CaseGenerator getCaseGenerator() {
		return caseGenerator;
	}

	public void setCaseGenerator(CaseGenerator caseGenerator) {
		this.caseGenerator = caseGenerator;
	}

	public int getFinishedCases() {
		return finishedCases;
	}

	public void setFinishedCases(int finishedCases) {
		this.finishedCases = finishedCases;
	}

	public double getTimeOfLastEvent() {
		return timeOfLastEvent;
	}

	public void setTimeOfLastEvent(double timeOfLastEvent) {
		this.timeOfLastEvent = timeOfLastEvent;
	}

	public HashMap<Integer, Case> getCaseList() {
		return caseList;
	}

	public void setCaseList(HashMap<Integer, Case> caseList) {
		this.caseList = caseList;
	}

	public int getStopRule() {
		return stopRule;
	}

	public int getCaseCount() {
		return caseCount;
	}

	public void setCaseCount(int caseCount) {
		this.caseCount = caseCount;
	}

	public double getLambda() {
		return lambda;
	}

	public void setLambda(double lambda) {
		this.lambda = lambda;
	}

	public double getThroughPut() {
		return throughPut;
	}

	public void setThroughPut(double throughPut) {
		this.throughPut = throughPut;
	}
	
	public void updateCaseNumStats(double now, double lastEvent){
		int cc = caseList.size();
		avgCasesInSystem += cc * (now - lastEvent);
		
		protocol.info(clckS() + "Aktuelle Zahl von Cases im Prozess ist " + cc);
		protocol.info(clckS() + "Durchschnittliche Anzahl Cases im Prozess ist " + (avgCasesInSystem / clock));
	}

	public double getTimeOfLastCaseNumChange() {
		return timeOfLastCaseNumChange;
	}

	public void setTimeOfLastCaseNumChange(double timeOfLastCaseNumChange) {
		this.timeOfLastCaseNumChange = timeOfLastCaseNumChange;
	}

	public double getCaseBusy() {
		return caseBusy;
	}

	public void setCaseBusy(double caseBusy) {
		this.caseBusy = caseBusy;
	}

	public double getCaseWait() {
		return caseWait;
	}

	public void setCaseWait(double caseWait) {
		this.caseWait = caseWait;
	}
	
	private void initProtocol(){
		protocolPath = ConfigurationManager.getConfiguration().getLogdir();
		protocolName = protocolPath + "/simproto.xml";
		
		protocol.setLevel(Level.ALL);
		protocol.setUseParentHandlers(false);
		
		/*try {
			handler = new FileHandler(protocolName);
			handler.setLevel(Level.ALL);
			handler.setFormatter(new SimXMLFormatter(protocolPath));
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
		out = new ByteArrayOutputStream();
		Handler handler = new StreamHandler(new BufferedOutputStream(out), new SimXMLFormatter(protocolPath));
		
		protocol.addHandler(handler);
		
		LoggerManager.info(Constants.QUANTANA_LOGGER, "Protocol for quantitative simulation initialized.");
	}
	
	private String printResModUsed(){
		switch(useResAlloc){
		case RES_USED:
			return "ja";
		default:
			return "nein";
		}
	}
	
	private String printQueueDiscipline(){
		switch(queueDiscipline){
		case QD_LIFO:
			return "Last in - First out (LIFO)";
		default:
			return "First in - First out (FIFO)";
		}
	}

	public String getProtocolName() {
		return protocolName;
	}
	
	public Logger getProtocol(){
		return protocol;
	}
	
	public byte[] getProtocolContent(){
		return out.toByteArray();
	}

	public String clckS() {
		String c = String.format("%,.2f", clock);
		return "(" + c + "): ";
	}
	
	public String printEventList(){
		String s = "[";
		Object[] list = eventList.toArray();
		int l = list.length;
		
		if (l > 1){
			for (int i = 0; i < l - 1; i++){
				s += ((SimEvent)list[i]).getName() + ",";
			}
		}
		
		if (l > 0) s += ((SimEvent)list[l - 1]).getName();
		
		return s + "]";
	}

	public String getProtocolPath() {
		return protocolPath;
	}

	public void setProtocolPath(String protocolPath) {
		this.protocolPath = protocolPath;
	}
}
