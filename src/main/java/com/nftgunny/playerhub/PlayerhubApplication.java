package com.nftgunny.playerhub;

import com.nftgunny.core.utils.InitiateConfigUtils;
import io.github.cdimascio.dotenv.Dotenv;
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
        Dotenv dotenv = Dotenv.configure().directory("./").load();
        System.setProperty("MONGODB_URI", dotenv.get("MONGODB_URI"));
        System.setProperty("MONGODB_DATABASE", dotenv.get("MONGODB_DATABASE"));
        String mongoUri = System.getenv("MONGODB_URI");
        System.out.println("MONGODB_URI: " + mongoUri);
        SpringApplication.run(PlayerhubApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initSslConfig() {
        initiateConfigUtils.initSslConfig();
        initiateConfigUtils.getWeightSystemConfig();
    }
}
