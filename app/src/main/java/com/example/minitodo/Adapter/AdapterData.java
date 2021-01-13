package com.example.minitodo.Adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.minitodo.DBHelper;
import com.example.minitodo.R;

import static android.view.View.GONE;

public class AdapterData extends CursorAdapter {
    private LayoutInflater ly;
    private SparseBooleanArray mSelectedItems;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public AdapterData(Context context, Cursor c, int flags) {
        super(context, c, flags);
        ly = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSelectedItems=new SparseBooleanArray();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View v = ly.inflate(R.layout.row_item,viewGroup,false);
        MyHolder holder = new MyHolder();

        holder.TvID = v.findViewById(R.id.tv_id);
        holder.TvTitle = v.findViewById(R.id.tv_CBox);
        holder.TvDesc = v.findViewById(R.id.tv_desc);
        holder.TvAlarm = v.findViewById(R.id.tv_alarm);

        v.setTag(holder);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        MyHolder holder = (MyHolder)view.getTag();
        holder.TvID.setText(cursor.getString(cursor.getColumnIndex(DBHelper.ROW_ID)));
        holder.TvTitle.setText(cursor.getString(cursor.getColumnIndex(DBHelper.ROW_TODO_TITLE)));
        holder.TvDesc.setText(cursor.getString(cursor.getColumnIndex(DBHelper.ROW_TODO_DESC)));
        holder.TvAlarm.setText(cursor.getString(cursor.getColumnIndex(DBHelper.ROW_DATE))+
                " " + cursor.getString(cursor.getColumnIndex(DBHelper.ROW_TIME)));


    }

    class MyHolder{
        TextView TvID;
        CheckBox TvTitle;
        TextView TvDesc;
        TextView TvAlarm;

    }
}
