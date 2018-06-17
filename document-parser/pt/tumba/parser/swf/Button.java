package pt.tumba.parser.swf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 *  A Button Symbol
 *
 *@author     unknown
 *@created    15 de Setembro de 2002
 */
public class Button extends Symbol {
    /**
     *  A layer of a button. The layer defines a symbol (Shape etc.) with
     *  associated Transform and color transform. There may be many layers in a
     *  button and each layer may take part in one or more of the 4 button
     *  states (up,over,down,hit-test).
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    public static class Layer {
        /**
         *  Description of the Field
         */
        protected Symbol symbol;
        /**
         *  Description of the Field
         */
        protected Transform matrix;
        /**
         *  Description of the Field
         */
        protected AlphaTransform cxform;
        /**
         *  Description of the Field
         */
        protected int depth;
        /**
         *  Description of the Field
         */
        protected boolean usedForHitArea, usedForUp, usedForDown, usedForOver;


        /**
         *  Gets the symbol attribute of the Layer object
         *
         *@return    The symbol value
         */
        public Symbol getSymbol() {
            return symbol;
        }


        /**
         *  Gets the transform attribute of the Layer object
         *
         *@return    The transform value
         */
        public Transform getTransform() {
            return matrix;
        }


        /**
         *  Gets the coloring attribute of the Layer object
         *
         *@return    The coloring value
         */
        public AlphaTransform getColoring() {
            return cxform;
        }


        /**
         *  Gets the depth attribute of the Layer object
         *
         *@return    The depth value
         */
        public int getDepth() {
            return depth;
        }


        /**
         *  Gets the usedForHitArea attribute of the Layer object
         *
         *@return    The usedForHitArea value
         */
        public boolean isUsedForHitArea() {
            return usedForHitArea;
        }


        /**
         *  Gets the usedForUp attribute of the Layer object
         *
         *@return    The usedForUp value
         */
        public boolean isUsedForUp() {
            return usedForUp;
        }


        /**
         *  Gets the usedForDown attribute of the Layer object
         *
         *@return    The usedForDown value
         */
        public boolean isUsedForDown() {
            return usedForDown;
        }


        /**
         *  Gets the usedForOver attribute of the Layer object
         *
         *@return    The usedForOver value
         */
        public boolean isUsedForOver() {
            return usedForOver;
        }


        /**
         *  Sets the symbol attribute of the Layer object
         *
         *@param  symbol  The new symbol value
         */
        public void setSymbol(Symbol symbol) {
            this.symbol = symbol;
        }


        /**
         *  Sets the transform attribute of the Layer object
         *
         *@param  matrix  The new transform value
         */
        public void setTransform(Transform matrix) {
            this.matrix = matrix;
        }


        /**
         *  Sets the coloring attribute of the Layer object
         *
         *@param  cxform  The new coloring value
         */
        public void setColoring(AlphaTransform cxform) {
            this.cxform = cxform;
        }


        /**
         *  Sets the depth attribute of the Layer object
         *
         *@param  depth  The new depth value
         */
        public void setDepth(int depth) {
            this.depth = depth;
        }


        /**
         *  Description of the Method
         *
         *@param  f  Description of the Parameter
         */
        public void usedForHitArea(boolean f) {
            usedForHitArea = f;
        }


        /**
         *  Description of the Method
         *
         *@param  f  Description of the Parameter
         */
        public void usedForUp(boolean f) {
            usedForUp = f;
        }


        /**
         *  Description of the Method
         *
         *@param  f  Description of the Parameter
         */
        public void usedForDown(boolean f) {
            usedForDown = f;
        }


        /**
         *  Description of the Method
         *
         *@param  f  Description of the Parameter
         */
        public void usedForOver(boolean f) {
            usedForOver = f;
        }


        /**
         *@param  depth           should be >= 1 and there should only be one
         *      symbol on any layer
         *@param  symbol          Description of the Parameter
         *@param  matrix          Description of the Parameter
         *@param  cxform          Description of the Parameter
         *@param  usedForHitArea  Description of the Parameter
         *@param  usedForUp       Description of the Parameter
         *@param  usedForDown     Description of the Parameter
         *@param  usedForOver     Description of the Parameter
         */
        public Layer(Symbol symbol, Transform matrix, AlphaTransform cxform,
                int depth, boolean usedForHitArea, boolean usedForUp,
                boolean usedForDown, boolean usedForOver) {
            if (matrix == null) {
                matrix = new Transform();
            }
            if (cxform == null) {
                cxform = new AlphaTransform();
            }

            this.symbol = symbol;
            this.matrix = matrix;
            this.cxform = cxform;
            this.depth = depth;
            this.usedForHitArea = usedForHitArea;
            this.usedForUp = usedForUp;
            this.usedForDown = usedForDown;
            this.usedForOver = usedForOver;
        }


        /**
         *  Gets the record attribute of the Layer object
         *
         *@param  movie             Description of the Parameter
         *@param  timelineWriter    Description of the Parameter
         *@param  definitionWriter  Description of the Parameter
         *@return                   The record value
         *@exception  IOException   Description of the Exception
         */
        protected ButtonRecord2 getRecord(Movie movie,
                SWFTagTypes timelineWriter,
                SWFTagTypes definitionWriter)
                 throws IOException {
            //--Make sure symbol is defined
            int symId = symbol.define(movie, timelineWriter, definitionWriter);

            int flags = 0;
            if (usedForHitArea) {
                flags |= ButtonRecord.BUTTON_HITTEST;
            }
            if (usedForUp) {
                flags |= ButtonRecord.BUTTON_UP;
            }
            if (usedForDown) {
                flags |= ButtonRecord.BUTTON_DOWN;
            }
            if (usedForOver) {
                flags |= ButtonRecord.BUTTON_OVER;
            }

            return new ButtonRecord2(symId, depth, matrix, cxform, flags);
        }
    }


    /**
     *  Description of the Field
     */
    protected List actions = new ArrayList();
    /**
     *  Description of the Field
     */
    protected List layers = new ArrayList();
    /**
     *  Description of the Field
     */
    protected boolean trackAsMenu;


    /**
     *  Constructor for the Button object
     *
     *@param  trackAsMenu  Description of the Parameter
     */
    public Button(boolean trackAsMenu) {
        this.trackAsMenu = trackAsMenu;
    }


    /**
     *  Gets the trackedAsMenu attribute of the Button object
     *
     *@return    The trackedAsMenu value
     */
    public boolean isTrackedAsMenu() {
        return trackAsMenu;
    }


    /**
     *  Description of the Method
     *
     *@param  f  Description of the Parameter
     */
    public void trackAsMenu(boolean f) {
        trackAsMenu = f;
    }


    /**
     *  Access the list of Button.Layer objects
     *
     *@return    The buttonLayers value
     */
    public List getButtonLayers() {
        return layers;
    }


    /**
     *  Access the list of Actions objects
     *
     *@return    The actions value
     */
    public List getActions() {
        return actions;
    }


    /**
     *  Add a layer to the button.
     *
     *@param  depth           should be >= 1 and there should only be one symbol
     *      on any layer
     *@param  symbol          The feature to be added to the Layer attribute
     *@param  matrix          The feature to be added to the Layer attribute
     *@param  cxform          The feature to be added to the Layer attribute
     *@param  usedForHitArea  The feature to be added to the Layer attribute
     *@param  usedForUp       The feature to be added to the Layer attribute
     *@param  usedForDown     The feature to be added to the Layer attribute
     *@param  usedForOver     The feature to be added to the Layer attribute
     *@return                 Description of the Return Value
     */
    public Button.Layer addLayer(Symbol symbol, Transform matrix,
            AlphaTransform cxform,
            int depth, boolean usedForHitArea, boolean usedForUp,
            boolean usedForDown, boolean usedForOver) {
        Layer layer = new Layer(symbol, matrix, cxform, depth,
                usedForHitArea, usedForUp, usedForDown, usedForOver);

        layers.add(layer);
        return layer;
    }


    /**
     *  Adds a feature to the Actions attribute of the Button object
     *
     *@param  conditionFlags  The feature to be added to the Actions attribute
     *@param  flashVersion    The feature to be added to the Actions attribute
     *@return                 Description of the Return Value
     */
    public Actions addActions(int conditionFlags, int flashVersion) {
        Actions acts = new Actions(conditionFlags, flashVersion);
        actions.add(acts);
        return acts;
    }


    /**
     *  Description of the Method
     *
     *@param  movie             Description of the Parameter
     *@param  timelineWriter    Description of the Parameter
     *@param  definitionWriter  Description of the Parameter
     *@return                   Description of the Return Value
     *@exception  IOException   Description of the Exception
     */
    protected int defineSymbol(Movie movie,
            SWFTagTypes timelineWriter,
            SWFTagTypes definitionWriter)
             throws IOException {
        int id = getNextId(movie);

        Vector recs = new Vector();
        for (Iterator it = layers.iterator(); it.hasNext(); ) {
            Layer layer = (Layer) it.next();
            recs.addElement(layer.getRecord(movie, timelineWriter, definitionWriter));
        }

        SWFActions acts = definitionWriter.tagDefineButton2(id, trackAsMenu, recs);

        for (Iterator it = actions.iterator(); it.hasNext(); ) {
            Actions actions = (Actions) it.next();

            acts.start(actions.getConditions());
            acts.blob(actions.bytes);
        }

        acts.done();

        return id;
    }
}
