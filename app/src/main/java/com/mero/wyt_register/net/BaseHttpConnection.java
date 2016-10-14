package com.mero.wyt_register.net;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import android.os.AsyncTask;
import android.util.Log;
import com.mero.wyt_register.Config;
import com.mero.wyt_register.HttpMethod;

/**
 *@项目名称: 简易通注册助手
 *@文件名称: BaseHttpConnection.java
 *@Date: 2016-7-14
 *@Copyright: 2016 Technology Mero Inc. All rights reserved.
 *注意：由Mero开发，禁止外泄以及使用本程序于其他的商业目的 。
 */
public class BaseHttpConnection{
	private static final String TAG = "baseNetTag";//用于网络访问的TAG日志
	public BaseHttpConnection(final String url,final HttpMethod httpMethod,final ISuccessCallback successCallback,final IFailedCallback failedCallback,String...params){
		new AsyncTask<Void, Void, String>(){
			@Override
			protected String doInBackground(Void... params) {
				StringBuffer buffer=new StringBuffer();//创建字符串连接对象
				for(int i=0;i<params.length;i+=2){
					buffer.append(params[i]).append("=").append(params[i+1]).append("&");
				}
				buffer.deleteCharAt(buffer.length()-1);
				Log.e(TAG, url+buffer.toString());
				try{
					URLConnection conn=null;
					switch(httpMethod){
						case POST:
						conn = new URL(url).openConnection();//打开链接
						conn.setDoInput(true);//数据通道设置
						BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), Config.CHARSET));
						bufferedWriter.write(params.toString());//把数据写到缓冲区
						break;
					case GET:
						conn=new URL(url+"?"+buffer.toString()).openConnection();
						break;
					
					}
					BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(conn.getInputStream()));//打开输入流
					String len=null;
					StringBuffer stringBuffer=new StringBuffer();
					for(len=bufferedReader.readLine();len!=null;bufferedReader.readLine()){
						stringBuffer.append(len);
					}
					return stringBuffer.toString();
				}catch(Exception e ){
					e.printStackTrace();
				}
				return null;
			}
			protected void onPostExecute(String result) {
				if(result!=null){
					if(successCallback!=null){
						successCallback.success(result);
					}else if(failedCallback!=null){
						failedCallback.fail();
					}
				}else{
					if(failedCallback!=null){
						failedCallback.fail();
					}
				}
			};
			
		}.execute();
		
	}
	//成功回调接口
	public interface ISuccessCallback{
		void success(String result);
	}
	//失败回调接口
	public interface IFailedCallback{
		void fail();
	}
}
