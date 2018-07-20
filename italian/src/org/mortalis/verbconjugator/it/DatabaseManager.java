
package org.mortalis.verbconjugator.it;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract.Contacts;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseManager extends SQLiteAssetHelper  {
  
    SQLiteDatabase db;
  
    public DatabaseManager(Context context) {
        super(context, "verbs_it.db", null, 1);
        db = this.getReadableDatabase();
    }
    
    
    Cursor getVerbsCursor(CharSequence str) {
      if(str == null) return null;
      
//      String[] columns = {"rowid _id", "verb"};
//      Cursor cursor = db.query("verbs", columns, null, null, null, null, null, null);
      
      Cursor cursor =  db.rawQuery( "select rowid _id,verb from verbs where verb like '" + str + "%'", null );
//      Cursor cursor =  db.rawQuery( "select rowid _id,verb from verbs", null );
//      Cursor cursor =  db.rawQuery( "select verb from verbs", null );
      
//      String x = Contacts._ID;
      
      return cursor;
    }
    
    VerbItem getVerbItem(String verb) {
        Cursor cursor = db.query("verbs", null, 
                "verb='" + verb + "'", null,
                null, null, null, null);
        
        if (cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            VerbItem item = new VerbItem();
            
            item.verb = verb;
            item.impersonalForms = cursor.getString(1);
            
            item.presentIndicative = cursor.getString(2);
            item.imperfectIndicative = cursor.getString(3);
            item.futureIndicative = cursor.getString(4);
            item.pastRemoteIndicative = cursor.getString(5);
            item.pastIndicative = cursor.getString(6);
            item.pluperfectIndicative = cursor.getString(7);
            item.futurePerfectIndicative = cursor.getString(8);
            item.pluperfectRemoteIndicative = cursor.getString(9);
            
            item.presentSubjunctive = cursor.getString(10);
            item.imperfectSubjunctive = cursor.getString(11);
            item.pastSubjunctive = cursor.getString(12);
            item.pluperfectSubjunctive = cursor.getString(13);
            item.presentConditional = cursor.getString(14);
            item.pastConditional = cursor.getString(15);
            item.imperative = cursor.getString(16);
            item.similarVerbs = cursor.getString(17);

            return item;
        }
        
        return null;
    }
}
