package com.android.eddi.notepad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListaNoteAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Note> listaNote;

    public ListaNoteAdapter(Context context, int layout, ArrayList<Note> listaNote) {
        this.context = context;
        this.layout = layout;
        this.listaNote = listaNote;
    }

    @Override
    public int getCount() {
        return listaNote.size();
    }

    @Override
    public Object getItem(int position) {
        return listaNote.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView textViewTitolo, textViewContenuto;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder holder = new ViewHolder();

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Note note = listaNote.get(position);

        if (holder != null){
            holder.textViewTitolo.setText(note.getTitolo());
            holder.textViewContenuto.setText(note.getContenuto());
        }else {
        }
        

        return row;
    }
}
