package HazecMUFR.Game;

import java.util.Arrays;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.entities.variables.SFSUserVariable;
import com.smartfoxserver.v2.entities.variables.UserVariable;
import com.smartfoxserver.v2.exceptions.SFSVariableException;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class AddPointHandler extends BaseClientRequestHandler {

	@Override
	public void handleClientRequest(User sender, ISFSObject params) {

		int pointType = params.getInt("t");
		int restingPoints = sender.getVariable("poi").getIntValue();
		ISFSObject response = new SFSObject();

		if (restingPoints > 0) {

			response.putInt("point", pointType);

			// Send back to requester
			send("add", response, sender);

			SFSUserVariable points = new SFSUserVariable("poi", restingPoints - 1);
			SFSUserVariable addTo;
			
			switch (pointType) {
			case 0:
				addTo = new SFSUserVariable("str", sender.getVariable("str").getIntValue() + 1);
				break;
			case 1:
				addTo = new SFSUserVariable("agi", sender.getVariable("agi").getIntValue() + 1);
				break;
			case 2:
				addTo = new SFSUserVariable("vit", sender.getVariable("vit").getIntValue() + 1);
				break;
			case 3:
				addTo = new SFSUserVariable("pow", sender.getVariable("pow").getIntValue() + 1);
				break;

			default:
				addTo = new SFSUserVariable("pow", sender.getVariable("pow").getIntValue());
				break;
			}

			getApi().setUserVariables(
			    		sender, 
			    		Arrays.asList(points, addTo));
		}
	}
}
