package com.gts.webservices.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gts.webservices.model.RouteDeviceInfo;

import flexjson.JSONSerializer;

@Controller
@RequestMapping("/assignDevice")
public class AssignDevice {

	    @Autowired
	    SessionFactory sessionFactory;
	    
	    @Transactional(propagation = Propagation.REQUIRED,readOnly=false)
		@RequestMapping(method = RequestMethod.GET)
		public void hello(ModelMap model,HttpServletResponse response,HttpServletRequest request) throws IOException {
			String JSESSIONID = request.getParameter("jsessionId");
			String geoCorridorId = request.getParameter("corridorId");
			String deviceId =  request.getParameter("deviceId");
			
			RouteDeviceInfo routeDeviceInfo = new RouteDeviceInfo();
			routeDeviceInfo.setDeviceId(deviceId);
			routeDeviceInfo.setGeoCorridorId(geoCorridorId);
			Session session = sessionFactory.getCurrentSession();
			session.save(routeDeviceInfo);
			
			Map<String,Object> obj = new HashMap<String,Object>();
			
			obj.put("Result","success");
			response.setContentType("application/json; charset=UTF-8"); 
			response.getWriter().print(new JSONSerializer().exclude("class","*.class","authorities").deepSerialize(obj));

		}
		
	}
