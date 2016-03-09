package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

//@WebServlet("/Something/*")
@WebServlet("/Something")
public class Something extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Logger log = LoggerFactory.getLogger(Default.class); // info trace debug warn error
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.debug("[doGet]");
		//response.getWriter().append("Served at: ").append(request.getContextPath());

		// TEXT AS JSON:
//		String text = "swag";
//		response.setContentType("text/plain");  // Set content type of the response so that jQuery knows what it can expect.
//	    response.setCharacterEncoding("UTF-8");
//	    response.getWriter().write(text);       // Write response body.
	    
	    // Returning List<String> as JSON:
//	    List<String> list = new ArrayList<String>();
//	    list.add("item1");
//	    list.add("item2");
//	    list.add("item3");
//	    String json = new Gson().toJson(list);
//
//	    response.setContentType("application/json");
//	    response.setCharacterEncoding("UTF-8");
//	    response.getWriter().write(json);
		
		//Returning Map<String, String> as JSON
	    Map<String, String> options = new LinkedHashMap<String, String>();
	    options.put("value1", "label1");
	    options.put("value2", "label2");
	    options.put("value3", "label3");
	    String json = new Gson().toJson(options);

	    response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().write(json);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.debug("[doPost]");
		doGet(request, response);
	}

}
