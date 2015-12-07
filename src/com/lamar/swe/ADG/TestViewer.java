/*
 * CPSC-5360 Software Engineering, 2015 Spring /Project
 * 
 * Author: Shaomin (Samuel) Zhang
 * 
 * Email : smicn@foxmail.com
 * */
package com.lamar.swe.ADG;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.text.MessageFormat;
 

public class TestViewer extends JFrame implements TestDriver.TestListener {
 
	private static TestDriver mTester;
	private boolean mTesting;
	
	public static TestDriver getTestDriver() {
		return mTester;
	}
	
	@Override
	public void callback(int index, int state, int result) {
		
		resultLabel.setText("Test results: (" + mTester.getSuccessCount() +
				"/" + mTester.getTestCaseCount() + ")");
		
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
			
			testsTable.updateUI();
		}
		else if (TestDriver.TestListener.TESTS_ALL_BEGIN == state) {
			DBG.w("Testcases begin to run:");
			mTesting = true;
			startButton.setEnabled(false);
		}
		if (TestDriver.TestListener.TESTS_ALL_FINISHED == state) {
			DBG.w("Testcases all finished!");
			mTesting = false;
			startButton.setEnabled(true);
			
			if (mTester.getSuccessCount() == mTester.getTestCaseCount()) {
				resultLabel.setText("Success: all testcases passed!");
			}
		}
	}
	
    /* Check mark and x mark items to render the "Passed" column */
    private static final ImageIcon passedIcon
        = new ImageIcon(CustomerTableViewer.class
				.getClassLoader().getResource(".").getPath() + "../res/passed.png");
    private static final ImageIcon failedIcon
        = new ImageIcon(CustomerTableViewer.class
				.getClassLoader().getResource(".").getPath() + "../res/failed.png");
 
    /* UI Components */
    private JPanel contentPane;
    private JLabel testsLabel;
    private JTable testsTable;
    private JScrollPane scroll;
	private JLabel resultLabel;
    private JButton startButton;
 
    /**
     * Constructs an instance of the demo.
     */
    public TestViewer() {
        super("AutoDocGen: Test Suite");
        
        mTester = new TestDriver(TestDataSet.getInstance().getTestCases());
		mTester.registerListener(this);
 
        testsLabel = new JLabel("Welcome to Lamar AutoDocGen Test Suite");
        testsLabel.setFont(new Font("Dialog", Font.BOLD, 16));
 
        testsTable = createTable(new TestsModel());
        testsTable.setFillsViewportHeight(true);
        testsTable.setRowHeight(58);
        testsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        testsTable.getColumnModel().getColumn(0).setMaxWidth(25);
        testsTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        testsTable.getColumnModel().getColumn(4).setMaxWidth(75);
        testsTable.getColumnModel().getColumn(0).setPreferredWidth(25);
        testsTable.getColumnModel().getColumn(1).setPreferredWidth(125);
        testsTable.getColumnModel().getColumn(2).setPreferredWidth(300);
        testsTable.getColumnModel().getColumn(3).setPreferredWidth(200);
        testsTable.getColumnModel().getColumn(4).setPreferredWidth(50);
        
        testsTable.getColumnModel().getColumn(0).setCellRenderer(new NoColumnRenderer());
        testsTable.getColumnModel().getColumn(2).setCellRenderer(new MultiLineTextColumnRenderer());
        testsTable.getColumnModel().getColumn(3).setCellRenderer(new MultiLineTextColumnRenderer());
        /* Set a custom renderer on the "Passed" column */
        testsTable.getColumn("Passed").setCellRenderer(createPassedColumnRenderer());
 
        scroll = new JScrollPane(testsTable);

		resultLabel = new JLabel("Test results: (0/0)");
        //resultLabel.setFont(new Font("Dialog", Font.BOLD, 12));
 
        startButton = new JButton("Start Test");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
            	DBG.i("CmdLine.exec(): call startTest():");
        		
        		mTester.startTest();
        		
        		DBG.i("CmdLine.exec(): startTest() called.");
            }
        });

 		JPanel bottomPanel = new JPanel();
		bottomPanel.add(startButton);
		bottomPanel.add(resultLabel);
		
        contentPane = new JPanel();
		GroupLayout layout = new GroupLayout(contentPane);
        contentPane.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(scroll, GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE)
                    .addComponent(testsLabel)
                    .addComponent(bottomPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(testsLabel)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scroll, GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bottomPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        setContentPane(contentPane);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
    }
    
    /**
     * Create and return a table for the given model.
     * <p>
     * This is protected so that a subclass can return an instance
     * of a different {@code JTable} subclass. This is interesting
     * only for {@code TablePrintDemo3} where we want to return a
     * subclass that overrides {@code getPrintable} to return a
     * custom {@code Printable} implementation.
     */
    protected JTable createTable(TableModel model) {
        return new JTable(model);
    }
 
    /**
     * Create and return a cell renderer for rendering the pass/fail column.
     * This is protected so that a subclass can further customize it.
     */
    protected TableCellRenderer createPassedColumnRenderer() {
        return new PassedColumnRenderer();
    }
    
    /**
     * Start the application.
     */
    public static void launch() {
    	new TestViewer().setVisible(true);
    }
 
    /**
     * A table model containing student tests.
     */
    private static class TestsModel implements TableModel {
        private TestDriver mTester;
 
        public TestsModel() {
        	mTester = TestViewer.getTestDriver();
        }
        
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {}
        public void addTableModelListener(TableModelListener l) {}
        public void removeTableModelListener(TableModelListener l) {}
 
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
         
        public int getRowCount() {
            return mTester.getTestCaseCount();
        }
         
        public int getColumnCount() {
            return 5;
        }
         
        public String getColumnName(int col) {
            switch(col) {
                case 0: return "No.";
                case 1: return "Testcase Name";
                case 2: return "Testcase Description";
                case 3: return "Results";
                case 4: return "Passed";
            }
             
            throw new AssertionError("invalid column");
        }
        
        public Class<?> getColumnClass(int col) {
            switch(col) {
                case 0:
                	return Integer.class;
                case 1:
                case 2:
                case 3:
                	return String.class;
                case 4:
                    return Boolean.class;
            }
 
            throw new AssertionError("invalid column");
        }
        
        public Object getValueAt(int row, int col) {
        	TestCase tc = mTester.getTestCase(row);
        	
            switch(col) {
                case 0:
                	return Integer.valueOf(row+1);
                case 1:
                	return tc.getName();
                case 2:
                	return tc.getDescription();
                case 3:
                	if (-1 == tc.getLastFailedRoutineStep()) {
                		return "normal";
                	}
                	else {
                		return "Failed in step(" + (tc.getLastFailedRoutineStep() + 1) + 
                			"/" + tc.getRoutineCount() + ") " + tc.getLastFailedRountineName() +
                			": " + tc.getLastFailedRountineDescription();
                	}
                case 4:
                	if (mTester.isPassedTestCase(row)) {
                		return true;
                	}
                	else {
                		return false;
                	}
            }
 
            throw new AssertionError("invalid column");
        }
    }
 
    /**
     * A custom cell renderer for rendering the "Passed" column.
     */
    protected static class PassedColumnRenderer extends DefaultTableCellRenderer {
            public Component getTableCellRendererComponent(JTable table,
                                                           Object value,
                                                           boolean isSelected,
                                                           boolean hasFocus,
                                                           int row,
                                                           int column) {
 
            super.getTableCellRendererComponent(table, value, isSelected,
                                                hasFocus, row, column);
 
            setText("");
            setHorizontalAlignment(SwingConstants.CENTER);
 
            /* set the icon based on the passed status */
            boolean status = (Boolean)value;
            setIcon(status ? passedIcon : failedIcon);
 
            return this;
        }
    }
    
    protected static class MultiLineTextColumnRenderer extends JTextArea implements TableCellRenderer {
        public Component getTableCellRendererComponent(JTable table,
                                                       Object value,
                                                       boolean isSelected,
                                                       boolean hasFocus,
                                                       int row,
                                                       int column) {
        	this.setText((String)value);
            this.setWrapStyleWord(true);            
            this.setLineWrap(true);

            return this;
        }
    }
    
    protected static class NoColumnRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table,
                                                       Object value,
                                                       boolean isSelected,
                                                       boolean hasFocus,
                                                       int row,
                                                       int column) {
        super.getTableCellRendererComponent(table, value, isSelected,
                                            hasFocus, row, column);
        setVerticalAlignment(SwingConstants.CENTER);
        setHorizontalAlignment(SwingConstants.CENTER);
        return this;
    }
}
}
