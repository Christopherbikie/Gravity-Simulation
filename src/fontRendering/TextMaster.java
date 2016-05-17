package fontRendering;

import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontMeshCreator.TextMeshData;
import renderEngine.Loader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Christopher on 16/05/2016.
 */
public class TextMaster {

	private static Loader loader;
	/**
	 * Hashmap storing fonts and text
	 * Key: Fonts being used
	 * Value: List of texts using the font
	 */
	private static Map<FontType, List<GUIText>> texts = new HashMap<>();
	private static FontRenderer renderer;

	/**
	 * Initialise the TextMaster
	 *
	 * @param theLoader loader to use
	 */
	public static void init(Loader theLoader) {
		renderer = new FontRenderer();
		loader = theLoader;
	}

	/**
	 * Render text
	 */
	public static void render() {
		renderer.render(texts);
	}

	/**
	 * Load text into the texts HashMap
	 *
	 * @param text Text to loaded
	 */
	public static void loadText(GUIText text) {
		FontType font = text.getFont();
		TextMeshData data = font.loadText(text);
		int vao = loader.loadToVAO(data.getVertexPositions(), data.getTextureCoords());
		text.setMeshInfo(vao, data.getVertexCount());
		List<GUIText> textBatch = texts.get(font);
		if (textBatch == null) {
			textBatch = new ArrayList<>();
			texts.put(font, textBatch);
		}
		textBatch.add(text);
	}

	/**
	 * Remove text from the texts HashMap
	 *
	 * @param text Text to be removed
	 */
	public static void removeText(GUIText text){
		List<GUIText> textBatch = texts.get(text.getFont());
		textBatch.remove(text);
		if(textBatch.isEmpty())
			texts.remove(texts.get(text.getFont()));
	}

	/**
	 * Clean up the renderer
	 */
	public static void cleanUp() {
		renderer.cleanUp();
	}
}
