package pt.tumba.parser.swf;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

/**
 *  An implementation of the SWFTagTypes interface that produces a debug dump
 *
 *@author     unknown
 *@created    15 de Setembro de 2002
 */
public class SWFTagDumper implements SWFTagTypes, SWFShape, SWFText {
    /**
     *  Description of the Field
     */
    protected PrintWriter writer;
    /**
     *  Description of the Field
     */
    protected String dashes = "---------------";
    /**
     *  Description of the Field
     */
    protected boolean dumpHex;
    /**
     *  Description of the Field
     */
    protected String indent = "";
    /**
     *  Description of the Field
     */
    protected boolean decompileActions = false;


    /**
     *  Dump to System.out
     *
     *@param  dumpHex           if true then binary data will dumped as hex -
     *      otherwise skipped
     *@param  decompileActions  Description of the Parameter
     */
    public SWFTagDumper(boolean dumpHex, boolean decompileActions) {
        this(System.out, dumpHex, decompileActions);
    }


    /**
     *  Dump to the given output stream
     *
     *@param  dumpHex           if true then binary data will dumped as hex -
     *      otherwise skipped
     *@param  out               Description of the Parameter
     *@param  decompileActions  Description of the Parameter
     */
    public SWFTagDumper(OutputStream out,
            boolean dumpHex,
            boolean decompileActions) {
        writer = new PrintWriter(out);
        this.dumpHex = dumpHex;
        this.decompileActions = decompileActions;
    }


    /**
     *  Constructor for the SWFTagDumper object
     *
     *@param  writer            Description of the Parameter
     *@param  dumpHex           Description of the Parameter
     *@param  decompileActions  Description of the Parameter
     */
    public SWFTagDumper(PrintWriter writer,
            boolean dumpHex,
            boolean decompileActions) {
        this.writer = writer;
        this.dumpHex = dumpHex;
        this.decompileActions = decompileActions;
    }


    /**
     *  Description of the Method
     *
     *@param  line  Description of the Parameter
     */
    protected void println(String line) {
        writer.println(indent + line);
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
        println("Tag " + tagType + " length=" + contents.length);

        if (dumpHex) {
            Hex.dump(writer, contents, 0L, indent + "    ", false);
            println(dashes);
        }
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
        println("header: version=" + version +
                " length=" + length + " width=" + twipsWidth +
                " height=" + twipsHeight + " rate=" + frameRate +
                " frame-count=" + frameCount);
    }


    /**
     *  SWFTagTypes interface
     *
     *@exception  IOException  Description of the Exception
     */
    public void tagEnd() throws IOException {
        println("end");
        println(dashes);
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  soundId          Description of the Parameter
     *@param  info             Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagStartSound(int soundId, SoundInfo info) throws IOException {
        println("start-sound id=" + soundId + " " + info);
    }


    /**
     *  SWFTagTypes interface
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
            int averageSampleCount) throws IOException {
        printSoundStreamHead("sound-stream-head",
                playbackFrequency, playback16bit, playbackStereo,
                streamFormat, streamFrequency, stream16bit, streamStereo,
                averageSampleCount);
    }


    /**
     *  SWFTagTypes interface
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
            int averageSampleCount) throws IOException {
        printSoundStreamHead("sound-stream-head-2",
                playbackFrequency, playback16bit, playbackStereo,
                streamFormat, streamFrequency, stream16bit, streamStereo,
                averageSampleCount);
    }


    /**
     *  Description of the Method
     *
     *@param  name                Description of the Parameter
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
    public void printSoundStreamHead(String name,
            int playbackFrequency, boolean playback16bit, boolean playbackStereo,
            int streamFormat, int streamFrequency, boolean stream16bit, boolean streamStereo,
            int averageSampleCount) throws IOException {
        String playFreq = "5.5";
        if (playbackFrequency == SWFConstants.SOUND_FREQ_11KHZ) {
            playFreq = "11";
        }
        if (playbackFrequency == SWFConstants.SOUND_FREQ_22KHZ) {
            playFreq = "22";
        }
        if (playbackFrequency == SWFConstants.SOUND_FREQ_44KHZ) {
            playFreq = "44";
        }

        String streamFreq = "5.5";
        if (streamFrequency == SWFConstants.SOUND_FREQ_11KHZ) {
            streamFreq = "11";
        }
        if (streamFrequency == SWFConstants.SOUND_FREQ_22KHZ) {
            streamFreq = "22";
        }
        if (streamFrequency == SWFConstants.SOUND_FREQ_44KHZ) {
            streamFreq = "44";
        }

        String format = "RawSamples";
        if (streamFormat == SWFConstants.SOUND_FORMAT_ADPCM) {
            format = "ADPCM";
        }
        if (streamFormat == SWFConstants.SOUND_FORMAT_MP3) {
            format = "MP3";
        }

        println(name + " play at " + playFreq + "kHz stereo=" + playbackStereo +
                " 16bit=" + playback16bit + " | Stream at " + streamFreq +
                "kHz format=" + format + " stereo=" + streamStereo +
                " 16bit=" + stream16bit + " Avg-Samples=" + averageSampleCount);
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  soundData        Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagSoundStreamBlock(byte[] soundData) throws IOException {
        println("sound-stream-block");

        if (dumpHex) {
            Hex.dump(writer, soundData, 0L, indent + "    ", false);
            println(dashes);
        }
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  serialNumber     Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagSerialNumber(String serialNumber) throws IOException {
        println("serial number =" + serialNumber);
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  data             Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagGenerator(byte[] data) throws IOException {
        println("generator tag");

        if (dumpHex) {
            Hex.dump(writer, data, 0L, indent + "    ", false);
            println(dashes);
        }
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  data             Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagGeneratorText(byte[] data) throws IOException {
        println("generator text");

        if (dumpHex) {
            Hex.dump(writer, data, 0L, indent + "    ", false);
            println(dashes);
        }
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  data             Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagGeneratorFont(byte[] data) throws IOException {
        println("generator font");

        if (dumpHex) {
            Hex.dump(writer, data, 0L, indent + "    ", false);
            println(dashes);
        }
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  data             Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagGeneratorCommand(byte[] data) throws IOException {
        println("generator command");

        if (dumpHex) {
            Hex.dump(writer, data, 0L, indent + "    ", false);
            println(dashes);
        }
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  data             Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagNameCharacter(byte[] data) throws IOException {
        println("generator name character");

        if (dumpHex) {
            Hex.dump(writer, data, 0L, indent + "    ", false);
            println(dashes);
        }
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  id               Description of the Parameter
     *@param  imageData        Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagDefineBits(int id, byte[] imageData) throws IOException {
        println("jpeg bits");

        if (dumpHex) {
            Hex.dump(writer, imageData, 0L, indent + "    ", false);
            println(dashes);
        }
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  jpegEncodingData  Description of the Parameter
     *@exception  IOException   Description of the Exception
     */
    public void tagJPEGTables(byte[] jpegEncodingData) throws IOException {
        println("jpeg encoding data");

        if (dumpHex) {
            Hex.dump(writer, jpegEncodingData, 0L, indent + "    ", false);
            println(dashes);
        }
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  id               Description of the Parameter
     *@param  imageData        Description of the Parameter
     *@param  alphaData        Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagDefineBitsJPEG3(int id, byte[] imageData, byte[] alphaData) throws IOException {
        println("jpeg with alpha");

        if (dumpHex) {
            Hex.dump(writer, imageData, 0L, indent + "    ", false);
            println("--- Alpha Channel follows ---");
            Hex.dump(writer, alphaData, 0L, indent + "    ", false);
            println(dashes);
        }
    }


    /**
     *  SWFTagTypes interface
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
            int sampleCount, byte[] soundData) throws IOException {
        String freq = "5.5";
        if (frequency == SWFConstants.SOUND_FREQ_11KHZ) {
            freq = "11";
        }
        if (frequency == SWFConstants.SOUND_FREQ_22KHZ) {
            freq = "22";
        }
        if (frequency == SWFConstants.SOUND_FREQ_44KHZ) {
            freq = "44";
        }

        String formatS = "RawSamples";
        if (format == SWFConstants.SOUND_FORMAT_ADPCM) {
            formatS = "ADPCM";
        }
        if (format == SWFConstants.SOUND_FORMAT_MP3) {
            formatS = "MP3";
        }

        println("define sound: id=" + id + " format=" + formatS +
                " freq=" + freq + "kHz 16bit=" + bits16 +
                " stereo=" + stereo + " samples=" + sampleCount);

        if (dumpHex) {
            Hex.dump(writer, soundData, 0L, indent + "    ", false);
            println(dashes);
        }
    }


    /**
     *  SWFTagTypes interface
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
             throws IOException {
        println("define button sound: id=" + buttonId);
        println("    roll-over sound=" + rollOverSoundId + " " + rollOverSoundInfo);
        println("    roll-out  sound=" + rollOutSoundId + " " + rollOutSoundInfo);
        println("    press     sound=" + pressSoundId + " " + pressSoundInfo);
        println("    release   sound=" + releaseSoundId + " " + releaseSoundInfo);
    }


    /**
     *  SWFTagTypes interface
     *
     *@exception  IOException  Description of the Exception
     */
    public void tagShowFrame() throws IOException {
        println("---------- frame ----------");
    }


    /**
     *  SWFTagTypes interface
     *
     *@return                  Description of the Return Value
     *@exception  IOException  Description of the Exception
     */
    public SWFActions tagDoAction() throws IOException {
        println("actions:");

        if (decompileActions) {
            return new Decompiler(writer, 1 + (indent.length() / 4));
        } else {
            ActionTextWriter acts = new ActionTextWriter(writer);
            acts.indent = "    " + indent;
            return acts;
        }
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
        println("shape id=" + id + "   " + outline);
        return this;
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
        println("shape2 id=" + id + "   " + outline);
        return this;
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
        println("shape3 id=" + id + "   " + outline);
        return this;
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  charId           Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagFreeCharacter(int charId) throws IOException {
        println("free character id=" + charId);
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
        println("place-object id=" + charId +
                " depth=" + depth + "  " + matrix + "  " + cxform);
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
        println("place-object2 move=" + isMove +
                " id=" + charId +
                " depth=" + depth +
                " clip=" + clipDepth +
                " ratio=" + ratio +
                " name=" + name +
                "  " + matrix + "  " + cxform);

        if (clipActionFlags != 0) {
            println("  clip-actions:");

            if (decompileActions) {
                return new Decompiler(writer, 1 + (indent.length() / 4));
            } else {
                ActionTextWriter acts = new ActionTextWriter(writer);
                acts.indent = "    " + indent;
                return acts;
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
        println("remove-object id=" + charId + " depth=" + depth);
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  depth            Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagRemoveObject2(int depth) throws IOException {
        println("remove-object2 depth=" + depth);
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  color            Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagSetBackgroundColor(Color color) throws IOException {
        println("background-color  " + color);
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  label            Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagFrameLabel(String label) throws IOException {
        println("frame-label " + label);
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  id               Description of the Parameter
     *@return                  Description of the Return Value
     *@exception  IOException  Description of the Exception
     */
    public SWFTagTypes tagDefineSprite(int id) throws IOException {
        println("sprite id=" + id);

        SWFTagDumper dumper = new SWFTagDumper(writer, dumpHex, decompileActions);
        dumper.indent = indent + "    ";
        return dumper;
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  password         Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagProtect(byte[] password) throws IOException {
        println("protect");
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  password         Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagEnableDebug(byte[] password) throws IOException {
        println("enable-debug");
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
        println("font id=" + id);
        return this;
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
        println("font-info id=" + fontId + " name=" + fontName +
                " flags=" + Integer.toBinaryString(flags) + " codes=" + codes.length);
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
        println("font2 id=" + id + " flags=" + Integer.toBinaryString(flags) +
                " name=" + name + " ascent=" + ascent + " descent=" + descent +
                " leading=" + leading + " has-kerns=" + (kernCodes1 != null));

        return this;
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
        println("edit-field id=" + fieldId + " name=" + fieldName +
                " text=" + initialText + " font=" + fontId + " size=" + fontSize +
                " chars=" + charLimit + " left=" + leftMargin +
                " right=" + rightMargin + " indent=" + indentation +
                " spacing=" + lineSpacing + " alignment=" + alignment +
                " flags=" + Integer.toBinaryString(flags) +
                " " + textColor);
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
        println("text id=" + id + " " + bounds + " " + matrix);
        return this;
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
        println("text2 id=" + id + " " + bounds + " " + matrix);
        return this;
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
        println("button id=" + id);

        for (Iterator enumerator = buttonRecords.iterator(); enumerator.hasNext(); ) {
            ButtonRecord rec = (ButtonRecord) enumerator.next();
            println("  " + rec);
        }

        println("  actions:");

        if (decompileActions) {
            return new Decompiler(writer, 1 + (indent.length() / 4));
        } else {
            ActionTextWriter acts = new ActionTextWriter(writer);
            acts.indent = "    " + indent;
            return acts;
        }
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
        println("button-cxform id=" + buttonId + "  " + transform);
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
        println("button2 id=" + id + " track-as-menu=" + trackAsMenu);

        for (Iterator enumerator = buttonRecord2s.iterator(); enumerator.hasNext(); ) {
            ButtonRecord2 rec = (ButtonRecord2) enumerator.next();
            println("  " + rec);
        }

        println("  actions:");

        if (decompileActions) {
            return new Decompiler(writer, 1 + (indent.length() / 4));
        } else {
            ActionTextWriter acts = new ActionTextWriter(writer);
            acts.indent = "    " + indent;
            return acts;
        }
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  names            Description of the Parameter
     *@param  ids              Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagExport(String[] names, int[] ids) throws IOException {
        println("export");

        for (int i = 0; i < names.length && i < ids.length; i++) {
            println("  id=" + ids[i] + " name=" + names[i]);
        }
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
        println("import library-movie=" + movieName);

        for (int i = 0; i < names.length && i < ids.length; i++) {
            println("  id=" + ids[i] + " name=" + names[i]);
        }
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  id               Description of the Parameter
     *@param  filename         Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagDefineQuickTimeMovie(int id, String filename) throws IOException {
        println("quicktime-movie id=" + id + " name=" + filename);
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  id               Description of the Parameter
     *@param  data             Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagDefineBitsJPEG2(int id, byte[] data) throws IOException {
        println("jpeg2 id=" + id);

        if (dumpHex) {
            Hex.dump(writer, data, 0L, indent + "    ", false);
            println(dashes);
        }
    }


    /**
     *  SWFTagTypes interface
     *
     *@param  id               Description of the Parameter
     *@param  jpegImage        Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void tagDefineBitsJPEG2(int id, InputStream jpegImage) throws IOException {
        println("jpeg2 id=" + id + " (from input stream)");
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
        println("morph-shape id=" + id + " start: " +
                startBounds + "  end: " + endBounds);
        return this;
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
        dumpBitsLossless("bits-lossless", id, format, width, height, colors, imageData);
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
        dumpBitsLossless("bits-lossless2", id, format, width, height, colors, imageData);
    }


    /**
     *  Description of the Method
     *
     *@param  name             Description of the Parameter
     *@param  id               Description of the Parameter
     *@param  format           Description of the Parameter
     *@param  width            Description of the Parameter
     *@param  height           Description of the Parameter
     *@param  colors           Description of the Parameter
     *@param  imageData        Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void dumpBitsLossless(String name, int id, int format,
            int width, int height,
            Color[] colors, byte[] imageData)
             throws IOException {
        int size = 0;
        if (format == SWFConstants.BITMAP_FORMAT_8_BIT) {
            size = 8;
        } else if (format == SWFConstants.BITMAP_FORMAT_16_BIT) {
            size = 16;
        } else if (format == SWFConstants.BITMAP_FORMAT_32_BIT) {
            size = 32;
        }

        println(name + " id=" + id + " bits=" + size +
                " width=" + width + " height=" + height);

        if (dumpHex) {
            for (int i = 0; i < colors.length; i++) {
                println("    " + i + ": " + colors[i]);
            }

            Hex.dump(writer, imageData, 0L, indent + "    ", false);
            println(dashes);
        }
    }


    /**
     *  SWFVectors interface SWFText interface
     *
     *@exception  IOException  Description of the Exception
     */
    public void done() throws IOException {
        println("    " + dashes);
    }


    /**
     *  SWFVectors interface
     *
     *@param  dx               Description of the Parameter
     *@param  dy               Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void line(int dx, int dy) throws IOException {
        println("    line  " + dx + "," + dy);
    }


    /**
     *  SWFVectors interface
     *
     *@param  cx               Description of the Parameter
     *@param  cy               Description of the Parameter
     *@param  dx               Description of the Parameter
     *@param  dy               Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void curve(int cx, int cy, int dx, int dy) throws IOException {
        println("    curve " + cx + "," + cy + " - " + dx + "," + dy);
    }


    /**
     *  SWFVectors interface
     *
     *@param  x                Description of the Parameter
     *@param  y                Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void move(int x, int y) throws IOException {
        println("    move  " + x + "," + y);
    }


    /**
     *  SWFShape interface
     *
     *@param  styleIndex       The new fillStyle0 value
     *@exception  IOException  Description of the Exception
     */
    public void setFillStyle0(int styleIndex) throws IOException {
        println("    fill0 = " + styleIndex);
    }


    /**
     *  SWFShape interface
     *
     *@param  styleIndex       The new fillStyle1 value
     *@exception  IOException  Description of the Exception
     */
    public void setFillStyle1(int styleIndex) throws IOException {
        println("    fill1 = " + styleIndex);
    }


    /**
     *  SWFShape interface
     *
     *@param  styleIndex       The new lineStyle value
     *@exception  IOException  Description of the Exception
     */
    public void setLineStyle(int styleIndex) throws IOException {
        println("    line  = " + styleIndex);
    }


    /**
     *  SWFShape interface
     *
     *@param  color            Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void defineFillStyle(Color color) throws IOException {
        println("    fill " + color);
    }


    /**
     *  SWFShape interface
     *
     *@param  matrix           Description of the Parameter
     *@param  ratios           Description of the Parameter
     *@param  colors           Description of the Parameter
     *@param  radial           Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void defineFillStyle(Matrix matrix, int[] ratios,
            Color[] colors, boolean radial)
             throws IOException {
        println("    fill radial=" + radial + "  " + matrix);

        for (int i = 0; i < ratios.length && i < colors.length; i++) {
            if (colors[i] == null) {
                continue;
            }
            println("         ratio=" + ratios[i] + " " + colors[i]);
        }
    }


    /**
     *  SWFShape interface
     *
     *@param  bitmapId         Description of the Parameter
     *@param  matrix           Description of the Parameter
     *@param  clipped          Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void defineFillStyle(int bitmapId, Matrix matrix, boolean clipped)
             throws IOException {
        println("    fill clipped=" + clipped + " image=" + bitmapId + " " + matrix);
    }


    /**
     *  SWFShape interface
     *
     *@param  width            Description of the Parameter
     *@param  color            Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void defineLineStyle(int width, Color color) throws IOException {
        println("    line-style width=" + width + "  " + color);
    }


    /**
     *  SWFText interface
     *
     *@param  fontId           Description of the Parameter
     *@param  textHeight       Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void font(int fontId, int textHeight) throws IOException {
        println("    font id=" + fontId + " size=" + textHeight);
    }


    /**
     *  SWFText interface
     *
     *@param  color            Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void color(Color color) throws IOException {
        println("    color " + color);
    }


    /**
     *  SWFText interface
     *
     *@param  x                The new x value
     *@exception  IOException  Description of the Exception
     */
    public void setX(int x) throws IOException {
        println("    x = " + x);
    }


    /**
     *  SWFText interface
     *
     *@param  y                The new y value
     *@exception  IOException  Description of the Exception
     */
    public void setY(int y) throws IOException {
        println("    y = " + y);
    }


    /**
     *  SWFText interface
     *
     *@param  glyphIndices     Description of the Parameter
     *@param  glyphAdvances    Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void text(int[] glyphIndices, int[] glyphAdvances) throws IOException {
        StringBuffer buff1 = new StringBuffer();
        StringBuffer buff2 = new StringBuffer();

        buff1.append("(");
        buff2.append("(");

        for (int i = 0; i < glyphIndices.length && i < glyphAdvances.length; i++) {
            buff1.append(" ");
            buff2.append(" ");

            buff1.append(glyphIndices[i]);
            buff2.append(glyphAdvances[i]);
        }

        buff1.append(" )");
        buff2.append(" )");

        println("    text");
        println("        glyph indices = " + buff1);
        println("        advances      = " + buff2);
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void flush() throws IOException {
        writer.flush();
    }


    /**
     *  args[0] = name of SWF file to dump to System.out args[1] = if exists
     *  then dump-hex is true (dumps binary as hex - otherwise skips) args[2] =
     *  if exists then decompiles action codes
     *
     *@param  args             The command line arguments
     *@exception  IOException  Description of the Exception
     */
    public static void main(String[] args) throws IOException {
        SWFTagDumper dumper = new SWFTagDumper(args.length > 1, args.length > 2);

        FileInputStream in = new FileInputStream(args[0]);
        SWFTags tagparser = new TagParser(dumper);
        SWFReader reader = new SWFReader(tagparser, in);
        reader.readFile();

        dumper.flush();
        in.close();
    }
}
