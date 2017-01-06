package HazecMUFR.Game;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.smartfoxserver.v2.db.IDBManager;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSErrorCode;
import com.smartfoxserver.v2.exceptions.SFSErrorData;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class DeletCharHandler extends BaseClientRequestHandler {

	@Override
	public void handleClientRequest(User sender, ISFSObject params) {

		// Get password from DB
		IDBManager dbManager = getParentExtension().getParentZone().getDBManager();
		Connection connection = null;
		ISFSObject response = new SFSObject();

		String charName = params.getUtfString("n");

		try {
			// Grab a connection from the DBManager connection pool
			connection = dbManager.getConnection();

			// Build a prepared statement
			PreparedStatement stmt = connection.prepareStatement("DELETE FROM `personagens` WHERE `nome` = ?");
			stmt.setString(1, charName);

			stmt.execute();
			
			response.putUtfString("result", "deleted");

			send("resultado", response, sender);
			
			
		} catch (SQLException e) {
			SFSErrorData errData = new SFSErrorData(SFSErrorCode.GENERIC_ERROR);
			errData.addParameter("SQL Error: " + e.getMessage());

			response.putUtfString("result", "error");

			send("resultado", response, sender);

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
