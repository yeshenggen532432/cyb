package com.qwb.view.audit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.qwb.utils.Constans;
import com.qwb.utils.MyGlideUtil;
import com.xmsx.qiweibao.R;

public class ShenPi_PicAdatper extends BaseAdapter {

	private Context context;
	private boolean isDelModel;

	public ShenPi_PicAdatper(Context context) {
		this.context = context ;
	}
	
	@Override
	public int getCount() {
		return Constans.publish_pics.size() > 0 ? Constans.publish_pics.size()+2 : 1;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (context == null) {
			return null ;
		}

        View inflate = LayoutInflater.from(context).inflate(R.layout.x_adapter_sp_pic, null);
        ImageView iv_simple = (ImageView) inflate.findViewById(R.id.iv_simple);
        ImageView iv_delpic = (ImageView) inflate.findViewById(R.id.iv_delpic);
        
        
        if (Constans.publish_pics.size() <= 0) {
			
            iv_simple.setImageResource(R.drawable.ckin_addpic);
		}else{
			if (position == Constans.publish_pics.size()) {
				iv_simple.setImageResource(R.drawable.ckin_addpic);
			}
			else if(position == Constans.publish_pics.size() + 1){
				iv_simple.setImageResource(R.drawable.delpic);
			}
			else{
				if (isDelModel) {
	            	iv_delpic.setVisibility(View.VISIBLE);
				}else{
					iv_delpic.setVisibility(View.GONE);
				}
				String sourcePath = Constans.publish_pics.get(position);
				iv_simple.setTag(sourcePath);
				MyGlideUtil.getInstance().displayImageSquere("file://"+sourcePath, iv_simple);
			}
		}
        return inflate;
    
	}

	public void refreshAdapter (boolean isDelModel){
		this.isDelModel = isDelModel ;
		this.notifyDataSetChanged();
	}
	
}
