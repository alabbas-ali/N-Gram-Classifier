package pt.tumba.parser;

/**
 * Container for meta-data properties 
 *
 *  @author Bruno Martins
 *
 */
public class MetaData {

	private String latitude;
	private String longitude;
	private String contributor;
	private String copyright;
	private String coverage;
	private String creator;
	private String date;
	private String dateAccepted;
	private String dateAcquired;
	private String dateAvailable;
	private String dateCreated;
	private String linkRSS;
	private String dateGathered;
	private String dateIssued;
	private String dateValid;
	private String description;
	private String distribution;
	private String expires;
	private String format;
	private String generator;
	private String keywords;
	private String relation;
	private String relationIsBasedOn;
	private String relationIsFormatOf;
	private String relationIsPartOf;
	private String relationIsVersionOf;
	private String relationReferences;
	private String relationRequires;
	private String revisit;
	private String robots;
	private String source;
	private String tabstract;
	private String author;
	private String publisher;
	private String identifier;
	private StringBuffer title;
	private String type;
			
	protected void appendTitle(String v) { title.append(v); }
	protected void appendTitle(char v) { title.append("" + v); }

	protected void setLatitude(String v) { latitude = v; }
		
	protected void setLongitude(String v) { longitude = v; } 
	
	protected void setContributor(String v) { contributor = v; }
	
	protected void setCopyright(String v) { copyright  = v; }
	
	protected void setCoverage(String v) {  coverage  = v; }
	
	protected void setCreator(String v) { creator = v; }
	
	protected void setDate(String v) { date = v; }
	
	protected void setDateAccepted(String v) { dateAccepted = v; }
	
	protected void setDateAcquired(String v) { dateAcquired = v; }
	
	protected void setDateAvailable(String v) { dateAvailable = v; }
	
	protected void setDateCreated(String v) { dateCreated = v; }
	
	protected void setDateGathered(String v) { dateGathered = v; }
	
	protected void setDateIssued(String v) { dateIssued = v; }
	
	protected void setDateValid(String v) { dateValid = v; }
	
	protected void setDescription(String v) { description = v; }
	
	protected void setDistribution(String v) { distribution = v; }
	
	protected void setExpires(String v) { expires = v; }
	
	protected void setFormat(String v) { format = v; }
	
	protected void setGenerator(String v) { generator = v; }
	
	protected void setKeywords(String v) { keywords = v; }
	
	protected void setRelation(String v) { relation = v; }
	
	protected void setRelationIsBasedOn(String v) { relationIsBasedOn = v; }
	
	protected void setRelationIsFormatOf(String v) { relationIsFormatOf = v; }
	
	protected void setRelationIsPartOf(String v) { relationIsPartOf = v; }
	
	protected void setRelationIsVersionOf(String v) { relationIsVersionOf = v; } 
	
	protected void setRelationReferences(String v) { relationReferences = v; }
	
	protected void setRelationRequires(String v) { relationRequires = v; }
	
	protected void setRevisit(String v) {  revisit = v; }
	
	protected void setRobots(String v) { robots = v; }
	
	protected void setSource(String v) { source = v; }
	
	protected void setAbstract(String v) { tabstract = v; }
	
	protected void setAuthor(String v) { author  = v; }
	
	protected void setPublisher(String v) { publisher  = v; }

	protected void setIdentifier(String v) { identifier  = v; }

	protected void setMIMEType(String v) { 
		if(v==null) type=""; else type  = v;
		int index = type.indexOf(";;"); 
		if(index!=-1) type = type.substring(0,index) + type.substring(index+1); 
	 }
	
	protected void setLinkRSS(String v) { linkRSS = v; }
	
	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	public String getContributor() {
		return contributor;
	}

	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	public String getCopyright() {
		return copyright;
	}
	
	public String getLatitude() {
		return latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	public String getCoverage() {
		return coverage;
	}

	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	public String getCreator() {
		return creator;
	}

	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	public String getDate() {
		return date;
	}

	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	public String getDateAccepted() {
		return dateAccepted;
	}

	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	public String getDateAcquired() {
		return dateAcquired;
	}

	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	public String getDateAvailable() {
		return dateAvailable;
	}

	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	public String getDateCreated() {
		return dateCreated;
	}

	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	public String getDateGathered() {
		return dateGathered;
	}

	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	public String getDateIssued() {
		return dateIssued;
	}

	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	public String getDateValid() {
		return dateValid;
	}

	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	public String getDescription() {
		return description;
	}

	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	public String getDistribution() {
		return distribution;
	}

	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	public String getExpires() {
		return expires;
	}

	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	public String getFormat() {
		return format;
	}

	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	public String getGenerator() {
		return generator;
	}

	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	public String getPublisher() {
		return publisher;
	}

	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	public String getRelation() {
		return relation;
	}

	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	public String getRelationIsBasedOn() {
		return relationIsBasedOn;
	}

	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	public String getRelationIsFormatOf() {
		return relationIsFormatOf;
	}

	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	public String getRelationIsPartOf() {
		return relationIsPartOf;
	}

	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	public String getRelationIsVersionOf() {
		return relationIsVersionOf;
	}

	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	public String getRelationReferences() {
		return relationReferences;
	}

	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	public String getRelationRequires() {
		return relationRequires;
	}

	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	public String getRevisit() {
		return revisit;
	}

	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	public String getRobots() {
		return robots;
	}


	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	public String getSource() {
		return source;
	}

	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	public String getAbstract() {
		return tabstract;
	}

	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	public String getKeywords() {
		return keywords;
	}

	/**
	 * Returns the title of the document
	 *
	 *@return   The title of the document
	 */
	public String getTitle() {
		if (title == null) return null;
		return title.toString();
	}

	/**
	 * Returns the MIME type associated with the document
	 *
	 *@return    The MIME type associated with the document
	 */
	public String getMIMEType() {
		return type;
	}
	
	public String getLinkRSS() {
		return linkRSS;
	}

    protected MetaData () {
		description = new String();
		keywords = new String();
		generator = new String();
		author = new String();
		tabstract = new String();
		copyright = new String();
		latitude = new String();
		longitude = new String();
		distribution = new String();
		expires = new String();
		revisit = new String();
		robots = new String();
		source = new String();
		creator = new String();
		date = new String();
		format = new String();
		publisher = new String();
		contributor = new String();
		identifier = new String();
		coverage = new String();
		dateCreated = new String();
		dateAvailable = new String();
		dateValid = new String();
		dateAcquired = new String();
		dateAccepted = new String();
		dateGathered = new String();
		dateIssued = new String();
		relation = new String();
		relationIsPartOf = new String();
		relationIsFormatOf = new String();
		relationIsVersionOf = new String();
		relationReferences = new String();
		relationIsBasedOn = new String();
		relationRequires = new String();
        title = new StringBuffer();
        type = new String();
		linkRSS = new String();
    }
}
