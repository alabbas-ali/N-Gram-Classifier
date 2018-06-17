package pt.tumba.ngram;

/**
 *  Wrapper for Exceptions used in the TCatNG package.
 *
 *@author     Bruno Martins
 */
public class TCatNGException extends Exception {

    /** The Exception message */
    String msg;

    /**
     *  Constructor for NGramException
     *
     *@param  msg  A String with the Exception message.
     */
    public TCatNGException(String msg) {
        this.msg = msg;
    }

	/**
	 *  Constructor for NGramException
	 *
	 *@param e  The Exception we wish to wrap.
	 */
	public TCatNGException(Exception e) {
		this.msg = e.toString();
	}

    /**
     * Returns the String message associated with this Exception.
     *
     *@return   A String with the message associated with this Exception.
     */
    public String toString() {
        return "TCatNGException{'" + msg + "'}";
    }
    
}
