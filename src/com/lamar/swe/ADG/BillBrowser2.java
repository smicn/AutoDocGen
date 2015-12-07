/*
 * CPSC-5360 Software Engineering, 2015 Spring /Project
 * 
 * Author: Shaomin (Samuel) Zhang
 * 
 * Email : smicn@foxmail.com
 * */
package com.lamar.swe.ADG;

/*
 * https://docs.oracle.com/javase/tutorial/uiswing/examples/misc/
 * TextAreaPrintingDemoProject/src/misc/TextAreaPrintingDemo.java
 * 
 * TextAreaPrintingDemo.java requires the following files:
 * toprint.txt
 * guide.txt
 * images/print.png
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import javax.swing.*;

public class BillBrowser2 extends JFrame {
	
	private CustomerLoader mCLoader;
	private BillLoader     mBLoader;
	private Customer       mCustomer;

	public BillBrowser2() {
		initComponents();
		load(textBill, "toprint.txt");
		load(textCustomer, "guide.txt");
	}

	public BillBrowser2(Customer c) {
		mCLoader = CustomerLoader.getInstance();
		mBLoader = BillLoader.getInstance();
		
		mBLoader.load();
		
		initComponents();
		
		loadCustomer2UI(c, "2015", "April");
		loadBill2UI("2015", "April");
	}

	private void loadCustomer2UI(Customer c, String year, String month) {
		if (null ==c) {
			c = mCLoader.getCustomer(0);
		}
		mCustomer = c;
		
		String text = mCustomer.getName() + "\n";
		text += "\nAccount number: " + mCustomer.getAccountNumber();
		text += "\nAccount phone:  " + mCustomer.mPhoneNumber;
		text += "\nEmail: " + mCustomer.mEmailAddress;
		text += "\n" + mCustomer.mAddress + "\n\n";
		
		text += "Service period\n";
		text += month + "," + year;

		textCustomer.setText(text);
	}

	private void loadBill2UI(String year, String month) {
		
		Bill[] bills = mBLoader.getBills(mCustomer);
		Bill bill = null;
		for (int ii = 0; ii < bills.length; ii++) {
			if (bills[ii].getYear().equals(year) &&
				bills[ii].getMonth().equalsIgnoreCase(month)) {
				bill = bills[ii];
				break;
			}
		}
		
		if (null == bill) {
			bill = new Bill(mCustomer, year, month);
			mBLoader.addBill(bill);
			mBLoader.save();
		}

		double total = bill.mPhoneChargeDollars 
					+ bill.mEnergyFeeDollars
					+ bill.mWaterCostDollars
					+ bill.mInternetFeeDollars
					+ bill.mTVFeeDollars;
		double tax = 0.085 * total;
		
		String text = "\n" + mCustomer.getName();
		text += "\nAccount number: " + mCustomer.getAccountNumber();
		text += "\nAccount phone:  " + mCustomer.mPhoneNumber;
		text += "\nEmail: " + mCustomer.mEmailAddress;
		text += "\n" + mCustomer.mAddress + "\n\n";

		text += "Service period\t\t\t Amount due\n";
		text += bill.getMonth() + "," + bill.getYear() + "\t\t\t $" + formatDollars(total + tax) + "\n";
		text += "---------------------------------------------------\n";
		text += "Payments for Phone Service:\n";
		text += "Service provider: " + bill.mPhoneServiceProvider + "\n";
		text += "Amount: " + bill.mPhoneChargeMinutes + " min\n";
		text += "Payment: $" + formatDollars(bill.mPhoneChargeDollars) + "\n\n";

		text += "---------------------------------------------------\n";
		text += "Payments for Electricity:\n";
		text += "Service provider: " + bill.mEnergyServiceProvider + "\n";
		text += "Amount: " + bill.mEnergyFeeKilowatts+ " KW\n";
		text += "Payment: $" + formatDollars(bill.mEnergyFeeDollars) + "\n\n";

		text += "---------------------------------------------------\n";
		text += "Payments for Water Service:\n";
		text += "Service provider: " + bill.mWaterServiceProvider + "\n";
		text += "Amount: " + bill.mWaterCostTon+ " ton\n";
		text += "Payment: $" + formatDollars(bill.mWaterCostDollars) + "\n\n";

		text += "---------------------------------------------------\n";
		text += "Payments for Internet Service:\n";
		text += "Service provider: " + bill.mInternetServiceProvider + "\n";
		text += "Amount: " + bill.mInternetDataGB + " GB\n";
		text += "Payment: $" + formatDollars(bill.mInternetFeeDollars) + "\n\n";

		text += "---------------------------------------------------\n";
		text += "Payments for TV Service:\n";
		text += "Service provider: " + bill.mTVServiceProvider + "\n";
		text += "Payment: $" + formatDollars(bill.mTVFeeDollars) + "\n\n";
		
		text += "\nTotal taxes and fees: $" + formatDollars(tax);
		text += "\nTotal due: $" + formatDollars(total + tax) + "\n\n";

		text += "--------------------------------------------------------";
		text += "--------------------------------------------------------\n";
		text += "This statement is generated automatically by Lamar AutoDocGen system. The \n";
		text += "information of customer and data of bills come from the service providers. Please\n";
		text += "contact your local service providers if you find any fees not correct.\n";
		text += "Thank you for using Lamar AutoDocGen online bill system.";
		
		textBill.setText(text);
	}

	private String formatDollars(double dollars) {
		return (new DecimalFormat("#0.00")).format(dollars);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed"
	// desc=" Generated Code ">//GEN-BEGIN:initComponents
	private void initComponents() {
		jScrollPane1 = new javax.swing.JScrollPane();
		textBill = new javax.swing.JTextArea();
		yearLabel = new javax.swing.JLabel();
		yearField = new javax.swing.JTextField();
		monthLabel = new javax.swing.JLabel();
		monthField = new javax.swing.JTextField();
		interactiveCheck = new javax.swing.JCheckBox();
		printButton = new javax.swing.JButton();
		textCustomer = new javax.swing.JTextArea();
		backgroundCheck = new javax.swing.JCheckBox();

		//setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("AutoDocGen: Bill Printing");
		textBill.setColumns(20);
		textBill.setRows(5);
		textBill.setMargin(new java.awt.Insets(2, 2, 2, 2));
		jScrollPane1.setViewportView(textBill);

		yearLabel.setText("Year");
		yearField.setText("2015");
		monthLabel.setText("Month");
		monthField.setText("April");
		
		yearField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				loadCustomer2UI(mCustomer, yearField.getText(), monthField.getText());
				loadBill2UI(yearField.getText(), monthField.getText());
			}
		});
		monthField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				loadCustomer2UI(mCustomer, yearField.getText(), monthField.getText());
				loadBill2UI(yearField.getText(), monthField.getText());
			}
		});

		interactiveCheck.setSelected(true);
		interactiveCheck.setText("Show Progress Dialog");
		interactiveCheck.setBorder(javax.swing.BorderFactory.createEmptyBorder(
				0, 0, 0, 0));

		printButton.setIcon(new javax.swing.ImageIcon(getClass()
				.getClassLoader().getResource(".").getPath()
				+ "../res/print.png"));
		printButton.setText("Print!");
		printButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				print(evt);
			}
		});

		textCustomer.setColumns(20);
		textCustomer.setEditable(false);
		textCustomer.setLineWrap(true);
		textCustomer.setRows(5);
		textCustomer.setWrapStyleWord(true);
		textCustomer.setOpaque(false);

		backgroundCheck.setSelected(true);
		backgroundCheck.setText("Print in Background");
		backgroundCheck.setBorder(javax.swing.BorderFactory.createEmptyBorder(
				0, 0, 0, 0));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING,
												false)
												.addComponent(textCustomer)
												.addGroup(
														layout.createSequentialGroup()
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.LEADING,
																				false)
																				.addComponent(
																						backgroundCheck,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						Short.MAX_VALUE)
																				.addComponent(
																						interactiveCheck,
																						javax.swing.GroupLayout.PREFERRED_SIZE,
																						159,
																						javax.swing.GroupLayout.PREFERRED_SIZE))
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		printButton,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		Short.MAX_VALUE))
												.addGroup(
														layout.createSequentialGroup()
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.TRAILING,
																				false)
																				.addComponent(
																						monthLabel,
																						javax.swing.GroupLayout.Alignment.LEADING,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						Short.MAX_VALUE)
																				.addComponent(
																						yearLabel,
																						javax.swing.GroupLayout.Alignment.LEADING,
																						javax.swing.GroupLayout.PREFERRED_SIZE,
																						74,
																						javax.swing.GroupLayout.PREFERRED_SIZE))
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.LEADING,
																				false)
																				.addComponent(
																						monthField)
																				.addComponent(
																						yearField,
																						javax.swing.GroupLayout.PREFERRED_SIZE,
																						209,
																						javax.swing.GroupLayout.PREFERRED_SIZE))))
								.addGap(15, 15, 15)
								.addComponent(jScrollPane1,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										343, Short.MAX_VALUE).addContainerGap()));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(
														jScrollPane1,
														javax.swing.GroupLayout.Alignment.TRAILING,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														403, Short.MAX_VALUE)
												.addGroup(
														javax.swing.GroupLayout.Alignment.TRAILING,
														layout.createSequentialGroup()
																.addComponent(
																		textCustomer,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		260,
																		Short.MAX_VALUE)
																.addGap(18, 18,
																		18)
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.BASELINE)
																				.addComponent(
																						yearLabel)
																				.addComponent(
																						yearField,
																						javax.swing.GroupLayout.PREFERRED_SIZE,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						javax.swing.GroupLayout.PREFERRED_SIZE))
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.BASELINE)
																				.addComponent(
																						monthLabel)
																				.addComponent(
																						monthField,
																						javax.swing.GroupLayout.PREFERRED_SIZE,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						javax.swing.GroupLayout.PREFERRED_SIZE))
																.addGap(27, 27,
																		27)
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.LEADING)
																				.addGroup(
																						layout.createSequentialGroup()
																								.addComponent(
																										interactiveCheck,
																										javax.swing.GroupLayout.PREFERRED_SIZE,
																										23,
																										javax.swing.GroupLayout.PREFERRED_SIZE)
																								.addPreferredGap(
																										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																								.addComponent(
																										backgroundCheck,
																										javax.swing.GroupLayout.PREFERRED_SIZE,
																										23,
																										javax.swing.GroupLayout.PREFERRED_SIZE))
																				.addComponent(
																						printButton,
																						javax.swing.GroupLayout.PREFERRED_SIZE,
																						52,
																						javax.swing.GroupLayout.PREFERRED_SIZE))))
								.addContainerGap()));
		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void load(JTextArea comp, String fileName) {
		try {
			comp.read(new InputStreamReader(new FileInputStream(new File("res/"
					+ fileName))), null);
		} catch (IOException ex) {
			// should never happen with the resources we provide
			ex.printStackTrace();
		}
	}

	private void print(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_print
		MessageFormat header = createFormat("Statement by Lamar AutoDocGen");
		MessageFormat footer = createFormat("Page {0}");
		boolean interactive = interactiveCheck.isSelected();
		boolean background = backgroundCheck.isSelected();

		PrintingTask task = new PrintingTask(header, footer, interactive);
		if (background) {
			task.execute();
		} else {
			task.run();
		}
	}// GEN-LAST:event_print

	private class PrintingTask extends SwingWorker<Object, Object> {
		private final MessageFormat headerFormat;
		private final MessageFormat footerFormat;
		private final boolean interactive;
		private volatile boolean complete = false;
		private volatile String message;

		public PrintingTask(MessageFormat header, MessageFormat footer,
				boolean interactive) {
			this.headerFormat = header;
			this.footerFormat = footer;
			this.interactive = interactive;
		}

		@Override
		protected Object doInBackground() {
			try {
				complete = textBill.print(headerFormat, footerFormat, true, null,
						null, interactive);
				message = "Printing " + (complete ? "complete" : "canceled");
			} catch (PrinterException ex) {
				message = "Sorry, a printer error occurred";
			} catch (SecurityException ex) {
				message = "Sorry, cannot access the printer due to security reasons";
			}
			return null;
		}

		@Override
		protected void done() {
			message(!complete, message);
		}
	}

	private MessageFormat createFormat(String text) {
		if (text != null && text.length() > 0) {
			try {
				return new MessageFormat(text);
			} catch (IllegalArgumentException e) {
				error("Sorry, this format is invalid.");
			}
		}
		return null;
	}

	private void message(boolean error, String msg) {
		int type = (error ? JOptionPane.ERROR_MESSAGE
				: JOptionPane.INFORMATION_MESSAGE);
		JOptionPane.showMessageDialog(this, msg, "Printing", type);
	}

	private void error(String msg) {
		message(true, msg);
	}

	public static void launch(Customer c) {
		JFrame frame = new BillBrowser2(c);
		frame.setLocationRelativeTo(null);
		frame.setSize(800, 600);
		frame.setVisible(true);
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JCheckBox backgroundCheck;
	private javax.swing.JTextField monthField;
	private javax.swing.JLabel monthLabel;
	private javax.swing.JTextArea textCustomer;
	private javax.swing.JTextField yearField;
	private javax.swing.JLabel yearLabel;
	private javax.swing.JCheckBox interactiveCheck;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JButton printButton;
	private javax.swing.JTextArea textBill;
	// End of variables declaration//GEN-END:variables
}