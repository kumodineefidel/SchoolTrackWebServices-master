package com.gts.webservices.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import flexjson.JSONSerializer;

@Controller
@RequestMapping("/login")
public class Login {
	
	@Autowired
    @Qualifier("schoolTrackSessionFactory")
    SessionFactory schoolTrackSessionFactory;
    
	public static HashMap<String, String> map = new HashMap<String, String>();
	public static HashMap<String, String> profile = new HashMap<String, String>();
	
		@RequestMapping(method = RequestMethod.GET)
		public void hello(ModelMap model,HttpServletResponse response,HttpServletRequest request) throws IOException {
			
			String url = "http://45.40.137.142:8080/track/Track?";
			Map<String,Object> obj = new HashMap<String,Object>();
			 String account = request.getParameter("account");
			 String user = request.getParameter("user");
			 String userName = null;
			 /*if(user.equals("Admin") || user.equals("School Admin") || user.equals("Parent") || user.equals("Student")){
				 userName = "System Administrator";
			 }
			*/ String password = request.getParameter("password");
		System.out.println("System Administrator:"+userName);
		Session session = schoolTrackSessionFactory.openSession();
		String login = "successful";
		String loginfail = "unsuccessful";
		String sessionId = null;
		 String sql = "SELECT * FROM schoolAdmin where username='"+user+"' and password='"+password+"' and accountType='"+account+"'";
		 Query query = session.createSQLQuery(sql);
		 
		 if(query.list().isEmpty()){
			 
			 obj.put("login",loginfail);
			 
		 }else{
			 obj.put("login",login);
			 HttpSession sessionn = request.getSession();
			 sessionId = sessionn.getId();
			 map.put(sessionId, sessionId);
			 List<Object[]> entities = query.list();
			 Integer parentId = 0;
			 Integer studentId = 0;
			 
			 String accountType = null;
			    for (Object[] entity : entities) {
			        for (Object entityCol : entity) {
			        	parentId=Integer.parseInt(entity[0].toString());
			        	System.out.println("ID"+parentId);
			        	accountType = entity[1].toString();
			            System.out.print("accountType:" + accountType);
			        }
			        System.out.println("");
			    }
			   if(accountType.equals("Parent")){
				   
				   String sqll = "SELECT studentId FROM ParenttoStudent where parentId="+parentId+"";
					 Query queryy = session.createSQLQuery(sqll);
					 
					 for (Object[] entity : entities) {
					        for (Object entityCol : entity) {
					        	studentId=Integer.parseInt(entity[0].toString());
					        	System.out.println("ID"+studentId);
					     
					        }
					        System.out.println("");
					    }
					 
			   }
			 //Object currentUser = (Object) ((ServletRequest) session).getAttribute("currentUser");
			 //response.setContentType("application/json; charset=UTF-8");
			 /*profile.put("accountId", );
			   map.put("accountType", currentUser.getAccountType());
			   map.put("name", currentUser.getName());
			   map.put("address", currentUser.getAddress());
			   map.put("email", currentUser.getEmail());
			   map.put("city", currentUser.getCity());
			   map.put("school", currentUser.getSchool());
			   map.put("userName", currentUser.getUsername());
			   map.put("age", currentUser.getAge());
			*/	obj.put("sessionId", sessionId);
		 }
		 
		 /*try{
			
				if(userName!=null){
				Connection.Response res = Jsoup.connect(url)
					    .data("account", account, "user", userName,"password",password)
					    .method(Method.POST)
					    .execute();
				
				Document doc = res.parse();
			
				String sessionId = res.cookie("JSESSIONID");
				String login = "successful";
				String loginfail = "unsuccessful";
				Elements tables = doc.select("table");
				boolean isLogin = false;
				int cnt=0;
				Elements navBars;
				for (Element table : tables) {
					navBars = table.getElementsByClass("menuBarUnsW");
				
					 
				
					 if(navBars != null && !navBars.isEmpty()){
					
					obj.put("login",login);
					obj.put("sessionId", sessionId);
					cnt++;
					if(cnt<=1){
					
					isLogin = true;
					}
					
					 	
					}
					
				}
				if(!isLogin){
					
					obj.put("login",loginfail);
				}
				}
				else{
				
					Connection.Response res = Jsoup.connect(url)
						    .data("account", account, "user", user,"password",password)
						    .method(Method.POST)
						    .execute();
					
					Document doc = res.parse();
				
					String sessionId = res.cookie("JSESSIONID");
					String login = "successful";
					String loginfail = "unsuccessful";
					Elements tables = doc.select("table");
					boolean isLogin = false;
					int cnt=0;
					Elements navBars;
					for (Element table : tables) {
						navBars = table.getElementsByClass("menuBarUnsW");
					
						 
					
						 if(navBars != null && !navBars.isEmpty()){
						
						obj.put("login",login);
						obj.put("sessionId", sessionId);
						cnt++;
						if(cnt<=1){
						
						isLogin = true;
						}
						
						 	
						}
						
					}
					if(!isLogin){
						
						obj.put("login",loginfail);
					}
				}

			}
			catch(Exception e){
				e.printStackTrace();
			}
*/			response.setContentType("application/json; charset=UTF-8"); 
			response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(obj));

		}
		
		public HashMap<String, String> getHashmap() {
		    
		    return map;
		}
		
	}
