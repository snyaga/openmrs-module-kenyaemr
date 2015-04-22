package org.openmrs.module.kenyaemr.calculation.library.cohort;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Concept;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.patient.PatientCalculationService;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.kenyacore.test.TestUtils;
import org.openmrs.module.kenyaemr.Dictionary;
import org.openmrs.module.kenyaemr.metadata.CommonMetadata;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class SixMonthCohortCalculationTest extends BaseModuleContextSensitiveTest{

	@Autowired
	CommonMetadata commonMetadata;
	@Before
	public void setUp() throws Exception {
		executeDataSet("dataset/test-concepts.xml");
		commonMetadata.install();
	}

	@Test
	public void testEvaluate() throws Exception {
		Concept rtc = Dictionary.getConcept(Dictionary.RETURN_VISIT_DATE);

		TestUtils.saveObs(TestUtils.getPatient(6), rtc, TestUtils.date(2014, 9, 9), TestUtils.date(2014, 9, 2));
		TestUtils.saveObs(TestUtils.getPatient(6), rtc, TestUtils.date(2014, 9, 16), TestUtils.date(2014, 9, 9));
		TestUtils.saveObs(TestUtils.getPatient(6), rtc, TestUtils.date(2014, 4, 16), TestUtils.date(2014, 3, 9));
		TestUtils.saveObs(TestUtils.getPatient(7), rtc, TestUtils.date(2014, 9, 18), TestUtils.date(2014, 9, 4));
		TestUtils.saveObs(TestUtils.getPatient(8), rtc, TestUtils.date(2015, 3, 9), TestUtils.date(2014, 9, 2));

		Context.flushSession();
		PatientCalculationContext context = Context.getService(PatientCalculationService.class).createCalculationContext();
		context.setNow(TestUtils.date(2014, 6, 10));
		List<Integer> cohort = Arrays.asList(6, 7, 8);
		CalculationResultMap resultMap = new SixMonthCohortCalculation().evaluate(cohort, null, context);
		Assert.assertEquals(1, resultMap.keySet().size());
	}
}