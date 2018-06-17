package pt.tumba.ngram.bayes;

import java.io.*;

public class InterchangeFormat {
    InputStream istream;
    XMLInterchangeFormat xml_bif03;
    BayesInterchangeFormat bif015;

    // Size of the buffer for reading and resetting streams
    private final static int MARK_READ_LIMIT = 10000;

    public InterchangeFormat() {
    }

	public InterchangeFormat(Reader is) {
		set_stream(is);
	}

    public InterchangeFormat(InputStream is) {
        set_stream(is);
    }

    public void set_stream(InputStream is) {
        istream = new BufferedInputStream(is);
    }

	public void set_stream(Reader is) {
		BufferedReader aux = new BufferedReader(is); 
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		int d;
		try {	while ((d=aux.read())!=-1) output.write(d); } catch ( Exception e ) {}
		istream = new BufferedInputStream(new ByteArrayInputStream(output.toByteArray())); 
	}

    public void CompilationUnit() throws IFException {
        StringBuffer error_messages = new StringBuffer("Error messages\n");
        xml_bif03 = null;
        bif015 = null;
        if (istream.markSupported()) try {
            	istream.mark(MARK_READ_LIMIT);
        	} catch ( Exception e ) { throw new IFException(e.toString());
  	    }
        else
            error_messages.append("\nNo support for reset operation.");

        xml_bif03 = new XMLInterchangeFormat(istream);
        try {
            xml_bif03.CompilationUnit();
            xml_bif03.invert_probability_tables();
        } catch (Throwable e4) { // Catch anything!
            error_messages.append(e4); 
            try { istream.reset();
            } catch (Exception e) { error_messages.append("\n\nReset not allowed!"); }
            error_messages.append("Input stream reset!\n");
        // Note that the following lines are within an enclosing catch block.
        {
            try { istream.reset();
            } catch (Exception e) {  error_messages.append("\n\nReset not allowed!"); }
            error_messages.append("Input stream reset!\n");
        // Note that the following lines are within an enclosing catch block.
        bif015 = new BayesInterchangeFormat(istream);
        try {
            bif015.CompilationUnit(); 
        } catch (Throwable e2) { // Catch anything!
            error_messages.append(e2);
            try { istream.reset();
            } catch (Exception e) {  error_messages.append("\n\nReset not allowed!"); }
            error_messages.append("Input stream reset!\n");
        } // End bif015
        } // End xml_bif02
        } // End xml_bif03
    }

    public IFBayesNet get_ifbn() {
        IFBayesNet ifbn = null;

        if (xml_bif03 != null) ifbn = xml_bif03.get_ifbn();
        if (ifbn != null) return(ifbn);
        else {
        // Note that the following lines are inside an else.
        if (ifbn != null) return(ifbn);
        else {
        // Note that the following lines are inside an else.
        if (bif015 != null) ifbn = bif015.get_ifbn();
        if (ifbn != null) return(ifbn);
        } // End of bif015
        } // End of xml_bif02
        return(ifbn);
    }
}






