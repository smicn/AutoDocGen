/*
 * CPSC-5360 Software Engineering, 2015 Spring /Project
 * 
 * Author: Shaomin (Samuel) Zhang
 * 
 * Email : smicn@foxmail.com
 * */
package com.lamar.swe.ADG;

public abstract class TestCase {
	
	private String mName;
	private String mDescription;
	private int mRoutineCount;
	private int mLastFailedRoutineStep;
	
	public abstract boolean routine(int routineStep);
	public abstract String getRoutineName(int routineStep);
	public abstract String getRoutineDesc(int routineStep);
	
	public TestCase(String name, int routineCount) {
		mName = name;
		mRoutineCount = routineCount;
		mLastFailedRoutineStep = -1;
	}
	
	public void setDescription(String desc) {
		mDescription = desc;
	}
	
	public String getName() {
		return mName;
	}
	
	public String getDescription() {
		return mDescription;
	}
	
	public boolean run() {
		mLastFailedRoutineStep = -1;
		for (int ii = 0; ii < mRoutineCount; ii++) {
			if (!routine(ii)) {
				mLastFailedRoutineStep = ii;
				return false;
			}
		}
		return true;
	}
	
	public int getRoutineCount() {
		return mRoutineCount;
	}
	
	public int getLastFailedRoutineStep() {
		return mLastFailedRoutineStep;
	}
	
	public String getLastFailedRountineName() {
		if (-1 == mLastFailedRoutineStep) {
			return null;
		}
		else {
			return getRoutineName(mLastFailedRoutineStep);
		}
	}
	
	public String getLastFailedRountineDescription() {
		if (-1 == mLastFailedRoutineStep) {
			return null;
		}
		else {
			return getRoutineDesc(mLastFailedRoutineStep);
		}
	}
}
