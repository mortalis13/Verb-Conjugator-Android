
package org.mortalis.verbconjugator.it;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseManager extends SQLiteAssetHelper {
  
  private static final String DATABASE_NAME = "verbs_it.db";
  
  private static final String VERBS_TABLE_NAME = "verbs";
  
  private static final String VERBS_COL_VERB = "verb";
  private static final String VERBS_COL_IMPERSONAL_FORMS = "impersonalForms";
  private static final String VERBS_COL_PRESENT_INDICATIVE = "presentIndicative";
  private static final String VERBS_COL_IMPERFECT_INDICATIVE = "imperfectIndicative";
  private static final String VERBS_COL_FUTURE_INDICATIVE = "futureIndicative";
  private static final String VERBS_COL_PAST_REMOTE_INDICATIVE = "pastRemoteIndicative";
  private static final String VERBS_COL_PAST_INDICATIVE = "pastIndicative";
  private static final String VERBS_COL_PLUPERFECT_INDICATIVE = "pluperfectIndicative";
  private static final String VERBS_COL_FUTURE_PERFECT_INDICATIVE = "futurePerfectIndicative";
  private static final String VERBS_COL_PLUPERFECT_REMOTE_INDICATIVE = "pluperfectRemoteIndicative";
  private static final String VERBS_COL_PRESENT_SUBJUNCTIVE = "presentSubjunctive";
  private static final String VERBS_COL_IMPERFECT_SUBJUNCTIVE = "imperfectSubjunctive";
  private static final String VERBS_COL_PAST_SUBJUNCTIVE = "pastSubjunctive";
  private static final String VERBS_COL_PLUPERFECT_SUBJUNCTIVE = "pluperfectSubjunctive";
  private static final String VERBS_COL_PRESENT_CONDITIONAL = "presentConditional";
  private static final String VERBS_COL_PAST_CONDITIONAL = "pastConditional";
  private static final String VERBS_COL_IMPERATIVE = "imperative";
  private static final String VERBS_COL_SIMILAR_VERBS = "similarVerbs";
  
  private SQLiteDatabase db;
  
  
  public DatabaseManager(Context context) {
    super(context, DATABASE_NAME, null, 1);
    db = this.getReadableDatabase();
  }
  
  
  public Cursor getVerbsCursor(CharSequence text) {
    if (text == null) return null;
    String sql = String.format("SELECT rowid _id, %s FROM %s WHERE %s LIKE ?", VERBS_COL_VERB, VERBS_TABLE_NAME, VERBS_COL_VERB);
    Cursor cursor = db.rawQuery(sql, new String[] {text.toString() + "%"});
    return cursor;
  }
  
  public VerbItem getVerbItem(String verb) {
    Cursor cursor = db.query(VERBS_TABLE_NAME, null, VERBS_COL_VERB + "=?", new String[] { verb }, null, null, null);
    
    if (cursor != null && cursor.getCount() != 0) {
      cursor.moveToFirst();
      VerbItem item = new VerbItem();
      
      item.verb = cursor.getString(cursor.getColumnIndex(VERBS_COL_VERB));
      item.impersonalForms = cursor.getString(cursor.getColumnIndex(VERBS_COL_IMPERSONAL_FORMS));
      
      item.presentIndicative = cursor.getString(cursor.getColumnIndex(VERBS_COL_PRESENT_INDICATIVE));
      item.imperfectIndicative = cursor.getString(cursor.getColumnIndex(VERBS_COL_IMPERFECT_INDICATIVE));
      item.futureIndicative = cursor.getString(cursor.getColumnIndex(VERBS_COL_FUTURE_INDICATIVE));
      item.pastRemoteIndicative = cursor.getString(cursor.getColumnIndex(VERBS_COL_PAST_REMOTE_INDICATIVE));
      item.pastIndicative = cursor.getString(cursor.getColumnIndex(VERBS_COL_PAST_INDICATIVE));
      item.pluperfectIndicative = cursor.getString(cursor.getColumnIndex(VERBS_COL_PLUPERFECT_INDICATIVE));
      item.futurePerfectIndicative = cursor.getString(cursor.getColumnIndex(VERBS_COL_FUTURE_PERFECT_INDICATIVE));
      item.pluperfectRemoteIndicative = cursor.getString(cursor.getColumnIndex(VERBS_COL_PLUPERFECT_REMOTE_INDICATIVE));
      
      item.presentSubjunctive = cursor.getString(cursor.getColumnIndex(VERBS_COL_PRESENT_SUBJUNCTIVE));
      item.imperfectSubjunctive = cursor.getString(cursor.getColumnIndex(VERBS_COL_IMPERFECT_SUBJUNCTIVE));
      item.pastSubjunctive = cursor.getString(cursor.getColumnIndex(VERBS_COL_PAST_SUBJUNCTIVE));
      item.pluperfectSubjunctive = cursor.getString(cursor.getColumnIndex(VERBS_COL_PLUPERFECT_SUBJUNCTIVE));
      item.presentConditional = cursor.getString(cursor.getColumnIndex(VERBS_COL_PRESENT_CONDITIONAL));
      item.pastConditional = cursor.getString(cursor.getColumnIndex(VERBS_COL_PAST_CONDITIONAL));
      item.imperative = cursor.getString(cursor.getColumnIndex(VERBS_COL_IMPERATIVE));
      item.similarVerbs = cursor.getString(cursor.getColumnIndex(VERBS_COL_SIMILAR_VERBS));
      
      return item;
    }
    
    return null;
  }
}
