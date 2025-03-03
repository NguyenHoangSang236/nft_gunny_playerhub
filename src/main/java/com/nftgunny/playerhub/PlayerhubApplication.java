package com.nftgunny.playerhub;

import com.nftgunny.core.repository.SystemConfigRepository;
import com.nftgunny.core.utils.InitiateConfigUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;

@SpringBootApplication(
		exclude = {SecurityAutoConfiguration.class},
		scanBasePackages = {
				"com.nftgunny.playerhub.usecases",
				"com.nftgunny.playerhub.entities",
				"com.nftgunny.playerhub.infrastructure",
				"com.nftgunny.playerhub.config",
				"com.nftgunny.playerhub",
				"com.nftgunny.core",
		}
)
@ComponentScan(basePackages = {
		"com.nftgunny.playerhub.usecases",
		"com.nftgunny.playerhub.entities",
		"com.nftgunny.playerhub.infrastructure",
		"com.nftgunny.playerhub.config",
		"com.nftgunny.playerhub",
		"com.nftgunny.core",
})
public class PlayerhubApplication {
	@Autowired
	InitiateConfigUtils initiateConfigUtils;

	public static void main(String[] args) {
		SpringApplication.run(PlayerhubApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void initSslConfig() {
		initiateConfigUtils.initSslConfig();
		initiateConfigUtils.getWeightSystemConfig();
	}
}
