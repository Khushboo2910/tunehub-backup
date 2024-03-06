package com.kodnest.tunehub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kodnest.tunehub.entity.Song;
import com.kodnest.tunehub.entity.User;
import com.kodnest.tunehub.service.SongService;
import com.kodnest.tunehub.serviceimpl.UserServiceImpl;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

/*	@PostMapping("/register")
    public String addUser(@RequestParam("username") String username , 
	  						@RequestParam("email") String email , 
	  						@RequestParam("password") String password , 
	  						@RequestParam("gender") String gender , 
	  						@RequestParam("role") String role , 
	  						@RequestParam("address") String address) {
    	System.out.println(username +" "+ email +"  "+ password +" "
    		+ gender +" "+ role +" "+ address);	
		return "home";
}*/
/*	@Autowired
	UserServiceImpl serviceImpl;
	@PostMapping("/register")
	public String addUser(@ModelAttribute User user) {
    	System.out.println(user.getUsername() +" "+ user.getEmail() +"  "+ 
    				user.getPassword() +" "+ user.getGender() +" "+ user.getRole() +" "+ user.getAddress());
	    serviceImpl.addUser(user);
	    return "home";
}*/
	@Autowired
	UserServiceImpl serviceImpl;
	
	@Autowired
	SongService songService;
	
	@PostMapping("/register")
	public String addUser(@ModelAttribute User user) {
		//email taken from registration form
		String email = user.getEmail();
		//checking if the email as entered in registration form is present in DB or not.
		boolean status = serviceImpl.emailExists(email);

		if(status == false) {
			serviceImpl.addUser(user);
			System.out.println("User added");
		}
		else {
			System.out.println("User already exists");
		}
		return "login";
	}

/*	@PostMapping("/validate")
	public String validate() {
		return "home";
	}
*/

	@PostMapping("/validate")
	public String validate(@RequestParam("email") String email ,
						   @RequestParam("password") String password , HttpSession session , Model model) {
		if (serviceImpl.validateUser(email , password) == true){
			//System.out.println("Valid user --> Home");
			String role = serviceImpl.getRole(email);
			
			session.setAttribute("email", email);
			
			if(role.equals("admin")) {
				return "adminhome";
			}
			else {
				User user = serviceImpl.getUser(email);
				boolean userstatus = user.isIspremium();
				
				//logic for fatch songs
				List<Song> fetchAllSongs = songService.fetchAllSongs();
				model.addAttribute("songs" , fetchAllSongs);
				
				model.addAttribute("ispremium" , userstatus);
				return "customerhome";
			}
		}else {
			return "login";
		}
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "login";
	}
	
}    


