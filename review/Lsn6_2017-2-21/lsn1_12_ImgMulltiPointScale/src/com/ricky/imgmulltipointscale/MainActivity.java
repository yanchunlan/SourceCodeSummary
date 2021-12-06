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
//			//第一个手指按下
//			switch (event.getAction()) {
//			case MotionEvent.ACTION_DOWN:
//				startX = event.getX();
//				startY = event.getY();
//				break;
//			case MotionEvent.ACTION_MOVE:
//				//移动图片
//				float x = event.getX();
//				float y = event.getY();
//				//dx:增量 delta
//				matrix.postTranslate(x-startX, y-startY);
//				//更新原来的坐标
//				startX = x;
//				startY = y;
//				img.setImageMatrix(matrix);//设置新的矩阵
//				break;
//			case MotionEvent.ACTION_UP:
//				break;
//	
//			default:
//				break;
//			}
//		}else if(pointerCount==2){
//			//缩放
//			switch (event.getAction()&MotionEvent.ACTION_MASK) {
//			case MotionEvent.ACTION_POINTER_DOWN:
//				//第二根手指按下去
//				//缩放的中心点
//				pointMid = getPointMidByEvent(event);
//				//当前两点的距离
//				firstDis = getDisByXY(event);
//				break;
//			case MotionEvent.ACTION_MOVE:
//				//两根手指移动--缩放
//				//四个参数：x方向缩放值；y方向缩放值；缩放中心点坐标x,y
//				//缩放值跟两点的距离有关
//				//现在两个点的距离
//				secondDis = getDisByXY(event);
//				float scale = secondDis/firstDis;//缩放比例
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
//			//第一个手指按下
//			switch (event.getAction()) {
//			case MotionEvent.ACTION_DOWN:
//				startX = event.getX();
//				startY = event.getY();
//				break;
//			case MotionEvent.ACTION_MOVE:
//				//移动图片
//				float x = event.getX();
//				float y = event.getY();
//				//dx:增量 delta
//				matrix.postTranslate(x-startX, y-startY);
//				//更新原来的坐标
//				startX = x;
//				startY = y;
//				img.setImageMatrix(matrix);//设置新的矩阵
//				break;
//			case MotionEvent.ACTION_UP:
//				break;
//
//			default:
//				break;
//			}
//		}else if(pointerCount==2){
//			//缩放
//			switch (event.getAction()&MotionEvent.ACTION_MASK) {
//			case MotionEvent.ACTION_POINTER_DOWN:
//				//第二根手指按下去
//				//缩放的中心点
//				pointMid = getPointMidByEvent(event);
//				//当前两点的距离
//				firstDis = getDisByXY(event);
//				break;
//			case MotionEvent.ACTION_MOVE:
//				//两根手指移动--缩放
//				//四个参数：x方向缩放值；y方向缩放值；缩放中心点坐标x,y
//				//缩放值跟两点的距离有关
//				//现在两个点的距离
//				secondDis = getDisByXY(event);
//				float scale = secondDis/firstDis;//缩放比例
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
	 * 两点之间的距离
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
		//缩放的中心点
		PointF point = new PointF();
		point.x = (event.getX()+event.getX(1))/2;
		point.y = (event.getY()+event.getY(1))/2;
		return point;
	}

}
