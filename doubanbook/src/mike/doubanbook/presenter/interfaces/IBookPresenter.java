package mike.doubanbook.presenter.interfaces;

import java.util.List;

import mike.doubanbook.model.BookMDL;

public interface IBookPresenter {
	void 	loadBookByIsbn(String isbn,ICompleteListener<BookMDL> listener);
	void 	searchByTitle(String title);
	void	searchByAuthor(String author);

}
