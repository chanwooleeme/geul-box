package com.bestbranch.geulboxapi.common.config;



import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.context.annotation.Configuration;



@Configuration
public class QuerydslConfig {
	@PersistenceContext
	private EntityManager entityManager;


}
