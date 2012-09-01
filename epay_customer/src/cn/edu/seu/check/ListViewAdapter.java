package cn.edu.seu.check;

import java.util.ArrayList;
import java.util.HashMap;

import cn.edu.seu.main.R;



import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ListViewAdapter extends BaseAdapter {
	
	private ArrayList<HashMap<String, Object>> datas = new ArrayList<HashMap<String, Object>>(); //数据
	private Context mContext;
	public ListViewAdapter(Context c) {
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
		((TextView)view.findViewById(R.id.name)).setText((String)datas.get(position).get("username"));//设置文本样式
		((TextView)view.findViewById(R.id.type)).setText((String)datas.get(position).get("type"));
		((TextView)view.findViewById(R.id.time)).setText((String)datas.get(position).get("time"));
		((TextView)view.findViewById(R.id.price)).setText((String)datas.get(position).get("price"));
		return view;
	}
}
