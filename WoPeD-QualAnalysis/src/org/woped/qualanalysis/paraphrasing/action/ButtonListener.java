package org.woped.qualanalysis.paraphrasing.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;

import org.woped.core.controller.IEditor;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.core.utilities.LoggerManager;
import org.woped.qualanalysis.paraphrasing.Constants;
import org.woped.qualanalysis.paraphrasing.controller.WebServiceThread;
import org.woped.qualanalysis.paraphrasing.editing.TextualDescriptionDialog;
import org.woped.qualanalysis.paraphrasing.view.ParaphrasingOutput;
import org.woped.qualanalysis.paraphrasing.view.ParaphrasingPanel;
import org.woped.translations.Messages;

public class ButtonListener implements ActionListener{

	private ParaphrasingPanel paraphrasingPanel = null;
	private WebServiceThread webService = null;
	
	private JTable table = null;
	private DefaultTableModel defaultTableModel = null;
	private ParaphrasingOutput paraphrasingOutput = null;
	private IEditor editor = null;
	
	
	public ButtonListener(ParaphrasingPanel paraphrasingPanel){
		this.paraphrasingPanel = paraphrasingPanel;
		this.table = paraphrasingPanel.getParaphrasingOutput().getTable();
		this.defaultTableModel = paraphrasingPanel.getParaphrasingOutput().getDefaultTableModel();
		this.paraphrasingOutput = paraphrasingPanel.getParaphrasingOutput();
		this.editor = paraphrasingPanel.getEditor();
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		
		//New Button
		if(e.getSource() == this.paraphrasingPanel.getNewButton()){
			
			if(JOptionPane.showConfirmDialog(null, Messages.getString("Paraphrasing.New.Question.Content"), Messages.getString("Paraphrasing.New.Question.Title"), JOptionPane.YES_NO_OPTION)  == JOptionPane.YES_OPTION){
				//delete existing table and create a new one
				clearAllHighlighting();
				this.defaultTableModel.setRowCount(0);
				this.defaultTableModel.fireTableDataChanged();
				this.paraphrasingOutput.updateElementContainer();
				
				LoggerManager.info(Constants.PARAPHRASING_LOGGER, " New description table created.");	
				
			}			
		}

		//Call Webservice Button
		if(e.getSource() == this.paraphrasingPanel.getLoadButton()){
			
			if(JOptionPane.showConfirmDialog(null, Messages.getString("Paraphrasing.Load.Question.Content"), Messages.getString("Paraphrasing.Load.Question.Title"), JOptionPane.YES_NO_OPTION)  == JOptionPane.YES_OPTION){
				
				if(paraphrasingPanel.getThreadInProgress() == false){
					this.paraphrasingOutput.setAnimationVisible();
					clearAllHighlighting();
					paraphrasingPanel.setThreadInProgress(true);
					webService = new WebServiceThread(paraphrasingPanel);
					webService.start();
					webService = null;
				}				
				else{
					JOptionPane.showMessageDialog(null, Messages.getString("Paraphrasing.Webservice.ThreadInProgress.Message"),
							Messages.getString("Paraphrasing.Webservice.Error.Title"), JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
		
		//Delete Button or Delete JMenuItem
		if(e.getSource() == this.paraphrasingPanel.getDeleteButton() || e.getSource() == this.paraphrasingPanel.getDeleteItem()){
			int selectedRow = this.table.getSelectedRow();
			if(selectedRow != -1){
				if(JOptionPane.showConfirmDialog(null, Messages.getString("Paraphrasing.Delete.Question.Notification"), Messages.getString("Paraphrasing.Delete.Question.Title"), JOptionPane.YES_NO_OPTION)  == JOptionPane.YES_OPTION){

					this.defaultTableModel.removeRow(selectedRow);
					clearAllHighlighting();
					
					this.paraphrasingOutput.updateElementContainer();
					
					LoggerManager.info(Constants.PARAPHRASING_LOGGER, "   ... Description deleted.");	
					
				}
			}
			else{
				JOptionPane.showMessageDialog(null,Messages.getString("Paraphrasing.Delete.Notification"));
			}
			
		}
		
		
		//Add Button or Add JMenuItem
		if(e.getSource() == this.paraphrasingPanel.getAddButton() || e.getSource() == this.paraphrasingPanel.getAddItem()){

			clearAllHighlighting();
			new TextualDescriptionDialog(this.paraphrasingPanel.getEditor() , this.table, this.paraphrasingPanel.getParaphrasingOutput().getDefaultTableModel(), "new");

		}
		
		
		//Up Button or Up JMenuItem
		if(e.getSource() == this.paraphrasingPanel.getUpButton() || e.getSource() == this.paraphrasingPanel.getUpItem()){
			int selectedRow = this.paraphrasingPanel.getParaphrasingOutput().getTable().getSelectedRow();
			if(selectedRow != -1){
				if(this.table.getSelectedRowCount() == 1){
					//Only swap row if a row except the first is selected
					if(selectedRow > 0){
						this.defaultTableModel.moveRow(selectedRow, selectedRow, selectedRow-1);
						this.table.setRowSelectionInterval(selectedRow-1, selectedRow-1);
						this.paraphrasingOutput.updateElementContainer();
					}
				}
			}
			else{
				JOptionPane.showMessageDialog(null,Messages.getString("Paraphrasing.Up.Notification"));
			}
		}
		
		//Down Button or Down JMenuItem
		if(e.getSource() == this.paraphrasingPanel.getDownButton()  || e.getSource() == this.paraphrasingPanel.getDownItem()){
			int selectedRow = this.table.getSelectedRow();
			if(selectedRow != -1){
				if(this.table.getSelectedRowCount() == 1){
					//Only swap row if a row except the last is selected
					if(selectedRow < this.defaultTableModel.getRowCount()-1){
						this.defaultTableModel.moveRow(selectedRow, selectedRow, selectedRow+1);
						this.table.setRowSelectionInterval(selectedRow+1, selectedRow+1);
						this.paraphrasingOutput.updateElementContainer();
					}
				}
			}
			else{
				JOptionPane.showMessageDialog(null,Messages.getString("Paraphrasing.Down.Notification"));
			}
		}
		
		
		//Export Button
		if(e.getSource() == this.paraphrasingPanel.getExportButton()){
			
			boolean fileTypeOk = false;
			if (this.defaultTableModel.getRowCount() > 0){
			JFileChooser jFileChooser = new JFileChooser();
			jFileChooser.setFileFilter(new FileFilter() {
	            public boolean accept(File f) {
	                return f.getName().toLowerCase().endsWith(".txt");
	            }
	            public String getDescription() {
	                return "*.txt File";
	            }
	        });
				while(!fileTypeOk){
					if(jFileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
						String path = jFileChooser.getSelectedFile().getAbsolutePath();
						if(!path.endsWith(".txt")){
							path = path + ".txt";
						}

						try{
							fileTypeOk = true;
							File file = new File(path);
								
							FileWriter out = new FileWriter(file);
							
							out.write("Created with Workflow PetriNet Designer Version " + Messages.getString("Application.Version") +" (woped.org) \n\n");
							//Export Ids
							if(JOptionPane.showConfirmDialog(null, Messages.getString("Paraphrasing.Export.Question.Message"), Messages.getString("Paraphrasing.Export.Question.Title"), JOptionPane.YES_NO_OPTION)  == JOptionPane.YES_OPTION){
								for(int i = 0; i < this.paraphrasingPanel.getParaphrasingOutput().getDefaultTableModel().getRowCount(); i++){
									//out.write(this.paraphrasingPanel.getParaphrasingOutput().getDefaultTableModel().getValueAt(i,0) + " - " + this.paraphrasingPanel.getParaphrasingOutput().getDefaultTableModel().getValueAt(i,1) + "\n");
									out.write(this.defaultTableModel.getValueAt(i,0) + "\n");
									out.write(this.defaultTableModel.getValueAt(i,1) + "\n\n");

								}
							}				            	
							else{
								for(int i = 0; i < this.defaultTableModel.getRowCount(); i++){
									out.write(this.defaultTableModel.getValueAt(i,1) + "\n\n");
								}
							}
							LoggerManager.info(Constants.PARAPHRASING_LOGGER, " Description exported to: " + path);	
							
							out.close();
						}
						catch(Exception ex){
//							System.out.println("Fehler: " + ex);
							
							JOptionPane.showMessageDialog(null, Messages.getString("Paraphrasing.Export.Error.Message") + "\n" + ex.getMessage(),
									Messages.getString("Paraphrasing.Export.Error.Title"), JOptionPane.INFORMATION_MESSAGE);
						}
						
					
					}
					else{
						fileTypeOk = true;
					}
					
				}
				
			}
			else{
				JOptionPane.showMessageDialog(null,Messages.getString("Paraphrasing.Export.Numberelements.Message"),Messages.getString("Paraphrasing.Export.Numberelements.Title"), JOptionPane.INFORMATION_MESSAGE);
			}	
		}
		
		//Properties JMenuItem
		if(e.getSource() == this.paraphrasingPanel.getPropertiesItem()){
			int selectedRow = this.table.getSelectedRow();
			if(selectedRow != -1){
				new TextualDescriptionDialog(this.editor, this.table, this.defaultTableModel, "edit", selectedRow);
				this.table.setRowSelectionInterval(selectedRow,selectedRow);
			}
		}
	}
	
	private void clearAllHighlighting(){

		Iterator<AbstractPetriNetElementModel> i = editor.getModelProcessor().getElementContainer().getRootElements().iterator();
		
		while (i.hasNext()) {
			AbstractPetriNetElementModel current = (AbstractPetriNetElementModel) i.next();
			current.setHighlighted(false);
			if (current.getType() == AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE)
            {
            	OperatorTransitionModel operatorModel = (OperatorTransitionModel) current;
                Iterator<AbstractPetriNetElementModel> simpleTransIter = operatorModel.getSimpleTransContainer().getElementsByType(AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE).values().iterator();
                while (simpleTransIter.hasNext())
                {
                    AbstractPetriNetElementModel simpleTransModel = (AbstractPetriNetElementModel) simpleTransIter.next();
                    if (simpleTransModel != null 
                            && operatorModel.getSimpleTransContainer().getElementById(simpleTransModel.getId()).getType() == AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE)
                    {
                    	simpleTransModel.setHighlighted(false);
                    }
                }
            }
		}
		table.clearSelection();
		editor.repaint();		
		table.repaint();
	}
}