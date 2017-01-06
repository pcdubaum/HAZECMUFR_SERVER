package HazecMUFR.Game;

import com.smartfoxserver.v2.api.ISFSMMOApi;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import com.smartfoxserver.v2.mmo.MMORoom;

import Assets.Item;
import HazecMUFR.Login.ZoneJoinEventHandler.CharData;

public class SwapItem extends  BaseClientRequestHandler{

	private ISFSMMOApi mmoAPi;
	ISFSObject response = new SFSObject();
	
	@Override
	public void handleClientRequest(User user, ISFSObject params) {
		// TODO Auto-generated method stub
	
		int slot1 = params.getInt("sl1");
		int id1 = params.getInt("id1");
		//int slot2 = params.getInt("sl2");
		//int id2 = params.getInt("id2");
		
		CharData data = (CharData) user.getProperty("CharData");
		
		for (Item item :  data.itemVault) {
			int id = item.getItemDbId();
			if(id == id1)
			{
				item.setItemSlot(slot1);
				break;
			}
		}
	}
}
