package util;

import entities.Entity;
import entities.EntityType;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christopher on 22/05/2016.
 */
public class XMLReader {

	/**
	 * Load a solar system
	 *
	 * @param path Location of the xml file with the solar system data
	 * @return Array of entities loaded from the file
	 */
	public static List<Entity> loadSystem(String path) {
		// Create an XML Document object
		Document xmlDoc = getDocument(path);

		// Get all of the entities from the XML Document
		assert xmlDoc != null;
		NodeList entityNodes = xmlDoc.getElementsByTagName("entity");

		// Create an array of Strings for each entity parameter
		List<String> names = getElement(entityNodes, "name");
		List<String> types = getElement(entityNodes, "type");
		List<String> models = getElement(entityNodes, "model");
		List<String> xPositions = getElement(entityNodes, "xPosition");
		List<String> yPositions = getElement(entityNodes, "yPosition");
		List<String> xVelocities = getElement(entityNodes, "xVelocity");
		List<String> yVelocities = getElement(entityNodes, "yVelocity");
		List<String> masses = getElement(entityNodes, "mass");
		List<String> rotationPeriods = getElement(entityNodes, "rotation-period");

		// Create an array of entity objects
		List<Entity> entities = new ArrayList<>();

		// Create entities from the arrays of strings
		for (int i = 0; i < names.size(); i++) {
			EntityType type;
			switch (types.get(i)) {
				case "star":
					type = EntityType.Star;
					break;
				case "planet":
					type = EntityType.Planet;
					break;
				default:
					type = EntityType.Planet;
					break;
			}

			Entity modelEntity;
			switch (models.get(i)) {
				case "sun":
					modelEntity = Entity.sun;
					break;
				case "mercury":
					modelEntity = Entity.mercury;
					break;
				case "venus":
					modelEntity = Entity.venus;
					break;
				case "earth":
					modelEntity = Entity.earth;
					break;
				case "mars":
					modelEntity = Entity.mars;
					break;
				case "jupiter":
					modelEntity = Entity.jupiter;
					break;
				case "saturn":
					modelEntity = Entity.saturn;
					break;
				case "uranus":
					modelEntity = Entity.uranus;
					break;
				case "neptune":
					modelEntity = Entity.neptune;
					break;
				case "pluto":
					modelEntity = Entity.pluto;
					break;
				case "brownDwarf":
					modelEntity = Entity.brownDwarf;
					break;
				default:
					modelEntity = Entity.sun;
			}
			Entity newEntity = new Entity(type, modelEntity, new Vector3f(Float.parseFloat(xPositions.get(i)), 0, Float.parseFloat(yPositions.get(i))), new Vector3f(0.1f, 0.1f, 0.1f), new Vector3f(0, 0, 0));
			newEntity.setName(names.get(i));
			newEntity.setVelocity(new Vector2f(Float.parseFloat(xVelocities.get(i)), Float.parseFloat(yVelocities.get(i))));
			newEntity.setMass(Float.parseFloat(masses.get(i)));
			newEntity.setRotationPeriod(Integer.parseInt(rotationPeriods.get(i)));

			entities.add(newEntity);
		}

		return entities;
	}

	/**
	 * Load an xml document
	 *
	 * @param path Location of the document
	 * @return the xml document
	 */
	private static Document getDocument(String path) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

			factory.setIgnoringComments(true);
			factory.setIgnoringElementContentWhitespace(true);
			factory.setValidating(true);

			DocumentBuilder builder = factory.newDocumentBuilder();
			builder.setErrorHandler(null);

			return builder.parse(Class.class.getResourceAsStream(path));

		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Get the instances of an element
	 *
	 * @param entities    Entities in which to fint the element
	 * @param elementName The name of the element we need to get
	 * @return String array of the found elements
	 */
	public static List<String> getElement(NodeList entities, String elementName) {
		List<String> elements = new ArrayList<>();
		for (int i = 0; i < entities.getLength(); i++) {
			Node entityNode = entities.item(i);
			Element entityElement = (Element) entityNode;
			NodeList nameList = entityElement.getElementsByTagName(elementName);
			Element nameElement = (Element) nameList.item(0);
			NodeList elementList = nameElement.getChildNodes();
			elements.add(elementList.item(0).getNodeValue().trim());
		}
		return elements;
	}
}
