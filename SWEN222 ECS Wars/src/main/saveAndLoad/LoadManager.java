package main.saveAndLoad;

import gameWorld.Controller;
import gameWorld.Room;
import gameWorld.characters.*;
import gameWorld.gameObjects.*;
import gameWorld.gameObjects.Item.Type;
import gameWorld.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
 * @author Jah Seng Lee
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
				
				//TODO: load items into rooms
				loadItems(controller, r, e);
				
				//add room to list of rooms
				rooms.add(r);
			}
		}
		
		
		//add list of players to the controller
		controller.setPlayers(players);
	}

	private static void loadItems(Controller controller, Room r, Element e) {
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
				case SmallTreasure:
					
					//load item into room at correct position
					roomContents[Integer.parseInt(ielement.getAttribute("x"))]
							[Integer.parseInt(ielement.getAttribute("y"))]
							= new SmallTreasure();
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
				//TODO: containers
				default:	//weapon
					loadWeapons(controller, r);
					break;
				}
			}
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
				switch(pelement.getAttribute("type")){
				case("PondyPlayer"):	//PondyPlayer
					p = new PondyPlayer(r, Integer.parseInt(pelement.getAttribute("x")), 
							Integer.parseInt(pelement.getAttribute("y")));
					break;
				default:	//DavePlayer
					p = new DavePlayer(r, Integer.parseInt(pelement.getAttribute("x")), 
							Integer.parseInt(pelement.getAttribute("y")));
					break;
				}
				
				//TODO: load inventory into player
				loadInventory(controller, p, pelement);
				
				//TODO: load weapon into player
				loadWeapon(controller, p, pelement);
				
				//add player object to room object
				r.addPlayer(p);
				
				//add player object to list of players
				players.add(p);
			}
		}
	}

	private static void loadWeapon(Controller controller, Player p,
			Element pelement) {
		// TODO Auto-generated method stub
		
	}

	private static void loadInventory(Controller controller, Player p,
			Element pelement) {
		// TODO Auto-generated method stub
		
	}
	
}
