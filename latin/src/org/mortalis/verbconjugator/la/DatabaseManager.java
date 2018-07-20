
package org.mortalis.verbconjugator.la;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract.Contacts;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseManager extends SQLiteAssetHelper  {
  
    SQLiteDatabase db;
  
    public DatabaseManager(Context context) {
        super(context, "verbs_la.db", null, 1);
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
            
            item.lookupWord = cursor.getString(1);
            
            item.impersonalActive = cursor.getString(2);
            item.presentIndicativeActive = cursor.getString(3);
            item.imperfectIndicativeActive = cursor.getString(4);
            item.future1IndicativeActive = cursor.getString(5);
            item.perfectIndicativeActive = cursor.getString(6);
            item.pluperfectIndicativeActive = cursor.getString(7);
            item.future2IndicativeActive = cursor.getString(8);
            
            item.presentConjunctiveActive = cursor.getString(9);
            item.imperfectConjunctiveActive = cursor.getString(10);
            item.perfectConjunctiveActive = cursor.getString(11);
            item.pluperfectConjunctiveActive = cursor.getString(12);
            
            item.impersonalPassive = cursor.getString(13);
            item.presentIndicativePassive = cursor.getString(14);
            item.imperfectIndicativePassive = cursor.getString(15);
            item.future1IndicativePassive = cursor.getString(16);
            item.perfectIndicativePassive = cursor.getString(17);
            item.pluperfectIndicativePassive = cursor.getString(18);
            item.future2IndicativePassive = cursor.getString(19);
            
            item.presentConjunctivePassive = cursor.getString(20);
            item.imperfectConjunctivePassive = cursor.getString(21);
            item.perfectConjunctivePassive = cursor.getString(22);
            item.pluperfectConjunctivePassive = cursor.getString(23);
            
            item.imperativeI = cursor.getString(24);
            item.imperativeII = cursor.getString(25);
            item.similarVerbs = cursor.getString(26);

            return item;
        }
        
        return null;
    }
}
