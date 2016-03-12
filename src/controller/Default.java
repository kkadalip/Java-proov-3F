package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import dao.Readxml;
import model.Currency;
import model.Result;

@WebServlet("")
public class Default extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Logger log = LoggerFactory.getLogger(Default.class); // info trace debug warn error

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("[doGet] START");
		//HttpSession httpSession = request.getSession(true);
		//String userName = (String)
		
		//Date sessionDate = (Date) request.getAttribute("selectedDate"); //setAttribute("SESSIONuserName", userName);
		//Date date = new Date();
		
		// kontrolli, kas on olemas sessioonis või mitte
		
		List<Currency> displayedCurrencies = Readxml.downloadAllForDate(getServletContext());
		request.setAttribute("displayedCurrencies", displayedCurrencies);
		
		
		// sessiooni võiks sellegipoolest salvestada (refresh page jmt jaoks)
		
		//ServletContext context = getContext();
		//URL resourceUrl = context.getResource("/WEB-INF/test/foo.txt");
		
		//List<Currency> displayedCurrencies = Readxml.getCurrencies(getServletContext()); // TODO add date, get by date, default will be current day (atm the latest possible, later dates need to be disabled!)
		
		// kontrolli, kas on sessiooni var-is olemas, kui ei, siis lisa, tglt ajaxiga javascripti abil teha??
		//@SuppressWarnings("unchecked")
		//List<Currency> displayedCurrencies = (List<Currency>) httpSession.getAttribute("displayedCurrencies");
		//if(displayedCurrencies.isEmpty()){
		//	log.debug("I DO NOT HAVE session displayedcurrencies, setting them now");
		//	displayedCurrencies = Readxml.getCurrencies();
		//	httpSession.setAttribute("displayedCurrencies", displayedCurrencies);
		//}else{
		//	log.debug("I have session displayedcurrencies.");
		//}
		//request.setAttribute("displayedCurrencies", displayedCurrencies);
		
		request.getRequestDispatcher("jsp/index.jsp").forward(request, response);
	}

	// NOT USING:
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("[doPost] START");
		//HttpSession httpSession = request.getSession(true);
		
		/*
		String inputMoneyAmount = request.getParameter("inputMoneyAmount");
		log.debug("inputMoneyAmount: " + inputMoneyAmount);
		String inputCurrency = request.getParameter("inputCurrency");
		log.debug("inputCurrency: " + inputCurrency); // rate
		String outputCurrency = request.getParameter("outputCurrency");
		log.debug("outputCurrency: " + outputCurrency);
		String selectedDate = request.getParameter("selectedDate");
		log.debug("selectedDate: " + selectedDate);

		// TODO java.lang.NumberFormatException: empty String
		Float inputMoneyAmountFloat = Float.parseFloat(inputMoneyAmount);
		Float inputCurrencyFloat = Float.parseFloat(inputCurrency);
		Float outputCurrencyFloat = Float.parseFloat(outputCurrency);
		
		Float outputAmount = inputCurrencyFloat / outputCurrencyFloat * inputMoneyAmountFloat;
		log.debug("output amount: " + outputAmount);
		
		// AJAXIFY http://stackoverflow.com/questions/4112686/how-to-use-servlets-and-ajax

	    boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
	    // ...

	    if (ajax) {
	    	log.debug("AJAX POST!!!");
	        // Handle ajax (JSON) response.
	    } else {
	    	log.debug("REGULAR POST!!!");
	        // Handle regular (JSP) response.
	    }
		
		//response.sendRedirect(""); // Success
		// TEXT AS JSON: SENDING BACK OUTPUT NUMBER (CHANGE TO JSON/XML LATER!)
		String textOwner = "Eesti Pank";
		String text = outputAmount.toString();
//		response.setContentType("text/plain");  // Set content type of the response so that jQuery knows what it can expect.
//	    response.setCharacterEncoding("UTF-8");
//	    response.getWriter().write(text);       // Write response body.
	    
		// Returning Map<String, String> as JSON
		
//		Map<String, String> options = new LinkedHashMap<String, String>();
//	    options.put(textOwner, text);
//	    options.put("value2", "label2");
//	    options.put("value3", "label3");
//	    String json = new Gson().toJson(options);
		
		//List<String> results = new ArrayList<String>();
		//results.add(text);
		//results.add("some other");
		
		List<Result> results = new ArrayList<Result>();
		results.add(new Result(textOwner, text));
		results.add(new Result("Whatever bank", "some result"));
		*/
		
		String inputMoneyAmount = request.getParameter("inputMoneyAmount");
		log.debug("inputMoneyAmount: " + inputMoneyAmount);
		String inputCurrency = request.getParameter("inputCurrency");
		log.debug("inputCurrency: " + inputCurrency); // rate
		String outputCurrency = request.getParameter("outputCurrency");
		log.debug("outputCurrency: " + outputCurrency);
		String selectedDate = request.getParameter("selectedDate");
		log.debug("selectedDate: " + selectedDate);

		// TODO java.lang.NumberFormatException: empty String
		Float inputMoneyAmountFloat = Float.parseFloat(inputMoneyAmount);
		//Float inputCurrencyFloat = Float.parseFloat(inputCurrency);
		//Float outputCurrencyFloat = Float.parseFloat(outputCurrency);
		
		List<Result> results = Readxml.calculateResults(getServletContext(), inputMoneyAmountFloat, inputCurrency, outputCurrency); //, date);
		
		String json = new Gson().toJson(results);
		log.debug("JSON IS: " + json);
	    response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().write(json);

	    //request.setAttribute("whatevers", results);
	    //request.getRequestDispatcher("/WEB-INF/xml/whatevers.jsp").forward(request, response);
		
		log.info("[doPost] END");
	}
}




































//		Long newUserID = null;
//		
//		try {
//			// TO DO FIX BAD LOGIC
//			// USE ONE SESSION INSTEAD OF TWO SEPARATE (getById AND THEN saveOrUpdate)
//			Long saved_user_id = (Long) httpSession.getAttribute("saved_user_id");
//			UserDao userDAO = new UserDao();
//			log.debug("[doPost] saved_user_id is: {}", saved_user_id); // null or 83 etc
//			User newUser = new User();  //  = new User();
//			log.debug("[doPost] going to set username to: " + userName);
//			newUser.setName(userName);
//			newUser.setUser_sectors(userSectors);
//			newUser.setAgreedToTerms(checkbox_checked);
//			LocalDateTime date = LocalDateTime.now();
//			log.debug("[doPost] going to save date: {}", date);
//			newUser.setDateAdded(date);
//			
//			if(saved_user_id != null){
//				log.debug("[doPost] saved_user_id NOT NULL: {}, UPDATING EXISTING", saved_user_id);
//				
//				newUserID = userDAO.addOrUpdateUser2(saved_user_id,newUser);
//				
//				//User findUser = userDAO.getUserById(saved_user_id);
//				//if(findUser != null){
//					//log.debug("[doPost] ACTUALLY FOUND A USER: {}", findUser);
//					//newUser = findUser;
//				//}else{
//					//log.debug("[doPost] DID NOT FIND A USER BY ID!");
//				//}
//			}else{
//				log.debug("[doPost] saved_user_id null!!! so saving new");
//				newUserID = userDAO.addUser(newUser);
//			}
//			log.debug("[doPost] SAVING SESSION ATTRIBUTE saved user id: {}", newUserID); //, newUser.getId());
//			httpSession.setAttribute("saved_user_id", newUserID); //newUser.getId());
//			// redirect here?
//		}catch (Exception e) {
//			log.error("Error adding or updating user", e); //should add e.printStackTrace(); automatically
//		}


//		try {
//			// TO DO FIX BAD LOGIC
//			// USE ONE SESSION INSTEAD OF TWO SEPARATE (getById AND THEN saveOrUpdate)
//			
//			Long saved_user_id = (Long) httpSession.getAttribute("saved_user_id");
//			UserDao userDAO = new UserDao();
//			log.debug("[doPost] saved_user_id is: {}", saved_user_id); // null or 83 etc
//			User newUser = new User();  //  = new User();
//			if(saved_user_id != null){
//				log.debug("[doPost] saved user id NOT NULL: {}, UPDATING EXISTING", saved_user_id);
//				User findUser = userDAO.getUserById(saved_user_id);
//				if(findUser != null){
//					log.debug("[doPost] ACTUALLY FOUND A USER: {}", findUser);
//					newUser = findUser;
//				}else{
//					log.debug("[doPost] DID NOT FIND A USER BY ID!");
//				}
//			}else{
//				log.debug("[doPost] existinguser null!!!");
//			}
//			log.debug("[doPost] going to set username to: " + userName);
//			newUser.setName(userName);
//			newUser.setUser_sectors(userSectors);
//			newUser.setAgreedToTerms(checkbox_checked);
//			LocalDateTime date = LocalDateTime.now();
//			log.debug("[doPost] going to save date: {}", date);
//			newUser.setDateAdded(date);
//			userDAO.addOrUpdateUser(newUser); //addUser(newUser); // TO DO ERROR org.hibernate.NonUniqueObjectException: A different object with the same identifier value was already associated with the session : [model.Sector#41]
//			log.debug("[doPost] saved user id: {}", newUser.getId());
//			httpSession.setAttribute("saved_user_id", newUser.getId());
//			
//
//			
//			// redirect here?
//		}catch (Exception e) {
//			log.error("Error adding or updating user", e); //should add e.printStackTrace(); automatically
//		}



// OLD NON WORKING:
//if(saved_user_id == null){	// ???
//log.debug("[doPost] saved user id NULL, CREATING NEW USER");
//User newUser = new User();
//newUser.setName(userName);
//newUser.setUser_sectors(userSectors);
//newUser.setAgreedToTerms(checkbox_checked);
//LocalDateTime date = LocalDateTime.now();
//log.debug("[doPost] going to save date: {}", date);
//newUser.setDateAdded(date);
//userDAO.addUser(newUser); // TO DO ERROR org.hibernate.NonUniqueObjectException: A different object with the same identifier value was already associated with the session : [model.Sector#41]
//log.debug("[doPost] saved user id: {}", newUser.getId());
//httpSession.setAttribute("saved_user_id", newUser.getId());
//}else{ // ???
//log.debug("[doPost] saved user id NOT NULL: {}, UPDATING EXISTING", saved_user_id);
//User existingUser = userDAO.getUserById(saved_user_id);
//if(existingUser != null){
//	log.debug("[doPost] existinguser NOT NULL, existing user name: {}, new username: {}", existingUser.getName(), userName);
//	existingUser.setName(userName); //(String) session.getAttribute("userName"));
//	userDAO.updateUser(existingUser);
//}else{
//	log.debug("[doPost] existinguser null!!!");
//}
//}


//userDAO.addUserDetails(userName); //, password, email, phone, city);

//private static final boolean ON = true;

// POST:
//String checkbox_checked = request.getParameter("accept_terms");	
//if(checkbox_checked == null){
//	System.out.println("checkbox NOT checked");
//}else{
//	System.out.println("checkbox IS checked");
//}

// GET:
//private List<User> getAllUsers(){ //(HttpServletRequest request){
//List<User> allUsers = new ArrayList<User>();
//dao.UserDao userDao = new dao.UserDao();
////try {
//	allUsers = userDao.getAllUsers();
////} catch (SQLException e) {
////	e.printStackTrace();
////}
//return allUsers;
//}	

//private List<Sector> getAllSectorsLevel0(HttpServletRequest request){
//private List<Sector> getAllSectorsLevel0(){	
//	System.out.println("[Default][getAllSectorsLevel0]");
//	List<Sector> allSectors = new ArrayList<Sector>();
//	dao.SectorDao sectorDao = new dao.SectorDao();
//	allSectors = sectorDao.findAllLevel0();
//	return allSectors;
//}

// LITERALLY GETS ALL (regardless of level/group)
/*
private List<Sector> getAllSectors(){ //(HttpServletRequest request){	
	System.out.println("[Default][getAllSectors]");
	List<Sector> allSectors = new ArrayList<Sector>();
	dao.SectorDao sectorDao = new dao.SectorDao();
	//try {
		allSectors = sectorDao.getAllSectors();
	//} catch (SQLException e) {
	//	e.printStackTrace();
	//}
	return allSectors;
}
 */

//used in doGET: doStuff(httpSession); //doStuff(request);	
//	private void doStuff(HttpSession session){ //private void doStuff(HttpServletRequest request){
//		//String behaviour = request.getParameter("do");
//		//String searchString = request.getParameter("searchString");
//		List<User> displayedUsers = new ArrayList<User>();
//		/*
//		if("delete".equals(behaviour)){
//			UserDAO userDao = new UserDAO();
//			try {
//				//System.out.println("Deleting item that has id " + request.getParameter("id"));
//				userDao.deleteByID(Integer.parseInt(request.getParameter("id")));
//			} catch (NumberFormatException e) {
//				e.printStackTrace();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//		if(searchString == null){
//			displayedUnits = getAllUnits(request);
//		}else{
//			displayedUnits = searchUnits(request);
//		}
//		*/
////		displayedUsers = getAllUsers(request);
//		displayedUsers = getAllUsers();
////		request.setAttribute("displayedUsers", displayedUsers);
//		session.setAttribute("displayedUsers", displayedUsers);
//	}

//doStuffSectors(httpSession); //doStuffSectors(request, session);
//// private void doStuffSectors(HttpServletRequest request, HttpSession session){
//private void doStuffSectors(HttpSession session){
//	List<Sector> displayedSectors = new ArrayList<Sector>();
//	//displayedSectors = getAllSectors(request);
////	displayedSectors = getAllSectorsLevel0(request);
//	displayedSectors = getAllSectorsLevel0();
////	request.setAttribute("displayedSectors", displayedSectors);
//	
//	// Cannot serialize session attribute displayedUsers for session EF876FDBDE448EE29D4F0051B72B35FE
//	// TO DO FIX java.io.NotSerializableException: model.User
//	// java.io.NotSerializableException: model.Sector and model.User
//	//session.setAttribute("displayedSectors", displayedSectors);
//	session.setAttribute("displayedSectors", new ArrayList<Sector>(displayedSectors));
//}

//request.getRequestDispatcher("WEB-INF/index.jsp").forward(request, response);
// WAS HERE request.getRequestDispatcher("index.jsp").forward(request, response);

// The client won't get the request back
//request.setAttribute("SESSIONuserName", userName);
//request.setAttribute("SESSIONcheckbox_checked", checkbox_checked);
//request.setAttribute("SESSIONselectedSectors", selectedSectors);

//httpSession.setAttribute("SESSIONuserName", userName);
//httpSession.setAttribute("SESSIONcheckbox_checked", checkbox_checked);
//httpSession.setAttribute("SESSIONselectedSectors", selectedSectors);

// FIX using (String) case sometimes java.lang.ClassCastException: java.io.ObjectStreamClass cannot be cast to java.lang.String
//String userName = (String) httpSession.getAttribute("userName"); // Breaks it if not assigned -> .toString(); //session.getAttribute("userName").toString(); // http://stackoverflow.com/questions/3521026/java-io-objectstreamclass-cannot-be-cast-to-java-lang-string


//String userName = "";
//if(httpSession.getAttribute("userName") != null){
//	userName = (String) httpSession.getAttribute("userName"); //.toString();
//	System.out.println("[Default][doGet] Session has attribute userName: " + userName);
//}else{
//	System.out.println("[Default][doGet] no userName ");
//}
