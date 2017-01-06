package HazecMUFR.Login;

import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.extensions.SFSExtension;

import HazecMUFR.Game.AddPointHandler;
import HazecMUFR.Game.AttackNPC;
import HazecMUFR.Game.CreateCharHandler;
import HazecMUFR.Game.DeletCharHandler;
import HazecMUFR.Game.GetPeopleHandler;
import HazecMUFR.Game.PickItem;
import HazecMUFR.Game.PlayerDisconnect;
import HazecMUFR.Game.RoomJoinEventHandler;
import HazecMUFR.Game.SelectCharHandler;
import HazecMUFR.Game.SwapItem;
import HazecMUFR.Game.UserVariablesHandler;;

public class ExtensionLogIn extends SFSExtension
{
	
	@Override
	public void init()
	{
		trace("Database Login Extension -- started");
		addEventHandler(SFSEventType.USER_LOGIN, LoginEventHandler.class);
		addEventHandler(SFSEventType.USER_JOIN_ZONE, ZoneJoinEventHandler.class);
		addEventHandler(SFSEventType.USER_DISCONNECT, PlayerDisconnect.class);
		//addEventHandler(SFSEventType.USER_JOIN_ROOM, RoomJoinEventHandler.class);
		addRequestHandler("GetChars", GetPeopleHandler.class);
		addRequestHandler("CreateChar", CreateCharHandler.class);
		addRequestHandler("SelectChar", SelectCharHandler.class);
		addEventHandler(SFSEventType.USER_VARIABLES_UPDATE, UserVariablesHandler.class);
		addRequestHandler("DeleteChar", DeletCharHandler.class);
		addRequestHandler("AttackNPC", AttackNPC.class);
		addRequestHandler("AddPoint", AddPointHandler.class);
		addRequestHandler("PickItem", PickItem.class);
		addRequestHandler("SwapItem", SwapItem.class);
	}
	
	@Override
	public void destroy()
	{
	   
	}
	
}
