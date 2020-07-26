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
 */
package com.xmsx.cnlife.widget.emoji;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;


/**
 * 
 * @ClassName: CCPTextView.java
 */
public class CCPTextView extends AppCompatTextView {

	/**
	 * @param context
	 */
	public CCPTextView(Context context) {
		super(context);
	}
	
	/**
	 * @param context
	 * @param attrs
	 */
	public CCPTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public CCPTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	//用它的方法获取SpannableString
	public SpannableString analyseString(String msg){
		return EmoticonUtil.emoji2CharSequence(getContext(), msg, (int) getTextSize(), false);
		
	}
	
	
	public void setEmojiText(String msg) {
		SpannableString emoji = EmoticonUtil.emoji2CharSequence(getContext(), msg, (int) getTextSize(), false);
		
		setText(emoji);
//		SpannableStringBuilder analyseHtml = analyseHtml(String.valueOf(emoji));
//		setText(analyseHtml);
	}
    /**
     * 将字符串解析成可点击的html
     * @return 
     * */
    private SpannableStringBuilder analyseHtml(String s) {
    	 int end = s.length();  
		   Spannable sp = (Spannable) Html.fromHtml(s);  
		   URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);  
		   
		   //清楚掉html标签 
		   SpannableStringBuilder style = new SpannableStringBuilder(sp);  
		   style.clearSpans(); 
    	
		   for (URLSpan url : urls) {  
//			    将关键字数组中的文字添加到新生成的style中去  
			    MyURLSpan myURLSpan = new MyURLSpan(url.getURL());  
			    style.setSpan(myURLSpan, sp.getSpanStart(url),  
			      sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
			    
			    //设置链接字体的颜�?
			    style.setSpan(new ForegroundColorSpan(Color.MAGENTA), sp.getSpanStart(url), 
			    		sp.getSpanEnd(url), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			   } 
		   return style ;
		   //设置可点�?
//		   tv.setMovementMethod(LinkMovementMethod.getInstance());  
	}
    
	 private class MyURLSpan extends ClickableSpan {  
		  private String mUrl;
		  
		  /** 
		   * 构�?�?
		   * @param url 可以点击的关键字，构造时传入�?
		   */  
		  MyURLSpan(String url) {  
		   mUrl = url;  
		  }
		  
		  @Override
		public void updateDrawState(TextPaint ds) {
			  ds.setColor(ds.linkColor);
		        ds.setUnderlineText(false); 
		}
		  
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Toast.makeText(getContext(), "ssss", Toast.LENGTH_SHORT).show();
		}  
	
	
	}
}
