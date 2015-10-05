package main.saveAndLoad;

import gameWorld.Controller;
import gameWorld.Room;
import gameWorld.characters.DavePlayer;
import gameWorld.characters.Player;
import gameWorld.characters.PondyPlayer;

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
	public static void loadGame(Controller controller, File saveFile){
		try {
			File file = saveFile;
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(file);
			
			doc.getDocumentElement().normalize();
			
			//Read rooms
			NodeList list = doc.getElementsByTagName("Room");
			
			ArrayList<Room> rooms = new ArrayList<>();
			ArrayList<Player> players = new ArrayList<>();
			
			for (int i = 0; i < list.getLength(); i++){
				Node node = (Node) list.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE){
					Element e = (Element) node;
					
					//read players in the room
					NodeList playerlist = e.getChildNodes();
					
					//get the room object from player
					Room r = controller.getRoom((e.getAttribute("id")));
					
					for (int j = 0; j < playerlist.getLength(); j++){
						Node pnode = (Node) playerlist.item(j);
						if(pnode.getNodeType() == Node.ELEMENT_NODE){
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
							
							//add player object to room object
							r.addPlayer(p);
							
							//add player object to list of players
							players.add(p);
						}
					}
					//add room to list of rooms
					rooms.add(r);
				}
			}
			
			
			//add list of players to the controller
			controller.setPlayers(players);
			controller.setCurrentPlayer(players.get(0));

		} catch (ParserConfigurationException | SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
