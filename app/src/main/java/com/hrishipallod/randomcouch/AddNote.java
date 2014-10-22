package com.hrishipallod.randomcouch;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;


public class AddNote extends Activity {

    private static final String TAG = "YOOOOO";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        // create a manager
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void addNewNote(View view)
    {
        EditText text = (EditText)findViewById(R.id.editText);
        DbHandler db = new DbHandler(this);
        db.addNewNote(text.getText().toString(), "important");
        MixpanelAPI mixpanel = MixpanelAPI.getInstance(this, "7aed6c3effbfa78881596ab4da29a67b");
        JSONObject prop = new JSONObject();
        try {
            prop.put("Note length", String.valueOf(text.getText().toString().length()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mixpanel.track("New Note added", prop);
        mixpanel.flush();
        text.setText("");

    }
}
