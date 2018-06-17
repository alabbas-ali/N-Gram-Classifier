package pt.tumba.parser.swf;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *  Utility to unprotect a SWF. First arg is input filename. Second arg is
 *  output filename.
 *
 *@author     unknown
 *@created    15 de Setembro de 2002
 */
public class Unprotector implements SWFTags {
    /**
     *  Description of the Field
     */
    protected SWFTags writer;


    /**
     *  Constructor for the Unprotector object
     *
     *@param  writer  Description of the Parameter
     */
    public Unprotector(SWFTags writer) {
        this.writer = writer;
    }


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
        writer.header(version, -1,
                twipsWidth, twipsHeight,
                frameRate, frameCount);
    }


    /**
     *  Interface SWFTags
     *
     *@param  tagType          Description of the Parameter
     *@param  longTag          Description of the Parameter
     *@param  contents         Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tag(int tagType, boolean longTag, byte[] contents)
             throws IOException {
        if (tagType == SWFConstants.TAG_PROTECT) {
            return;
        }
        //skip protect tag

        writer.tag(tagType, longTag, contents);
    }


    /**
     *  The main program for the Unprotector class
     *
     *@param  args             The command line arguments
     *@exception  IOException  Description of the Exception
     */
    public static void main(String[] args) throws IOException {
        FileInputStream in = new FileInputStream(args[0]);
        FileOutputStream out = new FileOutputStream(args[1]);

        SWFWriter writer = new SWFWriter(out);
        Unprotector unprotector = new Unprotector(writer);
        SWFReader reader = new SWFReader(unprotector, in);
        reader.readFile();

        in.close();
        out.flush();
        out.close();
    }
}
