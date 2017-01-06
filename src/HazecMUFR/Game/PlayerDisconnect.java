package HazecMUFR.Game;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.db.IDBManager;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

import Assets.Item;
import Assets.Item.EquipmentType;
import HazecMUFR.Login.ZoneJoinEventHandler.CharData;

public class PlayerDisconnect extends BaseServerEventHandler {

	public void handleServerEvent(ISFSEvent event) throws SFSException {

		User sender = (User) event.getParameter(SFSEventParam.USER);

		// Get password from DB
		IDBManager dbManager = getParentExtension().getParentZone().getDBManager();
		Connection connection = null;

		trace("\nSalvando");
		
		//if (sender.getLastJoinedRoom() != null)
			try {

				// Grab a connection from the DBManager connection pool
				connection = dbManager.getConnection();

				PreparedStatement stmt = connection.prepareStatement("UPDATE `personagens` SET " + "`classe`= ?,"
						+ "`level`= ?," + "`experiencia`= ?," + "`forca`= ?," + "`agilidade`= ?," + "`vitalidade`= ?,"
						+ "`energia`= ?," + "`mana`= ?," + "`hp`= ?," + "`pontos`= ?," + "`ouro`= ?," + "`mapa`= ?,"
						+ "`posx`= ?," + "`posz`= ? " + "WHERE `nome` = '" + sender.getVariable("na").getStringValue()
						+ "'");
				stmt.setInt(1, sender.getVariable("cl").getIntValue());
				stmt.setInt(2, sender.getVariable("lvl").getIntValue());
				stmt.setInt(3, sender.getVariable("exp").getIntValue());
				stmt.setInt(4, sender.getVariable("str").getIntValue());
				stmt.setInt(5, sender.getVariable("agi").getIntValue());
				stmt.setInt(6, sender.getVariable("vit").getIntValue());
				stmt.setInt(7, sender.getVariable("pow").getIntValue());
				stmt.setInt(8, sender.getVariable("ma").getIntValue());
				stmt.setInt(9, sender.getVariable("hp").getIntValue());
				stmt.setInt(10, sender.getVariable("poi").getIntValue());
				stmt.setInt(11, sender.getVariable("go").getIntValue());
				stmt.setInt(12, sender.getVariable("map").getIntValue());
				stmt.setInt(13, sender.getVariable("x").getDoubleValue().intValue());
				stmt.setInt(14, sender.getVariable("z").getDoubleValue().intValue());

				stmt.execute();
				
				CharData data = (CharData) sender.getProperty("CharData");
				
				for (Item item :  data.itemVault) {
										
					 stmt = connection.prepareStatement("INSERT INTO `itens`(  `classe`, `superclasse`, `dono`, `option1`, "
					 		+ "`raridade`, `sorte`, `option2`, `option3`, `option4`, `posicao`, `quantidade` , `slot` , `id`) VALUES "
					 		+ "(?, ?, ?, ?, ?,"
					 		+ "?, ?, ?, ?, ?, ?,?,?) ON DUPLICATE KEY UPDATE `slot` = values(slot)");
					 
					 stmt.setInt(1, item.getItemNumber());
					 stmt.setInt(2, item.getTypeasInt());
					 stmt.setString(3, sender.getVariable("na").getStringValue());
					 stmt.setInt(4, item.getOption1());
					 stmt.setInt(5, item.getrank());
					 stmt.setInt(6, 0);
					 stmt.setInt(7, item.getOption2());
					 stmt.setInt(8, item.getOption3());
					 stmt.setInt(9, item.getOption4());
					 stmt.setInt(10, item.getPos());
					 stmt.setInt(11, item.getItemQuantity());
					 stmt.setInt(12, item.getItemSlot());
					 stmt.setInt(13, item.getItemDbId());
						
					 stmt.execute();
				}
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				trace(e.toString());
		}
	}

	private void trace(Object printStackTrace) {
		// TODO Auto-generated method stub
		
	}
}
