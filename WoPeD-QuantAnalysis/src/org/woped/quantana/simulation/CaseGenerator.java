package org.woped.quantana.simulation;

@SuppressWarnings("unused")
public class CaseGenerator {
	private ProbabilityDistribution distribution;
	private int caseCount = 0;
	private double lastArrivalTime = 0.0;
	private Simulator sim = null;
	
	public CaseGenerator(ProbabilityDistribution dist, Simulator sim){
		this.distribution = dist;
		this.sim = sim;
	}

	public int getCaseCount() {
		return caseCount;
	}

	public void setCaseCount(int caseCount) {
		this.caseCount = caseCount;
	}

	public Case generateNextCase(){
		Case next = new Case(++caseCount);

		lastArrivalTime += distribution.getNextRandomValue();
		next.setSysArrivalTime(lastArrivalTime);
		
		return next;
	}

	public double getLastArrivalTime() {
		return lastArrivalTime;
	}

	public void setLastArrivalTime(double lastArrivalTime) {
		this.lastArrivalTime = lastArrivalTime;
	}
}