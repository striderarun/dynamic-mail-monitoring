package com.arun.daemon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arun.daemon.domain.Job;


public interface DaemonRepository extends JpaRepository<Job, Integer> {
	
	List<Job> findByMonitorAndStatus(String monitor,String status);
	
}
