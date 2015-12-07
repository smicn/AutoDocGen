/*
 * CPSC-5360 Software Engineering, 2015 Spring /Project
 * 
 * Author: Shaomin (Samuel) Zhang
 * 
 * Email : smicn@foxmail.com
 * */
package com.lamar.swe.ADG;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Welcome {

	public static void main(String s[]) {

		final JFrame frame = new JFrame(
				"Auto Document Generator (Lamar-CS: CPSC5360)");
		JPanel panel = new JPanel();
		frame.add(panel);
		
		JLabel label;
		JButton button1, button2, button3;
		
		final int LAYOUT_BOX = 3;
		final int LAYOUT_BORDER = 2;
		final int layout = LAYOUT_BORDER;

		label = new JLabel("Welcome to Lamar Auto Document Generator!");
		label.setFont(new Font("Dialog", Font.BOLD, 30));
		label.setHorizontalAlignment(SwingConstants.CENTER);

		ImageIcon icon = new ImageIcon("res/docs2.jpg");
		button1 = new JButton(null, icon);
		button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/* switch to CustomerViwer */
				DBG.i("Welcome: switch to CustomerViwer.");
				CustomerViewer.launch();
			}
		});
		
		
		button2 = new JButton();
		button2.setFont(new Font("Dialog", Font.BOLD, 16));
		button2.setText("Access AutoDocGen Database");
		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/* switch to CustomerViwer */
				DBG.i("Welcome: switch to CustomerViwer.");
				CustomerViewer.launch();
			}
		});
		

		button3 = new JButton();
		button3.setFont(new Font("Dialog", Font.BOLD, 16));
		button3.setText("Run AutoDocGen Testcases");
		button3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/* switch to TestViwer */
				DBG.i("Welcome: switch to TestViwer.");
				TestViewer.launch();
			}
		});
		
		switch (layout) {
			case LAYOUT_BOX: {
				panel.setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();
				c.fill = GridBagConstraints.HORIZONTAL;
				//c.weightx = 0.5;
				c.gridx = 0;
				c.gridy = 0;
				panel.add(label, c);
				//panel.add(label);
				//c.weightx = 0.5;
				c.gridx = 0;
				c.gridy = 2;
				panel.add(button1, c);
				c.weightx = 0.4;
				c.gridx = 0;
				c.gridy = 4;
				panel.add(button2, c);
				c.weightx = 0.4;
				c.gridx = 0;
				c.gridy = 5;
				panel.add(button3, c);
				}
				break;
				
			case LAYOUT_BORDER: {
				panel.setLayout(new BorderLayout());
				panel.add(label, BorderLayout.PAGE_START);
				panel.add(button1, BorderLayout.CENTER);
				JPanel panel2 = new JPanel();
				panel2.add(button2);
				panel2.add(button3);
				panel.add(panel2, BorderLayout.PAGE_END);
				}
				break;
		}

		frame.setSize(800, 600);

		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
