/*
 * CPSC-5360 Software Engineering, 2015 Spring /Project
 * 
 * Author: Shaomin (Samuel) Zhang
 * 
 * Email : smicn@foxmail.com
 * */
package com.lamar.swe.ADG;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class CustomerViewer extends JFrame implements ListSelectionListener {

	CustomerLoader cldr = CustomerLoader.getInstance();
	BillLoader bldr = BillLoader.getInstance();
	
	private JList list;
    private DefaultListModel listModel;
 
    private static final String addString    = "Add Customer";
    private static final String deleteString = "Delete Customer";
    private static final String viewString   = "View Bills";
    private JButton addButton;
    private JButton deleteButton;
    private JButton viewButton;
    private JTextField newCustomerName;
 
    public CustomerViewer(String caption) {
        super(caption);
        setLayout(new BorderLayout());
        
        cldr.load();
        bldr.load();
 
        listModel = new DefaultListModel();
        for (int ii = 0; ii < cldr.getCustomerCount(); ii++) {
        	Customer c = cldr.getCustomer(ii);
        	listModel.addElement(getDisplayCustomerString(c));
        }
 
        //Create the list and put it in a scroll pane.
        list = new JList(listModel);
        list.setFont(new Font("Monospaced", Font.PLAIN, 14));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(10);
        JScrollPane listScrollPane = new JScrollPane(list);
 
        addButton = new JButton(addString);
        AddCustomerListener addListener = new AddCustomerListener(addButton);
        addButton.setActionCommand(addString);
        addButton.addActionListener(addListener);
        addButton.setEnabled(false);
 
        deleteButton = new JButton(deleteString);
        deleteButton.setActionCommand(deleteString);
        deleteButton.addActionListener(new DeleteCustomerListener());
        
        viewButton = new JButton(viewString);
        viewButton.setActionCommand(viewString);
        viewButton.addActionListener(new ViewBillsListener());
 
        newCustomerName = new JTextField(10);
        newCustomerName.addActionListener(addListener);
        newCustomerName.getDocument().addDocumentListener(addListener);
        //String name = listModel.getElementAt(
        //                      list.getSelectedIndex()).toString();
 
        //Create a panel that uses BoxLayout.
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane,
                                           BoxLayout.LINE_AXIS));
        buttonPane.add(deleteButton);
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(newCustomerName);
        buttonPane.add(addButton);
        buttonPane.add(viewButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
 
        add(listScrollPane, BorderLayout.CENTER);
        add(buttonPane, BorderLayout.PAGE_END);
    }
 
    class ViewBillsListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	int index = list.getSelectedIndex();
        	
        	Customer c = cldr.getCustomer(index);
        	if (null == c) {
        		DBG.e("CustomerView: failed to get customer(" + index + ") to view bills.");
        	}
        	
        	DBG.d("CustomerView: will launch customer(" + index + ") to view bills.");
        	
        	BillBrowser2.launch(c);
        }
    }
    
    class DeleteCustomerListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //This method can be called only if
            //there's a valid selection
            //so go ahead and remove whatever's selected.
            int index = list.getSelectedIndex();
            
            deleteCustomer(index);
            listModel.remove(index);
 
            int size = listModel.getSize();
 
            if (size == 0) { //Nobody's left, disable firing.
                deleteButton.setEnabled(false);
 
            } else { //Select an index.
                if (index == listModel.getSize()) {
                    //removed item in last position
                    index--;
                }
 
                list.setSelectedIndex(index);
                list.ensureIndexIsVisible(index);
            }
        }
    }
 
    //This listener is shared by the text field and the add button.
    class AddCustomerListener implements ActionListener, DocumentListener {
        private boolean alreadyEnabled = false;
        private JButton button;
 
        public AddCustomerListener(JButton button) {
            this.button = button;
        }
 
        //Required by ActionListener.
        public void actionPerformed(ActionEvent e) {
            String name = newCustomerName.getText();
 
            //User didn't type in a unique name...
            if (name.equals("") || alreadyInList(name)) {
                Toolkit.getDefaultToolkit().beep();
                newCustomerName.requestFocusInWindow();
                newCustomerName.selectAll();
                return;
            }
 
            int index = list.getSelectedIndex(); //get selected index
            if (index == -1) { //no selection, so insert at beginning
                index = 0;
            } else {           //add after the selected item
                index++;
            }
 
            //listModel.insertElementAt(newCustomerName.getText(), index);
            //If we just wanted to add to the end, we'd do this:
            Customer c = addCustomer(newCustomerName.getText());
            listModel.addElement(getDisplayCustomerString(c));
 
            //Reset the text field.
            newCustomerName.requestFocusInWindow();
            newCustomerName.setText("");
 
            //Select the new item and make it visible.
            list.setSelectedIndex(index);
            list.ensureIndexIsVisible(index);
        }
 
        //This method tests for string equality. You could certainly
        //get more sophisticated about the algorithm.  For example,
        //you might want to ignore white space and capitalization.
        protected boolean alreadyInList(String name) {
            return listModel.contains(name);
        }
 
        //Required by DocumentListener.
        public void insertUpdate(DocumentEvent e) {
            enableButton();
        }
 
        //Required by DocumentListener.
        public void removeUpdate(DocumentEvent e) {
            handleEmptyTextField(e);
        }
 
        //Required by DocumentListener.
        public void changedUpdate(DocumentEvent e) {
            if (!handleEmptyTextField(e)) {
                enableButton();
            }
        }
 
        private void enableButton() {
            if (!alreadyEnabled) {
                button.setEnabled(true);
            }
        }
 
        private boolean handleEmptyTextField(DocumentEvent e) {
            if (e.getDocument().getLength() <= 0) {
                button.setEnabled(false);
                alreadyEnabled = false;
                return true;
            }
            return false;
        }
    }
 
    //This method is required by ListSelectionListener.
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {
 
            if (list.getSelectedIndex() == -1) {
            //No selection, disable delete button.
                deleteButton.setEnabled(false);
 
            } else {
            //Selection, enable the delete button.
                deleteButton.setEnabled(true);
            }
        }
    }
    
    private Customer addCustomer(String name) {    	
    	Customer c = new Customer();
    	c.setName(name);
    	
    	boolean ret = cldr.addCustomer(c);
    	if (!ret) {
    		DBG.e("CustomerView: add customer failed!");
    		return null;
    	}
    	else {
    		cldr.save();
    	}
    	return c;
    }
    
    private void deleteCustomer(int index) {
    	Customer c = cldr.getCustomer(index);
    	if (c != null) {
    		bldr.deleteBills(c);
    		bldr.save();
    		
    		boolean ret = cldr.deleteCustomer(c);
    		if (!ret) {
        		DBG.e("CustomerView: delete customer(" + index + ") failed!");
        	}
    		else {
    			cldr.save();
    		}
    	}
    }
    
    private String getDisplayCustomerString(Customer c) {
    	return String.format("%-10s  %-25s  DOB=%-10s  Tel: %-12s  Email: %s", 
    			c.getAccountNumber(), c.getName(), c.mDOB, c.mPhoneNumber, c.mEmailAddress);
    }

	public static void launch() {
		JFrame frame = new CustomerViewer("AutoDocGen: Customer List");
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
