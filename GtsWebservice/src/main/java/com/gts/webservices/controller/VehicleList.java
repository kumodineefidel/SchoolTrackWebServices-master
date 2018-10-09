package com.gts.webservices.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
@RequestMapping("/VehicleList")
public class VehicleList {

		@RequestMapping(method = RequestMethod.GET)
		public void hello(ModelMap model,HttpServletResponse response,HttpServletRequest request) throws IOException {
			String url = "http://45.40.137.142:8080/track/Track?";
			Map<String,Object> obj = new HashMap<String,Object>();
			 String page = request.getParameter("page");
			 String JSESSIONID = request.getParameter("JSESSIONID");
		
	
		try{		
			Connection.Response res = Jsoup.connect(url)
					.cookie("JSESSIONID", JSESSIONID)
				    .data("page", page)
				    .method(Method.POST)
				    .execute();
			Document doc = res.parse();
			Elements tables = doc.select("table[class=adminSelectTable_sortable]");
			
			Iterator<Element> header = tables.select("tr[class=adminTableHeaderRow]").select("th").iterator();
			Iterator<Element> ite = tables.select("tr").select("td[class=adminTableBodyCol]").iterator();
			List<String> list = new ArrayList<String>();
			List<String> list1 = new ArrayList<String>();
			while(header.hasNext()){
				String data = header.next().text();
				list.add(data);
			
			}
			obj.put("Header:", list.toString());
			
			while(ite.hasNext()){
				String data = ite.next().text();
				list1.add(data);
			
			}
			obj.put("Dat", list1.toString());
		
			}
			catch(Exception e){
				e.printStackTrace();
			}
			response.setContentType("application/json; charset=UTF-8"); 
			response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(obj));

		}
		
	}
