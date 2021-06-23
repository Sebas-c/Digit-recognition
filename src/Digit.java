package coursework2;

public class Digit {
	
	// Variables
	
	private Double[] bitmap;
	private Integer realAttribute;
	private Integer guessedAttribute;
	private Double confidence; //Used for knn
	
	
	// Constructor
	
	public Digit(Double[] bitmap, Integer attribute) {
		this.bitmap = bitmap;
		this.realAttribute = attribute;
		this.guessedAttribute = -1;
		this.confidence = -1.0;
	}
	
	// Getters and Setters
	
	public void setBitmap(Double[] bitmap) {
		this.bitmap = bitmap;
	}

	public Double[] getBitmap() {
		return bitmap;
	}

	public void setRealAttribute(Integer attribute) {
		this.realAttribute = attribute;
	}
	
	public Integer getRealAttribute() {
		return realAttribute;
	}
	
	public void setGuessedAttribute(Integer guessedAttribute) {
		this.guessedAttribute = guessedAttribute;
	}
	
	public Integer getGuessedAttribute() {
		return guessedAttribute;
	}

	public void setConfidence(Double confidence) {
		this.confidence = confidence;
	}
	
	public Double getConfidence() {
		return confidence;
	}

}