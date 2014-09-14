package com.alamaanah.nisaarnadiadwala;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class Fx {

	Context ctx;
	View v;

	public static void slide_down(Context ctx, View v) {
		Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_down);
		if (a != null) {
			a.reset();
			if (v != null) {
				v.clearAnimation();
				v.startAnimation(a);
			}
		}
	}

	public static void slide_up(Context ctx, final View v, final View v2) {
		Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_up);
		if (a != null) {
			a.reset();
			if (v != null) {
				v.clearAnimation();
				v.startAnimation(a);
			}
		}
		a.setAnimationListener(new Animation.AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				v.setVisibility(View.GONE);
				v2.setBackgroundResource(R.drawable.roundedcorners);
			}
		});
	}
	public static void slide_in_left(Context ctx, View v) {
		Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_in_left);
		if (a != null) {
			a.reset();
			if (v != null) {
				v.clearAnimation();
				v.startAnimation(a);
			}
		}
	}
	public static void slide_in_right(Context ctx, View v) {
		Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_in_right);
		if (a != null) {
			a.reset();
			if (v != null) {
				v.clearAnimation();
				v.startAnimation(a);
			}
		}
	}
	public static void delay(Context ctx, View v) {
		Animation a = AnimationUtils.loadAnimation(ctx, R.anim.delay);
		if (a != null) {
			a.reset();
			if (v != null) {
				v.clearAnimation();
				v.startAnimation(a);
			}
		}
	}

}
