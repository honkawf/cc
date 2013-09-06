package cn.edu.seu.spinner;

import java.util.List;

import cn.edu.seu.main.R;
import cn.edu.seu.spinner.AbstractSpinnerAdapter.IOnItemSelectListener;


import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.AdapterView.OnItemClickListener;
public class SpinnerPopWindow extends PopupWindow implements OnItemClickListener{

	private Context mContext;
	private ListView mListView;
	private AbstractSpinnerAdapter mAdapter;
	private IOnItemSelectListener mItemSelectListener;
	private View vi;
	
	
	public SpinnerPopWindow(Context context)
	{
		super(context);
		
		mContext = context;
		init();
	}
	
	
	public void setItemListener(View v,IOnItemSelectListener listener){
		mItemSelectListener = listener;
		vi=v;
	}
	
	public void setAdatper(AbstractSpinnerAdapter adapter){
		mAdapter = adapter;
		mListView.setAdapter(mAdapter);	
	}

	
	private void init()
	{
		View view = LayoutInflater.from(mContext).inflate(R.layout.spinner_window_layout, null);
		setContentView(view);		
		setWidth(LayoutParams.WRAP_CONTENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		
		setFocusable(true);
    	ColorDrawable dw = new ColorDrawable(0x00);
		setBackgroundDrawable(dw);
	
		
		mListView = (ListView) view.findViewById(R.id.listview);
		mListView.setOnItemClickListener(this);
	}
	
	
	public <T> void refreshData(List<T> list, int selIndex)
	{
		if (list != null && selIndex  != -1)
		{
			if (mAdapter != null){
				mAdapter.refreshData(list, selIndex);
			}		
		}
	}



	public void onItemClick(AdapterView<?> arg0, View view, int pos, long arg3) {
		dismiss();
	    
		if (mItemSelectListener != null){
			mItemSelectListener.onItemClick(vi,pos);
		}
	}


	
}

