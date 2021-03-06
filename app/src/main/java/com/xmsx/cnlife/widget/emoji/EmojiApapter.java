/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.cloopen.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */package com.xmsx.cnlife.widget.emoji;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.chiyong.t3.R;

import java.util.ArrayList;

/**
* <p>Title: EmojiApapter</p>
* <p>Description: </p>
* <p>Company: http://www.cloopen.com/</p>
* @author  Jorstin Chan
* @version 3.6
* @date 2013-12-26
 */
public class EmojiApapter extends BaseAdapter {


	ArrayList<CCPEmoji> emojis;
	
	LayoutInflater mInflater;

    public EmojiApapter(Context context) {
    	
    	mInflater = LayoutInflater.from(context);;
    }


	@Override
	public int getCount() {

		if(emojis != null && emojis.size() > 0) {
			return this.emojis.size() + 1;
		}
		
		return 0;
	}


	@Override
	public Object getItem(int position) {
		
		if(emojis != null && (position <= (emojis.size() - 1)))  {
			return emojis.get(position);
		}
		
		return null;
	}


	@Override
	public long getItemId(int position) {

		return position;
	}
	

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView == null) {
            viewHolder=new ViewHolder();
            convertView=mInflater.inflate(R.layout.x_adapter_emoji, null);
            viewHolder.emoji_icon=(ImageView)convertView.findViewById(R.id.emoji_id);
            viewHolder.emoji_icon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            convertView.setTag(viewHolder);
        } else {
            viewHolder=(ViewHolder)convertView.getTag();
        }
        
        
        if(getCount() - 1 == position) {
    		viewHolder.emoji_icon.setImageResource(R.drawable.emoji_del_selector);
        } else {
        	
        	CCPEmoji emoji=(CCPEmoji) getItem(position);
        	if(emoji != null) {
        		if(emoji.getId() == R.drawable.emoji_del_selector) {
        			convertView.setBackgroundDrawable(null);
        			viewHolder.emoji_icon.setImageResource(emoji.getId());
        		} else if(TextUtils.isEmpty(emoji.getEmojiDesc())) {
        			convertView.setBackgroundDrawable(null);
        			viewHolder.emoji_icon.setImageDrawable(null);
        		} else {
        			viewHolder.emoji_icon.setTag(emoji);
        			viewHolder.emoji_icon.setImageResource(emoji.getId());
        		}
        	}
        }

        return convertView;
    }

    class ViewHolder {

        public ImageView emoji_icon;
    }
    
    /**
     * @param emojis
     */
    public void updateEmoji(ArrayList<CCPEmoji> emojis) {
    	this.emojis = emojis;
    	if(this.emojis == null) {
    		emojis = new ArrayList<CCPEmoji>();
    	}
    	notifyDataSetChanged();
    }
}