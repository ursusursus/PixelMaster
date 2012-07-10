package com.pixelmaster.www;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

public class PixelView extends View {

	private Paint p = new Paint();
	private Bitmap bitmap;
	private int number = 4;
	private int blockWidth;
	private int blockHeight;
	public PixelView(Context context, Bitmap bitmap) {
		super(context);
		
		this.bitmap = bitmap;
		blockWidth = bitmap.getWidth() / number;
		blockHeight = bitmap.getHeight() / number;

	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		for (int i = 0; i < number; i++) {
		    for (int j = 0; j < number; j++) {
		        /*Color c = determineColor(sourceImg, i, j, blockSize, blockSize);
		        g.setColor(c);
		        g.fillRect(i, j, blockSize, blockSize);*/
		    	int color = bitmap.getPixel((i * blockWidth) + blockWidth/2,(j*blockHeight) + blockHeight/2);
		    	p.setColor(color);
		    	//Log.i("lol","wat");
		    	canvas.drawRect(new Rect(i*blockWidth,j*blockHeight,i*blockWidth+blockWidth,j*blockHeight+blockHeight),p);
		    }
		}
		
	}

}
