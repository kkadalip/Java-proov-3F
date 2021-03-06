package controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
//import java.text.ParseException;
//import java.text.DateFormat;
//import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
//import java.util.LinkedHashMap;
import java.util.List;
//import java.util.Locale;
//import java.util.ResourceBundle;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
//import java.util.SortedSet;
//import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import javax.servlet.jsp.jstl.core.Config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import dao.BankUtil;
import model.Currency;
import model.Result;

@WebServlet("")
public class Default extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Logger log = LoggerFactory.getLogger(Default.class); // info trace debug warn error

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("[doGet] START");
		//HttpSession httpSession = request.getSession(true);
		String selectedLanguage = request.getParameter("language");
		if(selectedLanguage != null){
			log.debug("[doGet] have selectedLanguage, it is: " + selectedLanguage);
		}else{
			log.debug("[doGet] selectedLanguage is null, setting it to english as default");
			selectedLanguage = "en";
			request.setAttribute("language", selectedLanguage);
		}
		String sessionDateFormat = "dd.MM.yyyy";
		String sessionDate = (String) request.getParameter("date"); // selectedDate
		if(sessionDate == null || sessionDate.isEmpty()){
			//sessionDate = "30.12.2010";
			log.debug("[doGet] sessionDate null (not in params/attrs)!, setting it as yesterday");
			DateFormat dateFormat = new SimpleDateFormat(sessionDateFormat); //("yyyy/MM/dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -1); // YESTERDAY
			sessionDate = dateFormat.format(cal.getTime());
			log.debug("[doGet] session date is now " + sessionDate);
		}
		request.setAttribute("selectedD", sessionDate);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(sessionDateFormat); //DateFormat format = new SimpleDateFormat("dd.MM.yy");
		LocalDate sessionDateAsLocalDate = null;
		try{
			sessionDateAsLocalDate = LocalDate.parse(sessionDate, formatter);
		}catch(DateTimeParseException e){
			log.error("[doGet] LocalDate parse exception");
		}
		if(sessionDateAsLocalDate != null){
			// UNSORTED DISPLAYED CURRENCIES WITHOUT TRANSLATIONS!!!!!
			List<String> displayedCurrencies = BankUtil.downloadAllForDate(getServletContext(), sessionDateAsLocalDate); //"30.12.2010");
			Locale selectedLocale = new Locale(selectedLanguage);
			ResourceBundle textBundle = ResourceBundle.getBundle("text",selectedLocale);

			List<Currency> currenciesWithTranslations= new ArrayList<Currency>();
			//SortedSet<Currency> currenciesWithTranslations = new TreeSet<Currency>();
			for(String currencyShortName : displayedCurrencies){
				// TRANSLATE FOR LOCALE:
				String translation;
				try{
					log.debug("[doGet] get translation for currency." + currencyShortName);
					translation = textBundle.getString("currency."+currencyShortName);
					log.debug("[doGet] translation: " + translation);
				}catch(MissingResourceException e){
					log.error("No error translation for " + currencyShortName, e);
					translation = "?";
				}
				Currency returnCurrency = new Currency(currencyShortName, translation);
				currenciesWithTranslations.add(returnCurrency);
			}
			Collections.sort(currenciesWithTranslations);

			request.setAttribute("displayedCurrencies", currenciesWithTranslations);
			//request.setAttribute("displayedCurrencies", displayedCurrencies);
		}
		request.getRequestDispatcher("jsp/index.jsp").forward(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("[doPost] START");
		//HttpSession httpSession = request.getSession(true);
		boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
		if(ajax){
			log.debug("[doPost] AJAX POST!!!");
			// Handle ajax (JSON) response.
			List<String> errors = new ArrayList<String>(); // if no errors... do the calculations etc...

			String inputMoneyAmount = request.getParameter("inputMoneyAmount");
			if(inputMoneyAmount == null || inputMoneyAmount.isEmpty()){
				log.error("[doPost] inputMoneyAmount NULL or empty!");
				errors.add("error.inputMoneyEmpty");//errors.add("Input money amount is empty!");
			}else{
				log.debug("[doPost] inputMoneyAmount: " + inputMoneyAmount);
			}

			String inputCurrency = request.getParameter("inputCurrency");
			log.debug("[doPost] inputCurrency: " + inputCurrency); // rate
			String outputCurrency = request.getParameter("outputCurrency");
			log.debug("[doPost] outputCurrency: " + outputCurrency);
			
			// LOCALE & TRANSLATIONS BUNDLE
			String currentLang = request.getParameter("lang");
			log.debug("currentLang is " + currentLang);
			Locale selectedLocale = new Locale(currentLang); //(selectedLanguage);
			ResourceBundle textBundle = ResourceBundle.getBundle("text",selectedLocale);

			String selectedDate = request.getParameter("selectedD"); // selectedDate // BEFORE WAS IN FORM, NOW ADDING TO SERIALIZED FORM IN JS
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy"); //DateFormat format = new SimpleDateFormat("dd.MM.yy");
			if(selectedDate != null){
				log.debug("[doPost] selectedDate is " + selectedDate);
				// CHECK IF SELECTED DATE IS GOOD (ADD TO SEPARATE METHOD LATER)
				if(selectedDate == null || selectedDate.isEmpty()){
					errors.add("error.selectDate"); //errors.add("Select a date!");
				}else{
					log.debug("[doPost] selectedDate: " + selectedDate);
					SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy");
					format.setLenient(false);
					try {
						Date date = format.parse(selectedDate);
						Calendar cal = Calendar.getInstance();
						cal.add(Calendar.DATE, -1);
						System.out.println("Yesterday's date = "+ cal.getTime());
						// IF SELECTED DATE IS AFTER YESTERDAY
						if(date.after(cal.getTime())){
							errors.add("error.notTodayOrFuture"); //("Date cannot be today or in the future!");
						}
					}catch(ParseException e){
						log.error("[doPost] Can't parse date",e);
						errors.add("error.dateFormatWrong"); //("Date format wrong!");
					}
				}
				// Try to parse selected date
				if(errors.isEmpty()){
					LocalDate selectedDateAsLocalDate = null;
					try{
						selectedDateAsLocalDate = LocalDate.parse(selectedDate, formatter);
					}catch(DateTimeParseException e){
						log.error("[doPost] could not parse date!");
						errors.add("error.dateFormatWrong");
					}
					if(errors.isEmpty()){
						log.debug("NO ERRORS, CONTINUING doPost!");
						Float inputMoneyAmountFloat = null;
						try{
							inputMoneyAmountFloat = Float.parseFloat(inputMoneyAmount);
						}catch(NumberFormatException e){ // java.lang.NumberFormatException
							log.error("[doPost] number field to float FAILED!",e);
							errors.add("error.inputAmountFormatWrong"); //("Input amount doesn't have correct format!");
						}
						if(inputMoneyAmountFloat != null){
							BankUtil bu = new BankUtil();
							List<Result> results = bu.calculateResults(getServletContext(), inputMoneyAmountFloat, inputCurrency, outputCurrency, selectedDateAsLocalDate); //, selectedDate); //, date);
							// TRANSLATING BANK NAMES IN RESULTS!
							for(Result res : results){
								try{
									String non_translated_bankName = res.get_bankName();
									res.set_bankName(textBundle.getString(non_translated_bankName));
								}catch(MissingResourceException e){
									log.error("No error translation for " + res.get_bankName(), e);
								}
							}
							String json = new Gson().toJson(results);
							log.debug("[doPost] results json: " + json);
							response.setContentType("application/json");
							response.setCharacterEncoding("UTF-8");
							response.getWriter().write(json);
						}
					}
				}
			}else{
				log.error("[doPost] selectedDate is NULL!");
			}
			// DISPLAYING ERRORS:
			if(!errors.isEmpty()){
				log.debug("HAVE ERRORS, will display them SoonTM");
				List<String> list = new ArrayList<String>();
				//list.add("some example error");

//				String currentLang = request.getParameter("lang");
//				log.debug("currentLang is " + currentLang);
//				Locale selectedLocale = new Locale(currentLang); //(selectedLanguage);
//				ResourceBundle textBundle = ResourceBundle.getBundle("text",selectedLocale);

				///request.setAttribute("displayValues", textBundle);
				//request.setAttribute("language", selectedLanguage);
				for(String error : errors){
					log.debug("[doPost] Errorslist error: " + error);
					//list.add(error);

					// TRANSLATE FOR LOCALE:
					String value;
					try{
						value = textBundle.getString(error);
					}catch(MissingResourceException e){
						log.error("No error translation for " + error, e);
						value = error;
					}
					list.add(value);
				}
				String json = new Gson().toJson(list);
				log.debug("[doPost]  Errors json: " + json);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(json);
			}
		}else{
			log.debug("[doPost] REGULAR POST!!!"); // Handle regular (JSP) response here.
		}
		log.info("[doPost] END");
	}
}








































// SESSION DATE STUFF:
//String sessionDate = (String) httpSession.getAttribute("sessionDate"); // selectedDate
// doGet
//httpSession.setAttribute("selectedDate", sessionDate);
//}else{
//	sessionDate = (String) httpSession.getAttribute("selectedDate");
//}
// doPost
//if(selectedDate != null){
//	httpSession.setAttribute("selectedDate", selectedDate);
//}
//String selectedDate = (String) httpSession.getAttribute("selectedDate");

// end of doGet
//ServletContext context = getContext();
//URL resourceUrl = context.getResource("/WEB-INF/test/foo.txt");

// check session date null?

//List<Currency> displayedCurrencies = Readxml.getCurrencies(getServletContext()); //  add date, get by date, default will be current day (atm the latest possible, later dates need to be disabled!)

//Date sessionDateAsDate = null;
//try {
//	sessionDateAsDate = format.parse(sessionDate);
//} catch (ParseException e) {
//	log.error("Could not parse sessionDate string!", e);
//}

// AT FIRST DOWNLOAD FOR DEFAULT DATE?
//String selectedDate = request.getParameter("selectedDate");
//log.debug("[doGet] selectedDate: " + selectedDate);
//List<Currency> displayedCurrencies = Readxml.downloadAllForDate(getServletContext(), sessionDate); //"30.12.2010");
//request.setAttribute("displayedCurrencies", displayedCurrencies);


//DELETE LATER
//if(httpSession.getAttribute("language")=="en"){
//httpSession.setAttribute("language","et");
//Config.set( httpSession, Config.FMT_LOCALE, new java.util.Locale("et") );// en_US
//}else{
//httpSession.setAttribute("language","en");
//Config.set( httpSession, Config.FMT_LOCALE, new java.util.Locale("en") );// en_US
//}

//Locale defaultLocale = Locale.getDefault();
//Locale englishLocale = new Locale("en"); //, "US");
//Locale estonianLocale = new Locale("et");
//ResourceBundle bundle1 = ResourceBundle.getBundle("currencies",englishLocale);
//request.setAttribute("displayValues", bundle1);

//Locale selectedLocale = new Locale(selectedLanguage);
//ResourceBundle textBundle = ResourceBundle.getBundle("text",selectedLocale);
///request.setAttribute("displayValues", textBundle);
//request.setAttribute("language", selectedLanguage);

//httpSession.setAttribute("language", "en");

// Check that date isn't in the future NOR today:
//if(date.after(new Date())){
//	errors.add("Date cannot be in the future!");
//}

// doPost REGULAR POST:
//String selectedLanguage = request.getParameter("language");
//log.debug("[doPost] POST SELECTED language is: " + selectedLanguage);
////httpSession.setAttribute("language", selectedLanguage);
//request.setAttribute("language", selectedLanguage);
//doGet(request, response);
////response.sendRedirect("");
////request.setAttribute("language", selectedLanguage);
////request.getRequestDispatcher("jsp/index.jsp").forward(request, response);


//log.debug("AUD:" + bundle1.getString("currency.AUD"));
//log.debug("NOK:" + bundle1.getString("currency.NOK"));	

//String selectedLanguage = (String) httpSession.getAttribute("language");
//String selectedLanguage = (String) request.getAttribute("language");

// FROM doGet:
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


// FROM doPost:
//Float inputCurrencyFloat = Float.parseFloat(inputCurrency);
//Float outputCurrencyFloat = Float.parseFloat(outputCurrency);

//response.sendError(400);
//response.setStatus(404);
//response.setHeader("Answertype", "error");

//request.setAttribute("whatevers", results);
//request.getRequestDispatcher("/WEB-INF/xml/whatevers.jsp").forward(request, response);

/*
String inputMoneyAmount = request.getParameter("inputMoneyAmount");
log.debug("inputMoneyAmount: " + inputMoneyAmount);
String inputCurrency = request.getParameter("inputCurrency");
log.debug("inputCurrency: " + inputCurrency); // rate
String outputCurrency = request.getParameter("outputCurrency");
log.debug("outputCurrency: " + outputCurrency);
String selectedDate = request.getParameter("selectedDate");
log.debug("selectedDate: " + selectedDate);

// java.lang.NumberFormatException: empty String
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
//response.setContentType("text/plain");  // Set content type of the response so that jQuery knows what it can expect.
//response.setCharacterEncoding("UTF-8");
//response.getWriter().write(text);       // Write response body.

// Returning Map<String, String> as JSON

//Map<String, String> options = new LinkedHashMap<String, String>();
//options.put(textOwner, text);
//options.put("value2", "label2");
//options.put("value3", "label3");
//String json = new Gson().toJson(options);

//List<String> results = new ArrayList<String>();
//results.add(text);
//results.add("some other");

List<Result> results = new ArrayList<Result>();
results.add(new Result(textOwner, text));
results.add(new Result("Whatever bank", "some result"));
 */


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
