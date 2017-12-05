package com.ef;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ParserTest extends TestCase {
	
	Parser parser = new Parser();

	/**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ParserTest( String testName ) {
        super( testName );
    }
    
    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite( ParserTest.class );
    }
    
    /**
     * Rigourous Test :-)
     */
    public void testParser() {
        assertTrue( true );
    }
    
    public void testValidateInputArguments() {
        assertTrue( parser.validateInputArguments("2017-01-01.15:00:00", "hourly", "200") );
    }
    
}
