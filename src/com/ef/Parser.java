package com.ef;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * 
 * @author Pedro Henrique Veras Coelho
 *
 */
public class Parser {
	
	private static ConnectionManager connectionManager;
	
	public Parser() {
		try {
			connectionManager = new ConnectionManager();
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	public void parseFile(Map<String, String> params) throws IOException {
		String filename = params.get("accesslog");
		String date = params.get("startDate");
		String duration = params.get("duration");
		Integer threshold = Integer.parseInt(params.get("threshold"));
		
		LocalDateTime startDate = LocalDateTime.
				parse(date,DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss"));
		
		System.out.println("Please wait, processing the log file...");
		
		try (Stream<String> stream = Files.lines(Paths.get(filename))) {
			stream.forEach(line -> {
				LocalDateTime logDate = LocalDateTime
						.parse(line.split("\\|")[0],
								DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
				String ip = line.split("\\|")[1];
				
				AccessHistory accessHistory = new AccessHistory(ip,logDate);
				try {
					connectionManager.insert(accessHistory);
				} catch (LogParserException e) {
					e.printStackTrace();
				}
			});
		} catch (IOException e) {
			throw new IOException("Cannot read file "+filename);
		}
		
		System.out.println("========== List of ip's ==========" );
		AccessHistory accessHistory = new AccessHistory(startDate,
				startDate.plusHours(Duration.getDuration(duration)), threshold);
		try {
			List<String> ips = connectionManager.getIpsByDateThreshold(accessHistory);
			ips.forEach(System.out::println);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("===================================");
	}
	
	public void validateParams(Map<String,String> params) throws LogParserException {
		try {
			LocalDateTime.
			parse(params.get("startDate"),DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss"));
		} catch (Exception e) {
			throw new LogParserException("Please provide a valid startDate argument "
					+ "in the format yyyy-MM-dd.HH:mm:ss");
		}
		
		try {
			Duration.getDuration(params.get("duration"));
		} catch (Exception e) {
			throw new LogParserException("Please provide a valid duration argument. "
					+ "Allowed values are: hourly or daily");
		}
		
		try {
			Integer threshold = Integer.parseInt(params.get("threshold"));
			if (threshold <= 0) {
				throw new Exception();
			}
		} catch (Exception e) {
			throw new LogParserException("Please provide a valid value for "
					+ "threshold argument. It should be a value greater than 0");
		}
		
		try {
			Files.lines(Paths.get(params.get("accesslog")));
		} catch (Exception e ) {
			throw new LogParserException("Please provide a valid value for accesslog argument."
					+ " It should be a valid file");
		}
	}
	
	public static void main(String[] args) {
		Parser parser = new Parser();
		Map<String, String> params = new HashMap<>();
		Arrays.stream(args).map(arg -> {
			return arg.split("--")[1].split("=");
		}).forEach(arg -> {
			if (arg.length == 2) 
				params.put(arg[0], arg[1]);
		});

		try {
			parser.validateParams(params);
		} catch (LogParserException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
		
		try {
			parser.parseFile(params);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
