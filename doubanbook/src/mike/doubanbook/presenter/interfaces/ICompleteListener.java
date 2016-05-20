package mike.doubanbook.presenter.interfaces;

public interface ICompleteListener <T> {
	void onSuccess(T data);
	void onError(String error);
}
