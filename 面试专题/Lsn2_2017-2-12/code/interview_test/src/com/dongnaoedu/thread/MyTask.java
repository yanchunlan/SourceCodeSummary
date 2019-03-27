package com.dongnaoedu.thread;

public class MyTask implements Runnable{
	int flag;
	Object obj1 = new String("obj1"); //资源
	Object obj2 = new String("obj2"); //资源

	@Override
	public void run() {
		if(flag == 1){
			synchronized (obj1) {
				System.out.println("locking "+obj1); //占用obj1
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				synchronized (obj2) {
					System.out.println("使用顺序obj1->obj2");
				}
			}
		}
		
		else if(flag == 2){
			synchronized (obj2) {
				System.out.println("locking "+obj2); //占用obj2
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				synchronized (obj1) {
					System.out.println("使用顺序obj2->obj1");
				}
			}
		}
	}
	
	public void setFlag(int flag) {
		this.flag = flag;
	}
}