package km.lmy.searchview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

/**
 */
public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.SearchViewHolder> {

    private OnItemClickListener onItemClickListener;
    private int                 historyTextColor;
    private List<SearchBean>        list;

    SearchRecyclerViewAdapter(List<SearchBean> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sv_view_rv_item, parent, false);
        final SearchViewHolder viewHolder = new SearchViewHolder(view);
        if (onItemClickListener != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(SearchRecyclerViewAdapter.this, v, viewHolder.getLayoutPosition());
                }
            });
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.historyTextView.setText(list.get(position).getName());
        holder.historyTextView.setTextColor(historyTextColor);
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    List<SearchBean> getData() {
        return this.list;
    }

    void setNewData(List<SearchBean> newData) {
        this.list = newData;
        notifyDataSetChanged();
    }

    void addData(@NonNull SearchBean data) {
        this.list.add(data);
        notifyItemInserted(this.list.size());
    }

    void addData(@NonNull List<SearchBean> data) {
        int oldSize = this.list.size();
        this.list.addAll(data);
        notifyItemRangeInserted(oldSize - data.size(), data.size());
    }


    void setHistoryTextColor(int historyTextColor) {
        this.historyTextColor = historyTextColor;
    }

    void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class SearchViewHolder extends RecyclerView.ViewHolder {
        TextView  historyTextView;

        private SearchViewHolder(View itemView) {
            super(itemView);
            historyTextView = itemView.findViewById(R.id.tv_history_item);
        }
    }


    interface OnItemClickListener {
        void onItemClick(SearchRecyclerViewAdapter adapter, View view, int position);
    }
}
