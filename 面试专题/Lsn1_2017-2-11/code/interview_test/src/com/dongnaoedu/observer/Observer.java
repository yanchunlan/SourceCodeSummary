package com.dongnaoedu.observer;

/**
 * 观察者接口
 * @author Jason
 * QQ: 1476949583
 * @date 2015年12月26日
 * @version 1.0
 */
public interface Observer {

	/**
	 * 更新（当目标对象的状态发生改变时，这个方法会被调用）
	 * @param subject 目标对象，方便获取相应目标对象的状态
	 */
	void update(Subject subject);
	
}
