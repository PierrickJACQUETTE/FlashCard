package com.projet.fashcard;

import android.app.DownloadManager;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class DownloadManagerActivity extends Service {

    private static String authority = "com.project.fcContentProvider";
    private DownloadManager dm;
    private long id;
    private XmlPullParser parser;

    @Override
    public void onCreate() {
        super.onCreate();
        loadFromInternet();
        this.stopSelf();
    }

    private void loadFromInternet() {
        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        String name = "import.xml";
        String adr = "https://www.dropbox.com/s/iu500a7b5cpq1iw/import.xml?dl=1";
        Uri uri = Uri.parse(adr);
        DownloadManager.Request req = new DownloadManager.Request(uri);
        req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE).
                setDescription("Download file for database").setTitle("Notification").
                setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name);
        id = dm.enqueue(req);
        try {
            XmlPullParserFactory xmlFactory = XmlPullParserFactory.newInstance();
            File sdcard = Environment.getExternalStorageDirectory();
            File file = new File(sdcard, Environment.DIRECTORY_DOWNLOADS + "/" + name);
            FileInputStream inputstream = new FileInputStream(file);
            parser = xmlFactory.newPullParser();
            parser.setInput(inputstream, null);
            parseFile();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void parseFile() throws XmlPullParserException, IOException {
        int event = parser.getEventType();
        long id = -1;
        ContentResolver resolver = getContentResolver();
        ContentValues values = new ContentValues();
        Uri.Builder builder;
        Uri uri;
        while (event != XmlPullParser.END_DOCUMENT) {
            if (event == XmlPullParser.START_TAG) {
                switch (parser.getName()) {
                    case "jeu":
                        values = new ContentValues();
                        values.put("nom", parser.getAttributeValue(0));
                        builder = new Uri.Builder();
                        builder.scheme("content").authority(authority).appendPath("jeu_table");
                        uri = builder.build();
                        uri = resolver.insert(uri, values);
                        id = ContentUris.parseId(uri);
                        break;
                    case "question":
                        values = new ContentValues();
                        values.put("question", parser.nextText());
                        break;
                    case "reponse":
                        values.put("reponse", parser.nextText());
                        values.put("niveau", "facile");
                        values.put("jeu_id", id);
                        builder = new Uri.Builder();
                        builder.scheme("content").authority(authority).appendPath("carte_table");
                        uri = builder.build();
                        uri = resolver.insert(uri, values);
                        break;
                }

            }
            event = parser.next();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
