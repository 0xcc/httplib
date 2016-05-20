package mike.doubanbook.model.impl;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

public final class  DoubanApis {
	private static final String fields="fields=id,catalog,author,rating,pubdate,title,subtitle,image,images,publisher,isbn10,isbn13,url,summary";

	
	private static String  idapi="https://api.douban.com/v2/book";
	public static String getBookIdApiUrl(String id){
		String result=idapi+File.separator+id+"?"+fields;
		return result;
	}
	
	private static String  isbnapi="https://api.douban.com/v2/book/isbn";
	public static String getBookIsbnApiUrl(String isbn){
		String result=isbnapi+File.separator+isbn+"?"+fields;
		return result;
	}
	
	private static String  searchApi="https://api.douban.com/v2/book/search";
	public static String getBookSearchApiUrl(Map<String,String> params){
		
		StringBuilder result=new StringBuilder(searchApi);
		result.append("?"+fields);
		for (Entry<String,String> entry:params.entrySet()){
			result.append("&").append(entry.getKey()).append("=").append(entry.getValue());
		}
		return result.toString();
		
	};
	
}
