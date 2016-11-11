package org.launchcode.blogz.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.launchcode.blogz.models.User;
import org.launchcode.blogz.models.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AuthenticationController extends AbstractController {
	
	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String signupForm() {
		return "signup";
	}
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String signup(HttpServletRequest request, Model model) {
		
		// TODO - implement signup
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String verify = request.getParameter("verify");
		
		if(User.isValidUsername(username) == false){
			model.addAttribute("username_error", "That is an invalid username.");
			model.addAttribute("username", username);
			return "signup";
		}
		if(User.isValidPassword(password) == false){
			model.addAttribute("password_error", "That is an invalid password.");
			model.addAttribute("username", username);
			return "signup";
		}
		if(!password.equals(verify)){
			model.addAttribute("verify_error", "The passwords do not match.");
			model.addAttribute("username", username);
			return "signup";
		}
		
		User user = new User(username, password);
		userDao.save(user);
		
		HttpSession thisSession = request.getSession();
		this.setUserInSession(thisSession, user);
		
		return "redirect:blog/newpost";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginForm() {
		return "login";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(HttpServletRequest request, Model model) {
		
		// TODO - implement login
		String username = request.getParameter("username");
		User user = userDao.findByUsername(username);
		String password = request.getParameter("password");
		
		if(user == null){
			model.addAttribute("error", "Username not found.");
			model.addAttribute("username", username);
			return "login";
		}
		if(user.isMatchingPassword(password) == false){
			model.addAttribute("error", "Password is incorrect.");
			model.addAttribute("username", username);
			return "login";
		}
		
		HttpSession thisSession = request.getSession();
		this.setUserInSession(thisSession, user);
		
		return "redirect:blog/newpost";
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request){
        request.getSession().invalidate();
		return "redirect:/";
	}
}
