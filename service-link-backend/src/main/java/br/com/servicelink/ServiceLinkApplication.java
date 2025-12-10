package br.com.servicelink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
        "br.com.servicelink",      // Scaneia esse projeto (ServiceLink)
        "br.com.serviceframework"  // Scaneia o Framework
})
@EntityScan({
        "br.com.servicelink.domain.entity",
        "br.com.serviceframework.domain.entity"
})
@EnableJpaRepositories({
        "br.com.servicelink.repository",
        "br.com.serviceframework.repository"
})
public class ServiceLinkApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceLinkApplication.class, args);
    }
}