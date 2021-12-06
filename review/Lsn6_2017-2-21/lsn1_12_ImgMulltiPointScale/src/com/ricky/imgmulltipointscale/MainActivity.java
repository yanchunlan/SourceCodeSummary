package com.ricky.imgmulltipointscale;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.widget.ImageView;

public class MainActivity extends Activity 
{
//implements OnTouchListener {

	private ImageView img;
	private Bitmap bitmap;
	private Matrix matrix;
	private float startX;
	private float startY;
	private PointF pointMid;
	private float firstDis;
	private float secondDis;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		matrix = new Matrix();
		img = (ImageView)findViewById(R.id.iv);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.liutao);
		img.setImageBitmap(bitmap);
//		img.setImageMatrix(matrix);
//		img.setOnTouchListener(this);
	}
	
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		int pointerCount = event.getPointerCount();
//		if(pointerCount==1){
//			//��һ����ָ����
//			switch (event.getAction()) {
//			case MotionEvent.ACTION_DOWN:
//				startX = event.getX();
//				startY = event.getY();
//				break;
//			case MotionEvent.ACTION_MOVE:
//				//�ƶ�ͼƬ
//				float x = event.getX();
//				float y = event.getY();
//				//dx:���� delta
//				matrix.postTranslate(x-startX, y-startY);
//				//����ԭ��������
//				startX = x;
//				startY = y;
//				img.setImageMatrix(matrix);//�����µľ���
//				break;
//			case MotionEvent.ACTION_UP:
//				break;
//	
//			default:
//				break;
//			}
//		}else if(pointerCount==2){
//			//����
//			switch (event.getAction()&MotionEvent.ACTION_MASK) {
//			case MotionEvent.ACTION_POINTER_DOWN:
//				//�ڶ�����ָ����ȥ
//				//���ŵ����ĵ�
//				pointMid = getPointMidByEvent(event);
//				//��ǰ����ľ���
//				firstDis = getDisByXY(event);
//				break;
//			case MotionEvent.ACTION_MOVE:
//				//������ָ�ƶ�--����
//				//�ĸ�������x��������ֵ��y��������ֵ���������ĵ�����x,y
//				//����ֵ������ľ����й�
//				//����������ľ���
//				secondDis = getDisByXY(event);
//				float scale = secondDis/firstDis;//���ű���
//				matrix.postScale(scale, scale, pointMid.x, pointMid.y);
//				firstDis = secondDis;
//				img.setImageMatrix(matrix);
//				break;
//	
//			default:
//				break;
//			}
//			
//		}
//		return super.onTouchEvent(event);
//	}

//	@Override
//	public boolean onTouch(View v, MotionEvent event) {
//		int pointerCount = event.getPointerCount();
//		if(pointerCount==1){
//			//��һ����ָ����
//			switch (event.getAction()) {
//			case MotionEvent.ACTION_DOWN:
//				startX = event.getX();
//				startY = event.getY();
//				break;
//			case MotionEvent.ACTION_MOVE:
//				//�ƶ�ͼƬ
//				float x = event.getX();
//				float y = event.getY();
//				//dx:���� delta
//				matrix.postTranslate(x-startX, y-startY);
//				//����ԭ��������
//				startX = x;
//				startY = y;
//				img.setImageMatrix(matrix);//�����µľ���
//				break;
//			case MotionEvent.ACTION_UP:
//				break;
//
//			default:
//				break;
//			}
//		}else if(pointerCount==2){
//			//����
//			switch (event.getAction()&MotionEvent.ACTION_MASK) {
//			case MotionEvent.ACTION_POINTER_DOWN:
//				//�ڶ�����ָ����ȥ
//				//���ŵ����ĵ�
//				pointMid = getPointMidByEvent(event);
//				//��ǰ����ľ���
//				firstDis = getDisByXY(event);
//				break;
//			case MotionEvent.ACTION_MOVE:
//				//������ָ�ƶ�--����
//				//�ĸ�������x��������ֵ��y��������ֵ���������ĵ�����x,y
//				//����ֵ������ľ����й�
//				//����������ľ���
//				secondDis = getDisByXY(event);
//				float scale = secondDis/firstDis;//���ű���
//				matrix.postScale(scale, scale, pointMid.x, pointMid.y);
//				firstDis = secondDis;
//				img.setImageMatrix(matrix);
//				break;
//
//			default:
//				break;
//			}
//		}
//		return false;
//	}

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
