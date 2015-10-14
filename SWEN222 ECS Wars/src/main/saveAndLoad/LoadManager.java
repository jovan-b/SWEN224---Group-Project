package main.saveAndLoad;

import gameWorld.Controller;
import gameWorld.Room;
import gameWorld.characters.*;
import gameWorld.characters.Player.PlayerType;
import gameWorld.gameObjects.*;
import gameWorld.gameObjects.Item.Type;
import gameWorld.gameObjects.containers.*;
import gameWorld.gameObjects.weapons.*;
import gameWorld.gameObjects.weapons.Weapon.WeaponType;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Parses an XML file to reload a previously saved game
 * Loads game with a top down approach,
 * 	loads all rooms
 * 	for each room, load all characters
 * 	for each character, load all items,
 * 	etc.
 * 
 * @author Jah Seng Lee 300279468
 * @author Carl Anderson 300264124
 *
 */
public class LoadManager {

	/**
	 * Load game based on specified saveFile
	 * Get's room based on ID and adds items/players to them
	 * 
	 * Throws I/O exception and Parser exception if error occur
	 * 
	 * @param controller
	 * @param saveFile
	 */
	public static void loadGame(File saveFile,Controller controller){
		try {
			File file = saveFile;
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(file);
			
			doc.getDocumentElement().normalize();
			
			loadRoom(controller, doc);

		} catch (ParserConfigurationException | SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void loadRoom(Controller controller, Document doc) {
		//Read rooms
		NodeList list = doc.getElementsByTagName("Room");
		
		ArrayList<Room> rooms = new ArrayList<>();
		ArrayList<Player> players = new ArrayList<>();
		
		for (int i = 0; i < list.getLength(); i++){
			Node node = (Node) list.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE){
				Element e = (Element) node;
				
				//get the room object from player
				Room r = controller.getRoom((e.getAttribute("id")));
				
				//load players into room
				loadPlayers(controller, r, e, players);
				
				//load items into rooms
				loadRoomItems(controller, r, e);
				
				//load cabinet contents into rooms
				loadCabinetContents(controller, r, e);
				
				//add room to list of rooms
				rooms.add(r);
			}
		}
		
		
		//add list of players to the controller
		controller.setPlayers(players);
		controller.setCurrentPlayer(players.get(0));

	}

	private static void loadCabinetContents(Controller controller, Room r,
			Element e) {
		NodeList itemlist = e.getChildNodes();
		
		for(int i = 0; i < itemlist.getLength(); i++){
			Element itemElem = (Element) itemlist.item(i);
			
			//if not a cabinet, start again
			if(!itemElem.getTagName().equals("Cabinet")) continue;
			//else, load cabinet contents
			
			//firstly, get the correct cabinet
			Cabinet c = (Cabinet)r.getContents()[Integer.parseInt(itemElem.getAttribute("x"))]
					[Integer.parseInt(itemElem.getAttribute("y"))];
			
			//then, load all items into cabinet
			loadContainerItems(c, itemElem);
		}
	}

	private static void loadRoomItems(Controller controller, Room r, Element e) {
		NodeList itemlist = e.getChildNodes();
		
		for(int i = 0; i < itemlist.getLength(); i++){
			Node inode = (Node) itemlist.item(i);
			if(inode.getNodeType() == Node.ELEMENT_NODE
					&& inode.getNodeName() == "Item"){
				Element ielement = (Element) inode;	//item element
				Item[][] roomContents = r.getContents();	//load room contents to modify
				
				switch(Type.valueOf(ielement.getAttribute("type"))){
				case KeyCard:
					
					//load item into room at correct position
					roomContents[Integer.parseInt(ielement.getAttribute("x"))]
							[Integer.parseInt(ielement.getAttribute("y"))]
							= new KeyCard();
					break;
				case Diamond:
					roomContents[Integer.parseInt(ielement.getAttribute("x"))]
							[Integer.parseInt(ielement.getAttribute("y"))] = 
					new SmallTreasure(ielement.getAttribute("type"),
							Integer.parseInt(ielement.getAttribute("points")), 
							ielement.getAttribute("quality"));
					break;
				case Ruby: 
					roomContents[Integer.parseInt(ielement.getAttribute("x"))]
							[Integer.parseInt(ielement.getAttribute("y"))] = 
					new SmallTreasure(ielement.getAttribute("type"),
							Integer.parseInt(ielement.getAttribute("points")), 
							ielement.getAttribute("quality"));
					break;
				case Emerald:
					roomContents[Integer.parseInt(ielement.getAttribute("x"))]
							[Integer.parseInt(ielement.getAttribute("y"))] = 
					new SmallTreasure(ielement.getAttribute("type"),
							Integer.parseInt(ielement.getAttribute("points")), 
							(ielement.getAttribute("quality")));
					break;
				case Sapphire:
					roomContents[Integer.parseInt(ielement.getAttribute("x"))]
							[Integer.parseInt(ielement.getAttribute("y"))] = 
					new SmallTreasure(ielement.getAttribute("type"),
							Integer.parseInt(ielement.getAttribute("points")), 
							(ielement.getAttribute("quality")));
					break;
				case Torch:
					
					//load item into room at correct position
					roomContents[Integer.parseInt(ielement.getAttribute("x"))]
							[Integer.parseInt(ielement.getAttribute("y"))]
							= new Torch();
					break;
				case MedicineBottle:
					
					//load item into room at correct position
					roomContents[Integer.parseInt(ielement.getAttribute("x"))]
							[Integer.parseInt(ielement.getAttribute("y"))]
							= new MedicineBottle();
					break;
				case PillBottle: 
					
					//load item into room at correct position
					roomContents[Integer.parseInt(ielement.getAttribute("x"))]
							[Integer.parseInt(ielement.getAttribute("y"))]
							= new PillBottle();
					break;
				case Map:
					
					//load item into room at correct position
					roomContents[Integer.parseInt(ielement.getAttribute("x"))]
							[Integer.parseInt(ielement.getAttribute("y"))]
							= new Map();
					break;
				//container items
				case Pouch:
					Pouch p = new Pouch();
					roomContents[Integer.parseInt(ielement.getAttribute("x"))]
							[Integer.parseInt(ielement.getAttribute("y"))]
							= p;
					loadContainerItems(p, ielement);
					break;
				default:	//weapon
					loadRoomWeapons(controller, r, roomContents, ielement);
					break;
				}
			}
		}
	}

	@SuppressWarnings("incomplete-switch")
	private static void loadContainerItems(Container c, Element e) {
		NodeList items = e.getChildNodes();
		
		for(int i = 0; i < items.getLength(); i++){
			Element itemElem = (Element)items.item(i);
			
			
			switch(Type.valueOf(itemElem.getAttribute("type"))){
			//add correct item to container
			case KeyCard:
				c.addItem(new KeyCard());
				break;
			case Diamond:
				c.addItem(new SmallTreasure(itemElem.getAttribute("type"),
						Integer.parseInt(itemElem.getAttribute("points")), 
						itemElem.getAttribute("quality")));
				break;
			case Ruby: 
				c.addItem(new SmallTreasure(itemElem.getAttribute("type"),
						Integer.parseInt(itemElem.getAttribute("points")), 
						itemElem.getAttribute("quality")));
				break;
			case Emerald:
				c.addItem(new SmallTreasure(itemElem.getAttribute("type"),
						Integer.parseInt(itemElem.getAttribute("points")), 
						itemElem.getAttribute("quality")));
				break;
			case Sapphire:
				c.addItem(new SmallTreasure(itemElem.getAttribute("type"),
						Integer.parseInt(itemElem.getAttribute("points")), 
						itemElem.getAttribute("quality")));
				break;
			case Torch:
				c.addItem(new Torch());
				break;
			case MedicineBottle:
				c.addItem(new MedicineBottle());
				break;
			case PillBottle: 
				c.addItem(new PillBottle());
				break;
			case Map:
				c.addItem(new Map());
				break;
			//container items
			case Pouch:
				Pouch p = new Pouch();
				//load inner pouch items
				loadContainerItems(p, itemElem);
				c.addItem(p);
				break;
			}
		}
		
	}

	private static void loadRoomWeapons(Controller controller, Room r, 
			Item[][] roomContents, Element ielement) {
		switch(WeaponType.valueOf(ielement.getAttribute("weaponType"))){
		case PaintballGun:
			roomContents[Integer.parseInt(ielement.getAttribute("x"))]
					[Integer.parseInt(ielement.getAttribute("y"))]
							= new PaintballGun();
			break;
		case ScatterGun:
			roomContents[Integer.parseInt(ielement.getAttribute("x"))]
					[Integer.parseInt(ielement.getAttribute("y"))]
							= new ScatterGun();
			break;
		case LTSAGun:roomContents[Integer.parseInt(ielement.getAttribute("x"))]
				[Integer.parseInt(ielement.getAttribute("y"))]
						= new LTSAGun();
			break;
		case Pistol:
			roomContents[Integer.parseInt(ielement.getAttribute("x"))]
					[Integer.parseInt(ielement.getAttribute("y"))]
							= new Pistol();
			break;
		}
	}

	private static void loadPlayers(Controller controller, Room r, Element e, 
			ArrayList<Player> players) {
		
		//read players in the room
		NodeList playerlist = e.getChildNodes();
		
		for (int i = 0; i < playerlist.getLength(); i++){
			Node pnode = (Node) playerlist.item(i);
			if(pnode.getNodeType() == Node.ELEMENT_NODE
					&& pnode.getNodeName() == "Player"){	//is a player node
				Element pelement = (Element) pnode;	//player element
				Player p;
				
				//create a player object from the node
				switch(PlayerType.valueOf(pelement.getAttribute("type"))){
				case PondyPlayer:
					p = new PondyPlayer(r, Integer.parseInt(pelement.getAttribute("x")), 
							Integer.parseInt(pelement.getAttribute("y")));
					break;
				case DavePlayer:
					p = new DavePlayer(r, Integer.parseInt(pelement.getAttribute("x")), 
							Integer.parseInt(pelement.getAttribute("y")));
					break;
				case StreaderPlayer:
					p = new StreaderPlayer(r, Integer.parseInt(pelement.getAttribute("x")), 
							Integer.parseInt(pelement.getAttribute("y")));
					break;
				case MarcoPlayer:
					p = new MarcoPlayer(r, Integer.parseInt(pelement.getAttribute("x")), 
							Integer.parseInt(pelement.getAttribute("y")));
					break;
				default:	//MarcoPlayer for now
					
					p = new PondyPlayer(r, Integer.parseInt(pelement.getAttribute("x")), 
							Integer.parseInt(pelement.getAttribute("y")));
					break;
					//TODO: add NPC players 
				}
				
				//load inventory into player
				loadPlayerItems(p.getInventory(), pelement);
				
				loadWeapon(controller, p, pelement);
				
				//add player object to room object
				r.addPlayer(p);
				
				//add player object to list of players
				players.add(p);
			}
		}
	}

	@SuppressWarnings("incomplete-switch")
	private static void loadPlayerItems(Item[] inventory, Element pelement) {
		NodeList items = pelement.getChildNodes();
		
		for(int i = 0; i < items.getLength(); i++){
			
			//check to see if of type Item
			if(items.item(i).getNodeName() != "Item") continue;
			
			Element itemElem = (Element)items.item(i);
			
			switch(Type.valueOf(itemElem.getAttribute("type"))){
			//add correct item to container
			case KeyCard:
				inventory[Integer.parseInt(itemElem.getAttribute("index"))] = new KeyCard();
				break;
			case Diamond:
				inventory[Integer.parseInt(itemElem.getAttribute("index"))] = 
				new SmallTreasure(itemElem.getAttribute("type"),
						Integer.parseInt(itemElem.getAttribute("points")), 
						itemElem.getAttribute("quality"));
				break;
			case Ruby: 
				inventory[Integer.parseInt(itemElem.getAttribute("index"))] = 
				new SmallTreasure(itemElem.getAttribute("type"),
						Integer.parseInt(itemElem.getAttribute("points")), 
						itemElem.getAttribute("quality"));
				break;
			case Emerald:
				inventory[Integer.parseInt(itemElem.getAttribute("index"))] = 
				new SmallTreasure(itemElem.getAttribute("type"),
						Integer.parseInt(itemElem.getAttribute("points")), 
						itemElem.getAttribute("quality"));
				break;
			case Torch:
				inventory[Integer.parseInt(itemElem.getAttribute("index"))] = new Torch();
				break;
			case MedicineBottle:
				inventory[Integer.parseInt(itemElem.getAttribute("index"))] = new MedicineBottle();
				break;
			case PillBottle: 
				inventory[Integer.parseInt(itemElem.getAttribute("index"))] = new PillBottle();
				break;
			case Map:
				inventory[Integer.parseInt(itemElem.getAttribute("index"))] = new Map();
				break;
			//container items
			case Pouch:
				Pouch p = new Pouch();
				//load inner pouch items
				loadContainerItems(p, itemElem);
				inventory[Integer.parseInt(itemElem.getAttribute("index"))] = p;
				break;
			}
		}
	}

	private static void loadWeapon(Controller controller, Player p,
			Element pelement) {
		NodeList weaponList = pelement.getChildNodes();
		
		for(int i = 0; i < weaponList.getLength(); i++){
			//check if a weapon, if not, keep iterating
			if(weaponList.item(i).getNodeName() != "Weapon") continue;
			
			Element weaponElem = (Element) weaponList.item(i);
			
			//if it is a weapon type, find out which one
			switch(WeaponType.valueOf(weaponElem.getAttribute("weaponType"))){
			case PaintballGun: 
				p.setCurrentWeapon(new PaintballGun());
				break;
			case LTSAGun:
				p.setCurrentWeapon(new LTSAGun());
				break;
			case ScatterGun:
				p.setCurrentWeapon(new ScatterGun());
				break;
			case Pistol:
				p.setCurrentWeapon(new Pistol());
				break;
			}
		}
	}
	
}
