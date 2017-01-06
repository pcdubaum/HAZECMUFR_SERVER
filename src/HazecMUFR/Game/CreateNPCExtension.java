package HazecMUFR.Game;

import com.smartfoxserver.v2.core.SFSEventType;

import com.smartfoxserver.v2.extensions.SFSExtension;


public class CreateNPCExtension extends SFSExtension {

	

	@Override
	public void init() {
		
		addEventHandler(SFSEventType.SERVER_READY, CreateAllNPC.class);

		
	}
}
