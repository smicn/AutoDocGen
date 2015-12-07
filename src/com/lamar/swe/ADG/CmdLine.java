/*
 * CPSC-5360 Software Engineering, 2015 Spring /Project
 * 
 * Author: Shaomin (Samuel) Zhang
 * 
 * Email : smicn@foxmail.com
 * */
package com.lamar.swe.ADG;

public class CmdLine implements TestDriver.TestListener {

	private TestDriver mTester;
	
	@Override
	public void callback(int index, int state, int result) {
		if (TestDriver.TestListener.TESTS_START == state) {
			DBG.w("Testcase_" + index + " start:");
		}
		else if (TestDriver.TestListener.TESTS_STOP == state) {
			if (TestDriver.TESTR_SUCCESS == result) {
				DBG.w("Testcase_" + index + " finished: PASSED. (" +
						mTester.getTestCase(index).getName() + ")");
			}
			else if (TestDriver.TESTR_FAILED == result) {
				DBG.w("Testcase_" + index + " finished: FAILED! (" +
						mTester.getTestCase(index).getName() + ")");
				TestCase tc = mTester.getTestCase(index);
				DBG.e("  At STEP_" + tc.getLastFailedRoutineStep() + ", " +
						tc.getLastFailedRountineName() + " (" +
						tc.getLastFailedRountineDescription() + ").");
			}
		}
	}
	
	public CmdLine() {
		DBG.i("CmdLine() init:");
		
		mTester = new TestDriver(TestDataSet.getInstance().getTestCases());
		mTester.registerListener(this);
		
		DBG.i("CmdLine() inited!");
	}
	
	public void execute() {
		int total = mTester.getTestCaseCount();
		int passed = 0;
		int failed = 0;
		
		DBG.i("CmdLine.exec(): call startTest():");
		
		mTester.startTest();
		
		DBG.i("CmdLine.exec(): startTest() called.");
		
		while ((passed + failed) < total) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			passed = mTester.getSuccessCount();
			failed = mTester.getFailureCount();
		}
		
		DBG.i("CmdLine.exec(): finished! now see the report:");
		DBG.i("total=" + total + ": passed=" + passed + ", failed=" + failed);
	}

	public final static void main(String[] args) {
		DBG.i("Welcome to CmdLine mode:");
		
		CmdLine cl = new CmdLine();
		cl.execute();
		
		DBG.i("Bye CmdLine!");
	}
}
