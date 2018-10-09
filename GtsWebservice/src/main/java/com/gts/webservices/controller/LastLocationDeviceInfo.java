package com.gts.webservices.controller;
	
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import flexjson.JSONSerializer;

@Controller
@RequestMapping("/getroutelocation")
public class LastLocationDeviceInfo {
	private final static String USER_AGENT = "Mozilla/5.0";
		@RequestMapping(method = RequestMethod.GET)
		public void hello(ModelMap model,HttpServletResponse response,HttpServletRequest request) throws IOException {
			String url = "http://45.40.137.142:8080/track/Track?";
			HttpClient client = HttpClientBuilder.create().build();	
			HttpPost post = new HttpPost(url);	
			
			 String page = "map.device.last";
			 String JSESSIONID = request.getParameter("JSESSIONID");
			 String page_cmd = "mapupd";
			 //String _uniq =  request.getParameter("_uniq");
			 String date_fr =  request.getParameter("date_fr");
			 System.out.println(date_fr);
			 String date_frr = date_fr + "/00:00";
			 String date_to =  date_fr + "/23:59";
			 System.out.println(date_to);
			 String date_tz = "GMT-05:00";
			 String device =  request.getParameter("device");
			 String limit =  "1";
			 String limType = "last";
			
			 post.setHeader("Cookie", "JSESSIONID="+ JSESSIONID);
		
			 
			
			post.setHeader("Host", "accounts.google.com");
			post.setHeader("User-Agent", USER_AGENT);
			post.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			post.setHeader("Accept-Language", "en-US,en;q=0.5");
			post.setHeader("Connection", "keep-alive");
			post.setHeader("Referer", "https://accounts.google.com/ServiceLoginAuth");
			post.setHeader("Content-Type", "application/x-www-form-urlencoded");
			
			Map<String,Object> object = new HashMap<String,Object>();
			
			List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			
			urlParameters.add(new BasicNameValuePair("page", page));
			urlParameters.add(new BasicNameValuePair("page_cmd", page_cmd));
			urlParameters.add(new BasicNameValuePair("date_fr", date_frr));
			urlParameters.add(new BasicNameValuePair("date_to", date_to));
			urlParameters.add(new BasicNameValuePair("date_tz", date_tz));
			urlParameters.add(new BasicNameValuePair("device", device));
			urlParameters.add(new BasicNameValuePair("limit", limit));
			urlParameters.add(new BasicNameValuePair("limType", limType));
			
			try{
				post.setEntity(new UrlEncodedFormEntity(urlParameters));
				
				HttpResponse res = client.execute(post);
				System.out.println("\nSending 'response : " + response);
				
				int responseCode = res.getStatusLine().getStatusCode();

				System.out.println("\nSending 'POST' request to URL : " + url);
				System.out.println("Post parameters : " + urlParameters);
				System.out.println("Response Code : " + responseCode);

				BufferedReader rd = new BufferedReader(
			                new InputStreamReader(res.getEntity().getContent()));

				StringBuffer result = new StringBuffer();
				String line = "";
				
				while ((line = rd.readLine()) != null) {
					result.append(line+"\n");
				}
				
			Document html = Jsoup.parse(result.toString());
				
			 String data = html.toString();
			/* System.out.println(data);*/
			 int index = data.indexOf("<body>");
			/* System.out.println(index);*/
			 int lastIndex = data.lastIndexOf("</body>");
			 /*System.out.println(lastIndex);*/
			
			int points = data.indexOf("Points");
			int last = data.lastIndexOf("Last");
			/*System.out.println("points:"+points);
			System.out.println("last:"+last);*/
			if(last != -1 && points != -1){
			String deviceData = data.substring(points, last);
			/*System.out.println(deviceData);*/
			int counter = 0;
			int pos8 =0;
			int pos9 =0;
			int pos10 =0;
			for( int i=0; i<deviceData.length(); i++ ) {
			    if( deviceData.charAt(i) == '|' ) {
			    	/*System.out.println("in side");*/
			        counter++;
			        if(counter==8){
			        	pos8 = i;
			        }
			        if(counter==9){
			        	pos9 = i;
			        }
			        if(counter==10){
			        	pos10 = i;
			        }   
			    } 
			}
			String deviceLat = deviceData.substring(pos8+1, pos9);
			String deviceLog = deviceData.substring(pos9+1, pos10);
			object.put("Lat", deviceLat);
			object.put("Lan", deviceLog);
			/*System.out.println(deviceLat);
			System.out.println(deviceLog);
			System.out.println("pos8:"+pos8+"   pos9:"+pos9+"   pos10:"+pos10);*/
			}else{
				/*System.out.println("device last not found");*/
				object.put("location", "not found");
			}
			
			}catch(Exception e){
				e.printStackTrace();
			}
			response.setContentType("application/json; charset=UTF-8"); 
			response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(object));
		}
		
	}
	