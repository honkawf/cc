package cn.edu.seu.financing;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter{

	private Context mcontext;
	private String [] list;
	
	public MyAdapter(Context context ,String [] str){
		mcontext = context;
		list = str;
	}
	
	public int getCount() {
		// TODO Auto-generated method stub
		return list.length;
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list[arg0];
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public View getView(int arg0, View arg1, ViewGroup arg2) {
		TextView mTextView = new TextView(mcontext);
		mTextView.setText((String)getItem(arg0));
		return mTextView;
	}

}
