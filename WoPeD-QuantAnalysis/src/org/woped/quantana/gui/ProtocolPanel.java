package org.woped.quantana.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.woped.quantana.gui.QuantitativeSimulationDialog;

@SuppressWarnings("unused")
public class ProtocolPanel extends JPanel {
	
	private static final long serialVersionUID = 3L;
	
	private QuantitativeSimulationDialog sod;
	
	private JLabel lblHeading = new JLabel("Protocol: ");
	private JTextArea txtProtocol;// = new JTextArea();
	private JScrollPane scrollPane;
	
	public ProtocolPanel(QuantitativeSimulationDialog sod){
		this.setPreferredSize(new Dimension(500,350));
		this.sod = sod;
		txtProtocol = sod.getTxtProtocol();
		
		init();
	}
	
	private void init(){
//		txtProtocol.setPreferredSize(new Dimension(460, 400));
		txtProtocol.setEditable(false);
		txtProtocol.setText("");
		sod.getProtocol();
		
		scrollPane = new JScrollPane(txtProtocol);
		scrollPane.setPreferredSize(new Dimension(460, 300));
		
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(5,5,5,5);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		add(lblHeading, constraints);
		
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		add(scrollPane, constraints);
	}
}
