package com.gts.webservices.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import flexjson.JSONSerializer;

@Controller
@RequestMapping("/calculateDistance")
public class CalculateDistance {
	private final static String USER_AGENT = "Mozilla/5.0";
		
		
		@Autowired
		SessionFactory sessionFactory;
		

		@Autowired
		@Qualifier("schoolTrackSessionFactory")
		SessionFactory schoolTrackSessionFactory;
		
	    @Transactional(propagation = Propagation.REQUIRED,readOnly=false)
		@RequestMapping(method = RequestMethod.GET)
		public void getrouteList(ModelMap model,HttpServletResponse response,HttpServletRequest request) throws IOException {
	    	
	    	
			String url = "http://45.40.137.142:8080/track/Track?";
			HttpClient client = HttpClientBuilder.create().build();	
			HttpPost post = new HttpPost(url);	
			  Map<String,Object> obj = new HashMap<String,Object>();
			 String page = "corridor.info";
			 String JSESSIONID = request.getParameter("JSESSIONID");
			
			 post.setHeader("Cookie", "JSESSIONID="+ JSESSIONID);
			
			 
			
			post.setHeader("Host", "accounts.google.com");
			post.setHeader("User-Agent", USER_AGENT);
			post.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			post.setHeader("Accept-Language", "en-US,en;q=0.5");
			post.setHeader("Connection", "keep-alive");
			post.setHeader("Referer", "https://accounts.google.com/ServiceLoginAuth");
			post.setHeader("Content-Type", "application/x-www-form-urlencoded");
			
			List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			
			urlParameters.add(new BasicNameValuePair("page", page));
			
			
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
			
				Elements tables = html.select("table[class=adminSelectTable_sortable]");
				
				if(tables.empty()!=null){
					
				
					double lat1=19.997453;
					double lat2=12.971599;
					double lon1=73.789802;
					double lon2=77.594563;
					
					double earthRadius = 6371000; //meters
				    double dLat = Math.toRadians(lat2-lat1);
				    double dLng = Math.toRadians(lon2-lon1);
				    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
				               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
				               Math.sin(dLng/2) * Math.sin(dLng/2);
				    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
				    float dist = (float) (earthRadius * c);
				    double kilometers = dist * 0.001;
				    System.out.println("dist:"+dist);
				    System.out.println("kilometers:"+kilometers);
					    

				}
				
			}catch(Exception e){
				e.printStackTrace();
			}
			response.setContentType("application/json; charset=UTF-8"); 
			response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(obj));
		}
		
	}
	