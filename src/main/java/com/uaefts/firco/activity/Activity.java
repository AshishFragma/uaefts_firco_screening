package com.uaefts.firco.activity;

import com.uaefts.firco.config.ApplicationContext;

public interface Activity {
	Outcome execute(ApplicationContext context) throws Exception;
	String getName();
}
