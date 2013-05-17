package org.neuro4j.example.web.mng;

import java.util.List;

import org.neuro4j.example.web.common.CreateException;

public interface AccountMng {
	
	public List<Account> getAccountList();
	
	public void removeAccount(String uuid);
	
	public void updateAccount(Account account);
	
	public Account createAccount(String firstName, String lastName) throws CreateException;
	
}
