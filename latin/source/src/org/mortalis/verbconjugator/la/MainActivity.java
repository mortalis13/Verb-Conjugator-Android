package org.mortalis.verbconjugator.la;

import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.FilterQueryProvider;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends Activity {
  
  DatabaseManager db;
  SimpleCursorAdapter cursorAdapter;
  Filter mFilter;
  
  boolean textInnerChange;
  boolean useAutocomplete;
  
  EditText etWord;
//  AutoCompleteTextView etWord;
  
  ImageButton bDown;
  ImageButton bUp;
  
  WebView wvContent;
  ListView lvWords;
  
  FrameLayout loContent;
  

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    init();
    setActions();
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    
    switch(id){
    case R.id.action_settings:
      return true;
    case R.id.action_autocomplete:
      toggleAutocomplete(item);
      return true;
    }
    
    return super.onOptionsItemSelected(item);
  }
  
  
//----------------------------------------------- Actions -----------------------------------------------
  
  void init(){
    db = new DatabaseManager(this);
     
    textInnerChange = false; 
    useAutocomplete = false;
    
    loContent = (FrameLayout) findViewById(R.id.loContent);
    
    lvWords = (ListView) findViewById(R.id.lvWords);
    etWord = (EditText) findViewById(R.id.etWord);
    
    bDown = (ImageButton) findViewById(R.id.bDown);
    bUp = (ImageButton) findViewById(R.id.bUp);
    wvContent = (WebView) findViewById(R.id.wvContent);
    

    WebSettings mWebSettings = wvContent.getSettings();
    mWebSettings.setJavaScriptEnabled(true);
    wvContent.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
    wvContent.setScrollbarFadingEnabled(false);
    wvContent.setWebChromeClient(new WebChromeClient());
    
    wvContent.setWebViewClient(new WebViewClient() {
      public void onPageFinished(WebView view, String url) {
      }
    });
    
    wvContent.addJavascriptInterface(new WebAppInterface(this), "Android");
    
    
    cursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null, 
        new String[]{"verb"}, new int[]{android.R.id.text1}, 0);
    lvWords.setAdapter(cursorAdapter);
    lvWords.setBackgroundColor(Color.WHITE);
    
    cursorAdapter.setFilterQueryProvider(new FilterQueryProvider() {
      public Cursor runQuery(CharSequence str) {
        return db.getVerbsCursor(str);
      }
    });
    
    mFilter = cursorAdapter.getFilter();
  }
  
  void setActions(){
    etWord.setOnFocusChangeListener(new OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus){
          System.out.println("etWord-focus");
          showList();
        }
      }
    }); 
    
    etWord.setOnEditorActionListener(new OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if( actionId == EditorInfo.IME_ACTION_SEARCH || 
            (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER 
            && event.getAction() == KeyEvent.ACTION_DOWN) )
        {
          acSearch();
          // return true;
        }
        
        return true;
        // return false;
      }
    });
    
    
    etWord.addTextChangedListener(new TextWatcher() {
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(!useAutocomplete) return;
        if(textInnerChange) return;
        
        System.out.println("onTextChanged");
        
        if(s.length() < 3){
          clearList();
          return;
        }
        
        mFilter.filter(s);
        
        // Cursor cursor = db.getVerbsCursor(s);
        // cursorAdapter.changeCursor(cursor);
        // cursorAdapter.notifyDataSetChanged();
      }
      
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
      @Override
      public void afterTextChanged(Editable s) {}
    });
    
    
    lvWords.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor itemCursor = (Cursor) parent.getItemAtPosition(position);
        String verb = itemCursor.getString(1);
        System.out.println("verb-list: " + verb);
        
        if(verb.length() == 0) return;
        
        textInnerChange = true;
        etWord.setText(verb);
        textInnerChange = false;
        
        searchVerb(verb);
      }
    });
    
    
    bDown.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
          wvContent.evaluateJavascript("bDownClick();", null);
        } 
        else {
          wvContent.loadUrl("javascript:bDownClick();");
        }
      }
    });
    
    bUp.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
          wvContent.evaluateJavascript("bUpClick();", null);
        } 
        else {
          wvContent.loadUrl("javascript:bUpClick();");
        }
      }
    });
  }
  
  
//----------------------------------------------- Handlers -----------------------------------------------
  
  void acSearch(){
    String verb = etWord.getText().toString();
    System.out.println("verb: " + verb);

    // if(verb.length() == 0)
    //   verb = "abalizar";
    
    searchVerb(verb);
  }
  
  void searchVerb(String verb){
    new SearchVerbTask().execute(verb);
  }
  
  void toggleAutocomplete(MenuItem menuItem){
    if (menuItem.isChecked()){
      System.out.println("toggling-off");
      menuItem.setChecked(false);
      
      useAutocomplete = false;
      clearList();
    }
    else{
      System.out.println("toggling-on");
      menuItem.setChecked(true);
      
      useAutocomplete = true;
    }
  }
  
  
  private class SearchVerbTask extends AsyncTask<String, Void, String> {
    
    @Override
    protected String doInBackground(String... args) {
      return process(args[0]);
    }

    @Override
    protected void onPreExecute() {
      wvContent.loadUrl("about:blank");
    }
    
    @Override
    protected void onPostExecute(String result) {
      if(result == null){
        toast("Verb not found");
        return;
      }
      
      hideKeyboard();
      hideList();
      
      wvContent.loadDataWithBaseURL("file:///android_asset/", result, "text/html", "UTF-8", null);
      // wvContent.loadData(result, "text/html; charset=UTF-8", null);
      loContent.requestFocus();
    }

    
    private String process(String verb) {
      VerbItem verbItem = db.getVerbItem(verb);
      if(verbItem == null) return null;
      
      String html = "";
      AssetManager assetManager = getApplicationContext().getResources().getAssets();
      try {
        InputStream inputStream = assetManager.open("tmpl.html");
        byte[] b = new byte[inputStream.available()];
        
        inputStream.read(b);
        html = new String(b);
        inputStream.close();
      } 
      catch (IOException e) {
        Log.e("mytag", "Couldn't open tmpl.html", e);
      }
      
      StringBuilder sb = new StringBuilder(html);
      
      int pos, len;
      String replStr;
      String[] list;
      
      list = verbItem.impersonalActive.split(",");
      for(int i=0; i<list.length; ++i){
        replStr = "=impAct"+i+"=";
        len = replStr.length();
        pos = sb.indexOf(replStr);
        sb.replace(pos, pos+len, list[i]);
      }

      list = verbItem.presentIndicativeActive.split(",");
      for(int i=0; i<list.length; ++i){
        replStr = "=presIndAct"+i+"=";
        len = replStr.length();
        pos = sb.indexOf(replStr);
        sb.replace(pos, pos+len, list[i]);
      }
      list = verbItem.imperfectIndicativeActive.split(",");
      for(int i=0; i<list.length; ++i){
        replStr = "=impIndAct"+i+"=";
        len = replStr.length();
        pos = sb.indexOf(replStr);
        sb.replace(pos, pos+len, list[i]);
      }
      list = verbItem.future1IndicativeActive.split(",");
      for(int i=0; i<list.length; ++i){
        replStr = "=fut1IndAct"+i+"=";
        len = replStr.length();
        pos = sb.indexOf(replStr);
        sb.replace(pos, pos+len, list[i]);
      }
      list = verbItem.perfectIndicativeActive.split(",");
      for(int i=0; i<list.length; ++i){
        replStr = "=perfIndAct"+i+"=";
        len = replStr.length();
        pos = sb.indexOf(replStr);
        sb.replace(pos, pos+len, list[i]);
      }
      list = verbItem.pluperfectIndicativeActive.split(",");
      for(int i=0; i<list.length; ++i){
        replStr = "=pluperfIndAct"+i+"=";
        len = replStr.length();
        pos = sb.indexOf(replStr);
        sb.replace(pos, pos+len, list[i]);
      }
      list = verbItem.future2IndicativeActive.split(",");
      for(int i=0; i<list.length; ++i){
        replStr = "=fut2IndAct"+i+"=";
        len = replStr.length();
        pos = sb.indexOf(replStr);
        sb.replace(pos, pos+len, list[i]);
      }

      list = verbItem.presentConjunctiveActive.split(",");
      for(int i=0; i<list.length; ++i){
        replStr = "=presConjAct"+i+"=";
        len = replStr.length();
        pos = sb.indexOf(replStr);
        sb.replace(pos, pos+len, list[i]);
      }
      list = verbItem.imperfectConjunctiveActive.split(",");
      for(int i=0; i<list.length; ++i){
        replStr = "=impConjAct"+i+"=";
        len = replStr.length();
        pos = sb.indexOf(replStr);
        sb.replace(pos, pos+len, list[i]);
      }

      list = verbItem.perfectConjunctiveActive.split(",");
      for(int i=0; i<list.length; ++i){
        replStr = "=perfConjAct"+i+"=";
        len = replStr.length();
        pos = sb.indexOf(replStr);
        sb.replace(pos, pos+len, list[i]);
      }
      list = verbItem.pluperfectConjunctiveActive.split(",");
      for(int i=0; i<list.length; ++i){
        replStr = "=pluperfConjAct"+i+"=";
        len = replStr.length();
        pos = sb.indexOf(replStr);
        sb.replace(pos, pos+len, list[i]);
      }

      list = verbItem.impersonalPassive.split(",");
      for(int i=0; i<list.length; ++i){
        replStr = "=impPasv"+i+"=";
        len = replStr.length();
        pos = sb.indexOf(replStr);
        sb.replace(pos, pos+len, list[i]);
      }
      list = verbItem.presentIndicativePassive.split(",");
      for(int i=0; i<list.length; ++i){
        replStr = "=presIndPasv"+i+"=";
        len = replStr.length();
        pos = sb.indexOf(replStr);
        sb.replace(pos, pos+len, list[i]);
      }

      list = verbItem.imperfectIndicativePassive.split(",");
      for(int i=0; i<list.length; ++i){
        replStr = "=impIndPasv"+i+"=";
        len = replStr.length();
        pos = sb.indexOf(replStr);
        sb.replace(pos, pos+len, list[i]);
      }
      list = verbItem.future1IndicativePassive.split(",");
      for(int i=0; i<list.length; ++i){
        replStr = "=fut1IndPasv"+i+"=";
        len = replStr.length();
        pos = sb.indexOf(replStr);
        sb.replace(pos, pos+len, list[i]);
      }
      list = verbItem.perfectIndicativePassive.split(",");
      for(int i=0; i<list.length; ++i){
        replStr = "=perfIndPasv"+i+"=";
        len = replStr.length();
        pos = sb.indexOf(replStr);
        sb.replace(pos, pos+len, list[i]);
      }
      list = verbItem.pluperfectIndicativePassive.split(",");
      for(int i=0; i<list.length; ++i){
        replStr = "=pluperfIndPasv"+i+"=";
        len = replStr.length();
        pos = sb.indexOf(replStr);
        sb.replace(pos, pos+len, list[i]);
      }
      list = verbItem.future2IndicativePassive.split(",");
      for(int i=0; i<list.length; ++i){
        replStr = "=fut2IndPasv"+i+"=";
        len = replStr.length();
        pos = sb.indexOf(replStr);
        sb.replace(pos, pos+len, list[i]);
      }
      list = verbItem.presentConjunctivePassive.split(",");
      for(int i=0; i<list.length; ++i){
        replStr = "=presConjPasv"+i+"=";
        len = replStr.length();
        pos = sb.indexOf(replStr);
        sb.replace(pos, pos+len, list[i]);
      }
      list = verbItem.imperfectConjunctivePassive.split(",");
      for(int i=0; i<list.length; ++i){
        replStr = "=impConjPasv"+i+"=";
        len = replStr.length();
        pos = sb.indexOf(replStr);
        sb.replace(pos, pos+len, list[i]);
      }
      list = verbItem.perfectConjunctivePassive.split(",");
      for(int i=0; i<list.length; ++i){
        replStr = "=perfConjPasv"+i+"=";
        len = replStr.length();
        pos = sb.indexOf(replStr);
        sb.replace(pos, pos+len, list[i]);
      }
      list = verbItem.pluperfectConjunctivePassive.split(",");
      for(int i=0; i<list.length; ++i){
        replStr = "=pluperfConjPasv"+i+"=";
        len = replStr.length();
        pos = sb.indexOf(replStr);
        sb.replace(pos, pos+len, list[i]);
      }

      int imperativeCount = 5;
      list = verbItem.imperativeI.split(",");
      for(int i=0; i<imperativeCount; ++i){
        replStr = "=imperI"+i+"=";
        len = replStr.length();
        pos = sb.indexOf(replStr);
        if(i < list.length)
          sb.replace(pos, pos+len, list[i]);
        else
          sb.replace(pos, pos+len, "");
      }
      list = verbItem.imperativeII.split(",");
      for(int i=0; i<imperativeCount; ++i){
        replStr = "=imperII"+i+"=";
        len = replStr.length();
        pos = sb.indexOf(replStr);
        if(i < list.length)
          sb.replace(pos, pos+len, list[i]);
        else
          sb.replace(pos, pos+len, "");
      }
      
      list = null;
      verbItem = null;
      html = null;
      
      return sb.toString();
    }

  }
  
  
//----------------------------------------------- UI -----------------------------------------------
  
  public void hideKeyboard(){
    InputMethodManager imm =(InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    // imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
  }
  
  void hideList(){
    if(!useAutocomplete) return;
    System.out.println("hiding-list");
    // lvWords.setVisibility(View.GONE);
    lvWords.setVisibility(View.INVISIBLE);
  }
  
  void showList(){
    if(!useAutocomplete) return;
    System.out.println("showing-list");
    lvWords.setVisibility(View.VISIBLE);
  }
  
  void clearList(){
    cursorAdapter.changeCursor(null);
    cursorAdapter.notifyDataSetChanged();
  }   
  
  
//----------------------------------------------- Service -----------------------------------------------
  
  public void toast(String msg){
    int duration = Toast.LENGTH_LONG;
    Toast toast = Toast.makeText(MainActivity.this, msg, duration);
    toast.show();
  }
  
  
  public class WebAppInterface {
    Context mContext;

    WebAppInterface(Context c) {
      mContext = c;
    }

    @JavascriptInterface
    public void showToast(String toast) {
      Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }
    
    @JavascriptInterface
    public int getWinHeight() {
      return wvContent.getHeight();
    }
  }

//----------------------------------------------- Other -----------------------------------------------
 
  void test(String verb){
    verb = "and";
    Cursor cursor = db.getVerbsCursor(verb);
    if(cursor != null){
      System.out.println("setting adapter");
      
      SimpleCursorAdapter adapter = new SimpleCursorAdapter(
            this,
            android.R.layout.simple_list_item_1,
            cursor,
            new String[]{"verb"},
            new int[]{android.R.id.text1},
            0);
      
      lvWords.setAdapter(adapter);
    }    
  }
  
}
