package com.hrishipallod.randomcouch;

import android.content.Context;
import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Manager;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;
import com.couchbase.lite.android.AndroidContext;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hrishi on 22/10/14.
 */
public class DbHandler {
    private static final String TAG = "YOOOOOO ==>> ";
    private static final String DB_NAME = "notes";
    private Manager manager;
    private Database database;
    private String currentTimestamp;

    public DbHandler(Context context)
    {
        try {
            manager = new Manager(new AndroidContext(context), Manager.DEFAULT_OPTIONS);
            Log.d(TAG, "Manager created");
        } catch (IOException e) {
            Log.e(TAG, "Cannot create manager object");
            return;
        }
        // create a name for the database and make sure the name is legal
        if (!Manager.isValidDatabaseName(DB_NAME)) {
            Log.e(TAG, "Bad database name");
            return;
        }
// create a new database

        try {
            database = manager.getDatabase(DB_NAME);
            Log.d (TAG, "Database created");
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Cannot get database");
            return;
        }
    }

    public ArrayList<String> getAllNotes()
    {
//        String[] str = new String[50]; //{ "item1", "item2", "item3", "item4", "item5","item6" };
        //String [] str = new String[] {"sadf", "sdfasdf", "asdfasdf"};
        ArrayList<String> aR= new ArrayList<String>();

        int i=0;
        QueryRow qR;
        try {
            Query q = (Query) database.createAllDocumentsQuery();

            Document document;
            Map<String, Object> dp;
            //String = q.toLiveQuery();
            QueryEnumerator qE= null;
            try {
                qE = q.run();
            } catch (CouchbaseLiteException e) {
                e.printStackTrace();
            }
            while (qE.hasNext()) {

                qR = qE.next();
                document = qR.getDocument();
                Log.d(TAG, (String) document.getProperty("_id").toString());
                aR.add((String) document.getProperty("message").toString());
                //i++;
            }
        }
        catch(NullPointerException e)
        {
            Log.d(TAG, "SADNESS");
        }
        //Document document = row.getDocument();
        return aR;

    }
    public void addNewNote(String note, String type) {
        String timestamp = getCurrentTimestamp();
        Map<String, Object> docContent = new HashMap<String, Object>();
        docContent.put("message", note);
        docContent.put("type", type);
        docContent.put("creationDate", timestamp);
        Log.d(TAG, "docContent=" + String.valueOf(docContent));
        Document document = database.createDocument();
        try {
            document.putProperties(docContent);
            Log.d(TAG, "Document written to database named " + DB_NAME + " with ID = " + document.getId());
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Cannot write document to database", e);
        }
        /*
        String docID = document.getId();
        Document retrievedDocument = database.getDocument(docID);
        Log.d(TAG, "retrievedDocument=" + String.valueOf(retrievedDocument.getProperties()));
        */

    }

    public String getCurrentTimestamp() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Calendar calendar = GregorianCalendar.getInstance();
        return dateFormatter.format(calendar.getTime());
    }
}
