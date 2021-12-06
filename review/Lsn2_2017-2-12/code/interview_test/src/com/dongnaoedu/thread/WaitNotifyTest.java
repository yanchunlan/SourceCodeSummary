package com.dongnaoedu.thread;

public class WaitNotifyTest {
	
	Object lock = new Object();
	
	class MyThread extends Thread{
		@Override
		public void run() {
			while(true){
				synchronized (lock) {
					try {
						System.out.println("waiting for notify");
						lock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					System.out.println("---notified----");
				}
			}
		}
		
		
	}

	public static void main(String[] args) {
		WaitNotifyTest outClass = new WaitNotifyTest();
		outClass.new MyThread().start();
		
		new Thread(){
			public void run() {
				while(true){
					//每隔2秒，通知打印线程打印
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					synchronized (outClass.lock) {
						outClass.lock.notify();
					}
				}
			}
		}.start();
		
	}

}
