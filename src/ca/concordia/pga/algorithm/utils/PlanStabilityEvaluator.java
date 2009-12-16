package ca.concordia.pga.algorithm.utils;

import java.util.HashSet;
import java.util.Set;

import ca.concordia.pga.models.PlanningGraph;
import ca.concordia.pga.models.Service;

/**
 * 
 * @author Ludeng Zhao(Eric)
 *
 */
public class PlanStabilityEvaluator {

	public static int evaluate(PlanningGraph oldpg, PlanningGraph pg){
		int distance = 0;
		
		Set<Service> oldServices = getServicesInPG(oldpg);
		Set<Service> currentServices = getServicesInPG(pg);
		Set<Service> temp = new HashSet<Service>();
		
		temp.addAll(oldServices);
		temp.removeAll(currentServices);
		distance += temp.size();
		
		temp.clear();
		temp.addAll(currentServices);
		temp.removeAll(oldServices);
		distance += temp.size();
		
		return distance;
		
	}
	
	private static Set<Service> getServicesInPG(PlanningGraph pg){
		Set<Service> services = new HashSet<Service>();
		
		for(Set<Service> aLevel : pg.getALevels()){
			services.addAll(aLevel);
		}
		
		return services;
		
	}
}
