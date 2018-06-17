package pt.tumba.parser.swf;

import java.io.IOException;

/**
 *  A QuickTime Movie Symbol
 *
 *@author     unknown
 *@created    15 de Setembro de 2002
 */
public class QTMovie extends Symbol {
    /**
     *  Description of the Field
     */
    protected String name;


    /**
     *  Constructor for the QTMovie object
     *
     *@param  name  Description of the Parameter
     */
    public QTMovie(String name) {
        this.name = name;
    }


    /**
     *  Gets the name attribute of the QTMovie object
     *
     *@return    The name value
     */
    public String getName() {
        return name;
    }


    /**
     *  Sets the name attribute of the QTMovie object
     *
     *@param  name  The new name value
     */
    public void setName(String name) {
        this.name = name;
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

        definitionWriter.tagDefineQuickTimeMovie(id, name);

        return id;
    }
}
