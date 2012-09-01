package cn.edu.seu.check;

import java.util.ArrayList;
import java.util.HashMap;

import cn.edu.seu.main.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class CheckAdapter extends BaseAdapter {
	
	private ArrayList<HashMap<String, Object>> datas = new ArrayList<HashMap<String, Object>>(); //数据
	private Context mContext;
	public CheckAdapter(Context c) {
		this.mContext = c;
	}
	public void addData(HashMap<String, Object> map){
		datas.add(map);
	}
	
	public void delData(){
		if(datas.size() > 0) datas.remove(datas.size() - 1);
	}
	
	public int getCount() {
		return datas.size();
	}

	public Object getItem(int arg0) {
		return datas.get(arg0);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view  = null ;
		if(datas.size()>1){//listView 数据是两条以上
			if(position == 0){ //第一条数据
				view = View.inflate(mContext, R.layout.list_item_top, null);
			}else if(position == datas.size() - 1){ //最后一条数据
				view = View.inflate(mContext, R.layout.list_item_bottom, null);
			}else{ //中间的数据
				view = View.inflate(mContext, R.layout.list_item_middle, null);
			}
		}else{ //只有一条数据
			view = View.inflate(mContext, R.layout.list_item_single, null);
		}
		((TextView)view.findViewById(R.id.t1)).setText((String)datas.get(position).get("name"));//设置文本样式
		((TextView)view.findViewById(R.id.t2)).setText((String)datas.get(position).get("price"));
		((TextView)view.findViewById(R.id.t3)).setText((String)datas.get(position).get("time"));
		((TextView)view.findViewById(R.id.t4)).setText((String)datas.get(position).get("cash"));
		return view;
	}
}
