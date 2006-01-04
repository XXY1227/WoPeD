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
package org.woped.core.controller;

import java.awt.event.MouseEvent;
import java.awt.print.Printable;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.ToolTipManager;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.BasicMarqueeHandler;
import org.jgraph.graph.CellView;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.GraphUndoManager;
import org.jgraph.graph.Port;
import org.woped.core.Constants;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.model.AbstractElementModel;
import org.woped.core.model.AbstractModelProcessor;
import org.woped.core.model.ArcModel;
import org.woped.core.model.petrinet.GroupModel;
import org.woped.core.model.petrinet.NameModel;
import org.woped.core.utilities.LoggerManager;

/**
 * 
 *  @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 *
 * TODO DOCUMENTATION (lai)
 */
public abstract class AbstractGraph extends org.jgraph.JGraph implements Printable
{
    private BasicMarqueeHandler editorMarquee      = null;
    private int                 modelPorcessorType = -1;
    private GraphUndoManager    undoManager        = null;

    public AbstractGraph(DefaultGraphModel model, BasicMarqueeHandler editorMarquee, GraphUndoManager undoManager, AbstractViewFactory viewFactory, int modelPorcessorType)
    {
        if (undoManager != null)
        {
            this.undoManager = undoManager;
            this.getModel().addUndoableEditListener(undoManager);
        }
        this.setModel(model);
        this.setGraphLayoutCache(new LayoutCache(model, viewFactory));
        this.modelPorcessorType = modelPorcessorType;
        this.setGridVisible(ConfigurationManager.getConfiguration().isShowGrid());
        ToolTipManager.sharedInstance().registerComponent(this);
        this.setMarqueeHandler(editorMarquee);
        this.editorMarquee = editorMarquee;

    }

    abstract public void undo();

    abstract public void redo();

    abstract public boolean isValidConnection(AbstractElementModel sourceCell, AbstractElementModel targetCell);

    abstract public void drawNet(AbstractModelProcessor processor);

    /**
     * Groups an <code>NameModel</code> ans all additional cells to their
     * <code>PetriNetModelElement</code>.
     * 
     * @param mainElement
     *            PetriNetModelElement
     * @param name
     *            NameModel
     * @param additional
     *            other cells to group
     * @param ungroupable
     *            flag for ungrouping
     * @return
     */
    public GroupModel groupName(AbstractElementModel mainElement, NameModel name, Object[] additional, boolean ungroupable)
    {
        return group(mainElement, name, additional, ungroupable);
    }

    public void groupSelection()
    {
        GroupModel group = groupName(null, null, getSelectionCells(), true);
        if (group != null)
        {
            getGraphLayoutCache().insertGroup(group, getSelectionCells());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.JComponent#getToolTipText(java.awt.event.MouseEvent)
     */
    public String getToolTipText(MouseEvent event)
    {
        Object cell = getFirstCellForLocation(event.getX(), event.getY());
        if (cell instanceof GroupModel)
        {
            cell = ((GroupModel) cell).getMainElement();
        }
        if (cell instanceof AbstractElementModel)
        {
            return ((AbstractElementModel) cell).getToolTipText();
        } else
        {
            return null;
        }
    }

    /**
     * Groups an <code>NameModel</code> to its
     * <code>PetriNetModelElement</code>.
     * 
     * @param mainElement
     * @param name
     * @return
     */
    public GroupModel groupName(AbstractElementModel mainElement, NameModel name)
    {
        return groupName(mainElement, name, null, true);
    }

    public void ungroupSelection()
    {
        ungroup(getSelectionCells());
    }

    /**
     * TODO: DOCUMENTATION (xraven, silenco)
     * 
     * @param enable
     */
    public void enableMarqueehandler(boolean enable)
    {
        if (enable)
        {
            setMarqueeHandler(getEditorMarqueeHandler());
        } else
        {
            setMarqueeHandler(null);
        }
    }

    /**
     * TODO: DOCUMENTATION (xraven, silenco)
     * 
     * @return
     */
    public BasicMarqueeHandler getEditorMarqueeHandler()
    {
        return editorMarquee;
    }

    /**
     * @return Returns the modelPorcessorType.
     */
    public int getModelPorcessorType()
    {
        return modelPorcessorType;
    }

    /**
     * TODO: DOCUMENTATION (xraven, silenco)
     * 
     * @return
     */
    public GraphUndoManager getUndoManager()
    {
        return undoManager;
    }

    /**
     * Uses the Edge between source and target Connecting two ports
     */
    public void connect(DefaultEdge edge, Port source, Port target)
    {
        // TODO: move validation in Editor
        if (isValidConnection(source, target))
        {
            source.addEdge(edge);
            target.addEdge(edge);
            // Use Edge to Connect Source and Target
            ConnectionSet cs = new ConnectionSet(edge, source, target);
            // Create a Map that holds the attributes for the edge
            AttributeMap attr = edge.getAttributes();// GraphConstants.createMap();
            // Add a Line End Attribute
            GraphConstants.setLineEnd(attr, GraphConstants.ARROW_CLASSIC);
            // Construct a Map from cells to Maps (for insert)
            Hashtable nest = new Hashtable();
            // Associate the Edge with its Attributes
            nest.put(edge, attr);
            // Insert wants an Object-Array
            Object[] arg = new Object[] { edge };
            // Insert the Edge and its Attributes
            getGraphLayoutCache().insert(new Object[] { edge }, nest, cs, null, null);
            //
        } else
        {
            LoggerManager.warn(Constants.CORE_LOGGER, "Not a valid connection, did nothing!");
        }

    }

    /**
     * Insert a new Edge between source and target Connecting two ports
     * 
     * @deprecated
     */
    public void connect(Port source, Port target)
    {
        // Construct Edge with no label
        ArcModel edge = new ArcModel();
        connect(edge, source, target);
    }

    /**
     * TODO: DOCUMENTATION (silenco)
     * 
     * @param edge
     */
    public void connect(DefaultEdge edge)
    {
        connect(edge, (Port) edge.getSource(), (Port) edge.getTarget());
    }

    /**
     * Does the Mapping Cell <->View new. TODO: Check Performance for huge nets.
     *  
     */
    public void refreshNet()
    {
        getGraphLayoutCache().reload();
    }

    /**
     * TODO: DOCUMENTATION (silenco) TODO: move validation in Editor
     * 
     * @param source
     * @param target
     * @return
     */
    public boolean isValidConnection(Port source, Port target)
    {
        AbstractElementModel sourceCell = (AbstractElementModel) ((DefaultPort) source).getParent();
        AbstractElementModel targetCell = (AbstractElementModel) ((DefaultPort) target).getParent();
        return isValidConnection(sourceCell, targetCell);
    }

    /**
     * Groups all cells in the Array.
     * 
     * @param cells
     */
    public GroupModel group(AbstractElementModel mainElement, NameModel name, Object[] additional, boolean ungroupable)
    {
        if (mainElement != null && name != null)
        {
            // Create Group Cell
            GroupModel group = new GroupModel(mainElement, name, additional, ungroupable);
            LoggerManager.debug(Constants.CORE_LOGGER, "Grouping of Elements created.");
            return group;
        } else
        {
            GroupModel group = new GroupModel(mainElement, name, additional, ungroupable);
            return group;
        }
    }

    /**
     * Ungroups all cells in the Array.
     * 
     * @param cells
     */
    public void ungroup(Object[] cells)
    {
        // Shortcut to the model
        GraphModel m = getModel();
        GroupModel aGroup;
        // If any Cells
        if (cells != null && cells.length > 0)
        {
            // List that Holds the Groups
            ArrayList groups = new ArrayList();
            // List that Holds the Children
            ArrayList children = new ArrayList();
            // Loop Cells
            for (int i = 0; i < cells.length; i++)
            {
                // If Cell is a Group
                if (isGroup(cells[i]))
                {
                    aGroup = (GroupModel) cells[i];
                    if (aGroup.isUngroupable())
                    {
                        // Add to List of Groups
                        groups.add(cells[i]);
                        // Loop Children of Cell
                        for (int j = 0; j < m.getChildCount(cells[i]); j++)
                            // Get Child from Model
                            children.add(m.getChild(cells[i], j));
                    }
                }
                // Remove Groups from Model (Without Children)
                m.remove(groups.toArray());
                // Select Children
                setSelectionCells(children.toArray());
                LoggerManager.debug(Constants.CORE_LOGGER, "Grouping of Elements deleted. ");
            }
        }
    }

    /**
     * Returns <code>true</code> if the cell is a member in a group.
     * 
     * @param cell
     * @return boolean
     */
    public boolean isGroup(Object cell)
    {
        CellView view = getGraphLayoutCache().getMapping(cell, false);
        if (view != null) return !view.isLeaf();
        return false;
    }
}
