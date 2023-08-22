package cn.cls.listview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class BeanAdapter extends BaseAdapter {

    private final List<Bean> data;
    private final Context context;

    public BeanAdapter(List<Bean> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
            viewHolder.textView = convertView.findViewById(R.id.tv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        TextView textView = convertView.findViewById(R.id.tv);
        viewHolder.textView.setText(data.get(position).getName());

        Log.e("cls", "getView: " + position);

        return convertView;
    }

    private static class ViewHolder {
        TextView textView;
    }

}
