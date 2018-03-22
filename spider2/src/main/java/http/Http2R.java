package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import model.MyHttpResponse;

public class Http2R {
	
	public static MyHttpResponse httpGet(String url){
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		MyHttpResponse result = new MyHttpResponse();
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpGet);
			result.setStatusLine(response.getStatusLine());
			if (response.getStatusLine().getStatusCode() < 300){
				StringBuilder sb = new StringBuilder();
				HttpEntity entity = response.getEntity();
				if (null != entity) {
					InputStream in = entity.getContent();
					entity.getContentLength();
					BufferedReader br = new BufferedReader(new InputStreamReader(in));
					for(String line = null; (line = br.readLine()) != null; ){
						sb.append(line);
					}
					result.setEntity(sb.toString());
				}
				EntityUtils.consume(entity);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	

}
