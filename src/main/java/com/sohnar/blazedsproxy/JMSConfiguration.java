package com.sohnar.blazedsproxy;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class JMSConfiguration {
	
//    <!-- A connection to ActiveMQ Note: Make sure the brokerURL is matching the one used in graniteds-context.xml -->
//    <bean id="connectionFactory" class="org.apache.activemq.ActiveMQConnectionFactory">
//        <property name="brokerURL" value="tcp://localhost:61616"/>
//    </bean>
//
//    <!-- A cached connection to wrap the ActiveMQ connection -->
//	<bean id="jmsFactory" class="org.apache.activemq.pool.PooledConnectionFactory"
//		destroy-method="stop">
//		<property name="connectionFactory" ref="connectionFactory"/>	
//	</bean>
//	
//    <!-- A MQTopic Note: Make sure the topic is matching the one used in graniteds-context.xml -->
//    <bean id="messagingTopic" class="org.apache.activemq.command.ActiveMQTopic">
//        <property name="physicalName" value="messagingTopic"/>
//    </bean>
//
//    <!-- A JmsTemplate instance that uses the cached connection and destination -->
//    <bean id="jmsTemplate" class="org.springframework.jms.core.JmsTemplate">
//        <property name="connectionFactory" ref="connectionFactory"/>
//        <property name="defaultDestination" ref="messagingTopic"/>
//        <property name="pubSubDomain" value="true"/>
//    </bean>
    
	@Bean
	public ConnectionFactory connectionFactory() {
		return new ActiveMQConnectionFactory("tcp://localhost:61616");
	}
	
	@Bean
	public PooledConnectionFactory pooledFactory(ConnectionFactory connectionFactory) {
		PooledConnectionFactory result = new PooledConnectionFactory();
		result.setConnectionFactory(connectionFactory);
		return result ;
	}
	
	@Bean
	public JmsTemplate jmsTemplate(PooledConnectionFactory pooledConnectionFactory) {
		JmsTemplate jmsTemplate = new JmsTemplate(pooledConnectionFactory);
		jmsTemplate.setPubSubDomain(true);
		return jmsTemplate;
	}
}
