package edu.casetools.mreasoner.core.engine;

import java.util.Vector;

import edu.casetools.mreasoner.core.elements.MRules;
import edu.casetools.mreasoner.core.elements.rules.NextTimeRule;
import edu.casetools.mreasoner.core.elements.rules.SameTimeRule;
import edu.casetools.mreasoner.core.elements.states.State;

public class RuleStratificator {
	
	Vector<SameTimeRule> sameTimeRules;
	Vector<NextTimeRule> nextTimeRules;
	Vector<State> independentStates;
	boolean lastRuleLevelEmpty;
	
	public RuleStratificator(MRules systemRules){
		this.sameTimeRules = systemRules.getSameTimeRules();
		this.nextTimeRules = systemRules.getNextTimeRules();
		lastRuleLevelEmpty = false;
	}
	
	public Vector<SameTimeRule> stratify(Vector<State> independentStates){
		Vector<Vector<SameTimeRule>> ruleLevels = new Vector<Vector<SameTimeRule>>();
		Vector<SameTimeRule> ruleLevel = new Vector<SameTimeRule>();
		this.independentStates = independentStates;
		
		addNextTimeRuleConsequencesAsIndependentStates();
		
		while(sameTimeRules.size() > 0){
			ruleLevel = getNextRuleLevel();
			if(!lastRuleLevelEmpty){
				ruleLevels.add(ruleLevel);
				changeToNextLevel(ruleLevel);
				printRuleLevel( ruleLevel, ruleLevels.size() );
			}
			else{
				lastRuleLevelEmpty = false;
			}

		}
		
		reorderRules(ruleLevels);
	
		return sameTimeRules;
	}
	
	private void changeToNextLevel(Vector<SameTimeRule> ruleLevel){
		for(int i=0;i<ruleLevel.size();i++){
			addIndependentState(ruleLevel.get(i).getConsequence());
		}
	}
	
	private void printRuleLevel(Vector<SameTimeRule> vector, int level) {
		System.out.println("______________________________________");
		System.out.println("Level : "+level+" has "+vector.size()+" rule/s");
		for(int i=0;i<vector.size();i++){
			vector.get(i).printRule();
		}
		
	}

	/*private void takeNextTimeRuleAntecedentsAsIndependentStates(){
		for(int i =0;i<nextTimeRules.size();i++){
			addIndependentState(nextTimeRules.get(i).getConsequence());
		}
	}
	
	private boolean checkIfContinue(){
		boolean doContinue = true;
		
		return doContinue;
	}*/
	
	private void addNextTimeRuleConsequencesAsIndependentStates(){
		for(int i=0;i<nextTimeRules.size();i++){
			independentStates.add( nextTimeRules.get(i).getConsequence());
		}
	}
	
	private Vector<SameTimeRule> getNextRuleLevel(){
		Vector<SameTimeRule> ruleLevel = new Vector<SameTimeRule>();
		
		for(int i=0;i<sameTimeRules.size();i++){
			
			if(isThisLevelRule(sameTimeRules.get(i))){
				ruleLevel.add(sameTimeRules.get(i));
				sameTimeRules.removeElementAt(i);
				i--;
			}
		}
		if(ruleLevel.isEmpty()) lastRuleLevelEmpty = true;
		else lastRuleLevelEmpty = false;
		
		return ruleLevel;
	}
	
	private boolean isThisLevelRule(SameTimeRule rule){
		
		boolean isThisLevelRule = true;
		boolean antecedentIsIndependent = true;
		State antecedent;
		State independentState;
		for(int i=0;i< rule.getAntecedents().size();i++){
			antecedent 		 = rule.getAntecedents().get(i);
			for( int j=0; j < independentStates.size(); j++ ){

				independentState = independentStates.get(j);
				if( isIndependentAntecedent(antecedent, independentState)){
					antecedentIsIndependent = true;
					j = independentStates.size();
				}else antecedentIsIndependent = false;
			}
			isThisLevelRule = isThisLevelRule && antecedentIsIndependent;
			if(!isThisLevelRule) i = rule.getAntecedents().size();
		}

		return isThisLevelRule;
		
	}
	
	private void addIndependentState(State s){
		
		boolean isOnTheList = false;
		for(int i=0;i<independentStates.size();i++){
			if(independentStates.get(i).getName().equalsIgnoreCase(s.getName())){
				if(independentStates.get(i).getStatus() == s.getStatus()){
					isOnTheList = true;
					i = independentStates.size();
				}
			}
		}
		if(!isOnTheList) independentStates.add(s);
	}
	
	private boolean isIndependentAntecedent(State antecedent, State independentState){
		boolean isIndependentAntecedent = false;
		if( antecedent.getName().equalsIgnoreCase(independentState.getName())){
			if(antecedent.getStatus() == (independentState.getStatus())){
				isIndependentAntecedent = true;
			}
		}
		return isIndependentAntecedent;
	}
	
	private void reorderRules(Vector<Vector<SameTimeRule>> ruleLevels){
		for(int i=0;i<ruleLevels.size();i++){
			for(int j=0;j<ruleLevels.get(i).size();j++){
				sameTimeRules.add(ruleLevels.get(i).get(j));
			}
		}
	}
	

}
