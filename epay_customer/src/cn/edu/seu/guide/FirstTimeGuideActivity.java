package cn.edu.seu.guide;

import java.util.ArrayList;
import java.util.List;

import cn.edu.seu.login.Mapplication;
import cn.edu.seu.main.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class FirstTimeGuideActivity extends Activity {

	private static final int TO_THE_END = 0;// �������һ��
	private static final int LEAVE_FROM_END = 1;// �뿪���һ��

	private int[] ids = { R.drawable.guide_1_bak, R.drawable.guide_2_bak,
			R.drawable.guide_3_bak, R.drawable.guide_4_bak};
	private List<View> guides = new ArrayList<View>();
	private ViewPager pager;
	private ImageView open;
	private ImageView curDot;
	private int offset;// λ����
	private int curPos = 0;// ��¼��ǰ��λ��

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.guide);
		Mapplication.getInstance().addActivity(this);

		for (int i = 0; i < ids.length; i++) {
			ImageView iv = new ImageView(this);
			iv.setImageResource(ids[i]);
			ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.FILL_PARENT);
			iv.setLayoutParams(params);
			iv.setScaleType(ScaleType.FIT_XY);
			guides.add(iv);
		}

		//curDot = (ImageView) findViewById(R.id.cur_dot);
		open = (ImageView) findViewById(R.id.open);
		/*curDot.getViewTreeObserver().addOnPreDrawListener(
				new OnPreDrawListener() {

					public boolean onPreDraw() {
						// TODO Auto-generated method stub
						offset = curDot.getWidth();
						return true;
					}
				});*/

		BookFisherPagerAdapter adapter = new BookFisherPagerAdapter(guides);
		pager = (ViewPager) findViewById(R.id.contentPager);
		pager.setAdapter(adapter);
		pager.setOnPageChangeListener(new OnPageChangeListener() {

			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				//moveCursorTo(arg0);
				if (arg0 == ids.length - 1) {// �����һ����
					handler.sendEmptyMessageDelayed(TO_THE_END, 500);
				} else if (curPos == ids.length - 1) {
					handler.sendEmptyMessageDelayed(LEAVE_FROM_END, 100);
				}
				curPos = arg0;
			}

			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * �ƶ�ָ�뵽���ڵ�λ��
	 * 
	 * @param position
	 * 		  		ָ�������ֵ
	 * */
	private void moveCursorTo(int position) {
		// ʹ�þ��λ��
		TranslateAnimation anim = new TranslateAnimation(offset*curPos, offset*position, 0, 0);
		anim.setDuration(200);
		anim.setFillAfter(true);
		curDot.startAnimation(anim);
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what == TO_THE_END)
				open.setVisibility(View.VISIBLE);
			else if (msg.what == LEAVE_FROM_END)
				open.setVisibility(View.GONE);
		}

	};
}