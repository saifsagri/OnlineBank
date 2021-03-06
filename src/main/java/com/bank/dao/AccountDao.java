package com.bank.dao;


import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessException.*; 
import org.springframework.jdbc.core.ResultSetExtractor;import org.springframework.jdbc.core.RowMapper;

import com.bank.model.Account;
import com.bank.model.Address;
import com.bank.model.Customer;
import com.bank.model.InternetBankingUser;
import com.bank.model.Login;
import com.bank.model.Payee;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;


public class AccountDao implements IAccountDao
{
	JdbcTemplate jdbcTemplate;
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	public int createAccount(Customer customer, Account account, Address address)
	{
		int i=0,j=0,k=0;

		String getCustomerId= "select GR13_customers_seq.nextval from dual";
		long customerId = getCustomerSeq(getCustomerId);


		String getAccountNumber= "select GR13_accounts_seq.nextval from dual";
		long accountNumber = getCustomerSeq(getAccountNumber);


		String getAddressId= "select GR13_addresses_seq.nextval from dual";
		long addressId = getCustomerSeq(getAddressId);


		String customerTableQuery="insert into GR13_customers values("+customerId+",'"+customer.getFirst_name()+"','"+customer.getMiddle_name()+"','"+customer.getLast_name()+"','"+customer.getFather_name()+"','"+customer.getEmail_id()+"',"+customer.getMobile_number()+","+customer.getAadhar_card()+",'"+customer.getDate_of_birth()+"',"+customer.getAnnual_income()+")";


		//ADD ALL THE CONDITIONS

		i= jdbcTemplate.update(customerTableQuery);




		String accountTableQuery="insert into GR13_accounts values("+accountNumber+",500000,'SAVINGS',"+customerId+")";

		i =  jdbcTemplate.update(accountTableQuery);



		String addressTableQuery="insert into GR13_addresses values("+addressId+",'"+address.getAddress_line_1()+"','"+address.getAddress_line_2()+"',"+address.getPin_code()+",'"+address.getCity()+"','"+address.getState()+"',"+customerId+")";

		//	String query="insert into customers values('"+rf.getUserId()+"','"+rf.getMobileNo()+"','"+rf.getAmount()+"','"+rf.getOperator()+"')";

		i =  jdbcTemplate.update(addressTableQuery);



		//EMAIL ACCOUNT NUMER
		//emailAccountNumber(customer, account, address);

		return i;
	}


	//search account number before this

	public int register(InternetBankingUser ibu)
	{
		int i =0;



		String register="insert into GR13_internet_banking_users values('"+ibu.getUser_id()+"','"+ibu.getLogin_password()+"','"+ibu.getTransaction_password()+"',0,'enabled','"+ibu.getSecurity_questions()+"','"+ibu.getSecurity_answers()+"',"+ibu.getAccount_number()+")";
		i= jdbcTemplate.update(register);		


		return i;

	}


	public boolean login(Login login)
	{


		String validateUserQuery = "select * from GR13_internet_banking_users where GIBU_USER_ID='" + login.getUser_id() + "' and GIBU_LOGIN_PASSWORD='" + login.getPassword() +"'";
		List<InternetBankingUser> users = jdbcTemplate.query(validateUserQuery, new UserMapper());
		return users.size() > 0 ? true : false;



	}


	public long getAccountNumber(Login login) {
		// TODO Auto-generated method stub



		String getAccountNumberQuery="select GIBU_GA_ACCOUNT_NUMBER from gr13_internet_banking_users where gibu_user_id='"+login.getUser_id()+"'"; 

		long accountNumber =jdbcTemplate.queryForObject(getAccountNumberQuery, Long.class); 	 

		System.out.println(accountNumber+" got it?");
		return accountNumber;


	}



	private long getCustomerSeq(String query) {

		long res = jdbcTemplate.queryForObject(query, Long.class);
		return res;
	}


	class UserMapper implements RowMapper<InternetBankingUser> {
		public InternetBankingUser mapRow(ResultSet rs, int arg1) throws SQLException {
			InternetBankingUser user = new InternetBankingUser();
		

			user.setUser_id(rs.getString(1));
			user.setLogin_password(rs.getString(2));


			return user;
		}

	}

	public float checkBalance(long customerAccountNumber){  
		//System.out.println("inside dao"+rf.getUserId());
		String getBalanceQuery="select ga_balance from gr13_accounts where ga_account_number="+customerAccountNumber; 



		float balance = jdbcTemplate.queryForObject(getBalanceQuery, Float.class); 	 

		return balance;
	}
	
	


	public static void emailAccountNumber(Customer customer, Account account, Address address)
	{
		String to = customer.getEmail_id();

		// Sender's email ID needs to be mentioned
		String from = "isaac.m@somaiya.edu";

		// Assuming you are sending email from localhost
		String host = "localhost";

		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.setProperty("mail.smtp.host", "8081");

		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties);

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			// Set Subject: header field
			message.setSubject("Your Account is Activated!");

			// Send the actual HTML message, as big as you like
			message.setContent("<h1>Your Account Number is "+account.getAccount_number()+"</h1>", "text/html");

			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}

}  

