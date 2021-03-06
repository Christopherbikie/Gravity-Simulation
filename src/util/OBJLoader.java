package util;

import models.RawModel;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.Loader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Christopher on 24/04/2016.
 */
public class OBJLoader {

	/**
	 * Load an OBJ model file.
	 *
	 * @param fileName The path and file name of the object to load.
	 *                 The file must be in the res directory or one of it's subdirectories.
	 *                 Format: "path/filename" (Do not add "res/" or ".obj".
	 * @param loader A loader to load the file with.
	 * @return The RawModel loaded from the file.
	 */
	public static RawModel loadObjModel(String fileName, Loader loader) {
		InputStreamReader isr = new InputStreamReader(Class.class.getResourceAsStream(fileName + ".obj"));
		BufferedReader reader = new BufferedReader(isr);
		String line;
		List<Vector3f> vertices = new ArrayList<>();
		List<Vector2f> textures = new ArrayList<>();
		List<Vector3f> normals = new ArrayList<>();
		List<Integer> indices = new ArrayList<>();
		float[] verticesArray;
		float[] texturesArray = null;
		float[] normalsArray = null;
		int[] indicesArray;
		try {
			while (true) {
				line = reader.readLine();
				String[] currentLine = line.split(" ");
				if (line.startsWith("v ")) {
					Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]),
							Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
					vertices.add(vertex);
				} else if (line.startsWith("vt ")) {
					Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]),
							Float.parseFloat(currentLine[2]));
					textures.add(texture);
				} else if (line.startsWith("vn ")) {
					Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]),
							Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
					normals.add(normal);
				} else if (line.startsWith("f ")) {
					texturesArray = new float[vertices.size() * 2];
					normalsArray = new float[vertices.size() * 3];
					break;
				}
			}

			while (line != null) {
				if (!line.startsWith("f ")) {
					line = reader.readLine();
					continue;
				}
				String[] currentLine = line.split(" ");
				String[] vertex1 = currentLine[1].split("/");
				if (Objects.equals(vertex1[1], "")) vertex1[1] = "1";
				String[] vertex2 = currentLine[2].split("/");
				if (Objects.equals(vertex2[1], "")) vertex2[1] = "2";
				String[] vertex3 = currentLine[3].split("/");
				if (Objects.equals(vertex3[1], "")) vertex3[1] = "3";

				processVertex(vertex1, indices, textures, normals, texturesArray, normalsArray);
				processVertex(vertex2, indices, textures, normals, texturesArray, normalsArray);
				processVertex(vertex3, indices, textures, normals, texturesArray, normalsArray);
				line = reader.readLine();
			}
			reader.close();
		} catch(Exception e) {
			e.printStackTrace();
		}

		verticesArray = new float[vertices.size() * 3];
		indicesArray = new int[indices.size()];

		int vertexPointer = 0;
		for(Vector3f vertex : vertices) {
			verticesArray[vertexPointer++] = vertex.x;
			verticesArray[vertexPointer++] = vertex.y;
			verticesArray[vertexPointer++] = vertex.z;
		}
		for (int i = 0; i < indices.size(); i++)
			indicesArray[i] = indices.get(i);

		return loader.loadToVAO(verticesArray, texturesArray, normalsArray, indicesArray);
	}

	private static void processVertex(String[] vertexData, List<Integer> indices, List<Vector2f> textures,
	                                  List<Vector3f> normals, float[] textureArray, float[] normalsArray) {
		int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
		indices.add(currentVertexPointer);
		Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1]) - 1);
		textureArray[currentVertexPointer * 2] = currentTex.x;
		textureArray[currentVertexPointer * 2 + 1] = 1 - currentTex.y;
		Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
		normalsArray[currentVertexPointer * 3] = currentNorm.x;
		normalsArray[currentVertexPointer * 3 + 1] = currentNorm.y;
		normalsArray[currentVertexPointer * 3 + 2] = currentNorm.z;
	}
}
