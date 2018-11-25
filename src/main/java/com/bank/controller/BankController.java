package com.bank.controller;

import java.io.IOException;
import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.bank.dao.AccountDao;
import com.bank.model.Account;
import com.bank.model.Address;
import com.bank.model.Customer;
import com.bank.model.InternetBankingUser;
import com.bank.model.Login;
import com.bank.model.Payee;
import com.bank.service.IAccountService;
import com.bank.service.IFundTransferService;

@Controller
public class BankController
{
	@Autowired
	private IAccountService accountService;
	
	@Autowired IFundTransferService fundTransferService;
	
	@RequestMapping("/open")
	public ModelAndView openAccount(ModelAndView model, @ModelAttribute Customer customer, Account account, Address address)
	{
		
		account.setCustomer_id(customer.getCustomer_id());
		
		
	   int i =  accountService.openAccount(customer,account,address);
		
		
		model.setViewName("Register");
		
		return model;
		
		
		
		
	}
	
	
	@RequestMapping("/register")
	public ModelAndView register(ModelAndView model, @ModelAttribute InternetBankingUser ibu)
	{
		
		//CHECK IF ACCOUNT NUMBER EXISTS
		
      
		
		
		//REGISTER IF IT EXISTS
		int i = accountService.registerOnline(ibu);
		
		if(i>0)
		{
			
		//model.addObject("account_number", ibu.getAccount_number());		
			
		model.setViewName("Login");
		
		
		}
		else
			model.setViewName("Register");
		
		
		return model;
		
		
		
		
	}
	
	@RequestMapping("/login")
	public ModelAndView login(ModelAndView model, @ModelAttribute Login login)
	{
		
		

		if(accountService.validateUser(login))
		{
			
			
			
			model.setViewName("Dashboard");

		}
		else
		{
			model.setViewName("Login");

		}
			
		
		return model;		
		
		
		
	}
	
	
	@RequestMapping("/addPayee")
	public ModelAndView addPayee(ModelAndView model, @ModelAttribute Payee payee)
	{
		
		
		
		int i = fundTransferService.addPayee(payee);
		
		if(i>0)
			model.setViewName("Dashboard");
			
		else			
		model.setViewName("Payee");
		
		
		return model;
		
		
		
		
	}
	
	
	
	
	
}