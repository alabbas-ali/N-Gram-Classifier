package pt.tumba.parser.swf;

import java.io.IOException;

/**
 *  An Edit Field Symbol. In order to limit the chars in the field use a
 *  separate font and only load those glyphs that are required.
 *
 *@author     unknown
 *@created    15 de Setembro de 2002
 */
public class EditField extends Symbol {
    /**
     *  Description of the Field
     */
    protected AlphaColor textColor;
    /**
     *  Description of the Field
     */
    protected int alignment;
    /**
     *  Description of the Field
     */
    protected int charLimit;
    /**
     *  Description of the Field
     */
    protected double leftMargin;
    /**
     *  Description of the Field
     */
    protected double rightMargin;
    /**
     *  Description of the Field
     */
    protected double indentation;
    /**
     *  Description of the Field
     */
    protected double lineSpacing;

    /**
     *  Description of the Field
     */
    protected String fieldName;
    /**
     *  Description of the Field
     */
    protected String initialText;
    /**
     *  Description of the Field
     */
    protected Font font;
    /**
     *  Description of the Field
     */
    protected double fontSize;
    /**
     *  Description of the Field
     */
    protected double minX, minY, maxX, maxY;

    /**
     *  Description of the Field
     */
    protected boolean isSelectable = true;
    /**
     *  Description of the Field
     */
    protected boolean hasBorder = true;
    /**
     *  Description of the Field
     */
    protected boolean isHtml;
    /**
     *  Description of the Field
     */
    protected boolean usesSystemFont;
    /**
     *  Description of the Field
     */
    protected boolean hasWordWrap;
    /**
     *  Description of the Field
     */
    protected boolean isMultiline;
    /**
     *  Description of the Field
     */
    protected boolean isPassword;
    /**
     *  Description of the Field
     */
    protected boolean isEditable = true;


    /**
     *  Gets the selectable attribute of the EditField object
     *
     *@return    The selectable value
     */
    public boolean isSelectable() {
        return isSelectable;
    }


    /**
     *  Description of the Method
     *
     *@return    Description of the Return Value
     */
    public boolean hasBorder() {
        return hasBorder;
    }


    /**
     *  Gets the html attribute of the EditField object
     *
     *@return    The html value
     */
    public boolean isHtml() {
        return isHtml;
    }


    /**
     *  Description of the Method
     *
     *@return    Description of the Return Value
     */
    public boolean usesSystemFont() {
        return usesSystemFont;
    }


    /**
     *  Description of the Method
     *
     *@return    Description of the Return Value
     */
    public boolean hasWordWrap() {
        return hasWordWrap;
    }


    /**
     *  Gets the multiline attribute of the EditField object
     *
     *@return    The multiline value
     */
    public boolean isMultiline() {
        return isMultiline;
    }


    /**
     *  Gets the password attribute of the EditField object
     *
     *@return    The password value
     */
    public boolean isPassword() {
        return isPassword;
    }


    /**
     *  Gets the editable attribute of the EditField object
     *
     *@return    The editable value
     */
    public boolean isEditable() {
        return isEditable;
    }


    /**
     *  Sets the properties attribute of the EditField object
     *
     *@param  isSelectable    The new properties value
     *@param  hasBorder       The new properties value
     *@param  isHtml          The new properties value
     *@param  usesSystemFont  The new properties value
     *@param  hasWordWrap     The new properties value
     *@param  isMultiline     The new properties value
     *@param  isPassword      The new properties value
     *@param  isEditable      The new properties value
     */
    public void setProperties(boolean isSelectable, boolean hasBorder,
            boolean isHtml, boolean usesSystemFont,
            boolean hasWordWrap, boolean isMultiline,
            boolean isPassword, boolean isEditable) {
        this.isSelectable = isSelectable;
        this.hasBorder = hasBorder;
        this.isHtml = isHtml;
        this.usesSystemFont = usesSystemFont;
        this.hasWordWrap = hasWordWrap;
        this.isMultiline = isMultiline;
        this.isPassword = isPassword;
        this.isEditable = isEditable;
    }


    /**
     *  Gets the textColor attribute of the EditField object
     *
     *@return    The textColor value
     */
    public AlphaColor getTextColor() {
        return textColor;
    }


    /**
     *  Gets the alignment attribute of the EditField object
     *
     *@return    The alignment value
     */
    public int getAlignment() {
        return alignment;
    }


    /**
     *  Gets the charLimit attribute of the EditField object
     *
     *@return    The charLimit value
     */
    public int getCharLimit() {
        return charLimit;
    }


    /**
     *  Gets the leftMargin attribute of the EditField object
     *
     *@return    The leftMargin value
     */
    public double getLeftMargin() {
        return leftMargin;
    }


    /**
     *  Gets the rightMargin attribute of the EditField object
     *
     *@return    The rightMargin value
     */
    public double getRightMargin() {
        return rightMargin;
    }


    /**
     *  Gets the indentation attribute of the EditField object
     *
     *@return    The indentation value
     */
    public double getIndentation() {
        return indentation;
    }


    /**
     *  Gets the lineSpacing attribute of the EditField object
     *
     *@return    The lineSpacing value
     */
    public double getLineSpacing() {
        return lineSpacing;
    }


    /**
     *  Gets the fieldName attribute of the EditField object
     *
     *@return    The fieldName value
     */
    public String getFieldName() {
        return fieldName;
    }


    /**
     *  Gets the initialText attribute of the EditField object
     *
     *@return    The initialText value
     */
    public String getInitialText() {
        return initialText;
    }


    /**
     *  Gets the font attribute of the EditField object
     *
     *@return    The font value
     */
    public Font getFont() {
        return font;
    }


    /**
     *  Gets the fontSize attribute of the EditField object
     *
     *@return    The fontSize value
     */
    public double getFontSize() {
        return fontSize;
    }


    /**
     *  Gets the minX attribute of the EditField object
     *
     *@return    The minX value
     */
    public double getMinX() {
        return minX;
    }


    /**
     *  Gets the minY attribute of the EditField object
     *
     *@return    The minY value
     */
    public double getMinY() {
        return minY;
    }


    /**
     *  Gets the maxX attribute of the EditField object
     *
     *@return    The maxX value
     */
    public double getMaxX() {
        return maxX;
    }


    /**
     *  Gets the maxY attribute of the EditField object
     *
     *@return    The maxY value
     */
    public double getMaxY() {
        return maxY;
    }


    /**
     *  Sets the textColor attribute of the EditField object
     *
     *@param  color  The new textColor value
     */
    public void setTextColor(AlphaColor color) {
        this.textColor = color;
    }


    /**
     *  Sets the alignment attribute of the EditField object
     *
     *@param  alignment  The new alignment value
     */
    public void setAlignment(int alignment) {
        this.alignment = alignment;
    }


    /**
     *  Sets the charLimit attribute of the EditField object
     *
     *@param  charLimit  The new charLimit value
     */
    public void setCharLimit(int charLimit) {
        this.charLimit = charLimit;
    }


    /**
     *  Sets the leftMargin attribute of the EditField object
     *
     *@param  leftMargin  The new leftMargin value
     */
    public void setLeftMargin(double leftMargin) {
        this.leftMargin = leftMargin;
    }


    /**
     *  Sets the rightMargin attribute of the EditField object
     *
     *@param  rightMargin  The new rightMargin value
     */
    public void setRightMargin(double rightMargin) {
        this.rightMargin = rightMargin;
    }


    /**
     *  Sets the indentation attribute of the EditField object
     *
     *@param  indentation  The new indentation value
     */
    public void setIndentation(double indentation) {
        this.indentation = indentation;
    }


    /**
     *  Sets the lineSpacing attribute of the EditField object
     *
     *@param  lineSpacing  The new lineSpacing value
     */
    public void setLineSpacing(double lineSpacing) {
        this.lineSpacing = lineSpacing;
    }


    /**
     *  Sets the fieldName attribute of the EditField object
     *
     *@param  name  The new fieldName value
     */
    public void setFieldName(String name) {
        this.fieldName = name;
    }


    /**
     *  Sets the initialText attribute of the EditField object
     *
     *@param  text  The new initialText value
     */
    public void setInitialText(String text) {
        this.initialText = text;
    }


    /**
     *  Sets the font attribute of the EditField object
     *
     *@param  font  The new font value
     */
    public void setFont(Font font) {
        this.font = font;
    }


    /**
     *  Sets the fontSize attribute of the EditField object
     *
     *@param  fontSize  The new fontSize value
     */
    public void setFontSize(double fontSize) {
        this.fontSize = fontSize;
    }


    /**
     *  Sets the minX attribute of the EditField object
     *
     *@param  minX  The new minX value
     */
    public void setMinX(double minX) {
        this.minX = minX;
    }


    /**
     *  Sets the minY attribute of the EditField object
     *
     *@param  minY  The new minY value
     */
    public void setMinY(double minY) {
        this.minY = minY;
    }


    /**
     *  Sets the maxX attribute of the EditField object
     *
     *@param  maxX  The new maxX value
     */
    public void setMaxX(double maxX) {
        this.maxX = maxX;
    }


    /**
     *  Sets the maxY attribute of the EditField object
     *
     *@param  maxY  The new maxY value
     */
    public void setMaxY(double maxY) {
        this.maxY = maxY;
    }


    /**
     *  Create an Edit Field with black text and default settings
     *
     *@param  fieldName    may be null
     *@param  initialText  Description of the Parameter
     *@param  font         Description of the Parameter
     *@param  fontSize     Description of the Parameter
     *@param  minX         Description of the Parameter
     *@param  minY         Description of the Parameter
     *@param  maxX         Description of the Parameter
     *@param  maxY         Description of the Parameter
     */
    public EditField(String fieldName, String initialText,
            Font font, double fontSize,
            double minX, double minY, double maxX, double maxY) {
        this.fieldName = fieldName;
        this.initialText = initialText;
        this.font = font;
        this.fontSize = fontSize;

        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;

        this.textColor = new AlphaColor(0, 0, 0, 255);
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

        //--Make sure that the font is defined.
        int fontId = font.define(false, movie, definitionWriter);

        Rect bounds = new Rect((int) (minX * SWFConstants.TWIPS),
                (int) (minY * SWFConstants.TWIPS),
                (int) (maxX * SWFConstants.TWIPS),
                (int) (maxY * SWFConstants.TWIPS));

        int flags = 0;

        if (!isSelectable) {
            flags |= SWFConstants.TEXTFIELD_NO_SELECTION;
        }
        if (hasBorder) {
            flags |= SWFConstants.TEXTFIELD_DRAW_BORDER;
        }
        if (isHtml) {
            flags |= SWFConstants.TEXTFIELD_HTML;
        }
        if (!usesSystemFont) {
            flags |= SWFConstants.TEXTFIELD_FONT_GLYPHS;
        }
        if (hasWordWrap) {
            flags |= SWFConstants.TEXTFIELD_WORD_WRAP;
        }
        if (isMultiline) {
            flags |= SWFConstants.TEXTFIELD_IS_MULTILINE;
        }
        if (isPassword) {
            flags |= SWFConstants.TEXTFIELD_IS_PASSWORD;
        }
        if (!isEditable) {
            flags |= SWFConstants.TEXTFIELD_DISABLE_EDIT;
        }

        if (initialText != null && initialText.length() > 0) {
            flags |= SWFConstants.TEXTFIELD_HAS_TEXT;
        }

        if (charLimit > 0) {
            flags |= SWFConstants.TEXTFIELD_LIMIT_CHARS;
        }

        //--define the edit field
        definitionWriter.tagDefineTextField(
                id, fieldName, initialText, bounds, flags,
                textColor, alignment, fontId,
                (int) (fontSize * SWFConstants.TWIPS),
                charLimit,
                (int) (leftMargin * SWFConstants.TWIPS),
                (int) (rightMargin * SWFConstants.TWIPS),
                (int) (indentation * SWFConstants.TWIPS),
                (int) (lineSpacing * SWFConstants.TWIPS));

        return id;
    }

}
