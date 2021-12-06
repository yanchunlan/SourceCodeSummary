package com.ricky.imgmulltipointscale;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.widget.ImageView;

public class MyImageView extends ImageView {

	private float startX;
	private float startY;
	private Matrix matrix;
	private PointF pointMid;
	private float firstDis;
	private float secondDis;

	public MyImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		matrix = new Matrix();
		setImageMatrix(matrix);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		System.out.println("~~~~~~~onTouchEvent");
		int pointerCount = event.getPointerCount();
		if(pointerCount==1){
			//��һ����ָ����
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				startX = event.getX();
				startY = event.getY();
				break;
			case MotionEvent.ACTION_MOVE:
				//�ƶ�ͼƬ
				float x = event.getX();
				float y = event.getY();
				//dx:���� delta
				matrix.postTranslate(x-startX, y-startY);
				//����ԭ��������
				startX = x;
				startY = y;
				setImageMatrix(matrix);//�����µľ���
				break;
			case MotionEvent.ACTION_UP:
				break;

			default:
				break;
			}
		}else if(pointerCount==2){
			//����
			switch (event.getAction()&MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_POINTER_DOWN:
				//�ڶ�����ָ����ȥ
				//���ŵ����ĵ�
				pointMid = getPointMidByEvent(event);
				//��ǰ����ľ���
				firstDis = getDisByXY(event);
				break;
			case MotionEvent.ACTION_MOVE:
				//������ָ�ƶ�--����
				//�ĸ�������x��������ֵ��y��������ֵ���������ĵ�����x,y
				//����ֵ������ľ����й�
				//����������ľ���
				secondDis = getDisByXY(event);
				float scale = secondDis/firstDis;//���ű���
				matrix.postScale(scale, scale, pointMid.x, pointMid.y);
				setImageMatrix(matrix);
				firstDis = secondDis;
				break;

			default:
				break;
			}
			
		}
		return true;
	}

	/**
	 * ����֮��ľ���
	 * @param event
	 * @return
	 */
	private float getDisByXY(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		
		float x2 = event.getX(1);
		float y2 = event.getY(1);
		return FloatMath.sqrt((x-x2)*(x-x2)+(y-y2)*(y-y2));
	}

	private PointF getPointMidByEvent(MotionEvent event) {
		//���ŵ����ĵ�
		PointF point = new PointF();
		point.x = (event.getX()+event.getX(1))/2;
		point.y = (event.getY()+event.getY(1))/2;
		return point;
	}
	
}
