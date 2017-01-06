package HazecMUFR.Game;

import java.sql.SQLException;
import com.smartfoxserver.v2.db.IDBManager;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import com.smartfoxserver.v2.extensions.ExtensionLogLevel;

public class GetPeopleHandler extends BaseClientRequestHandler
{
	@Override
	public void handleClientRequest(User sender, ISFSObject params)
	{
		String userName = (String) sender.getName();
		IDBManager dbManager = getParentExtension().getParentZone().getDBManager();
		
		String charsSql = "SELECT `id`, `nome` , `classe` , `level`, `experiencia`, `pontos`, `mapa` , `agilidade` , `vitalidade` , `energia` , `forca`, `posx`, `posz`, `ouro`, `hp`, `mana`  FROM `personagens` WHERE `usuario` = '" + userName + "'";
		String itensSql = "SELECT * FROM `itens` WHERE `dono` = '" + userName + "'";
			
		try
        {
	        ISFSArray res = dbManager.executeQuery(charsSql, new Object[] {});
	        
	        // Populate the response parameters
	        ISFSObject response = new SFSObject();
	        response.putSFSArray("chars", res);
	        
	        //trace("Start Dump");
	        //trace(response.getDump());
	        //trace("Finish Dump");
	        
	        // Send back to requester
	        send("chars2", response, sender);
	        
	       

        }
        catch (SQLException e)
        {
	        trace(ExtensionLogLevel.WARN, "SQL Failed: " + e.toString());
        }
	}
}

