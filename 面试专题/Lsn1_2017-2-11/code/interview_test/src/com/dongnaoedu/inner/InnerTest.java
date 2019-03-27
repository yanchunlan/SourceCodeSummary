package com.dongnaoedu.inner;

public class InnerTest {

	class InnerClassA extends ParentA{
		public int getAge(){
			return super.age;
		}
	}
	
	class InnerClassB extends ParentB{
		public String getName(){
			return super.name;
		}
	}
	
	
	public void doSomething(){
		new InnerClassA().getAge();
		new InnerClassB().getName();
	}
	
	
}
