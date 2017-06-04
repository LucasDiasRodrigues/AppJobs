package com.teamappjobs.appjobs.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.teamappjobs.appjobs.R;
import com.teamappjobs.appjobs.modelo.Categoria;

import java.util.List;

/**
 * Created by Lucas on 04/06/2017.
 */

public class ExpandableListViewCategoriasAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<Categoria> categorias;

    public ExpandableListViewCategoriasAdapter(Context context, List<Categoria> categorias) {
        this.context = context;
        this.categorias = categorias;
    }

    @Override
    public int getGroupCount() {
        return categorias.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return categorias.get(groupPosition).getSubCategorias().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return categorias.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return categorias.get(groupPosition).getSubCategorias().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return categorias.get(groupPosition).getCodCategoria();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return categorias.get(groupPosition).getSubCategorias().get(childPosition).getCodSubCat();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.expandable_categorias_group_item, parent, false);
        }

        ((TextView) convertView.findViewById(R.id.nameCategoria)).setText(categorias.get(groupPosition).getCategoria());

        if (isExpanded)
            ((TextView) convertView.findViewById(R.id.nameCategoria)).setTextColor(context.getResources().getColor(R.color.colorAccent));
        else
            ((TextView) convertView.findViewById(R.id.nameCategoria)).setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.expandable_categorias_child_item, parent, false);
        }

        ((TextView) convertView.findViewById(R.id.nameSubCategoria))
                .setText(categorias.get(groupPosition).getSubCategorias().get(childPosition).getSubCat());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
