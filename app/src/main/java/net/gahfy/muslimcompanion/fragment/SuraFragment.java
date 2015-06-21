package net.gahfy.muslimcompanion.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.gahfy.muslimcompanion.DbManager;
import net.gahfy.muslimcompanion.R;
import net.gahfy.muslimcompanion.adapter.AyaListAdapter;

public class SuraFragment extends AbstractFragment{
    RecyclerView listAya;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_sura, container, false);

        listAya = (RecyclerView) fragmentView.findViewById(R.id.list_aya);

        listAya.setLayoutManager(new LinearLayoutManager(getActivity()));

        return fragmentView;
    }

    @Override
    public void onStart(){
        super.onStart();
        try {
            DbManager dbManager = new DbManager(getActivity());
            dbManager.createDataBase();
            dbManager.openDataBase();
            SQLiteDatabase db = dbManager.getDb();
            String query = "SELECT text FROM quran WHERE sura = 2 ORDER BY aya ASC;";
            Cursor c = db.rawQuery(query, null);
            if(c.getCount() > 0) {
                String[] ayaList = new String[c.getCount()];
                c.moveToFirst();
                for(int i=0; i<c.getCount(); i++) {
                    ayaList[i] = c.getString(0);
                    c.moveToNext();
                }
                listAya.setAdapter(new AyaListAdapter(ayaList));
            }
            c.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
