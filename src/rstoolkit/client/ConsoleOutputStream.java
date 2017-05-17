package rstoolkit.client;

import java.io.IOException;
import java.io.OutputStream;

public class ConsoleOutputStream extends OutputStream {

	private OutputStream stdOut;
	private Console console;
	
	public ConsoleOutputStream(OutputStream stdOut) {
		this.stdOut = stdOut;
	}
	
	@Override
	public void write(int b) throws IOException {
		stdOut.write(b);
	       // redirects data to the text area
		if (console != null) {
			console.append(String.valueOf((char)b));
	        // keeps the textArea up to date
			console.update(console.getGraphics());
		}
	}
	
	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		stdOut.write(b, off, len);
		if (console != null) {
			console.append(new String(b, off, len));
	        // keeps the textArea up to date
			console.update(console.getGraphics());
		}
	}
	
	public void setConsole(Console console) {
		this.console = console;
	}

}
