package org.woped.controller;

import java.awt.geom.Point2D;

import javax.swing.JComponent;

import org.jgraph.graph.DefaultGraphCell;
import org.woped.model.AbstractElementModel;
import org.woped.model.AbstractModelProcessor;
import org.woped.model.ArcModel;
import org.woped.model.CreationMap;
import org.woped.model.petrinet.PetriNetModelElement;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 * 
 * An Editor has to implement the IEditor interface.
 */
public interface IEditor extends IViewController
{
    /**
     * Returns the Graph for the Editor
     * 
     * @return
     */
    public AbstractGraph getGraph();

    /**
     * Creates the Element in the Graph and stores it in the used ModelProcessor
     * 
     * @param creationMap
     * @return
     */
    public AbstractElementModel createElement(CreationMap creationMap);

    /**
     * Creates the Arc in the Graph and stores it in the used ModelProcessor
     * 
     * @param map
     * @return
     */
    public ArcModel createArc(CreationMap map);

    /**
     * Returns the used ModelProcessor
     * 
     * @return
     */
    public AbstractModelProcessor getModelProcessor();

    /**
     * Sets the ModelProcessor to use.
     * 
     * @param modelProcessor
     */
    public void setModelProcessor(AbstractModelProcessor modelProcessor);

    /**
     * Remembers the last mouse postition
     * 
     * @param position
     */
    public void setLastMousePosition(Point2D position);

    /**
     * Returns the last mouse position the AbstractMarqueeHandler has noticed.
     * It is necessary register the AbstractmarqueeHandler at the used Graph
     * when instanciated.
     * 
     * @return position
     */
    public Point2D getLastMousePosition();

    /**
     * Adds an routing point to the arc.
     * 
     * @param arc
     * @param point
     */
    public void addPointToArc(ArcModel arc, Point2D point);

    /**
     * Returns the drawing mode. If the net is in drawing mode, clicking the
     * left mouse button will draw the Element with the set creation type.
     * 
     * @see getCreateElementType
     * @return drawing mode
     */
    public boolean isDrawingMode();

    /**
     * Sets the drawing mode. If the net is in drawing mode, clicking the left
     * mouse button will draw the Element with the set creation type.
     * 
     * @see getCreateElementType
     * @param flag
     */
    public void setDrawingMode(boolean flag);

    /**
     * Returns the type of the element, which will be created in drawing mode.
     * 
     * @see PetriNetModelElement for element types
     * @return int
     */
    public int getCreateElementType();

    /**
     * Sets the type of the element, which will be created in drawing mode.
     * 
     * @see PetriNetModelElement for element types
     * @param createElementType
     *  
     */
    public void setCreateElementType(int createElementType);

    /**
     * Returns the Containter in witch the Editor is running. The Editor doesn't
     * need a Container if it is itself the Component.
     * 
     * @return
     */
    public JComponent getContainer();

    /**
     * Sets (ONLY THE REFERENCE to) the Container in witch the Editor is
     * running. The Editor doesn't need a Container if it is itself the
     * Component.
     * 
     * @param container
     */
    public void setContainer(JComponent container);
   
    public void deleteCells(Object toDelete[]);
    
    public void deleteCell(DefaultGraphCell toDelete);
}
