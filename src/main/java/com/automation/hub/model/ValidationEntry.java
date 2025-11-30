package com.automation.hub.model;

public class ValidationEntry {
	    private String carrier;    // parsed from text (or note)
	    private String country;    // parsed from text (or note)
	    private String roCarrier;  // from column C
	    private String eLabel;     // from column K
	    private String dpm;        // from column T
	    
	    
	    public ValidationEntry() {
	    }

	    public ValidationEntry(String carrier, String country, String roCarrier, String eLabel, String dpm) {
	        this.carrier = carrier;
	        this.country = country;
	        this.roCarrier = roCarrier;
	        this.eLabel = eLabel;
	        this.dpm = dpm;
	    }

	    public String getCarrier() {
	        return carrier;
	    }

	    public void setCarrier(String carrier) {
	        this.carrier = carrier;
	    }

	    public String getCountry() {
	        return country;
	    }

	    public void setCountry(String country) {
	        this.country = country;
	    }

	    public String getRoCarrier() {
	        return roCarrier;
	    }

	    public void setRoCarrier(String roCarrier) {
	        this.roCarrier = roCarrier;
	    }

	    public String getELabel() {
	        return eLabel;
	    }

	    public void setELabel(String eLabel) {
	        this.eLabel = eLabel;
	    }

	    public String getDpm() {
	        return dpm;
	    }

	    public void setDpm(String dpm) {
	        this.dpm = dpm;
	    }

	    @Override
	    public String toString() {
	        return "ValidationEntry{" +
	                "carrier='" + carrier + '\'' +
	                ", country='" + country + '\'' +
	                ", roCarrier='" + roCarrier + '\'' +
	                ", eLabel='" + eLabel + '\'' +
	                ", dpm='" + dpm + '\'' +
	                '}';
	    }
}
