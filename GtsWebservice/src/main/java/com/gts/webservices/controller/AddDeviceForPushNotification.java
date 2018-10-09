package com.gts.webservices.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gts.webservices.model.AddDeviceForNotification;

import flexjson.JSONSerializer;

@Controller
@RequestMapping("/addDeviceForPushNotification")
public class AddDeviceForPushNotification {

	    @Autowired
	    @Qualifier("schoolTrackSessionFactory")
	    SessionFactory schoolTrackSessionFactory;
	    
	    private final static String USER_AGENT = "Mozilla/5.0";
	    
	    @Transactional(propagation = Propagation.REQUIRED,readOnly=false)
		@RequestMapping(method = RequestMethod.GET)
		public void hello(ModelMap model,HttpServletResponse response,HttpServletRequest request) throws IOException {
			
	    	String page = "corridor.info";
	    	
	    	String JSESSIONID = request.getParameter("jsessionId");
	    	String IsAndroid =  request.getParameter("IsAndroid");
			String IMEI = request.getParameter("IMEI");
			String RegistrationNo =  request.getParameter("RegistrationNo");
			String UId = request.getParameter("UId");
			String TokenId = request.getParameter("TokenId");
			
			String url = "http://45.40.137.142:8080/track/Track?";
			HttpPost post = new HttpPost(url);
			
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
				HttpClient client = HttpClientBuilder.create().build();
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
				Map<String,Object> obj = new HashMap<String,Object>();
				if(tables.empty() != null){
			
					if(IsAndroid.equals("true")){ 
						AddDeviceForNotification addDeviceForNotification = new AddDeviceForNotification();
						addDeviceForNotification.setIMEI(IMEI);
						addDeviceForNotification.setRegistrationNo(RegistrationNo);
						addDeviceForNotification.setIsAndroid(IsAndroid);
						Session session = schoolTrackSessionFactory.openSession();
					    
						String hql = "FROM AddDeviceForNotification A WHERE A.IMEI = :IMEI";
						Query query = session.createQuery(hql);
						query.setString("IMEI",IMEI);
						//query.setString("IsAndroid",IsAndroid);
						AddDeviceForNotification results = (AddDeviceForNotification) query.uniqueResult();
						
						if(results!=null){
							String hqll = "update AddDeviceForNotification set RegistrationNo = :RegistrationNo where IMEI = :IMEI";
						    Query queryy = session.createQuery(hqll);
						    queryy.setString("RegistrationNo",RegistrationNo);
						    queryy.setString("IMEI",IMEI);
						    //queryy.setString("IsAndroid",IsAndroid);				    
						    int rowCount = queryy.executeUpdate();
						    if(rowCount!=0){
						    	obj.put("Result","success");
						    }
						}else{
						session.save(addDeviceForNotification);
						obj.put("Result","success");
						}
					}
					else if(IsAndroid.equals("false")){
						AddDeviceForNotification addDeviceForNotification = new AddDeviceForNotification();
						addDeviceForNotification.setUId(UId);
						addDeviceForNotification.setTokenId(TokenId);
						addDeviceForNotification.setIsAndroid(IsAndroid);
						Session session = schoolTrackSessionFactory.openSession();
					    
						String hql = "FROM AddDeviceForNotification A WHERE A.UId = :UId";
						Query query = session.createQuery(hql);
						query.setString("UId",UId);
						//query.setString("IsAndroid",IsAndroid);
						AddDeviceForNotification results = (AddDeviceForNotification) query.uniqueResult();
						
						if(results!=null){
							String hqll = "update AddDeviceForNotification set TokenId = :TokenId where UId = :UId";
						    Query queryy = session.createQuery(hqll);
						    queryy.setString("TokenId",TokenId);
						    queryy.setString("UId",UId);
						  //  queryy.setString("IsAndroid",IsAndroid);				    
						    int rowCount = queryy.executeUpdate();
						    if(rowCount!=0){
						    	obj.put("Result","success");
						    }
						}else{
						session.save(addDeviceForNotification);
						obj.put("Result","success");
						}
						
						
					}
					
					
				}
			
			
			response.setContentType("application/json; charset=UTF-8"); 
			response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(obj));
			}
			catch(Exception e){
				e.printStackTrace();
			}

		}
		
	}
