//package com.kodnest.tunehub.controller;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//
//@Controller
//public class PaymentController {
//
//	@GetMapping("/pay")
//	public String pay() {
//		return "pay";
//	}
//}


/*package com.kodnest.tunehub.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kodnest.tunehub.service.UserService;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import jakarta.servlet.http.HttpSession;

@Controller
public class PaymentController {
	@Autowired
	UserService userService;

	@GetMapping("/pay")
	public String pay() {
		return "pay";
	}

	@SuppressWarnings("finally")
	@PostMapping("/createOrder")
	@ResponseBody
	public String createOrder(HttpSession session) {

		int  amount  = 1;
		Order order=null;
		try {
			RazorpayClient razorpay=new RazorpayClient("rzp_test_DSvCPkteIt8Kpk", "YrNSgTmjeEy1Z7nBQf2JLMId");

			JSONObject orderRequest = new JSONObject();
			orderRequest.put("amount", amount*100); // amount in the smallest currency unit
			orderRequest.put("currency", "INR");
			orderRequest.put("receipt", "order_rcptid_11");

			order = razorpay.orders.create(orderRequest);

			//extra
			//			String mail =  (String) session.getAttribute("email");
			//			User user = userService.getUser(mail);
			//			user.setIspremium(true);
			//			userService.updateUser(user);

			@PostMapping("/verify");
			@ResponseBody
			public boolean verifyPayment(@RequestParam String orderId, 
					@RequestParam String paymentId,
					@RequestParam String signature) 
			{
				try {
					RazorpayClient razorpayClient = new RazorpayClient("rzp_test_DSvCPkteIt8Kpk" , "YrNSgTmjeEy1Z7nBQf2JLMId");
					String verificationData = orderId + " " + paymentId;
					boolean isValidSignature = Units.verifySignature(verificationData , signature , "YrNSgTmjeEy1Z7nBQf2JLMId");
					return isValidSignature;
				}catch (RazorpayException e) {
					e.printStackTrace();
				}
			}
			//finally {
			//	return order.toString();
			//}
			
			@GetMapping("/payment-success")
			public String paymentSuccess(HttpSession session) {
				String mail =  (String) session.getAttribute("email");
				User user = userService.getUser(mail);
				user.setIspremium(true);
				userService.updateUser(user);
				return "customerhome";
			}
			@GetMapping("/payment-failure")
			public String paymentfailure() {
				return null;
			}
		}	
		
*/

package com.kodnest.tunehub.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kodnest.tunehub.entity.User;
import com.kodnest.tunehub.service.UserService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;

import jakarta.servlet.http.HttpSession;

@Controller
public class PaymentController {
	@Autowired
	UserService userService;

	@GetMapping("/pay")
	public String pay() {
		return "pay";
	}

	@SuppressWarnings("finally")
	@PostMapping("/createOrder")
	@ResponseBody
	public String createOrder(HttpSession session) {

		int  amount  = 1;
		Order order=null;
		try {
			RazorpayClient razorpay=new RazorpayClient("rzp_test_hJybW6aB2JWXGc", "rGPTEDIQzbqTM7S79B5PVkGc");

			JSONObject orderRequest = new JSONObject();
			orderRequest.put("amount", amount*100); // amount in the smallest currency unit
			orderRequest.put("currency", "INR");
			orderRequest.put("receipt", "order_rcptid_11");

			order = razorpay.orders.create(orderRequest);

//			String mail =  (String) session.getAttribute("email");
//			User user = userService.getUser(mail);
//			user.setIspremium(true);
//			userService.updateUser(user);
			

		} catch (RazorpayException e) {
			e.printStackTrace();
		}
		finally {
			return order.toString();
		}
	}
	
	
	@PostMapping("/verify")
	@ResponseBody
	public boolean verifyPayment(@RequestParam  String orderId, @RequestParam String paymentId,
											@RequestParam String signature) {
	    try {
	        // Initialize Razorpay client with your API key and secret
	        RazorpayClient razorpayClient = new RazorpayClient("rzp_test_hJybW6aB2JWXGc", 
	        								"rGPTEDIQzbqTM7S79B5PVkGc");
	        // Create a signature verification data string
	        String verificationData = orderId + "|" + paymentId;

	        // Use Razorpay's utility function to verify the signature
	        boolean isValidSignature = Utils.verifySignature(verificationData, signature, 
	        													"rGPTEDIQzbqTM7S79B5PVkGc");

	        return isValidSignature;
	    } catch (RazorpayException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	//payment-success -> update to premium user
	@GetMapping("/payment-success")
	public String paymentSuccess(HttpSession session){
		
		String email = (String) session.getAttribute("email");
		User user = userService.getUser(email);
		user.setIspremium(true);
		userService.updateUser(user);
		return "customerhome";
	}
	
	
	//payment-failure -> redirect to login 
	@GetMapping("/payment-failure")
	public String paymentFailure(){
		//payment-error page
		return "customerhome";
	}
	
	
	
		
}