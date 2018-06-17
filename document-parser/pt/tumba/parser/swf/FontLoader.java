package pt.tumba.parser.swf;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 *  Font loading utilities
 *
 *@author     unknown
 *@created    15 de Setembro de 2002
 */
public class FontLoader extends SWFTagTypesImpl {
    /**
     *  Description of the Field
     */
    protected FontDefinition fontDef;


    /**
     *  Constructor for the FontLoader object
     */
    public FontLoader() {
        super(null);
    }


    /**
     *  Load the first font from the given Flash movie
     *
     *@param  filename         Description of the Parameter
     *@return                  null if no font was found
     *@exception  IOException  Description of the Exception
     */
    public static FontDefinition loadFont(String filename)
             throws IOException {
        FileInputStream in = new FileInputStream(filename);

        FontDefinition def = loadFont(in);

        in.close();

        return def;
    }


    /**
     *  Load the first font from the given Flash movie
     *
     *@param  flashMovie       Description of the Parameter
     *@return                  null if no font was found
     *@exception  IOException  Description of the Exception
     */
    public static FontDefinition loadFont(InputStream flashMovie)
             throws IOException {
        FontLoader fontloader = new FontLoader();
        SWFTags swfparser = new TagParser(fontloader);
        SWFReader swfreader = new SWFReader(swfparser, flashMovie);

        swfreader.readFile();

        return fontloader.fontDef;
    }


    /**
     *  SWFTagTypes Interface
     *
     *@param  id               Description of the Parameter
     *@param  flags            Description of the Parameter
     *@param  name             Description of the Parameter
     *@param  numGlyphs        Description of the Parameter
     *@param  ascent           Description of the Parameter
     *@param  descent          Description of the Parameter
     *@param  leading          Description of the Parameter
     *@param  codes            Description of the Parameter
     *@param  advances         Description of the Parameter
     *@param  bounds           Description of the Parameter
     *@param  kernCodes1       Description of the Parameter
     *@param  kernCodes2       Description of the Parameter
     *@param  kernAdjustments  Description of the Parameter
     *@return                  Description of the Return Value
     *@exception  IOException  Description of the Exception
     */
    public SWFVectors tagDefineFont2(int id, int flags, String name, int numGlyphs,
            int ascent, int descent, int leading,
            int[] codes, int[] advances, Rect[] bounds,
            int[] kernCodes1, int[] kernCodes2,
            int[] kernAdjustments) throws IOException {
        if (fontDef != null) {
            return null;
        }
        //only load first font

        double twips = (double) SWFConstants.TWIPS;

        fontDef = new FontDefinition(name,
                ((double) ascent) / twips,
                ((double) descent) / twips,
                ((double) leading) / twips,
                (flags & SWFConstants.FONT2_UNICODE) != 0,
                (flags & SWFConstants.FONT2_SHIFTJIS) != 0,
                (flags & SWFConstants.FONT2_ANSI) != 0,
                (flags & SWFConstants.FONT2_ITALIC) != 0,
                (flags & SWFConstants.FONT2_BOLD) != 0,
                (flags & SWFConstants.FONT2_HAS_LAYOUT) != 0);

        if (kernCodes1 != null && kernCodes1.length > 0) {
            //System.out.println( "Number of Kernings --> " + kernCodes1.length );

            List kerns = fontDef.getKerningPairList();

            for (int i = 0; i < kernCodes1.length; i++) {
                FontDefinition.KerningPair pair =
                        new FontDefinition.KerningPair(
                        kernCodes1[i],
                        kernCodes2[i],
                        ((double) kernAdjustments[i]) / twips);

                kerns.add(pair);
            }
        }

        return new VectorImpl(codes, advances, bounds);
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class VectorImpl implements SWFVectors {
        /**
         *  Description of the Field
         */
        protected int[] codes;
        /**
         *  Description of the Field
         */
        protected int[] advances;
        /**
         *  Description of the Field
         */
        protected Rect[] bounds;
        /**
         *  Description of the Field
         */
        protected int i;
        /**
         *  Description of the Field
         */
        protected Shape shape;
        /**
         *  Description of the Field
         */
        protected int currx;
        /**
         *  Description of the Field
         */
        protected int curry;
        /**
         *  Description of the Field
         */
        protected double twips = (double) SWFConstants.TWIPS;


        /**
         *  Constructor for the VectorImpl object
         *
         *@param  codes     Description of the Parameter
         *@param  advances  Description of the Parameter
         *@param  bounds    Description of the Parameter
         */
        protected VectorImpl(int[] codes, int[] advances, Rect[] bounds) {
            this.codes = codes;
            this.advances = advances;
            this.bounds = bounds;
            i = 0;

            shape = new Shape();
        }


        /**
         *  Description of the Method
         */
        public void done() {
            //System.out.println( "------------" );
            double advance = (advances == null) ? 0.0 : ((double) advances[i]) / twips;
            int code = codes[i];

            Rect rect = bounds[i];
            shape.minX = ((double) rect.getMinX()) / twips;
            shape.minY = ((double) rect.getMinY()) / twips;
            shape.maxX = ((double) rect.getMaxX()) / twips;
            shape.maxY = ((double) rect.getMaxY()) / twips;

            FontDefinition.Glyph g = new FontDefinition.Glyph(shape, advance, code);

            fontDef.getGlyphList().add(g);

            i++;

            if (i < codes.length) {
                shape = new Shape();
            }

            currx = curry = 0;
        }


        /**
         *  Description of the Method
         *
         *@param  dx  Description of the Parameter
         *@param  dy  Description of the Parameter
         */
        public void line(int dx, int dy) {
            currx += dx;
            curry += dy;

            shape.line(currx / twips, curry / twips);

            //System.out.println( "L: " + dx + " " + dy + "     " + currx + " " + curry );
        }


        /**
         *  Description of the Method
         *
         *@param  cx  Description of the Parameter
         *@param  cy  Description of the Parameter
         *@param  dx  Description of the Parameter
         *@param  dy  Description of the Parameter
         */
        public void curve(int cx2, int cy2, int dx2, int dy2) {
            int cx = cx2+currx;
            int cy = cy2+curry;
            int dx = dx2+cx;
            int dy = dy2+cy;

            currx = dx;
            curry = dy;

            shape.curve(dx / twips, dy / twips, cx / twips, cy / twips);

            //System.out.println( "C: " + cx + " " + cy + " " + dx + " " + dy +
            //                    "     " + (cx/twips) + " " + (cy/twips) +
            //                    " " + (dx/twips) + " " + (dy/twips) );

        }


        /**
         *  Description of the Method
         *
         *@param  x  Description of the Parameter
         *@param  y  Description of the Parameter
         */
        public void move(int x, int y) {
            currx = x;
            curry = y;

            shape.move(x / twips, y / twips);

            //System.out.println( "M: " + x + " " + y + "     " + (x/twips) + " " + (y/twips) );
        }
    }
}
