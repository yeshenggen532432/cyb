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

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SimpleTreeAdapter_zuzhi<T> extends TreeListViewAdapter<T> {
	private Context mContext;
	private boolean mIsSingle;

	/**
	 * @param mTree
	 * @param datas
	 * @param isSingle 是否单选:默认多选
	 */
	public SimpleTreeAdapter_zuzhi(ListView mTree, Context context,
                                   List<T> datas, int defaultExpandLevel, boolean isSingle)
			throws IllegalArgumentException, IllegalAccessException {
		super(mTree, context, datas, defaultExpandLevel);
		this.mContext = context;
		this.mIsSingle = isSingle;
	}

	@Override
	public View getConvertView(final Node node, int position, View convertView,
                               ViewGroup parent) {
		/**
		 * 注释：node.getIcon()==-1或node.getChildren()为size为0：说明没有下级
		 *      node.isExpand()：//是否展开
		 */
		
		View root = mInflater.inflate(R.layout.x_adapter_simple_tree_zuzhi, parent, false);
		ImageView icon = (ImageView) root.findViewById(R.id.id_treenode_icon);//父：上下图标
		TextView label = (TextView) root.findViewById(R.id.id_treenode_label);//标签：设置名称
		final CheckBox cb = (CheckBox) root.findViewById(R.id.cb);//复选框：是否选中

		final List<Node> children = node.getChildren();
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
			if(mIsSingle){//单选
				cb.setVisibility(View.INVISIBLE);
			}else{
				cb.setVisibility(View.VISIBLE);
			}
			icon.setVisibility(View.VISIBLE);
			
			//上下图标
			if(node.isExpand()){//是否展开
				Drawable drawable_expand = mContext.getResources().getDrawable(R.drawable.y_icon_xl);
				icon.setImageDrawable(drawable_expand);
			}else{
				Drawable drawable = mContext.getResources().getDrawable(R.drawable.y_icon_gray_more_right);
				icon.setImageDrawable(drawable);
			}
		}
		
//		final List<Node> children = node.getChildren();
		if(children!=null && children.size()>0){//父

			Integer integer = Constans.ParentTrueMap_zuzhi.get(node.getId());
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
			Boolean isChecked = Constans.ziTrueMap_zuzhi.get(node.getId());
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
						Constans.ParentTrueMap_zuzhi.put(node.getId(), 1);//全选中
					}else{
						Constans.ParentTrueMap_zuzhi.put(node.getId(), 0);//没选中
					}
					for (int i = 0; i < children.size(); i++) {//遍历--设置下级
						Node node2 = children.get(i);
						Constans.ziTrueMap_zuzhi.put(node2.getId(), checked);
					}
					notifyDataSetChanged();
				}else{//子
					Node parent2 = node.getParent();//得到当前的父对象：parent2
					if(checked){//当为true时：遍历--设置上级(有一个为false,父为false)
						if(mIsSingle){//单选时：遍历map是否有之前有选中的，如果有应设为false
							Iterator<Map.Entry<Integer, Boolean>> it = Constans.ziTrueMap_zuzhi.entrySet().iterator();
							  while (it.hasNext()) {
							   Map.Entry<Integer, Boolean> entry = it.next();
							   Boolean value = entry.getValue();
							      if(value!=null && value==true){
							    	  Constans.ziTrueMap_zuzhi.put(entry.getKey(), false);
							    	  break;
							      }
							  }
							  Constans.ziTrueMap_zuzhi.put(node.getId(), checked);
							  notifyDataSetChanged();
						}else{//多选
							Constans.ziTrueMap_zuzhi.put(node.getId(), checked);
							List<Node> children2 = parent2.getChildren();
							for (int i = 0; i < children2.size(); i++) {//遍历--设置上级
								Node node2 = children2.get(i);
								Boolean ischeck = Constans.ziTrueMap_zuzhi.get(node2.getId());
								if(ischeck==null || ischeck==false){
									Constans.ParentTrueMap_zuzhi.put(parent2.getId(), 2);//部分选中
									notifyDataSetChanged();
									return;
								}
							}
							Constans.ParentTrueMap_zuzhi.put(parent2.getId(), 1);//全选中
							notifyDataSetChanged();
						}
					}else{//当为false时:父为false
						Constans.ziTrueMap_zuzhi.put(node.getId(), checked);
						List<Node> children2 = parent2.getChildren();
						for (int i = 0; i < children2.size(); i++) {//遍历--设置上级
							Node node2 = children2.get(i);
							Boolean ischeck = Constans.ziTrueMap_zuzhi.get(node2.getId());
							if(ischeck==null || ischeck==true){//
								Constans.ParentTrueMap_zuzhi.put(parent2.getId(), 2);//部分选中
								notifyDataSetChanged();
								return;
							}
						}
						Constans.ParentTrueMap_zuzhi.put(parent2.getId(), 0);
						notifyDataSetChanged();
					}
				}
			}
		});

		return root;
	}
}
