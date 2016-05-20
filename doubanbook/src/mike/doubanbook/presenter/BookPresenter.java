package mike.doubanbook.presenter;

 
import mike.doubanbook.model.BookMDL;
import mike.doubanbook.model.impl.BookImpl;
import mike.doubanbook.presenter.interfaces.IBookPresenter;
import mike.doubanbook.presenter.interfaces.ICompleteListener;
import mike.doubanbook.views.interfaces.IBookView;

public class BookPresenter implements IBookPresenter {
	
	 BookImpl bookImpl=new BookImpl();
	 IBookView bookView;
	 
	 public BookPresenter(IBookView bookView){
		 this.bookView=bookView;
	 }
	 
	@Override
	public void loadBookByIsbn(String isbn ,ICompleteListener<BookMDL> listener) {
		
	
		
		bookView.showDialog("≤È—Ø");
		bookImpl.loadBookByIsbn(isbn, listener);
	}

	@Override
	public void searchByTitle(String title) {
		
		
	}

	@Override
	public void searchByAuthor(String author) {
		
		
	}

	
	
}
