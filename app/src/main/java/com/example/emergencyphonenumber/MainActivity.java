package com.example.emergencyphonenumber;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.emergencyphonenumber.adapter.PhoneListAdapter;
import com.example.emergencyphonenumber.db.PhoneDbHelper;
import com.example.emergencyphonenumber.model.PhoneItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private PhoneDbHelper mHelper;
    private SQLiteDatabase mDb;

    private ArrayList<PhoneItem> mPhoneItemList = new ArrayList<>();
    private PhoneListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelper = new PhoneDbHelper(this);
        mDb = mHelper.getReadableDatabase();

        loadDataFromDb();
        mAdapter = new PhoneListAdapter(
                this,
                R.layout.item,
                mPhoneItemList
        );
        ListView lv = findViewById(R.id.list_view);
        lv.setAdapter(mAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                PhoneItem item = mPhoneItemList.get(position);
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+item.number));
                startActivity(intent);
            }
        });

        Button insertButton = findViewById(R.id.insert_button);
        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText phoneTitleEditText = findViewById(R.id.phone_title_editText);
                EditText phoneNumberEditText = findViewById(R.id.phone_number_editText);

                // todo: check EditText is null ?

                String phoneTitle = phoneTitleEditText.getText().toString();
                String phoneNumber = phoneNumberEditText.getText().toString();

                ContentValues cv = new ContentValues();
                cv.put(PhoneDbHelper.COL_TITLE, phoneTitle);
                cv.put(PhoneDbHelper.COL_NUMBER, phoneNumber);
                cv.put(PhoneDbHelper.COL_PICTURE, "ic_launcher.png");

                mDb.insert(PhoneDbHelper.TABLE_NAME, null, cv);
                loadDataFromDb();
                mAdapter.notifyDataSetChanged();
                phoneTitleEditText.setText("");
                phoneNumberEditText.setText("");

                // Delete row
                mDb.delete(
                        PhoneDbHelper.TABLE_NAME,
                        "number=? OR title=?",
                        new String[]{"999"}
                );

            }
        });
    }

    private void loadDataFromDb() {
        Cursor cursor = mDb.query(
                PhoneDbHelper.TABLE_NAME,
                null, // null == all
                null,// "category=1"
                null,
                null,
                null,
                null
        );

        mPhoneItemList.clear(); // Clear List

        while (cursor.moveToNext()){ // condition all row
            //int id = cursor.getInt(0);  First Coulumn is 0 per row
            int id = cursor.getInt(cursor.getColumnIndex(PhoneDbHelper.COL_ID)); // _id column
            //Log.i("Main", String.valueOf(id));
            String title = cursor.getString(cursor.getColumnIndex(PhoneDbHelper.COL_TITLE)); //Title Coulumn
            String number = cursor.getString(cursor.getColumnIndex(PhoneDbHelper.COL_NUMBER)); //Number Coulumn
            String picture = cursor.getString(cursor.getColumnIndex(PhoneDbHelper.COL_PICTURE)); //Image Coulumn

            PhoneItem item = new PhoneItem(id, title, number, picture);
            mPhoneItemList.add(item);
        }
    }
}
