package fontRendering;

import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;

import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class FontRenderer {

	/**
	 * Shader to render fonts with
	 */
	private FontShader shader;

	/**
	 * Constructor to create a new FontRenderer
	 */
	public FontRenderer() {
		shader = new FontShader();
	}

	/**
	 * Render text
	 *
	 * @param texts Map of fonts, and text to be rendered in each font.
	 */
	public void render(Map<FontType, List<GUIText>> texts) {
		prepare();
		for (FontType font : texts.keySet()) {
			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, font.getTextureAtlas());
			texts.get(font).forEach(this::renderText);
		}
		endRendering();
	}

	/**
	 * Clean up the shader
	 */
	public void cleanUp() {
		shader.cleanUp();
	}

	/**
	 * Prepare for rendering
	 */
	private void prepare() {
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glDisable(GL_DEPTH_TEST);
		shader.start();
	}

	/**
	 * Render text.
	 *
	 * @param text Text to be rendered
	 */
	private void renderText(GUIText text) {
		glBindVertexArray(text.getMesh());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		shader.loadColour(text.getColour());
		shader.loadTranslation(text.getPosition());
		glDrawArrays(GL_TRIANGLES, 0, text.getVertexCount());
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
	}

	/**
	 * Stop rendering
	 */
	private void endRendering() {
		shader.stop();
		glDisable(GL_BLEND);
		glEnable(GL_DEPTH_TEST);
	}

}
