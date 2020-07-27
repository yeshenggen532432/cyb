package com.qwb.view.ware.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.chiyong.t3.R;
import com.zhy.tree.bean.Node;
import com.zhy.tree.bean.TreeHelper;

import java.util.List;

/**
 * @功能描述：选择商品 参考：TreeListViewAdapter ，因TreeListViewAdapter如果改变数据不能刷新，才重写（如果数据没改变的，继承TreeListViewAdapter就可以）
 * @修改作者： 
 * @修改时间：
 * @修改描述：
 */
public class WareTreeAdapter<T> extends BaseAdapter {
	public int mId=-1;//记录当前点击的item，颜色变色（-1常售商品）

	public Context mContext;
	public List<Node> mNodes;
	public LayoutInflater mInflater;
	public List<Node> mAllNodes;
	public OnTreeNodeClickListener onTreeNodeClickListener;

	public WareTreeAdapter(ListView mTree, Context context, List<T> datas, int defaultExpandLevel) throws IllegalArgumentException, IllegalAccessException {
		this.mContext = context;
		this.mAllNodes = TreeHelper.getSortedNodes(datas, defaultExpandLevel);
		this.mNodes = TreeHelper.filterVisibleNode(this.mAllNodes);
		this.mInflater = LayoutInflater.from(context);
		mTree.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				WareTreeAdapter.this.expandOrCollapse(position);
				if(WareTreeAdapter.this.onTreeNodeClickListener != null) {
					WareTreeAdapter.this.onTreeNodeClickListener.onClick((Node)WareTreeAdapter.this.mNodes.get(position), position);
				}

			}
		});
	}

	public void expandOrCollapse(int position) {
		Node n = this.mNodes.get(position);
		if(n != null && !n.isLeaf()) {
			n.setExpand(!n.isExpand());
			this.mNodes = TreeHelper.filterVisibleNode(this.mAllNodes);
			this.notifyDataSetChanged();
		}

	}

	public int getCount() {
		return this.mNodes.size();
	}

	public Object getItem(int position) {
		return this.mNodes.get(position);
	}

	public long getItemId(int position) {
		return (long)position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Node node = this.mNodes.get(position);
		convertView = this.getConvertView(node, position, convertView, parent);
		//上级与下级之间的间距
		convertView.setPadding(node.getLevel() * 30, 3, 3, 3);
		return convertView;
	}

	public void setOnTreeNodeClickListener(OnTreeNodeClickListener onTreeNodeClickListener) {
		this.onTreeNodeClickListener = onTreeNodeClickListener;
	}
	public interface OnTreeNodeClickListener {
		void onClick(Node var1, int var2);
	}

	public View getConvertView(final Node node, int position, View convertView, ViewGroup parent) {
		convertView = mInflater.inflate(R.layout.x_tree_item_choose_shop, parent, false);
		TextView label = convertView.findViewById(R.id.id_treenode_label);

		label.setText(node.getName());

		if(node.getId() == mId){
			//选中
			label.setTextColor(Color.parseColor("#0082CE"));
		}else{
			int level = node.getLevel();
			if(0 == level){
				label.setTextColor(Color.parseColor("#333333"));
			}else if(1 == level){
				label.setTextColor(Color.parseColor("#666666"));
			}else if(2 == level){
				label.setTextColor(Color.parseColor("#999999"));
			}
//			if(node.isLeaf()){
//				//最后一层
//				label.setTextColor(Color.parseColor("#666666"));
//			}else{
//				label.setTextColor(Color.parseColor("#333333"));
//			}

		}

		return convertView;
	}

	public int getmId() {
		return mId;
	}

	public void setmId(int mId) {
		this.mId = mId;
	}

	public List<Node> getNodes() {
		return mNodes;
	}

	public void setNodes(List<T> datas, int defaultExpandLevel) {
		try {
			this.mAllNodes = TreeHelper.getSortedNodes(datas, defaultExpandLevel);
			this.mNodes = TreeHelper.filterVisibleNode(this.mAllNodes);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
