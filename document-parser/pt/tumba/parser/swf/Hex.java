package pt.tumba.parser.swf;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 *  Hex dump conversion utilities
 *
 *@author     unknown
 *@created    15 de Setembro de 2002
 */
public class Hex {

	private static final Hex _theInstance = new  Hex();

			private  Hex() {
			}

			public static  Hex getInstance() {
				return _theInstance;
			}

    
    /**
     *  Description of the Method
     *
     *@param  data          Description of the Parameter
     *@param  startAddress  Description of the Parameter
     *@param  indent        Description of the Parameter
     *@return               Description of the Return Value
     */
    public static String dump(byte[] data, long startAddress, String indent) {
        StringWriter writer = new StringWriter();
        PrintWriter printer = new PrintWriter(writer, true);

        dump(printer, data, startAddress, indent, false);

        return writer.toString();
    }


    /**
     *  Description of the Method
     *
     *@param  data          Description of the Parameter
     *@param  startAddress  Description of the Parameter
     *@param  indent        Description of the Parameter
     *@return               Description of the Return Value
     */
    public static String dumpWithBinary(byte[] data, long startAddress, String indent) {
        StringWriter writer = new StringWriter();
        PrintWriter printer = new PrintWriter(writer, true);

        dump(printer, data, startAddress, indent, true);

        return writer.toString();
    }


    /**
     *  Description of the Method
     *
     *@param  out            Description of the Parameter
     *@param  data           Description of the Parameter
     *@param  startAddress   Description of the Parameter
     *@param  indent         Description of the Parameter
     *@param  includeBinary  Description of the Parameter
     */
    public static void dump(PrintWriter out,
            byte[] data,
            long startAddress,
            String indent,
            boolean includeBinary) {
        dump(out, data, 0, data.length, startAddress, indent, includeBinary);
    }


    /**
     *  Description of the Method
     *
     *@param  out            Description of the Parameter
     *@param  data           Description of the Parameter
     *@param  startIndex     Description of the Parameter
     *@param  length         Description of the Parameter
     *@param  startAddress   Description of the Parameter
     *@param  indent         Description of the Parameter
     *@param  includeBinary  Description of the Parameter
     */
    public static void dump(PrintWriter out,
            byte[] data,
            int startIndex,
            int length,
            long startAddress2,
            String indent,
            boolean includeBinary) {
        long startAddress = startAddress2;
        if (data == null) {
            return;
        }
        //nothing to dump

        int i = startIndex;
        int endIndex = startIndex + length - 1;
        if (endIndex >= data.length) {
            endIndex = data.length - 1;
        }

        while (i <= endIndex) {
            String hex = "";
            String chars = " ";
            String binary = "";

            for (int j = 0; j < 16; j++) {
                if (i + j <= endIndex) {
                    hex += " " + leadingZeros(Integer.toHexString(getByte(data[i + j])), 2);

                    if (includeBinary) {
                        binary += " " + leadingZeros(Integer.toBinaryString(getByte(data[i + j])), 8);
                    }

                    if (data[i + j] < 32) {
                        chars += ".";
                    } else {
                        chars += new String(new byte[]{data[i + j]});
                    }
                } else {
                    hex += " --";
                    chars += " ";

                    if (includeBinary) {
                        binary += " --------";
                    }
                }
            }

            out.println(((indent != null) ? indent : "")
                    + leadingZeros(Long.toHexString(startAddress), 8)
                    + hex
                    + chars
                    + binary);

            i += 16;
            startAddress += 16;
        }
    }


    /**
     *  Pad the string with leading zeros until it it reached the given size
     *
     *@param  string  Description of the Parameter
     *@param  size    Description of the Parameter
     *@return         Description of the Return Value
     */
    public static String leadingZeros(String string, int size) {
        String s = string;

        while (s.length() < size) {
            s = "0" + s;
        }

        return s;
    }


    /**
     *  Get unsigned byte
     *
     *@param  b  Description of the Parameter
     *@return    The byte value
     */
    protected static int getByte(byte b) {
        if (b >= 0) {
            return (int) b;
        }

        //else byte is negative and needs conversion to an unsigned int
        return b + 256;
    }
}
