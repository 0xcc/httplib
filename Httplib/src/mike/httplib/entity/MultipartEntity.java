package mike.httplib.entity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;

import android.text.TextUtils;

public class MultipartEntity implements HttpEntity {

	private final static char[] MULTIPART_CHARS = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
            .toCharArray();
	private final String NEW_LINE_STR = "\r\n";
	private final String CONTENT_TYPE = "Content-Type: ";
	private final String CONTENT_DISPOSITION = "Content-Disposition: ";
	
	private final String TYPE_TEXT_CHARSET = "text/plain; charset=UTF-8";
	private final String TYPE_OCTET_STREAM = "application/octet-stream";
	private final byte[] BINARY_ENCODING = "Content-Transfer-Encoding: binary\r\n\r\n".getBytes();
	private final byte[] BIT_ENCODING = "Content-Transfer-Encoding: 8bit\r\n\r\n".getBytes();
	
	private String mBoundary = null;
	
	ByteArrayOutputStream mOutputStream = new ByteArrayOutputStream();
	   
   public MultipartEntity() {
        this.mBoundary = generateBoundary();
    }
	    
	@Override
	public void consumeContent() throws IOException {
        if (isStreaming()) {
            throw new UnsupportedOperationException(
                    "Streaming entity does not implement #consumeContent()");
        }	
	}
	
	/**
     * 生成分隔符
     * 
     * @return
     */
    private final String generateBoundary() {
        final StringBuffer buf = new StringBuffer();
        final Random rand = new Random();
        for (int i = 0; i < 30; i++) {
            buf.append(MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)]);
        }
        return buf.toString();
    }

    /*
     * 参数的分割符
     * */
    
    private void writeFirstBoundary() throws IOException{
    	mOutputStream.write(("--" + mBoundary + "\r\n").getBytes());
    }
    


    private void writeToOutputStream(String paramName, byte[] rawData, String type,
            byte[] encodingBytes,
            String fileName){
    	
    	try {
    		writeFirstBoundary();
			mOutputStream.write((CONTENT_TYPE+type+NEW_LINE_STR).getBytes());
			mOutputStream.write(getContentDispositionBytes(paramName,fileName));
			mOutputStream.write(encodingBytes);
			mOutputStream.write(rawData);
			mOutputStream.write(NEW_LINE_STR.getBytes());
			
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    public void addStringPart(final String paramName,final String value){
    	writeToOutputStream(paramName,value.getBytes(),TYPE_TEXT_CHARSET,BIT_ENCODING,"");
    }
    
    public void addBinaryPart(String paramName, final byte[] rawData) {
        writeToOutputStream(paramName, rawData, TYPE_OCTET_STREAM, BINARY_ENCODING, "no-file");
    }
    
    public void addFilePart(final String key, final File file){
    	InputStream fin = null;
        try {
            fin = new FileInputStream(file);
            writeFirstBoundary();
            final String type = CONTENT_TYPE + TYPE_OCTET_STREAM + NEW_LINE_STR;
            mOutputStream.write(getContentDispositionBytes(key, file.getName()));
            mOutputStream.write(type.getBytes());
            mOutputStream.write(BINARY_ENCODING);

            final byte[] tmp = new byte[4096];
            int len = 0;
            while ((len = fin.read(tmp)) != -1) {
                mOutputStream.write(tmp, 0, len);
            }
            mOutputStream.flush();
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            closeSilently(fin);
        }	
    }
    
    private void closeSilently(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
		    
    
    private byte[] getContentDispositionBytes(String paramName, String fileName){
    	StringBuilder stringBuilder = new StringBuilder();
    	stringBuilder.append(CONTENT_DISPOSITION+" form-data; name=\""+paramName+"\"");
    	if (!TextUtils.isEmpty(fileName)) {
            stringBuilder.append("; filename=\""
                    + fileName + "\"");
        }
    	return stringBuilder.append(NEW_LINE_STR).toString().getBytes();
    }
    
	@Override
	public InputStream getContent() throws IOException, IllegalStateException {
		return  new ByteArrayInputStream(mOutputStream.toByteArray());
	}

	@Override
	public Header getContentEncoding() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getContentLength() {
		// TODO Auto-generated method stub
		return mOutputStream.toByteArray().length;
	}

	@Override
	public Header getContentType() {
		// TODO Auto-generated method stub
		return new BasicHeader("Content-Type", "multipart/form-data; boundary=" + mBoundary);
	}

	@Override
	public boolean isChunked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRepeatable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isStreaming() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void writeTo(OutputStream outstream) throws IOException {
		// 参数最末尾的结束符
        final String endString = "--" + mBoundary + "--\r\n";
        // 写入结束符
        mOutputStream.write(endString.getBytes());
        //
        outstream.write(mOutputStream.toByteArray());
		
	}

}
