package HazecMUFR.Game;

import java.nio.file.SecureDirectoryStream;
import java.sql.SQLException;
import java.util.Arrays;

import com.smartfoxserver.v2.SmartFoxServer;
import com.smartfoxserver.v2.api.ISFSMMOApi;
import com.smartfoxserver.v2.db.IDBManager;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.entities.variables.SFSUserVariable;
import com.smartfoxserver.v2.entities.variables.UserVariable;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import com.smartfoxserver.v2.extensions.ExtensionLogLevel;
import com.smartfoxserver.v2.mmo.Vec3D;

import Assets.Item;
import Assets.Item.EquipmentType;
import HazecMUFR.Login.ZoneJoinEventHandler.CharData;

public class SelectCharHandler extends BaseClientRequestHandler {
	
	private ISFSMMOApi mmoAPi;
	
	@Override
	public void handleClientRequest(User sender, ISFSObject params) {
		
		String charName = params.getUtfString("n");
		IDBManager dbManager = getParentExtension().getParentZone().getDBManager();
		mmoAPi = SmartFoxServer.getInstance().getAPIManager().getMMOApi();
		CharData data = (CharData) sender.getProperty("CharData");
		
		try {

			// Build a prepared statement
			String sql = "SELECT * FROM `personagens` WHERE `nome` = '" + charName +"'";
			String sqlItems = "SELECT * FROM `itens` WHERE `dono` = '" + charName +"'";

			ISFSArray res = dbManager.executeQuery(sql, new Object[] {});
			ISFSArray thisItems = dbManager.executeQuery(sqlItems, new Object[] {});
			
			ISFSObject thisChar = res.getSFSObject(0);
			// public user var
			
			// Basic Information
			UserVariable name = new SFSUserVariable("na", charName);
		    UserVariable pClass = new SFSUserVariable("cl", thisChar.getInt("classe"));
		    UserVariable posx = new SFSUserVariable("x", (double)thisChar.getInt("posx"));
		    UserVariable posz = new SFSUserVariable("z", (double)thisChar.getInt("posz"));
		    UserVariable futurex = new SFSUserVariable("fx", (double)thisChar.getInt("posx"));
		    UserVariable futurez = new SFSUserVariable("fz", (double)thisChar.getInt("posz"));
		    
		    UserVariable level = new SFSUserVariable("lvl", thisChar.getInt("level"));
		    UserVariable mana = new SFSUserVariable("ma", thisChar.getInt("mana"));
		    UserVariable hp = new SFSUserVariable("hp", thisChar.getInt("hp"));

		    UserVariable map = new SFSUserVariable("map", thisChar.getInt("mapa"));
		    
			// stats
			UserVariable str = new SFSUserVariable("str", thisChar.getInt("forca"));
		    UserVariable vit = new SFSUserVariable("vit", thisChar.getInt("vitalidade"));
		    UserVariable agi = new SFSUserVariable("agi", thisChar.getInt("agilidade"));
		    UserVariable pow = new SFSUserVariable("pow", thisChar.getInt("energia"));
		    UserVariable points = new SFSUserVariable("poi", thisChar.getInt("pontos"));
		    UserVariable gold = new SFSUserVariable("go", thisChar.getInt("ouro"));
		    UserVariable experiencia = new SFSUserVariable("exp", thisChar.getInt("experiencia"));
		    		    
		    SFSArray items = new SFSArray();
		    
		    for (int i = 0; i < thisItems.size(); i++) {
				Item item = new Item(
						thisItems.getSFSObject(i).getInt("superclasse"), 
						thisItems.getSFSObject(i).getInt("classe"), 
						thisItems.getSFSObject(i).getInt("id"),
						thisItems.getSFSObject(i).getInt("raridade"), 
						thisItems.getSFSObject(i).getInt("sorte"), 
						thisItems.getSFSObject(i).getInt("option1"),
						thisItems.getSFSObject(i).getInt("option2"),
						thisItems.getSFSObject(i).getInt("option3"),
						thisItems.getSFSObject(i).getInt("option4"),
						thisItems.getSFSObject(i).getInt("posicao"),
						thisItems.getSFSObject(i).getInt("quantidade"),
						thisItems.getSFSObject(i).getInt("slot"),
						thisItems.getSFSObject(i).getInt("level"));
				
				SFSObject itemObj = new SFSObject();
				itemObj.putInt("ty", item.getTypeasInt());
				itemObj.putInt("in", item.getItemNumber());
				itemObj.putInt("id", item.getItemDbId());
				itemObj.putInt("ra", item.getrank());
				itemObj.putInt("lv", item.getItemLevel());
				itemObj.putBool("lu", item.isLuck());
				itemObj.putInt("o1", item.getOption1());
				itemObj.putInt("o2", item.getOption2());
				itemObj.putInt("o3", item.getOption3());
				itemObj.putInt("o4", item.getOption4());
				itemObj.putInt("po", item.getPos());
				itemObj.putInt("qu", item.getItemQuantity());
				itemObj.putInt("sl", item.getItemSlot());
				
				
				data.itemVault.add(item);
				items.addSFSObject(itemObj);
		    }
		    
		    
		    // Set this information as private(will not propagate to otherPlayers)
		    str.setHidden(true);
		    vit.setHidden(true);
		    agi.setHidden(true);
		    pow.setHidden(true);
		    points.setHidden(true);
		    experiencia.setHidden(true);
		    //itemsVault.setHidden(true);
		     
		    // Set variables via the server side API
		    getApi().setUserVariables(
		    		sender, 
		    		Arrays.asList(
		    				name, 
		    				pClass,
		    				posx,
		    				posz,
		    				futurex,
		    				futurez,
		    				gold,
		    				map,
		    				mana,
		    				hp,
		    				str,
		    				vit,
		    				agi,
		    				pow,
		    				points,
		    				experiencia,
		    				level));
		        
		    Vec3D pos = new Vec3D(thisChar.getInt("posx"), 0.0f, thisChar.getInt("posz"));
		    
		    // Populate the response parameters
		    ISFSObject response = new SFSObject();
		    response.putSFSArray("char", res);
		    
		    ISFSObject response2 = new SFSObject();
		    response2.putSFSArray("items", items);
		        
		    // Send back to requester
		    //mmoAPi.setUserPosition(sender, pos, sender.getLastJoinedRoom());
		    send("char", response, sender);
		    send("items", response2, sender);
		}   catch (SQLException e)
        {
	        trace(ExtensionLogLevel.WARN, "SQL Failed: " + e.toString());
        }
	}

}
