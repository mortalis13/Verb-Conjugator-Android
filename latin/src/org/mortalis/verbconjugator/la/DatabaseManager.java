
package org.mortalis.verbconjugator.la;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseManager extends SQLiteAssetHelper {
  
  private static final String DATABASE_NAME = "verbs_it.db";
  
  private static final String VERBS_TABLE_NAME = "verbs";
  
  private static final String VERBS_COL_VERB = "verb";
  private static final String VERBS_COL_LOOKUP_WORD = "lookupWord";
  private static final String VERBS_COL_IMPERSONAL_ACTIVE = "impersonalActive";
  private static final String VERBS_COL_PRESENT_INDICATIVE_ACTIVE = "presentIndicativeActive";
  private static final String VERBS_COL_IMPERFECT_INDICATIVE_ACTIVE = "imperfectIndicativeActive";
  private static final String VERBS_COL_FUTURE1_INDICATIVE_ACTIVE = "future1IndicativeActive";
  private static final String VERBS_COL_PERFECT_INDICATIVE_ACTIVE = "perfectIndicativeActive";
  private static final String VERBS_COL_PLUPERFECT_INDICATIVE_ACTIVE = "pluperfectIndicativeActive";
  private static final String VERBS_COL_FUTURE2_INDICATIVE_ACTIVE = "future2IndicativeActive";
  private static final String VERBS_COL_PRESENT_CONJUNCTIVE_ACTIVE = "presentConjunctiveActive";
  private static final String VERBS_COL_IMPERFECT_CONJUNCTIVE_ACTIVE = "imperfectConjunctiveActive";
  private static final String VERBS_COL_PERFECT_CONJUNCTIVE_ACTIVE = "perfectConjunctiveActive";
  private static final String VERBS_COL_PLUPERFECT_CONJUNCTIVE_ACTIVE = "pluperfectConjunctiveActive";
  private static final String VERBS_COL_IMPERSONAL_PASSIVE = "impersonalPassive";
  private static final String VERBS_COL_PRESENT_INDICATIVE_PASSIVE = "presentIndicativePassive";
  private static final String VERBS_COL_IMPERFECT_INDICATIVE_PASSIVE = "imperfectIndicativePassive";
  private static final String VERBS_COL_FUTURE1_INDICATIVE_PASSIVE = "future1IndicativePassive";
  private static final String VERBS_COL_PERFECT_INDICATIVE_PASSIVE = "perfectIndicativePassive";
  private static final String VERBS_COL_PLUPERFECT_INDICATIVE_PASSIVE = "pluperfectIndicativePassive";
  private static final String VERBS_COL_FUTURE2_INDICATIVE_PASSIVE = "future2IndicativePassive";
  private static final String VERBS_COL_PRESENT_CONJUNCTIVE_PASSIVE = "presentConjunctivePassive";
  private static final String VERBS_COL_IMPERFECT_CONJUNCTIVE_PASSIVE = "imperfectConjunctivePassive";
  private static final String VERBS_COL_PERFECT_CONJUNCTIVE_PASSIVE = "perfectConjunctivePassive";
  private static final String VERBS_COL_PLUPERFECT_CONJUNCTIVE_PASSIVE = "pluperfectConjunctivePassive";
  private static final String VERBS_COL_IMPERATIVE_I = "imperativeI";
  private static final String VERBS_COL_IMPERATIVE_II = "imperativeII";
  private static final String VERBS_COL_SIMILAR_VERBS = "similarVerbs";
  
  private SQLiteDatabase db;
  
  
  public DatabaseManager(Context context) {
    super(context, DATABASE_NAME, null, 1);
    db = this.getReadableDatabase();
  }
  
  
  public Cursor getVerbsCursor(CharSequence text) {
    if (text == null) return null;
    String sql = String.format("SELECT rowid _id, %s FROM %s WHERE %s LIKE '?%'", VERBS_COL_VERB, VERBS_TABLE_NAME, VERBS_COL_VERB);
    Cursor cursor = db.rawQuery(sql, new String[] { text.toString() });
    return cursor;
  }
  
  public VerbItem getVerbItem(String verb) {
    Cursor cursor = db.query(VERBS_TABLE_NAME, null, VERBS_COL_VERB + "=?", new String[] { verb }, null, null, null);
    
    if (cursor != null && cursor.getCount() != 0) {
      cursor.moveToFirst();
      VerbItem item = new VerbItem();
      
      item.verb = cursor.getString(cursor.getColumnIndex(VERBS_COL_VERB));
      item.lookupWord = cursor.getString(cursor.getColumnIndex(VERBS_COL_LOOKUP_WORD));
      
      item.impersonalActive = cursor.getString(cursor.getColumnIndex(VERBS_COL_IMPERSONAL_ACTIVE));
      item.presentIndicativeActive = cursor.getString(cursor.getColumnIndex(VERBS_COL_PRESENT_INDICATIVE_ACTIVE));
      item.imperfectIndicativeActive = cursor.getString(cursor.getColumnIndex(VERBS_COL_IMPERFECT_INDICATIVE_ACTIVE));
      item.future1IndicativeActive = cursor.getString(cursor.getColumnIndex(VERBS_COL_FUTURE1_INDICATIVE_ACTIVE));
      item.perfectIndicativeActive = cursor.getString(cursor.getColumnIndex(VERBS_COL_PERFECT_INDICATIVE_ACTIVE));
      item.pluperfectIndicativeActive = cursor.getString(cursor.getColumnIndex(VERBS_COL_PLUPERFECT_INDICATIVE_ACTIVE));
      item.future2IndicativeActive = cursor.getString(cursor.getColumnIndex(VERBS_COL_FUTURE2_INDICATIVE_ACTIVE));
      
      item.presentConjunctiveActive = cursor.getString(cursor.getColumnIndex(VERBS_COL_PRESENT_CONJUNCTIVE_ACTIVE));
      item.imperfectConjunctiveActive = cursor.getString(cursor.getColumnIndex(VERBS_COL_IMPERFECT_CONJUNCTIVE_ACTIVE));
      item.perfectConjunctiveActive = cursor.getString(cursor.getColumnIndex(VERBS_COL_PERFECT_CONJUNCTIVE_ACTIVE));
      item.pluperfectConjunctiveActive = cursor.getString(cursor.getColumnIndex(VERBS_COL_PLUPERFECT_CONJUNCTIVE_ACTIVE));
      
      item.impersonalPassive = cursor.getString(cursor.getColumnIndex(VERBS_COL_IMPERSONAL_PASSIVE));
      item.presentIndicativePassive = cursor.getString(cursor.getColumnIndex(VERBS_COL_PRESENT_INDICATIVE_PASSIVE));
      item.imperfectIndicativePassive = cursor.getString(cursor.getColumnIndex(VERBS_COL_IMPERFECT_INDICATIVE_PASSIVE));
      item.future1IndicativePassive = cursor.getString(cursor.getColumnIndex(VERBS_COL_FUTURE1_INDICATIVE_PASSIVE));
      item.perfectIndicativePassive = cursor.getString(cursor.getColumnIndex(VERBS_COL_PERFECT_INDICATIVE_PASSIVE));
      item.pluperfectIndicativePassive = cursor.getString(cursor.getColumnIndex(VERBS_COL_PLUPERFECT_INDICATIVE_PASSIVE));
      item.future2IndicativePassive = cursor.getString(cursor.getColumnIndex(VERBS_COL_FUTURE2_INDICATIVE_PASSIVE));
      
      item.presentConjunctivePassive = cursor.getString(cursor.getColumnIndex(VERBS_COL_PRESENT_CONJUNCTIVE_PASSIVE));
      item.imperfectConjunctivePassive = cursor.getString(cursor.getColumnIndex(VERBS_COL_IMPERFECT_CONJUNCTIVE_PASSIVE));
      item.perfectConjunctivePassive = cursor.getString(cursor.getColumnIndex(VERBS_COL_PERFECT_CONJUNCTIVE_PASSIVE));
      item.pluperfectConjunctivePassive = cursor.getString(cursor.getColumnIndex(VERBS_COL_PLUPERFECT_CONJUNCTIVE_PASSIVE));
      
      item.imperativeI = cursor.getString(cursor.getColumnIndex(VERBS_COL_IMPERATIVE_I));
      item.imperativeII = cursor.getString(cursor.getColumnIndex(VERBS_COL_IMPERATIVE_II));
      item.similarVerbs = cursor.getString(cursor.getColumnIndex(VERBS_COL_SIMILAR_VERBS));
      
      return item;
    }
    
    return null;
  }
}
