package com.automation.hub.web;

public class SheetRequest {
	 private String validationUrl;
	    private String workbookUrl;

	    // Required empty constructor for Spring to deserialize JSON
	    public SheetRequest() {}

	    public SheetRequest(String validationUrl, String workbookUrl) {
	        this.validationUrl = validationUrl;
	        this.workbookUrl = workbookUrl;
	    }

	    public String getValidationUrl() {
	        return validationUrl;
	    }

	    public void setValidationUrl(String validationUrl) {
	        this.validationUrl = validationUrl;
	    }

	    public String getWorkbookUrl() {
	        return workbookUrl;
	    }

	    public void setWorkbookUrl(String workbookUrl) {
	        this.workbookUrl = workbookUrl;
	    }

	    @Override
	    public String toString() {
	        return "SheetRequest{" +
	                "validationUrl='" + validationUrl + '\'' +
	                ", workbookUrl='" + workbookUrl + '\'' +
	                '}';
	    }
}
