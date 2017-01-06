package HazecMUFR.Login;

import java.util.ArrayList;
import java.util.List;

import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;

import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

import Assets.Item;
import Assets.Player;


public class ZoneJoinEventHandler extends BaseServerEventHandler
{
	public final static class CharData {
		public boolean alive;
		public int timer;
		public int movimentTimer;
		public int attackTimer;
		public int minAttack;
		public int maxAttack;

		public int hp;
		public int defense;

		public Item itemChest;
		public Item itemHead;
		public Item itemGloves;
		public Item itemPants;
		public Item itemBoots;
		public Item itemMainHand;
		public Item itemOffHand;
		public List<Item> itemVault;
		public Item itemStorage;
	}
	
	@Override
	public void handleServerEvent(ISFSEvent event) throws SFSException
	{
		User theUser = (User) event.getParameter(SFSEventParam.USER);
		CharData data = new CharData();
		data.itemVault = new ArrayList<Item>();
		
		theUser.setProperty("CharData", data);
				
		// Join the user	
		Room selection = getParentExtension().getParentZone().getRoomByName("CharSelection");
		
		if(!theUser.isNpc())
			getApi().joinRoom((User)theUser, selection);
		
	}
}