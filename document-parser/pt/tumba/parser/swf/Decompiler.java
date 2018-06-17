package pt.tumba.parser.swf;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Stack;

/**
 *  An implementation of SWFActions that decompiles the Action Code
 *
 *@author     unknown
 *@created    15 de Setembro de 2002
 */
public class Decompiler extends SWFActionsImpl implements SWFActions {
    /**
     *  Description of the Field
     */
    protected Writer writer;
    /**
     *  Description of the Field
     */
    protected int indent = 0;

    /**
     *  Description of the Field
     */
    protected Stack labelStack = new Stack();
    /**
     *  Description of the Field
     */
    protected Stack stack = new Stack();
    /**
     *  Description of the Field
     */
    protected String[] lookupTable = null;
    /**
     *  Description of the Field
     */
    protected Object[] registers = new Object[10];
    //actually only 4 registers ??
    /**
     *  Description of the Field
     */
    protected Stack writerStack = new Stack();
    /**
     *  Description of the Field
     */
    protected boolean duplicated = false;


    /**
     *  Constructor for the Decompiler object
     *
     *@param  writer  Description of the Parameter
     */
    public Decompiler(Writer writer) {
        this.writer = writer;
    }


    /**
     *  Constructor for the Decompiler object
     *
     *@param  writer  Description of the Parameter
     *@param  indent  Description of the Parameter
     */
    public Decompiler(Writer writer, int indent) {
        this.writer = writer;
        this.indent = indent;
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    protected void indent() throws IOException {
        for (int i = 0; i < indent; i++) {
            writer.write("    ");
        }
    }


    /**
     *  Description of the Method
     *
     *@param  s  Description of the Parameter
     *@return    Description of the Return Value
     */
    protected String destring(String s2) {
        String s = s2.trim();
        if (s.startsWith("\"") && s.endsWith("\"")) {
            if (s.length() >= 3) {
                return s.substring(1, s.length() - 1);
            }
            return "";
        }

        return s;
    }


    /**
     *  Description of the Method
     *
     *@param  s  Description of the Parameter
     *@return    Description of the Return Value
     */
    protected String string(String s) {
        StringBuffer buff = new StringBuffer("\"");

        int len = s.length();
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);

            switch (c) {
                case '\b':
                    buff.append("\\b");
                    break;
                case '\f':
                    buff.append("\\f");
                    break;
                case '\n':
                    buff.append("\\n");
                    break;
                case '\r':
                    buff.append("\\r");
                    break;
                case '\t':
                    buff.append("\\t");
                    break;
                case '\"':
                    buff.append("\\\"");
                    break;
                case '\\':
                    buff.append("\\\\");
                    break;
                default:
                    buff.append(""+c);
                    break;
            }
        }

        buff.append("\"");

        return buff.toString();
    }


    /**
     *  Start of actions
     *
     *@param  flags            Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void start(int flags) throws IOException {
        writeConditions(flags);
        labelStack.clear();
        stack.clear();
        writerStack.clear();
        lookupTable = null;
    }


    /**
     *  Description of the Method
     *
     *@param  flags            Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    protected void writeConditions(int flags) throws IOException {
        indent();
        writer.write("Conditions: " + Integer.toBinaryString(flags) + "\n\n");
    }


    /**
     *  Description of the Method
     *
     *@return    Description of the Return Value
     */
    protected Object pop2() {
        if (stack.isEmpty()) {
            return "void()";
        }
        duplicated = false;
        return stack.pop();
    }


    /**
     *  Description of the Method
     *
     *@return    Description of the Return Value
     */
    protected Object peek() {
        if (stack.isEmpty()) {
            return "void()";
        }
        return stack.peek();
    }


    /**
     *  End of all action blocks
     */
    public void done() {
        //nothing
    }


    /**
     *  End of actions
     *
     *@exception  IOException  Description of the Exception
     */
    public void end() throws IOException {
        writer.write("\n");
    }


    /**
     *  Pass through a blob of actions
     *
     *@param  blob  Description of the Parameter
     */
    public void blob(byte[] blob) {
        //nothing
    }


    /**
     *  Unrecognized action code
     *
     *@param  data  may be null
     *@param  code  Description of the Parameter
     */
    public void unknown(int code, byte[] data) {
        //nothing
    }


    /**
     *  Target label for a jump - this method call immediately precedes the
     *  target action.
     *
     *@param  label            Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void jumpLabel(String label) throws IOException {
        if (!labelStack.isEmpty()) {
            while ((!labelStack.isEmpty()) && labelStack.peek().equals(label)) {
                labelStack.pop();
                indent--;
                indent();
                writer.write("}\n\n");
            }
        }
    }


    /**
     *  Comment Text - useful for debugging purposes
     *
     *@param  comment          Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void comment(String comment) throws IOException {
        writer.write("\n");
        indent();
        writer.write("/* ");
        writer.write(comment);
        writer.write(" */\n");
    }


    /**
     *  Description of the Field
     */
    protected String gotoFrame;


    /**
     *  Description of the Method
     *
     *@param  frameNumber      Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void gotoFrame(int frameNumber) throws IOException {
        gotoFrame = Integer.toString(frameNumber + 1);
    }


    /**
     *  Description of the Method
     *
     *@param  label            Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void gotoFrame(String label) throws IOException {
        gotoFrame = string(label);
    }


    /**
     *  Gets the uRL attribute of the Decompiler object
     *
     *@param  url              Description of the Parameter
     *@param  target           Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void getURL(String url2, String target) throws IOException {
        String url = url2;
        if (url == null) {
            url = "";
        }

        indent();
        writer.write("getURL( ");
        writer.write(string(url));

        if (target != null) {
            writer.write(", ");
            writer.write(string(target));
        }

        writer.write(" );\n");
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void nextFrame() throws IOException {
        indent();
        writer.write("nextFrame();\n");
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void prevFrame() throws IOException {
        indent();
        writer.write("prevFrame();\n");
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void play() throws IOException {
        indent();

        if (gotoFrame != null) {
            writer.write("gotoAndPlay( ");
            writer.write(gotoFrame);
            writer.write(" );\n");
            gotoFrame = null;
        } else {
            writer.write("play();\n");
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void stop() throws IOException {
        indent();

        if (gotoFrame != null) {
            writer.write("gotoAndStop( ");
            writer.write(gotoFrame);
            writer.write(" );\n");
            gotoFrame = null;
        } else {
            writer.write("stop();\n");
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void toggleQuality() throws IOException {
        indent();
        writer.write("toggleHighQuality();\n");
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void stopSounds() throws IOException {
        indent();
        writer.write("stopAllSounds();\n");
    }


    /**
     *  Description of the Method
     *
     *@param  frameNumber      Description of the Parameter
     *@param  jumpLabel        Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void waitForFrame(int frameNumber, String jumpLabel) throws IOException {
        writer.write("\n");
        indent();
        writer.write("ifFrameLoaded( ");
        writer.write(Integer.toString(frameNumber + 1));
        writer.write(" )\n");
        indent();
        writer.write("{\n");
        indent++;

        labelStack.push(jumpLabel);
    }


    /**
     *  Sets the target attribute of the Decompiler object
     *
     *@param  target           The new target value
     *@exception  IOException  Description of the Exception
     */
    public void setTarget(String target) throws IOException {
        if (target == null || target.length() == 0) {
            indent--;
            indent();
            writer.write("}\n\n");
        } else {
            writer.write("\n");
            indent();
            writer.write("tellTarget( ");
            writer.write(string(target));
            writer.write(" )\n");
            indent();
            writer.write("{\n");
            indent++;
        }
    }


    /**
     *  Description of the Method
     *
     *@param  op               Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    protected void operator(String op) throws IOException {
        Object one = pop2();
        Object two = pop2();

        if (one instanceof StringBuffer) {
            one = "(" + one + ")";
        }

        if (two instanceof StringBuffer) {
            two = "(" + two + ")";
        }

        StringBuffer buff = new StringBuffer();

        buff.append(two.toString());
        buff.append(op);
        buff.append(one.toString());

        stack.push(buff);
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void add() throws IOException {
        operator(" + ");
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void substract() throws IOException {
        operator(" - ");
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void multiply() throws IOException {
        operator(" * ");
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void divide() throws IOException {
        operator(" / ");
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void equals() throws IOException {
        operator(" == ");
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void lessThan() throws IOException {
        operator(" < ");
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void and() throws IOException {
        operator(" && ");
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void or() throws IOException {
        operator(" || ");
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void not() throws IOException {
        StringBuffer buff = new StringBuffer("not( ");
        buff.append(pop2());
        buff.append(" )");

        stack.push(buff);
    }


    /*
     *  public void stringEquals() throws IOException;
     *  public void stringLength() throws IOException;
     *  public void concat() throws IOException;
     *  public void substring() throws IOException;
     *  public void stringLessThan() throws IOException;
     *  public void stringLengthMB() throws IOException;
     *  public void substringMB() throws IOException;
     */
    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void toInteger() throws IOException {
        stack.push("int( " + pop2().toString() + " )");
    }


    /*
     *  public void charToAscii() throws IOException;
     *  public void asciiToChar() throws IOException;
     *  public void charMBToAscii() throws IOException;
     *  public void asciiToCharMB() throws IOException;
     *  public void jump( String jumpLabel ) throws IOException;
     *  public void ifJump( String jumpLabel ) throws IOException;
     *  public void call() throws IOException;
     */
    /**
     *  Gets the variable attribute of the Decompiler object
     *
     *@exception  IOException  Description of the Exception
     */
    public void getVariable() throws IOException {
        Object name = pop2();

        if ((name instanceof String) && ((String) name).startsWith("\"")) {
            name = destring((String) name);
        } else {
            name = "eval( " + name.toString() + " )";
        }

        stack.push(name);
    }


    /**
     *  Sets the variable attribute of the Decompiler object
     *
     *@exception  IOException  Description of the Exception
     */
    public void setVariable() throws IOException {
        indent();

        String value = (stack.size() < 2) ? "void()" : pop2().toString();
        String varname = destring(pop2().toString());
        writer.write(varname + " = " + value);
        if (!value.trim().endsWith("}")) {
            writer.write(";\n");
        }
    }


    /*
     *  /----------------------------------------------------------
     *  public static final int GET_URL_SEND_VARS_NONE = 0;  //don't send variables
     *  public static final int GET_URL_SEND_VARS_GET  = 1;  //send vars using GET
     *  public static final int GET_URL_SEND_VARS_POST = 2;  //send vars using POST
     *  public static final int GET_URL_MODE_LOAD_MOVIE_INTO_LEVEL  = 0;
     *  public static final int GET_URL_MODE_LOAD_MOVIE_INTO_SPRITE = 1;
     *  public static final int GET_URL_MODE_LOAD_VARS_INTO_LEVEL   = 3;
     *  public static final int GET_URL_MODE_LOAD_VARS_INTO_SPRITE  = 4;
     *  public void getURL( int sendVars, int loadMode ) throws IOException;
     *  /----------------------------------------------------------
     *  public void gotoFrame( boolean play ) throws IOException;
     *  public void setTarget() throws IOException;
     *  public void getProperty() throws IOException;
     *  public void setProperty() throws IOException;
     *  public void cloneSprite() throws IOException;
     *  public void removeSprite() throws IOException;
     *  public void startDrag() throws IOException;
     *  public void endDrag() throws IOException;
     *  public void waitForFrame( String jumpLabel ) throws IOException;
     */
    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void trace() throws IOException {
        indent();
        writer.write("trace( " + pop2().toString() + " );\n");
    }


    /**
     *  Gets the time attribute of the Decompiler object
     *
     *@exception  IOException  Description of the Exception
     */
    public void getTime() throws IOException {
        stack.push("getTimer()");
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void randomNumber() throws IOException {
        stack.push("random( " + destring(pop2().toString()) + " )");
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void callFunction() throws IOException {
        String name = destring(pop2().toString());
        int numargs = ((Number) pop2()).intValue();

        StringBuffer buff = new StringBuffer();
        buff.append(name);
        buff.append("(");

        for (int i = 0; i < numargs; i++) {
            if (i > 0) {
                buff.append(",");
            }
            buff.append(" ");
            buff.append(destring(pop2().toString()));
        }

        if (numargs > 0) {
            buff.append(" ");
        }
        buff.append(")");
        stack.push(buff);
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void callMethod() throws IOException {
        String name = pop2().toString();
        Object obj = pop2().toString();
        int numargs = ((Number) pop2()).intValue();

        if (obj instanceof StringBuffer) {
            obj = "(" + obj + ")";
        }

        if (name.startsWith("\"")) {
            name = "." + destring(name);
        } else {
            name = "[" + name + "]";
        }

        StringBuffer buff = new StringBuffer();
        buff.append(obj.toString());
        buff.append(name);
        buff.append("(");

        for (int i = 0; i < numargs; i++) {
            if (i > 0) {
                buff.append(",");
            }
            buff.append(" ");
            buff.append(destring(pop2().toString()));
        }

        if (numargs > 0) {
            buff.append(" ");
        }
        buff.append(")");
        stack.push(buff);
    }


    /**
     *  Description of the Method
     *
     *@param  values           Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void lookupTable(String[] values) throws IOException {
        lookupTable = values;
    }


    /**
     *  Description of the Method
     *
     *@param  name             Description of the Parameter
     *@param  paramNames       Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void startFunction(String name, String[] paramNames2) throws IOException {
        String paramNames[] = paramNames2;
        if (name == null || name.trim().length() == 0) {
            writerStack.push(writer);
            writer = new StringWriter();
            writer.write("function(");
        } else {
            indent();
            writer.write("function " + name + "(");
        }

        if (paramNames == null) {
            paramNames = new String[0];
        }
        for (int i = 0; i < paramNames.length; i++) {
            if (i > 0) {
                writer.write(",");
            }
            writer.write(" ");
            writer.write(paramNames[i]);
        }

        if (paramNames.length > 0) {
            writer.write(" ");
        }
        writer.write(")\n");
        indent();
        writer.write("{\n");
        indent++;
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void endBlock() throws IOException {
        indent--;
        indent();
        writer.write("}\n\n");

        if (!writerStack.isEmpty()) {
            stack.push(((StringWriter) writer).getBuffer());
            writer = (Writer) writerStack.pop();
        }
    }



    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void defineLocalValue() throws IOException {
        String value = pop2().toString();
        String name = destring(pop2().toString());

        indent();
        writer.write("var " + name + " = " + value + ";\n");
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void defineLocal() throws IOException {
        String name = destring(pop2().toString());

        indent();
        writer.write("var " + name + ";\n");
    }


    /*
     *  public void deleteProperty() throws IOException;
     *  public void deleteThreadVars() throws IOException;
     *  public void enumerate() throws IOException;
     */
    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void typedEquals() throws IOException {
        equals();
    }


    /*
     *  public void getMember() throws IOException;
     *  public void initArray() throws IOException;
     *  public void initObject() throws IOException;
     *  public void newMethod() throws IOException;
     *  public void newObject() throws IOException;
     *  public void setMember() throws IOException;
     *  public void getTargetPath() throws IOException;
     */
    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void startWith() throws IOException {
        indent();
        writer.write("with( " + destring(pop2().toString()) + " )\n");
        indent();
        writer.write("{\n");
        indent++;
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void convertToNumber() throws IOException {
        stack.push("Number( " + pop2().toString() + " )");
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void convertToString() throws IOException {
        stack.push("String( " + pop2().toString() + " )");
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void typeOf() throws IOException {
        stack.push("typeOf( " + pop2().toString() + " )");
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void typedAdd() throws IOException {
        add();
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void typedLessThan() throws IOException {
        lessThan();
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void modulo() throws IOException {
        operator(" % ");
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void bitAnd() throws IOException {
        operator(" & ");
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void bitOr() throws IOException {
        operator(" | ");
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void bitXor() throws IOException {
        operator(" % ");
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void shiftLeft() throws IOException {
        operator(" << ");
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void shiftRight() throws IOException {
        operator(" >> ");
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void shiftRightUnsigned() throws IOException {
        operator(" >>> ");
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void decrement() throws IOException {
        Object value = pop2();

        if (value instanceof StringBuffer) {
            value = "(" + value + ")";
        }

        StringBuffer buff = new StringBuffer(value.toString());
        buff.append(" - 1");
        stack.push(buff);
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void increment() throws IOException {
        Object value = pop2();

        if (value instanceof StringBuffer) {
            value = "(" + value + ")";
        }

        StringBuffer buff = new StringBuffer(value.toString());
        buff.append(" + 1");
        stack.push(buff);
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void duplicate() throws IOException {
        stack.push(peek());
        duplicated = true;
    }


    /*
     *  public void returnValue() throws IOException;
     */
    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void swap() throws IOException {
        Object top = pop2();
        Object two = pop2();
        stack.push(top);
        stack.push(two);
    }


    /**
     *  Description of the Method
     *
     *@param  registerNumber   Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void storeInRegister(int registerNumber) throws IOException {
        if (registerNumber >= registers.length) {
            throw new IOException("Register index out of bounds");
        }

        registers[registerNumber] = peek();
    }


    /**
     *  Description of the Method
     *
     *@param  value            Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void push(double value) throws IOException {
        stack.push(new Double(value));
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void pushNull() throws IOException {
        stack.push("null");
    }


    /**
     *  Description of the Method
     *
     *@param  registerNumber   Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void pushRegister(int registerNumber) throws IOException {
        if (registerNumber >= registers.length) {
            throw new IOException("Register index out of bounds");
        }

        Object value = registers[registerNumber];
        if (value == null) {
            value = "null";
        }

        stack.push(value);
    }


    /**
     *  Description of the Method
     *
     *@param  value            Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void push(boolean value) throws IOException {
        stack.push(Boolean.valueOf(value));
    }


    /**
     *  Description of the Method
     *
     *@param  value            Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void push(int value) throws IOException {
        stack.push(new Integer(value));
    }


    /**
     *  Description of the Method
     *
     *@param  value            Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void push(String value) throws IOException {
        stack.push(string(value));
    }


    /**
     *  Description of the Method
     *
     *@param  value            Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void push(float value) throws IOException {
        stack.push(new Float(value));
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void pop() throws IOException {
        indent();
        writer.write(pop2().toString() + ";\n");
    }


    /**
     *  Description of the Method
     *
     *@param  dictionaryIndex  Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void lookup(int dictionaryIndex) throws IOException {
        if (lookupTable == null || dictionaryIndex >= lookupTable.length) {
            throw new IOException("Lookup index is out of bounds of the lookup table");
        }

        stack.push(string(lookupTable[dictionaryIndex]));
    }
}
