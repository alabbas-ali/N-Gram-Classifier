package pt.tumba.parser.swf;

import java.io.IOException;

/**
 *  Implements the SWFTags interface and does nothing Use as a sink when output
 *  is not required
 *
 *@author     unknown
 *@created    15 de Setembro de 2002
 */
public class DummySWFWriter implements SWFTags {
    /**
     *  Interface SWFTags
     *
     *@param  version          Description of the Parameter
     *@param  length           Description of the Parameter
     *@param  twipsWidth       Description of the Parameter
     *@param  twipsHeight      Description of the Parameter
     *@param  frameRate        Description of the Parameter
     *@param  frameCount       Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void header(int version, long length,
            int twipsWidth, int twipsHeight,
            int frameRate, int frameCount) throws IOException {
        //do nothing
    }


    /**
     *  Interface SWFTags
     *
     *@param  tagType          Description of the Parameter
     *@param  longTag          Description of the Parameter
     *@param  contents         Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tag(int tagType, boolean longTag,
            byte[] contents) throws IOException {
        //do nothing
    }
}
