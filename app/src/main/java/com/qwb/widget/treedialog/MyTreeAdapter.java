package com.qwb.widget.treedialog;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.qwb.utils.MyColorUtil;
import com.xmsx.qiweibao.R;
import com.zhy.tree.bean.Node;
import com.zhy.tree.bean.TreeListViewAdapter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MyTreeAdapter<T> extends TreeListViewAdapter<T> {
	private Context mContext;
	private boolean multiple = true;

	public Map<Integer, Integer> mCheckMap = new LinkedHashMap<>();
	//初始化时mCheckMap和mTempMap值一样；修改时：按确定按钮mCheckMap赋值给mTempMap；按取取消按钮mTempMap赋值给mCheckMap
	public Map<Integer, Integer> mTempMap = new LinkedHashMap<>();

	/**
	 * @mTree mTree
	 * @datas datas
	 * @defaultExpandLevel 默认展开几级树
	 * @isSingle 是否单选:默认多选
	 */
	public MyTreeAdapter(ListView mTree, Context context, List<T> datas, HashMap<Integer, Integer> checkMap,  int defaultExpandLevel, boolean multiple)
			throws IllegalArgumentException, IllegalAccessException {
		super(mTree, context, datas, defaultExpandLevel);
		this.mContext = context;
		this.multiple = multiple;
		if(null != checkMap && !checkMap.isEmpty()){
			this.mCheckMap.putAll(checkMap);
			this.mTempMap.putAll(checkMap);
		}
	}

	@Override
	public View getConvertView(final Node node, int position, View convertView,ViewGroup parent) {
		/**
		 * 注释：node.getIcon()==-1或node.getChildren()为size为0：说明没有下级 node.isExpand()：//是否展开
		 *
		 */
		
		View root = mInflater.inflate(R.layout.x_adapter_tree, parent, false);
		ImageView icon = root.findViewById(R.id.id_tree_icon);//父：上下图标
		TextView label = root.findViewById(R.id.id_tree_label);//标签：设置名称
		ImageView ivCheck = root.findViewById(R.id.id_tree_check);//复选框：是否选中
		View layoutCheck = root.findViewById(R.id.id_tree_layout_check);//复选框：是否选中

		label.setText(node.getName());
		Integer checkState = mCheckMap.get(node.getId());
		if(null != checkState){
			switch (checkState) {
				case 0:
					ivCheck.setImageResource(R.mipmap.x_ic_tree_checkbox_uncheck);
					break;
				case 1:
					ivCheck.setImageResource(R.mipmap.x_ic_tree_checkbox_check);
					break;
				case 2:
					ivCheck.setImageResource(R.mipmap.x_ic_tree_checkbox_bufen_check);
					break;
			}
		}else{
			ivCheck.setImageResource(R.mipmap.x_ic_tree_checkbox_uncheck);
		}


		List<Node> children = node.getChildren();
		if(multiple){
			if (null == children || children.isEmpty()) {
//				node.getLevel() == 0 :是第一级并且是最后一层
				icon.setVisibility(View.INVISIBLE);
				label.setTextColor(MyColorUtil.getColorResId(R.color.gray_9));
			} else {
				icon.setVisibility(View.VISIBLE);
				label.setTextColor(MyColorUtil.getColorResId(R.color.gray_6));

				if (node.isExpand()) {
					icon.animate().rotation(90).setDuration(0).start();
//					icon.animate().rotation(90).setDuration(200).start();
				} else {
					icon.animate().rotation(0).setDuration(0).start();
				}
			}
		}else{
			//单选
			if (null == children || children.isEmpty()) {
				layoutCheck.setVisibility(View.VISIBLE);
				icon.setVisibility(View.INVISIBLE);
				label.setTextColor(MyColorUtil.getColorResId(R.color.gray_9));
			} else {
				layoutCheck.setVisibility(View.GONE);
				icon.setVisibility(View.VISIBLE);
				label.setTextColor(MyColorUtil.getColorResId(R.color.gray_3));

				if (node.isExpand()) {
					icon.animate().rotation(90).setDuration(0).start();
				} else {
					icon.animate().rotation(0).setDuration(0).start();
				}
			}
		}


		final int id = node.getId();
		layoutCheck.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Integer checkedState = mCheckMap.get(id);
				if(null == checkedState || 0 == checkedState){
					if(multiple){
						//多选：选中
						setMultipleChecked(id, node);
					}else{
						//单选：没选中
						setSingleChecked(id);
					}

				}else{
					if(multiple){
						//多选：没选中
						setMultipleUnchecked(id, node);
					}else{
						//单选：没选中
						setSingleUnChecked(id);
					}
				}
				notifyDataSetChanged();
			}
		});

		return root;
	}

	public Map<Integer, Integer> getCheckMap() {
		return mCheckMap;
	}

	public void setCheckMap(Map<Integer, Integer> tempMap) {
		this.mCheckMap.clear();
		this.mCheckMap.putAll(tempMap);
	}

	public Map<Integer, Integer> getTempMap() {
		return mTempMap;
	}

	public void setTempMap(Map<Integer, Integer> checkMap) {
		this.mTempMap.clear();
		this.mTempMap.putAll(checkMap);
	}

	//----------------------------------------------------------------------------------------------

	//多选：选中
	private void setMultipleChecked(int id, Node node) {
		//遍历下级(2级)全部选中(checkState = 1)
		mCheckMap.put(id,1);
		//1
		List<Node> childrenOne = node.getChildren();
		if(null != childrenOne && !childrenOne.isEmpty()){
			for (Node node1: childrenOne) {
				mCheckMap.put(node1.getId(),1);
				//2
				List<Node> childrenTwo = node1.getChildren();
				if(null != childrenTwo && !childrenTwo.isEmpty()){
					for (Node node2: childrenOne) {
						mCheckMap.put(node2.getId(),1);
					}
				}
			}
		}

		//------------------遍历上级(2级)(checkState = 1 或 checkState = 2)---------------------------------------
		//1
		Node parentNode1 = node.getParent();
		if(null != parentNode1){
			List<Node> children = parentNode1.getChildren();
			boolean flag = true;
			for (Node node1: children) {
				if(node1.getId() != id) {//遍历不包含本身
					Integer checkState1 = mCheckMap.get(node1.getId());
					if(null == checkState1 || 0 == checkState1 || 2 == checkState1){
						flag = false;
						break;
					}
				}
			}
			if(flag){
				mCheckMap.put(parentNode1.getId(),1);
				//2
				Node parentNode2 = parentNode1.getParent();
				if(null != parentNode2){
					List<Node> children2 = parentNode2.getChildren();
					boolean flag2 = true;
					for (Node node2: children2) {
						if(node2.getId() != parentNode1.getId()) {//遍历不包含本身
							Integer checkState2 = mCheckMap.get(node2.getId());
							if(null == checkState2 || 0 == checkState2 || 2 == checkState2){
								flag2 = false;
								break;
							}
						}
					}
					if(flag2){
						mCheckMap.put(parentNode2.getId(),1);
					}else{
						mCheckMap.put(parentNode2.getId(),2);
					}
				}
			}else{
				mCheckMap.put(parentNode1.getId(),2);
			}
		}
	}

	//多选：没选中
	private void setMultipleUnchecked(int id, Node node) {
		mCheckMap.put(id,0);
		//遍历下级(2级)全部不选中(checkstate = 0)
		//1
		List<Node> childrenOne = node.getChildren();
		if(null != childrenOne && !childrenOne.isEmpty()){
            for (Node node1: childrenOne) {
                mCheckMap.put(node1.getId(),0);
                //2
                List<Node> childrenTwo = node1.getChildren();
                if(null != childrenTwo && !childrenTwo.isEmpty()){
                    for (Node node2: childrenOne) {
                        mCheckMap.put(node2.getId(),0);
                    }
                }
            }
        }

		//------------------遍历上级(2级)(checkState = 0 或 checkState = 2)---------------------------------------
		//1
		Node parentNode1 = node.getParent();
		if(null != parentNode1){
            List<Node> children = parentNode1.getChildren();
            boolean flag = true;
            for (Node node1: children) {
            	if(node1.getId() != id){//遍历不包含本身
					Integer checkState1 = mCheckMap.get(node1.getId());
					if(null != checkState1 && 1 == checkState1){
						flag = false;
						break;
					}
				}
            }
            if(flag){
                mCheckMap.put(parentNode1.getId(),0);
                //2
                Node parentNode2 = parentNode1.getParent();
                if(null != parentNode2){
                    List<Node> children2 = parentNode2.getChildren();
                    boolean flag2 = true;
                    for (Node node2: children2) {
						if(node2.getId() != parentNode1.getId()){//遍历不包含本身
							Integer checkState2 = mCheckMap.get(node2.getId());
							if(null != checkState2 && 1 == checkState2){
								flag2 = false;
								break;
							}
						}
                    }
                    if(flag2){
                        mCheckMap.put(parentNode2.getId(),0);
                    }else{
                        mCheckMap.put(parentNode2.getId(),2);
                    }
                }
            }else{
                mCheckMap.put(parentNode1.getId(),2);
                //2
                Node parentNode2 = parentNode1.getParent();
                if(null != parentNode2){
                    mCheckMap.put(parentNode2.getId(),2);
                }
            }
        }
	}

	//单选：选中
	private void setSingleChecked(int id) {
		if(null != mCheckMap){
			mCheckMap.clear();
			mCheckMap.put(id,1);
		}
	}
	//单选：没选中
	private void setSingleUnChecked(int id) {
		if(null != mCheckMap){
			mCheckMap.clear();
			mCheckMap.put(id,0);
		}
	}

}
