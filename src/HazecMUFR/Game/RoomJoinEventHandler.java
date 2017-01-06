package HazecMUFR.Game;

import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

import Assets.Player;


public class RoomJoinEventHandler extends BaseServerEventHandler{
	

	@Override
	public void handleServerEvent(ISFSEvent event) throws SFSException {
		
		Player player = (Player) event.getParameter(SFSEventParam.USER);
	}

}
