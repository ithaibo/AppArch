package com.andy.apparch;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andy.ui.recycler.HolderBinder;
import com.andy.ui.recycler.HolderFactory;
import com.andy.ui.recycler.HolderHelper;
import com.andy.ui.recycler.TypedAdapter;

import java.util.LinkedList;
import java.util.List;

public class DemoList extends AppCompatActivity {
    private List<String> raw;
    private List<String> listLess;
    private List<String> listMore;


    private RecyclerView recyclerView;
    private TypedAdapter<String> adapter;
    private TextView headerView;
    private TextView footerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_list);
        initData();

        initVies();
        showRaw(null);
    }

    private void initData() {
        raw = new LinkedList<>();
        listLess = new LinkedList<>();
        listMore = new LinkedList<>();
        int countLess = 3;
        int countRaw = 5;
        int countMore = 8;
        for (int i = 10; i < 10 + countLess; i++) {
            String item = String.valueOf(i);
            listLess.add(item);
        }
        for (int i = 1; i < countRaw; i++) {
            String item = String.valueOf(i);
            raw.add(item);
        }
        for (int i = 20; i < 20 + countMore; i++) {
            String item = String.valueOf(i);
            listMore.add(item);
        }
    }

    private void initVies() {
        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();
        recyclerView.setAdapter(adapter);
    }
    private void initAdapter() {
        if (null != adapter) {
            return;
        }
        adapter = new TypedAdapter.Builder<String>()
                .setHolderFactory(new HolderFactory() {
                    @Override
                    public RecyclerView.ViewHolder create(ViewGroup parent, int type) {
                        return new RecyclerView.ViewHolder(HolderHelper.inflateItemView(R.layout.item_list, parent)) {};
                    }
                })
                .setHolderBinder(new HolderBinder<String, RecyclerView.ViewHolder>() {
                    @Override
                    public void bind(@NonNull RecyclerView.ViewHolder holder, int position, String data) {
                        bindItem((TextView)holder.itemView, data);
                    }
                })
                .build();
        headerView = (TextView) HolderHelper.inflateItemView(R.layout.header_list, recyclerView);
        footerView = (TextView) HolderHelper.inflateItemView(R.layout.footer_list, recyclerView);
        adapter.addHeaderView(headerView, false);
        adapter.addFooterView(footerView, false);
    }


    public void showRaw(@Nullable View v) {
        adapter.updateDataList(raw);
//        adapter.getDataList().clear();
//        adapter.getDataList().addAll(raw);
//        adapter.updateItemCount();
//        adapter.notifyDataSetChanged();
        headerView.setText("header, raw list");
        footerView.setText("footer, raw list, size: " + adapter.getDataItemCount());
    }
    public void showLess(@Nullable View v) {
        adapter.updateDataList(listLess);
        headerView.setText("header, less list");
        footerView.setText("footer, less list, size: " + adapter.getDataItemCount());
    }
    public void showMore(@Nullable View v) {
        adapter.updateDataList(listMore);
        headerView.setText("header, more list");
        footerView.setText("footer, more list, size: " + adapter.getDataItemCount());
    }

    private void bindItem(TextView itemView, String data) {
        itemView.setText(data);
    }
}
