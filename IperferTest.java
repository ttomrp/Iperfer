import static org.junit.Assert.assertEquals;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IperferTest 
{
	/*
	 * setup that's necessary for capturing standard output in unit tests
	 * https://stackoverflow.com/a/1119559
	 */
	final PrintStream standardOut = System.out;
	final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	
    @BeforeEach
    public void setUpOutputStream() 
    {
    	System.setOut(new PrintStream(outputStream));
    }
    
    @AfterEach
    public void restoreStandardOutputStream()
    {
    	System.setOut(standardOut);
    }
    
	@Test
    public void programInvokedWithNoArguments() throws Exception
    {
    	String[] args = new String[0];
    	Iperfer.main(args);	
    	assertEquals("Error: missing or additional arguments", outputStream.toString().trim());
    }
	
	@Test
    public void programInvokedWithSomeNotAllArgumentsServerMode() throws Exception
    {
    	String[] args = new String[1];
    	args[0] = "-s";
    	Iperfer.main(args);	
    	assertEquals("Error: missing or additional arguments", outputStream.toString().trim());
    }
	
	@Test
    public void programInvokedWithSomeNotAllArgumentsClientMode() throws Exception
    {
    	String[] args = new String[2];
    	args[0] = "-c";
    	args[1] = "-h";
    	Iperfer.main(args);	
    	assertEquals("Error: missing or additional arguments", outputStream.toString().trim());
    }
	
	@Test
    public void nonnumericPortNumberServerMode() throws Exception
    {
    	String[] args = new String[3];
    	args[0] = "-s";
    	args[1] = "-p";
    	args[2] = "abc";
    	Iperfer.main(args);	
    	assertEquals("Error: port number not converted to int", outputStream.toString().trim());
    }
	
	@Test
    public void nonnumericPortNumberClientMode() throws Exception
    {
    	String[] args = new String[7];
    	args[0] = "-c";
    	args[1] = "-h";
    	args[2] = "hostname";
    	args[3] = "-p";
    	args[4] = "abc";
    	args[5] = "-t";
    	args[6] = "10";
    	Iperfer.main(args);	
    	assertEquals("Error: port number not converted to int", outputStream.toString().trim());
    }
	
	@Test
    public void nullHostName() throws Exception
    {
    	String[] args = new String[7];
    	args[0] = "-c";
    	args[1] = "-h";
    	args[2] = null;
    	Iperfer.main(args);	
    	assertEquals("Error: host name cannot be null", outputStream.toString().trim());
    }
	
	@Test
    public void portNumberNotInRangeServerMode() throws Exception
    {
    	String[] args = new String[3];
    	args[0] = "-s";
    	args[1] = "-p";
    	args[2] = "123";
    	Iperfer.main(args);	
    	assertEquals("Error: port number must be in the range 1024 to 65535", outputStream.toString().trim());
    }
	
	@Test
    public void portNumberNotInRangeClientMode() throws Exception
    {
    	String[] args = new String[7];
    	args[0] = "-c";
    	args[1] = "-h";
    	args[2] = "hostname";
    	args[3] = "-p";
    	args[4] = "123";
    	args[5] = "-t";
    	args[6] = "10";
    	Iperfer.main(args);	
    	assertEquals("Error: port number must be in the range 1024 to 65535", outputStream.toString().trim());
    }
	
	@Test
    public void nonnumericTimeValue() throws Exception
    {
    	String[] args = new String[7];
    	args[0] = "-c";
    	args[1] = "-h";
    	args[2] = "hostname";
    	args[3] = "-p";
    	args[4] = "12345";
    	args[5] = "-t";
    	args[6] = "abc";
    	Iperfer.main(args);	
    	assertEquals("Error: time not converted to int", outputStream.toString().trim());
    }
}
