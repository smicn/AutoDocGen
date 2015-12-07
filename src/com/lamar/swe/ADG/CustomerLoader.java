/*
 * CPSC-5360 Software Engineering, 2015 Spring /Project
 * 
 * Author: Shaomin (Samuel) Zhang
 * 
 * Email : smicn@foxmail.com
 * */
package com.lamar.swe.ADG;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Vector;

public class CustomerLoader implements Serializable {

	private static final long serialVersionUID = 3175949133161193299L;
	
	private static CustomerLoader mInst;
	private transient Vector<Customer> mCustomers;
	private Vector<String> mCustomerNames;
	private final static String PATH = "data/customers.dat";
	
	public static CustomerLoader getInstance() {
		if (null == mInst) {
			mInst = new CustomerLoader();
		}
		return mInst;
	}
	
	private CustomerLoader() {
		mCustomerNames = new Vector<String>();
	}

	private static CustomerLoader loadIt() {
		CustomerLoader ldr = null;
		try {
			FileInputStream fin = new FileInputStream(PATH);
			ObjectInputStream in = new ObjectInputStream(fin);
			ldr = (CustomerLoader)in.readObject();
			in.close();
			fin.close();
		} catch (IOException e) {
			DBG.w("CustomerLoader(): " + PATH + " not exist!");
		} catch (ClassNotFoundException e) {
			DBG.e("CustomerLoader class not found!");
			e.printStackTrace();
		}
		return ldr;
	}
	
	public void load() {
		CustomerLoader ldr = loadIt();
		if (ldr != null) {
			mCustomerNames = ldr.mCustomerNames;
		}
		
		mCustomers = new Vector<Customer>();
		
		for (String name : mCustomerNames) {
			String path = "data/" + name + ".dat";
			Customer c = loadCustomer(path);
			mCustomers.add(c);
			
			if (!c.getSerialFilePath().equals(path)) {
				DBG.w("CustomerLoder.load(): warning, inconsistent path: path=" 
						+ path + ", c.path=" + c.getSerialFilePath());
				c.setSerialFilePath(path);
			}
		}
	}
	
	public void save() {
		for (Customer c : mCustomers) {
			saveCustomer(c);
		}
		
		try {
			FileOutputStream fout = new FileOutputStream(PATH);
			ObjectOutputStream out = new ObjectOutputStream(fout);
			out.writeObject(this);
			out.close();
			fout.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Customer loadCustomer(String serialFilePath) {
		Customer c = null;
		try {
			FileInputStream fin = new FileInputStream(serialFilePath);
			ObjectInputStream in = new ObjectInputStream(fin);
			c = (Customer)in.readObject();
			in.close();
			fin.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			DBG.e("CustomerLoader.loadCustomer(): Customer class not found!");
			e.printStackTrace();
		}
		return c;
	}
	
	public void saveCustomer(Customer c) {
		try {
			FileOutputStream fout = new FileOutputStream(c.getSerialFilePath());
			ObjectOutputStream out = new ObjectOutputStream(fout);
			out.writeObject(c);
			out.close();
			fout.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getCustomerCount() {
		return mCustomers.size();
	}
	
	public Customer getCustomer(int index) {
		return mCustomers.get(index);
	}
	
	public Customer[] getCustomers() {
		return (Customer[])mCustomers.toArray();
	}
	
	public boolean addCustomer(Customer c) {
		String accountNumber = c.getAccountNumber();
		for (Customer ci : mCustomers) {
			if (accountNumber.equals(ci.getAccountNumber())) {
				DBG.e("CustomerLoader.addCustomer(): same account-number!");
				return false;
			}
		}
		boolean ret = mCustomerNames.add(accountNumber);
		if (!ret) {
			DBG.e("CustomerLoader.addCustomer(): add name failed!");
			return false;
		}
		ret = mCustomers.add(c);
		if (!ret) {
			DBG.e("CustomerLoader.addCustomer(): add c-obj failed!");
			return false;
		}
		return true;
	}
	
	public boolean deleteCustomer(Customer c) {
		boolean ret = false;
		String accountNumber = c.getAccountNumber();
		for (String name : mCustomerNames) {
			if (name.equals(accountNumber)) {
				ret = mCustomerNames.remove(name);
				break;
			}
		}
		
		if (!ret) {
			DBG.e("CustomerLoader.deleteCustomer(): c.name not found OR names.remove() failed!");
			return false;
		}
		else {
			File file = new File(c.getSerialFilePath());
			file.deleteOnExit();
			
			ret = mCustomers.remove(c);
			if (!ret) {
				DBG.e("CustomerLoader.deleteCustomer(): cs.remove() failed!");
				return false;
			}
		}
		return true;
	}
}
