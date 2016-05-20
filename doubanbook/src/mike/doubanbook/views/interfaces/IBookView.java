package mike.doubanbook.views.interfaces;

import java.util.List;

import mike.doubanbook.model.BookMDL;

public interface IBookView {

	void showDialog(String msg);
	void hideDialog();
	void showError(String msg);
	
	void setBookInfo(BookMDL book);
	void setBookList(List<BookMDL> list);
	
}
