package com.projet.fashcard;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ListeJeuxFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static String authority = "com.project.fcContentProvider";
    private OnFragmentInteractionListener mListener;
    private SimpleCursorAdapter adapter;
    private LoaderManager manager;

    public ListeJeuxFragment() {
    }

    public static ListeJeuxFragment newInstance() {
        ListeJeuxFragment fragment = new ListeJeuxFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1, null, new String[]{"nom"}, new int[]{android.R.id.text1}, 0);
        setListAdapter(adapter);
        manager = getLoaderManager();
        manager.initLoader(0, null, this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri;
        Uri.Builder builder = new Uri.Builder();
        uri = builder.scheme("content").authority(authority).appendPath("jeu_table").build();
        return new CursorLoader(getActivity(), uri, new String[]{"_id", "nom"}, null, null, null);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (mListener != null) {
            mListener.onJeuxSelection(id);
        }
    }

    public interface OnFragmentInteractionListener {
        void onJeuxSelection(long index);

        void onFragmentInteraction(Uri uri);
    }
}
