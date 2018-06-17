package pt.tumba.parser.swf;

import java.io.IOException;

/**
 *  A pass-through implementation of the SWFActions interface
 *
 *@author     unknown
 *@created    15 de Setembro de 2002
 */
public class SWFActionsImpl implements SWFActions {
    /**
     *  Description of the Field
     */
    protected SWFActions acts;


    /**
     *@param  acts  may be null
     */
    public SWFActionsImpl(SWFActions acts) {
        this.acts = acts;
    }


    /**
     *  Constructor for the SWFActionsImpl object
     */
    public SWFActionsImpl() {
        this(null);
    }


    /**
     *  Set the pass-through target
     *
     *@param  acts  may be null
     */
    public void setSWFActions(SWFActions acts) {
        this.acts = acts;
    }


    /**
     *  Description of the Method
     *
     *@param  conditions       Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void start(int conditions) throws IOException {
        if (acts != null) {
            acts.start(conditions);
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void end() throws IOException {
        if (acts != null) {
            acts.end();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void done() throws IOException {
        if (acts != null) {
            acts.done();
        }
    }


    /**
     *  Description of the Method
     *
     *@param  blob             Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void blob(byte[] blob) throws IOException {
        if (acts != null) {
            acts.blob(blob);
        }
    }


    /**
     *  Description of the Method
     *
     *@param  code             Description of the Parameter
     *@param  data             Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void unknown(int code, byte[] data) throws IOException {
        if (acts != null) {
            acts.unknown(code, data);
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void initArray() throws IOException {
        if (acts != null) {
            acts.initArray();
        }
    }


    /**
     *  Description of the Method
     *
     *@param  label            Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void jumpLabel(String label) throws IOException {
        if (acts != null) {
            acts.jumpLabel(label);
        }
    }


    /**
     *  Description of the Method
     *
     *@param  frameNumber      Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void gotoFrame(int frameNumber) throws IOException {
        if (acts != null) {
            acts.gotoFrame(frameNumber);
        }
    }


    /**
     *  Description of the Method
     *
     *@param  label            Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void gotoFrame(String label) throws IOException {
        if (acts != null) {
            acts.gotoFrame(label);
        }
    }


    /**
     *  Gets the uRL attribute of the SWFActionsImpl object
     *
     *@param  url              Description of the Parameter
     *@param  target           Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void getURL(String url, String target) throws IOException {
        if (acts != null) {
            acts.getURL(url, target);
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void nextFrame() throws IOException {
        if (acts != null) {
            acts.nextFrame();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void prevFrame() throws IOException {
        if (acts != null) {
            acts.prevFrame();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void play() throws IOException {
        if (acts != null) {
            acts.play();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void stop() throws IOException {
        if (acts != null) {
            acts.stop();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void toggleQuality() throws IOException {
        if (acts != null) {
            acts.toggleQuality();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void stopSounds() throws IOException {
        if (acts != null) {
            acts.stopSounds();
        }
    }


    /**
     *  Sets the target attribute of the SWFActionsImpl object
     *
     *@param  target           The new target value
     *@exception  IOException  Description of the Exception
     */
    public void setTarget(String target) throws IOException {
        if (acts != null) {
            acts.setTarget(target);
        }
    }


    /**
     *  Description of the Method
     *
     *@param  jumpLabel        Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void jump(String jumpLabel) throws IOException {
        if (acts != null) {
            acts.jump(jumpLabel);
        }
    }


    /**
     *  Description of the Method
     *
     *@param  jumpLabel        Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void ifJump(String jumpLabel) throws IOException {
        if (acts != null) {
            acts.ifJump(jumpLabel);
        }
    }


    /**
     *  Description of the Method
     *
     *@param  frameNumber      Description of the Parameter
     *@param  jumpLabel        Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void waitForFrame(int frameNumber, String jumpLabel) throws IOException {
        if (acts != null) {
            acts.waitForFrame(frameNumber, jumpLabel);
        }
    }


    /**
     *  Description of the Method
     *
     *@param  jumpLabel        Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void waitForFrame(String jumpLabel) throws IOException {
        if (acts != null) {
            acts.waitForFrame(jumpLabel);
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void pop() throws IOException {
        if (acts != null) {
            acts.pop();
        }
    }


    /**
     *  Description of the Method
     *
     *@param  value            Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void push(String value) throws IOException {
        if (acts != null) {
            acts.push(value);
        }
    }


    /**
     *  Description of the Method
     *
     *@param  value            Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void push(float value) throws IOException {
        if (acts != null) {
            acts.push(value);
        }
    }


    /**
     *  Description of the Method
     *
     *@param  value            Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void push(double value) throws IOException {
        if (acts != null) {
            acts.push(value);
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void pushNull() throws IOException {
        if (acts != null) {
            acts.pushNull();
        }
    }


    /**
     *  Description of the Method
     *
     *@param  registerNumber   Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void pushRegister(int registerNumber) throws IOException {
        if (acts != null) {
            acts.pushRegister(registerNumber);
        }
    }


    /**
     *  Description of the Method
     *
     *@param  value            Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void push(boolean value) throws IOException {
        if (acts != null) {
            acts.push(value);
        }
    }


    /**
     *  Description of the Method
     *
     *@param  value            Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void push(int value) throws IOException {
        if (acts != null) {
            acts.push(value);
        }
    }


    /**
     *  Description of the Method
     *
     *@param  dictionaryIndex  Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void lookup(int dictionaryIndex) throws IOException {
        if (acts != null) {
            acts.lookup(dictionaryIndex);
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void add() throws IOException {
        if (acts != null) {
            acts.add();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void substract() throws IOException {
        if (acts != null) {
            acts.substract();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void multiply() throws IOException {
        if (acts != null) {
            acts.multiply();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void divide() throws IOException {
        if (acts != null) {
            acts.divide();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void equals() throws IOException {
        if (acts != null) {
            acts.equals();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void lessThan() throws IOException {
        if (acts != null) {
            acts.lessThan();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void and() throws IOException {
        if (acts != null) {
            acts.and();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void or() throws IOException {
        if (acts != null) {
            acts.or();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void not() throws IOException {
        if (acts != null) {
            acts.not();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void stringEquals() throws IOException {
        if (acts != null) {
            acts.stringEquals();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void stringLength() throws IOException {
        if (acts != null) {
            acts.stringLength();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void concat() throws IOException {
        if (acts != null) {
            acts.concat();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void substring() throws IOException {
        if (acts != null) {
            acts.substring();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void stringLessThan() throws IOException {
        if (acts != null) {
            acts.stringLessThan();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void stringLengthMB() throws IOException {
        if (acts != null) {
            acts.stringLengthMB();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void substringMB() throws IOException {
        if (acts != null) {
            acts.substringMB();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void toInteger() throws IOException {
        if (acts != null) {
            acts.toInteger();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void charToAscii() throws IOException {
        if (acts != null) {
            acts.charToAscii();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void asciiToChar() throws IOException {
        if (acts != null) {
            acts.asciiToChar();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void charMBToAscii() throws IOException {
        if (acts != null) {
            acts.charMBToAscii();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void asciiToCharMB() throws IOException {
        if (acts != null) {
            acts.asciiToCharMB();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void call() throws IOException {
        if (acts != null) {
            acts.call();
        }
    }


    /**
     *  Gets the variable attribute of the SWFActionsImpl object
     *
     *@exception  IOException  Description of the Exception
     */
    public void getVariable() throws IOException {
        if (acts != null) {
            acts.getVariable();
        }
    }


    /**
     *  Sets the variable attribute of the SWFActionsImpl object
     *
     *@exception  IOException  Description of the Exception
     */
    public void setVariable() throws IOException {
        if (acts != null) {
            acts.setVariable();
        }
    }


    /**
     *  Gets the uRL attribute of the SWFActionsImpl object
     *
     *@param  sendVars         Description of the Parameter
     *@param  loadMode         Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void getURL(int sendVars, int loadMode) throws IOException {
        if (acts != null) {
            acts.getURL(sendVars, loadMode);
        }
    }


    /**
     *  Description of the Method
     *
     *@param  play             Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void gotoFrame(boolean play) throws IOException {
        if (acts != null) {
            acts.gotoFrame(play);
        }
    }


    /**
     *  Sets the target attribute of the SWFActionsImpl object
     *
     *@exception  IOException  Description of the Exception
     */
    public void setTarget() throws IOException {
        if (acts != null) {
            acts.setTarget();
        }
    }


    /**
     *  Gets the property attribute of the SWFActionsImpl object
     *
     *@exception  IOException  Description of the Exception
     */
    public void getProperty() throws IOException {
        if (acts != null) {
            acts.getProperty();
        }
    }


    /**
     *  Sets the property attribute of the SWFActionsImpl object
     *
     *@exception  IOException  Description of the Exception
     */
    public void setProperty() throws IOException {
        if (acts != null) {
            acts.setProperty();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void cloneSprite() throws IOException {
        if (acts != null) {
            acts.cloneSprite();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void removeSprite() throws IOException {
        if (acts != null) {
            acts.removeSprite();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void startDrag() throws IOException {
        if (acts != null) {
            acts.startDrag();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void endDrag() throws IOException {
        if (acts != null) {
            acts.endDrag();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void trace() throws IOException {
        if (acts != null) {
            acts.trace();
        }
    }


    /**
     *  Gets the time attribute of the SWFActionsImpl object
     *
     *@exception  IOException  Description of the Exception
     */
    public void getTime() throws IOException {
        if (acts != null) {
            acts.getTime();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void randomNumber() throws IOException {
        if (acts != null) {
            acts.randomNumber();
        }
    }


    /**
     *  Description of the Method
     *
     *@param  values           Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void lookupTable(String[] values) throws IOException {
        if (acts != null) {
            acts.lookupTable(values);
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void callFunction() throws IOException {
        if (acts != null) {
            acts.callFunction();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void callMethod() throws IOException {
        if (acts != null) {
            acts.callMethod();
        }
    }


    /**
     *  Description of the Method
     *
     *@param  name             Description of the Parameter
     *@param  paramNames       Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void startFunction(String name, String[] paramNames) throws IOException {
        if (acts != null) {
            acts.startFunction(name, paramNames);
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void endBlock() throws IOException {
        if (acts != null) {
            acts.endBlock();
        }
    }


    /**
     *  Description of the Method
     *
     *@param  comment          Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void comment(String comment) throws IOException {
        if (acts != null) {
            acts.comment(comment);
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void defineLocalValue() throws IOException {
        if (acts != null) {
            acts.defineLocalValue();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void defineLocal() throws IOException {
        if (acts != null) {
            acts.defineLocal();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void deleteProperty() throws IOException {
        if (acts != null) {
            acts.deleteProperty();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void deleteThreadVars() throws IOException {
        if (acts != null) {
            acts.deleteThreadVars();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void enumerate() throws IOException {
        if (acts != null) {
            acts.enumerate();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void typedEquals() throws IOException {
        if (acts != null) {
            acts.typedEquals();
        }
    }


    /**
     *  Gets the member attribute of the SWFActionsImpl object
     *
     *@exception  IOException  Description of the Exception
     */
    public void getMember() throws IOException {
        if (acts != null) {
            acts.getMember();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void initObject() throws IOException {
        if (acts != null) {
            acts.initObject();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void newMethod() throws IOException {
        if (acts != null) {
            acts.newMethod();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void newObject() throws IOException {
        if (acts != null) {
            acts.newObject();
        }
    }


    /**
     *  Sets the member attribute of the SWFActionsImpl object
     *
     *@exception  IOException  Description of the Exception
     */
    public void setMember() throws IOException {
        if (acts != null) {
            acts.setMember();
        }
    }


    /**
     *  Gets the targetPath attribute of the SWFActionsImpl object
     *
     *@exception  IOException  Description of the Exception
     */
    public void getTargetPath() throws IOException {
        if (acts != null) {
            acts.getTargetPath();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void startWith() throws IOException {
        if (acts != null) {
            acts.startWith();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void duplicate() throws IOException {
        if (acts != null) {
            acts.duplicate();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void returnValue() throws IOException {
        if (acts != null) {
            acts.returnValue();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void swap() throws IOException {
        if (acts != null) {
            acts.swap();
        }
    }


    /**
     *  Description of the Method
     *
     *@param  registerNumber   Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void storeInRegister(int registerNumber) throws IOException {
        if (acts != null) {
            acts.storeInRegister(registerNumber);
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void convertToNumber() throws IOException {
        if (acts != null) {
            acts.convertToNumber();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void convertToString() throws IOException {
        if (acts != null) {
            acts.convertToString();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void typeOf() throws IOException {
        if (acts != null) {
            acts.typeOf();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void typedAdd() throws IOException {
        if (acts != null) {
            acts.typedAdd();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void typedLessThan() throws IOException {
        if (acts != null) {
            acts.typedLessThan();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void modulo() throws IOException {
        if (acts != null) {
            acts.modulo();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void bitAnd() throws IOException {
        if (acts != null) {
            acts.bitAnd();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void bitOr() throws IOException {
        if (acts != null) {
            acts.bitOr();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void bitXor() throws IOException {
        if (acts != null) {
            acts.bitXor();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void shiftLeft() throws IOException {
        if (acts != null) {
            acts.shiftLeft();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void shiftRight() throws IOException {
        if (acts != null) {
            acts.shiftRight();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void shiftRightUnsigned() throws IOException {
        if (acts != null) {
            acts.shiftRightUnsigned();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void decrement() throws IOException {
        if (acts != null) {
            acts.decrement();
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  IOException  Description of the Exception
     */
    public void increment() throws IOException {
        if (acts != null) {
            acts.increment();
        }
    }
}

