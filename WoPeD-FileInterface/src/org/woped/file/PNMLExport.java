/*
 * 
 * Copyright (C) 2004-2005, see @author in JavaDoc for the author 
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *
 * For contact information please visit http://woped.ba-karlsruhe.de
 *
 */
package org.woped.file;

import java.awt.geom.Point2D;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TAssign;
import org.woped.bpel.gui.transitionproperties.BaseActivity;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.IStatusBar;
import org.woped.core.model.ArcModel;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.model.petrinet.AbstractPetriNetModelElement;
import org.woped.core.model.petrinet.EditorLayoutInfo;
import org.woped.core.model.petrinet.NameModel;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.core.model.petrinet.PetriNetModelElement;
import org.woped.core.model.petrinet.PlaceModel;
import org.woped.core.model.petrinet.ResourceClassModel;
import org.woped.core.model.petrinet.ResourceModel;
import org.woped.core.model.petrinet.SimulationModel;
import org.woped.core.model.petrinet.SubProcessModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.core.model.petrinet.TransitionResourceModel;
import org.woped.core.model.petrinet.TriggerModel;
import org.woped.core.utilities.LoggerManager;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.pnml.AnnotationGraphisType;
import org.woped.pnml.ArcNameType;
import org.woped.pnml.ArcToolspecificType;
import org.woped.pnml.ArcType;
import org.woped.pnml.DimensionType;
import org.woped.pnml.FiredtransitionType;
import org.woped.pnml.GraphicsArcType;
import org.woped.pnml.GraphicsNodeType;
import org.woped.pnml.GraphicsSimpleType;
import org.woped.pnml.NetToolspecificType;
import org.woped.pnml.NetType;
import org.woped.pnml.NodeNameType;
import org.woped.pnml.OperatorType;
import org.woped.pnml.OrganizationUnitType;
import org.woped.pnml.TPartnerLinks;
import org.woped.pnml.TVariables;

import org.woped.pnml.PlaceToolspecificType;
import org.woped.pnml.PlaceType;
import org.woped.pnml.PnmlDocument;
import org.woped.pnml.PnmlType;
import org.woped.pnml.PositionType;
import org.woped.pnml.ResourceMappingType;
import org.woped.pnml.ResourceType;
import org.woped.pnml.ResourcesType;
import org.woped.pnml.RoleType;
import org.woped.pnml.SimulationType;
import org.woped.pnml.SimulationsType;
import org.woped.pnml.ToolspecificType;
import org.woped.pnml.TransitionResourceType;
import org.woped.pnml.TransitionToolspecificType;
import org.woped.pnml.TransitionType;
import org.woped.pnml.TransitionsequenceType;
import org.woped.pnml.TriggerType;
import org.woped.pnml.NetType.Page;
import org.woped.translations.Messages;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 * 
 * Created on: 13.01.2005 Last Change on: 13.01.2005
 */
public class PNMLExport
{

    public PNMLExport(IStatusBar[] statusBars)
    {
        this.statusBars = statusBars;
    }

    private PnmlDocument       pnmlDoc    = null;

    private IStatusBar[]       statusBars = null;

    public static final String comment    = "\nPLEASE DO NOT EDIT THIS FILE\nCreated with Workflow PetriNet Designer Version 1.0 (woped.org)\n";

    /**
     * Method saveToFile. Saves a PetriNet Object to *.pnml File.
     * 
     * @param fileName
     */
    public boolean saveToFile(EditorVC editor, String fileName)
    {
        LoggerManager.debug(Constants.FILE_LOGGER, "##### START PNML EXPORT #####");
        long begin = System.currentTimeMillis();
        try
        {
            createJavaBeansInstances(editor);
            XmlOptions opt = new XmlOptions();
            opt.setUseDefaultNamespace();
            opt.setSavePrettyPrint();
            opt.setSavePrettyPrintIndent(2);
            Map<String, String> map = new HashMap<String, String>();
            map.put("", "pnml.woped.org");
            opt.setSaveImplicitNamespaces(map);

            pnmlDoc.save(new File(fileName), opt);
            
            return true;
        } catch (IOException e)
        {
            LoggerManager.error(Constants.FILE_LOGGER, "Could not save file. " + e.getMessage());
            return false;
        } finally
        {
            LoggerManager.debug(Constants.FILE_LOGGER, "##### END PNML EXPORT ##### (" + (System.currentTimeMillis() - begin) + " ms)");
        }
    }
    
    /**
     * Method saveToFile. Saves a PetriNet Object to *.pnml File.
     * 
     * @param fileName
     */
    public boolean saveToWebFile(EditorVC editor, ByteArrayOutputStream baos)
    {
        LoggerManager.debug(Constants.FILE_LOGGER, "##### START PNML EXPORT #####");
        long begin = System.currentTimeMillis();
        try
        {
            createJavaBeansInstances(editor);
            XmlOptions opt = new XmlOptions();
            opt.setUseDefaultNamespace();
            opt.setSavePrettyPrint();
            opt.setSavePrettyPrintIndent(2);
            Map<String, String> map = new HashMap<String, String>();
            map.put("", "pnml.woped.org");
            opt.setSaveImplicitNamespaces(map);

            pnmlDoc.save(baos, opt);
            
            return true;
        } catch (IOException e)
        {
            LoggerManager.error(Constants.FILE_LOGGER, "Could not write to the Stream. " + e.getMessage());
            return false;
        } finally
        {
            LoggerManager.debug(Constants.FILE_LOGGER, "##### END PNML EXPORT ##### (" + (System.currentTimeMillis() - begin) + " ms)");
        }
    }


    private void createJavaBeansInstances(EditorVC editor)
    {
        ModelElementContainer elementContainer = editor.getModelProcessor().getElementContainer();
        PetriNetModelProcessor petrinetModel = (PetriNetModelProcessor) editor.getModelProcessor();
        pnmlDoc = PnmlDocument.Factory.newInstance();
        PnmlType iPnml = pnmlDoc.addNewPnml();

        // Initialisieren der Statusbar
        int resources = petrinetModel.getResources().size();
        int roles = petrinetModel.getRoles().size();
        int orgaUnits = petrinetModel.getOrganizationUnits().size();
        int resourcesMapping = petrinetModel.getResourceMapping().size();
        int rootElements = elementContainer.getRootElements().size();
        int arcs = elementContainer.getArcMap().size();
        // TODO (blackfox) - extend statusbars for simulations

        for (int i = 0; i < statusBars.length; i++)
            statusBars[i].startProgress("Save to File", resources + roles + resourcesMapping + orgaUnits + rootElements + arcs);

        // ------------------------------
        // pnmlDoc.documentProperties();
        XmlCursor cursor = iPnml.newCursor();
        cursor.insertComment(comment);
        /* ##### NET ##### */
        NetType iNet = iPnml.addNewNet();
        // attr type
        iNet.setType(petrinetModel.getType());
        // attr id
        iNet.setId(petrinetModel.getId());
        // name

        if (petrinetModel.getName() != null)
        {
            iNet.addNewName().setText(petrinetModel.getName());
        }
        if (ConfigurationManager.getConfiguration().isExportToolspecific())
        {
            NetToolspecificType iNetToolSpec = iNet.addNewToolspecific();

            iNetToolSpec.setTool("WoPeD");
            iNetToolSpec.setVersion("1.0"); //TODO Version aus properties �bernehmen!?
            //Get PartnerLinks and Variables
            TPartnerLinks iPLs = iNetToolSpec.addNewPartnerLinks();
            if(petrinetModel.getElementContainer().getTPartnerLinkList()!=null)iPLs.set(petrinetModel.getElementContainer().getTPartnerLinkList());
            TVariables iVs = iNetToolSpec.addNewVariables();
            if(petrinetModel.getElementContainer().getVariableList()!=null)iVs.set(petrinetModel.getElementContainer().getTVariablesList());
            // graphics
            GraphicsSimpleType iGraphicsNet = iNetToolSpec.addNewBounds();
            EditorLayoutInfo layoutInfo = editor.getSavedLayoutInfo();
            if (layoutInfo.getSavedSize() != null)
            {
                DimensionType dim = iGraphicsNet.addNewDimension();
                dim.setX(new BigDecimal(layoutInfo.getSavedSize().getWidth()));
                dim.setY(new BigDecimal(layoutInfo.getSavedSize().getHeight()));
            }
            if (layoutInfo.getSavedLocation() != null)
            {
            	PositionType location = iGraphicsNet.addNewPosition();
                location.setX(new BigDecimal(layoutInfo.getSavedLocation().getX()));
                location.setY(new BigDecimal(layoutInfo.getSavedLocation().getY()));
            }
            // Store the width of the tree view
            iNetToolSpec.setTreeWidth(layoutInfo.getTreeViewWidth());
            // resources
            ResourcesType iNetResources = iNetToolSpec.addNewResources();
            // Rescources
            ResourceType iResourceType;
            ResourceModel rModelTemp;

            for (Iterator<ResourceModel> iter = petrinetModel.getResources().iterator(); iter.hasNext();)
            {
                rModelTemp = (ResourceModel) iter.next();
                iResourceType = iNetResources.addNewResource();
                iResourceType.setName(rModelTemp.getName());
                for (int i = 0; i < statusBars.length; i++)
                    statusBars[i].nextStep();
            }
            // Roles

            RoleType iRoleType;
            ResourceClassModel roleModelTemp;
            for (Iterator<ResourceClassModel> iter = petrinetModel.getRoles().iterator(); iter.hasNext();)
            {
                roleModelTemp = (ResourceClassModel) iter.next();
                iRoleType = iNetResources.addNewRole();
                iRoleType.setName(roleModelTemp.getName());
                for (int i = 0; i < statusBars.length; i++)
                    statusBars[i].nextStep();
            }
            // Orga Units
            OrganizationUnitType iOrganizationUnitType;
            ResourceClassModel orgunitModelTemp;
            for (Iterator<ResourceClassModel> iter = petrinetModel.getOrganizationUnits().iterator(); iter.hasNext();)
            {
                orgunitModelTemp = (ResourceClassModel) iter.next();
                iOrganizationUnitType = iNetResources.addNewOrganizationUnit();
                iOrganizationUnitType.setName(orgunitModelTemp.getName());
                for (int i = 0; i < statusBars.length; i++)
                    statusBars[i].nextStep();
            }
            // ResourceMap
            ResourceMappingType iNetResourceMap;            
            for (Iterator<String> iter = petrinetModel.getResourceMapping().keySet().iterator();iter.hasNext();)
            {
                String tempResourceClass = (String) iter.next();
                Vector<String> values = petrinetModel.getResourceMapping().get(tempResourceClass);
                // TODO check if mapping exists NullPointerExeption bei
                // speicherung ge�nderter orgUnit die keine zugeordnete Resource
                // hat!
                for (Iterator<String> iterator = values.iterator(); iterator.hasNext();)
                {
                    iNetResourceMap = iNetResources.addNewResourceMapping();
                    iNetResourceMap.setResourceClass(tempResourceClass);
                    iNetResourceMap.setResourceID(iterator.next().toString());
                }
                for (int i = 0; i < statusBars.length; i++)
                    statusBars[i].nextStep();
            }
            
            // Simulations
            SimulationsType iNetSimulations = iNetToolSpec.addNewSimulations();
            SimulationType iSimulation;
            TransitionsequenceType iTransitionsequence;
            FiredtransitionType iFiredTransition;
            for (Iterator<SimulationModel> iter = petrinetModel.getSimulations().iterator();iter.hasNext();)
            {
            	SimulationModel currSimulation = iter.next();
            	int answer = 0;
            	if(!petrinetModel.isLogicalFingerprintEqual(currSimulation.getFingerprint()))
            	{
            		Object[] options = {Messages.getString("Tokengame.ChangedNetDialog.ButtonKeep"),Messages.getString("Tokengame.ChangedNetDialog.ButtonDelete")};
        			answer = JOptionPane.showOptionDialog(null, Messages.getString("Tokengame.ChangedNetDialog.Export.Message").replaceAll("##SIMULATIONNAME##", currSimulation.getName()), Messages.getString("Tokengame.ChangedNetDialog.Title"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
        			// if the user didn't choose one of the buttons but closed the OptionDialog don't drop the simulation
        			if(answer == -1)
        			{
        				answer = 0;
        			}
            	}
            	if(answer == 0)
            	{
                	iSimulation = iNetSimulations.addNewSimulation();
                	iSimulation.setId(currSimulation.getId());
                	iSimulation.setSimulationname(currSimulation.getName());
                	iTransitionsequence = iSimulation.addNewTransitionsequence();
                	for(Iterator<TransitionModel> iterator = currSimulation.getFiredTransitions().iterator();iterator.hasNext();)
                	{
                		iFiredTransition = iTransitionsequence.addNewFiredtransition();
                		iFiredTransition.setTransitionID((iterator.next()).getId());
                	}
                	iSimulation.setNetFingerprint(currSimulation.getFingerprint());
                	LoggerManager.debug(Constants.FILE_LOGGER, "   ... Simulation (ID:" + currSimulation.getId() + ") set");
            	}
            	else
            	{
            		LoggerManager.debug(Constants.FILE_LOGGER, "   ... Simulation (ID:" + currSimulation.getId() + ") dropped by user");
            	}
            }
            
            // toolspecific
            for (short i = 0; i < petrinetModel.getUnknownToolSpecs().size(); i++)
            {
                iNet.addNewToolspecific();
                if (petrinetModel.getUnknownToolSpecs().get(i) instanceof ToolspecificType)
                {
                    iNet.setToolspecificArray(iNet.getToolspecificArray().length - 1, (NetToolspecificType) petrinetModel.getUnknownToolSpecs().get(i));
                }
            }
        }
        // Now save the root model element container into the 
        // NetType XMLBean holding our net
        saveModelElementContainer(iNet, elementContainer);

    }
    
    //! Dump the specified ModelElementContainer into the specified XMLBeans bean responsible for
    //! the net layout
    //! This method may be called multiple times:
    //! It is called once for the main model (the root net)
    //! and recursively for all sub-process ModelElementContainer instances found
    //! @param iNet specifies the XMLBeans object representing the PNML section that will store the specified net
    //! @param modelElementContainer specifies the ModelElementContainer to be stored to the specified XMLBean
    private void saveModelElementContainer(NetType iNet, ModelElementContainer elementContainer)
    {
        Iterator root2Iter = elementContainer.getRootElements().iterator();
        while (root2Iter.hasNext())
        {
            PetriNetModelElement currentModel = (PetriNetModelElement) root2Iter.next();
            /* ##### PLACES ##### */
            if (currentModel.getType() == PetriNetModelElement.PLACE_TYPE)
            {
                initPlace(iNet.addNewPlace(), (PlaceModel) currentModel);
            } else if (currentModel.getType() == PetriNetModelElement.TRANS_SIMPLE_TYPE)
            /* ##### TRANSITION ##### */
            {
                initTransition(iNet.addNewTransition(), (TransitionModel) currentModel, null);

            } else if (currentModel.getType() == PetriNetModelElement.SUBP_TYPE)
            {
                // A sub-process is a reference transition with an associated page 
            	// First, generate the transition itself
                initTransition(iNet.addNewTransition(), (TransitionModel) currentModel, null);
                // Create the page and add the sub-net to it
                // by calling ourselves recursively
                Page newPage = iNet.addNewPage();
                // Associate the new page with the ID of the sub-process model
                // so it can be assigned back later on when importing the net
                newPage.setId(currentModel.getId());
                // Create a new XMLBean representing the sub-net
                NetType newNet = newPage.addNewNet();
                
                ModelElementContainer subProcessContainer =                 
                	((SubProcessModel)currentModel).getSimpleTransContainer();
                
                EditorLayoutInfo subProcessLayout =subProcessContainer.getEditorLayoutInfo();
                if (subProcessLayout!=null)
                {
                	// This sub-process model stores some information about
                	// the layout of the subprocessor editor
                	// Convert it to XMLBeans information and store it
                	NetToolspecificType subPToolSpec = newNet.addNewToolspecific();
                	
                	subPToolSpec.setTool("WoPeD");
                	subPToolSpec.setVersion("1.0");
                	// graphics
                	GraphicsSimpleType iGraphicsNet = subPToolSpec.addNewBounds();
                	if (subProcessLayout.getSavedSize() != null)
                	{
                		DimensionType dim = iGraphicsNet.addNewDimension();
                		dim.setX(new BigDecimal(subProcessLayout.getSavedSize().getWidth()));
                		dim.setY(new BigDecimal(subProcessLayout.getSavedSize().getHeight()));
                	}
                	if (subProcessLayout.getSavedLocation() != null)
                	{
                		PositionType location = iGraphicsNet.addNewPosition();
                		location.setX(new BigDecimal(subProcessLayout.getSavedLocation().getX()));
                		location.setY(new BigDecimal(subProcessLayout.getSavedLocation().getY()));
                	}
                	// Store the width of the tree view
                	subPToolSpec.setTreeWidth(subProcessLayout.getTreeViewWidth());
                }
                
                // Call ourselves recursively to store the sub-process net
                saveModelElementContainer(newNet, subProcessContainer);                
            } else if (currentModel.getType() == PetriNetModelElement.TRANS_OPERATOR_TYPE)
            {
            	// Special handling code for operators:
            	// Instead of the operator itself, the inner transitions and places 
            	// will be written to the PNML file
            	// Their location (screen coordinates) are those of the original operator
            	// (and also have to be because the operator screen location is not stored separately
            	// but restored from its replacement elements)

                LoggerManager.debug(Constants.FILE_LOGGER, "   ... Setting InnerTtransitions for Operator (ID:" + currentModel.getId() + ")");
                OperatorTransitionModel operatorModel = (OperatorTransitionModel) currentModel;
                Iterator simpleTransIter = operatorModel.getSimpleTransContainer().getElementsByType(AbstractPetriNetModelElement.TRANS_SIMPLE_TYPE).values().iterator();
                while (simpleTransIter.hasNext())
                {
                    PetriNetModelElement simpleTransModel = (PetriNetModelElement) simpleTransIter.next();
                    if (simpleTransModel != null // Sometimes the iterator
                            // returns null...
                            && operatorModel.getSimpleTransContainer().getElementById(simpleTransModel.getId()).getType() == PetriNetModelElement.TRANS_SIMPLE_TYPE)
                    {
                        initTransition(iNet.addNewTransition(), (TransitionModel) operatorModel.getSimpleTransContainer().getElementById(simpleTransModel.getId()), operatorModel);
                    }

                }
                if (operatorModel.getCenterPlace() != null)
                {
                    PlaceType iCenterPlace = initPlace(iNet.addNewPlace(), operatorModel.getCenterPlace());
                    initToolspecific(iCenterPlace.addNewToolspecific(), operatorModel.getCenterPlace(), operatorModel.getId(), operatorModel.getOperatorType());
                }
                LoggerManager.debug(Constants.FILE_LOGGER, "   ... InnerTtransitions set.");
            }
            for (int i = 0; i < statusBars.length; i++)
                statusBars[i].nextStep();
        }
        /* ##### ARCS ##### */
        
        // When iterating through our arcs, we remember all
        // transitions that are either source or destination of
        // any arc we encounter
        // Instead of serializing the arc itself, we serialize
        // the "inner arcs" of all such transitions
        // To sort out duplicates, we create a set
        Set<PetriNetModelElement> connectedTransitions = new HashSet<PetriNetModelElement>();  
        Iterator arcIter = elementContainer.getArcMap().keySet().iterator();
        while (arcIter.hasNext())
        {
            ArcModel currentArc = elementContainer.getArcById(arcIter.next());
            PetriNetModelElement currentTargetModel = (PetriNetModelElement) elementContainer.getElementById(currentArc.getTargetId());
            PetriNetModelElement currentSourceModel = (PetriNetModelElement) elementContainer.getElementById(currentArc.getSourceId());
            // Remember either source or target if it is a transition
            // Please note that one special condition of petri nets is that
            // a transition is never directly connected to another transition
            // so either source or target may be a transition, never both
            if (currentTargetModel.getType() == PetriNetModelElement.TRANS_OPERATOR_TYPE)
            	connectedTransitions.add(currentTargetModel);
            else if (currentSourceModel.getType() == PetriNetModelElement.TRANS_OPERATOR_TYPE)
            	connectedTransitions.add(currentSourceModel);
            else
            {
            	// The current arc is not connected to any transition
            	// We do not need to take care of any inner arcs
            	// and instead store the currentArc itself
                initArc(iNet.addNewArc(), currentArc, null);
            }
            for (int i = 0; i < statusBars.length; i++)
                statusBars[i].nextStep();
        }
    	// A transition can be a very complex construct consisting
    	// of a lot more than just one primitive petri-net transition (e.g.
    	// XOR Split, XOR Join, ...
    	// When dumping the PNML structure we must create primitive petri-net
    	// objects for applications that cannot read our tool specific
    	// complex transitions
    	// This is why all transitions store a map of primitive transitions
    	// with (ID, Object-Reference) entries.
        // For all transitions connected to at least one arc we will
        // dump the internal arcs now instead of the (previously ignored) visible arcs
        Iterator currentTransition = connectedTransitions.iterator();
        while (currentTransition.hasNext())
        {
        	OperatorTransitionModel currentConnectedModel = (OperatorTransitionModel)currentTransition.next();
        	Iterator innerArcIter = currentConnectedModel.getSimpleTransContainer().getArcMap().keySet().iterator();
        	while (innerArcIter.hasNext())
        	{
           		// Dump all inner arcs of connected transitions
        		ArcModel currentInnerArc = (ArcModel) currentConnectedModel.getSimpleTransContainer().getArcMap().get(innerArcIter.next());
        		// Find outer arc corresponding to inner arc
        		// (carries graphics information)
        		ArcModel currentOuterArc = null;
        		if (elementContainer.getElementById(currentInnerArc.getSourceId())!=null)
        		{
        			currentOuterArc = elementContainer.findArc(currentInnerArc.getSourceId(),
        					currentConnectedModel.getId()); 
        		}
        		if (elementContainer.getElementById(currentInnerArc.getTargetId())!=null)
        		{
        			currentOuterArc = elementContainer.findArc(currentConnectedModel.getId(), 
        					currentInnerArc.getTargetId()); 
        		}
        		
        		// Always try to pass an outer arc with graphics information
        		// (contains way points)
        		initArc(iNet.addNewArc(), (currentOuterArc!=null)?currentOuterArc:currentInnerArc, 
        				currentInnerArc);
        	}
        }    	
    }

    private PlaceType initPlace(PlaceType iPlace, PlaceModel currentModel)
    {
        // Name
        initNodeName(iPlace.addNewName(), currentModel.getNameModel());
        // initNodeName
        initElementGraphics(iPlace.addNewGraphics(), currentModel);
        // initalMarkings
        if (currentModel.getTokenCount() > 0)
        {
            iPlace.addNewInitialMarking().setText(String.valueOf(currentModel.getTokenCount()));
        }
        // toolspecific
        if (ConfigurationManager.getConfiguration().isExportToolspecific())
        {
            for (short i = 0; i < currentModel.getUnknownToolSpecs().size(); i++)
            {
                iPlace.addNewToolspecific();
                if (currentModel.getUnknownToolSpecs().get(i) instanceof ToolspecificType)
                {
                    iPlace.setToolspecificArray(iPlace.getToolspecificArray().length - 1, (PlaceToolspecificType) currentModel.getUnknownToolSpecs().get(i));
                }
                // TODO 1 Hochz�hlen
            }
        }
        // attr. id
        iPlace.setId(currentModel.getId());
        LoggerManager.debug(Constants.FILE_LOGGER, "   ... Place (ID:" + currentModel.getId() + ") set");

        return iPlace;
    }

    private TransitionType initTransition(TransitionType iTransition, TransitionModel currentModel, OperatorTransitionModel operatorModel)
    {
        TransitionModel takenModel = operatorModel == null ? currentModel : operatorModel;
        // name
        initNodeName(iTransition.addNewName(), takenModel.getNameModel());
        // graphics
        initElementGraphics(iTransition.addNewGraphics(), takenModel);
        if (ConfigurationManager.getConfiguration().isExportToolspecific())
        {
            // toolspecific
            for (short i = 0; i < takenModel.getUnknownToolSpecs().size(); i++)
            {
                iTransition.addNewToolspecific();
                if (takenModel.getUnknownToolSpecs().get(i) instanceof ToolspecificType)
                {
                    iTransition.setToolspecificArray(iTransition.getToolspecificArray().length - 1, (TransitionToolspecificType) takenModel.getUnknownToolSpecs().get(i));
                }
                // TODO 1 Hochz�hlen
            }
            initToolspecific(iTransition.addNewToolspecific(), takenModel);
        }
        // attr. id
        iTransition.setId(currentModel.getId());
        LoggerManager.debug(Constants.FILE_LOGGER, "   ... Transition (ID:" + currentModel.getId() + ") set");

        return iTransition;
    }

    private NodeNameType initNodeName(NodeNameType nodeName, NameModel element)
    {
        // name
        nodeName.setText(element.getNameValue());
        /*
         * graphics
         * 
         * An annotation's graphics part requires an offset element describing
         * the offset the lower left point of the surrounding text box has to
         * the reference point of the net object on which the annotation occurs.
         * TOD O:
         */
        AnnotationGraphisType iGraphics = nodeName.addNewGraphics();
        PositionType pos = iGraphics.addNewOffset();
        pos.setX(BigDecimal.valueOf(element.getX()));
        pos.setY(BigDecimal.valueOf(element.getY()));

        return nodeName;
    }

    private GraphicsNodeType initElementGraphics(GraphicsNodeType iGraphics, AbstractPetriNetModelElement element)
    {
        DimensionType dim = iGraphics.addNewDimension();
        dim.setX(BigDecimal.valueOf(element.getWidth()));
        dim.setY(BigDecimal.valueOf(element.getHeight()));
        PositionType pos = iGraphics.addNewPosition();
        pos.setX(BigDecimal.valueOf(element.getX()));
        pos.setY(BigDecimal.valueOf(element.getY()));

        return iGraphics;
    }

    private PlaceToolspecificType initToolspecific(PlaceToolspecificType iToolspecific, PlaceModel currentModel, String operatorId, int operatorType)
    {
        iToolspecific.setTool("WoPeD");
        iToolspecific.setVersion("1.0");
        initOperator(iToolspecific.addNewOperator(), operatorId, operatorType);

        return iToolspecific;
    }

    private TransitionToolspecificType initToolspecific(TransitionToolspecificType iToolspecific, TransitionModel currentModel)
    {
        iToolspecific.setTool("WoPeD");
        iToolspecific.setVersion("1.0");
        if (org.woped.bpel.gui.transitionproperties.Assign.class.isInstance(currentModel.getBpelData())){
        	org.woped.pnml.TAssign iAssign = iToolspecific.addNewAssign();
        	iAssign.set((XmlObject)((BaseActivity)currentModel.getBpelData()).getActivity());
        }
        if (org.woped.bpel.gui.transitionproperties.Invoke.class.isInstance(currentModel.getBpelData())){
        	org.woped.pnml.TInvoke iInvoke = iToolspecific.addNewInvoke();
        	iInvoke.set((XmlObject)((BaseActivity)currentModel.getBpelData()).getActivity());
        }
        if (org.woped.bpel.gui.transitionproperties.Receive.class.isInstance(currentModel.getBpelData())){
        	org.woped.pnml.TReceive iReceive = iToolspecific.addNewReceive();
        	iReceive.set((XmlObject)((BaseActivity)currentModel.getBpelData()).getActivity());
        }
        if (org.woped.bpel.gui.transitionproperties.Reply.class.isInstance(currentModel.getBpelData())){
        	org.woped.pnml.TReply iReply = iToolspecific.addNewReply();
        	iReply.set((XmlObject)((BaseActivity)currentModel.getBpelData()).getActivity());
        }
        if (org.woped.bpel.gui.transitionproperties.Wait.class.isInstance(currentModel.getBpelData())){
        	org.woped.pnml.TWait iWait = iToolspecific.addNewWait();
        	iWait.set((XmlObject)((BaseActivity)currentModel.getBpelData()).getActivity());
        }
        /*if (org.woped.bpel.gui.transitionproperties.Empty.class.isInstance(currentModel.getBpelData())){
        	org.woped.pnml.TEmpty iEmpty = iToolspecific.addNewEmpty();
        	iEmpty.set((XmlObject)((BaseActivity)currentModel.getBpelData()).getActivity());
        }*/
        if (currentModel.getToolSpecific().getOperatorId() != null)
        {
            initOperator(iToolspecific.addNewOperator(), currentModel.getToolSpecific().getOperatorId(), currentModel.getToolSpecific().getOperatorType());
            LoggerManager.debug(Constants.FILE_LOGGER, "   ... Operator for Transition (ID:" + currentModel.getId() + ") set");

        }
        if (currentModel.getToolSpecific().getTrigger() != null)
        {
            initTrigger(iToolspecific.addNewTrigger(), currentModel.getToolSpecific().getTrigger());
            LoggerManager.debug(Constants.FILE_LOGGER, "   ... Trigger for Transition (ID:" + currentModel.getId() + ") set");
        }
        if (currentModel.getToolSpecific().isSubprocess())
        {
            iToolspecific.setSubprocess(true);
            LoggerManager.debug(Constants.FILE_LOGGER, "   ... Subprocess (ID:" + currentModel.getId() + ") set");
        }
        if (currentModel.getToolSpecific().getTransResource() != null)
        {
            initTransResource(iToolspecific.addNewTransitionResource(), currentModel.getToolSpecific().getTransResource());
            LoggerManager.debug(Constants.FILE_LOGGER, "   ... Resource for Transition (ID:" + currentModel.getId() + ") set");
        }
        // Store the timing of this transition
        iToolspecific.setTime(currentModel.getToolSpecific().getTime());
        iToolspecific.setTimeUnit(currentModel.getToolSpecific().getTimeUnit());
        return iToolspecific;
    }

    private OperatorType initOperator(OperatorType iOperator, String id, int type)
    {
        // attr. id
        iOperator.setId(id);
        // attr. type
        iOperator.setType(type);
        return iOperator;
    }

    private TriggerType initTrigger(TriggerType iTrigger, TriggerModel trigger)
    {
        // attr. id
        iTrigger.setId(trigger.getId());
        // attr. type
        iTrigger.setType(trigger.getTriggertype());
        // graphics
        GraphicsSimpleType iGraphics = iTrigger.addNewGraphics();
        DimensionType dim = iGraphics.addNewDimension();
        dim.setX(BigDecimal.valueOf(trigger.getWidth()));
        dim.setY(BigDecimal.valueOf(trigger.getHeight()));
        PositionType pos = iGraphics.addNewPosition();
        pos.setX(BigDecimal.valueOf(trigger.getX()));
        pos.setY(BigDecimal.valueOf(trigger.getY()));
        return iTrigger;
    }

    private TransitionResourceType initTransResource(TransitionResourceType iTransResource, TransitionResourceModel transResource)
    {
        // set Role & orgUnit
        iTransResource.setOrganizationalUnitName(transResource.getTransOrgUnitName());
        iTransResource.setRoleName(transResource.getTransRoleName());

        // graphics
        GraphicsSimpleType iGraphics = iTransResource.addNewGraphics();
        DimensionType dim = iGraphics.addNewDimension();
        dim.setX(BigDecimal.valueOf(transResource.getWidth()));
        dim.setY(BigDecimal.valueOf(transResource.getHeight()));
        PositionType pos = iGraphics.addNewPosition();
        pos.setX(BigDecimal.valueOf(transResource.getX()));
        pos.setY(BigDecimal.valueOf(transResource.getY()));
        return iTransResource;
    }

    //! Initialize arc dump to XML beans
    //! @param outerArc specifies the outer arc to be dumped.
    //!        The outerArc argument may not be null. It is the element whose
    //!        graphics information (way-points) will be dumped
    //! @param innerArc specifies the (optional) inner arc to be dumped
    //!        If !=null, this arc will be dumped to PNML, together with the graphics
    //!        information of the specified outerArc
    private ArcType initArc(ArcType iArc, ArcModel outerArc, ArcModel innerArc)
    {
        ArcModel useArc = innerArc == null ? outerArc : innerArc;
        // inscription
        initNodeName(iArc.addNewInscription(), useArc);
        // graphics
        initArcGraphics(iArc.addNewGraphics(), outerArc);
        // attr. id
        iArc.setId(outerArc.getId());
        // attr. source
        iArc.setSource(useArc.getSourceId());
        if (ConfigurationManager.getConfiguration().isExportToolspecific())
        {
            ArcToolspecificType iArcTool = iArc.addNewToolspecific();
            iArcTool.setTool("WoPeD");
            iArcTool.setVersion("1.0");
            if (outerArc.isRoute())
                iArcTool.setRoute(true);
            iArcTool.setProbability(outerArc.getProbability());
            iArcTool.setDisplayProbabilityOn(outerArc.isDisplayOn());
            PositionType probPos = iArcTool.addNewDisplayProbabilityPosition();
            Point2D probPosPoint = outerArc.getLabelPosition();
            probPos.setX(BigDecimal.valueOf(probPosPoint.getX()));
            probPos.setY(BigDecimal.valueOf(probPosPoint.getY()));

            iArcTool.setDisplayProbabilityPosition(probPos);
            // toolspecific
            for (short i = 0; i < outerArc.getUnknownToolSpecs().size(); i++)
            {
                iArc.addNewToolspecific();
                if (outerArc.getUnknownToolSpecs().get(i) instanceof ToolspecificType)
                {
                    iArc.setToolspecificArray(iArc.getToolspecificArray().length - 1, (ArcToolspecificType) outerArc.getUnknownToolSpecs().get(i));
                }
            }
        }
        // attr. target
        iArc.setTarget(useArc.getTargetId());
        LoggerManager.debug(Constants.FILE_LOGGER, "   ... Arc (ID:" + useArc.getId() + "( " + useArc.getSourceId() + " -> " + useArc.getTargetId() + ") set");

        return iArc;
    }

    private ArcNameType initNodeName(ArcNameType nodeName, ArcModel element)
    {
        // name
        nodeName.setText(element.getInscriptionValue());
        // graphics
        /*
         * AnnotationGraphisType iGraphics = nodeName.addNewGraphics();
         * PositionType pos = iGraphics.addNewOffset();
         * pos.setX(BigDecimal.valueOf(element.getX()));
         * pos.setY(BigDecimal.valueOf(element.getY())); pos =
         * iGraphics.addNewOffset(); pos.setX(BigDecimal.valueOf(element.getX() +
         * element.getWidth())); pos.setY(BigDecimal.valueOf(element.getY() +
         * element.getHeight()));
         */

        return nodeName;
    }

    private GraphicsArcType initArcGraphics(GraphicsArcType iGraphics, ArcModel arc)
    {
        // position
        if (arc.getPoints().length > 2)
        {
            PositionType pos;
            for (int i = 1; i < arc.getPoints().length - 1; i++)
            {
                pos = iGraphics.addNewPosition();
                pos.setX(BigDecimal.valueOf((int) arc.getPoints()[i].getX()));
                pos.setY(BigDecimal.valueOf((int) arc.getPoints()[i].getY()));
            }
        }
        // line
        // ...none

        return iGraphics;
    }
    //
    // private boolean isOperator(PetriNet net, String elementId)
    // {
    // if (elementId != null &&
    // net.getElementContainer().getElementById(elementId) != null
    // && net.getElementContainer().getElementById(elementId).getType() ==
    // PetriNetModelElement.TRANS_OPERATOR_TYPE)
    // {
    // return true;
    // } else
    // {
    // return false;
    // }
    // }
}