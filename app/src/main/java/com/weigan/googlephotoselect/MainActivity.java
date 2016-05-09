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

        //监听滑动选择
        mRecyclerView.addOnItemTouchListener(touchListener);

        touchListener.setSelectListener(new DragSelectTouchListener.onSelectListener() {
            @Override
            public void onSelect(int start, int end) {
                //选择的范围回调
                adapter.selectRangeChange(start, end);
                actionBar.setTitle(String.valueOf(adapter.getSelectedSize()) + " selected");
            }

            @Override
            public void reset() {
                //长按只触发一次滑动选择，结束后需要初始化相关的值
                adapter.resetStartAndEnd();
            }
        });

        RecyclerView.ItemAnimator itemAnimator = new SelectItemAnimator();
        //设置选择状态切换时候的动画执行时间
        itemAnimator.setChangeDuration(300);

        mRecyclerView.setItemAnimator(itemAnimator);
    }
}
