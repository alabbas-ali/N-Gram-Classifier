package pt.tumba.parser.swf;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

/**
 *  Description of the Class
 *
 *@author     unknown
 *@created    15 de Setembro de 2002
 */
public class Byte4ByteDebugStreams extends OutputStream {
    /**
     *  Description of the Field
     */
    protected ByteArrayInputStream in;

    /**
     *  Description of the Field
     */
    protected byte[] bytesIn;
    /**
     *  Description of the Field
     */
    protected byte[] bytesOut;

    /**
     *  Description of the Field
     */
    protected int bytePtr = 0;


    /**
     *  Constructor for the Byte4ByteDebugStreams object
     *
     *@param  filenameIn     Description of the Parameter
     *@exception  Exception  Description of the Exception
     */
    public Byte4ByteDebugStreams(String filenameIn) throws Exception {
        RandomAccessFile raIn = new RandomAccessFile(filenameIn, "r");

        bytesIn = new byte[(int) raIn.length()];
        bytesOut = new byte[(int) raIn.length()];
        raIn.readFully(bytesIn);
        raIn.close();
    }


    /**
     *  Constructor for the Byte4ByteDebugStreams object
     *
     *@param  bytesIn  Description of the Parameter
     */
    public Byte4ByteDebugStreams(byte[] bytesIn) {
        this.bytesIn = bytesIn;
        bytesOut = new byte[bytesIn.length];
    }


    /**
     *  Gets the inputStream attribute of the Byte4ByteDebugStreams object
     *
     *@return    The inputStream value
     */
    public InputStream getInputStream() {
        if (in != null) {
            return in;
        }

        in = new ByteArrayInputStream(bytesIn);

        return in;
    }


    /**
     *  Sets the inputBytes attribute of the Byte4ByteDebugStreams object
     *
     *@param  inBytes  The new inputBytes value
     */
    public void setInputBytes(byte[] inBytes) {
        bytesIn = inBytes;
    }


    /**
     *  Description of the Method
     *
     *@param  b                Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void write(int b2) throws IOException {
        int b = b2;
        if (b > 127) {
            b = (b & 0x7f) - 128;
        }

        bytesOut[bytePtr] = (byte) b;

        if (bytesOut[bytePtr] != bytesIn[bytePtr]) {
            IOException ioe = new IOException("Byte mismatch between input and output at byte #" + bytePtr
                    + " 0x" + Integer.toHexString(bytePtr)
                    + "\nexpected 0x"
                    + Integer.toHexString(bytesIn[bytePtr])
                    + " but got 0x"
                    + Integer.toHexString(b));

            ioe.printStackTrace();

            throw ioe;
        }

        bytePtr++;
    }


    /**
     *  Description of the Method
     *
     *@param  filenameOut      Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void write(String filenameOut) throws IOException {
        FileOutputStream out = new FileOutputStream(filenameOut);

        out.write(bytesOut);
        out.flush();
        out.close();
    }
}
