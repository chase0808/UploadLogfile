package htmlLogfileExtractor;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class LogfileExtractor {

	public static void uploadDatabase(Connection con,
			ArrayList<Transaction> transactions) {

		System.out.println(transactions.size());
		PreparedStatement stmt = null;
		for (Transaction transaction : transactions) {
			try {
				String sql = "insert into loginfo.tloginfo(clientIpAddress, unknown, remoteLogname, authenticatedUsername,requestTimestamp, "
						+ "requestLine, statusCode, bytes, referrer, userAgent, filename, requestMethod, transportProtocol, "
						+ "requestStem, requestQueryString) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				stmt = con.prepareStatement(sql);
				stmt.setString(1, transaction.getClientIpAddress());
				stmt.setString(2, transaction.getUnknown());
				stmt.setString(3, transaction.getRemoteLogname());
				stmt.setString(4, transaction.getAuthenticatedUsername());
				stmt.setString(5, transaction.getRequestTimestamp());
				stmt.setString(6, transaction.getRequestLine());
				stmt.setString(7, transaction.getStatusCode());
				stmt.setString(8, transaction.getBytes());
				stmt.setString(9, transaction.getReferrer());
				stmt.setString(10, transaction.getUserAgent());
				stmt.setString(11, transaction.getFilename());
				stmt.setString(12, transaction.getRequestMethod());
				stmt.setString(13, transaction.getTransportProtocol());
				stmt.setString(14, transaction.getRequestStem());
				stmt.setString(15, transaction.getRequestQueryString());
				stmt.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
		System.out.println("Upload done!");
	}

	public static void skip(FileReader in, int number) throws IOException {
		for (int i = 0; i < number; i++) {
			int c = in.read();
		}
	}

	public static void getMore(Transaction t) {
		String target = t.getRequestLine();
		StringBuilder str = new StringBuilder();
		int status = 1;
		for (int i = 0; i < target.length(); i++) {
			char temp = target.charAt(i);
			switch (status) {
			// status 1 for the request method;
			case 1:
				if (temp == ' ') {
					t.setRequestMethod(str.toString());
					status = 2;
					str = new StringBuilder();
					break;
				}
				str.append(temp);
				break;
			// status 2 for the request stem;
			case 2:
				if (temp == '?') {
					t.setRequestStem(str.toString());
					status = 3;
					str = new StringBuilder();
					break;
				}
				if (temp == ' ') {
					t.setRequestStem(str.toString());
					status = 4;
					str = new StringBuilder();
					break;
				}
				str.append(temp);
				break;
			// status 3 for the request query string;
			case 3:
				if (temp == ' ') {
					t.setRequestQueryString("?" + str.toString());
					status = 4;
					str = new StringBuilder();
					break;
				}
				str.append(temp);
				break;
			case 4:
				break;
			}
			if (status == 4) {
				str.append(temp);
			}
		}
		t.setTransportProtocol(str.toString().substring(1,
				str.toString().length()));
		String requeststem = t.getRequestStem();
		for (int i = requeststem.length() - 1; i >= 0; i--) {
			if (requeststem.charAt(i) == '/') {
				StringBuilder filename = new StringBuilder();
				for (int j = i + 1; j < requeststem.length(); j++) {
					char temp = requeststem.charAt(j);
					filename.append(temp);
				}
				t.setFilename(filename.toString());
				break;
			}
		}
	}

	public static ArrayList<Transaction> infoExtractor(File filename)
			throws IOException {
		FileReader in = new FileReader(filename);

		int c;
		StringBuilder str = new StringBuilder();
		int status = 1;
		Transaction tr = new Transaction();
		ArrayList<Transaction> arr = new ArrayList<Transaction>();
		while ((c = in.read()) != -1) {
			char temp = (char) c;
			switch (status) {
			// status 1 for the client IP address;
			case 1:
				if (temp == ' ') {
					tr.setClientIpAddress(str.toString());
					status = 10;
					str = new StringBuilder();
					break;
				}
				str.append(temp);
				break;
			// status 10 for unknown;
			case 10:
				if (temp == ' ') {
					tr.setUnknown(str.toString());
					status = 2;
					str = new StringBuilder();
					break;
				}
				str.append(temp);
				break;
			// status 2 for the remote Logname;
			case 2:
				if (temp == ' ') {
					tr.setRemoteLogname(str.toString());
					status = 3;
					str = new StringBuilder();
					break;
				}
				str.append(temp);
				break;
			// status 3 for the authenticated username;
			case 3:
				if (temp == ' ') {
					tr.setAuthenticatedUsername(str.toString());
					status = 4;
					str = new StringBuilder();
					break;
				}
				str.append(temp);
				break;
			// status 4 for the requestTimestamp;
			case 4:
				if (temp == ']') {
					str.append(temp);
					tr.setRequestTimestamp(str.toString());
					status = 5;
					str = new StringBuilder();
					skip(in, 2);
					break;
				}
				str.append(temp);
				break;
			// status 5 for the request line;
			case 5:
				if (temp == '"') {
					tr.setRequestLine(str.toString());
					status = 6;
					str = new StringBuilder();
					skip(in, 1);
					break;
				}
				str.append(temp);
				break;
			// status 6 for the status code;
			case 6:
				if (temp == ' ') {
					tr.setStatusCode(str.toString());
					status = 7;
					str = new StringBuilder();
					break;
				}
				str.append(temp);
				break;
			// status 7 for the bytes sent from server to client;
			case 7:
				if (temp == ' ') {
					tr.setBytes(str.toString());
					status = 8;
					str = new StringBuilder();
					skip(in, 1);
					break;
				}
				str.append(temp);
				break;
			// status 8 for the referrer;
			case 8:
				if (temp == '"') {
					tr.setReferrer(str.toString());
					status = 9;
					str = new StringBuilder();
					skip(in, 2);
					break;
				}
				str.append(temp);
				break;
			// status 9 for the user agent;
			case 9:
				if (temp == '"') {
					tr.setUserAgent(str.toString());
					status = 1;
					str = new StringBuilder();
					getMore(tr);
					arr.add(tr);
					tr = new Transaction();
					break;
				}
				str.append(temp);
				break;
			}
		}

		in.close();
		System.out.println(filename + " Reading Successfully!");
		return arr;
	}

	public static void logFileExtrator(String folderLocation) {
		// ArrayList<Transaction> result = infoExtractor("test.txt");
		Connection con = null;
		try {
			Class.forName("com.ibm.as400.access.AS400JDBCDriver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			con = DriverManager.getConnection("jdbc:as400://zeus/zdbxdci005",
					"DCIINTERN", "qixin808");
			System.out.println(con.toString());
			String directoryPath = "C:\\weblog";
			File dir = new File(directoryPath);
			File[] directoryListing = dir.listFiles();
			if (directoryListing != null) {

				for (File child : directoryListing) {
					ArrayList<Transaction> result = infoExtractor(child);
					uploadDatabase(con, result);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// System.out.println(result.size());

	}

	public static void main(String[] args) throws IOException {
		logFileExtrator("Add the folder location Here");
	}
}
