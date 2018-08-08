
package org.mortalis.verbconjugator.es;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseManager extends SQLiteAssetHelper {
  
  private static final String DATABASE_NAME = "verbs_es.db";
  
  private static final String VERBS_TABLE_NAME = "verbs";
  
  private static final String VERBS_COL_VERB = "verb";
  private static final String VERBS_COL_IMPERSONAL_FORMS = "impersonalForms";
  private static final String VERBS_COL_PRESENT_INDICATIVE = "presentIndicative";
  private static final String VERBS_COL_PRETERIT_IMPERFECT_INDICATIVE = "preteritImperfectIndicative";
  private static final String VERBS_COL_PRETERIT_PERFECT_SIMPLE_INDICATIVE = "preteritPerfectSimpleIndicative";
  private static final String VERBS_COL_FUTURE_INDICATIVE = "futureIndicative";
  private static final String VERBS_COL_PRETERIT_PERFECT_INDICATIVE = "preteritPerfectIndicative";
  private static final String VERBS_COL_PRETERIT_PLUPERFECT_INDICATIVE = "preteritPluperfectIndicative";
  private static final String VERBS_COL_PRETERIT_PREVIUOUS_INDICATIVE = "preteritPreviuousIndicative";
  private static final String VERBS_COL_FUTURE_PERFECT_INDICATIVE = "futurePerfectIndicative";
  private static final String VERBS_COL_PRESENT_SUBJUNCTIVE = "presentSubjunctive";
  private static final String VERBS_COL_PRETERIT_PERFECT_SUBJUNCTIVE = "preteritPerfectSubjunctive";
  private static final String VERBS_COL_PRETERIT_IMPERFECT_SUBJUNCTIVE = "preteritImperfectSubjunctive";
  private static final String VERBS_COL_PRETERIT_PLUPERFECT_SUBJUNCTIVE = "preteritPluperfectSubjunctive";
  private static final String VERBS_COL_FUTURE_SUBJUNCTIVE = "futureSubjunctive";
  private static final String VERBS_COL_FUTURE_PERFECT_SUBJUNCTIVE = "futurePerfectSubjunctive";
  private static final String VERBS_COL_CONDITIONAL_SIMPLE = "conditionalSimple";
  private static final String VERBS_COL_CONDITIONAL_COMPLEX = "conditionalComplex";
  private static final String VERBS_COL_IMPERATIVE = "imperative";
  private static final String VERBS_COL_IMPERATIVE_NEGATIVE = "imperativeNegative";
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
      item.impersonalForms = cursor.getString(cursor.getColumnIndex(VERBS_COL_IMPERSONAL_FORMS));
      
      item.presentIndicative = cursor.getString(cursor.getColumnIndex(VERBS_COL_PRESENT_INDICATIVE));
      item.preteritImperfectIndicative = cursor.getString(cursor.getColumnIndex(VERBS_COL_PRETERIT_IMPERFECT_INDICATIVE));
      item.preteritPerfectSimpleIndicative = cursor.getString(cursor.getColumnIndex(VERBS_COL_PRETERIT_PERFECT_SIMPLE_INDICATIVE));
      item.futureIndicative = cursor.getString(cursor.getColumnIndex(VERBS_COL_FUTURE_INDICATIVE));
      item.preteritPerfectIndicative = cursor.getString(cursor.getColumnIndex(VERBS_COL_PRETERIT_PERFECT_INDICATIVE));
      item.preteritPluperfectIndicative = cursor.getString(cursor.getColumnIndex(VERBS_COL_PRETERIT_PLUPERFECT_INDICATIVE));
      item.preteritPreviuousIndicative = cursor.getString(cursor.getColumnIndex(VERBS_COL_PRETERIT_PREVIUOUS_INDICATIVE));
      item.futurePerfectIndicative = cursor.getString(cursor.getColumnIndex(VERBS_COL_FUTURE_PERFECT_INDICATIVE));
      
      item.presentSubjunctive = cursor.getString(cursor.getColumnIndex(VERBS_COL_PRESENT_SUBJUNCTIVE));
      item.preteritPerfectSubjunctive = cursor.getString(cursor.getColumnIndex(VERBS_COL_PRETERIT_PERFECT_SUBJUNCTIVE));
      item.preteritImperfectSubjunctive = cursor.getString(cursor.getColumnIndex(VERBS_COL_PRETERIT_IMPERFECT_SUBJUNCTIVE));
      item.preteritPluperfectSubjunctive = cursor.getString(cursor.getColumnIndex(VERBS_COL_PRETERIT_PLUPERFECT_SUBJUNCTIVE));
      item.futureSubjunctive = cursor.getString(cursor.getColumnIndex(VERBS_COL_FUTURE_SUBJUNCTIVE));
      item.futurePerfectSubjunctive = cursor.getString(cursor.getColumnIndex(VERBS_COL_FUTURE_PERFECT_SUBJUNCTIVE));
      
      item.conditionalSimple = cursor.getString(cursor.getColumnIndex(VERBS_COL_CONDITIONAL_SIMPLE));
      item.conditionalComplex = cursor.getString(cursor.getColumnIndex(VERBS_COL_CONDITIONAL_COMPLEX));
      item.imperative = cursor.getString(cursor.getColumnIndex(VERBS_COL_IMPERATIVE));
      item.imperativeNegative = cursor.getString(cursor.getColumnIndex(VERBS_COL_IMPERATIVE_NEGATIVE));
      item.similarVerbs = cursor.getString(cursor.getColumnIndex(VERBS_COL_SIMILAR_VERBS));
      
      return item;
    }
    
    return null;
  }
}
