package com.leezp.test;

import com.leezp.intercepting.AuthenticationFilter;
import com.leezp.intercepting.Client;
import com.leezp.intercepting.DebugFilter;
import com.leezp.intercepting.FilterManager;
import com.leezp.intercepting.Target;

public class InterceptingFilterDemo {
	public static void main(String[] args) {
		FilterManager filterManager = new FilterManager(new Target());
		filterManager.setFilter(new AuthenticationFilter());
		filterManager.setFilter(new DebugFilter());
		
		Client client = new Client();
		client.setFilterManager(filterManager);
		client.sendRequest("HOME");
	}
}
