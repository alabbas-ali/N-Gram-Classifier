package pt.tumba.parser.swf;

import java.io.IOException;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *  A Movie Clip (aka Sprite) Symbol
 *
 *@author     unknown
 *@created    15 de Setembro de 2002
 */
public class MovieClip extends Symbol implements TimeLine {
    /**
     *  Description of the Field
     */
    protected SortedMap frames = new TreeMap();
    /**
     *  Description of the Field
     */
    protected int frameCount = 0;

    /**
     *  Description of the Field
     */
    protected int depth = 1;


    //the next available depth

    /**
     *  Constructor for the MovieClip object
     */
    public MovieClip() { }


    /**
     *  Get the current number of frames in the timeline.
     *
     *@return    The frameCount value
     */
    public int getFrameCount() {
        return frameCount;
    }


    /**
     *  Get the Frame object for the given frame number - or create one if none
     *  exists. If the frame number is larger than the current frame count then
     *  the frame count is increased.
     *
     *@param  frameNumber  must be 1 or larger
     *@return              The frame value
     */
    public Frame getFrame(int frameNumber) {
        if (frameNumber < 1) {
            return null;
        }

        Integer num = new Integer(frameNumber);
        Frame frame = (Frame) frames.get(num);

        if (frame == null) {
            frame = new Frame(frameNumber, this);
            frames.put(num, frame);
            if (frameNumber > frameCount) {
                frameCount = frameNumber;
            }
        }

        return frame;
    }


    /**
     *  Append a frame to the end of the timeline
     *
     *@return    Description of the Return Value
     */
    public Frame appendFrame() {
        frameCount++;
        Frame frame = new Frame(frameCount, this);
        frames.put(new Integer(frameCount), frame);
        return frame;
    }


    /**
     *  Get the next available depth in the timeline
     *
     *@return    The availableDepth value
     */
    public int getAvailableDepth() {
        return depth;
    }


    /**
     *  Set the next available depth in the timeline
     *
     *@param  depth  must be >= 1
     */
    public void setAvailableDepth(int depth) {
        if (depth < 1) {
            return;
        }
        this.depth = depth;
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

        //--flush all symbol definitions
        for (Iterator iter = frames.values().iterator(); iter.hasNext(); ) {
            Frame frame = (Frame) iter.next();
            frame.flushDefinitions(movie, timelineWriter, definitionWriter);
        }

        SWFTagTypes spriteWriter = definitionWriter.tagDefineSprite(id);

        int lastFrame = 0;
        for (Iterator iter = frames.values().iterator(); iter.hasNext(); ) {
            Frame frame = (Frame) iter.next();

            int number = frame.getFrameNumber();

            //write any intermediate empty frames
            while (number > lastFrame + 1) {
                spriteWriter.tagShowFrame();
                lastFrame++;
            }

            frame.write(movie, definitionWriter, spriteWriter);

            lastFrame = number;
        }

        //end of time line
        spriteWriter.tagEnd();

        return id;
    }
}
