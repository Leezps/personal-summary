package com.leezp.observe;

public abstract class Observer {
	protected Subject subject;
	public abstract void update();
}
