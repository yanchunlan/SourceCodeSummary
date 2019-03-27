package com.dongnaoedu.observer;

/**
 * 具体的目标
 * 
 * @author Jason QQ: 1476949583
 * @date 2015年12月26日
 * @version 1.0
 */
public class ConcreteSubject extends Subject {

	/**
	 * 目标对象的状态
	 */
	private String subjectState;

	public String getSubjectState() {
		return subjectState;
	}

	public void setSubjectState(String subjectState) {
		this.subjectState = subjectState;
		//目标对象的状态发生改变，通知各个观察者
		notifyObservers();
	}

}
