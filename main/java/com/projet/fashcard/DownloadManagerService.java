package com.projet.fashcard;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.preference.PreferenceManager;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class DownloadManagerService extends Service {

    private DownloadManager dm;
    private long id2;
    private XmlPullParser parser;
    private String name = "import.xml";

    BroadcastReceiver onComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (id2 == reference) {
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
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        loadFromInternet();
    }

    private void loadFromInternet() {
        dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String adr = SP.getString("url", getString(R.string.dowload));
        Uri uri = Uri.parse(adr);
        DownloadManager.Request req = new DownloadManager.Request(uri);
        req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE).
                setDescription("Download file for database").
                setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name);

        id2 = dm.enqueue(req);
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(onComplete);
        super.onDestroy();
    }

    public void parseFile() throws XmlPullParserException, IOException {
        int event = parser.getEventType();
        long id = -1;
        String date = "";
        String question = "";
        LienProvider lien;
        while (event != XmlPullParser.END_DOCUMENT) {
            if (event == XmlPullParser.START_TAG) {
                switch (parser.getName()) {
                    case "jeu":
                        lien = new LienProvider(this);
                        Uri uri = lien.insertLienJeu(parser.getAttributeValue(0), parser.getAttributeValue(1));
                        id = ContentUris.parseId(uri);
                        break;
                    case "carte":
                        date = parser.getAttributeValue(0);
                        break;
                    case "question":
                        question = parser.nextText();
                        break;
                    case "reponse":
                        lien = new LienProvider(this);
                        lien.insertLienCarte(question, parser.nextText(), id);
                        break;
                }

            }
            event = parser.next();
        }
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
