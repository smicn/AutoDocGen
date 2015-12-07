/*
 * CPSC-5360 Software Engineering, 2015 Spring /Project
 * 
 * Author: Shaomin (Samuel) Zhang
 * 
 * Email : smicn@foxmail.com
 * */
package com.lamar.swe.ADG;

public final class TestDataSet {

	private final static int STEP_0 = 0;
	private final static int STEP_1 = 1;
	private final static int STEP_2 = 2;
	private final static int STEP_3 = 3;
	private final static int STEP_4 = 4;
	private final static int STEP_5 = 5;
	private final static int STEP_6 = 6;
	private final static int STEP_7 = 7;
	private final static int STEP_8 = 8;
	private final static int STEP_9 = 9;
	
	private static TestDataSet mInst;
	
	private TestDataSet() {
		if (mTestcaseDescriptions.length != mTestcases.length) {
			System.out.println("Error, the lengths of two arrays are not same!");
			return;
		}
		
		for (int ii = 0; ii < mTestcases.length; ii++) {
			TestCase tc = mTestcases[ii];
			tc.setDescription(mTestcaseDescriptions[ii]);
		}
	}
	
	public static TestDataSet getInstance() {
		if (null == mInst) {
			mInst = new TestDataSet();
		}
		return mInst;
	}
	
	public TestCase[] getTestCases() {
		return mTestcases;
	}
	
	private final String mTestcaseDescriptions[] = {
		"TESTCASE_01. This is only test the basic structure, the initilizations of skeleton models.",
		
		"TESTCASE_02. This is to test the main feature of CustomerLoader class, which is to load " +
		"the customers information from database, and to save modified data written back to the "  +
		"database by some chances.",
		
		"TESTCASE_03. This is to test adding/deleting customer.",
		
		"TESTCASE_04. This is to test reading bill of given customer and check it out, but this " +
		"operation will not change the information of bill itself.",
		
		"TESTCASE_05. This is to test the feature of adding bill to the given customer. Please " +
		"also note that we donnot have a feature as deleting a bill.",
		
		"TESTCASE_06. This is to test checking out multiple bills, unlike TESTCASE_04, this test " +
		"case focuses on the performance, but not only the data consistency.",
	};
	
	private final TestCase mTestcases[] = {
			
		new TestCase("Init", 2) {
			
			@Override
			public boolean routine(int routineStep) {
				switch (routineStep) {
				case STEP_0:
					CustomerLoader cldr = CustomerLoader.getInstance();
					return true;
				case STEP_1:
					BillLoader bldr = BillLoader.getInstance();
					return true;
				default:
					return false;
				}
			}

			@Override
			public String getRoutineName(int routineStep) {
				switch (routineStep) {
				case STEP_0:
					return "CustomerLoader";
				case STEP_1:
					return "BillLoader";
				default:
					return null;
				}
			}

			@Override
			public String getRoutineDesc(int routineStep) {
				switch (routineStep) {
				case STEP_0:
					return "CustomerLoader singleton init.";
				case STEP_1:
					return "BillLoader singleton init.";
				default:
					return null;
				}
			}
			
		},
		
		new TestCase("Load_Save", 3) {

			@Override
			public boolean routine(int routineStep) {
				CustomerLoader cldr = CustomerLoader.getInstance();
				BillLoader bldr = BillLoader.getInstance();
				boolean ret = false;
				
				switch (routineStep) {
				case STEP_0:
					cldr.load();
					if (cldr.getCustomerCount() <= 0) {
						Customer c = new Customer();
						ret = cldr.addCustomer(c);
						if (!ret) {
							DBG.e("cldr.addCustomer(c) failed!");
						}
						cldr.save();
					}
					return true;
				case STEP_1:
					cldr.load();
					bldr.load();
					if (cldr.getCustomerCount() <= 0) {
						DBG.e("  CustomerLoader.add() failed, 'coz loaded and cldr.count()="
					          + cldr.getCustomerCount());
						return false;
					}
					else {
						Customer c = cldr.getCustomer(0);
						if (null == c) {
							DBG.e("cldr.getCustomer(0) failed!");
							return false;
						}
						Bill[] bills = bldr.getBills(c);
						if (bills.length <= 0) {
							Bill b = new Bill(c);
							ret = bldr.addBill(b);
							if (!ret) {
								DBG.e("bldr.addBill(b) failed!");
							}
							bldr.save();
						}
					}
					return true;
				case STEP_2:
					bldr.load();
					Customer c = cldr.getCustomer(0);
					Bill[] bills = bldr.getBills(c);
					if (bills.length <= 0) {
						DBG.e("  BillLoader.add() failed, 'coz loaded and bills.length="
						          + bills.length);
						return false;
					}
					return true;
				default:
					return false;
				}
			}

			@Override
			public String getRoutineName(int routineStep) {
				switch (routineStep) {
				case STEP_0:
					return "CustomerLoader:load";
				case STEP_1:
					return "BillLoader:load, save";
				case STEP_2:
					return "BillLoader:check";
				default:
					return null;
				}
			}

			@Override
			public String getRoutineDesc(int routineStep) {
				switch (routineStep) {
				case STEP_0:
					return "CustomerLoader loads data, if empty, add one customer and save it.";
				case STEP_1:
					return "BillLoader loads data, if no bill exists, create one and save it.";
				case STEP_2:
					return "BillLoader loads data again, at least one bill must be there.";
				default:
					return null;
				}
			}
		},
		
		new TestCase("Add_Delete_Customer", 3) {
			
			private int customerCount = 0;
			
			@Override
			public boolean routine(int routineStep) {
				CustomerLoader cldr = CustomerLoader.getInstance();
				BillLoader bldr = BillLoader.getInstance();
				Customer c;
				boolean ret;
				
				switch (routineStep) {	
				case STEP_0:
					cldr.load();
					bldr.load();
					customerCount = cldr.getCustomerCount();
					c = new Customer();
					ret = cldr.addCustomer(c);
					if (!ret) {
						DBG.e("cldr.addCustomer(c) failed!");
					}
					cldr.save();
					return true;
				case STEP_1:
					cldr.load();
					if (cldr.getCustomerCount() != (customerCount + 1)) {
						return false;
					}
					else {
						c = cldr.getCustomer(0);
						bldr.deleteBills(c);
						bldr.save();
						cldr.deleteCustomer(c);
						cldr.save();
					}
					return true;
				case STEP_2:
					cldr.load();
					if (cldr.getCustomerCount() != customerCount) {
						return false;
					}
					return true;
				}
				return false;
			}

			@Override
			public String getRoutineName(int routineStep) {
				switch (routineStep) {
				case STEP_0:
					return "CustomerLoader:add";
				case STEP_1:
					return "CustomerLoader:delete";
				case STEP_2:
					return "CustomerLoader:check";
				default:
					return null;
				}
			}

			@Override
			public String getRoutineDesc(int routineStep) {
				switch (routineStep) {
				case STEP_0:
					return "CustomerLoader loads data, add two customers and save them.";
				case STEP_1:
					return "CustomerLoader loads data, check the recent two customers and delete one.";
				case STEP_2:
					return "CustomerLoader loads data, check it again.";
				default:
					return null;
				}
			}
			
		},
		
		new TestCase("Check_Bill", 2) {

			@Override
			public boolean routine(int routineStep) {
				CustomerLoader cldr = CustomerLoader.getInstance();
				BillLoader bldr = BillLoader.getInstance();
				Customer c;
				Bill b;
				boolean ret;
				
				switch (routineStep) {
				case STEP_0:
					cldr.load();
					bldr.load();
					c = new Customer();
					ret = cldr.addCustomer(c);
					if (!ret) {
						DBG.e("cldr.addCustomer(c) failed!");
					}
					b = new Bill(c);
					ret = bldr.addBill(b);
					if (!ret) {
						DBG.e("bldr.addBill(b) failed!");
					}
					cldr.save();
					bldr.save();
					return true;
				case STEP_1:
					cldr.load();
					bldr.load();
					c = cldr.getCustomer(cldr.getCustomerCount()-1);
					//c = cldr.getCustomer(0);
					if (null == c) {
						DBG.e("cldr.getCustomer(cnt-1) failed!");
						//DBG.e("cldr.getCustomer(0) failed!");
						return false;
					}
					Bill[] bills = bldr.getBills(c);
					if (bills.length <= 0) {
						DBG.e("bldr.getBills(c) ret bills.length<=0, which is unexpected!");
						return false;
					}
					b = bills[0];
					DBG.i("  Bill.customer=" + b.getCustomer().getAccountNumber() +
						  ", " + b.getYear() + b.getMonth());
					DBG.i("    .phone: provider=" + b.mPhoneServiceProvider + ", cost=" + 
							  b.mPhoneChargeMinutes + "min, " + b.mPhoneChargeDollars + "USD.");
					DBG.i("    .enegy: provider=" + b.mEnergyServiceProvider + ", cost=" + 
							  b.mEnergyFeeKilowatts + "watts, " + b.mEnergyFeeDollars + "USD.");
					DBG.i("    .water: provider=" + b.mWaterServiceProvider + ", cost=" + 
							  b.mWaterCostTon + "tons, " + b.mWaterCostDollars + "USD.");
					DBG.i("    .internet: provider=" + b.mInternetServiceProvider + ", cost=" + 
							  b.mInternetDataGB + "GB, " + b.mInternetFeeDollars + "USD.");
					DBG.i("    .tv: provider=" + b.mTVServiceProvider + ", cost=" + 
							  b.mTVFeeDollars + "USD.");
					return true;
				default:
					return false;
				}
			}

			@Override
			public String getRoutineName(int routineStep) {
				switch (routineStep) {
				case STEP_0:
					return "Bill:load";
				case STEP_1:
					return "Bill:check";
				default:
					return null;
				}
			}

			@Override
			public String getRoutineDesc(int routineStep) {
				switch (routineStep) {
				case STEP_0:
					return "CustomerLoader&BillLoader load data, add at least one if empty.";
				case STEP_1:
					return "BillLoader loads the first Bill and check it.";
				default:
					return null;
				}
			}
			
		},
		
		new TestCase("Add_Bill", 3) {

			private int billCount = 0;
			
			@Override
			public boolean routine(int routineStep) {
				CustomerLoader cldr = CustomerLoader.getInstance();
				BillLoader bldr = BillLoader.getInstance();
				Customer c;
				Bill[] bills;
				Bill b;
				boolean ret;
				
				switch (routineStep) {
				case STEP_0:
					cldr.load();
					if (cldr.getCustomerCount() <= 0) {
						c = new Customer();
						ret = cldr.addCustomer(c);
						if (!ret) {
							DBG.e("cldr.addCustomer(c) failed!");
						}
						cldr.save();
					}
					return true;
				case STEP_1:
					bldr.load();
					c = cldr.getCustomer(0);
					bills = bldr.getBills(c);
					billCount = bills.length;
					b = new Bill(c);
					ret = bldr.addBill(b);
					if (!ret) {
						DBG.e("bldr.addBill(b) failed!");
					}
					b = new Bill(c);
					ret = bldr.addBill(b);
					if (!ret) {
						DBG.e("bldr.addBill(b) failed!");
					}
					b = new Bill(c);
					ret = bldr.addBill(b);
					if (!ret) {
						DBG.e("bldr.addBill(b) failed!");
					}
					bldr.save();
					return true;
				case STEP_2:
					bldr.load();
					c = cldr.getCustomer(0);
					bills = bldr.getBills(c);
					if (bills.length != (billCount + 3)) {
						return false;
					}
					return true;
				default:
					return false;
				}
			}

			@Override
			public String getRoutineName(int routineStep) {
				switch (routineStep) {
				case STEP_0:
					return "Customer:preparation";
				case STEP_1:
					return "Bill:add";
				case STEP_2:
					return "Bill:check";
				default:
					return null;
				}
			}

			@Override
			public String getRoutineDesc(int routineStep) {
				switch (routineStep) {
				case STEP_0:
					return "CustomerLoader&BillLoader load data, add at least one if empty.";
				case STEP_1:
					return "BillLoader adds three new bills to the first customer.";
				case STEP_2:
					return "BillLoader loads data and check the count of bills again.";
				default:
					return null;
				}
			}
			
		},
		
		new TestCase("Check_Multiple_Bills", 2) {

			@Override
			public boolean routine(int routineStep) {
				CustomerLoader cldr = CustomerLoader.getInstance();
				BillLoader bldr = BillLoader.getInstance();
				Customer c;
				Bill[] bills;
				Bill b;
				
				switch (routineStep) {
				case STEP_0:
					cldr.load();
					bldr.load();
					if (cldr.getCustomerCount() <= 0) {
						return false;
					}
					c = cldr.getCustomer(0);
					bills = bldr.getBills(c);
					if ( bills.length <= 0) {
						return false;
					}
					return true;
				case STEP_1:
					for (int ii = 0; ii < cldr.getCustomerCount(); ii++) {
						c = cldr.getCustomer(ii);
						bills = bldr.getBills(c);
						for (int jj = 0; jj < bills.length; jj++) {
							b = bills[jj];
							DBG.i("  Bill.customer=" + b.getCustomer().getAccountNumber() +
									  ", " + b.getYear() + b.getMonth());
							if (null == b.mPhoneServiceProvider  || //0 == b.mPhoneChargeMinutes ||
								null == b.mEnergyServiceProvider || //0 == b.mEnergyFeeKilowatts||
								null == b.mWaterServiceProvider  || //0 == b.mInternetDataGB ||
								null == b.mTVServiceProvider) {
								return false;
							}
						}
					}
					return true;
				default:
					return false;
				}
			}

			@Override
			public String getRoutineName(int routineStep) {
				switch (routineStep) {
				case STEP_0:
					return "Customer_Bill_Loaders:load";
				case STEP_1:
					return "Bills:check";
				default:
					return null;
				}
			}

			@Override
			public String getRoutineDesc(int routineStep) {
				switch (routineStep) {
				case STEP_0:
					return "CustomerLoader_BillLoader load data, fail test if empty.";
				case STEP_1:
					return "BillLoader load all the bills and check them valid.";
				default:
					return null;
				}
			}
			
		},
	};
}
