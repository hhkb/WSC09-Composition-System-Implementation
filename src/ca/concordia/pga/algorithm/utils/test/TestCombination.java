package ca.concordia.pga.algorithm.utils.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ca.concordia.pga.algorithm.utils.CombinationHelper;
import ca.concordia.pga.models.Service;

public class TestCombination {
	
    @SuppressWarnings("unchecked")
	public static void main(String[] args){
    	
    	Set<Service> set = new HashSet<Service>();
    	set.add(new Service("serv731492911"));
    	set.add(new Service("serv1493608930"));
    	set.add(new Service("serv800925144"));
    	
    	List<List<Service>> sets =  CombinationHelper.findCombinations(set);
    	sets.remove(new ArrayList<Service>());
    	System.out.println("count: " + sets.size());

    	
    }
    
}
