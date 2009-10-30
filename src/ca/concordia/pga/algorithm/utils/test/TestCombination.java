package ca.concordia.pga.algorithm.utils.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ca.concordia.pga.models.Service;

public class TestCombination {
	
//    public static List<List<String>> findCombinations(List<String> elements)
//    {
//        List<List<String>> result = new ArrayList<List<String>>();
//        
//        for (int i = 0; i <= elements.size(); i++)
//            result.addAll(findCombinations(elements, i));
//
//        return result;
//    }
//    
    public static <T extends Comparable<? super T>> List<List<T>> findCombinations(Collection<T> elements)
    {
        List<List<T>> result = new ArrayList<List<T>>();
        
        for (int i = 0; i <= elements.size(); i++)
            result.addAll(findCombinations(elements, i));

        return result;
    }
    
    
    
    public static <T extends Comparable<? super T>> List<List<T>> findCombinations(Collection<T> elements, int n)
    {
        List<List<T>> result = new ArrayList<List<T>>();
        
        if (n == 0)
        {
            result.add(new ArrayList<T>());
            
            return result;
        }
        
        List<List<T>> combinations = findCombinations(elements, n - 1);
        for (List<T> combination: combinations)
        {
            for (T element: elements)
            {
                if (combination.contains(element))
                {
                    continue;
                }
                
                List<T> list = new ArrayList<T>();

                list.addAll(combination);
                
                if (list.contains(element))
                    continue;
                
                list.add(element);
                Collections.sort(list);
                
                if (result.contains(list))
                    continue;
                
                result.add(list);
            }
        }
        
        return result;
    }
    @SuppressWarnings("unchecked")
	public static void main(String[] args){
    	
    	Set<Service> set = new HashSet<Service>();
    	set.add(new Service("serv731492911"));
    	set.add(new Service("serv1493608930"));
    	set.add(new Service("serv800925144"));
    	
    	List<Set<Service>> sets =  findCombinations(set);
    	sets.remove(new ArrayList<Service>());
    	System.out.println("count: " + sets.size());

    	
    }
    
}
