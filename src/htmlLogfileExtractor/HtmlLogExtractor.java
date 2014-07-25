package htmlLogfileExtractor;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlLogExtractor {
	public static void upload(Connection con,
			ArrayList<DeveloperLogTransaction> transctions) {
		for (DeveloperLogTransaction transaction : transctions) {
			try {
				PreparedStatement stmt = con
						.prepareStatement("insert into loginfo.tdeveloperlog(timestamp, level, category, message) values (?,?,?,?)");
				stmt.setString(1, transaction.getTimeStamp());
				stmt.setString(2, transaction.getLevel());
				stmt.setString(3, transaction.getCategory());
				stmt.setString(4, transaction.getMessage());
				stmt.execute();
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		System.out.println("upload Done!");
	}

	@SuppressWarnings("resource")
	public static void developerLogExtractor(String location) {
		try {
			FileInputStream fis = new FileInputStream(location);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					fis));
			String line;

			Pattern p = Pattern.compile("<td>(.*)</td>");
			Pattern pMessage = Pattern
					.compile("<td title=\"Message\">(.*)</td>");
			Pattern pLevel = Pattern.compile("\\d\">(.*)</font>");
			Pattern pCategory = Pattern
					.compile("com.dci.db4.interceptor.ActionLogger\\scategory\">(.*)</td>");
			ArrayList<String> timeStamp = new ArrayList<String>();
			ArrayList<String> messages = new ArrayList<String>();
			ArrayList<String> levels = new ArrayList<String>();
			ArrayList<String> categories = new ArrayList<String>();
			while ((line = reader.readLine()) != null) {
				while (line.substring(0, 3).equals("<td")
						&& !line.substring(line.length() - 5, line.length())
								.equals("</td>")) {
					line = line + reader.readLine();
				}

				Matcher m = p.matcher(line);
				Matcher mMessage = pMessage.matcher(line);
				Matcher mLevel = pLevel.matcher(line);
				Matcher mCategories = pCategory.matcher(line);
				if (m.find()) {
					String time = m.group(1);
					timeStamp.add(time);
				}
				if (mMessage.find()) {
					String mesg = mMessage.group(1);
					messages.add(mesg);
				}
				if (mLevel.find()) {
					String lel = mLevel.group(1);
					if (lel.charAt(0) == '<') {
						lel = lel.substring(8, lel.length() - 9);
						levels.add(lel);
					} else {
						levels.add(lel);
					}

				}
				if (mCategories.find()) {
					String cat = mCategories.group(1);
					categories.add(cat);
				}
			}

			System.out.println(timeStamp.size());
			System.out.println(messages.size());
			System.out.println(levels.size());
			System.out.println(categories.size());

			ArrayList<DeveloperLogTransaction> transactions = new ArrayList<DeveloperLogTransaction>();
			for (int i = 0; i < timeStamp.size(); i++) {
				DeveloperLogTransaction transaction = new DeveloperLogTransaction();
				transaction.setCategory(categories.get(i));
				transaction.setLevel(levels.get(i));
				transaction.setMessage(messages.get(i));
				transaction.setTimeStamp(timeStamp.get(i));
				transactions.add(transaction);
			}
			for (DeveloperLogTransaction t : transactions) {
				System.out.println(t.getMessage());
			}

			try {
				Connection con = null;
				Class.forName("com.ibm.as400.access.AS400JDBCDriver");
				con = DriverManager
						.getConnection("jdbc:as400://zeus/zdbxdci005",
								"DCIINTERN", "qixin808");
				upload(con, transactions);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		developerLogExtractor("Specify Your Location Here");
	}
}
