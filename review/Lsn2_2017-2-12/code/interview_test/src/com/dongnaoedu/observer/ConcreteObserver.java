package com.dongnaoedu.observer;

/**
 * 具体的观察者
 * @author Jason
 * QQ: 1476949583
 * @date 2015年12月26日
 * @version 1.0
 */
public class ConcreteObserver implements Observer{

	@Override
	public void update(Subject subject) {
		System.out.println("目标对象状态发生改变,"+((ConcreteSubject)subject).getSubjectState());
	}

}
