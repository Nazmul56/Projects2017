package com.androidsrc.draganddrop;

import android.app.Activity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MainActivity extends Activity implements OnTouchListener, OnDragListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//set ontouch listener for box views
		findViewById(R.id.box_view1).setOnTouchListener(this);
		findViewById(R.id.box_view2).setOnTouchListener(this);
		findViewById(R.id.box_view3).setOnTouchListener(this);
		findViewById(R.id.box_view4).setOnTouchListener(this);
		
		//set ondrag listener for right and left parent views
		findViewById(R.id.left_view).setOnDragListener(this);
		findViewById(R.id.right_view).setOnDragListener(this);
	}

	@Override
	public boolean onDrag(View v, DragEvent event) {
		// TODO Auto-generated method stub
		if(event.getAction()==DragEvent.ACTION_DROP){
			//we want to make sure it is dropped only to left and right parent view
			View view = (View)event.getLocalState();
			
			if(v.getId() == R.id.left_view || v.getId() == R.id.right_view){
				
				ViewGroup source = (ViewGroup) view.getParent();
				source.removeView(view);
				
				LinearLayout target = (LinearLayout) v;
				target.addView(view);
			}
			//make view visible as we set visibility to invisible while starting drag
			view.setVisibility(View.VISIBLE);
		}
		return true;
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		// TODO Auto-generated method stub
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
			view.startDrag(null, shadowBuilder, view, 0);
			view.setVisibility(View.INVISIBLE);
			return true;
		}
		return false;
	}

}
