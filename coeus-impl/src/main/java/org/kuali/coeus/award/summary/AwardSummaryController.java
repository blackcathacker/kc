package org.kuali.coeus.award.summary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.annotate.JsonView;
import org.kuali.coeus.sys.framework.rest.JsonViews;
import org.kuali.kra.award.home.Award;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codiform.moo.curry.Translate;

@Controller("awardSummaryController")
public class AwardSummaryController {

	@Autowired
	@Qualifier("businessObjectService")
	private BusinessObjectService businessObjectService;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	public AwardSummaryController() {
		objectMapper.configure(Feature.DEFAULT_VIEW_INCLUSION, false);
		objectMapper.configure(Feature.INDENT_OUTPUT, true);
	}

	@RequestMapping("/v1Jackson/awardSummary")
	public @ResponseBody String getAwardSummaryJackson(@RequestParam(value="updatedSince", required=false) String updatedSince) throws JsonGenerationException, JsonMappingException, IOException {
		return objectMapper.writerWithView(JsonViews.Summary.class)
				.writeValueAsString(getBusinessObjectService().findAll(Award.class));
	}
	
	@RequestMapping("/v1Map/awardSummary")
	public @ResponseBody List<Map> getAwardSummaryMap() {
		List<Map> result = new ArrayList<>();
		for (Award award : getBusinessObjectService().findAll(Award.class)) {
			result.add(award.toSummaryMap());
		}
		return result;
	}

	@RequestMapping("/v1Dto/awardSummary")
	public @ResponseBody List<AwardSummaryDto> getAwardSummaryDto() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		List<AwardSummaryDto> result = new ArrayList<>();
		for (Award award : new ArrayList<Award>(getBusinessObjectService().findAll(Award.class)).subList(0, 100)) {
			result.add(modelMapper.map(award, AwardSummaryDto.class));
		}
		return result;
	}
	
	@RequestMapping("/v1Moo/awardSummary")
	public @ResponseBody Collection<AwardSummaryDto> getAwardSummaryMoo() {
		return Translate.to(AwardSummaryDto.class).fromEach(getBusinessObjectService().findAll(Award.class));
	}

	public BusinessObjectService getBusinessObjectService() {
		return businessObjectService;
	}

	public void setBusinessObjectService(BusinessObjectService businessObjectService) {
		this.businessObjectService = businessObjectService;
	}
	
}
