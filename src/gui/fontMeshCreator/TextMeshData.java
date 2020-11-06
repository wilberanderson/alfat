package gui.fontMeshCreator;

/**
 * Stores the vertex data for all the quads on which a text will be rendered.
 * @author Karl
 *
 */
public class TextMeshData {
	
	private float[] vertexPositions;
	private float[] textureCoords;
	private float[] characterEdges;
	
	protected TextMeshData(float[] vertexPositions, float[] textureCoords, float[] characterEdges){
		this.vertexPositions = vertexPositions;
		this.textureCoords = textureCoords;
		this.characterEdges = characterEdges;
	}

	public float[] getVertexPositions() {
		return vertexPositions;
	}

	public float[] getTextureCoords() {
		return textureCoords;
	}

	public int getVertexCount() {
		return vertexPositions.length/2;
	}

	public float[] getCharacterEdges(){
		return characterEdges;
	}
}
