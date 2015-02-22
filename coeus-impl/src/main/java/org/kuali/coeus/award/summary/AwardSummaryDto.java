package org.kuali.coeus.award.summary;

import java.util.List;

import com.codiform.moo.annotation.CollectionProperty;
import com.codiform.moo.annotation.Property;

public class AwardSummaryDto {

	private String awardNumber;
	private Integer sequenceNumber;
	private String accountNumber;
	private String modificationNumber;
	private String sponsorAwardNumber;
	private String cfdaNumber;
	private String title;
	@Property(translate = true)
	private AwardStatusDto awardStatus;
	@Property(translate = true)
	private ActivityTypeDto activityType;
	@Property(translate = true)
	private SponsorDto sponsor;
	@CollectionProperty
	private List<SubAwardDto> subAwardList;
	@Property(translate = true)
	private InvestigatorDto principalInvestigator;
	
	public String getAwardNumber() {
		return awardNumber;
	}
	public void setAwardNumber(String awardNumber) {
		this.awardNumber = awardNumber;
	}
	public Integer getSequenceNumber() {
		return sequenceNumber;
	}
	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getModificationNumber() {
		return modificationNumber;
	}
	public void setModificationNumber(String modificationNumber) {
		this.modificationNumber = modificationNumber;
	}
	public String getSponsorAwardNumber() {
		return sponsorAwardNumber;
	}
	public void setSponsorAwardNumber(String sponsorAwardNumber) {
		this.sponsorAwardNumber = sponsorAwardNumber;
	}
	public String getCfdaNumber() {
		return cfdaNumber;
	}
	public void setCfdaNumber(String cfdaNumber) {
		this.cfdaNumber = cfdaNumber;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public AwardStatusDto getAwardStatus() {
		return awardStatus;
	}
	public void setAwardStatus(AwardStatusDto awardStatus) {
		this.awardStatus = awardStatus;
	}
	public ActivityTypeDto getActivityType() {
		return activityType;
	}
	public void setActivityType(ActivityTypeDto activityType) {
		this.activityType = activityType;
	}
	public List<SubAwardDto> getSubAwardList() {
		return subAwardList;
	}
	public void setSubAwardList(List<SubAwardDto> subAwardList) {
		this.subAwardList = subAwardList;
	}
	public InvestigatorDto getPrincipalInvestigator() {
		return principalInvestigator;
	}
	public void setPrincipalInvestigator(InvestigatorDto principalInvestigator) {
		this.principalInvestigator = principalInvestigator;
	}


}
