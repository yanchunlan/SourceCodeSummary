package com.dongnaoedu.thread;

public class DeadLockTest {

	public static void main(String[] args) {
		MyTask task = new MyTask();
		
		task.setFlag(1);
		Thread t1 = new Thread(task);
		t1.start();
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		MyTask task2 = new MyTask();
		//改变条件
		task2.setFlag(2);
		Thread t2 = new Thread(task2);
		t2.start();
		
	}

}
