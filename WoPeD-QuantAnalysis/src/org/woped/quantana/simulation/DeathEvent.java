package org.woped.quantana.simulation;

@SuppressWarnings("unused")
public class DeathEvent extends SimEvent {
	
	private int type = SimEvent.DEATH_EVENT;
	
	private Case c;
	
	public DeathEvent(Simulator sim, double time, Case c){
		super(sim, time);
		this.c = c;
	}
	
	public void invoke(){
		Simulator sim = getSim();
		double t = getTime();
		
		c.setSysDepartureTime(t);
		sim.setFinishedCases(sim.getFinishedCases() + 1);
		sim.setThroughPut(sim.getThroughPut() + c.getSysDepartureTime() - c.getSysArrivalTime());
		sim.setCaseBusy(sim.getCaseBusy() + c.getTimeService());
		sim.setCaseWait(sim.getCaseWait() + c.getTimeWait());
		sim.updateCaseNumStats(getTime(), sim.getTimeOfLastCaseNumChange());
		
		sim.getCaseList().remove(c.getId());
		
		sim.setTimeOfLastCaseNumChange(getTime());
	}
}