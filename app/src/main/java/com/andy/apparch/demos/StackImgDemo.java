package com.andy.apparch.demos;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andy.apparch.R;
import com.andy.ui.recycler.HolderBinder;
import com.andy.ui.recycler.HolderFactory;
import com.andy.ui.recycler.HolderHelper;
import com.andy.ui.recycler.TypedAdapter;

import java.util.LinkedList;
import java.util.List;


public class StackImgDemo extends AppCompatActivity {

    private RecyclerView list;
    private TypedAdapter<FundManager> adapter;

    private ListView listView;
    private AdapterList adapterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stack_img_demo);

        list = findViewById(R.id.list);
        list.setVisibility(View.GONE);
//        initList();

//        loadMockData();

        listView = findViewById(R.id.list_view);
        adapterList = new AdapterList();
        listView.setAdapter(adapterList);
        adapterList.dataList = mockDataList();
        adapterList.notifyDataSetChanged();
    }

    private void loadMockData() {
        adapter.updateDataList(mockDataList());
    }

    private void initList() {
        list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TypedAdapter.Builder<FundManager>()
                .setHolderFactory(new HolderFactory() {
                    @Override
                    public RecyclerView.ViewHolder create(ViewGroup parent, int type) {
                        return new RecyclerView.ViewHolder(
                                HolderHelper.inflateItemView(
                                        R.layout.list_item_stack_img,
                                        parent)
                        ){};
                    }
                })
                .setHolderBinder(new HolderBinder<FundManager, RecyclerView.ViewHolder>() {
                    @Override
                    public void bind(@NonNull RecyclerView.ViewHolder holder,
                                     int position,
                                     FundManager data) {
                        renderItem(holder.itemView, data);
                    }
                })
                .build();

        list.setAdapter(adapter);
    }

    static void renderItem(@NonNull View itemView, @NonNull FundManager data) {
        TextView tvName = itemView.findViewById(R.id.tv_name);
        tvName.setText(data.name);

        StackFrame stackBgView = itemView.findViewById(R.id.stack_bg_view);
        stackBgView.setStackSize(data.count);

        TextView tvCount = itemView.findViewById(R.id.tv_count);
        tvCount.setText(String.format("count:%d", data.count));
    }


    private static List<FundManager> mockDataList() {
        List<FundManager> list = new LinkedList<>();
        for (int i =0; i < 30; i++) {
            FundManager fundManager = new FundManager();
            fundManager.name = "Manager-" + i;
            fundManager.count = i % 5;
            list.add(fundManager);
        }
        return list;
    }
}
