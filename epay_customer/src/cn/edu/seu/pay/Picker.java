package cn.edu.seu.pay;


import cn.edu.seu.main.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Picker extends LinearLayout{
	
	private Button plus,minus;
	private TextView textView;
	public Picker(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	 public Picker(Context context, AttributeSet attrs) {
	        super(context, attrs);  
	        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
	        inflater.inflate(R.layout.picker, this);  
	        plus=(Button)findViewById(R.id.plus);
	       /* plus.setOnClickListener(new Button.OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Integer quantity=Integer.parseInt(textView.getText().toString())+1;
					textView.setText(Integer.toString(quantity));
				}
	        	
	        });*/
	        minus=(Button)findViewById(R.id.minus);
	        /*minus.setOnClickListener(new Button.OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					Integer quantity=Integer.parseInt(textView.getText().toString());
					if(quantity>=1)
					{
						quantity--;
						textView.setText(Integer.toString(quantity));
					}
				}
	        	
	        });*/
	        textView=(TextView)findViewById(R.id.quantity);
	 }
	 public Button getButtonPlus()
	 {
		 return plus;
	 }
	 public Button getButtonMinus()
	 {
		 return minus;
	 }
	 public void setText(String text)
	 {
		 textView.setText(text);
	 }
	 public String getText()
	 {
		 return textView.getText().toString();
	 }

}
