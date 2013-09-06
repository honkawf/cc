package cn.edu.seu.spinner;

import java.util.ArrayList;
import java.util.List;

import cn.edu.seu.main.R;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public abstract class AbstractSpinnerAdapter<T> extends BaseAdapter {

	public static interface IOnItemSelectListener{
		public void onItemClick(View v,int pos);
	};
	
	 private Context mContext;   
	 private List<T> mObjects = new ArrayList<T>();
	 private int mSelectItem = 0;
	    
	 private LayoutInflater mInflater;
	
	 public  AbstractSpinnerAdapter(Context context){
		 init(context);
	 }
	 
	 public void refreshData(List<T> objects, int selIndex){
		 mObjects = objects;
		 if (selIndex < 0){
			 selIndex = 0;
		 }
		 if (selIndex >= mObjects.size()){
			 selIndex = mObjects.size() - 1;
		 }
		 
		 mSelectItem = selIndex;
	 }
	 
	 private void init(Context context) {
	        mContext = context;
	        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 }
	    
	    

	public int getCount() {

		return mObjects.size();
	}


	public Object getItem(int pos) {
		return mObjects.get(pos).toString();
	}


	public long getItemId(int pos) {
		return pos;
	}


	public View getView(int pos, View convertView, ViewGroup arg2) {
		 ViewHolder viewHolder;
    	 
	     if (convertView == null) {
	    	 convertView = mInflater.inflate(R.layout.spinner_item_layout, null);
	         viewHolder = new ViewHolder();
	         viewHolder.mTextView = (TextView) convertView.findViewById(R.id.textView);
	         convertView.setTag(viewHolder);
	     } else {
	         viewHolder = (ViewHolder) convertView.getTag();
	     }

	     
	     Object item =  getItem(pos);
		 viewHolder.mTextView.setText(item.toString());

	     return convertView;
	}

	public static class ViewHolder
	{
	    public TextView mTextView;
    }


}