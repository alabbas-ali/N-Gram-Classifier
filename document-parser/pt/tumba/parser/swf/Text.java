package pt.tumba.parser.swf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *  A Text Symbol.
 *
 *@author     unknown
 *@created    15 de Setembro de 2002
 */
public class Text extends Symbol {
    /**
     *  A set of contiguous characters in one font, size and color.
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    public static class Row {
        /**
         *  Description of the Field
         */
        protected Font.Chars chars;
        /**
         *  Description of the Field
         */
        protected double x;
        /**
         *  Description of the Field
         */
        protected double y;
        /**
         *  Description of the Field
         */
        protected boolean hasX;
        /**
         *  Description of the Field
         */
        protected boolean hasY;
        /**
         *  Description of the Field
         */
        protected Color color;


        /**
         *  Gets the chars attribute of the Row object
         *
         *@return    The chars value
         */
        public Font.Chars getChars() {
            return chars;
        }


        /**
         *  Gets the x attribute of the Row object
         *
         *@return    The x value
         */
        public double getX() {
            return x;
        }


        /**
         *  Gets the y attribute of the Row object
         *
         *@return    The y value
         */
        public double getY() {
            return y;
        }


        /**
         *  Gets the color attribute of the Row object
         *
         *@return    The color value
         */
        public Color getColor() {
            return color;
        }


        /**
         *  Description of the Method
         *
         *@return    Description of the Return Value
         */
        public boolean hasX() {
            return hasX;
        }


        /**
         *  Description of the Method
         *
         *@return    Description of the Return Value
         */
        public boolean hasY() {
            return hasY;
        }


        /**
         *@param  chars  the characters to display
         *@param  color  may be AlphaColor.
         *@param  x      new X position for text - only valid if hasX is true
         *@param  y      new Y position for text - only valid if hasY is true
         *@param  hasX   Description of the Parameter
         *@param  hasY   Description of the Parameter
         */
        public Row(Font.Chars chars, Color color, double x, double y, boolean hasX, boolean hasY) {
            this.chars = chars;
            this.color = color;
            this.x = x;
            this.y = y;
            this.hasX = hasX;
            this.hasY = hasY;
        }


        /**
         *  Description of the Method
         *
         *@param  text             Description of the Parameter
         *@param  changeColor      Description of the Parameter
         *@param  changeFont       Description of the Parameter
         *@exception  IOException  Description of the Exception
         */
        protected void write(SWFText text, boolean changeColor, boolean changeFont)
                 throws IOException {
            if (changeFont) {
                Font font = chars.getFont();
                int fontid = font.getId();

                text.font(fontid, (int) (chars.getSize() * SWFConstants.TWIPS));
            }

            if (changeColor) {
                text.color(color);
            }
            if (hasX) {
                text.setX((int) (x * SWFConstants.TWIPS));
            }
            if (hasY) {
                text.setY((int) (y * SWFConstants.TWIPS));
            }

            text.text(chars.indices, chars.advances);
        }
    }


    /**
     *  Description of the Field
     */
    protected boolean hasAlpha;
    /**
     *  Description of the Field
     */
    protected Transform matrix;
    /**
     *  Description of the Field
     */
    protected List rows = new ArrayList();


    /**
     *  Create a Text Symbol which is transformed by the given matrix
     *
     *@param  matrix  if null then an identity transform is assumed
     */
    public Text(Transform matrix) {
        if (matrix == null) {
            matrix = new Transform();
        }
        this.matrix = matrix;
    }


    /**
     *  Access the list of Row instances.
     *
     *@return    The rows value
     */
    public List getRows() {
        return rows;
    }


    /**
     *  Get the transformation matrix applied to the text
     *
     *@return    The transform value
     */
    public Transform getTransform() {
        return matrix;
    }


    /**
     *  Sets the transform attribute of the Text object
     *
     *@param  matrix  The new transform value
     */
    public void setTransform(Transform matrix) {
        this.matrix = matrix;
    }


    /**
     *  Add a contiguous set of characters that have the same font, size, color
     *  and vertical position.
     *
     *@param  chars  the characters to display)
     *@param  x      new X position for text - only valid if hasX is true
     *@param  y      new Y position for text - only valid if hasY is true
     *@param  color  Description of the Parameter
     *@param  hasX   Description of the Parameter
     *@param  hasY   Description of the Parameter
     *@return        the new X position after writing the chars
     */
    public Row row(Font.Chars chars, Color color,
            double x, double y, boolean hasX, boolean hasY) {
        Row row = new Row(chars, color, x, y, hasX, hasY);

        rows.add(row);

        return row;
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
        Font currentFont = null;
        double currentSize = 0.0;
        Color currentColor = null;
        boolean hasAlpha = false;
        double currentX = 0.0;
        double currentY = 0.0;
        double minX = 0.0;
        double minY = 0.0;
        double maxX = 0.0;
        double maxY = 0.0;

        //--make sure that all fonts are defined and figure out the alpha
        for (Iterator it = rows.iterator(); it.hasNext(); ) {
            Object obj = it.next();

            if (obj instanceof Text.Row) {
                Text.Row row = (Text.Row) obj;

                if (row.color != null
                        && (row.color instanceof AlphaColor)) {
                    hasAlpha = true;
                }

                Font font = row.chars.getFont();
                //double size = row.chars.getSize();

                if (currentFont == null || font != currentFont) {
                    font.define(true, movie, definitionWriter);
                }

                currentFont = font;

                if (row.hasX) {
                    currentX = row.x;
                }
                if (row.hasY) {
                    currentY = row.y;
                }

                double leftEdge = currentX - row.chars.getLeftMargin();
                double rightEdge = currentX + row.chars.getTotalAdvance()
                        + row.chars.getRightMargin();
                double topEdge = currentY - row.chars.getAscent();
                double bottomEdge = currentY + row.chars.getDescent();

                if (leftEdge < minX) {
                    minX = leftEdge;
                }
                if (rightEdge > maxX) {
                    maxX = rightEdge;
                }
                if (topEdge < minY) {
                    minY = topEdge;
                }
                if (bottomEdge > maxY) {
                    maxY = bottomEdge;
                }

                currentX += row.chars.getTotalAdvance();
            }
        }

        int id = getNextId(movie);
        Rect bounds = new Rect((int) (minX * SWFConstants.TWIPS),
                (int) (minY * SWFConstants.TWIPS),
                (int) (maxX * SWFConstants.TWIPS),
                (int) (maxY * SWFConstants.TWIPS));

        SWFText text = hasAlpha ?
                definitionWriter.tagDefineText2(id, bounds, matrix) :
                definitionWriter.tagDefineText(id, bounds, matrix);

        currentFont = null;
        currentSize = 0.0;
        currentColor = null;

        for (Iterator it = rows.iterator(); it.hasNext(); ) {
            Object obj = it.next();

            if (obj instanceof Text.Row) {
                Text.Row row = (Text.Row) obj;

                Font font = row.chars.getFont();
                double size = row.chars.getSize();
                Color color = row.color;

                boolean changeFont = currentFont == null ||
                        font != currentFont ||
                        size != currentSize;

                boolean changeColor = currentColor == null ||
                        (color != null && !color.equals(currentColor));

                row.write(text, changeColor, changeFont);

                if (color != null) {
                    currentColor = color;
                }
            }
        }

        text.done();

        return id;
    }
}
