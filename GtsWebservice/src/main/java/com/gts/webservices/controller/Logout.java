package com.gts.webservices.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
@RequestMapping("/logout")
public class Logout {

        @RequestMapping(method = RequestMethod.GET)
        public void logOut(ModelMap model,HttpServletResponse response,HttpServletRequest request) throws IOException {
            //String url = "http://45.40.137.142:8080/track/Track?page=login";
            Map<String,Object> obj = new HashMap<String,Object>();
       	 String page = request.getParameter("page");
    	 String JSESSIONID = request.getParameter("JSESSIONID");
    	 
    	 HttpSession session=request.getSession();  
         session.invalidate();  
         String logout = "successful";
         String logoutfail = "unsuccessful";
         Login lg = new Login();
		 HashMap<String, String> map = lg.getHashmap();
		 Object value = map.get(JSESSIONID);
		 if(value == null){
			 obj.put("logout",logoutfail);
				obj.put("sessionId", JSESSIONID);
			 System.out.println("false");
		 }
		 System.out.println(value.toString());
		 String objVal = value.toString();
		 if(objVal.equals(JSESSIONID)){
			 System.out.println("true");
			 map.remove(JSESSIONID);
			 obj.put("logout",logout);
		 }
         
         
           //  String sessionId =null;
            /* try{
            		Connection.Response res = Jsoup.connect(url)
        					.cookie("JSESSIONID", JSESSIONID)
        				    .data("page", page)
        				    .method(Method.POST)
        				    .execute();
        			Document doc = res.parse();
        			String sessionId = res.cookie("JSESSIONID");
        			Elements tables = doc.select("table");
        			String logoutfail = "unsuccessful";
    				String logout = "successful";
        			boolean isLogin = false;
    				int cnt=0;
    				Elements navBars;
    				for (Element table : tables) {
    					navBars = table.getElementsByClass("menuBarUnsW");
    				
    					 
    				
    					 if(navBars != null && !navBars.isEmpty()){
    		
    					obj.put("logout",logoutfail);
    					obj.put("sessionId", sessionId);
    					cnt++;
    					if(cnt<=1){
    					//	System.out.println("in cnt");
    					isLogin = true;
    					}
    					//obj.put("menuBarUnsW", navBars.toString());
    					 	
    					}
    					
    				}
    				if(!isLogin){
    					//System.out.println("in out");
    					obj.put("logout",logout);
    				}


             
             
                 

             }
             catch(Exception e){
                 e.printStackTrace();
             } 
*/         	response.setContentType("application/json; charset=UTF-8"); 
			response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(obj));
             }
        }
        
    
