package com.sohnar.blazedsproxy;

import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieHttpSessionStrategy;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.HttpSessionStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import flex.messaging.AbstractFlexSessionProvider;
import flex.messaging.FlexSession;
import flex.messaging.HttpFlexSession;
import flex.messaging.MessageClient;
import flex.messaging.client.FlexClient;
import flex.messaging.util.TimeoutAbstractObject;
import flex.messaging.util.TimeoutManager;

@EnableRedisHttpSession
public class SessionConfig implements BeanClassLoaderAware {

	private ClassLoader loader;

	@Bean
	HttpSessionStrategy httpSessionStrategy() {
		CookieHttpSessionStrategy cookieStrategy = new CookieHttpSessionStrategy();
		cookieStrategy.setCookieSerializer(cookieSerializer());
		return cookieStrategy;
	}
	
	CookieSerializer cookieSerializer() {
		DefaultCookieSerializer serializer = new DefaultCookieSerializer();
		serializer.setCookieName("JSESSIONID");
		serializer.setCookiePath("/");
		serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$");
		return serializer;
	}
	
	@Bean
	public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
		return new GenericJackson2JsonRedisSerializer(objectMapper());
	}
	
	ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModules(SecurityJackson2Modules.getModules(this.loader));
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.addMixIn(TimeoutAbstractObject.class, TimeoutAbstractObjectMixin.class);
		mapper.addMixIn(FlexSession.class, FlexSessionMixin.class);
		return mapper;
	}

	@Override
	public void setBeanClassLoader(ClassLoader arg0) {
		this.loader = arg0;
	}
	
	abstract class FlexSessionMixin {
		@JsonIgnore
		abstract AbstractFlexSessionProvider getFlexSessionProvider();
		@JsonIgnore
		abstract List<FlexClient> getFlexClients();
		@JsonIgnore
	    abstract List<MessageClient> getMessageClients();
		@JsonIgnore
		abstract String getId();
	}
	
	/**
	 * We need to exclude certain unserializable properties from this session object.
	 * 
	 * @author simonstewart
	 *
	 */
	abstract class TimeoutAbstractObjectMixin {
		@JsonIgnore
	    abstract TimeoutManager getTimeoutManager();
		@JsonIgnore
	    abstract void setTimeoutManager(TimeoutManager timeoutManager);
		@JsonIgnore
	    abstract Runnable getTimeoutTask();
		@JsonIgnore
	    abstract void setTimeoutTask(Runnable timeoutTask);
		@JsonIgnore
	    abstract Future getTimeoutFuture();
		@JsonIgnore
	    abstract void setTimeoutFuture(Future timeoutFuture);

	}
	

}
