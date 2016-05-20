package mike.doubanbook.model.interfaces;

import java.util.List;

import mike.doubanbook.model.BookMDL;
import mike.doubanbook.presenter.interfaces.ICompleteListener;

public interface IBook {
	void 	loadBookByIsbn(String isbn,ICompleteListener<BookMDL> listener);
	void 	searchByTitle(String title,ICompleteListener< List<BookMDL> > listener);
	void	searchByAuthor(String author,ICompleteListener< List<BookMDL> > listener);
}
