/*
 * CPSC-5360 Software Engineering, 2015 Spring /Project
 * 
 * Author: Shaomin (Samuel) Zhang
 * 
 * Email : smicn@foxmail.com
 * */
package com.lamar.swe.ADG;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Vector;

public class BillLoader implements Serializable {

	private static final long serialVersionUID = -1149210621489530333L;
	
	private static BillLoader mInst;
	private transient Vector<Bill> mBills;
	private Vector<String> mBillPaths;
	private final static String PATH = "data/bills.dat";
	
	private BillLoader() {
		mBillPaths = new Vector<String>();
	}
	
	public static BillLoader getInstance() {
		if (null == mInst) {
			mInst = new BillLoader();
		}
		return mInst;
	}
	
	private static BillLoader loadIt() {
		BillLoader ldr = null;
		try {
			FileInputStream fin = new FileInputStream(PATH);
			ObjectInputStream in = new ObjectInputStream(fin);
			ldr = (BillLoader)in.readObject();
			in.close();
			fin.close();
		} catch (IOException e) {
			DBG.w("BillLoader(): " + PATH + " not exist!");
		} catch (ClassNotFoundException e) {
			DBG.e("BillLoader class not found!");
			e.printStackTrace();
		}
		return ldr;
	}
	
	public void load() {
		BillLoader ldr = loadIt();
		if (ldr != null) {
			mBillPaths = ldr.mBillPaths;
		}
		
		mBills = new Vector<Bill>();
		
		for (String path : mBillPaths) {
			Bill b = loadBill(path);
			mBills.add(b);
		}
	}
	
	public void save() {
		for (Bill b : mBills) {
			saveBill(b);
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
	
	public Bill loadBill(String serialFilePath) {
		Bill b = null;
		try {
			FileInputStream fin = new FileInputStream(serialFilePath);
			ObjectInputStream in = new ObjectInputStream(fin);
			b = (Bill)in.readObject();
			in.close();
			fin.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			DBG.e("BillLoader.loadBill(): Bill class not found!");
			e.printStackTrace();
		}
		return b;
	}
	
	public void saveBill(Bill b) {
		try {
			FileOutputStream fout = new FileOutputStream(getBillPath(b));
			ObjectOutputStream out = new ObjectOutputStream(fout);
			out.writeObject(b);
			out.close();
			fout.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Bill[] getBills(Customer c) {
		if (null == c) {
			return null;
		}
		Vector<Bill> vb = new Vector<Bill>();
		for (Bill b : mBills) {
			if (b.getCustomer().getAccountNumber().equals(c.getAccountNumber())) {
				vb.add(b);
			}
		}
		return (Bill[])vb.toArray(new Bill[vb.size()]);
	}
	
	public void deleteBills(Customer c) {
		if (null == c) {
			return;
		}
		Vector<Bill> vb = new Vector<Bill>();
		for (Bill b : mBills) {
			if (b.getCustomer().getAccountNumber().equals(c.getAccountNumber())) {
				vb.add(b);
			}
		}
		mBills.removeAll(vb);
	}
	
	public boolean addBill(Bill b) {
		boolean b1, b2, b3;
		for (Bill bi : mBills) {
			if (b == bi) {
				return false;
			}
			else {
				b1 = b.getCustomer() == bi.getCustomer();
				b2 = b.getYear() == bi.getYear();
				b3 = b.getMonth() == bi.getMonth();
				if (b1 && b2 && b3) {
					DBG.e("BillLoader.addBill(): totally same bill!");
					return false;
				}
			}
		}
		b1 = mBillPaths.add(getBillPath(b));
		if (!b1) {
			DBG.e("BillLoader.addBill(): add-path failed!");
			return false;
		}
		b2 = mBills.add(b);
		if (!b2) {
			DBG.e("BillLoader.addBill(): add-bill failed!");
			return false;
		}
		return true;
	}

	private String getBillPath(Bill b) {
		return "data/B" + b.getCustomer().getAccountNumber().substring(1) +
				"_" + b.getYear() + b.getMonth() + ".dat";
	}
}
