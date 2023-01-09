package com.bestbranch.geulboxapi.common.infrastructure;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class DefaultQuerydslRepositorySupport extends QuerydslRepositorySupport {

	public DefaultQuerydslRepositorySupport(Class<?> domainClass) {
		super(domainClass);
	}

	@PersistenceContext
	@Override
	public void setEntityManager(EntityManager entityManager) {
		super.setEntityManager(entityManager);
	}
}
