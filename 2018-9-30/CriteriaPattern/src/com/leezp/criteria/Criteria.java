package com.leezp.criteria;

import java.util.List;

import com.leezp.criteria.entity.Person;

public interface Criteria {
	public List<Person> meetCriteria(List<Person> persons);
}
