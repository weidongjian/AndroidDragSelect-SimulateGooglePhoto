package com.weigan.googlephotoselect;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/7.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private static final int ITEM_COUNT = 150;

    private int colorSelect = Color.MAGENTA;
    private int colorNormal = Color.WHITE;

    private List<DataModel> items;

    private View.OnLongClickListener longClickListener;
    private View.OnClickListener clickListener;

    private int lastStart, lastEnd;

    public MyAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        items = new ArrayList<>();
        for (int i = 0; i < ITEM_COUNT; i++) {
            items.add(new DataModel(false, i));
        }
        resetStartAndEnd();
    }

    public void resetStartAndEnd() {
        lastStart = RecyclerView.NO_POSITION;
        lastEnd = RecyclerView.NO_POSITION;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_view, parent, false);
        view.setOnLongClickListener(longClickListener);
        view.setOnClickListener(clickListener);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DataModel item = items.get(position);
        if (item.isSelected()) {
            ViewCompat.setScaleX(holder.itemView, 0.8f);
            ViewCompat.setScaleY(holder.itemView, 0.8f);
            holder.textView.setTextColor(colorSelect);
        } else {
            ViewCompat.setScaleX(holder.itemView, 1f);
            ViewCompat.setScaleY(holder.itemView, 1f);
            holder.textView.setTextColor(colorNormal);
        }
        holder.textView.setText(String.valueOf(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setSelected(int position, boolean selected) {
        if (items.get(position).isSelected() != selected) {
            items.get(position).setSelected(selected);
            notifyItemChanged(position);
        }
    }

    public void removeItem(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    public void setLongClickListener(View.OnLongClickListener clickListener) {
        this.longClickListener = clickListener;
    }

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void selectRangeChange(int start, int end) {
        if (start == RecyclerView.NO_POSITION || end == RecyclerView.NO_POSITION) {
            return;
        }

        int newStart, newEnd;
        newStart = Math.min(start, end);
        newEnd = Math.max(start, end);
        if (lastStart == RecyclerView.NO_POSITION || lastEnd == RecyclerView.NO_POSITION) {
            if (newEnd - newStart == 1) {
                dataSelect(newEnd, newEnd);
            } else {
                dataSelect(newStart, newEnd);
            }
        } else {
            if (newStart > lastStart) {
                dataUnselect(lastStart, newStart - 1);
            } else if (newStart < lastStart) {
                dataSelect(newStart, lastStart - 1);
            }

            if (newEnd > lastEnd) {
                dataSelect(lastEnd + 1, newEnd);
            } else if (newEnd < lastEnd) {
                dataUnselect(newEnd + 1, lastEnd);
            }
        }

        lastStart = newStart;
        lastEnd = newEnd;
    }

    public int getSelectedSize() {
        if (items.isEmpty()) {
            return 0;
        }
        int result = 0;
        for (DataModel item : items) {
            if (item.isSelected()) {
                result++;
            }
        }
        return result;
    }

    private void dataSelect(int start, int end) {
        for (int i = start; i <= end; i++) {
            DataModel model = getItem(i);
            if (!model.isSelected()) {
                model.setSelected(true);
                notifyItemChanged(i);
            }
        }
    }

    private void dataUnselect(int start, int end) {
        for (int i = start; i <= end; i++) {
            DataModel model = getItem(i);
            if (model.isSelected()) {
                model.setSelected(false);
                notifyItemChanged(i);
            }
        }
    }


    public DataModel getItem(int i) {
        return items.get(i);
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private View parentView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
            parentView = itemView.findViewById(R.id.parent);
        }
    }
}
