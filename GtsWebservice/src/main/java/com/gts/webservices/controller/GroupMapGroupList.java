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
@RequestMapping("/GroupMapGroupList")
public class GroupMapGroupList {
	private final static String USER_AGENT = "Mozilla/5.0";
		@RequestMapping(method = RequestMethod.GET)
		public void hello(ModelMap model,HttpServletResponse response,HttpServletRequest request) throws IOException {
			String url = "http://45.40.137.142:8080/track/Track?";
			HttpClient client = HttpClientBuilder.create().build();	
			HttpPost post = new HttpPost(url);	
			Map<String,Object> obj = new HashMap<String,Object>();
			 String page = request.getParameter("page");
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
				Elements tables = html.select("table[class=sortableX]");
				Iterator<Element> data = tables.select("tr[class=devSelectorRowData]").select("td").iterator();
				List<String> list = new ArrayList<String>();
				while(data.hasNext()){
					String tdData = data.next().text();
					list.add(tdData);
				}
				int cnt=0;
				String description=null;
				for (String element : list) {
					cnt++;
				
				    if(cnt==1){
				    	description=element;
				    }
				    	
				    if(cnt==2){
				    obj.put(description, element);
				    cnt=0;
				    }
				}
			
				

			}catch(Exception e){
				e.printStackTrace();
			}
			response.setContentType("application/json; charset=UTF-8"); 
			response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(obj));
		}
		
	}
	