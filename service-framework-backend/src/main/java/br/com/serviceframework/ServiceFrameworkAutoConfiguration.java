package br.com.serviceframework;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "br.com.serviceframework.domain.entity")
@EnableJpaRepositories(basePackages = "br.com.serviceframework.repository")
@ComponentScan(basePackages = "br.com.serviceframework")
public class ServiceFrameworkAutoConfiguration {
}
