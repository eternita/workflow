package org.neuro4j.example.web.mng;

import java.util.List;
import java.util.Map;

import org.neuro4j.example.web.common.CreateException;

public interface AccountMng {
	
	
	public  Map<String, Account> init();
	
	public List<Account> getAccountList(Map<String, Account> accounts);
	
	public void removeAccount(Map<String, Account> accounts, String uuid);
	
	public void updateAccount(Map<String, Account> accounts, Account account);
	
	public Account createAccount(Map<String, Account> accounts, String firstName, String lastName) throws CreateException;
	
}
