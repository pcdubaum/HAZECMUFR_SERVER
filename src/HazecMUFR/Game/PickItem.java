package HazecMUFR.Game;

import java.util.Arrays;
import java.util.List;

import com.smartfoxserver.v2.api.ISFSMMOApi;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.entities.variables.SFSUserVariable;
import com.smartfoxserver.v2.entities.variables.UserVariable;
import com.smartfoxserver.v2.exceptions.SFSVariableException;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import com.smartfoxserver.v2.mmo.BaseMMOItem;
import com.smartfoxserver.v2.mmo.MMOItem;
import com.smartfoxserver.v2.mmo.MMORoom;

import Assets.Item;
import Assets.Item.EquipmentType;
import HazecMUFR.Login.ZoneJoinEventHandler.CharData;;

public class PickItem extends BaseClientRequestHandler{

	private ISFSMMOApi mmoAPi;
	ISFSObject response = new SFSObject();
	
	@Override
	public void handleClientRequest(User user, ISFSObject params) {
		// TODO Auto-generated method stub
	
		int itemID = params.getInt("id");
		int itemSlot = params.getInt("sl");
		
		MMORoom room = user.getCurrentMMORoom();
		Item item  =  (Item) room.getMMOItemById(itemID);
		item.setItemSlot(itemSlot);
		
		ISFSObject response = new SFSObject();
		SFSArray items = new SFSArray();
		SFSObject itemObj = new SFSObject();
		CharData data = (CharData) user.getProperty("CharData");
			
		switch (item.getType()) {
		case Jewels:
			// Manage Server events
			SFSUserVariable gold = new SFSUserVariable("go",  user.getVariable("go").getIntValue() + item.getOption1());
			getApi().setUserVariables(user, Arrays.asList(gold));
			
			// Manage client response
			response.putInt("equipType", item.getTypeasInt());
			response.putInt("baseClass", item.getItemNumber());
			response.putInt("total", user.getVariable("go").getIntValue());
			break;
			
		case Weapon_MainHand:
		case Gloves:
		case Chest:
		case Head:
		case Boots:
		case Pants:
			items = new SFSArray();
			
			itemObj = new SFSObject();
			response.putInt("ty", item.getTypeasInt());
			response.putInt("in", item.getItemNumber());
			response.putInt("id", item.getItemDbId());
			response.putInt("lv", item.getItemLevel());
			response.putInt("ra", item.getrank());
			response.putBool("lu", item.isLuck());
			response.putInt("o1", item.getOption1());
			response.putInt("o2", item.getOption2());
			response.putInt("o3", item.getOption3());
			response.putInt("o4", item.getOption4());
			response.putInt("po", 0);
			response.putInt("sl", item.getItemSlot());
			
			items.addSFSObject(itemObj);
					
			response.putSFSArray("items", items);
			
			data.itemVault.add(item);
			break;
		case Consumable:
			items = new SFSArray();
			
			itemObj = new SFSObject();
			response.putInt("ty", item.getTypeasInt());
			response.putInt("in", item.getItemNumber());
			response.putInt("id", item.getItemDbId());
			response.putInt("po", 0);
			
			items.addSFSObject(itemObj);
					
			response.putSFSArray("items", items);
			
			data.itemVault.add(item);
			break;

		default:
			break;
		}
		
		
		
		trace("\nItem Adicionado id:" + response.getInt("id"));
		room.removeMMOItem(item);
		
		// Send back to requester
	     send("items", response, user);
	     
	}
}
