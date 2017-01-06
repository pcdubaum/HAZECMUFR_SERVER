package HazecMUFR.Game;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.smartfoxserver.v2.db.IDBManager;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSErrorCode;
import com.smartfoxserver.v2.exceptions.SFSErrorData;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class CreateCharHandler extends BaseClientRequestHandler {
	@Override
	public void handleClientRequest(User sender, ISFSObject params) {
		
		String userName = sender.getName();
		String charName = params.getUtfString("n");
		int pClass = params.getInt("c");

		// Get password from DB
		IDBManager dbManager = getParentExtension().getParentZone().getDBManager();
		Connection connection = null;
		ISFSObject response = new SFSObject();
		
		if (charName.length() <= 3) {
			response.putUtfString("result", "nomePequeno");
			response.putUtfString("nome", charName);
			
			send("resultado", response, sender);
		} else {
			try {
				// Grab a connection from the DBManager connection pool
				connection = dbManager.getConnection();

				// Build a prepared statement
				PreparedStatement stmt = connection.prepareStatement("SELECT * FROM `personagens` WHERE `nome` = ?");
				stmt.setString(1, charName);

				// Execute query
				ResultSet res = stmt.executeQuery();

				// Verify that one record was found
				if (!res.first()) {
					
					int força = 0;
					int agilidade = 0;
					int vitalidade = 0;
					int energia = 0;
					
					// Important information must be done in code;
					if(pClass == 0)
					{
						força = 28;
						agilidade = 20;
						vitalidade = 25;
						energia = 10;
					}
					else if (pClass == 1){
						força = 18;
						agilidade = 18;
						vitalidade = 15;
						energia = 30;
					}
					else if (pClass == 2){
						força = 22;
						agilidade = 25;
						vitalidade = 20;
						energia = 15;
					}
					else if (pClass == 3){
						força = 21;
						agilidade = 21;
						vitalidade = 18;
						energia = 23;
					}
					else if (pClass == 4){
						força = 26;
						agilidade = 26;
						vitalidade = 26;
						energia = 16;
					}
					else if (pClass == 5){
						força = 26;
						agilidade = 26;
						vitalidade = 26;
						energia = 26;
					}
					else if (pClass == 6){
						força = 32;
						agilidade = 27;
						vitalidade = 25;
						energia = 22;
					}
					else if (pClass == 7){
						força = 30;
						agilidade = 30;
						vitalidade = 25;
						energia = 25;
					}
					
					stmt = connection.prepareStatement("INSERT INTO `personagens` (`usuario`, `nome`, `classe`, `experiencia`, `forca`, `agilidade`, `vitalidade`, `energia`, `level`, `posx`, `posz`) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
					stmt.setString(1, userName);
					stmt.setString(2, charName);
					stmt.setInt(3, pClass);
					stmt.setInt(4, 0);
					stmt.setInt(5, força);
					stmt.setInt(6, agilidade);
					stmt.setInt(7, vitalidade);
					stmt.setInt(8, energia);
					stmt.setInt(9, 1);
					stmt.setInt(10, 128);
					stmt.setInt(11, 79);
					
					stmt.execute();
					
			        response.putUtfString("result", "sucesso");
			        response.putUtfString("nome", charName);
			        response.putInt("classe", pClass);
					
					send("resultado", response, sender);
					
					trace("Enviado");
				}
				else{
					response.putUtfString("result", "nomeError");
					response.putUtfString("nome", charName);
					
					send("resultado", response, sender);
				}
			} catch (SQLException e) {
				SFSErrorData errData = new SFSErrorData(SFSErrorCode.GENERIC_ERROR);
				errData.addParameter("SQL Error: " + e.getMessage());
				
				response.putUtfString("result", errData.toString());
				
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
}
