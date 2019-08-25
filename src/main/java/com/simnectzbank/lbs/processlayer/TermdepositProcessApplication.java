package com.simnectzbank.lbs.processlayer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.csi.sbs.common.business.log.InitLog;

@SpringBootApplication
@EnableEurekaClient
public class TermdepositProcessApplication {

	@Bean
    @LoadBalanced
    public RestTemplate rest() {
        return new RestTemplate();
    }
	
	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(TermdepositProcessApplication.class, args);
		
		//init log configuration
		InitLog.loadLogConfig(context,"account-open-process");
	}

}
