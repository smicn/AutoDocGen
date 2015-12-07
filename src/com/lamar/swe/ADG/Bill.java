/*
 * CPSC-5360 Software Engineering, 2015 Spring /Project
 * 
 * Author: Shaomin (Samuel) Zhang
 * 
 * Email : smicn@foxmail.com
 * */
package com.lamar.swe.ADG;

import java.io.Serializable;
import java.util.Random;

public class Bill implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1721399849094744319L;
	
	private final static String PSP[] = {
		"T-Mobile", "AT&T", "Verizon", "H2O-Wireless"
	};
	private final static String ESP[] = {
		"Entegy",
	};
	private final static String WSP[] = {
		"Texas-Water",
	};
	private final static String ISP[] = {
		"AT&T", "TimeWarner",	
	};
	private final static String TVSP[] = {
		"TimeWarnerCable", "Dish", "DirectTV"
	};
	private final static double phoneChargePrice = 0.1;
	private final static double energyFeePrice   = 0.68;
	private final static double waterPrice       = 3.65;
	private final static double internetPrice    = 12.0;
	private final static double tvFeePrice       = 18.0;
	
	private final static int phoneMinUpperBound   = 2000;
	private final static int energyKWUpperBound   = 200;
	private final static int waterTonUpperBound   = 20;
	private final static int internetGBUpperBound = 10;
	private final static int tvFeeUpperBound      = 3;
		
	private String mYear;
	private String mMonth;
	
	public String mPhoneServiceProvider;
	public double mPhoneChargeDollars;
	public int    mPhoneChargeMinutes;
	
	public String mEnergyServiceProvider;
	public double mEnergyFeeDollars;
	public int    mEnergyFeeKilowatts;
	
	public String mWaterServiceProvider;
	public double mWaterCostDollars;
	public int    mWaterCostTon;
	
	public String mInternetServiceProvider;
	public double mInternetFeeDollars;
	public int    mInternetDataGB;
	
	public String mTVServiceProvider;
	public double mTVFeeDollars;
	
	private Customer mCustomer;
	
	public Bill(Customer c) {
		mCustomer = c;
		autoGen();
	}
	
	public Bill(Customer c, String year, String month) {
		mCustomer = c;
		autoGen();
		
		if (!checkValidYear(year)) {
			DBG.e("Bill(): invalid year=" + year);
		}
		if (!checkValidMonth(month)) {
			DBG.e("Bill(): invalid month=" + month);
		}
		mYear  = year;
		mMonth = month;
	}
	
	public Customer getCustomer() {
		return mCustomer;
	}
	
	public String getYear() {
		return mYear;
	}
	
	public String getMonth() {
		return mMonth;
	}
	
	public void selectPhoneService(String psp) {
		mPhoneServiceProvider = psp;
	}
	
	public void consumePhoneService(int min) {
		mPhoneChargeMinutes = Math.max(min, phoneMinUpperBound);
		mPhoneChargeDollars = phoneChargePrice * mPhoneChargeMinutes;
	}
	
	public void selectEnergyService(String esp) {
		mEnergyServiceProvider = esp;
	}
	
	public void consumeEnergyService(int kw) {
		mEnergyFeeKilowatts = Math.max(kw, energyKWUpperBound);
		mEnergyFeeDollars = energyFeePrice * mEnergyFeeKilowatts;
	}
	
	public void selectWaterService(String wsp) {
		mWaterServiceProvider = wsp;
	}
	
	public void consumeWaterService(int ton) {
		mWaterCostTon = Math.max(ton, waterTonUpperBound);
		mWaterCostDollars = waterPrice * mWaterCostTon;
	}
	
	public void selectInternetService(String isp) {
		mInternetServiceProvider = isp;
	}
	
	public void consumeInternetService(int GB) {
		mInternetDataGB = Math.max(GB, internetGBUpperBound);
		mInternetFeeDollars = internetPrice * mInternetDataGB;
	}
	
	public void selectTVService(String tvsp) {
		mTVServiceProvider = tvsp;
	}
	
	public void consumeTVService(int tv) {
		tv = Math.max(tv, tvFeeUpperBound);
		mTVFeeDollars = tvFeePrice * tv;
	}
	
	private void autoGen() {
		Random r = new Random(System.currentTimeMillis());
		
		mYear = Integer.toString( 2013 + r.nextInt(3));
		final String months[] = {
				"Jan", "Feb", "Mar", "Apr", "May", "Jun",
				"Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
		};
		mMonth = months[ r.nextInt(months.length) ];
				
		mPhoneServiceProvider = PSP[ r.nextInt(PSP.length) ];
		mPhoneChargeMinutes = r.nextInt(phoneMinUpperBound);
		mPhoneChargeDollars = phoneChargePrice * mPhoneChargeMinutes;
		
		mEnergyServiceProvider = ESP[ r.nextInt(ESP.length) ];
		mEnergyFeeKilowatts = r.nextInt(energyKWUpperBound);
		mEnergyFeeDollars = energyFeePrice * mEnergyFeeKilowatts;
		
		mWaterServiceProvider = WSP[ r.nextInt(WSP.length) ];
		mWaterCostTon = r.nextInt(waterTonUpperBound);
		mWaterCostDollars = waterPrice * mWaterCostTon;
		
		mInternetServiceProvider = ISP[ r.nextInt(ISP.length) ];
		mInternetDataGB = r.nextInt(internetGBUpperBound);
		mInternetFeeDollars = internetPrice * mInternetDataGB;

		mTVServiceProvider = TVSP[ r.nextInt(TVSP.length) ];
		mTVFeeDollars = tvFeePrice * r.nextInt(tvFeeUpperBound);
	}
	
	private boolean checkValidMonth(String month) {
		final String valids[] = {
			"Jan", "Feb", "Mar", "Apr", "May", "Jun",
			"Jul", "Aug", "Sep", "Oct", "Nov", "Dec",
			
			"January", "Febrary", "March", "April", "May", "June",
			"July", "August", "September", "October", "November", "December",
			
			"01", "02", "03", "04", "05", "06",
			"07", "08", "09", "10", "11", "12",
		};
		for (String v : valids) {
			if (v.equalsIgnoreCase(month)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean checkValidYear(String year) {
		int intY = Integer.parseInt(year);
		if (1970 <= intY && intY <= 2099) {
			return true;
		}
		return false;
	}
}
