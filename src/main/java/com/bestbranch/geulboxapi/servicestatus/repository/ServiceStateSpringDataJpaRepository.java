package com.bestbranch.geulboxapi.servicestatus.repository;

import com.bestbranch.geulboxapi.servicestatus.domain.ServiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public interface ServiceStateSpringDataJpaRepository extends JpaRepository<ServiceStatus, Long> {

}
