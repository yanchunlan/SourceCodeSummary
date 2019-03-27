package com.dongnaoedu.oop;

public class UFO implements Plane {
	
	
	
	@Override
	public void land() {
		System.out.println("UFO降落...");
	}

	@Override
	public void fly() {
		System.out.println("UFO起飞...");
	}

}
