package com.gts.webservices.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
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
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.jsoup.Jsoup;
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

import com.gts.webservices.model.GeoCorridor;
import com.gts.webservices.util.DateUtil;

import flexjson.JSONSerializer;

@Controller
@RequestMapping("/routeLocation")
public class RouteLocation {
	
	
	   @Autowired
	   SessionFactory sessionFactory;
	   

		@Autowired
		@Qualifier("schoolTrackSessionFactory")
		SessionFactory schoolTrackSessionFactory;
	   
	   private final static String USER_AGENT = "Mozilla/5.0";
	
	    @Transactional(propagation = Propagation.REQUIRED,readOnly=false) 
		@RequestMapping(method = RequestMethod.GET)
		public void hello(ModelMap model,HttpServletResponse response,HttpServletRequest request) throws IOException {
			String url = "http://45.40.137.142:8080/track/Track?";
			String accountId = request.getParameter("accountId");
			
			HttpClient client = HttpClientBuilder.create().build();	
			HttpPost post = new HttpPost(url);	
			  Map<String,Object> obj = new HashMap<String,Object>();
			 String page = "corridor.info";
			 String JSESSIONID = request.getParameter("JSESSIONID");
			 String page_cmd = "select";
			String corridorId = request.getParameter("routeId");
			String z_subview = "View";
			
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
			urlParameters.add(new BasicNameValuePair("page_cmd", page_cmd));
			urlParameters.add(new BasicNameValuePair("z_zone", corridorId));
			urlParameters.add(new BasicNameValuePair("z_subview", z_subview));
			
			
			try{
				post.setEntity(new UrlEncodedFormEntity(urlParameters));
				
				HttpResponse res = client.execute(post);
				System.out.println("\nSending 'response : " + response);
				
				int responseCode = res.getStatusLine().getStatusCode();
				double latitude = 0;
				double longitute = 0;
				
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
		
				Elements input = html.select("input[class=latlonInput]");
			
				List<String> list = new ArrayList<String>();
				String val=null;
				String anObject = "0.0";
				for (Element element : input) {
					 val = element.attr("value");
					 if((!val.equals(anObject))){
					list.add(val);
					 }
				}
				System.out.println(list.toString());
				 List<Map<String,Object>> listOfMAp = new ArrayList<Map<String,Object>>();
				 int cnt=0;
				 String lat=null;
				 for (String element : list) {
						cnt++;
					
					    if(cnt==1){
					    	lat=element;
					    }
					    	
					    if(cnt==2){
					    Map<String,Object> map1 = new HashMap<String,Object>();	
					    map1.put("Lat", lat);
					    map1.put("Lon", element);
				
					   
					    cnt=0;
					    }
					}
				
		
					Session session = sessionFactory.getCurrentSession();
					
					String sql = "select lastValidLongitude ,lastValidLatitude ,lastEventTimestamp from Device where deviceID "
                                 +"in (Select deviceId from  RouteDeviceInfo where geoCorridorId  = :corridorId)";
					
					Query query = session.createSQLQuery(sql);
					query.setParameter("corridorId", corridorId);
					List<Object[]> object = query.list();
					String epoch = null;
					Map<String,Object> map = new HashMap<String,Object>();
					
					for (Object[] object2 : object) {
						System.out.println(object2[0]);
						System.out.println(object2[1]);
						System.out.println(object2[2]);
						if(!object2[2].equals("0")){
							epoch = object2[2].toString();
						}
						if(!object2[0].equals("0.0") && !object2[0].equals("0.0")){
							latitude = (Double) object2[1];
							longitute = (Double) object2[0];
							map.put("Lat", object2[1]);
							map.put("Lon",object2[0]);
						
						}
						
					}
					
					
					/*Criteria cr = session.createCriteria(GeoCorridor.class);
					cr.add(Restrictions.eq("accountId", accountId));
					System.out.println("accountId="+accountId);
					cr.add(Restrictions.eq("corridorId", corridorId));
					System.out.println("corridorId:"+corridorId);
					GeoCorridor geoCorridor= (GeoCorridor) cr.uniqueResult();
					System.out.println("geoCorridor:"+geoCorridor.toString());*/
					
				
					
				  
					Date date = DateUtil.getDateFromLong(Integer.valueOf(epoch));
					System.out.println("*******"+ date );
//					Calendar cal = Calendar.getInstance();
//					String month = new SimpleDateFormat("MMM").format(cal.getTime());
					
					SimpleDateFormat SimpleDateformat = new SimpleDateFormat("E"); // three digit abbreviation
					String day = SimpleDateformat.format(date);
					
					SimpleDateFormat SimpleDateformatMonth = new SimpleDateFormat("MM"); // three digit abbreviation
					String month = SimpleDateformatMonth.format(date);
					
				    String currentDate = day+" "+date.getDate()+"/"+month+" "+date.getHours()+":"+date.getMinutes()+"H"; 
				  
				    obj.put("lastUpdatedDate",currentDate );
				    
				    /*int index = new Random().nextInt(listOfMAp.size());
				    Object randomObj = listOfMAp.get(index);*/
				    obj.put("CurrentValue", map);
				    
				    
				   Session session1 =  schoolTrackSessionFactory.openSession();
				   String hql = "select * from schooltrack.stops where stops.routeId in ( select routeNo from schooltrack.route where corridorID = '"+corridorId+"')";
				   Query query1 =  session1.createSQLQuery(hql);
				   List<Object> rows = query1.list();
				   
				   for (Object string : rows) {
					Object[] row = (Object[]) string;
					Map<String,Object> mapObj = new HashMap<String,Object>();
					mapObj.put("Lat", row[3]);
					mapObj.put("Lon", row[4]);
					mapObj.put("Stop", row[5]);
					double lat1=latitude;
					double lat2=(Double) row[3];
					double lon1=longitute;
					double lon2=(Double) row[4];
					
					double earthRadius = 6371000; //meters
				    double dLat = Math.toRadians(lat2-lat1);
				    double dLng = Math.toRadians(lon2-lon1);
				    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
				               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
				               Math.sin(dLng/2) * Math.sin(dLng/2);
				    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
				    float dist = (float) (earthRadius * c);
				    double kilometers = dist * 0.001;
					mapObj.put("distance", kilometers);
					mapObj.put("isVisited", row[9]);
					listOfMAp.add(mapObj);
				}
				   obj.put("LocationList", listOfMAp);   
			}catch(Exception e){
				e.printStackTrace();
			}
			response.setContentType("application/json; charset=UTF-8"); 
			response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(obj));
		}
	}
	