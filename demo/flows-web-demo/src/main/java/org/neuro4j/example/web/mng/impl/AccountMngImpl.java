package org.neuro4j.example.web.mng.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.neuro4j.example.web.common.CreateException;
import org.neuro4j.example.web.mng.Account;
import org.neuro4j.example.web.mng.AccountMng;

public class AccountMngImpl implements AccountMng {

	static Random randomFirstName = new Random(); 
	static Random randomLastName = new Random(); 
	private static final  String[] FIRST_NAMES = new String[]{"Jonn", "Jason", "Anna", "Katrin", "Charlotte"};
	private static final String[] LAST_NAMES = new String[]{"Richmond", "Hanson", "Berhoff", "Black", "Branson"};
	
	private static Map<String, Account> accounts = new HashMap<String, Account>();
	
	
	private static AccountMng instance = new AccountMngImpl();
	
	private AccountMngImpl()
	{
		init();
	}
	
	
	private static void init() {
		
		for (int i = 1; i < 6; i++)
		{
			Account acount =  new Account(getRandomFromArray(randomFirstName, FIRST_NAMES), getRandomFromArray(randomLastName, LAST_NAMES));
			accounts.put(acount.getUuid(), acount);
		}
		
	}

	public static AccountMng getInstance() {
		return instance;
	}

	public List<Account> getAccountList() {
		List<Account> list = new ArrayList<Account>(accounts.size());
		Collection<Account> values = accounts.values();
		list.addAll(values);
		return list;
	}
	

	public void removeAccount(String uuid) {
		accounts.remove(uuid);	
	}

	public void updateAccount(Account account) {
		accounts.put(account.getUuid(), account);	
	}

	public Account createAccount(String firstName, String lastName)
			throws CreateException {
		
		if (firstName == null || lastName == null)
		{
			throw new CreateException("FirstName and LastName should not be empty");
		}
		Account acount =  new Account(firstName, lastName);
		accounts.put(acount.getUuid(), acount);
		return acount;
	}
	
	
	
	private static  String getRandomFromArray(Random rand, String[] array)
	{
		int index = rand.nextInt(array.length); 
		return array[index];
	}

}
