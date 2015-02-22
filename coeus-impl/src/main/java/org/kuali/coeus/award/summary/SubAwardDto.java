package org.kuali.coeus.award.summary;

public class SubAwardDto {

	private String subAwardCode;
	private String sequenceNumber;
	private OrganizationDto organization;
	
	public String getSubAwardCode() {
		return subAwardCode;
	}
	public void setSubAwardCode(String subAwardCode) {
		this.subAwardCode = subAwardCode;
	}
	public String getSequenceNumber() {
		return sequenceNumber;
	}
	public void setSequenceNumber(String sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	public OrganizationDto getOrganization() {
		return organization;
	}
	public void setOrganization(OrganizationDto organization) {
		this.organization = organization;
	}
}
