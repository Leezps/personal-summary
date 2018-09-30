package com.leezp.criteria.impl;

import java.util.ArrayList;
import java.util.List;

import com.leezp.criteria.Criteria;
import com.leezp.criteria.entity.Person;

public class CriteriaSingle implements Criteria {

	@Override
	public List<Person> meetCriteria(List<Person> persons) {
		List<Person> singlePersons = new ArrayList<Person>();
		for(Person person : persons) {
			if (person.getMaritalStatus().equalsIgnoreCase("SINGLE")) {
				singlePersons.add(person);
			}
		}
		return singlePersons;
	}
}
