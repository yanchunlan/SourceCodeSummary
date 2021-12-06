package com.dongnaoedu.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * 目标对象
 * 持有观察者（订阅者）列表，并提供维护
 * @author Jason
 * QQ: 1476949583
 * @date 2015年12月26日
 * @version 1.0
 */
public class Subject {

	/**
	 * 观察者（订阅者）列表
	 */
	private List<Observer> observers = new ArrayList<Observer>();
	
	/**
	 * 注册观察者
	 * @param observer
	 */
	public void attach(Observer observer){
		observers.add(observer);
	}
	
	/**
	 * 取消注册
	 * @param observer
	 */
	public void detach(Observer observer){
		observers.remove(observer);
	}
	
	/**
	 * 通知所有注册的观察者对象
	 */
	protected void notifyObservers(){
		for (Observer observer : observers) {
			observer.update(this);
		}
	}
}
