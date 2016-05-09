package com.weigan.googlephotoselect;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private MyAdapter adapter;
    private DragSelectTouchListener touchListener;

    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar = getSupportActionBar();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new MyAdapter(this);
        mRecyclerView.setAdapter(adapter);

        adapter.setLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = mRecyclerView.getChildAdapterPosition(v);
                adapter.setSelected(position, true);
                touchListener.setStartSelectPosition(position);
                return false;
            }
        });

        adapter.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mRecyclerView.getChildAdapterPosition(v);
                adapter.setSelected(position, true);
            }
        });

        touchListener = new DragSelectTouchListener();

        mRecyclerView.addOnItemTouchListener(touchListener);

        touchListener.setSelectListener(new DragSelectTouchListener.onSelectListener() {
            @Override
            public void onSelect(int start, int end) {
                adapter.selectRangeChange(start, end);
                actionBar.setTitle(String.valueOf(adapter.getSelectedSize()) + " selected");
            }

            @Override
            public void reset() {
                adapter.resetStartAndEnd();
            }
        });

        RecyclerView.ItemAnimator itemAnimator = new SelectItemAnimator();
        itemAnimator.setChangeDuration(300);

        mRecyclerView.setItemAnimator(itemAnimator);
    }
}
