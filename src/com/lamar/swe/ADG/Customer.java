/*
 * CPSC-5360 Software Engineering, 2015 Spring /Project
 * 
 * Author: Shaomin (Samuel) Zhang
 * 
 * Email : smicn@foxmail.com
 * */
package com.lamar.swe.ADG;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Random;

public class Customer implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3658818417273466178L;

	private String mAccountNumber;
	
	private String mName;
	
	private String mID;
	
	public String mDOB;
	
	public String mBankAccountNumber;
	
	@SuppressWarnings("unused")
	private String mPasswordHash;
	
	public String mAddress;
	
	public String mPhoneNumber;
	
	public String mEmailAddress;
	
	private String mSerialFilePath;
	
	private void autoGen() {
		
		Random r = new Random(System.currentTimeMillis());
		
		/*
		 * ID is a random 8 digit number
		 * */
		mID = "";
		for (int ii = 0; ii < 8; ii++) {
			mID += Integer.toString(r.nextInt(10));
		}
		
		/*
		 * account number is starting with "C" and then with ID
		 * */
		mAccountNumber = "C" + mID;
		
		/*
		 * randomly pick up pairs of first and second names
		 **/
		final String namesFirst[] = {
				"JAMES",
				"JOHN",
				"ROBERT",
				"MICHAEL",
				"WILLIAM",
				"DAVID",
				"RICHARD",
				"CHARLES",
				"JOSEPH",
				"THOMAS",
				"CHRISTOPHER",
				"DANIEL",
				"PAUL",
				"MARK",
				"LAWRENCE",
				"NICHOLAS",
				"BENJAMIN",
				"BRUCE",
				"BRANDON",
				"ADAM",
				"HARRY",
				"FRED",
				"WAYNE",
				"BILLY",
				"STEVE",
				"LOUIS",
				"JEREMY",
		};
		final String namesLast[] = {
				"SMITH",
				"JOHNSON",
				"BROWN",
				"JONES",
				"MILLER",
				"DAVIS",
				"GARCIA",
				"WILSON",
				"TAYLOR",
				"THOMAS",
		};
		String firstName = namesFirst[ r.nextInt(namesFirst.length) ];
		String lastName  = namesLast [ r.nextInt(namesLast.length)  ];
		mName = firstName + " " + lastName;
		
		/*
		 * DOB, random select a day after 01/01/1970
		 * **/
		mDOB = Integer.toString( 1 + r.nextInt(12) ) + "/" +
			   Integer.toString( 1 + r.nextInt(28) ) + "/" +
			   Integer.toString( 1970 + r.nextInt(30));
		
		/*
		 * Email: gives everybody an email address according his name
		 * */
		mEmailAddress = (firstName + lastName.substring(0, 1)).toLowerCase() + "@gmail.com";
		
		/*
		 * Address: let people live near Lamar University
		 **/
		mAddress = Integer.toString(r.nextInt(1200)) + " Georgia St, apt " +
				Integer.toString(r.nextInt(10)) + "\nBeaumont TX 77706";
		
		/*
		 * Bank Account Number: a 16 digit number
		 * **/
		mBankAccountNumber = "8";
		for (int ii = 0; ii < 15; ii++) {
			mBankAccountNumber += Integer.toString(r.nextInt(10));
		}
		
		/*
		 * Phone number: give everyone a beaumont phone number
		 **/
		mPhoneNumber = "409-";
		for (int ii = 0; ii < 3; ii++) {
			mPhoneNumber += Integer.toString(r.nextInt(10));
		}
		mPhoneNumber += "-";
		for (int ii = 0; ii < 4; ii++) {
			mPhoneNumber += Integer.toString(r.nextInt(10));
		}
		
		/*
		 * The path where to load this object
		 * */
		mSerialFilePath = "data/" + mAccountNumber + ".dat";
	}
	
	public Customer() {
		autoGen();
	}
	
	public Customer(String ID, String accountNumber) {
		mID = ID;
		mAccountNumber = accountNumber;
	}
	
	public Customer(String serialFilePath) {
		mSerialFilePath = serialFilePath;
	}
	
	public String getAccountNumber() {
		return mAccountNumber;
	}
	
	public String getName() {
		return mName;
	}
	
	public String getSerialFilePath() {
		return mSerialFilePath;
	}
	
	public void setName(String name) {
		mName = name.toUpperCase();
		
		String[] names = name.split(" ");
		if (names.length > 1) {
			mEmailAddress = (names[0] + names[1].substring(0, 1)).toLowerCase();
		}
		else {
			mEmailAddress = name.toLowerCase();
		}
		
		mEmailAddress += "@gmail.com";
	}
	
	public void setSerialFilePath(String path) {
		mSerialFilePath = path;
	}
	
	public void serialize() {
		try {
			FileOutputStream fout = new FileOutputStream(mSerialFilePath);
			ObjectOutputStream out = new ObjectOutputStream(fout);
			out.writeObject(this);
			out.close();
			fout.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
