package com.teamappjobs.appjobs.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.teamappjobs.appjobs.R;

import java.util.List;

public class ListViewConfiguracoesAdapter extends BaseAdapter {
    Activity activity;
    List<String> mList;

    public ListViewConfiguracoesAdapter(Activity activity, List<String> mList){
        this.activity = activity;
        this.mList = mList;

    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = activity.getLayoutInflater().inflate(R.layout.item_list_configuracoes, parent, false);

        String option = mList.get(position);
        TextView textView = (TextView)view.findViewById(R.id.textView);
        textView.setText(option);

        ImageView imageView = (ImageView)view.findViewById(R.id.icon);

        switch (option){
            case "Perfil":
                imageView.setImageResource(R.drawable.ic_account_grey600_48dp);
                break;

            case "Conta":
                imageView.setImageResource(R.drawable.ic_account_key_grey600_48dp);
                break;
        }


        return view ;
    }
}
