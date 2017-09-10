package helper;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import model.Merchandise;

/**
 * Created by chong on 9/9/2017.
 */


public class MerchandiseLab {
    private static final String TAG = "MerchandiseLab";
    private static final String FILENAME = "merchandise.json";

    private ArrayList<Merchandise> merchandise_list;
    private MerchandiseJSONSerializer mSerializer;

    private static MerchandiseLab sMerchandiseLab;
    private Context mAppContext;

    private MerchandiseLab(Context appContext) {
        mAppContext = appContext;
        mSerializer = new MerchandiseJSONSerializer(mAppContext, FILENAME);

        try {
            merchandise_list = mSerializer.LoadMerchandiseList();
        } catch (Exception e) {
            merchandise_list = new ArrayList<Merchandise>();
            Log.e(TAG, "Error loading  merchandise: ", e);
        }
    }

    public static MerchandiseLab get(Context c) {
        if (sMerchandiseLab == null) {
            sMerchandiseLab = new MerchandiseLab(c.getApplicationContext());
        }
        return sMerchandiseLab;
    }

    public Merchandise getMerchandise(int id) {
        for (Merchandise m : merchandise_list) {
            if (m.getId() == id)
                return m;
        }
        return null;
    }

    public void AddMerchandise(Merchandise m) {
        if(ContainMerchandise(m)) return;
        merchandise_list.add(m);
        SaveMerchandise();
    }

    public void RemoveMerchandise(Merchandise m) {
        for(Merchandise item : merchandise_list){
            if(m.getId() == item.getId()){
                merchandise_list.remove(item);
                break;
            }
        }
        SaveMerchandise();
    }

    public boolean ContainMerchandise(Merchandise m) {
        if(m == null) return false;
        for(Merchandise item : merchandise_list){
            if(m.getId() == item.getId())
                return true;
        }
        return false;
    }

    public ArrayList<Merchandise> getMerchandiseList() {
        return merchandise_list;
    }

    public boolean SaveMerchandise() {
        try {
            mSerializer.SaveMerchandise(merchandise_list);
            Log.d(TAG, "Merchandises saved to file");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error saving Merchandises: " + e);
            return false;
        }
    }
}

