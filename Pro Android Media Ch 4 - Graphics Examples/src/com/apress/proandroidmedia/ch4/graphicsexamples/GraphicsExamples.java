package com.apress.proandroidmedia.ch4.graphicsexamples;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ImageView;

public class GraphicsExamples extends Activity {

	ImageView drawingImageView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		drawingImageView = (ImageView) this.findViewById(R.id.DrawingImageView);

		Bitmap bitmap = Bitmap.createBitmap((int) getWindowManager()
				.getDefaultDisplay().getWidth(), (int) getWindowManager()
				.getDefaultDisplay().getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawingImageView.setImageBitmap(bitmap);

		// Point
		/*
		 * Paint paint = new Paint(); paint.setColor(Color.GREEN);
		 * paint.setStrokeWidth(100); canvas.drawPoint(199,201,paint);
		 */

		// Line
		/*
		 * Paint paint = new Paint(); paint.setColor(Color.GREEN);
		 * paint.setStrokeWidth(10); int startx = 50; int starty = 100; int endx
		 * = 150; int endy = 210;
		 * canvas.drawLine(startx,starty,endx,endy,paint);
		 */

		// Rectangle
		/*
		 * Paint paint = new Paint(); paint.setColor(Color.GREEN);
		 * paint.setStyle(Paint.Style.FILL_AND_STROKE);
		 * paint.setStrokeWidth(10); float leftx = 20; float topy = 20; float
		 * rightx = 50; float bottomy = 100; canvas.drawRect(leftx, topy,
		 * rightx, bottomy, paint);
		 */

		// RectF Rectangle
		/*
		 * Paint paint = new Paint(); float leftx = 20; float topy = 20; float
		 * rightx = 50; float bottomy = 100; RectF rectangle = new
		 * RectF(leftx,topy,rightx,bottomy); canvas.drawRect(rectangle, paint);
		 */

		// Oval
		/*
		 * Paint paint = new Paint(); paint.setColor(Color.GREEN);
		 * paint.setStyle(Paint.Style.STROKE); float leftx = 20; float topy =
		 * 20; float rightx = 50; float bottomy = 100; RectF ovalBounds = new
		 * RectF(leftx,topy,rightx,bottomy); canvas.drawOval(ovalBounds, paint);
		 */

		// Circle
		/*
		 * Paint paint = new Paint(); paint.setColor(Color.GREEN);
		 * paint.setStyle(Paint.Style.STROKE); float x = 50; float y = 50; float
		 * radius = 20; canvas.drawCircle(x, y, radius, paint);
		 */

		// Path
		/*
		 * Paint paint = new Paint(); paint.setStyle(Paint.Style.STROKE);
		 * paint.setColor(Color.GREEN); Path p = new Path(); p.moveTo (20, 20);
		 * p.lineTo(100, 200); p.lineTo(200, 100); p.lineTo(240, 155);
		 * p.lineTo(250, 175); p.lineTo(20, 20); canvas.drawPath(p, paint);
		 */

		// Text
		/*
		 * Paint paint = new Paint(); paint.setColor(Color.GREEN);
		 * paint.setTextSize(40); float text_x = 120; float text_y = 120;
		 * canvas.drawText("Hello", text_x, text_y, paint);
		 */

		// Custom Font Text
		/*
		 * Paint paint = new Paint(); paint.setColor(Color.GREEN);
		 * paint.setTextSize(40); Typeface chops =
		 * Typeface.createFromAsset(getAssets(), "ChopinScript.ttf");
		 * paint.setTypeface(chops); float text_x = 120; float text_y = 120;
		 * canvas.drawText("Hello", text_x, text_y, paint);
		 */

		// Text on a Path
		Paint paint = new Paint();
		paint.setColor(Color.GREEN);
		paint.setTextSize(20);
		paint.setTypeface(Typeface.DEFAULT);
		Path p = new Path();
		p.moveTo(20, 20);
		p.lineTo(100, 150);
		p.lineTo(200, 220);
		canvas.drawTextOnPath("Hello this is text on a path", p, 0, 0, paint);
	}
}
