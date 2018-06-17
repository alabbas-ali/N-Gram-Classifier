package pt.tumba.parser.swf;

import java.io.IOException;
import java.io.InputStream;

/**
 *  Base class for Image symbols. Note that Images cannot be placed directly on
 *  the stage - they have to be used as image fills for shapes.
 *
 *@author     unknown
 *@created    15 de Setembro de 2002
 */
public abstract class Image extends Symbol {
    /**
     *  A lossless image (similar to PNG). There are 3 formats - 8, 16 and 32
     *  bit. For 8 and 16 bit images there is a color table and the image data
     *  consists of either an 8 or 16 bit index into the table for each pixel.
     *  32 bit images have no color table - each pixel consists of 4 bytes:
     *  (alpha,red,green,blue). If there is no alpha then the first byte will be
     *  255. For all formats, the length of each row of pixel data must be a
     *  multiple of 32 bits. If the actual row data is smaller then it should be
     *  padded up to next multiple of 32 bits.
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    public static class Lossless extends Image {
        /**
         *  Description of the Field
         */
        protected byte[] imageData;
        /**
         *  Description of the Field
         */
        protected Color[] colorTable;
        /**
         *  Description of the Field
         */
        protected double width;
        /**
         *  Description of the Field
         */
        protected double height;
        /**
         *  Description of the Field
         */
        protected boolean hasAlpha;
        /**
         *  Description of the Field
         */
        protected int format;


        /**
         *@param  colorTable  may be null for 32 bit bitmaps
         *@param  imageData   the pixel data
         *@param  width       in pixels
         *@param  height      in pixels
         *@param  hasAlpha    whether the image contains alpha values
         *@param  format      one of: SWFConstants.BITMAP_FORMAT_8_BIT,
         *      SWFConstants.BITMAP_FORMAT_16_BIT,
         *      SWFConstants.BITMAP_FORMAT_32_BIT
         */
        public Lossless(Color[] colorTable, byte[] imageData, double width,
                double height, boolean hasAlpha, int format) {
            this.colorTable = colorTable;
            this.imageData = imageData;
            this.width = width;
            this.height = height;
            this.hasAlpha = hasAlpha;
            this.format = format;
        }


        /**
         *  Gets the imageData attribute of the Lossless object
         *
         *@return    The imageData value
         */
        public byte[] getImageData() {
            return imageData;
        }


        /**
         *  Gets the colorTable attribute of the Lossless object
         *
         *@return    The colorTable value
         */
        public Color[] getColorTable() {
            return colorTable;
        }


        /**
         *  Gets the width attribute of the Lossless object
         *
         *@return    The width value
         */
        public double getWidth() {
            return width;
        }


        /**
         *  Gets the height attribute of the Lossless object
         *
         *@return    The height value
         */
        public double getHeight() {
            return height;
        }


        /**
         *  Description of the Method
         *
         *@return    Description of the Return Value
         */
        public boolean hasAlpha() {
            return hasAlpha;
        }


        /**
         *  Gets the format attribute of the Lossless object
         *
         *@return    The format value
         */
        public int getFormat() {
            return format;
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

            if (hasAlpha) {
                definitionWriter.tagDefineBitsLossless2(
                        id, format, (int) width, (int) height,
                        colorTable, imageData);
            } else {
                definitionWriter.tagDefineBitsLossless(
                        id, format, (int) width, (int) height,
                        colorTable, imageData);
            }

            return id;
        }
    }


    /**
     *  A JPEG Image that can be used as a fill for Shapes. The JPEG image must
     *  be "baseline" - a "progressive" JPEG will cause the Flash player to have
     *  runtime problems.
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    public static class JPEG extends Image {
        /**
         *  Description of the Field
         */
        protected InputStream jpegIn;
        /**
         *  Description of the Field
         */
        protected byte[] jpegData;


        /**
         *  A JPEG image that will read from an input stream.
         *
         *@param  jpegImage  Description of the Parameter
         */
        public JPEG(InputStream jpegImage) {
            jpegIn = jpegImage;
        }


        /**
         *  Construct a JPEG image from byte data. Note that the data must
         *  include the JPEG header ( 0xff,0xd9,0xff,0xd8 ).
         *
         *@param  imageData  Description of the Parameter
         */
        public JPEG(byte[] imageData) {
            jpegData = imageData;
        }


        /**
         *  Get the raw image data. This will include the JPEG stream header(s)
         *  ( 0xff,0xd9,0xff,0xd8 ).
         *
         *@return    The imageData value
         */
        public byte[] getImageData() {
            return jpegData;
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

            if (jpegData != null) {
                definitionWriter.tagDefineBitsJPEG2(id, jpegData);
            } else if (jpegIn != null) {
                definitionWriter.tagDefineBitsJPEG2(id, jpegIn);
            }

            return id;
        }
    }

}
