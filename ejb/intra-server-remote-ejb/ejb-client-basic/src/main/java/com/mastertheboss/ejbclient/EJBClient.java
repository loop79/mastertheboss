 
package com.mastertheboss.ejbclient;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.mastertheboss.ejb.*;
import com.mastertheboss.exception.*;


import javax.naming.spi.NamingManager;
import javax.ejb.Stateless;
@Stateless
public class EJBClient {

	public float callRemoteEJBs(long money) throws Exception {
		Calculator calculator = lookupCalculatorEJB();
		Account account = lookupAccountEJB();
		System.out.println("Create Account with "+money);

		account.createAccount(money);
		System.out.println("Deposit " +(money/2));
		account.deposit(money/2);

		try {
			System.out.println("Withdraw "+(money/3));
			account.withdraw(money/3);
		} catch (InsufficientFundsException e) {

			e.printStackTrace();
		}
		money = account.getMoney();
		System.out.println("Money left " + money);
		float totalMoney = calculator.calculateInterest(money);
		System.out.println("Money plus interests " + totalMoney);
		return totalMoney;



	}

	private static Calculator lookupCalculatorEJB() throws NamingException {
	      final Hashtable<String, String> jndiProperties = new Hashtable<>();
        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
            //use HTTP upgrade, an initial upgrade requests is sent to upgrade to the remoting protocol
            jndiProperties.put(Context.PROVIDER_URL,"remote+http://localhost:8080");
      final Context context = new InitialContext(jndiProperties);

		return (Calculator) context
				.lookup("ejb:/ejb-server-basic/CalculatorEJB!com.mastertheboss.ejb.Calculator");
	}

	private static Account lookupAccountEJB() throws NamingException {

		final Hashtable<String, String> jndiProperties = new Hashtable<>();
		jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
		//use HTTP upgrade, an initial upgrade requests is sent to upgrade to the remoting protocol
		jndiProperties.put(Context.PROVIDER_URL,"remote+http://localhost:8080");
		final Context context = new InitialContext(jndiProperties);

		return (Account) context
				.lookup("ejb:/ejb-server-basic/AccountEJB!com.mastertheboss.ejb.Account?stateful");
	}
}
