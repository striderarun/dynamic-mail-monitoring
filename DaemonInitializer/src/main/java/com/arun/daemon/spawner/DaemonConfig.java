package com.arun.daemon.spawner;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.arun.daemon.service.DaemonService;

@SpringBootApplication
@ComponentScan(basePackages = "com")
@EnableScheduling
@EntityScan(basePackages = "com.arun.daemon.domain")
@EnableJpaRepositories(basePackages = "com.arun.daemon.repository")
@EnableTransactionManagement
public class DaemonConfig {
	
	public static void main(String[] args) {
		SpringApplication.run(DaemonConfig.class, args);
	}
}
