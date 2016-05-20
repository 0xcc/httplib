package mike.doubanbook.views;

import com.google.zxing.oned.rss.FinderPattern;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ViewScaleType;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import mike.doubanbook.R;
import mike.doubanbook.model.BookMDL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

public class FragBookDetials extends Fragment {

	BookMDL book;
	
	private ImageView imageBook;
	private TextView txtTitle;
	private RatingBar barRating;
	private TextView txtAverageRating;
	private TextView txtnumRaters;
	private TextView txtPublisher;
	private TextView txtPubdate;
	private TextView txtAuthor;
	private TextView txtSummary;
	private TextView txtCatalog;
	
	
	public FragBookDetials(BookMDL book){
		this.book=book;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		 View v=inflater.inflate(R.layout.frag_book_detials,null);
		 initViews(v);
		 BindBook();
		return v;
	}
	
	private void initViews(View v){
		//Activity activity=getActivity();
		imageBook=(ImageView)v.findViewById(R.id.imageBook);
		txtTitle=(TextView)v.findViewById(R.id.txtTitle);
		barRating=(RatingBar)v.findViewById(R.id.rating);
		txtAverageRating=(TextView)v.findViewById(R.id.txtAverageRating);
		txtnumRaters=(TextView)v.findViewById(R.id.txtnumRaters);
		
		txtPublisher=(TextView)v.findViewById(R.id.txtPublisher);
		txtPubdate=(TextView)v.findViewById(R.id.txtPubdate);
		txtAuthor=(TextView)v.findViewById(R.id.txtAuthor);
		txtSummary=(TextView)v.findViewById(R.id.txtSummary);
		txtCatalog=(TextView)v.findViewById(R.id.txtCatalog);
	}
	
	public void setBook(BookMDL book){
		this.book=book;
		BindBook();
		
	}
	
	private void BindBook(){
		if(book!=null){
			txtTitle.setText(book.getTitle());
			//barRating.setMax(Integer.valueOf(book.getRating().getMax()));
			Float rating=Float.valueOf(book.getRating().getAverage());
			
			if(rating!=0){
				rating=rating/Float.valueOf( book.getRating().getMax());
				int max=barRating.getMax();
				rating=max*rating;
				
			}
			float r=rating;
			barRating.setRating(r);
		
			
			
			txtAverageRating.setText("平均: "+ book.getRating().getAverage());
			//txtnumRaters.setText(book.getRating().getNumRaters());
			txtnumRaters.setText("");
			txtPublisher.setText("出版社: "+book.getPublisher());
			txtPubdate.setText("出版日期: "+book.getPubdate());
			String[] authors=book.getAuthor();
			StringBuilder names=new StringBuilder();
			for(int i=0;i<authors.length;i++){
				String name=authors[i];
				 if(i==0){
					 names.append(name);
				 }else{
					 names.append(","+name);
				 }
				
			}
			txtAuthor.setText("作者: "+names);
			txtSummary.setText(book.getSummary());
			 txtCatalog.setText(book.getCatalog());
			
			DisplayImageOptions options=new DisplayImageOptions.Builder()
										.cacheInMemory(true)
										.cacheOnDisc(true)
										
										.build();
			
			ImageLoaderConfiguration configuration=new ImageLoaderConfiguration.Builder(getActivity()).build();
			
			ImageLoader loader=ImageLoader.getInstance();
			loader.init(configuration);
	 
			 
			loader.loadImage(book.getImages().getLarge(),options, new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String arg0, View arg1) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
					
					BitmapDrawable backgournd=new BitmapDrawable(arg2);
					
					imageBook.setBackgroundDrawable(backgournd);
					
				}
				
				@Override
				public void onLoadingCancelled(String arg0, View arg1) {
					// TODO Auto-generated method stub
					
				}
			});
			
			
			 	
		}
		
	}
	
	

}
