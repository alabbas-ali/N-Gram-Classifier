package pt.tumba.parser.swf;

import java.io.IOException;

/**
 *  A Morph Shape Symbol
 *
 *@author     unknown
 *@created    15 de Setembro de 2002
 */
public class MorphShape extends Symbol {
    /**
     *  Description of the Field
     */
    protected Shape shape1;
    /**
     *  Description of the Field
     */
    protected Shape shape2;


    /**
     *  Constructor for the MorphShape object
     *
     *@param  shape1  Description of the Parameter
     *@param  shape2  Description of the Parameter
     */
    public MorphShape(Shape shape1, Shape shape2) {
        this.shape1 = shape1;
        this.shape2 = shape2;
    }


    /**
     *  Gets the shape1 attribute of the MorphShape object
     *
     *@return    The shape1 value
     */
    public Shape getShape1() {
        return shape1;
    }


    /**
     *  Gets the shape2 attribute of the MorphShape object
     *
     *@return    The shape2 value
     */
    public Shape getShape2() {
        return shape2;
    }


    /**
     *  Sets the shape1 attribute of the MorphShape object
     *
     *@param  s  The new shape1 value
     */
    public void setShape1(Shape s) {
        shape1 = s;
    }


    /**
     *  Sets the shape2 attribute of the MorphShape object
     *
     *@param  s  The new shape2 value
     */
    public void setShape2(Shape s) {
        shape2 = s;
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

        SWFShape shape = definitionWriter.tagDefineMorphShape(id,
                shape1.getRect(),
                shape2.getRect());
        shape1.hasAlpha = true;
        shape2.hasAlpha = true;

        shape1.writeShape(shape);
        shape2.writeShape(shape);

        return id;
    }
}
