//Author: Okemdi Udeh
//Student ID: S1903344

package org.me.gcu.equakestartercode;

//Import the necessary libraries
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

//Definition of MainActivity Class
public class MainActivity extends AppCompatActivity {
    Button fetch;
    RecyclerView recyclerView;
    ItemAdapter itemAdapter;

    public static List<Item> itemList = new ArrayList<>();
    ProgressDialog progressDialog;
    String text;
    String urlSource = "http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Locate the widgets in the activity and initialize them
        fetch = findViewById(R.id.fetch);
        recyclerView = findViewById(R.id.recycler_view);

        //Add an onClickListener to the button
        fetch.setOnClickListener(view -> {
            itemList = new ArrayList<>();
            new AsyncTaskExample().execute(urlSource);
        });

        if (savedInstanceState != null) {
            itemList.clear();
            itemList.addAll(savedInstanceState.getParcelableArrayList("key"));
            itemAdapter = new ItemAdapter(MainActivity.this, itemList);
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(itemAdapter);
        }
    }


    //Definition of AsyncTaskExample class to get the data from the url
    private class AsyncTaskExample extends AsyncTask<String, String, List<Item>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Inform the user that the data is being loaded using a progress dialog
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Fetching earthquake data...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected List<Item> doInBackground(String... strings) {
            int i = 0;
            Item item = null;
            URL url;
            URLConnection urlConnection;
            BufferedReader in = null;

            try {
                Log.e("MyTag", "in try");
                url = new URL(strings[0]);
                urlConnection = url.openConnection();
                in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                Log.e("MyTag", "after ready");

                try {
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser parser = factory.newPullParser();
                    parser.setInput(urlConnection.getInputStream(), null);
                    int eventType = parser.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        String tagName = parser.getName();
                        switch (eventType) {
                            case XmlPullParser.START_TAG:
                                if (tagName.equalsIgnoreCase("item")) {
                                    item = new Item();
                                }
                                break;

                            case XmlPullParser.TEXT:
                                text = parser.getText();
                                break;

                            case XmlPullParser.END_TAG:
                                if (item != null) {
                                    if (tagName.equalsIgnoreCase("title")) {
                                        item.setTitle(text);
                                    } else if (tagName.equalsIgnoreCase("description")) {
                                        String[] strings1 = text.split(";");
                                        String location = strings1[1].split(":")[1].trim();
                                        String[] latLong = strings1[2].split(":")[1].trim().split(",");
                                        String depth = strings1[3].replaceAll("[^\\d.]", "").replaceAll(":", "");
                                        String magnitude = strings1[4].replaceAll("[^\\d.]", "").replaceAll(":", "");
                                        item.setDescription(text);
                                        item.setLocation(location);
                                        item.setDepth(Double.parseDouble(depth));
                                        item.setMagnitude(Double.parseDouble(magnitude));
                                        item.setLatitude(Double.parseDouble(latLong[0]));
                                        item.setLongitude(Double.parseDouble(latLong[1]));
                                    } else if (tagName.equalsIgnoreCase("link")) {
                                        item.setLink(text);
                                    } else if (tagName.equalsIgnoreCase("pubDate")) {
                                        item.setPubDate(text);
                                    } else if (tagName.equalsIgnoreCase("category")) {
                                        item.setCategory(text);
                                    } else if (tagName.equalsIgnoreCase("item")) {
                                        i++;
                                        item.setId(i);
                                        Log.d("theS", "doInBackground: " + item.toString());
                                        itemList.add(item);
                                    }
                                }

                                break;

                            default:
                                break;
                        }
                        eventType = parser.next();
                    }

                    Collections.sort(itemList, new Comparator<Item>() {
                        public int compare(Item obj1, Item obj2) {
                            return Double.compare(obj2.getMagnitude(), obj1.getMagnitude());
                        }
                    });
                } catch (XmlPullParserException | IOException e) {
                    e.printStackTrace();
                }
                in.close();
            } catch (IOException ae) {
                Log.e("MyTag", "ioexception in run");
            }
            return itemList;
        }

        @Override
        protected void onPostExecute(List<Item> itemList) {
            super.onPostExecute(itemList);
            if (itemList != null) {
                progressDialog.hide();

                itemAdapter = new ItemAdapter(MainActivity.this, itemList);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
                recyclerView.setAdapter(itemAdapter);
            } else {
                progressDialog.show();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("key", new ArrayList<Item>(itemList));

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        itemList.clear();
        itemList.addAll(savedInstanceState.getParcelableArrayList("key"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_filter:
                Intent intent = new Intent(MainActivity.this, FilterActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}