package mike.doubanbook.views;

import java.util.List;


import com.example.qr_codescan.MipcaActivityCapture;

import mike.doubanbook.R;
import mike.doubanbook.R.layout;
import mike.doubanbook.model.BookMDL;
import mike.doubanbook.model.impl.BookImpl;
import mike.doubanbook.presenter.BookPresenter;
import mike.doubanbook.presenter.interfaces.IBookPresenter;
import mike.doubanbook.presenter.interfaces.ICompleteListener;
import mike.doubanbook.views.interfaces.IBookView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class MainActivity extends FragmentActivity implements IBookView {
	
	private final static int SCANNIN_GREQUEST_CODE = 1;

	AlertDialog mDialog=null;
	ImageButton imagebtn;
	EditText editText;
	
	FragBookDetials fragDetail;
	Fragment fragList;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        
    	super.onCreate(savedInstanceState);
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE); 
        setContentView(R.layout.activity_main);
        
        //getSupportFragmentManager()
        //getWindow().requestFeature(featureId)
        
        editText=(EditText)findViewById(R.id.editTxtSearch);
        editText.setOnEditorActionListener(editTxtSearchListener);
      
        ImageButton imagebtn=(ImageButton)findViewById(R.id.btnScanIsbn);
        imagebtn.setOnClickListener(btnOnClickListener);
    }
    
    OnClickListener btnOnClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, MipcaActivityCapture.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
			
			
			
		}
	};
    
    OnEditorActionListener editTxtSearchListener=new OnEditorActionListener() {
		
		@Override
		public boolean onEditorAction(TextView arg0, int actionId, KeyEvent event) {
			
			if(actionId==EditorInfo.IME_ACTION_SEARCH && event!=null && event.getKeyCode()==KeyEvent.KEYCODE_ENTER){
				
			}
			
			return true;
		}
	};
    
	ICompleteListener<BookMDL> listener=new ICompleteListener<BookMDL>() {
		
		@Override
		public void onSuccess(BookMDL data) {
			Log.i("book", data.getTitle());
			FragmentManager manager=getSupportFragmentManager();
			FragmentTransaction transaction= manager.beginTransaction();
			if(fragDetail==null){
				fragDetail=new FragBookDetials(data);
				
				
				transaction.add(R.id.container, fragDetail);
				transaction.show(fragDetail);
				
			}else{
				fragDetail.setBook(data);
				if(fragDetail.isHidden()){
					transaction.add(R.id.container, fragDetail);
					transaction.show(fragDetail);
				}
			}
			transaction.commit();
		}
		
		@Override
		public void onError(String error) {
			Log.i("error", error);
		}
	};

	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
		case SCANNIN_GREQUEST_CODE:
			if(resultCode == RESULT_OK){
				Bundle bundle = data.getExtras();
				//显示扫描到的内容
				String isbn=bundle.getString("result");
				Log.i("isbn", isbn);
				IBookPresenter presenter=new BookPresenter(this);
				presenter.loadBookByIsbn(isbn,listener);
				
			break;
			}
        }
	}
    
    
    
    

	@Override
	public void showDialog(String msg) {
		if(mDialog!=null){
			mDialog.setMessage(msg);
			mDialog.show();
		}	
	}

	@Override
	public void hideDialog() {
		if(mDialog!=null){
			mDialog.hide();
		}
	}

	@Override
	public void showError(String msg) {
		if(mDialog!=null){
			mDialog.setMessage(msg);
			mDialog.show();
		}
		
	}

	@Override
	public void setBookInfo(BookMDL book) {
			
	}

	@Override
	public void setBookList(List<BookMDL> list) {
		
		
	}
    
	
    
    
    
}
