package pt.tumba.parser.swf;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *  MP3 Utilities
 *
 *@author     unknown
 *@created    15 de Setembro de 2002
 */
public class MP3Helper {
	
	private static final MP3Helper _theInstance = new  MP3Helper();

				private  MP3Helper() {
				}

				public static  MP3Helper getInstance() {
					return _theInstance;
				}
    
    /**
     *  Read an MP3 input file. Write the Sound Stream Header to the SWFTagTypes
     *  interface. Return a list of byte[] - one for each Streaming Sound Block
     *
     *@param  mp3              Description of the Parameter
     *@param  tags             Description of the Parameter
     *@param  framesPerSecond  Description of the Parameter
     *@return                  Description of the Return Value
     *@exception  IOException  Description of the Exception
     */
    public static List streamingBlocks(InputStream mp3, SWFTagTypes tags,
            int framesPerSecond)
             throws IOException {
        ArrayList list = new ArrayList();

        MP3Frame frame = MP3Frame.readFrame(mp3);

        int samplesPerFrame = frame.getSamplesPerFrame();
        int sampleRate = frame.getSampleRate();

        int mp3FramesPerSecond = sampleRate / samplesPerFrame;
        int mp3FramesPerSwfFrame = mp3FramesPerSecond / framesPerSecond;

        boolean isStereo = frame.isStereo();
        int blockSize = (frame.getDataLength() + 4) * mp3FramesPerSwfFrame;

        int rate = SWFConstants.SOUND_FREQ_5_5KHZ;
        if (sampleRate >= 44000) {
            rate = SWFConstants.SOUND_FREQ_44KHZ;
        } else if (sampleRate >= 22000) {
            rate = SWFConstants.SOUND_FREQ_22KHZ;
        } else if (sampleRate >= 11000) {
            rate = SWFConstants.SOUND_FREQ_11KHZ;
        }

        tags.tagSoundStreamHead(rate, true, isStereo,
                SWFConstants.SOUND_FORMAT_MP3,
                rate, true, isStereo,
                mp3FramesPerSwfFrame * samplesPerFrame);

        ByteArrayOutputStream bout = new ByteArrayOutputStream(blockSize + 1000);

        while (true && frame != null) {
            //--Write dummy sample count
            bout.write(0);
            bout.write(0);

            //--Write DelaySeek of zero
            bout.write(0);
            bout.write(0);

            int frameCount = mp3FramesPerSwfFrame;
            int sampleCount = 0;

            while (frameCount > 0 && frame != null) {
                sampleCount += frame.getSamplesPerFrame();
                frame.write(bout);
                frameCount--;
                frame = MP3Frame.readFrame(mp3);
            }

            bout.flush();
            byte[] bytes = bout.toByteArray();
            bytes[0] = (byte) (sampleCount & 0xFF);
            bytes[1] = (byte) (sampleCount >> 8);

            list.add(bytes);
            bout.reset();
        }

        mp3.close();

        return list;
    }


    /**
     *  Makes a streaming SWF from an MP3. args[0] = MP3 in filename args[1] =
     *  SWF out filename
     *
     *@param  args             The command line arguments
     *@exception  IOException  Description of the Exception
     */
    public static void main(String[] args) throws IOException {
        FileInputStream mp3 = new FileInputStream(args[0]);
        SWFWriter swfwriter = new SWFWriter(args[1]);

        SWFTagTypes tags = new TagWriter(swfwriter);

        tags.header(5, -1, 200, 200, 12, -1);
        tags.tagSetBackgroundColor(new Color(255, 255, 255));

        List blocks = MP3Helper.streamingBlocks(mp3, tags, 12);

        for (Iterator it = blocks.iterator(); it.hasNext(); ) {
            byte[] data = (byte[]) it.next();

            tags.tagSoundStreamBlock(data);
            tags.tagShowFrame();
        }

        tags.tagEnd();
    }
}
