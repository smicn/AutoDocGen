/*
 * CPSC-5360 Software Engineering, 2015 Spring /Project
 * 
 * Author: Shaomin (Samuel) Zhang
 * 
 * Email : smicn@foxmail.com
 * */
package com.lamar.swe.ADG;

import java.util.ArrayList;
import java.util.List;

/**
 * This file, along with TestCase.java, provides a simple reusable auto-test 
 * suite module that can be used to build automatic integration tests for 
 * any other well-designed, testable Java projects.
 **/
public final class TestDriver {
	
	public final static int TESTR_NOTYET  = 0;
	public final static int TESTR_SUCCESS = 1;
	public final static int TESTR_FAILED  = 2;
	public final static int TESTR_TIMEOUT = 3;
	
	public interface TestListener {
		public final static int TESTS_START = 1;
		public final static int TESTS_STOP  = 2;
		public final static int TESTS_ALL_BEGIN     = 3;
		public final static int TESTS_ALL_FINISHED  = 4;
		
		public void callback(int index, int state, int result);
	}
	
	private List<TestCase> mTestcases;
	private int[]          mResults;
	private Thread         mThread;
	private boolean        mThreadRunning;
	private TestListener   mListener;
	
	public TestDriver(List<TestCase> testcases) {
		mTestcases = testcases;
		mResults = new int[mTestcases.size()];
	}
	
	public TestDriver(TestCase[] testcases) {
		mTestcases = new ArrayList<TestCase>();
		for (TestCase tc : testcases) {
			mTestcases.add(tc);
		}
		mResults = new int[mTestcases.size()];
	}
	
	public void registerListener(TestListener listener) {
		mListener = listener;
	}
	
	public void startTest() {
		for (int ii = 0; ii < mResults.length; ii++) {
			mResults[ii] = TESTR_NOTYET;
		}
		
		mThreadRunning = true;
		mThread = new Thread() {
			@Override
			public void run() {
				
				if (mListener != null) {
					mListener.callback(-1, TestListener.TESTS_ALL_BEGIN, mTestcases.size());
				}
				
				for (int ii = 0; ii < mTestcases.size(); ii++) {
					if (!mThreadRunning) {
						break;
					}
					
					TestCase tc = mTestcases.get(ii);
					
					if (mListener != null) {
						mListener.callback(ii, TestListener.TESTS_START, TESTR_NOTYET);
					}
					
					boolean ret = tc.run();
					if (ret) {
						mResults[ii] = TESTR_SUCCESS;
					}
					else {
						mResults[ii] = TESTR_FAILED;
					}
					
					if (mListener != null) {
						mListener.callback(ii, TestListener.TESTS_STOP, mResults[ii]);
					}
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				if (mListener != null) {
					mListener.callback(-1, TestListener.TESTS_ALL_FINISHED, getSuccessCount());
				}
			}
		};
		mThread.start();
	}
	
	public void stopTest() {
		mThreadRunning = false;
		try {
			mThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public int getTestCaseCount() {
		return mTestcases.size();
	}
	
	public TestCase getTestCase(int index) {
		return mTestcases.get(index);
	}
	
	public boolean isPassedTestCase(int index) {
		return (TESTR_SUCCESS == mResults[index]);
	}
	
	public int getSuccessCount() {
		int count = 0;
		for (int ii = 0; ii < mTestcases.size(); ii++) {
			if (TESTR_SUCCESS == mResults[ii]) {
				count++;
			}
		}
		return count;
	}
	
	public int getFailureCount() {
		int count = 0;
		for (int ii = 0; ii < mTestcases.size(); ii++) {
			if (TESTR_FAILED == mResults[ii]) {
				count++;
			}
		}
		return count;
	}
}
