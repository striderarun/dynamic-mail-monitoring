package com.arun.daemon.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.arun.daemon.domain.Job;
import com.arun.daemon.repository.DaemonRepository;

@Component
public class DaemonService {

	@Autowired
	private DaemonRepository daemonRepository;
	
	@Scheduled(fixedRate = 10000)
//	@PostConstruct
	public void job() {
		System.out.println("Starting job");
		List<Job> jobList = daemonRepository.findByMonitorAndStatus("Y", null);
		if(!CollectionUtils.isEmpty(jobList)) {
			for(Job job: jobList) {
				String command = String.format("sh /Users/arun_subramonian/Desktop/mail.sh --EMAIL=imaps://%s:%s@%s:%s/inbox", job.getUserName(),job.getPassword(),job.getMailServer(),job.getPort());
				System.out.println("Starting daemon");
				String pid = triggerDaemon(command);
				job.setStatus("RUNNING");
				job.setPid(pid);
				daemonRepository.save(job);
			}
		}
		
	}
	
	private String getPid(InputStream ins) throws Exception {
	    String out = "";
		String line = null;
	    BufferedReader in = new BufferedReader(new InputStreamReader(ins));
	    while ((line = in.readLine()) != null) {
	    	out += line;
	    }
	    return out;
	}
	
	private String triggerDaemon(String command) {
		String output = null;
		try {
			Process pro = Runtime.getRuntime().exec(command);		    
		    if (null != pro.getInputStream()) {
		    	output = getPid(pro.getInputStream());
		    }
		    pro.waitFor();
		    System.out.println("Process ID" + output);
		    if(output.contains(" ")) {
		    	output = output.split(" ")[0];
		    }
		    System.out.println("Final out" + output);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}
}
