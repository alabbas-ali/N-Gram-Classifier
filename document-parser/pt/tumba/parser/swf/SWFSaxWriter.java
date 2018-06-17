package pt.tumba.parser.swf;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *  A SAX2 Handler that drives any implementation of the SWFTagTypes interface
 *  and understands the XML Vocabulary produced by SWFSaxParser. This class is
 *  not thread-safe but it can be reused by setting a different SWFTagTypes
 *  value. Reuse is recommended since the initialization overhead is expensive.
 *
 *@author     unknown
 *@created    15 de Setembro de 2002
 */
public class SWFSaxWriter extends SaxHandlerBase {
    /**
     *  Description of the Field
     */
    protected SWFTagTypes tags;
    /**
     *  Description of the Field
     */
    protected SWFTagTypes movieTags;
    /**
     *  Description of the Field
     */
    protected SWFShape shape;
    /**
     *  Description of the Field
     */
    protected SWFVectors vectors;
    /**
     *  Description of the Field
     */
    protected SWFActions actions;
    /**
     *  Description of the Field
     */
    protected SWFText text;

    /**
     *  Description of the Field
     */
    protected int actionMode = 0;
    //1=button actions, 2=clip actions
    /**
     *  Description of the Field
     */
    protected int flashVersion = 5;

    /**
     *  Description of the Field
     */
    protected boolean idAllocate = false;
    //whether to auto allocate ids
    /**
     *  Description of the Field
     */
    protected int newId = 1;

    /**
     *  Description of the Field
     */
    protected Matrix matrix;
    /**
     *  Description of the Field
     */
    protected Color color;
    /**
     *  Description of the Field
     */
    protected AlphaTransform cxform;
    /**
     *  Description of the Field
     */
    protected boolean hasAlpha;

    /**
     *  Description of the Field
     */
    protected Map ids = new HashMap();

    //--for buttons
    /**
     *  Description of the Field
     */
    protected List buttonRecords = new Vector();

    //--for bitmaps/jpeg:
    /**
     *  Description of the Field
     */
    protected List bitmapColors;
    /**
     *  Description of the Field
     */
    protected byte[] pixelData;
    /**
     *  Description of the Field
     */
    protected byte[] jpegAlpha;

    //--for gradients:
    /**
     *  Description of the Field
     */
    protected List colors;
    /**
     *  Description of the Field
     */
    protected List ratios;

    //--for import and export
    /**
     *  Description of the Field
     */
    protected List symbols;

    //--for text chars
    /**
     *  Description of the Field
     */
    protected List chars;

    //--for font-info
    /**
     *  Description of the Field
     */
    protected List codes;

    //--for lookup tables
    /**
     *  Description of the Field
     */
    protected List lookupValues = new Vector();

    //--for sound:
    /**
     *  Description of the Field
     */
    protected List soundInfos;


    /**
     *  Constructor for the SWFSaxWriter object
     *
     *@param  tags  Description of the Parameter
     */
    public SWFSaxWriter(SWFTagTypes tags) {
		this.tags = tags;
		this.movieTags = tags;
        initElements();
    }


    /**
     *  Sets the tagTypes attribute of the SWFSaxWriter object
     *
     *@param  tags  The new tagTypes value
     */
    public void setTagTypes(SWFTagTypes tags) {
        this.tags = tags;
        this.movieTags = tags;
    }


    /**
     *  Resolve a symbolic id to a numeric id
     *
     *@param  id                Description of the Parameter
     *@return                   The id value
     *@exception  SAXException  Description of the Exception
     */
    protected int getId(String id) throws SAXException {
        if (id == null) {
            throw new SAXException("Missing id.");
        }

        if (idAllocate) {
            Integer idI = (Integer) ids.get(id);

            if (idI != null) {
                return idI.intValue();
            }

            throw new SAXException("Id not found: " + id);
        }

        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException nfe) {
            throw new SAXException("Invalid id: " + id);
        }
    }


    /**
     *  Get an existing symbolic id - or allocate one if it does not exist
     *
     *@param  id                Description of the Parameter
     *@return                   The orAllocateId value
     *@exception  SAXException  Description of the Exception
     */
    protected int getOrAllocateId(String id) throws SAXException {
        if (id == null) {
            throw new SAXException("Missing id.");
        }

        if (idAllocate) {
            Integer idI = (Integer) ids.get(id);

            if (idI != null) {
                return idI.intValue();
            }

            ids.put(id, new Integer(newId));
            return newId++;
        }

        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException nfe) {
            throw new SAXException("Invalid id: " + id);
        }
    }


    /**
     *  Define a symbolic id and allocate the numeric id
     *
     *@param  id                Description of the Parameter
     *@return                   Description of the Return Value
     *@exception  SAXException  Description of the Exception
     */
    protected int newId(String id) throws SAXException {
        if (id == null) {
            throw new SAXException("Missing id.");
        }

        if (idAllocate) {
            ids.put(id, new Integer(newId));
            return newId++;
        }

        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException nfe) {
            throw new SAXException("Invalid id: " + id);
        }
    }


    /**
     *  Description of the Method
     */
    protected final void initElements() {
        elementTypes.put("movie", new Movie());
        elementTypes.put("sprite", new Sprite());
        elementTypes.put("frame", new Frame());
        elementTypes.put("tag", new Tag());
        elementTypes.put("background-color", new BackgroundColor());
        elementTypes.put("protect", new Protect());
        elementTypes.put("enable-debug", new EnableDebug());
        elementTypes.put("actions", new Actions());
        elementTypes.put("shape", new Shape());
        elementTypes.put("release", new Release());
        elementTypes.put("remove", new Remove());
        elementTypes.put("place", new Place());
        elementTypes.put("jpeg", new Jpeg());
        elementTypes.put("bitmap", new Bitmap());
        elementTypes.put("import", new Import());
        elementTypes.put("export", new Export());
        elementTypes.put("quicktime-movie", new QTMovie());
        elementTypes.put("text-font", new TextFont());
        elementTypes.put("text", new Text());
        elementTypes.put("font-info", new FontInfo());
        elementTypes.put("edit-font", new EditFont());
        elementTypes.put("edit-field", new EditField());
        elementTypes.put("morph", new Morph());
        elementTypes.put("button", new Button());
        elementTypes.put("jpeg-header", new JPEGHeader());

        //--Sound:
        elementTypes.put("sound", new Sound());
        elementTypes.put("button-sound", new ButtonSound());
        elementTypes.put("sound-info", new Sound_Info());
        elementTypes.put("start-sound", new StartSound());
        elementTypes.put("sound-stream-header", new SoundHeader());
        elementTypes.put("sound-stream-block", new SoundBlock());

        //--For JPEG:
        elementTypes.put("image", new JPEGImage());
        elementTypes.put("alpha", new JPEGAlpha());

        //--For Button
        elementTypes.put("layer", new Layer());

        //--For Generator
        elementTypes.put("serial-number", new SerialNumber());
        elementTypes.put("generator", new Generator());
        elementTypes.put("generator-text", new GeneratorText());
        elementTypes.put("generator-command", new GeneratorCommand());
        elementTypes.put("generator-font", new GeneratorFont());
        elementTypes.put("generator-name-character", new GeneratorNameCharacter());

        //--For Text
        elementTypes.put("set-font", new SetFont());
        elementTypes.put("set-x", new SetX());
        elementTypes.put("set-y", new SetY());
        elementTypes.put("char", new Char());

        //--For fonts
        elementTypes.put("glyph", new Glyph());
        elementTypes.put("code", new Code());
        elementTypes.put("anticlockwise", new Anticlockwise());

        //--For import and export
        elementTypes.put("symbol", new Symbol());

        //--Bitmap elements
        elementTypes.put("colors", new Colors());
        elementTypes.put("pixels", new Pixels());

        //--Structures
        elementTypes.put("color", new ColorElem());
        elementTypes.put("matrix", new MatrixElem());
        elementTypes.put("color-transform", new CXFormElem());

        //--Shape elements:
        elementTypes.put("line", new ShapeLine());
        elementTypes.put("curve", new ShapeCurve());
        elementTypes.put("move", new ShapeMove());
        elementTypes.put("set-primary-fill", new ShapeFill0());
        elementTypes.put("set-secondary-fill", new ShapeFill1());
        elementTypes.put("set-line-style", new ShapeSetLine());
        elementTypes.put("color-fill", new ShapeColorFill());
        elementTypes.put("gradient-fill", new ShapeGradient());
        elementTypes.put("image-fill", new ShapeImage());
        elementTypes.put("line-style", new ShapeLineStyle());
        elementTypes.put("step", new Step());

        //--Actions
        elementTypes.put("unknown", new ActionUnknown());
        elementTypes.put("jump-label", new ActionJumpLabel());
        elementTypes.put("comment", new ActionComment());
        elementTypes.put("goto-frame", new ActionGotoFrame());
        elementTypes.put("get-url", new ActionGetUrl());
        elementTypes.put("next-frame", new ActionNextFrame());
        elementTypes.put("prev-frame", new ActionPrevFrame());
        elementTypes.put("play", new ActionPlay());
        elementTypes.put("stop", new ActionStop());
        elementTypes.put("toggle-quality", new ActionToggleQuality());
        elementTypes.put("stop-sounds", new ActionStopSounds());
        elementTypes.put("wait-for-frame", new ActionWaitForFrame());
        elementTypes.put("set-target", new ActionSetTarget());
        elementTypes.put("push", new ActionPush());
        elementTypes.put("pop", new ActionPop());
        elementTypes.put("add", new ActionAdd());
        elementTypes.put("subtract", new ActionSubtract());
        elementTypes.put("multiply", new ActionMultiply());
        elementTypes.put("divide", new ActionDivide());
        elementTypes.put("equals", new ActionEquals());
        elementTypes.put("less-than", new ActionLessThan());
        elementTypes.put("and", new ActionAnd());
        elementTypes.put("or", new ActionOr());
        elementTypes.put("not", new ActionNot());
        elementTypes.put("string-equals", new ActionStringEquals());
        elementTypes.put("string-length", new ActionStringLength());
        elementTypes.put("concat", new ActionConcat());
        elementTypes.put("substring", new ActionSubstring());
        elementTypes.put("string-less-than", new ActionStringLessThan());
        elementTypes.put("mutlibyte-string-length", new ActionMutlibyteStringLength());
        elementTypes.put("multibyte-substring", new ActionMultibyteSubstring());
        elementTypes.put("to-integer", new ActionToInteger());
        elementTypes.put("char-to-ascii", new ActionCharToAscii());
        elementTypes.put("ascii-to-char", new ActionAsciiToChar());
        elementTypes.put("mutlibyte-char-to-ascii", new ActionMutlibyteCharToAscii());
        elementTypes.put("ascii-to-multibyte-char", new ActionAsciiToMultibyteChar());
        elementTypes.put("jump", new ActionJump());
        elementTypes.put("if", new ActionIf());
        elementTypes.put("call", new ActionCall());
        elementTypes.put("get-variable", new ActionGetVariable());
        elementTypes.put("set-variable", new ActionSetVariable());
        elementTypes.put("set-target", new ActionSetTarget());
        elementTypes.put("get-property", new ActionGetProperty());
        elementTypes.put("set-property", new ActionSetProperty());
        elementTypes.put("clone-sprite", new ActionCloneSprite());
        elementTypes.put("remove-sprite", new ActionRemoveSprite());
        elementTypes.put("start-drag", new ActionStartDrag());
        elementTypes.put("end-drag", new ActionEndDrag());
        elementTypes.put("wait-for-frame", new ActionWaitForFrame());
        elementTypes.put("trace", new ActionTrace());
        elementTypes.put("get-time", new ActionGetTime());
        elementTypes.put("random-number", new ActionRandomNumber());
        elementTypes.put("call-function", new ActionCallFunction());
        elementTypes.put("call-method", new ActionCallMethod());
        elementTypes.put("lookup-table", new ActionLookupTable());
        elementTypes.put("function", new ActionFunction());
        elementTypes.put("define-local-value", new ActionDefineLocalValue());
        elementTypes.put("define-local", new ActionDefineLocal());
        elementTypes.put("delete-property", new ActionDeleteProperty());
        elementTypes.put("delete-thread-vars", new ActionDeleteThreadVars());
        elementTypes.put("enumerate", new ActionEnumerate());
        elementTypes.put("typed-equals", new ActionTypedEquals());
        elementTypes.put("get-member", new ActionGetMember());
        elementTypes.put("init-array", new ActionInitArray());
        elementTypes.put("init-object", new ActionInitObject());
        elementTypes.put("new-method", new ActionNewMethod());
        elementTypes.put("new-object", new ActionNewObject());
        elementTypes.put("set-member", new ActionSetMember());
        elementTypes.put("get-target-path", new ActionGetTargetPath());
        elementTypes.put("with", new ActionWith());
        elementTypes.put("to-number", new ActionToNumber());
        elementTypes.put("to-string", new ActionToString());
        elementTypes.put("type-of", new ActionTypeOf());
        elementTypes.put("typed-add", new ActionTypedAdd());
        elementTypes.put("typed-less-than", new ActionTypedLessThan());
        elementTypes.put("modulo", new ActionModulo());
        elementTypes.put("bit-and", new ActionBitAnd());
        elementTypes.put("bit-or", new ActionBitOr());
        elementTypes.put("bit-xor", new ActionBitXor());
        elementTypes.put("shift-left", new ActionShiftLeft());
        elementTypes.put("shift-right", new ActionShiftRight());
        elementTypes.put("shift-right-unsigned", new ActionShiftRightUnsigned());
        elementTypes.put("decrement", new ActionDecrement());
        elementTypes.put("increment", new ActionIncrement());
        elementTypes.put("duplicate", new ActionDuplicate());
        elementTypes.put("return", new ActionReturn());
        elementTypes.put("swap", new ActionSwap());
        elementTypes.put("store", new ActionStore());

        //--For lookup tables
        elementTypes.put("value", new LookupValue());
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class Sound extends SaxHandlerBase.ContentElementType {
        /**
         *  Description of the Method
         *
         *@exception  Exception  Description of the Exception
         */
        public void endElement() throws Exception {
            int id = newId(attrs.getValue("", "id"));

            String form = getAttr(attrs, "format", "mp3");
            int format = SWFConstants.SOUND_FORMAT_MP3;
            if (form.equals("raw")) {
                format = SWFConstants.SOUND_FORMAT_RAW;
            }
            if (form.equals("adpcm")) {
                format = SWFConstants.SOUND_FORMAT_ADPCM;
            }

            String rate = getAttr(attrs, "rate", "11");
            int freq = SWFConstants.SOUND_FREQ_11KHZ;
            if (rate.equals("5.5")) {
                freq = SWFConstants.SOUND_FREQ_5_5KHZ;
            }
            if (rate.equals("22")) {
                freq = SWFConstants.SOUND_FREQ_22KHZ;
            }
            if (rate.equals("44")) {
                freq = SWFConstants.SOUND_FREQ_44KHZ;
            }

            String bits = getAttr(attrs, "bits", "16");
            boolean bits16 = bits.equals("16");

            boolean stereo = getAttrBool(attrs, "stereo", false);
            int sampleCount = getAttrInt(attrs, "sample-count", 0);

            tags.tagDefineSound(id, format, freq, bits16, stereo, sampleCount,
                    Base64.decode(buff.toString()));
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class Sound_Info extends SaxHandlerBase.GatheringElementType {
        /**
         *  Description of the Field
         */
        protected List envPoints = new Vector();


        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            super.startElement(atts);
            envPoints.clear();
            startGatherMode(this);
        }


        /**
         *  Description of the Method
         *
         *@param  localName  Description of the Parameter
         *@param  atts       Description of the Parameter
         *@return            Description of the Return Value
         */
        public boolean gatherElement(String localName, Attributes atts) {
            if (localName.equals("envelope-point")) {
                int position = getAttrInt(atts, "position", 0);
                int level0 = getAttrInt(atts, "level-0", 0);
                int level1 = getAttrInt(atts, "level-1", 0);

                envPoints.add(new SoundInfo.EnvelopePoint(position, level0, level1));
            }

            return false;
            //do not gather
        }


        /**
         *  Description of the Method
         *
         *@exception  Exception  Description of the Exception
         */
        public void endElement() throws Exception {
            boolean noMultiplePlay = getAttrBool(attrs, "single-instance", false);
            boolean stopSound = getAttrBool(attrs, "stop-playing", false);
            int inPoint = getAttrInt(attrs, "fade-in", -1);
            int outPoint = getAttrInt(attrs, "fade-out", -1);
            int loopCount = getAttrInt(attrs, "loop-count", 1);

            int id = getId(getAttr(attrs, "sound-id", ""));
            String event = attrs.getValue("", "event");

            SoundInfo.EnvelopePoint[] envelope =
                    new SoundInfo.EnvelopePoint[envPoints.size()];

            envPoints.toArray(envelope);
            envPoints.clear();

            soundInfos.add(new Object[]{
                    new Integer(id),
                    event,
                    new SoundInfo(noMultiplePlay, stopSound,
                    envelope, inPoint, outPoint, loopCount)});
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ButtonSound extends SaxHandlerBase.ContentElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            super.startElement(atts);
            soundInfos = new ArrayList();
        }


        /**
         *  Description of the Method
         *
         *@exception  Exception  Description of the Exception
         */
        public void endElement() throws Exception {
            int id = getId(attrs.getValue("", "id"));

            int rolloverId = 0;
            SoundInfo rolloverInfo = null;

            int rolloutId = 0;
            SoundInfo rolloutInfo = null;

            int pressId = 0;
            SoundInfo pressInfo = null;

            int releaseId = 0;
            SoundInfo releaseInfo = null;

            for (Iterator it = soundInfos.iterator(); it.hasNext(); ) {
                Object[] oo = (Object[]) it.next();

                int soundId = ((Integer) oo[0]).intValue();
                String event = (String) oo[1];
                SoundInfo si = (SoundInfo) oo[2];

                if ("roll-over".equals(event)) {
                    rolloverId = soundId;
                    rolloverInfo = si;
                } else if ("roll-out".equals(event)) {
                    rolloutId = soundId;
                    rolloutInfo = si;
                } else if ("press".equals(event)) {
                    pressId = soundId;
                    pressInfo = si;
                } else if ("release".equals(event)) {
                    releaseId = soundId;
                    releaseInfo = si;
                }
            }

            tags.tagDefineButtonSound(id,
                    rolloverId, rolloverInfo,
                    rolloutId, rolloutInfo,
                    pressId, pressInfo,
                    releaseId, releaseInfo);

            soundInfos = null;
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class StartSound extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            soundInfos = new ArrayList();
        }


        /**
         *  Description of the Method
         *
         *@exception  Exception  Description of the Exception
         */
        public void endElement() throws Exception {
            if (soundInfos.size() < 1) {
                return;
            }

            Object[] oo = (Object[]) soundInfos.get(0);

            int id = ((Integer) oo[0]).intValue();
            SoundInfo si = (SoundInfo) oo[2];

            tags.tagStartSound(id, si);
            soundInfos = null;
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class SoundHeader extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            String form = getAttr(atts, "stream-format", "mp3");
            int format = SWFConstants.SOUND_FORMAT_MP3;
            if (form.equals("raw")) {
                format = SWFConstants.SOUND_FORMAT_RAW;
            }
            if (form.equals("adpcm")) {
                format = SWFConstants.SOUND_FORMAT_ADPCM;
            }

            String rate = getAttr(atts, "play-rate", "11");
            int freq = SWFConstants.SOUND_FREQ_11KHZ;
            if (rate.equals("5.5")) {
                freq = SWFConstants.SOUND_FREQ_5_5KHZ;
            }
            if (rate.equals("22")) {
                freq = SWFConstants.SOUND_FREQ_22KHZ;
            }
            if (rate.equals("44")) {
                freq = SWFConstants.SOUND_FREQ_44KHZ;
            }
            int playFreq = freq;

            rate = getAttr(atts, "stream-rate", "11");
            freq = SWFConstants.SOUND_FREQ_11KHZ;
            if (rate.equals("5.5")) {
                freq = SWFConstants.SOUND_FREQ_5_5KHZ;
            }
            if (rate.equals("22")) {
                freq = SWFConstants.SOUND_FREQ_22KHZ;
            }
            if (rate.equals("44")) {
                freq = SWFConstants.SOUND_FREQ_44KHZ;
            }
            int streamFreq = freq;

            boolean play16bit = getAttr(atts, "play-bits", "16").equals("16");
            boolean stream16bit = getAttr(atts, "stream-bits", "16").equals("16");

            boolean playStereo = getAttrBool(atts, "play-stereo", false);
            boolean streamStereo = getAttrBool(atts, "stream-stereo", false);

            int averageSampleCount = getAttrInt(atts, "average-sample-count", 0);

            tags.tagSoundStreamHead2(playFreq, play16bit, playStereo,
                    format, streamFreq, stream16bit,
                    streamStereo, averageSampleCount);
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class SoundBlock extends SaxHandlerBase.ContentElementType {
        /**
         *  Description of the Method
         *
         *@exception  Exception  Description of the Exception
         */
        public void endElement() throws Exception {
            tags.tagSoundStreamBlock(Base64.decode(buff.toString()));
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class Movie extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            int width = getAttrInt(atts, "width", 550);
            int height = getAttrInt(atts, "height", 400);
            int rate = getAttrInt(atts, "rate", 12);
            int version = getAttrInt(atts, "version", 5);
            idAllocate = getAttrBool(atts, "allocate-ids", false);

            flashVersion = version;

            tags.header(version, -1,
                    width * SWFConstants.TWIPS,
                    height * SWFConstants.TWIPS,
                    rate, -1);
        }


        /**
         *  Description of the Method
         *
         *@exception  Exception  Description of the Exception
         */
        public void endElement() throws Exception {
            tags.tagEnd();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class Sprite extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            int id = newId(atts.getValue("", "id"));

            tags = tags.tagDefineSprite(id);
        }


        /**
         *  Description of the Method
         *
         *@exception  Exception  Description of the Exception
         */
        public void endElement() throws Exception {
            tags.tagEnd();
            //end the sprite timeline
            tags = movieTags;
            //restore main timeline
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class Frame extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            String label = atts.getValue("", "label");
            if (label != null && label.length() > 0) {
                tags.tagFrameLabel(label);
            }

            int count = getAttrInt(atts, "count", 1);

            while (count > 0) {
                tags.tagShowFrame();
                count--;
            }
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class SerialNumber extends SaxHandlerBase.ContentElementType {
        /**
         *  Description of the Method
         *
         *@exception  Exception  Description of the Exception
         */
        public void endElement() throws Exception {
            tags.tagSerialNumber(buff.toString());
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class Generator extends SaxHandlerBase.ContentElementType {
        /**
         *  Description of the Method
         *
         *@exception  Exception  Description of the Exception
         */
        public void endElement() throws Exception {
            tags.tagGenerator(Base64.decode(buff.toString()));
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class GeneratorText extends SaxHandlerBase.ContentElementType {
        /**
         *  Description of the Method
         *
         *@exception  Exception  Description of the Exception
         */
        public void endElement() throws Exception {
            tags.tagGeneratorText(Base64.decode(buff.toString()));
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class GeneratorCommand extends SaxHandlerBase.ContentElementType {
        /**
         *  Description of the Method
         *
         *@exception  Exception  Description of the Exception
         */
        public void endElement() throws Exception {
            tags.tagGeneratorCommand(Base64.decode(buff.toString()));
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class GeneratorFont extends SaxHandlerBase.ContentElementType {
        /**
         *  Description of the Method
         *
         *@exception  Exception  Description of the Exception
         */
        public void endElement() throws Exception {
            tags.tagGeneratorFont(Base64.decode(buff.toString()));
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class GeneratorNameCharacter extends SaxHandlerBase.ContentElementType {
        /**
         *  Description of the Method
         *
         *@exception  Exception  Description of the Exception
         */
        public void endElement() throws Exception {
            tags.tagNameCharacter(Base64.decode(buff.toString()));
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class Tag extends SaxHandlerBase.ContentElementType {
        /**
         *  Description of the Method
         *
         *@exception  Exception  Description of the Exception
         */
        public void endElement() throws Exception {
            int type = getAttrInt(attrs, "type", 0);

            String base64 = buff.toString().trim();

            byte[] bytes = (base64.length() > 0) ? Base64.decode(base64) : null;

            tags.tag(type, false, bytes);
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class Protect extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            String password = atts.getValue("", "password");

            byte[] bytes = null;
            if (password != null && password.length() > 0) {
                bytes = Base64.decode(password);
            }

            tags.tagProtect(bytes);
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class EnableDebug extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            String password = atts.getValue("", "password");

            byte[] bytes = null;
            if (password != null && password.length() > 0) {
                bytes = Base64.decode(password);
            }

            tags.tagEnableDebug(bytes);
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class BackgroundColor extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            int red = getAttrInt(atts, "red", 255);
            int green = getAttrInt(atts, "green", 255);
            int blue = getAttrInt(atts, "blue", 255);

            tags.tagSetBackgroundColor(new Color(red, green, blue));
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class Shape extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Field
         */
        protected boolean insideMorph;


        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            if (shape == null) {
                insideMorph = false;

                hasAlpha = getAttrBool(atts, "has-alpha", false);

                int id = newId(atts.getValue("", "id"));

                double minx = getAttrDouble(atts, "min-x", 0.0);
                double miny = getAttrDouble(atts, "min-y", 0.0);
                double maxx = getAttrDouble(atts, "max-x", 0.0);
                double maxy = getAttrDouble(atts, "max-y", 0.0);

                Rect rect = new Rect((int) (minx * SWFConstants.TWIPS),
                        (int) (miny * SWFConstants.TWIPS),
                        (int) (maxx * SWFConstants.TWIPS),
                        (int) (maxy * SWFConstants.TWIPS));

                shape = hasAlpha ?
                        tags.tagDefineShape3(id, rect) :
                        tags.tagDefineShape2(id, rect);

                vectors = shape;
            } else {
                insideMorph = true;
            }
        }


        /**
         *  Description of the Method
         *
         *@exception  Exception  Description of the Exception
         */
        public void endElement() throws Exception {
            vectors.done();

            if (!insideMorph) {
                vectors = null;
                shape = null;
            }
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ShapeLine extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            double dx = getAttrDouble(atts, "dx", 0.0);
            double dy = getAttrDouble(atts, "dy", 0.0);

            vectors.line((int) (dx * SWFConstants.TWIPS),
                    (int) (dy * SWFConstants.TWIPS));
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ShapeCurve extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            double cx = getAttrDouble(atts, "cx", 0.0);
            double cy = getAttrDouble(atts, "cy", 0.0);
            double dx = getAttrDouble(atts, "dx", 0.0);
            double dy = getAttrDouble(atts, "dy", 0.0);

            vectors.curve((int) (cx * SWFConstants.TWIPS),
                    (int) (cy * SWFConstants.TWIPS),
                    (int) (dx * SWFConstants.TWIPS),
                    (int) (dy * SWFConstants.TWIPS));
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ShapeMove extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            double x = getAttrDouble(atts, "x", 0.0);
            double y = getAttrDouble(atts, "y", 0.0);

            vectors.move((int) (x * SWFConstants.TWIPS),
                    (int) (y * SWFConstants.TWIPS));
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ShapeFill0 extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            int index = getAttrInt(atts, "index", 1);
            shape.setFillStyle0(index);
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ShapeFill1 extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            int index = getAttrInt(atts, "index", 1);
            shape.setFillStyle1(index);
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ShapeSetLine extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            int index = getAttrInt(atts, "index", 1);
            shape.setLineStyle(index);
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ShapeColorFill extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            super.startElement(atts);
            color = null;
        }


        /**
         *  Description of the Method
         *
         *@exception  Exception  Description of the Exception
         */
        public void endElement() throws Exception {
            if (color == null) {
                color = new Color(255, 255, 255);
            }
            shape.defineFillStyle(color);
            color = null;
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ShapeGradient extends SaxHandlerBase.ContentElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            super.startElement(atts);

            colors = new ArrayList();
            ratios = new ArrayList();

            matrix = null;
        }


        /**
         *  Description of the Method
         *
         *@exception  Exception  Description of the Exception
         */
        public void endElement() throws Exception {
            boolean radial = getAttrBool(attrs, "radial", false);

            if (matrix == null) {
                matrix = new Matrix();
            }

            Color[] colorsA = new Color[colors.size()];
            int[] ratiosA = new int[colors.size()];

            for (int i = 0; i < colorsA.length; i++) {
                colorsA[i] = (Color) colors.get(i);
                ratiosA[i] = ((Integer) ratios.get(i)).intValue();
            }

            shape.defineFillStyle(matrix, ratiosA, colorsA, radial);
            matrix = null;
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ShapeImage extends SaxHandlerBase.ContentElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            super.startElement(atts);
            matrix = null;
        }


        /**
         *  Description of the Method
         *
         *@exception  Exception  Description of the Exception
         */
        public void endElement() throws Exception {
            if (matrix == null) {
                matrix = new Matrix();
            }
            int bitmapId = getId(attrs.getValue("", "image-id"));
            boolean clipped = getAttrBool(attrs, "clipped", false);

            shape.defineFillStyle(bitmapId, matrix, clipped);
            matrix = null;
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ShapeLineStyle extends SaxHandlerBase.ContentElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            super.startElement(atts);
            color = null;
        }


        /**
         *  Description of the Method
         *
         *@exception  Exception  Description of the Exception
         */
        public void endElement() throws Exception {
            double width = getAttrDouble(attrs, "width", 1.0);

            if (color == null) {
                color = new Color(0, 0, 0);
            }
            shape.defineLineStyle((int) (width * SWFConstants.TWIPS), color);
            color = null;
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class Step extends SaxHandlerBase.ContentElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            super.startElement(atts);
            color = null;
        }


        /**
         *  Description of the Method
         *
         *@exception  Exception  Description of the Exception
         */
        public void endElement() throws Exception {
            if (color == null) {
                color = new Color(0, 0, 0);
            }
            int ratio = getAttrInt(attrs, "ratio", 0);

            ratios.add(new Integer(ratio));
            colors.add(color);
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class Release extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            int id = getId(atts.getValue("", "id"));

            tags.tagFreeCharacter(id);
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class Remove extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            int depth = getAttrInt(atts, "depth", 0);

            tags.tagRemoveObject2(depth);
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class Place extends SaxHandlerBase.GatheringElementType {
        /**
         *  Description of the Field
         */
        protected int actionFlags;


        //cummulative action conditions

        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            super.startElement(atts);
            matrix = null;
            cxform = null;
            actionFlags = 0;

            startGatherMode(this);
        }


        /**
         *  Description of the Method
         *
         *@param  localName  Description of the Parameter
         *@param  atts       Description of the Parameter
         *@return            Description of the Return Value
         */
        public boolean gatherElement(String localName, Attributes atts) {
            if (localName.equals("actions")) {
                String flags = getAttr(atts, "conditions", "");

                StringTokenizer tok = new StringTokenizer(flags);
                while (tok.hasMoreTokens()) {
                    String f = tok.nextToken();

                    if (f.equals("load")) {
                        actionFlags |= SWFConstants.CLIP_ACTION_ON_LOAD;
                    } else if (f.equals("enter-frame")) {
                        actionFlags |= SWFConstants.CLIP_ACTION_ENTER_FRAME;
                    } else if (f.equals("unload")) {
                        actionFlags |= SWFConstants.CLIP_ACTION_UNLOAD;
                    } else if (f.equals("mouse-move")) {
                        actionFlags |= SWFConstants.CLIP_ACTION_MOUSE_MOVE;
                    } else if (f.equals("mouse-down")) {
                        actionFlags |= SWFConstants.CLIP_ACTION_MOUSE_DOWN;
                    } else if (f.equals("mouse-up")) {
                        actionFlags |= SWFConstants.CLIP_ACTION_MOUSE_UP;
                    } else if (f.equals("key-down")) {
                        actionFlags |= SWFConstants.CLIP_ACTION_KEY_DOWN;
                    } else if (f.equals("key-up")) {
                        actionFlags |= SWFConstants.CLIP_ACTION_KEY_UP;
                    } else if (f.equals("data")) {
                        actionFlags |= SWFConstants.CLIP_ACTION_DATA;
                    }
                }
            }

            return true;
        }


        /**
         *  Description of the Method
         *
         *@exception  Exception  Description of the Exception
         */
        public void endElement() throws Exception {
            int id = getId(getAttr(attrs, "id", "-1"));
            boolean alter = getAttrBool(attrs, "alter", false);
            int clip = getAttrInt(attrs, "clip-depth", -1);
            int ratio = getAttrInt(attrs, "ratio", -1);
            int depth = getAttrInt(attrs, "depth", -1);
            String name = attrs.getValue("", "name");

            dispatchGatheredElement("matrix");
            dispatchGatheredElement("color-transform");

            actions = tags.tagPlaceObject2(alter, clip, depth, id, matrix,
                    cxform, ratio, name, actionFlags);

            if (actionFlags != 0 && actions != null) {
                actionMode = 2;
                endGatherMode();
                //replay all the actions
                actions.done();
                actionMode = 0;
            }

            matrix = null;
            cxform = null;
            actions = null;
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class Jpeg extends SaxHandlerBase.ContentElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            super.startElement(atts);
            pixelData = null;
            jpegAlpha = null;
        }


        /**
         *  Description of the Method
         *
         *@exception  Exception  Description of the Exception
         */
        public void endElement() throws Exception {
            int id = newId(attrs.getValue("", "id"));
            String filename = attrs.getValue("", "file");
            boolean commonHeader = getAttrBool(attrs, "common-header", false);

            if (filename != null) {
                tags.tagDefineBitsJPEG2(id, new FileInputStream(filename));

                return;
            }

            if (jpegAlpha != null) {
                tags.tagDefineBitsJPEG3(id, pixelData, jpegAlpha);
            } else if (commonHeader) {
                tags.tagDefineBits(id, pixelData);
            } else {
                tags.tagDefineBitsJPEG2(id, pixelData);
            }

            pixelData = null;
            jpegAlpha = null;
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class JPEGImage extends SaxHandlerBase.ContentElementType {
        /**
         *  Description of the Method
         *
         *@exception  Exception  Description of the Exception
         */
        public void endElement() throws Exception {
            pixelData = Base64.decode(buff.toString());
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class JPEGAlpha extends SaxHandlerBase.ContentElementType {
        /**
         *  Description of the Method
         *
         *@exception  Exception  Description of the Exception
         */
        public void endElement() throws Exception {
            jpegAlpha = Base64.decode(buff.toString());
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class JPEGHeader extends SaxHandlerBase.ContentElementType {
        /**
         *  Description of the Method
         *
         *@exception  Exception  Description of the Exception
         */
        public void endElement() throws Exception {
            byte[] data = Base64.decode(buff.toString());
            tags.tagJPEGTables(data);
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class Bitmap extends SaxHandlerBase.ContentElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            super.startElement(atts);
            bitmapColors = null;
        }


        /**
         *  Description of the Method
         *
         *@exception  Exception  Description of the Exception
         */
        public void endElement() throws Exception {
            int id = newId(attrs.getValue("", "id"));

            boolean hasAlpha = getAttrBool(attrs, "has-alpha", false);
            int width = getAttrInt(attrs, "width", 0);
            int height = getAttrInt(attrs, "height", 0);
            int bits = getAttrInt(attrs, "bits", 32);

            int format = SWFConstants.BITMAP_FORMAT_32_BIT;
            if (bits == 8) {
                format = SWFConstants.BITMAP_FORMAT_8_BIT;
            }
            if (bits == 16) {
                format = SWFConstants.BITMAP_FORMAT_16_BIT;
            }

            Color[] colors = null;
            if (bitmapColors != null && !bitmapColors.isEmpty()) {
                colors = new Color[bitmapColors.size()];

                for (int i = 0; i < colors.length; i++) {
                    colors[i] = (Color) bitmapColors.get(i);
                }
            } else if (format != SWFConstants.BITMAP_FORMAT_32_BIT) {
                colors = new Color[0];
            }

            if (hasAlpha) {
                tags.tagDefineBitsLossless2(id, format, width, height,
                        colors, pixelData);
            } else {
                tags.tagDefineBitsLossless(id, format, width, height,
                        colors, pixelData);
            }

            bitmapColors = null;
            pixelData = null;
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class Colors extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            bitmapColors = new ArrayList();
        }


        /**
         *  Description of the Method
         *
         *@exception  Exception  Description of the Exception
         */
        public void endElement() throws Exception { }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class Pixels extends SaxHandlerBase.ContentElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            super.startElement(atts);
        }


        /**
         *  Description of the Method
         *
         *@exception  Exception  Description of the Exception
         */
        public void endElement() throws Exception {
            pixelData = Base64.decode(buff.toString().trim());
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class Import extends SaxHandlerBase.ContentElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            super.startElement(atts);
            symbols = new ArrayList();
        }


        /**
         *  Description of the Method
         *
         *@exception  Exception  Description of the Exception
         */
        public void endElement() throws Exception {
            String movie = attrs.getValue("", "movie");
            if (movie == null) {
                throw new SAXException("Missing movie name in import.");
            }

            String[] names = new String[symbols.size()];
            int[] ids = new int[names.length];

            for (int i = 0; i < names.length; i++) {
                Object[] sym = (Object[]) symbols.get(i);
                names[i] = (String) sym[0];
                ids[i] = ((Integer) sym[1]).intValue();
            }

            tags.tagImport(movie, names, ids);
            symbols = null;
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class Export extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            symbols = new ArrayList();
        }


        /**
         *  Description of the Method
         *
         *@exception  Exception  Description of the Exception
         */
        public void endElement() throws Exception {
            String[] names = new String[symbols.size()];
            int[] ids = new int[names.length];

            for (int i = 0; i < names.length; i++) {
                Object[] sym = (Object[]) symbols.get(i);
                names[i] = (String) sym[0];
                ids[i] = ((Integer) sym[1]).intValue();
            }

            tags.tagExport(names, ids);
            symbols = null;
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class Symbol extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            int id = getOrAllocateId(atts.getValue("", "id"));
            String name = atts.getValue("", "name");

            if (name == null) {
                throw new SAXException("Missing name in import/export symbol");
            }
            if (id < 1) {
                throw new SAXException("Invalid id in import/export symbol");
            }

            if (symbols != null) {
                symbols.add(new Object[]{name, new Integer(id)});
            }
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class TextFont extends SaxHandlerBase.GatheringElementType {
        /**
         *  Description of the Field
         */
        protected int glyphCount;


        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            super.startElement(atts);
            glyphCount = 0;
            startGatherMode(this);
        }


        /**
         *  Description of the Method
         *
         *@param  localName  Description of the Parameter
         *@param  atts       Description of the Parameter
         *@return            Description of the Return Value
         */
        public boolean gatherElement(String localName, Attributes atts) {
            if (localName.equals("glyph")) {
                glyphCount++;
            }
            return true;
        }


        /**
         *  Description of the Method
         *
         *@exception  Exception  Description of the Exception
         */
        public void endElement() throws Exception {
            int id = newId(attrs.getValue("", "id"));

            vectors = tags.tagDefineFont(id, glyphCount);
            endGatherMode();
            //dispatch glyphs
            vectors = null;
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class EditField extends SaxHandlerBase.ContentElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            super.startElement(atts);
            color = null;
        }


        /**
         *  Description of the Method
         *
         *@exception  Exception  Description of the Exception
         */
        public void endElement() throws Exception {
            int id = newId(attrs.getValue("", "id"));

            String name = getAttr(attrs, "name", "");
            String text = attrs.getValue("", "text");

            double minx = getAttrDouble(attrs, "min-x", 0.0);
            double miny = getAttrDouble(attrs, "min-y", 0.0);
            double maxx = getAttrDouble(attrs, "max-x", 0.0);
            double maxy = getAttrDouble(attrs, "max-y", 0.0);

            Rect rect = new Rect((int) (minx * SWFConstants.TWIPS),
                    (int) (miny * SWFConstants.TWIPS),
                    (int) (maxx * SWFConstants.TWIPS),
                    (int) (maxy * SWFConstants.TWIPS));

            String align = getAttr(attrs, "align", "left").trim();
            int alignment = SWFConstants.TEXTFIELD_ALIGN_LEFT;
            if (align.equals("right")) {
                alignment = SWFConstants.TEXTFIELD_ALIGN_RIGHT;
            }
            if (align.equals("center")) {
                alignment = SWFConstants.TEXTFIELD_ALIGN_CENTER;
            }
            if (align.equals("justify")) {
                alignment = SWFConstants.TEXTFIELD_ALIGN_JUSTIFY;
            }

            int fontId = getId(attrs.getValue("", "font"));
            int fontSize = (int) (getAttrDouble(attrs, "size", 12.0) * SWFConstants.TWIPS);
            int limit = getAttrInt(attrs, "limit", 0);
            int leftMargin = (int) (getAttrDouble(attrs, "left-margin", 12.0) * SWFConstants.TWIPS);
            int rightMargin = (int) (getAttrDouble(attrs, "right-margin", 12.0) * SWFConstants.TWIPS);
            int indentation = (int) (getAttrDouble(attrs, "indentation", 12.0) * SWFConstants.TWIPS);
            int lineSpacing = (int) (getAttrDouble(attrs, "linespacing", 12.0) * SWFConstants.TWIPS);

            int flags = (text != null) ? SWFConstants.TEXTFIELD_HAS_TEXT : 0;
            if (!getAttrBool(attrs, "selectable", true)) {
                flags |= SWFConstants.TEXTFIELD_NO_SELECTION;
            }
            if (getAttrBool(attrs, "border", true)) {
                flags |= SWFConstants.TEXTFIELD_DRAW_BORDER;
            }
            if (getAttrBool(attrs, "html", false)) {
                flags |= SWFConstants.TEXTFIELD_HTML;
            }
            if (!getAttrBool(attrs, "system-font", false)) {
                flags |= SWFConstants.TEXTFIELD_FONT_GLYPHS;
            }
            if (getAttrBool(attrs, "wordwrap", false)) {
                flags |= SWFConstants.TEXTFIELD_WORD_WRAP;
            }
            if (getAttrBool(attrs, "multiline", false)) {
                flags |= SWFConstants.TEXTFIELD_IS_MULTILINE;
            }
            if (getAttrBool(attrs, "password", false)) {
                flags |= SWFConstants.TEXTFIELD_IS_PASSWORD;
            }
            if (getAttrBool(attrs, "read-only", false)) {
                flags |= SWFConstants.TEXTFIELD_DISABLE_EDIT;
            }

            if (color == null) {
                color = new AlphaColor(0, 0, 0, 0);
            } else if (!(color instanceof AlphaColor)) {
                color = new AlphaColor(color, 255);
            }

            tags.tagDefineTextField(id, name, text, rect, flags, (AlphaColor) color,
                    alignment, fontId, fontSize, limit,
                    leftMargin, rightMargin, indentation,
                    lineSpacing);
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class Button extends SaxHandlerBase.GatheringElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            super.startElement(atts);

            buttonRecords.clear();

            startGatherMode(this);
        }


        /**
         *  Description of the Method
         *
         *@exception  Exception  Description of the Exception
         */
        public void endElement() throws Exception {
            int id = newId(attrs.getValue("", "id"));
            boolean menu = getAttrBool(attrs, "menu", false);

            dispatchAllMatchingGatheredElements("layer");

            actions = tags.tagDefineButton2(id, menu, buttonRecords);

            if (actions != null) {
                actionMode = 1;
                endGatherMode();
                //dispatch any actions
                actions.done();
                actionMode = 0;
            }

            buttonRecords.clear();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class Layer extends SaxHandlerBase.ContentElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            super.startElement(atts);
            cxform = null;
            matrix = null;
        }


        /**
         *  Description of the Method
         *
         *@exception  Exception  Description of the Exception
         */
        public void endElement() throws Exception {
            int id = getId(attrs.getValue("", "id"));
            int depth = getAttrInt(attrs, "depth", 1);
            String fors = getAttr(attrs, "for", "hit over up down");

            int flags = 0;
            StringTokenizer tok = new StringTokenizer(fors);
            while (tok.hasMoreTokens()) {
                String token = tok.nextToken();

                if (token.equals("hit")) {
                    flags |= ButtonRecord.BUTTON_HITTEST;
                } else if (token.equals("over")) {
                    flags |= ButtonRecord.BUTTON_OVER;
                } else if (token.equals("up")) {
                    flags |= ButtonRecord.BUTTON_UP;
                } else if (token.equals("down")) {
                    flags |= ButtonRecord.BUTTON_DOWN;
                }
            }

            ButtonRecord2 rec = new ButtonRecord2(id, depth, matrix, cxform, flags);
            buttonRecords.add(rec);
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class Morph extends SaxHandlerBase.GatheringElementType {
        /**
         *  Description of the Field
         */
        protected Rect startBounds;
        /**
         *  Description of the Field
         */
        protected Rect endBounds;


        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            super.startElement(atts);

            startBounds = null;
            endBounds = null;

            startGatherMode(this);
        }


        /**
         *  Description of the Method
         *
         *@param  localName  Description of the Parameter
         *@param  atts       Description of the Parameter
         *@return            Description of the Return Value
         */
        public boolean gatherElement(String localName, Attributes atts) {
            if (localName.equals("shape")) {
                double minx = getAttrDouble(atts, "min-x", 0.0);
                double miny = getAttrDouble(atts, "min-y", 0.0);
                double maxx = getAttrDouble(atts, "max-x", 0.0);
                double maxy = getAttrDouble(atts, "max-y", 0.0);

                Rect rect = new Rect((int) (minx * SWFConstants.TWIPS),
                        (int) (miny * SWFConstants.TWIPS),
                        (int) (maxx * SWFConstants.TWIPS),
                        (int) (maxy * SWFConstants.TWIPS));

                if (startBounds == null) {
                    startBounds = rect;
                } else {
                    endBounds = rect;
                }
            }

            return true;
        }


        /**
         *  Description of the Method
         *
         *@exception  Exception  Description of the Exception
         */
        public void endElement() throws Exception {
            int id = newId(attrs.getValue("", "id"));

            shape = tags.tagDefineMorphShape(id, startBounds, endBounds);
            vectors = shape;

            endGatherMode();
            //dispatch shapes

            shape = null;
            vectors = null;
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class EditFont extends SaxHandlerBase.GatheringElementType {
        /**
         *  Description of the Field
         */
        protected int glyphCount;
        /**
         *  Description of the Field
         */
        protected List codes = new ArrayList();
        /**
         *  Description of the Field
         */
        protected List advances = new ArrayList();
        /**
         *  Description of the Field
         */
        protected List bounds = new ArrayList();
        /**
         *  Description of the Field
         */
        protected List kerns = new ArrayList();
        /**
         *  Description of the Field
         */
        protected boolean hasLayout;


        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            super.startElement(atts);

            codes.clear();
            advances.clear();
            bounds.clear();
            kerns.clear();
            hasLayout = false;

            glyphCount = 0;
            startGatherMode(this);
        }


        /**
         *  Description of the Method
         *
         *@param  localName  Description of the Parameter
         *@param  atts       Description of the Parameter
         *@return            Description of the Return Value
         */
        public boolean gatherElement(String localName, Attributes atts) {
            if (localName.equals("glyph")) {
                glyphCount++;

                double minx = getAttrDouble(atts, "min-x", 0.0);
                double miny = getAttrDouble(atts, "min-y", 0.0);
                double maxx = getAttrDouble(atts, "max-x", 0.0);
                double maxy = getAttrDouble(atts, "max-y", 0.0);

                Rect rect = new Rect((int) (minx * SWFConstants.TWIPS),
                        (int) (miny * SWFConstants.TWIPS),
                        (int) (maxx * SWFConstants.TWIPS),
                        (int) (maxy * SWFConstants.TWIPS));

                bounds.add(rect);

                int code = getAttrInt(atts, "code", 0);
                int advance = (int) (getAttrDouble(atts, "advance", 0.0) *
                        SWFConstants.TWIPS);

                if (code > 0 && advance != 0) {
                    hasLayout = true;
                }

                codes.add(new Integer(code));
                advances.add(new Integer(advance));
            } else if (localName.equals("kerning")) {
                hasLayout = true;

                int[] info = new int[]
                        {
                        getAttrInt(atts, "code-1", 0),
                        getAttrInt(atts, "code-2", 0),
                        (int) (getAttrDouble(atts, "offset", 0.0) * SWFConstants.TWIPS)
                        };

                kerns.add(info);

                return false;
            }

            return true;
        }


        /**
         *  Description of the Method
         *
         *@exception  Exception  Description of the Exception
         */
        public void endElement() throws Exception {
            int id = newId(attrs.getValue("", "id"));

            int flags = 0;
            String f = getAttr(attrs, "flags", "");

            StringTokenizer tok = new StringTokenizer(f);
            while (tok.hasMoreTokens()) {
                String t = tok.nextToken();

                if (t.equals("unicode")) {
                    flags |= SWFConstants.FONT2_UNICODE;
                }
                if (t.equals("shiftjis")) {
                    flags |= SWFConstants.FONT2_SHIFTJIS;
                }
                if (t.equals("ansi")) {
                    flags |= SWFConstants.FONT2_ANSI;
                }
                if (t.equals("italic")) {
                    flags |= SWFConstants.FONT2_ITALIC;
                }
                if (t.equals("bold")) {
                    flags |= SWFConstants.FONT2_BOLD;
                }
            }

            String name = getAttr(attrs, "name", "unknown");
            int ascent = (int) (getAttrDouble(attrs, "ascent", 0.0) * SWFConstants.TWIPS);
            int descent = (int) (getAttrDouble(attrs, "descent", 0.0) * SWFConstants.TWIPS);
            int leading = (int) (getAttrDouble(attrs, "leading", 0.0) * SWFConstants.TWIPS);

            int[] advancesA = new int[glyphCount];
            int[] codesA = new int[glyphCount];
            Rect[] boundsA = new Rect[glyphCount];

            for (int i = 0; i < glyphCount; i++) {
                advancesA[i] = ((Integer) advances.get(i)).intValue();
                codesA[i] = ((Integer) codes.get(i)).intValue();
                boundsA[i] = (Rect) bounds.get(i);
            }

            int[] kern1 = new int[kerns.size()];
            int[] kern2 = new int[kerns.size()];
            int[] kernA = new int[kerns.size()];

            for (int i = 0; i < kern1.length; i++) {
                int[] info = (int[]) kerns.get(i);
                kern1[i] = info[0];
                kern2[i] = info[1];
                kernA[i] = info[2];
            }

            if (ascent > 0 || descent > 0 || leading > 0 || hasLayout) {
                flags |= SWFConstants.FONT2_HAS_LAYOUT;
            }

            vectors = tags.tagDefineFont2(id, flags, name, glyphCount,
                    ascent, descent, leading,
                    codesA, advancesA, boundsA, kern1,
                    kern2, kernA);

            endGatherMode();
            //dispatch glyphs

            if (glyphCount == 0) {
                vectors.done();
            }
            vectors = null;
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class Glyph extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception { }


        /**
         *  Description of the Method
         *
         *@exception  Exception  Description of the Exception
         */
        public void endElement() throws Exception {
            vectors.done();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class Anticlockwise extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@exception  Exception  Description of the Exception
         */
        public void endElement() throws Exception {
            SWFShape shape = (vectors instanceof SWFShape) ?
                    (SWFShape) vectors :
                    null;

            if (shape != null) {
                shape.setFillStyle0(1);
            }
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class FontInfo extends SaxHandlerBase.ContentElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            super.startElement(atts);
            codes = new ArrayList();
        }


        /**
         *  Description of the Method
         *
         *@exception  Exception  Description of the Exception
         */
        public void endElement() throws Exception {
            int id = getId(attrs.getValue("", "id"));

            String fs = attrs.getValue("", "flags");
            String name = attrs.getValue("", "name");

            if (name == null) {
                throw new SAXException("Missing name in font-info");
            }

            if (fs == null) {
                fs = "";
            }

            int flags = 0;

            StringTokenizer tok = new StringTokenizer(fs);
            while (tok.hasMoreTokens()) {
                String s = tok.nextToken();

                if (s.equalsIgnoreCase("unicode")) {
                    flags += SWFConstants.FONT_UNICODE;
                } else if (s.equalsIgnoreCase("shiftjis")) {
                    flags += SWFConstants.FONT_SHIFTJIS;
                } else if (s.equalsIgnoreCase("ansi")) {
                    flags += SWFConstants.FONT_ANSI;
                } else if (s.equalsIgnoreCase("italic")) {
                    flags += SWFConstants.FONT_ITALIC;
                } else if (s.equalsIgnoreCase("bold")) {
                    flags += SWFConstants.FONT_BOLD;
                }
            }

            int[] codesA = new int[codes.size()];
            for (int i = 0; i < codesA.length; i++) {
                codesA[i] = ((Integer) codes.get(i)).intValue();
            }
            codes = null;

            tags.tagDefineFontInfo(id, name, flags, codesA);
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class Code extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            codes.add(new Integer(getAttrInt(atts, "code", 0)));
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class Text extends SaxHandlerBase.GatheringElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            super.startElement(atts);
            startGatherMode(this);
        }


        /**
         *  Description of the Method
         *
         *@exception  Exception  Description of the Exception
         */
        public void endElement() throws Exception {
            int id = newId(attrs.getValue("", "id"));

            boolean hasAlpha = getAttrBool(attrs, "has-alpha", false);
            double minx = getAttrDouble(attrs, "min-x", 0.0);
            double miny = getAttrDouble(attrs, "min-y", 0.0);
            double maxx = getAttrDouble(attrs, "max-x", 0.0);
            double maxy = getAttrDouble(attrs, "max-y", 0.0);

            Rect rect = new Rect((int) (minx * SWFConstants.TWIPS),
                    (int) (miny * SWFConstants.TWIPS),
                    (int) (maxx * SWFConstants.TWIPS),
                    (int) (maxy * SWFConstants.TWIPS));

            //--need to get the matrix from the front of the gathered elements
            dispatchGatheredElement("matrix");

            text = hasAlpha ?
                    tags.tagDefineText2(id, rect, matrix) :
                    tags.tagDefineText(id, rect, matrix);

            chars = new ArrayList();

            endGatherMode();
            //dispatch the gathered elements

            flushChars();
            chars = null;

            text.done();
            text = null;
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  Exception  Description of the Exception
     */
    protected void flushChars() throws Exception {
        if (chars.isEmpty()) {
            return;
        }

        int[] indices = new int[chars.size()];
        int[] advances = new int[indices.length];

        for (int i = 0; i < indices.length; i++) {
            int[] ii = (int[]) chars.get(i);
            indices[i] = ii[0];
            advances[i] = ii[1];
        }

        text.text(indices, advances);
        chars.clear();
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class SetFont extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            flushChars();

            int id = getId(atts.getValue("", "id"));
            double size = getAttrDouble(atts, "size", 0.0);

            if (id < 1) {
                throw new SAXException("Invalid id for font in text");
            }

            text.font(id, (int) (size * SWFConstants.TWIPS));
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class SetX extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            flushChars();

            double x = getAttrDouble(atts, "x", 0.0);

            text.setX((int) (x * SWFConstants.TWIPS));
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class SetY extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            flushChars();

            double y = getAttrDouble(atts, "y", 0.0);

            text.setY((int) (y * SWFConstants.TWIPS));
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class Char extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            double adv = getAttrDouble(atts, "advance", 0.0);
            int idx = getAttrInt(atts, "glyph-index", 0);

            chars.add(new int[]{idx, (int) (adv * SWFConstants.TWIPS)});
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class QTMovie extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            int id = newId(atts.getValue("", "id"));
            String name = atts.getValue("", "name");

            if (name == null) {
                throw new SAXException("Missing name for QuickTime movie");
            }
            if (id < 1) {
                throw new SAXException("Invalid id for QuickTime movie");
            }

            tags.tagDefineQuickTimeMovie(id, name);
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ColorElem extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            int red = getAttrInt(atts, "red", 0);
            int green = getAttrInt(atts, "green", 0);
            int blue = getAttrInt(atts, "blue", 0);
            int alpha = getAttrInt(atts, "alpha", 255);

            if (alpha != 255) {
                color = new AlphaColor(red, green, blue, alpha);
            } else {
                color = new Color(red, green, blue);
            }

            if (bitmapColors != null) {
                bitmapColors.add(color);
                color = null;
            }

            if (text != null) {
                flushChars();
                text.color(color);
                color = null;
            }
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class MatrixElem extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            double skew0 = getAttrDouble(atts, "skew0", 0.0);
            double skew1 = getAttrDouble(atts, "skew1", 0.0);
            double scalex = getAttrDouble(atts, "scale-x", 1.0);
            double scaley = getAttrDouble(atts, "scale-y", 1.0);
            double x = getAttrDouble(atts, "x", 0.0);
            double y = getAttrDouble(atts, "y", 0.0);

            matrix = new Matrix(scalex, scaley, skew0, skew1,
                    x * SWFConstants.TWIPS,
                    y * SWFConstants.TWIPS);
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class CXFormElem extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            int addRed = getAttrInt(atts, "add-red", 0);
            int addGreen = getAttrInt(atts, "add-green", 0);
            int addBlue = getAttrInt(atts, "add-blue", 0);
            int addAlpha = getAttrInt(atts, "add-alpha", 0);

            double multRed = getAttrDouble(atts, "mult-red", 1.0);
            double multGreen = getAttrDouble(atts, "mult-green", 1.0);
            double multBlue = getAttrDouble(atts, "mult-blue", 1.0);
            double multAlpha = getAttrDouble(atts, "mult-alpha", 1.0);

            cxform = new AlphaTransform(multRed, multGreen, multBlue, multAlpha,
                    addRed, addGreen, addBlue, addAlpha);
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class Actions extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            if (actionMode == 0) {
                actions = tags.tagDoAction();
            }

            int actionFlags = 0;

            if (actionMode == 1) {
                //button

                String flags = getAttr(atts, "conditions", "release");

                StringTokenizer tok = new StringTokenizer(flags);
                while (tok.hasMoreTokens()) {
                    String f = tok.nextToken();

                    if (f.equals("menu-drag-out")) {
                        actionFlags |= SWFConstants.BUTTON2_OVERDOWN2IDLE;
                    } else if (f.equals("menu-drag-over")) {
                        actionFlags |= SWFConstants.BUTTON2_IDLE2OVERDOWN;
                    } else if (f.equals("release-outside")) {
                        actionFlags |= SWFConstants.BUTTON2_OUTDOWN2IDLE;
                    } else if (f.equals("drag-over")) {
                        actionFlags |= SWFConstants.BUTTON2_OUTDOWN2OVERDOWN;
                    } else if (f.equals("drag-out")) {
                        actionFlags |= SWFConstants.BUTTON2_OVERDOWN2OUTDOWN;
                    } else if (f.equals("release")) {
                        actionFlags |= SWFConstants.BUTTON2_OVERDOWN2OVERUP;
                    } else if (f.equals("press")) {
                        actionFlags |= SWFConstants.BUTTON2_OVERUP2OVERDOWN;
                    } else if (f.equals("roll-out")) {
                        actionFlags |= SWFConstants.BUTTON2_OVERUP2IDLE;
                    } else if (f.equals("roll-over")) {
                        actionFlags |= SWFConstants.BUTTON2_IDLE2OVERUP;
                    }
                }

                int charCode = getAttrInt(atts, "char-code", 0);

                if (charCode > 0) {
                    actionFlags |= (charCode << 9) & 0xfe00;
                }
            } else if (actionMode == 2) {
                //clip actions

                String flags = getAttr(atts, "conditions", "");

                StringTokenizer tok = new StringTokenizer(flags);
                while (tok.hasMoreTokens()) {
                    String f = tok.nextToken();

                    if (f.equals("load")) {
                        actionFlags |= SWFConstants.CLIP_ACTION_ON_LOAD;
                    } else if (f.equals("enter-frame")) {
                        actionFlags |= SWFConstants.CLIP_ACTION_ENTER_FRAME;
                    } else if (f.equals("unload")) {
                        actionFlags |= SWFConstants.CLIP_ACTION_UNLOAD;
                    } else if (f.equals("mouse-move")) {
                        actionFlags |= SWFConstants.CLIP_ACTION_MOUSE_MOVE;
                    } else if (f.equals("mouse-down")) {
                        actionFlags |= SWFConstants.CLIP_ACTION_MOUSE_DOWN;
                    } else if (f.equals("mouse-up")) {
                        actionFlags |= SWFConstants.CLIP_ACTION_MOUSE_UP;
                    } else if (f.equals("key-down")) {
                        actionFlags |= SWFConstants.CLIP_ACTION_KEY_DOWN;
                    } else if (f.equals("key-up")) {
                        actionFlags |= SWFConstants.CLIP_ACTION_KEY_UP;
                    } else if (f.equals("data")) {
                        actionFlags |= SWFConstants.CLIP_ACTION_DATA;
                    }
                }
            }

            actions.start(actionFlags);
        }


        /**
         *  Description of the Method
         *
         *@exception  Exception  Description of the Exception
         */
        public void endElement() throws Exception {
            actions.end();
            if (actionMode == 0) {
                actions.done();
            }
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionUnknown extends SaxHandlerBase.ContentElementType {
        /**
         *  Description of the Method
         *
         *@exception  Exception  Description of the Exception
         */
        public void endElement() throws Exception {
            int code = getAttrInt(attrs, "code", 0);
            byte[] data = Base64.decode(buff.toString());
            buff = null;

            actions.unknown(code, data);
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionJumpLabel extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.jumpLabel(getAttr(atts, "label", ""));
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionComment extends SaxHandlerBase.ContentElementType {
        /**
         *  Description of the Method
         *
         *@exception  Exception  Description of the Exception
         */
        public void endElement() throws Exception {
            actions.comment(buff.toString());
            buff = null;
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionGotoFrame extends SaxHandlerBase.ContentElementType {
        /**
         *  Description of the Method
         *
         *@exception  Exception  Description of the Exception
         */
        public void endElement() throws Exception {
            String label = attrs.getValue("", "label");
            if (label != null) {
                actions.gotoFrame(label);
                return;
            }

            int number = getAttrInt(attrs, "number", -1);
            if (number >= 0) {
                actions.gotoFrame(number);
                return;
            }

            boolean play = getAttrBool(attrs, "play", true);
            actions.gotoFrame(play);
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionGetUrl extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            String url = atts.getValue("", "url");
            String target = atts.getValue("", "target");

            if (url != null || target != null) {
                if (url == null) {
                    url = "";
                }
                if (target == null) {
                    target = "";
                }

                actions.getURL(url, target);
                return;
            }

            String sendVars = getAttr(atts, "send-vars", "none");
            boolean targetSprite = getAttrBool(atts, "target-sprite", false);
            String loadVars = atts.getValue("", "load-vars-into");

            int send = SWFActions.GET_URL_SEND_VARS_POST;
            if (sendVars.equalsIgnoreCase("post")) {
                send = SWFActions.GET_URL_SEND_VARS_POST;
            } else if (sendVars.equalsIgnoreCase("get")) {
                send = SWFActions.GET_URL_SEND_VARS_GET;
            }

            int load = 0;

            if (loadVars != null) {
                load = SWFActions.GET_URL_MODE_LOAD_VARS_INTO_LEVEL;

                if (loadVars.equalsIgnoreCase("sprite")) {
                    load = SWFActions.GET_URL_MODE_LOAD_VARS_INTO_SPRITE;
                }
            } else {
                load = targetSprite ?
                        SWFActions.GET_URL_MODE_LOAD_MOVIE_INTO_SPRITE :
                        SWFActions.GET_URL_MODE_LOAD_MOVIE_INTO_LEVEL;
            }

            actions.getURL(send, load);
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionNextFrame extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.nextFrame();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionPrevFrame extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.prevFrame();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionPlay extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.play();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionStop extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.stop();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionToggleQuality extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.toggleQuality();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionStopSounds extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.stopSounds();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionWaitForFrame extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            int number = getAttrInt(atts, "number", -1);
            String jumpLabel = getAttr(atts, "jump-label", "");

            if (number >= 0) {
                actions.waitForFrame(number, jumpLabel);
            } else {
                actions.waitForFrame(jumpLabel);
            }
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionSetTarget extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            String target = atts.getValue("", "target");

            if (target != null) {
                actions.setTarget(target);
            } else {
                actions.setTarget();
            }
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionPush extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            String value = atts.getValue("", "string");
            if (value != null) {
                actions.push(value);
                return;
            }

            value = atts.getValue("", "float");
            if (value != null) {
                actions.push(Float.parseFloat(value));
                return;
            }

            value = atts.getValue("", "double");
            if (value != null) {
                actions.push(Double.parseDouble(value));
                return;
            }

            value = atts.getValue("", "register");
            if (value != null) {
                actions.pushRegister(Integer.parseInt(value));
                return;
            }

            value = atts.getValue("", "boolean");
            if (value != null) {
                actions.push((value.equalsIgnoreCase("yes") ||
                        value.equalsIgnoreCase("true")) ? true : false);
                return;
            }

            value = atts.getValue("", "int");
            if (value != null) {
                actions.push(Integer.parseInt(value));
                return;
            }

            value = atts.getValue("", "lookup");
            if (value != null) {
                actions.lookup(Integer.parseInt(value));
                return;
            }

            actions.pushNull();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionPop extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.pop();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionAdd extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.add();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionSubtract extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.substract();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionMultiply extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.multiply();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionDivide extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.divide();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionEquals extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.equals();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionLessThan extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.lessThan();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionAnd extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.and();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionOr extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.or();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionNot extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.not();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionStringEquals extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.stringEquals();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionStringLength extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.stringLength();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionConcat extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.concat();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionSubstring extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.substring();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionStringLessThan extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.stringLessThan();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionMutlibyteStringLength extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.stringLengthMB();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionMultibyteSubstring extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.substringMB();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionToInteger extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.toInteger();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionCharToAscii extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.charToAscii();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionAsciiToChar extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.asciiToChar();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionMutlibyteCharToAscii extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.charMBToAscii();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionAsciiToMultibyteChar extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.asciiToCharMB();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionCall extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.call();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionGetVariable extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.getVariable();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionSetVariable extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.setVariable();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionGetProperty extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.getProperty();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionSetProperty extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.setProperty();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionCloneSprite extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.cloneSprite();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionRemoveSprite extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.removeSprite();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionStartDrag extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.startDrag();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionEndDrag extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.endDrag();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionTrace extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.trace();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionGetTime extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.getTime();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionRandomNumber extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.randomNumber();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionCallFunction extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.callFunction();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionCallMethod extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.callMethod();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionDefineLocalValue extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.defineLocalValue();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionDefineLocal extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.defineLocal();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionDeleteProperty extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.deleteProperty();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionDeleteThreadVars extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.deleteThreadVars();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionEnumerate extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.enumerate();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionTypedEquals extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.typedEquals();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionGetMember extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.getMember();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionInitArray extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.initArray();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionInitObject extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.initObject();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionNewMethod extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.newMethod();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionNewObject extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.newObject();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionSetMember extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.setMember();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionGetTargetPath extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.getTargetPath();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionToNumber extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.convertToNumber();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionToString extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.convertToString();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionTypeOf extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.typeOf();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionTypedAdd extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.typedAdd();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionTypedLessThan extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.typedLessThan();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionModulo extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.modulo();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionBitAnd extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.bitAnd();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionBitOr extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.bitOr();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionBitXor extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.bitXor();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionShiftLeft extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.shiftLeft();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionShiftRight extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.shiftRight();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionShiftRightUnsigned extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.shiftRightUnsigned();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionDecrement extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.decrement();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionIncrement extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.increment();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionDuplicate extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.duplicate();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionReturn extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.returnValue();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionSwap extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.swap();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionLookupTable extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            lookupValues.clear();
        }


        /**
         *  Description of the Method
         *
         *@exception  Exception  Description of the Exception
         */
        public void endElement() throws Exception {
            String[] values = new String[lookupValues.size()];
            lookupValues.toArray(values);
            lookupValues.clear();
            actions.lookupTable(values);
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class LookupValue extends SaxHandlerBase.ContentElementType {
        /**
         *  Description of the Method
         *
         *@exception  Exception  Description of the Exception
         */
        public void endElement() throws Exception {
            lookupValues.add(buff.toString());
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionJump extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.jump(getAttr(atts, "jump-label", ""));
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionIf extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.ifJump(getAttr(atts, "jump-label", ""));
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionStore extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.storeInRegister(getAttrInt(atts, "register", 0));
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionWith extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            actions.startWith();
        }


        /**
         *  Description of the Method
         *
         *@exception  Exception  Description of the Exception
         */
        public void endElement() throws Exception {
            actions.endBlock();
        }
    }


    /**
     *  Description of the Class
     *
     *@author     unknown
     *@created    15 de Setembro de 2002
     */
    protected class ActionFunction extends SaxHandlerBase.ElementType {
        /**
         *  Description of the Method
         *
         *@param  atts           Description of the Parameter
         *@exception  Exception  Description of the Exception
         */
        public void startElement(Attributes atts) throws Exception {
            String name = getAttr(atts, "name", "");
            String parms = getAttr(atts, "params", "");

            StringTokenizer tok = new StringTokenizer(parms);
            Vector p = new Vector();
            while (tok.hasMoreTokens()) {
                p.add(tok.nextToken());
            }

            String[] params = new String[p.size()];
            p.copyInto(params);

            actions.startFunction(name, params);
        }


        /**
         *  Description of the Method
         *
         *@exception  Exception  Description of the Exception
         */
        public void endElement() throws Exception {
            actions.endBlock();
        }
    }
}
