package org.kuali.coeus.award.summary;

import java.util.Collection;

import org.codehaus.jackson.map.annotate.JsonView;
import org.kuali.kra.award.home.Award;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("awardSummaryController")
public class AwardSummaryController {

	@Autowired
	@Qualifier("businessObjectService")
	private BusinessObjectService businessObjectService;

	@RequestMapping("/v1/awardSummary")
	public @ResponseBody Collection<Award> getAwardSummary(@RequestParam(value="updatedSince", required=false) String updatedSince) {
		return getBusinessObjectService().findAll(Award.class);
	}

	public BusinessObjectService getBusinessObjectService() {
		return businessObjectService;
	}

	public void setBusinessObjectService(BusinessObjectService businessObjectService) {
		this.businessObjectService = businessObjectService;
	}
	
}
