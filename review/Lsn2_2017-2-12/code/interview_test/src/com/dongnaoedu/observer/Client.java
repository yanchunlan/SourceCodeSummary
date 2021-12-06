package com.dongnaoedu.observer;

public class Client {

	public static void main(String[] args) {
		//目标
		ConcreteSubject subject = new ConcreteSubject();
		
		//订阅者
		Observer observer1 = new ConcreteObserver();
		Observer observer2 = new ConcreteObserver();
		
		//注册
		subject.attach(observer1);
		subject.attach(observer2);
		
		subject.setSubjectState("仍然是单身");
		
		subject.detach(observer2);
		
		System.out.println("------------");
		
		subject.setSubjectState("终于找到我的女神啦！哈哈哈");
	}

}
