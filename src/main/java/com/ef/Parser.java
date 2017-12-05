package com.ef;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 * @author amit.kumar
 * Main Parser Class to read, parse & load the log data file
 */
public class Parser {

	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss");
	
	/**
	 * This function is used to create a HQL to fetch the IPs from the table as per given input command
	 * @param startDateStr
	 * @param duration
	 * @param threshold
	 * @return Query String
	 */
	private String constructQuery(String startDateStr, String duration, int threshold) {
		Calendar cal = Calendar.getInstance();
		String endDateStr = null;
		String query = null;
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = dateFormat.parse(startDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			System.exit(1);
		}
		if(duration.equalsIgnoreCase("hourly")) {
			cal.setTime(startDate);
			cal.add(Calendar.HOUR_OF_DAY,1);
			endDate = cal.getTime();
		} else {
			cal.setTime(startDate);
			cal.add(Calendar.DATE,1);
			endDate = cal.getTime();
		}
		endDateStr = dateFormat.format(endDate);
		query = "FROM ServerLog where logDateTime between '" + startDateStr.replace('.', ' ') + "' AND '" + endDateStr.replace('.', ' ') + "' GROUP BY clientIp HAVING COUNT(clientIp) >=" + threshold;
		return query;
	}
	

	/**
	 * This function is used to validate the input command argument
	 * @param startDate
	 * @param duration
	 * @param threshold
	 * @return boolean to determine whether validation is successful or not
	 */
	public boolean validateInputArguments(String startDate, String duration, String threshold) {
		boolean validation = true;
		try {
			dateFormat.parse(startDate);
		} catch (ParseException e) {
			validation = false;
			System.err.println("Error : StartDate format should be - yyyy-MM-dd.HH:mm:ss");
		}
		if(!duration.equalsIgnoreCase("hourly") && !duration.equalsIgnoreCase("daily")) {
			validation = false;
			System.err.println("Error : Duration should be either hourly or daily only.");
		}
		try {
			int thres = (int)Integer.valueOf(threshold);
			if(thres <= 0) {
				validation = false;
				System.err.println("Error : Threshold should only be an integer value.");
			}
		} catch (Exception exp) {
			validation = false;
			System.err.println("Error : Threshold should only be an integer value.");
		}
		return validation;
	}
	
	
	/**
	 * Used to read and parse the log file using FileReader & Scanner classes. Reading one line at a time to avoid out of memory error for large file.
	 * We assumed that the log file name and location is -  /target/access.log
	 * @return List of ServerLog which needs to be store in the database
	 */
	private List<ServerLog> parseLogFile() {
		FileReader fileReader = null;
        Scanner scanner = null;
        StringTokenizer strTokenizer = null;
        List<ServerLog> list = new ArrayList<ServerLog>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			System.out.println("Reading and parsing log file data..."); 
        	fileReader = new FileReader("/target/access.log");
            scanner = new Scanner(fileReader);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                strTokenizer = new StringTokenizer(line, "|");
                //Create a ServerLog object for each line in the file
        		while (strTokenizer.hasMoreElements()) {
        			list.add(new ServerLog(dateFormat.parse(strTokenizer.nextElement().toString().trim()), 
        					strTokenizer.nextElement().toString().trim(), strTokenizer.nextElement().toString().trim(), 
        					Short.parseShort(strTokenizer.nextElement().toString().trim()), strTokenizer.nextElement().toString().trim()));
        		}
            }
            // note that Scanner suppresses exceptions
            if (scanner.ioException() != null) {
                throw scanner.ioException();
            }
        }catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
            if (fileReader != null) {
                try {
                	fileReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
            if (scanner != null) {
            	scanner.close();
            }
        }
		return list;
	}
	
	

	/**
	 * This function is used to fetch the IPs from the database table as per the input command and also save the result into another database table
	 * @param startDate
	 * @param duration
	 * @param threashold
	 * @param serverLogDAO
	 */
	private void executeCommand(String startDate, String duration, int threashold, ServerLogDAO serverLogDAO) {
		
		ParserRunDAO parserRunDAO = new ParserRunDAO();
		//Fetch IPs as per input arguments/command
		List<ServerLog> list = serverLogDAO.findByQuery(constructQuery(startDate, duration, threashold));
        
		if(list != null && list.size() > 0) {
        	ParserRun parserRun = null;
        	int maxParserRunId = parserRunDAO.fetchMaxRunId();
        	System.out.println("List of blocked IPs:");
        	String command = "--startDate="+startDate+" --duration="+duration+" --threshold="+threashold;
        	String comments = "IP is blocked for further request as it has exceded the max request limit";
        	
        	for(ServerLog log : list) {
        		System.out.println(log.getClientIp()); 
        		parserRun = new ParserRun(new ParserRunId(maxParserRunId + 1, log.getClientIp()), command, comments);
        		
        		//As per requirement save the run data in another table in the same database. Table name is - Parser_Run.
        		parserRunDAO.persist(parserRun);
        	}
        	
        }else {
        	System.out.println("No IP found for the given command."); 
        }
		
	}
	
	
	
    /**
     * This is the main function which is getting called from JVM
     * @param args
     */
    public static void main( String[] args ) {
    	
    	Parser parser = new Parser();
    	Options options = new Options();
    	Option startDate = new Option("startDate", "startDate", true, "Start date");
    	startDate.setRequired(true);
        options.addOption(startDate);

        Option duration = new Option("duration", "duration", true, "duration");
        duration.setRequired(true);
        options.addOption(duration);

        Option threshold = new Option("threshold", "threshold", true, "threshold");
        threshold.setRequired(true);
        options.addOption(threshold);

        CommandLineParser cmdLineParser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
			cmd = cmdLineParser.parse(options, args);
        } catch (org.apache.commons.cli.ParseException e) {
            System.err.println(e.getMessage());
            formatter.printHelp("com.ef.Parser", options);
            System.exit(1);
            return;
        }

        //Validating input arguments
        System.out.println("Validating Input Arguments...");
        if(!parser.validateInputArguments(cmd.getOptionValue("startDate").trim(), cmd.getOptionValue("duration").trim(),cmd.getOptionValue("threshold").trim())) {
        	System.err.println("Input Arguments Validation Failed.");
        	System.exit(1);
        }else {
        	System.out.println("Input Arguments Validation Completed Successfuly.");
        }
        
        String startDateValue = cmd.getOptionValue("startDate").trim();
        String durationValue = cmd.getOptionValue("duration").trim();
        int thresholdValue = (int)Integer.valueOf(cmd.getOptionValue("threshold").trim());
    	
        //Reading and parsing the log file
        List<ServerLog> list = parser.parseLogFile();
        
        ServerLogDAO serverLogDAO = new ServerLogDAO();
        //Deleting all previous log record
        serverLogDAO.deleteAll();
        
        //Loading log file data into MySQL database using Hibernate.
        serverLogDAO.persistAll(list);
        
        //Find the IPs against the log data as per input argument/command
        parser.executeCommand(startDateValue, durationValue, thresholdValue, serverLogDAO);
        
        System.exit(0);
    }

}
