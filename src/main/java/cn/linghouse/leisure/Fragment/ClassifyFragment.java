package cn.linghouse.leisure.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import cn.linghouse.leisure.R;

public class ClassifyFragment extends Fragment {
    private View view;
    private String[] data = {"测试数据","测试数据","测试数据","测试数据","测试数据","测试数据","测试数据",
            "测试数据","测试数据","测试数据","测试数据"};
    private ListView lvdirectory;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_classify,container,false);
        lvdirectory = view.findViewById(R.id.lv_directory);
        ArrayAdapter adapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,data);
        lvdirectory.setAdapter(adapter);
        return view;
    }
}
