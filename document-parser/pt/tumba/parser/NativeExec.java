package pt.tumba.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

/**
 * Description of the Class
 *
 * @author Bruno Martins
 * @created 11 de Setembro de 2002
 */
public class NativeExec extends Thread {

	private static final NativeExec _theInstance = new NativeExec();

	/**
	 * Description of the Method
	 *
	 * @param args
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception Exception
	 *                Description of the Exception
	 */
	public static int execute(String args) throws Exception {
		return execute(args, null);
	}

	/**
	 * Description of the Method
	 *
	 * @param args
	 *            Description of the Parameter
	 * @param out
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception Exception
	 *                Description of the Exception
	 */
	public static int execute(String a, PrintWriter out) throws Exception {
		String osName = System.getProperty("os.name");
		String args = a;
		if (osName.equals("Windows NT")) {
			args = "cmd.exe /C " + args;
		} else if (osName.equals("Windows 95")) {
			args = "command.exe /C " + args;
		} else if (osName.equals("Windows 2000")) {
			args = "cmd.exe /C " + args;
		}
		StringTokenizer st = new StringTokenizer(args);
		int i = 0;
		String tok = "";
		while (st.hasMoreTokens()) {
			tok = st.nextToken();
			i++;
		}
		String[] cmd = new String[i];
		st = new StringTokenizer(args);
		i = 0;
		while (st.hasMoreTokens()) {
			tok = st.nextToken();
			cmd[i++] = tok;
		}
		Runtime rt = Runtime.getRuntime();
		Process proc = rt.exec(cmd);
		NativeExec errorGobbler = new NativeExec(proc.getErrorStream(), "ERROR", null);
		NativeExec outputGobbler = new NativeExec(proc.getInputStream(), "OUTPUT", out);
		errorGobbler.start();
		outputGobbler.start();
		outputGobbler.join();
		return proc.waitFor();
	}

	public static NativeExec getInstance() {
		return _theInstance;
	}

	/**
	 * Description of the Method
	 *
	 * @param args
	 *            Description of the Parameter
	 */
	public static void main(String args[]) {
		if (args.length < 1) {
			System.out.println("USAGE: java nativeExec <cmd>");
			System.exit(1);
		}
		try {
			int exitVal = execute(args[0]);
			System.out.println("ExitValue: " + exitVal);
		} catch (Exception t) {
			t.printStackTrace();
		}
	}

	InputStream is;
	PrintWriter outputw = new PrintWriter(System.out);
	String type;

	private NativeExec() {
	}

	/**
	 * Constructor for the NativeExec object
	 *
	 * @param is
	 *            Description of the Parameter
	 * @param type
	 *            Description of the Parameter
	 */
	NativeExec(InputStream is, String type) {
		this.is = is;
		this.type = type;
	}

	/**
	 * Constructor for the NativeExec object
	 *
	 * @param is
	 *            Description of the Parameter
	 * @param type
	 *            Description of the Parameter
	 * @param outputw
	 *            Description of the Parameter
	 */
	NativeExec(InputStream is, String type, PrintWriter outputw) {
		this.is = is;
		this.type = type;
		if (outputw != null) {
			this.outputw = outputw;
		}
	}

	/**
	 * Main processing method for the NativeExec object
	 */
	public void run() {
		try {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				outputw.println(line);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
