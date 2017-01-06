package HazecMUFR.Login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.smartfoxserver.bitswarm.sessions.ISession;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.db.IDBManager;
import com.smartfoxserver.v2.exceptions.SFSErrorCode;
import com.smartfoxserver.v2.exceptions.SFSErrorData;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.exceptions.SFSLoginException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

public class LoginEventHandler extends BaseServerEventHandler {
	@Override
	public void handleServerEvent(ISFSEvent event) throws SFSException {
		// Grab parameters from client request
		String userName = (String) event.getParameter(SFSEventParam.LOGIN_NAME);
		String cryptedPass = (String) event.getParameter(SFSEventParam.LOGIN_PASSWORD);
		ISession session = (ISession) event.getParameter(SFSEventParam.SESSION);

		// Get password from DB
		IDBManager dbManager = getParentExtension().getParentZone().getDBManager();
		Connection connection = null;

		try {
			// Grab a connection from the DBManager connection pool
			connection = dbManager.getConnection();

			// Build a prepared statement
			PreparedStatement stmt = connection.prepareStatement("SELECT `senha` FROM `usuario` WHERE `nome` = ?");
			stmt.setString(1, userName);

			// Execute query
			ResultSet res = stmt.executeQuery();

			// Verify that one record was found
			if (!res.first()) {
				// This is the part that goes to the client
				SFSErrorData errData = new SFSErrorData(SFSErrorCode.LOGIN_BAD_USERNAME);
				errData.addParameter(userName);

				// This is logged on the server side
				throw new SFSLoginException("Bad user name: " + userName, errData);
			}

			String dbPword = res.getString("senha");

			// Verify the secure password
			if (!getApi().checkSecurePassword(session, dbPword, cryptedPass)) {
				SFSErrorData data = new SFSErrorData(SFSErrorCode.LOGIN_BAD_PASSWORD);
				data.addParameter(userName);

				throw new SFSLoginException("Login failed for user: " + userName, data);
			}

			// session.setProperty("DBid", res.getInt("id"));
		}

		// User name was not found
		catch (SQLException e) {
			SFSErrorData errData = new SFSErrorData(SFSErrorCode.GENERIC_ERROR);
			errData.addParameter("SQL Error: " + e.getMessage());

			throw new SFSLoginException("A SQL Error occurred: " + e.getMessage(), errData);
		}

		finally {
			// Return connection to the DBManager connection pool
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
}
