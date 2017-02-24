package com.sohnar.blazedsproxy;

import java.util.Properties;

import org.apache.flex.blazeds.spring.BlazeDsAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.flex.security3.FlexAuthenticationEntryPoint;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieHttpSessionStrategy;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.HttpSessionStrategy;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

@SpringBootConfiguration
@EnableAutoConfiguration(exclude={BlazeDsAutoConfiguration.class, SecurityAutoConfiguration.class})
@ComponentScan
@ImportResource("classpath:spring-blazeds-config.xml")
public class BlazeDsProxySlaveApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlazeDsProxySlaveApplication.class, args);
	}
	
	@Bean
	public SimpleUrlHandlerMapping sampleServletMapping() {
		SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
		mapping.setOrder(Integer.MAX_VALUE - 2);

		Properties urlProperties = new Properties();
		urlProperties.put("/messagebroker/*", "_messageBroker");

		mapping.setMappings(urlProperties);
		return mapping;
	}
	
	@Configuration
	@EnableWebSecurity
	class SecurityConfig extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.antMatcher("/messagebroker/**").csrf().disable()
				.anonymous().disable()
				.exceptionHandling().authenticationEntryPoint(new FlexAuthenticationEntryPoint());
		}
		@Autowired
		public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

			auth.inMemoryAuthentication()
				.withUser("user").password("password").roles("USER");
		}
		
	   @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	   @Override
	   public AuthenticationManager authenticationManagerBean() throws Exception {
	       return super.authenticationManagerBean();
	   }
	}
	
}
