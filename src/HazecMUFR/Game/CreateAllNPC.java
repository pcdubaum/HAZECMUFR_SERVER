package HazecMUFR.Game;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.omg.CORBA.VersionSpecHelper;

import com.smartfoxserver.v2.SmartFoxServer;
import com.smartfoxserver.v2.api.ISFSMMOApi;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.db.IDBManager;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.entities.variables.SFSUserVariable;
import com.smartfoxserver.v2.entities.variables.UserVariable;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;
import com.smartfoxserver.v2.extensions.ExtensionLogLevel;
import com.smartfoxserver.v2.mmo.IMMOItemVariable;
import com.smartfoxserver.v2.mmo.MMOItem;
import com.smartfoxserver.v2.mmo.MMOItemVariable;
import com.smartfoxserver.v2.mmo.MMORoom;
import com.smartfoxserver.v2.mmo.Vec3D;

import Assets.Item;
import Assets.Item.EquipmentType;
import Assets.Monster;

public class CreateAllNPC<NPCData> extends BaseServerEventHandler {

	private ISFSMMOApi mmoAPi;

	private NPCRunner npcRunner;
	private ScheduledFuture<?> npcRunnerTask;
	private List<User> allNpcs;

	ISFSObject response = new SFSObject();

	private Monster[] npcsdatas;
	private Item[] mmoItens;

	@Override
	public void handleServerEvent(ISFSEvent event) throws SFSException {

		npcRunner = new NPCRunner();
		mmoAPi = SmartFoxServer.getInstance().getAPIManager().getMMOApi();
		int lastId = -1;

		IDBManager dbManager = getParentExtension().getParentZone().getDBManager();
		String sql = "SELECT MAX(`id`) FROM `itens`";
		String monstersql = "SELECT * FROM `monsters_lorencia` ";

		try {
			// Obtain a resultset
			ISFSArray res = dbManager.executeQuery(sql, new Object[] {});
			ISFSArray monsteres = dbManager.executeQuery(monstersql, new Object[] {});
			
			if (res.contains(0)) {
				lastId = res.getInt(0);
			}
			
			int size = monsteres.size();
			
			npcsdatas = new Monster[size];
			
			for (int i = 0;  i < size; i++) {
				
				SFSObject obj = (SFSObject) monsteres.getSFSObject(i);
				
				npcsdatas[i] = new Monster(obj.getInt("id"), true, 0, 0, obj.getInt("hp"), obj.getInt("hpregen"), obj.getInt("mana") ,obj.getInt("manaregen"),
						obj.getInt("level"), 0, obj.getInt("mindmg"), obj.getInt("maxdmg"), obj.getInt("defense"), 0, 0, 10, 10, obj.getInt("skill"), obj.getInt("levelrange"), obj.getInt("spwntime"), obj.getUtfString("name")); 
						
			}
			
		} catch (SQLException e) {
			
			trace(ExtensionLogLevel.WARN, "SQL Failed: " + e.toString());
		}
		// -3, -3
		// 18, - 19

		try {
			createAdvancedNPCs(lastId);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void createAdvancedNPCs(int lastId) throws Exception {

		allNpcs = new LinkedList<User>();
		Random rnd = new Random();
	
		mmoItens = new Item[10];

		// Gold
		mmoItens[0] = new Item(EquipmentType.Jewels, 0, 0, 0, 300);

		// Short Sword, Kriss, florete, Falchion
		mmoItens[1] = new Item(EquipmentType.Weapon_MainHand, 0, 0, 0, 1);
		mmoItens[2] = new Item(EquipmentType.Weapon_MainHand, 1, 2, 0, 30);
		mmoItens[3] = new Item(EquipmentType.Weapon_MainHand, 2, 3, 0, 59);
		mmoItens[4] = new Item(EquipmentType.Weapon_MainHand, 3, 4, 0, 88);

		// Leather Set
		mmoItens[5] = new Item(EquipmentType.Chest, 0, 1, 0, 12);
		mmoItens[6] = new Item(EquipmentType.Gloves, 0, 1, 0, 1);
		mmoItens[7] = new Item(EquipmentType.Boots, 0, 1, 0, 4);
		mmoItens[8] = new Item(EquipmentType.Head, 0, 1, 0, 7);
		mmoItens[9] = new Item(EquipmentType.Pants, 0, 1, 0, 10);

		mmoItens[9] = new Item(EquipmentType.Consumable, 0, 0, 0, 1);
		
		Vec3D monsterPos[] = new Vec3D[18];
		
		monsterPos[0] = new Vec3D(109, 32, 15);
		monsterPos[1] = new Vec3D(147, 66, 15);
		
		monsterPos[2] = new Vec3D(77, 43, 15);
		monsterPos[3] = new Vec3D(116, 71, 15);
		
		monsterPos[4] = new Vec3D(51, 65, 15);
		monsterPos[5] = new Vec3D(84, 99, 15);
		
		monsterPos[6] = new Vec3D(50, 91, 15);
		monsterPos[7] = new Vec3D(73, 142, 15);
		
		monsterPos[8] = new Vec3D(70, 137, 15);
		monsterPos[9] = new Vec3D(144, 170, 15);
		
		monsterPos[10] = new Vec3D(181, 122, 15);
		monsterPos[11] = new Vec3D(200, 161, 15);
		
		monsterPos[12] = new Vec3D(102, 178, 15);
		monsterPos[13] = new Vec3D(108, 207, 15);
		
		monsterPos[14] = new Vec3D(202, 103, 15);
		monsterPos[15] = new Vec3D(224, 153, 15);
		
		monsterPos[16] = new Vec3D(81, 207, 15);
		monsterPos[17] = new Vec3D(98, 224, 15);
		
		MMORoom mmoRoom = (MMORoom) getParentExtension().getParentRoom();

		for (int j = 0; j < 4; j++) {
						
			for (int i = 0; i < mmoItens.length; i++) {
				npcsdatas[j].AddItemToAvaiablesItems(mmoItens[i]);
			}

			for (int i = 0; i < 16; i++) {

				User thisNpc = getApi().createNPC("NPC#" + (i + (j * 16)), getParentExtension().getParentZone(), false);

				int offsetX = monsterPos[j * 2 + 1].intX() -  monsterPos[j * 2].intX();
				int offsetZ = monsterPos[j * 2 + 1].intY() -  monsterPos[j * 2].intY();;

				int rndX = rnd.nextInt(offsetX) + monsterPos[j * 2].intX();
				int rndZ = rnd.nextInt(offsetZ) + monsterPos[j * 2].intY();

				List<UserVariable> uVars = Arrays.asList((UserVariable) new SFSUserVariable("npc", true),
						new SFSUserVariable("et", j), new SFSUserVariable("x", (double) rndX),
						new SFSUserVariable("z", (double) rndZ), new SFSUserVariable("fx", (double) rndX),
						new SFSUserVariable("fz", (double) rndZ), 
						new SFSUserVariable("hp", npcsdatas[j].GetMaxHP()),
						new SFSUserVariable("xp", 27 * j), new SFSUserVariable("al", true));

				

				lastId = lastId + 1;
				
				Monster data = new Monster(npcsdatas[j]);
				data.GetNewItem(lastId);

				//data.item = GetNewItem(npcsdatas[j].avaiableIten, lastId);

				thisNpc.setProperty("npcData", data);
				thisNpc.setVariables(uVars);

				// Prepare a list of variables for the MMOItem
				// List<IMMOItemVariable> variables = new
				// LinkedList<IMMOItemVariable>();
				// variables.add( new MMOItemVariable("id", data.item.getId())
				// );
				// variables.add( new MMOItemVariable("bc", itemBaseClasse) );
				// variables.add( new MMOItemVariable("sc", itemSuperClasse) );
				// variables.add( new MMOItemVariable("o1", 0) );
				// variables.add( new MMOItemVariable("ra", 0) );
				// variables.add( new MMOItemVariable("lu", 0) );

				// Create the MMOItem
				// data.item.setVariables(variables);

				allNpcs.add((User) thisNpc);

				// Join Room
				getApi().joinRoom((User) thisNpc, mmoRoom);

				// Set User Position
				mmoAPi.setUserPosition((User) thisNpc, new Vec3D(rndX, 1.0f, rndZ), mmoRoom);

				// Set Vars
				getApi().setUserVariables(thisNpc, data.GetVariables());
			}
		}
		// Start NPC Task
		npcRunnerTask = SmartFoxServer.getInstance().getTaskScheduler().scheduleAtFixedRate(npcRunner, 0, // 0
																											// initial
																											// //
																											// delay
				100, // run every 100ms
				TimeUnit.MILLISECONDS);
	}

	


	private class NPCRunner implements Runnable {
		@Override
		public void run() {

			try
			{
			for (User npc : allNpcs) {

				float posX = npc.getVariable("x").getDoubleValue().floatValue();
				float posY = 1.0f;
				float posZ = npc.getVariable("z").getDoubleValue().floatValue();
				Vec3D pos = new Vec3D(posX, posY, posZ);
				
				Monster monster = (Monster) npc.getProperty("npcData");
				
				monster.CheckDie(npc.getVariable("hp").getIntValue(), mmoAPi, getApi(), npc, posX, posZ, npc.getLastJoinedRoom());
				
				
				monster.Tick(getApi(), mmoAPi, npc, ((MMORoom) getParentExtension().getParentRoom())
						.getProximityList(new Vec3D(posX, posY, posZ)), (MMORoom) getParentExtension().getParentRoom());
			
			}
			}
			catch (Exception e) {
				trace(e);
			}
		}

	}
}
