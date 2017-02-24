package com.sohnar.blazedsproxy.services;

import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.stereotype.Service;

@Service
@RemotingDestination
public class TestService {

	public String echo(String value) {
		return "SLAVE Echo Response: "+value;
	}
	
}
