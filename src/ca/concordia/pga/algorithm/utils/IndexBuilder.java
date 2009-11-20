package ca.concordia.pga.algorithm.utils;

import java.util.Map;

import ca.concordia.pga.models.Concept;
import ca.concordia.pga.models.Param;
import ca.concordia.pga.models.Service;

public class IndexBuilder {

	/**
	 * Build inverted indexing table: concept -> all services who accept the
	 * concept
	 * 
	 * @param conceptMap
	 * @param serviceMap
	 */
	public static void buildInvertedIndex(Map<String, Concept> conceptMap,
			Map<String, Service> serviceMap) {
		for (String serviceKey : serviceMap.keySet()) {
			Service service = serviceMap.get(serviceKey);
			for (Param param : service.getInputParamSet()) {
				Concept concept = conceptMap.get(param.getThing().getType());
				for (Concept childrenConcept : concept
						.getChildrenConceptsIndex()) {
					childrenConcept.addServiceToIndex(service);
				}
			}
		}
	}

}
