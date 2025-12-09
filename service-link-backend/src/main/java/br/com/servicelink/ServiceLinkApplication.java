package br.com.servicelink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "br.com.servicelink",      // Scaneia esse projeto (ServiceLink)
        "br.com.serviceframework"  // Scaneia o Framework
})
public class ServiceLinkApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceLinkApplication.class, args);
    }
}