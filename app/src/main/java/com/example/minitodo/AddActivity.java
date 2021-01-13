package com.example.minitodo;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.allyants.notifyme.NotifyMe;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    DBHelper dbHelper;
    ActionBar actionBar;
    EditText EtTitle,EtDesc,EtID,EtDate,EtTime;
    long id;
    Calendar now = Calendar.getInstance();
    TimePickerDialog tpd;
    DatePickerDialog dpd;
    SimpleDateFormat dateFormatter,timeFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        actionBar = getSupportActionBar();
        SetupActionBar();
        Initialize();
        id = getIntent().getLongExtra(DBHelper.ROW_ID,0);
        dbHelper = new DBHelper(this);
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        timeFormatter = new SimpleDateFormat("HH:mm", Locale.US);
        getData();

        dpd = DatePickerDialog.newInstance(
                AddActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        tpd = TimePickerDialog.newInstance(
                AddActivity.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                now.get(Calendar.SECOND),
                true
        );

        EtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dpd.show(getSupportFragmentManager(), "Datepickerdialog");
            }
        });
        EtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tpd.show(getSupportFragmentManager(),"Timepickerdialog");
            }
        });

    }

    private void getData() {
        Cursor cur = dbHelper.oneData(id);
        if(cur.moveToFirst()){
            String ID = cur.getString(cur.getColumnIndex(DBHelper.ROW_ID));
            String Title  = cur.getString(cur.getColumnIndex(DBHelper.ROW_TODO_TITLE));
            String Desc = cur.getString(cur.getColumnIndex(DBHelper.ROW_TODO_DESC));
            String Date = cur.getString(cur.getColumnIndex(DBHelper.ROW_DATE));
            String Time = cur.getString(cur.getColumnIndex(DBHelper.ROW_TIME));

            EtID.setText(ID);
            EtTitle.setText(Title);
            EtDesc.setText(Desc);
            EtDate.setText(Date);
            EtTime.setText(Time);

            if(EtID.equals("")){

            }else{

            }

        }
    }

    private void Initialize(){
        EtID = findViewById(R.id.et_id);
        EtTitle = findViewById(R.id.et_title);
        EtDesc = findViewById(R.id.et_desc);
        EtDate = findViewById(R.id.et_date);
        EtTime = findViewById(R.id.et_time);
    }

    protected void SetupActionBar(){
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_save_24);// set drawable icon
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                InsertAndUpdate();
                return true;
            case R.id.action_delete:
                Delete();
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void InsertAndUpdate(){
        String ID = EtID.getText().toString().trim();
        String Title = EtTitle.getText().toString().trim();
        String Desc = EtDesc.getText().toString().trim();
        String Date = EtDate.getText().toString().trim();
        String Time = EtTime.getText().toString().trim();

        ContentValues values = new ContentValues();

        values.put(DBHelper.ROW_TODO_TITLE,Title);
        values.put(DBHelper.ROW_TODO_DESC,Desc);
        values.put(DBHelper.ROW_DATE,Date);
        values.put(DBHelper.ROW_TIME,Time);

        if(Title.equals("")){
            Toast.makeText(AddActivity.this,"Isi Judul", Toast.LENGTH_SHORT).show();
        }else{
            if(ID.equals("")){
                dbHelper.addOne(values);
            }else{
                dbHelper.updateData(values,id);
            }

            Toast.makeText(AddActivity.this,"Data Tersimpan", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void Delete(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this,R.style.CustomAlertDialog);
        builder.setMessage("Data ini Akan dihapus");
        builder.setCancelable(true);
        builder.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dbHelper.deleteData(id);
                Toast.makeText(AddActivity.this,"Terhapus",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        now.set(Calendar.YEAR, year);
        now.set(Calendar.MONTH, monthOfYear);
        now.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        EtDate.setText(dateFormatter.format(now.getTime()));
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
            now.set(Calendar.HOUR_OF_DAY,hourOfDay);
            now.set(Calendar.MINUTE,minute);
            now.set(Calendar.SECOND,second);
            EtTime.setText(timeFormatter.format(now.getTime()));
            NotifyMe notifyMe = new NotifyMe.Builder(getApplicationContext())
                    .title(EtTitle.getText().toString())
                    .content(EtDesc.getText().toString())
                    .color(255,0,0,255)
                    .led_color(255,255,255,255)
                    .time(now)
                    .addAction(new Intent(),"Dismiss",true,false)
                    .key("test")
                    .addAction(new Intent(),"Dismiss",true,false)
                    .addAction(new Intent(),"Dismiss",true,false)
                    .large_icon(R.mipmap.ic_launcher_round)
                    .rrule("FREQ=MINUTELY;INTERVAL=5;COUNT=2")
                    .build();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.item_menu, menu);
        String ID = EtID.getText().toString().trim();
        MenuItem itemDelete = menu.findItem(R.id.action_delete) ;
        if(ID.equals("")){
            itemDelete.setVisible(false);
        }else{
            itemDelete.setVisible(true);
        }
        return true;
    }
}