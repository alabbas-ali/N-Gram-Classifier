package pt.tumba.parser.swf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *  An MP3 sound data frame.
 *
 *@author     unknown
 *@created    15 de Setembro de 2002
 */
public class MP3Frame {
    /**
     *  Description of the Field
     */
    public final static int MPEG_Version_2_5 = 0;
    /**
     *  Description of the Field
     */
    public final static int MPEG_Version_2 = 2;
    /**
     *  Description of the Field
     */
    public final static int MPEG_Version_1 = 3;

    /**
     *  Description of the Field
     */
    public final static int MPEG_Layer_3 = 1;
    /**
     *  Description of the Field
     */
    public final static int MPEG_Layer_2 = 2;
    /**
     *  Description of the Field
     */
    public final static int MPEG_Layer_1 = 3;

    /**
     *  Description of the Field
     */
    public final static int CHANNEL_MODE_STEREO = 0;
    /**
     *  Description of the Field
     */
    public final static int CHANNEL_MODE_JOINT_STEREO = 1;
    /**
     *  Description of the Field
     */
    public final static int CHANNEL_MODE_DUAL_CHANNEL = 2;
    /**
     *  Description of the Field
     */
    public final static int CHANNEL_MODE_MONO = 3;

    /**
     *  Description of the Field
     */
    public final static int EMPHASIS_NONE = 0;
    /**
     *  Description of the Field
     */
    public final static int EMPHASIS_50_15_MS = 1;
    /**
     *  Description of the Field
     */
    public final static int EMPHASIS_RESERVED = 2;
    /**
     *  Description of the Field
     */
    public final static int EMPHASIS_CCIT_J17 = 3;

    /**
     *  Description of the Field
     */
    protected final static int[] MPEG1BitRates = {0, 32000, 40000, 48000, 56000, 64000, 80000, 96000, 112000, 128000, 160000, 192000, 224000, 256000, 320000, 0};
    /**
     *  Description of the Field
     */
    protected final static int[] MPEG2BitRates = {0, 8000, 16000, 24000, 32000, 40000, 48000, 56000, 64000, 80000, 96000, 112000, 128000, 144000, 160000, 0};

    /**
     *  Description of the Field
     */
    protected final static int[] MPEG10SampleRates = {44100, 48000, 32000, 0};
    /**
     *  Description of the Field
     */
    protected final static int[] MPEG20SampleRates = {22050, 24000, 16000, 0};
    /**
     *  Description of the Field
     */
    protected final static int[] MPEG25SampleRates = {11025, 12000, 8000, 0};

    private static int FRAME_SAMPLES_MPEG_1 = 1152;
    private static int FRAME_SAMPLES_MPEG_2 = 576;

    /**
     *  Description of the Field
     */
    protected int mpegVersion;
    /**
     *  Description of the Field
     */
    protected int mpegLayer;
    /**
     *  Description of the Field
     */
    protected boolean isProtected;
    /**
     *  Description of the Field
     */
    protected int bitRate;
    /**
     *  Description of the Field
     */
    protected int sampleRate;
    /**
     *  Description of the Field
     */
    protected boolean padded;
    /**
     *  Description of the Field
     */
    protected int channelMode;
    /**
     *  Description of the Field
     */
    protected int modeExtension;
    /**
     *  Description of the Field
     */
    protected boolean copyrighted;
    /**
     *  Description of the Field
     */
    protected boolean original;
    /**
     *  Description of the Field
     */
    protected int emphasis;
    /**
     *  Description of the Field
     */
    protected byte[] data;

    /**
     *  Description of the Field
     */
    protected int bit_rate;
    /**
     *  Description of the Field
     */
    protected int sample_rate;


    /**
     *  Gets the bitRate attribute of the MP3Frame object
     *
     *@return    The bitRate value
     */
    public int getBitRate() {
        return bitRate;
    }


    /**
     *  Gets the sampleRate attribute of the MP3Frame object
     *
     *@return    The sampleRate value
     */
    public int getSampleRate() {
        return sampleRate;
    }


    /**
     *  Gets the stereo attribute of the MP3Frame object
     *
     *@return    The stereo value
     */
    public boolean isStereo() {
        return channelMode != MP3Frame.CHANNEL_MODE_MONO;
    }


    /**
     *  Gets the dataLength attribute of the MP3Frame object
     *
     *@return    The dataLength value
     */
    public int getDataLength() {
        return data.length;
    }


    /**
     *  Gets the samplesPerFrame attribute of the MP3Frame object
     *
     *@return    The samplesPerFrame value
     */
    public int getSamplesPerFrame() {
        if (mpegVersion == MP3Frame.MPEG_Version_1) {
            return MP3Frame.FRAME_SAMPLES_MPEG_1;
        } else {
            return MP3Frame.FRAME_SAMPLES_MPEG_2;
        }
    }


    /**
     *  Read the next MP3 frame from the stream - return null if no more
     *
     *@param  in               Description of the Parameter
     *@return                  Description of the Return Value
     *@exception  IOException  Description of the Exception
     */
    public static MP3Frame readFrame(InputStream in) throws IOException {
        MP3Frame frame = new MP3Frame();

        while (true) {
            int b = in.read();

            if (b < 0) {
                return null;
            }
            if (b == 0xFF) {
                b = in.read();

                if (b < 0) {
                    return null;
                }
                if ((b & 0xE0) != 0xE0) {
                    continue;
                }

                frame.mpegVersion = (b & 0x18) >> 3;
                frame.mpegLayer = (b & 0x06) >> 1;
                frame.isProtected = (b & 1) == 0;

                if (frame.mpegLayer != MPEG_Layer_3) {
                    continue;
                }
                break;
            }
        }

        //skip the CRC
        if (frame.isProtected) {
            in.read();
            in.read();
        }

        int b = in.read();
        if (b < 0) {
            return null;
        }

        frame.bit_rate = (b & 0xF0) >> 4;

        if (frame.mpegVersion == MPEG_Version_1) {
            frame.bitRate = MPEG1BitRates[frame.bit_rate];
        } else {
            frame.bitRate = MPEG2BitRates[frame.bit_rate];
        }

        frame.sample_rate = (b & 0x0C) >> 2;
        if (frame.mpegVersion == MPEG_Version_1) {
            frame.sampleRate = MPEG10SampleRates[frame.sample_rate];
        } else if (frame.mpegVersion == MPEG_Version_2) {
            frame.sampleRate = MPEG20SampleRates[frame.sample_rate];
        } else {
            frame.sampleRate = MPEG25SampleRates[frame.sample_rate];
        }

        frame.padded = (b & 2) != 0;

        b = in.read();
        if (b < 0) {
            return null;
        }

        frame.channelMode = (b & 0xC0) >> 6;
        frame.modeExtension = (b & 0x30) >> 4;

        frame.copyrighted = ((b & 0x80) >> 3) != 0;
        frame.original = ((b & 0x40) >> 2) != 0;

        frame.emphasis = b & 0x02;

        int size = (((frame.mpegVersion == MPEG_Version_1 ? 144 : 72)
                * frame.bitRate) / frame.sampleRate) +
                (frame.padded ? 1 : 0) - 4;

        byte[] data = new byte[size];
        int read = 0;
        int r;

        while ((r = in.read(data, read, size - read)) >= 0 && read < size) {
            read += r;
        }

        if (read != size) {
            throw new IOException("Unexpected end of MP3 data");
        }

        frame.data = data;

        //System.out.print( "." );
        return frame;
    }


    /**
     *  Constructor for the MP3Frame object
     */
    public MP3Frame() { }


    /**
     *  Description of the Method
     *
     *@param  out              Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void write(OutputStream out) throws IOException {
        out.write(0xff);

        int b = 0xE1;
        b |= this.mpegVersion << 3;
        b |= this.mpegLayer << 1;
        out.write(b);

        b = bit_rate << 4;
        b |= sample_rate << 2;
        if (padded) {
            b |= 2;
        }
        out.write(b);

        b = channelMode << 6;
        b |= modeExtension << 4;
        b |= emphasis;
        out.write(b);

        out.write(data);
    }


    /**
     *  Description of the Method
     *
     *@return    Description of the Return Value
     */
    public String toString() {
        String version = null;
        if (mpegVersion == MP3Frame.MPEG_Version_1) {
            version = "1";
        } else if (mpegVersion == MP3Frame.MPEG_Version_2) {
            version = "2";
        } else if (mpegVersion == MP3Frame.MPEG_Version_2_5) {
            version = "2.5";
        }

        return "MP3 Frame: " +
                " version=" + version +
                " bit-rate=" + bitRate +
                " sample-rate=" + sampleRate +
                " stereo=" + (channelMode != MP3Frame.CHANNEL_MODE_MONO);
    }
}
