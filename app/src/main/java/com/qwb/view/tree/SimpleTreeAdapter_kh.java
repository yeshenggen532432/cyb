package com.qwb.view.tree;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.qwb.utils.Constans;
import com.xmsx.qiweibao.R;
import com.zhy.tree.bean.Node;
import com.zhy.tree.bean.TreeListViewAdapter;

import java.util.List;

public class SimpleTreeAdapter_kh<T> extends TreeListViewAdapter<T> {
	private Context mContext;

	/**
	 * 
	 * @param mTree
	 * @param datas
	 * @param defaultExpandLevel
	 *            默认展开几级树
	 */
	public SimpleTreeAdapter_kh(ListView mTree, Context context,
                                List<T> datas, int defaultExpandLevel)
			throws IllegalArgumentException, IllegalAccessException {
		super(mTree, context, datas, defaultExpandLevel);
		this.mContext = context;
	}

	@Override
	public View getConvertView(final Node node, int position, View convertView,
                               ViewGroup parent) {
		/**
		 * 注释：node.getIcon()==-1或node.getChildren()为size为0：说明没有下级
		 *      node.isExpand()：//是否展开
		 */
		
		View root = mInflater.inflate(R.layout.x_adapter_simple_tree, parent, false);
		ImageView icon = (ImageView) root.findViewById(R.id.id_treenode_icon);
		TextView label = (TextView) root.findViewById(R.id.id_treenode_label);
		final CheckBox cb = (CheckBox) root.findViewById(R.id.cb);

		label.setText(node.getName());
		
		if (node.getIcon() == -1) {//最后一层
			if(node.getLevel()==0){
				cb.setVisibility(View.INVISIBLE);
				icon.setVisibility(View.INVISIBLE);
			}else{
				cb.setVisibility(View.VISIBLE);
				icon.setVisibility(View.INVISIBLE);
			}
		} else {
			cb.setVisibility(View.VISIBLE);
			icon.setVisibility(View.VISIBLE);
			if(node.isExpand()){//是否展开
				Drawable drawable_expand = mContext.getResources().getDrawable(R.drawable.y_icon_xl);
				icon.setImageDrawable(drawable_expand);
			}else{
				Drawable drawable = mContext.getResources().getDrawable(R.drawable.y_icon_gray_more_right);
				icon.setImageDrawable(drawable);
			}
		}
		
		final List<Node> children = node.getChildren();
		if(children!=null && children.size()>0){//父
//			Boolean isChecked = Constans.ParentTrueMap.get(node.getId());
//			if (isChecked == null) {
//				cb.setChecked(false);
//			} else {
//				cb.setChecked(isChecked);
//			}
			Integer integer = Constans.ParentTrueMap2.get(node.getId());
			if(integer!=null){
				switch (integer) {
				case 0:
					cb.setChecked(false);
					cb.setBackgroundResource(R.drawable.icon_dx);
					break;
				case 1:
					cb.setChecked(true);
					cb.setBackgroundResource(R.drawable.icon_dxz);
					break;
				case 2:
					cb.setChecked(true);
					cb.setBackgroundResource(R.drawable.icon_dx_bufen);
					break;
				}
			}else{
				cb.setChecked(false);
				cb.setBackgroundResource(R.drawable.icon_dx);
			}
		}else{//子
			// 防止cb复用
			Boolean isChecked = Constans.ziTrueMap.get(node.getId());
			if (isChecked == null) {
				cb.setChecked(false);
				cb.setBackgroundResource(R.drawable.icon_dx);
			} else {
				if(isChecked){
				cb.setChecked(true);
				cb.setBackgroundResource(R.drawable.icon_dxz);
			}else{
				cb.setChecked(false);
				cb.setBackgroundResource(R.drawable.icon_dx);
			}
		}
		}

		cb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean checked = cb.isChecked();
				if(children!=null && children.size()>0){//父
					if(checked){
						Constans.ParentTrueMap2.put(node.getId(), 1);//全选中
					}else{
						Constans.ParentTrueMap2.put(node.getId(), 0);//没选中
					}
					for (int i = 0; i < children.size(); i++) {//遍历--设置下级
						Node node2 = children.get(i);
						Constans.ziTrueMap.put(node2.getId(), checked);
					}
					notifyDataSetChanged();
				}else{//子
					Node parent2 = node.getParent();//得到当前的父对象：parent2
					if(checked){//当为true时：遍历--设置上级(有一个为false,父为false)
						//多选
							Constans.ziTrueMap.put(node.getId(), checked);
							List<Node> children2 = parent2.getChildren();
							for (int i = 0; i < children2.size(); i++) {//遍历--设置上级
								Node node2 = children2.get(i);
								Boolean ischeck = Constans.ziTrueMap.get(node2.getId());
								if(ischeck==null || ischeck==false){
									Constans.ParentTrueMap2.put(parent2.getId(), 2);//部分选中
									notifyDataSetChanged();
									return;
								}
							}
							Constans.ParentTrueMap2.put(parent2.getId(), 1);//全选中
							notifyDataSetChanged();
						
					}else{//当为false时:父为false
						Constans.ziTrueMap.put(node.getId(), checked);
						List<Node> children2 = parent2.getChildren();
						for (int i = 0; i < children2.size(); i++) {//遍历--设置上级
							Node node2 = children2.get(i);
							Boolean ischeck = Constans.ziTrueMap.get(node2.getId());
							if(ischeck==null || ischeck==true){//
								Constans.ParentTrueMap2.put(parent2.getId(), 2);//部分选中
								notifyDataSetChanged();
								return;
							}
						}
						Constans.ParentTrueMap2.put(parent2.getId(), 0);
						notifyDataSetChanged();
					}
				}
			}
		});
		return root;
	}
}
