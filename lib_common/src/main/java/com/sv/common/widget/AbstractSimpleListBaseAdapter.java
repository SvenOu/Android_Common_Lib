package com.sv.common.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.sv.common.util.WidgetUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sven-ou
 * 抽象的 view holder 适配器
 * 配合 CheckedLinelayout 实现单选或多选
 */
public abstract class AbstractSimpleListBaseAdapter<T> extends BaseAdapter{
    private static final String TAG = AbstractSimpleListBaseAdapter.class.getSimpleName();

    private Context context;
    private List<T> data = new ArrayList<>();
    protected Map<Integer, Boolean> selectStatus = new HashMap();
    private LayoutInflater layoutInflater;
    private ViewBinder viewBinder;

    public AbstractSimpleListBaseAdapter(Context context, List<T> data) {
        this.context = context;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
        this.viewBinder = new ViewBinder();
        if(null == this.data){
            this.data = new ArrayList<>();
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public T getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    protected abstract void onBindViewHolder(ViewBinder viewBinder);
    protected abstract void onBindView(T itemData,  Map viewHolder, View convertView, int position);

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        T itemData = getItem(position);
        Map viewHolder = null;
        if(null == convertView){
            viewHolder = new HashMap();
            onBindViewHolder(viewBinder);
            if(-1 == viewBinder.itemId || null == viewBinder.elementIds){
                throw  new RuntimeException("viewBinder.itemId and viewBinder.elementIds must be set !");
            }
            convertView = LayoutInflater.from(this.context).inflate(viewBinder.itemId, null);
            for(String resId: viewBinder.elementIds){
                String[] resIdAttr = resId.split("\\.");
                int id = WidgetUtil.getResourceId(context, resIdAttr[1], resIdAttr[0]);
                viewHolder.put(resId, convertView.findViewById(id));
            }
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (Map) convertView.getTag();
        }
        viewHolder.put("position",  position);
        onBindView(itemData, viewHolder, convertView, position);
        // 处理新生成不可见的点击
        onConvertViewSelect(convertView, null != selectStatus.get(position) && selectStatus.get(position),  null, -1);
        return convertView;
    }

    /**
     * 提供给使用者重写
     * select == false 的时候 position 不准确
     */
    protected void onConvertViewSelect(View convertView, boolean select, @Nullable T itemData, int position){
        convertView.setSelected(select);
    }

    protected static class ViewBinder{
         public int itemId = -1;
         public String[] elementIds;
    }

    public Context getContext() {
        return context;
    }

    public List<T> getData() {
        return data;
    }

    public LayoutInflater getLayoutInflater() {
        return layoutInflater;
    }

    public ViewBinder getViewBinder() {
        return viewBinder;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public void setViewBinder(ViewBinder viewBinder) {
        this.viewBinder = viewBinder;
    }
}
