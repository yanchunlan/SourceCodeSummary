package com.dongnaoedu.oop;

public class Test {

	public static void main(String[] args) {
		PlaneBiz biz = new PlaneBiz();
		biz.doSomething(new Jet());
		
		biz.doSomething(new UFO());
	}

}
