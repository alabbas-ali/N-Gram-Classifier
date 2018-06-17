package pt.tumba.parser.swf;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 *  An implementation of the SWFTagTypes interface that builds a Movie object
 *
 *@author     unknown
 *@created    15 de Setembro de 2002
 */
public class MovieBuilder implements SWFTagTypes {
    /**
     *  Description of the Field
     */
    protected Movie movie;
    /**
     *  Description of the Field
     */
    protected MovieClip clip;
    /**
     *  Description of the Field
     */
    protected boolean newMovie = false;
    /**
     *  Description of the Field
     */
    protected Frame frame;
    /**
     *  Description of the Field
     */
    protected Map symbols = new HashMap();
    /**
     *  Description of the Field
     */
    protected TimeLine timeline;
    /**
     *  Description of the Field
     */
    protected Map instances = new HashMap();


    /**
     *  Build a new Movie
     */
    public MovieBuilder() {
        movie = new Movie();
        newMovie = true;
        timeline = movie;
    }


    /**
     *  Append to an existing movie (do not change size,rate,color,version etc)
     *
     *@param  movie  Description of the Parameter
     */
    public MovieBuilder(Movie movie) {
        this.movie = movie;
        newMovie = false;
        timeline = movie;
    }


    /**
     *  Build the timeline of a MovieClip
     *
     *@param  parent  Description of the Parameter
     *@param  clip    Description of the Parameter
     */
    protected MovieBuilder(MovieBuilder parent, MovieClip clip) {
        this.movie = parent.movie;
        this.symbols = parent.symbols;
        this.clip = clip;
        newMovie = false;
        timeline = clip;
    }


    /**
     *  Gets the movie attribute of the MovieBuilder object
     *
     *@return    The movie value
     */
    public Movie getMovie() {
        return movie;
    }


    /**
     *  Get the defined symbols - keyed by Integer( symbolId )
     *
     *@return    The definedSymbols value
     */
    public Map getDefinedSymbols() {
        return symbols;
    }


    /**
     *  Gets the symbol attribute of the MovieBuilder object
     *
     *@param  id  Description of the Parameter
     *@return     The symbol value
     */
    protected Symbol getSymbol(int id) {
        return (Symbol) symbols.get(new Integer(id));
    }


    /**
     *  Description of the Method
     *
     *@param  id  Description of the Parameter
     *@param  s   Description of the Parameter
     */
    protected void saveSymbol(int id, Symbol s) {
        symbols.put(new Integer(id), s);
    }


    /**
     *  Gets the instance attribute of the MovieBuilder object
     *
     *@param  depth  Description of the Parameter
     *@return        The instance value
     */
    protected Instance getInstance(int depth) {
        return (Instance) instances.get(new Integer(depth));
    }


    /**
     *  Description of the Method
     *
     *@param  depth  Description of the Parameter
     *@param  inst   Description of the Parameter
     */
    protected void saveInstance(int depth, Instance inst) {
        instances.put(new Integer(depth), inst);
    }


    /**
     *  SWFTags interface
     *
     *@param  tagType          Description of the Parameter
     *@param  longTag          Description of the Parameter
     *@param  contents         Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tag(int tagType, boolean longTag, byte[] contents)
             throws IOException {
        //ignore unknown tags
    }


    /**
     *  SWFHeader interface.
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
        if (newMovie) {
            movie.setVersion(version);
            movie.setWidth(twipsWidth / SWFConstants.TWIPS);
            movie.setHeight(twipsHeight / SWFConstants.TWIPS);
            movie.setFrameRate(frameRate);
        }
    }


    /**
     *  SWFTagTypes interface
     *
     *@exception  IOException  Description of the Exception
     */
    public void tagEnd() throws IOException {
        //nothing to do
    }


    /**
     *  SWFTagTypes interface
     *
     *@exception  IOException  Description of the Exception
     */
    public void tagShowFrame() throws IOException {
        //--complete the current frame
        if (frame == null) {
            timeline.appendFrame();
        } else {
            frame = null;
        }
    }


    /**
     *  Description of the Method
     *
     *@return    Description of the Return Value
     */
    protected Frame currentFrame() {
        if (frame == null) {
            frame = timeline.appendFrame();
        }
        return frame;
    }


    //--Tags yet to be implemented...
    /**
     *  Description of the Method
     *
     *@param  id               Description of the Parameter
     *@param  format           Description of the Parameter
     *@param  frequency        Description of the Parameter
     *@param  bits16           Description of the Parameter
     *@param  stereo           Description of the Parameter
     *@param  sampleCount      Description of the Parameter
     *@param  soundData        Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagDefineSound(int id, int format, int frequency,
            boolean bits16, boolean stereo,
            int sampleCount, byte[] soundData)
             throws IOException { }


    /**
     *  Description of the Method
     *
     *@param  buttonId           Description of the Parameter
     *@param  rollOverSoundId    Description of the Parameter
     *@param  rollOverSoundInfo  Description of the Parameter
     *@param  rollOutSoundId     Description of the Parameter
     *@param  rollOutSoundInfo   Description of the Parameter
     *@param  pressSoundId       Description of the Parameter
     *@param  pressSoundInfo     Description of the Parameter
     *@param  releaseSoundId     Description of the Parameter
     *@param  releaseSoundInfo   Description of the Parameter
     *@exception  IOException    Description of the Exception
     */
    public void tagDefineButtonSound(int buttonId,
            int rollOverSoundId, SoundInfo rollOverSoundInfo,
            int rollOutSoundId, SoundInfo rollOutSoundInfo,
            int pressSoundId, SoundInfo pressSoundInfo,
            int releaseSoundId, SoundInfo releaseSoundInfo)
             throws IOException { }


    /**
     *  Description of the Method
     *
     *@param  soundId          Description of the Parameter
     *@param  info             Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagStartSound(int soundId, SoundInfo info) throws IOException { }


    /**
     *  Description of the Method
     *
     *@param  playbackFrequency   Description of the Parameter
     *@param  playback16bit       Description of the Parameter
     *@param  playbackStereo      Description of the Parameter
     *@param  streamFormat        Description of the Parameter
     *@param  streamFrequency     Description of the Parameter
     *@param  stream16bit         Description of the Parameter
     *@param  streamStereo        Description of the Parameter
     *@param  averageSampleCount  Description of the Parameter
     *@exception  IOException     Description of the Exception
     */
    public void tagSoundStreamHead(
            int playbackFrequency, boolean playback16bit, boolean playbackStereo,
            int streamFormat, int streamFrequency, boolean stream16bit, boolean streamStereo,
            int averageSampleCount) throws IOException { }


    /**
     *  Description of the Method
     *
     *@param  playbackFrequency   Description of the Parameter
     *@param  playback16bit       Description of the Parameter
     *@param  playbackStereo      Description of the Parameter
     *@param  streamFormat        Description of the Parameter
     *@param  streamFrequency     Description of the Parameter
     *@param  stream16bit         Description of the Parameter
     *@param  streamStereo        Description of the Parameter
     *@param  averageSampleCount  Description of the Parameter
     *@exception  IOException     Description of the Exception
     */
    public void tagSoundStreamHead2(
            int playbackFrequency, boolean playback16bit, boolean playbackStereo,
            int streamFormat, int streamFrequency, boolean stream16bit, boolean streamStereo,
            int averageSampleCount) throws IOException { }


    /**
     *  Description of the Method
     *
     *@param  soundData        Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagSoundStreamBlock(byte[] soundData) throws IOException { }


    /**
     *  Description of the Method
     *
     *@param  serialNumber     Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagSerialNumber(String serialNumber) throws IOException { }


    /**
     *  Description of the Method
     *
     *@param  data             Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagGenerator(byte[] data) throws IOException { }


    /**
     *  Description of the Method
     *
     *@param  data             Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagGeneratorText(byte[] data) throws IOException { }


    /**
     *  Description of the Method
     *
     *@param  data             Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagGeneratorCommand(byte[] data) throws IOException { }


    /**
     *  Description of the Method
     *
     *@param  data             Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagGeneratorFont(byte[] data) throws IOException { }


    /**
     *  Description of the Method
     *
     *@param  data             Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagNameCharacter(byte[] data) throws IOException { }


    /**
     *  Description of the Method
     *
     *@param  id               Description of the Parameter
     *@param  imageData        Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagDefineBits(int id, byte[] imageData) throws IOException { }


    /**
     *  Description of the Method
     *
     *@param  jpegEncodingData  Description of the Parameter
     *@exception  IOException   Description of the Exception
     */
    public void tagJPEGTables(byte[] jpegEncodingData) throws IOException { }


    /**
     *  Description of the Method
     *
     *@param  id               Description of the Parameter
     *@param  imageData        Description of the Parameter
     *@param  alphaData        Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagDefineBitsJPEG3(int id, byte[] imageData, byte[] alphaData) throws IOException { }


    /**
     *  SWFTagTypes interface
     *
     *@return                  Description of the Return Value
     *@exception  IOException  Description of the Exception
     */
    public SWFActions tagDoAction() throws IOException {
        Actions acts = currentFrame().actions(movie.getVersion());

        return acts;
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  id               Description of the Parameter
     *@param  outline          Description of the Parameter
     *@return                  Description of the Return Value
     *@exception  IOException  Description of the Exception
     */
    public SWFShape tagDefineShape(int id, Rect outline) throws IOException {
        Shape s = new Shape();
        s.setBoundingRectangle(((double) outline.getMinX()) / SWFConstants.TWIPS,
                ((double) outline.getMinY()) / SWFConstants.TWIPS,
                ((double) outline.getMaxX()) / SWFConstants.TWIPS,
                ((double) outline.getMaxY()) / SWFConstants.TWIPS);

        saveSymbol(id, s);

        return new ShapeBuilder(s);
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  id               Description of the Parameter
     *@param  outline          Description of the Parameter
     *@return                  Description of the Return Value
     *@exception  IOException  Description of the Exception
     */
    public SWFShape tagDefineShape2(int id, Rect outline) throws IOException {
        Shape s = new Shape();
        s.setBoundingRectangle(((double) outline.getMinX()) / SWFConstants.TWIPS,
                ((double) outline.getMinY()) / SWFConstants.TWIPS,
                ((double) outline.getMaxX()) / SWFConstants.TWIPS,
                ((double) outline.getMaxY()) / SWFConstants.TWIPS);

        saveSymbol(id, s);

        return new ShapeBuilder(s);
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  id               Description of the Parameter
     *@param  outline          Description of the Parameter
     *@return                  Description of the Return Value
     *@exception  IOException  Description of the Exception
     */
    public SWFShape tagDefineShape3(int id, Rect outline) throws IOException {
        Shape s = new Shape();
        s.setBoundingRectangle(((double) outline.getMinX()) / SWFConstants.TWIPS,
                ((double) outline.getMinY()) / SWFConstants.TWIPS,
                ((double) outline.getMaxX()) / SWFConstants.TWIPS,
                ((double) outline.getMaxY()) / SWFConstants.TWIPS);

        saveSymbol(id, s);

        return new ShapeBuilder(s);
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  charId           Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagFreeCharacter(int charId) throws IOException {
        //nothing
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  charId           Description of the Parameter
     *@param  depth            Description of the Parameter
     *@param  matrix           Description of the Parameter
     *@param  cxform           Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagPlaceObject(int charId, int depth,
            Matrix matrix, AlphaTransform cxform)
             throws IOException {
        Symbol s = getSymbol(charId);
        if (s == null) {
            return;
        }

        timeline.setAvailableDepth(depth);
        Instance inst = currentFrame().placeSymbol(s, new Transform(matrix), cxform);
        saveInstance(depth, inst);
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  isMove           Description of the Parameter
     *@param  clipDepth        Description of the Parameter
     *@param  depth            Description of the Parameter
     *@param  charId           Description of the Parameter
     *@param  matrix           Description of the Parameter
     *@param  cxform           Description of the Parameter
     *@param  ratio            Description of the Parameter
     *@param  name             Description of the Parameter
     *@param  clipActionFlags  Description of the Parameter
     *@return                  Description of the Return Value
     *@exception  IOException  Description of the Exception
     */
    public SWFActions tagPlaceObject2(boolean isMove,
            int clipDepth,
            int depth,
            int charId,
            Matrix matrix,
            AlphaTransform cxform,
            int ratio,
            String name,
            int clipActionFlags)
             throws IOException {
        Instance inst = null;

        if (isMove && charId <= 0) {
            inst = getInstance(depth);
            if (inst == null) {
                System.out.println("Failed to find Instance at depth " + depth);
                return null;
            }

            currentFrame().alter(inst, new Transform(matrix), cxform, ratio);
        } else {
            Symbol s = getSymbol(charId);

            if (s == null) {
                System.out.println("Failed to find Symbol with id " + charId);
                return null;
            }

            if (name != null) {
                Frame frame = currentFrame();

                if (isMove) {
                    frame.replaceMovieClip(s, depth, new Transform(matrix),
                            cxform, name, null);
                } else {
                    timeline.setAvailableDepth(depth);

                    frame.placeMovieClip(s, new Transform(matrix),
                            cxform, name, null);
                }

                saveInstance(depth, inst);
            } else if (clipActionFlags != 0) {
                return new ClipActionBuilder(s, matrix, cxform, depth, name,
                        movie.getVersion(), isMove);
            } else {
                Frame frame = currentFrame();

                if (isMove) {
                    inst = frame.replaceSymbol(s, depth, new Transform(matrix),
                            cxform, ratio, clipDepth);
                } else {
                    timeline.setAvailableDepth(depth);

                    inst = frame.placeSymbol(s, new Transform(matrix),
                            cxform, ratio, clipDepth);
                }

                saveInstance(depth, inst);
            }
        }

        return null;
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  charId           Description of the Parameter
     *@param  depth            Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagRemoveObject(int charId, int depth) throws IOException {
        Instance inst = getInstance(depth);
        if (inst == null) {
            return;
        }

        currentFrame().remove(inst);
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  depth            Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagRemoveObject2(int depth) throws IOException {
        Instance inst = getInstance(depth);
        if (inst == null) {
            return;
        }

        currentFrame().remove(inst);
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  color            Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagSetBackgroundColor(Color color) throws IOException {
        if (newMovie) {
            movie.setBackColor(color);
        }
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  label            Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagFrameLabel(String label) throws IOException {
        currentFrame().setLabel(label);
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  id               Description of the Parameter
     *@return                  Description of the Return Value
     *@exception  IOException  Description of the Exception
     */
    public SWFTagTypes tagDefineSprite(int id) throws IOException {
        MovieClip clip = new MovieClip();
        saveSymbol(id, clip);

        return new MovieBuilder(this, clip);
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  password         Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagProtect(byte[] password) throws IOException {
        if (newMovie) {
            movie.protect(true);
        }
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  password         Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagEnableDebug(byte[] password) throws IOException {
        //not implemented
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  id               Description of the Parameter
     *@param  numGlyphs        Description of the Parameter
     *@return                  Description of the Return Value
     *@exception  IOException  Description of the Exception
     */
    public SWFVectors tagDefineFont(int id, int numGlyphs) throws IOException {
        FontDefinition fontDef = new FontDefinition();
        Font font = new Font(fontDef);

        saveSymbol(id, font);

        return new GlyphBuilder(fontDef, font, numGlyphs);
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  fontId           Description of the Parameter
     *@param  fontName         Description of the Parameter
     *@param  flags            Description of the Parameter
     *@param  codes            Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagDefineFontInfo(int fontId, String fontName, int flags, int[] codes)
             throws IOException {
        Symbol s = getSymbol(fontId);
        if (s == null || !(s instanceof Font)) {
            return;
        }

        Font font = (Font) s;
        FontDefinition def = font.getDefinition();

        def.setName(fontName);

        boolean isUnicode = ((flags & SWFConstants.FONT_UNICODE) != 0);
        boolean isShiftJIS = ((flags & SWFConstants.FONT_SHIFTJIS) != 0);
        boolean isAnsi = ((flags & SWFConstants.FONT_ANSI) != 0);
        boolean isItalic = ((flags & SWFConstants.FONT_ITALIC) != 0);
        boolean isBold = ((flags & SWFConstants.FONT_BOLD) != 0);

        def.setFontFlags(isUnicode, isShiftJIS, isAnsi, isItalic, isBold, false);

        //--set the glyph codes
        List glyphs = font.getGlyphList();
        int glyphCount = glyphs.size();

        for (int i = 0; i < codes.length && i < glyphCount; i++) {
            int code = codes[i];
            font.setCode(i, code);
        }
    }


    /**
     *  SWFTagTypes interface
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
        boolean hasMetrics = ((flags & SWFConstants.FONT2_HAS_LAYOUT) != 0);
        boolean isShiftJIS = ((flags & SWFConstants.FONT2_SHIFTJIS) != 0);
        boolean isUnicode = ((flags & SWFConstants.FONT2_UNICODE) != 0);
        boolean isAnsi = ((flags & SWFConstants.FONT2_ANSI) != 0);
        boolean isItalic = ((flags & SWFConstants.FONT2_ITALIC) != 0);
        boolean isBold = ((flags & SWFConstants.FONT2_BOLD) != 0);

        FontDefinition fontDef = new FontDefinition(
                name,
                ((double) ascent) / SWFConstants.TWIPS,
                ((double) descent) / SWFConstants.TWIPS,
                ((double) leading) / SWFConstants.TWIPS,
                isUnicode, isShiftJIS, isAnsi, isItalic,
                isBold, hasMetrics);

        Font font = new Font(fontDef);
        saveSymbol(id, font);

        //--save the kerning info
        if (hasMetrics && kernCodes1 != null) {
            List kerns = fontDef.getKerningPairList();

            for (int i = 0; i < kernCodes1.length; i++) {
                FontDefinition.KerningPair pair =
                        new FontDefinition.KerningPair(
                        kernCodes1[i],
                        kernCodes2[i],
                        ((double) kernAdjustments[i]) / SWFConstants.TWIPS);

                kerns.add(pair);
            }
        }

        return new GlyphBuilder(fontDef, font, codes, advances, bounds);
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  fieldId          Description of the Parameter
     *@param  fieldName        Description of the Parameter
     *@param  initialText      Description of the Parameter
     *@param  boundary         Description of the Parameter
     *@param  flags            Description of the Parameter
     *@param  textColor        Description of the Parameter
     *@param  alignment        Description of the Parameter
     *@param  fontId           Description of the Parameter
     *@param  fontSize         Description of the Parameter
     *@param  charLimit        Description of the Parameter
     *@param  leftMargin       Description of the Parameter
     *@param  rightMargin      Description of the Parameter
     *@param  indentation      Description of the Parameter
     *@param  lineSpacing      Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagDefineTextField(int fieldId, String fieldName,
            String initialText, Rect boundary, int flags,
            AlphaColor textColor, int alignment, int fontId, int fontSize,
            int charLimit, int leftMargin, int rightMargin, int indentation,
            int lineSpacing)
             throws IOException {
        Symbol f = getSymbol(fontId);
        if (f == null || !(f instanceof Font)) {
            return;
        }

        Font font = (Font) f;

        EditField field = new EditField(fieldName, initialText, font,
                ((double) fontSize) / SWFConstants.TWIPS,
                ((double) boundary.getMinX()) / SWFConstants.TWIPS,
                ((double) boundary.getMinY()) / SWFConstants.TWIPS,
                ((double) boundary.getMaxX()) / SWFConstants.TWIPS,
                ((double) boundary.getMaxY()) / SWFConstants.TWIPS);

        field.setTextColor(textColor);
        field.setAlignment(alignment);
        field.setCharLimit(charLimit);
        field.setLeftMargin(((double) leftMargin) / SWFConstants.TWIPS);
        field.setRightMargin(((double) rightMargin) / SWFConstants.TWIPS);
        field.setIndentation(((double) indentation) / SWFConstants.TWIPS);
        field.setLineSpacing(((double) lineSpacing) / SWFConstants.TWIPS);

        boolean isSelectable = ((flags & SWFConstants.TEXTFIELD_NO_SELECTION) == 0);
        boolean hasBorder = ((flags & SWFConstants.TEXTFIELD_DRAW_BORDER) != 0);
        boolean isHtml = ((flags & SWFConstants.TEXTFIELD_HTML) != 0);
        boolean usesSystemFont = ((flags & SWFConstants.TEXTFIELD_FONT_GLYPHS) == 0);
        boolean hasWordWrap = ((flags & SWFConstants.TEXTFIELD_WORD_WRAP) != 0);
        boolean isMultiline = ((flags & SWFConstants.TEXTFIELD_IS_MULTILINE) != 0);
        boolean isPassword = ((flags & SWFConstants.TEXTFIELD_IS_PASSWORD) != 0);
        boolean isEditable = ((flags & SWFConstants.TEXTFIELD_DISABLE_EDIT) == 0);

        field.setProperties(isSelectable, hasBorder, isHtml, usesSystemFont,
                hasWordWrap, isMultiline, isPassword, isEditable);

        saveSymbol(fieldId, field);
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  id               Description of the Parameter
     *@param  bounds           Description of the Parameter
     *@param  matrix           Description of the Parameter
     *@return                  Description of the Return Value
     *@exception  IOException  Description of the Exception
     */
    public SWFText tagDefineText(int id, Rect bounds, Matrix matrix)
             throws IOException {
        Text text = new Text(new Transform(matrix));
        saveSymbol(id, text);

        return new TextBuilder(text);
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  id               Description of the Parameter
     *@param  bounds           Description of the Parameter
     *@param  matrix           Description of the Parameter
     *@return                  Description of the Return Value
     *@exception  IOException  Description of the Exception
     */
    public SWFText tagDefineText2(int id, Rect bounds, Matrix matrix) throws IOException {
        Text text = new Text(new Transform(matrix));
        saveSymbol(id, text);

        return new TextBuilder(text);
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  id               Description of the Parameter
     *@param  buttonRecords    Description of the Parameter
     *@return                  Description of the Return Value
     *@exception  IOException  Description of the Exception
     */
    public SWFActions tagDefineButton(int id, List buttonRecords)
             throws IOException {
        Button but = new Button(false);
        saveSymbol(id, but);

        for (Iterator e = buttonRecords.iterator(); e.hasNext(); ) {
            ButtonRecord rec = (ButtonRecord) e.next();

            Symbol s = getSymbol(rec.getCharId());
            if (s == null) {
                continue;
            }

            int flags = rec.getFlags();
            boolean hit = ((flags & ButtonRecord.BUTTON_HITTEST) != 0);
            boolean up = ((flags & ButtonRecord.BUTTON_UP) != 0);
            boolean over = ((flags & ButtonRecord.BUTTON_OVER) != 0);
            boolean down = ((flags & ButtonRecord.BUTTON_DOWN) != 0);

            but.addLayer(s, new Transform(rec.getMatrix()), null,
                    rec.getLayer(),
                    hit, up, down, over);
        }

        return new ButtonActionBuilder(but, movie.getVersion());
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  buttonId         Description of the Parameter
     *@param  transform        Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagButtonCXForm(int buttonId, ColorTransform transform)
             throws IOException {
        Symbol s = getSymbol(buttonId);
        if (s == null || !(s instanceof Button)) {
            return;
        }

        Button but = (Button) s;

        List layers = but.getButtonLayers();

        //apply the transform to all the layers of the button
        for (Iterator it = layers.iterator(); it.hasNext(); ) {
            Button.Layer layer = (Button.Layer) it.next();

            layer.setColoring(new AlphaTransform(transform));
        }
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  id               Description of the Parameter
     *@param  trackAsMenu      Description of the Parameter
     *@param  buttonRecord2s   Description of the Parameter
     *@return                  Description of the Return Value
     *@exception  IOException  Description of the Exception
     */
    public SWFActions tagDefineButton2(int id,
            boolean trackAsMenu,
            List buttonRecord2s)
             throws IOException {
        Button but = new Button(trackAsMenu);
        saveSymbol(id, but);

        for (Iterator e = buttonRecord2s.iterator(); e.hasNext(); ) {
            ButtonRecord2 rec = (ButtonRecord2) e.next();

            Symbol s = getSymbol(rec.getCharId());
            if (s == null) {
                continue;
            }

            int flags = rec.getFlags();
            boolean hit = ((flags & ButtonRecord.BUTTON_HITTEST) != 0);
            boolean up = ((flags & ButtonRecord.BUTTON_UP) != 0);
            boolean over = ((flags & ButtonRecord.BUTTON_OVER) != 0);
            boolean down = ((flags & ButtonRecord.BUTTON_DOWN) != 0);

            but.addLayer(s, new Transform(rec.getMatrix()), rec.getTransform(),
                    rec.getLayer(),
                    hit, up, down, over);
        }

        return new ButtonActionBuilder(but, movie.getVersion());
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  names            Description of the Parameter
     *@param  ids              Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagExport(String[] names, int[] ids) throws IOException {
        Symbol[] symbols = new Symbol[ids.length];

        for (int i = 0; i < ids.length; i++) {
            symbols[i] = getSymbol(ids[i]);
        }

        movie.exportSymbols(names, symbols);
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  movieName        Description of the Parameter
     *@param  names            Description of the Parameter
     *@param  ids              Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagImport(String movieName, String[] names, int[] ids)
             throws IOException {
        movie.importSymbols(movieName, names);
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  id               Description of the Parameter
     *@param  filename         Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagDefineQuickTimeMovie(int id, String filename) throws IOException {
        saveSymbol(id, new QTMovie(filename));
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  id               Description of the Parameter
     *@param  data             Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagDefineBitsJPEG2(int id, byte[] data) throws IOException {
        saveSymbol(id, new Image.JPEG(data));
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  id               Description of the Parameter
     *@param  jpegImage        Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagDefineBitsJPEG2(int id, InputStream jpegImage) throws IOException {
        saveSymbol(id, new Image.JPEG(jpegImage));
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  id               Description of the Parameter
     *@param  format           Description of the Parameter
     *@param  width            Description of the Parameter
     *@param  height           Description of the Parameter
     *@param  colors           Description of the Parameter
     *@param  imageData        Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagDefineBitsLossless(int id, int format, int width, int height,
            Color[] colors, byte[] imageData)
             throws IOException {
        saveSymbol(id, new Image.Lossless(colors, imageData,
                ((double) width) / SWFConstants.TWIPS,
                ((double) height) / SWFConstants.TWIPS,
                false, format));
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  id               Description of the Parameter
     *@param  format           Description of the Parameter
     *@param  width            Description of the Parameter
     *@param  height           Description of the Parameter
     *@param  colors           Description of the Parameter
     *@param  imageData        Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagDefineBitsLossless2(int id, int format, int width, int height,
            Color[] colors, byte[] imageData)
             throws IOException {
        saveSymbol(id, new Image.Lossless(colors, imageData,
                ((double) width) / SWFConstants.TWIPS,
                ((double) height) / SWFConstants.TWIPS,
                true, format));
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  id               Description of the Parameter
     *@param  startBounds      Description of the Parameter
     *@param  endBounds        Description of the Parameter
     *@return                  Description of the Return Value
     *@exception  IOException  Description of the Exception
     */
    public SWFShape tagDefineMorphShape(int id, Rect startBounds, Rect endBounds)
             throws IOException {
        return new MorphShapeBuilder(id, startBounds, endBounds);
    }


    /**
     *  A SWFActions implementation that breaks multiple action arrays into
     *  separate Actions objects
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected static class ActionsBuilder extends SWFActionsImpl {
        /**
         *  Description of the Field
         */
        protected int version;
        /**
         *  Description of the Field
         */
        protected List actions = new Vector();


        /**
         *  Constructor for the ActionsBuilder object
         *
         *@param  flashVersion  Description of the Parameter
         */
        protected ActionsBuilder(int flashVersion) {
            super(null);
            version = flashVersion;
        }


        /**
         *  Gets the actions attribute of the ActionsBuilder object
         *
         *@return    The actions value
         */
        public Actions[] getActions() {
            Actions[] a = new Actions[actions.size()];
            actions.toArray(a);
            return a;
        }


        /**
         *  Description of the Method
         *
         *@param  conditions  Description of the Parameter
         */
        public void start(int conditions) {
            acts = new Actions(conditions, version);
            actions.add(acts);
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ButtonActionBuilder extends MovieBuilder.ActionsBuilder {
        /**
         *  Description of the Field
         */
        protected Button but;


        /**
         *  Constructor for the ButtonActionBuilder object
         *
         *@param  b             Description of the Parameter
         *@param  flashVersion  Description of the Parameter
         */
        protected ButtonActionBuilder(Button b, int flashVersion) {
            super(flashVersion);
            but = b;
        }


        /**
         *  Description of the Method
         *
         *@param  conditions  Description of the Parameter
         */
        public void start(int conditions2) {
            int conditions = conditions2;
            //--DefineButton has implicit conditions..
            if (conditions == 0) {
                conditions = SWFConstants.BUTTON2_OVERDOWN2OVERUP;
            }

            acts = but.addActions(conditions, version);
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ClipActionBuilder extends MovieBuilder.ActionsBuilder {
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
        protected String name;
        /**
         *  Description of the Field
         */
        protected int depth;
        /**
         *  Description of the Field
         */
        protected boolean isMove;


        /**
         *  Constructor for the ClipActionBuilder object
         *
         *@param  symbol   Description of the Parameter
         *@param  matrix   Description of the Parameter
         *@param  cxform   Description of the Parameter
         *@param  depth    Description of the Parameter
         *@param  name     Description of the Parameter
         *@param  version  Description of the Parameter
         *@param  isMove   Description of the Parameter
         */
        protected ClipActionBuilder(Symbol symbol, Matrix matrix,
                AlphaTransform cxform, int depth,
                String name, int version, boolean isMove) {
            super(version);
            this.symbol = symbol;
            this.matrix = (matrix != null) ? new Transform(matrix) : null;
            this.cxform = cxform;
            this.name = name;
            this.depth = depth;
            this.isMove = isMove;
        }


        /**
         *  All actions are done - place the instance
         *
         *@exception  IOException  Description of the Exception
         */
        public void done() throws IOException {
            Frame frame = currentFrame();

            Instance inst = null;

            if (isMove) {
                frame.replaceMovieClip(symbol, depth, matrix, cxform, name, getActions());
            } else {
                timeline.setAvailableDepth(depth);

                frame.placeMovieClip(symbol, matrix, cxform, name, getActions());
            }

            saveInstance(depth, inst);
        }
    }


    /**
     *  SWFShape implementation that builds a Shape
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ShapeBuilder implements SWFShape {
        /**
         *  Description of the Field
         */
        protected Shape s;
        /**
         *  Description of the Field
         */
        protected int currx, curry;


        /**
         *  Constructor for the ShapeBuilder object
         *
         *@param  s  Description of the Parameter
         */
        protected ShapeBuilder(Shape s) {
            this.s = s;
        }


        /**
         *  Description of the Method
         */
        public void done() {
            //nothing
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

            s.line(((double) currx) / SWFConstants.TWIPS,
                    ((double) curry) / SWFConstants.TWIPS);
        }


        /**
         *  Description of the Method
         *
         *@param  cx  Description of the Parameter
         *@param  cy  Description of the Parameter
         *@param  dx  Description of the Parameter
         *@param  dy  Description of the Parameter
         */
        public void curve(int cx2, int cy2, int dx, int dy) {
            int cx = cx2+currx;
            int cy = cy2+curry;

            currx = cx + dx;
            curry = cy + dy;

            s.curve(((double) currx) / SWFConstants.TWIPS,
                    ((double) curry) / SWFConstants.TWIPS,
                    ((double) cx) / SWFConstants.TWIPS,
                    ((double) cy) / SWFConstants.TWIPS);
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

            s.move(((double) currx) / SWFConstants.TWIPS,
                    ((double) curry) / SWFConstants.TWIPS);
        }


        /**
         *  Sets the fillStyle0 attribute of the ShapeBuilder object
         *
         *@param  styleIndex  The new fillStyle0 value
         */
        public void setFillStyle0(int styleIndex) {
            s.setLeftFillStyle(styleIndex);
        }


        /**
         *  Sets the fillStyle1 attribute of the ShapeBuilder object
         *
         *@param  styleIndex  The new fillStyle1 value
         */
        public void setFillStyle1(int styleIndex) {
            s.setRightFillStyle(styleIndex);
        }


        /**
         *  Sets the lineStyle attribute of the ShapeBuilder object
         *
         *@param  styleIndex  The new lineStyle value
         */
        public void setLineStyle(int styleIndex) {
            s.setLineStyle(styleIndex);
        }


        /**
         *  Description of the Method
         *
         *@param  color  Description of the Parameter
         */
        public void defineFillStyle(Color color) {
            s.defineFillStyle(color);
        }


        /**
         *  Description of the Method
         *
         *@param  matrix  Description of the Parameter
         *@param  ratios  Description of the Parameter
         *@param  colors  Description of the Parameter
         *@param  radial  Description of the Parameter
         */
        public void defineFillStyle(Matrix matrix, int[] ratios,
                Color[] colors, boolean radial) {
            s.defineFillStyle(colors, ratios, new Transform(matrix), radial);
        }


        /**
         *  Description of the Method
         *
         *@param  bitmapId  Description of the Parameter
         *@param  matrix    Description of the Parameter
         *@param  clipped   Description of the Parameter
         */
        public void defineFillStyle(int bitmapId, Matrix matrix, boolean clipped) {
            Symbol image = getSymbol(bitmapId);
            s.defineFillStyle(image, new Transform(matrix), clipped);
        }


        /**
         *  Description of the Method
         *
         *@param  width  Description of the Parameter
         *@param  color  Description of the Parameter
         */
        public void defineLineStyle(int width, Color color) {
            s.defineLineStyle(((double) width) / SWFConstants.TWIPS, color);
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class MorphShapeBuilder extends MovieBuilder.ShapeBuilder {
        /**
         *  Description of the Field
         */
        protected int id;
        /**
         *  Description of the Field
         */
        protected Rect startBounds;
        /**
         *  Description of the Field
         */
        protected Rect endBounds;
        /**
         *  Description of the Field
         */
        protected Shape shape1;


        /**
         *  Constructor for the MorphShapeBuilder object
         *
         *@param  id           Description of the Parameter
         *@param  startBounds  Description of the Parameter
         *@param  endBounds    Description of the Parameter
         */
        protected MorphShapeBuilder(int id, Rect startBounds, Rect endBounds) {
            super(new Shape());
            this.id = id;
            this.startBounds = startBounds;
            this.endBounds = endBounds;
        }


        /**
         *  Description of the Method
         */
        public void done() {
            if (shape1 == null) {
                shape1 = s;
                s = new Shape();
                return;
            }

            Shape shape2 = s;

            MorphShape morph = new MorphShape(shape1, shape2);
            saveSymbol(id, morph);
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class GlyphBuilder extends MovieBuilder.ShapeBuilder {
        /**
         *  Description of the Field
         */
        protected FontDefinition fontDef;
        /**
         *  Description of the Field
         */
        protected Font font;
        /**
         *  Description of the Field
         */
        protected int count;
        /**
         *  Description of the Field
         */
        protected int[] advances;
        /**
         *  Description of the Field
         */
        protected int[] codes;
        /**
         *  Description of the Field
         */
        protected Rect[] bounds;
        /**
         *  Description of the Field
         */
        protected List defGlyphs;
        /**
         *  Description of the Field
         */
        protected List fontGlyphs;
        /**
         *  Description of the Field
         */
        protected int index = 0;


        /**
         *  Constructor for the GlyphBuilder object
         *
         *@param  fontDef     Description of the Parameter
         *@param  font        Description of the Parameter
         *@param  glyphCount  Description of the Parameter
         */
        public GlyphBuilder(FontDefinition fontDef, Font font, int glyphCount) {
            super(new Shape());

            this.fontDef = fontDef;
            this.font = font;
            this.count = glyphCount;

            defGlyphs = fontDef.getGlyphList();
            fontGlyphs = font.getGlyphList();
        }


        /**
         *  Constructor for the GlyphBuilder object
         *
         *@param  fontDef   Description of the Parameter
         *@param  font      Description of the Parameter
         *@param  codes     Description of the Parameter
         *@param  advances  Description of the Parameter
         *@param  bounds    Description of the Parameter
         */
        public GlyphBuilder(FontDefinition fontDef, Font font, int[] codes,
                int[] advances, Rect[] bounds) {
            this(fontDef, font, codes.length);
            this.codes = codes;
            this.advances = advances;
            this.bounds = bounds;
        }


        /**
         *  Description of the Method
         */
        public void done() {
            if (index >= count) {
                return;
            }

            if (bounds != null) {
                Rect r = bounds[index];
                s.setBoundingRectangle(((double) r.getMinX()) / SWFConstants.TWIPS,
                        ((double) r.getMinY()) / SWFConstants.TWIPS,
                        ((double) r.getMaxX()) / SWFConstants.TWIPS,
                        ((double) r.getMaxY()) / SWFConstants.TWIPS);
            }

            double advance = (advances != null) ?
                    (((double) advances[index]) / SWFConstants.TWIPS) :
                    0.0;

            int code = (codes != null) ? codes[index] : 0;

            FontDefinition.Glyph g = new FontDefinition.Glyph(s, advance, code);

            defGlyphs.add(g);
            font.addGlyph(g);

            index++;

            if (index < count) {
                s = new Shape();
            }
        }
    }


    /**
     *  A SWFText implementation that builds a Text object
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class TextBuilder implements SWFText {
        /**
         *  Description of the Field
         */
        protected Text t;
        /**
         *  Description of the Field
         */
        protected Font font;
        /**
         *  Description of the Field
         */
        protected double size;
        /**
         *  Description of the Field
         */
        protected Color color;
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
         *  Constructor for the TextBuilder object
         *
         *@param  text  Description of the Parameter
         */
        protected TextBuilder(Text text) {
            t = text;
        }


        /**
         *  SWFText interface
         *
         *@param  fontId      Description of the Parameter
         *@param  textHeight  Description of the Parameter
         */
        public void font(int fontId, int textHeight) {
            Symbol f = getSymbol(fontId);
            if (f == null || !(f instanceof Font)) {
                return;
            }

            font = (Font) f;
            size = ((double) textHeight) / SWFConstants.TWIPS;
        }


        /**
         *  SWFText interface
         *
         *@param  color  Description of the Parameter
         */
        public void color(Color color) {
            this.color = color;
        }


        /**
         *  SWFText interface
         *
         *@param  x  The new x value
         */
        public void setX(int x) {
            hasX = true;
            this.x = ((double) x) / SWFConstants.TWIPS;
        }


        /**
         *  SWFText interface
         *
         *@param  y  The new y value
         */
        public void setY(int y) {
            hasY = true;
            this.y = ((double) y) / SWFConstants.TWIPS;
        }


        /**
         *  SWFText interface
         *
         *@param  glyphIndices   Description of the Parameter
         *@param  glyphAdvances  Description of the Parameter
         */
        public void text(int[] glyphIndices, int[] glyphAdvances) {
            List glyphs = font.getGlyphList();
            char[] chars = new char[glyphIndices.length];

            for (int i = 0; i < glyphIndices.length; i++) {
                int index = glyphIndices[i];
                double advance = ((double) glyphAdvances[i]) / SWFConstants.TWIPS;

                //normalize the advance
                advance = advance * (1024.0 / SWFConstants.TWIPS) / size;

                FontDefinition.Glyph g = (FontDefinition.Glyph) glyphs.get(index);

                chars[i] = (char) g.getCode();

                //--retrofit the glyph advance - this will tend to cancel out
                // any kerning (unfortunately)
                if (advance > g.getAdvance()) {
                    g.setAdvance(advance);
                }
            }

            try {
                Font.Chars cc = font.chars(new String(chars), size);

                t.row(cc, color, x, y, hasX, hasY);
            } catch (Font.NoGlyphException nge) {color=null;}

            color = null;
            hasX = false;
            hasY = false;
        }


        /**
         *  SWFText interface
         */
        public void done() {
            //nothing
        }
    }
}
