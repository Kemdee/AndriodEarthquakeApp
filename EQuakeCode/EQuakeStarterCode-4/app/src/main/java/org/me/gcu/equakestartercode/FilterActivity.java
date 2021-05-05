//Author: Okemdi Udeh
//Student ID: S1903344

package org.me.gcu.equakestartercode;

//Import the necessary libraries

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;


//Definition of FilterActivity class
public class FilterActivity extends AppCompatActivity {

    //Define variables
    TextView specificDate, startRange, endRange;
    Button apply;
    RadioButton specificOption, rangeOption;
    RecyclerView recyclerView;
    FilterAdapter filterAdapter;
    List<Item> itemList = new ArrayList<>();


    int highestLatitudeId, lowestLatitudeId, highestLongitudeId, lowestLongitudeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        itemList.addAll(MainActivity.itemList);

        //Add a back icon in the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Locate the widgets in the activity and initialize them
        specificOption = findViewById(R.id.specificOption);
        rangeOption = findViewById(R.id.rangeOption);
        specificDate = findViewById(R.id.specificDate);
        startRange = findViewById(R.id.startRange);
        endRange = findViewById(R.id.endRange);
        apply = findViewById(R.id.apply);
        recyclerView = findViewById(R.id.recycler_view);

        //Check if the list is empty
        if (itemList.size() > 0) {
            //Sort the list according to magnitude
            arrangeList();
            //Set a FilterAdapter on the recyclerView
            filterAdapter = new FilterAdapter(FilterActivity.this, itemList, getHighestIndex(), getLowestIndex(), highestLatitudeId, lowestLatitudeId, highestLongitudeId, lowestLongitudeId);
            recyclerView.setLayoutManager(new LinearLayoutManager(FilterActivity.this, LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(filterAdapter);
        }


        //Set onClickListeners on the radio buttons
        specificOption.setOnClickListener(view -> rangeOption.setChecked(false));
        rangeOption.setOnClickListener(view -> specificOption.setChecked(false));

        //Set onClickListeners for the start and end dates
        specificDate.setOnClickListener(view -> datePickerDialog(0));
        startRange.setOnClickListener(view -> datePickerDialog(1));
        endRange.setOnClickListener(view -> datePickerDialog(2));

        //Define functionality of apply button
        apply.setOnClickListener(view -> {
            if (specificOption.isChecked()) {
                if (TextUtils.isEmpty(specificDate.getText().toString())) {
                    Toast.makeText(FilterActivity.this, "Please specify a date", Toast.LENGTH_SHORT).show();
                } else {
                    getSingleDateData();
                }
            } else if (rangeOption.isChecked()) {
                if (TextUtils.isEmpty(startRange.getText().toString())) {
                    Toast.makeText(FilterActivity.this, "Please select a start date", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(endRange.getText().toString())) {
                    Toast.makeText(FilterActivity.this, "Please select an end date", Toast.LENGTH_SHORT).show();
                } else {
                    getRangeDateData();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(FilterActivity.this, MainActivity.class));
        finish();

    }

    //Create a datePickerDialog for selecting the date
    public void datePickerDialog(int index) {
        DatePickerDialog mDatePicker;
        final Calendar mCalendar = Calendar.getInstance();

        mDatePicker = new DatePickerDialog(FilterActivity.this, (datePicker, year, monthOfYear, dayOfMonth) -> {
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, monthOfYear);
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String formattedDate = new SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault()).format(mCalendar.getTime());
            if (index == 0) {
                specificDate.setText(formattedDate);
            } else if (index == 1) {
                startRange.setText(formattedDate);
            } else if (index == 2) {
                endRange.setText(formattedDate);
            }
        }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
        mDatePicker.show();
    }

    //Method for checking if  both dates are equal
    public boolean isDateEquals(String eqDateStr, String newDateStr) {
        boolean result = false;
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy");
        try {
            Date eqDate = sdf.parse(eqDateStr);
            Date newDate = sdf.parse(newDateStr);
            result = newDate.equals(eqDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    //Method for checking if selected dates are within the range
    public boolean isDateEqualToRange(String eqDateStr, String startDateStr, String endDateStr) {
        boolean result = false;
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy");
        try {
            Date eqDate = sdf.parse(eqDateStr);
            Date startDate = sdf.parse(startDateStr);
            Date endDate = sdf.parse(endDateStr);
            result = (eqDate.equals(startDate) || eqDate.after(startDate)) && (eqDate.equals(endDate) || eqDate.before(endDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    //Method for retrieving the selected date
    public void getSingleDateData() {
        String eqDateStr = specificDate.getText().toString();
        itemList.clear();
        itemList.addAll(MainActivity.itemList);
        List<Item> newItemList = new ArrayList<>();
        for (Item item : itemList) {
            if (isDateEquals(item.getPubDate(), eqDateStr)) {
                newItemList.add(item);
            }
        }
        if (newItemList.size() > 0) {
            itemList.clear();
            itemList.addAll(newItemList);
            arrangeList();
            filterAdapter.reloadData(getHighestIndex(), getLowestIndex(), highestLatitudeId, lowestLatitudeId, highestLongitudeId, lowestLongitudeId);
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(FilterActivity.this).create();
            alertDialog.setTitle("Result");
            alertDialog.setMessage("No earthquakes were recorded on " + eqDateStr);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    (dialog, which) -> dialog.dismiss());
            alertDialog.show();
            itemList.clear();
        }

        filterAdapter.notifyDataSetChanged();

    }

    //Method for retrieving the selected range of dates
    public void getRangeDateData() {
        String startDateStr = startRange.getText().toString();
        String endDateStr = endRange.getText().toString();
        itemList.clear();
        itemList.addAll(MainActivity.itemList);
        List<Item> newItemList = new ArrayList<>();

        for (Item item : itemList) {
            if (isDateEqualToRange(item.getPubDate(), startDateStr, endDateStr)) {
                newItemList.add(item);
            }
        }
        if (newItemList.size() > 0) {
            itemList.clear();
            itemList.addAll(newItemList);
            arrangeList();
            filterAdapter.reloadData(getHighestIndex(), getLowestIndex(), highestLatitudeId, lowestLatitudeId, highestLongitudeId, lowestLongitudeId);

        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(FilterActivity.this).create();
            alertDialog.setTitle("Result");
            alertDialog.setMessage("No earthquakes were recorded on " + startDateStr + " , " + endDateStr);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    (dialog, which) -> dialog.dismiss());
            alertDialog.show();
            itemList.clear();
        }

        filterAdapter.notifyDataSetChanged();

    }

    //Method to arrange the list according to magnitude
    public void arrangeList() {
        Collections.sort(itemList, (obj1, obj2) -> Double.compare(obj2.getMagnitude(), obj1.getMagnitude()));

        getLatitudeId();

        getLongitudeId();
    }

    //Method to get the depth highest value index
    public int getHighestIndex() {
        double highest = itemList.get(0).getDepth();
        int highestIndex = 0;

        for (int s = 1; s < itemList.size(); s++) {
            double curValue = itemList.get(s).getDepth();
            if (curValue > highest) {
                highest = curValue;
                highestIndex = s;
            }
        }
        return highestIndex;
    }

    //Method to get the depth lowest value index
    public int getLowestIndex() {
        double lowest = itemList.get(0).getDepth();
        int lowestIndex = 0;

        for (int s = 1; s < itemList.size(); s++) {
            double curValue = itemList.get(s).getDepth();
            if (curValue < lowest) {
                lowest = curValue;
                lowestIndex = s;
            }
        }
        return lowestIndex;
    }

    //Method to get the highest and lowest latitude IDs
    public void getLatitudeId() {
        double highest = itemList.get(0).getLatitude();
        double lowest = itemList.get(0).getLatitude();
        highestLatitudeId = itemList.get(0).getId();
        lowestLatitudeId = itemList.get(0).getId();

        for (int s = 1; s < itemList.size(); s++) {
            double curValue = itemList.get(s).getLatitude();
            if (curValue > highest) {
                highest = curValue;
                highestLatitudeId = itemList.get(s).getId();
            }
            if (curValue < lowest) {
                lowest = curValue;
                lowestLatitudeId = itemList.get(s).getId();
            }
        }
    }

    //Method to get the highest and lowest longitude IDs
    public void getLongitudeId() {

        double highest = itemList.get(0).getLongitude();
        double lowest = itemList.get(0).getLongitude();
        highestLongitudeId = itemList.get(0).getId();
        lowestLongitudeId = itemList.get(0).getId();

        for (int s = 1; s < itemList.size(); s++) {
            double curValue = itemList.get(s).getLongitude();
            if (curValue > highest) {
                highest = curValue;
                highestLongitudeId = itemList.get(s).getId();
            }
            if (curValue < lowest) {
                lowest = curValue;
                lowestLongitudeId = itemList.get(s).getId();
            }
        }
    }
}