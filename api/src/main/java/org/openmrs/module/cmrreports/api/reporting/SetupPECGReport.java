/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.cmrreports.api.reporting;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.openmrs.Program;
import org.openmrs.api.context.Context;
import org.openmrs.module.cmrreports.api.reporting.library.Cohorts;
import org.openmrs.module.cmrreports.api.reporting.library.Indicators;
import org.openmrs.module.reporting.cohort.definition.AgeCohortDefinition;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.evaluation.parameter.ParameterizableUtil;
import org.openmrs.module.reporting.indicator.CohortIndicator;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.openmrs.module.reporting.report.service.ReportService;

public class SetupPECGReport {
	
	private Program HIVProgram;
	
	private List<Program> HIVPrograms = new ArrayList<Program>();
	
	public void setup() throws Exception {
		
		setUpProperties();
		
		ReportDefinition rd = createReportDefinition();
		ReportDesign design = Helper.createRowPerPatientXlsOverviewReportDesign(rd, "PECG.xls", "PECG.xls_", null);
		Properties props = new Properties();
		props.put("repeatingSections", "sheet:1,dataset:PECG Data Set");
		props.put("sortWeight", "5000");
		design.setProperties(props);
		Helper.saveReportDesign(design);
		
	}
	
	public void delete() {
		ReportService rs = Context.getService(ReportService.class);
		for (ReportDesign rd : rs.getAllReportDesigns(false)) {
			if ("PECG.xls_".equals(rd.getName())) {
				rs.purgeReportDesign(rd);
			}
		}
		Helper.purgeReportDefinition("PECG Report");
	}
	
	private ReportDefinition createReportDefinition() {
		
		ReportDefinition rd = new ReportDefinition();
		rd.addParameter(new Parameter("reportingStartDate", "Start Date", Date.class));
		rd.addParameter(new Parameter("endDate", "End Date", Date.class));
		rd.setName("PECG Report");
		rd.setBaseCohortDefinition(Cohorts.createInProgramParameterizableByDate("hiv", HIVProgram), ParameterizableUtil.createParameterMappings("onDate=${endDate}"));
		rd.addDataSetDefinition(createBaseDataSet(), ParameterizableUtil.createParameterMappings("endDate=${endDate},reportingStartDate=${reportingStartDate}"));
		Helper.saveReportDefinition(rd);
		return rd;
	}
	
	private CohortIndicatorDataSetDefinition createBaseDataSet() {
		CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
		dsd.setName("PECG Data Set");
		dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
		dsd.addParameter(new Parameter("reportingStartDate", "Start Date", Date.class));
		createIndicators(dsd);
		return dsd;
	}
	
	private void createIndicators(CohortIndicatorDataSetDefinition dsd) {
		dsd.addColumn("2All<1", "PECG: Currently on ART: Patients below 1 year", new Mapped<CohortIndicator>(Indicators.getPatientBelow1YearIndicator(), ParameterizableUtil.createParameterMappings("startDate=${startDate},endDate=${endDate}")), "");
		dsd.addColumn("2All1-4", "PECG: Currently on ART: Patients between 1 and 4 years", new Mapped<CohortIndicator>(Indicators.getPatientBetween1And4YearsIndicator(), ParameterizableUtil.createParameterMappings("startDate=${startDate},endDate=${endDate}")), "");
		dsd.addColumn("2All5-9", "PECG: Currently on ART: Patients between 5 and 9 years", new Mapped<CohortIndicator>(Indicators.getPatientBetween5And9YearsIndicator(), ParameterizableUtil.createParameterMappings("startDate=${startDate},endDate=${endDate}")), "");
		dsd.addColumn("2All10-14", "PECG: Currently on ART: Patients between 10 and 14 years", new Mapped<CohortIndicator>(Indicators.getPatientBetween10And14YearsIndicator(), ParameterizableUtil.createParameterMappings("startDate=${startDate},endDate=${endDate}")), "");
		dsd.addColumn("2All15-19", "PECG: Currently on ART: Patients between 15 and 19 years", new Mapped<CohortIndicator>(Indicators.getPatientBetween15And19YearsIndicator(), ParameterizableUtil.createParameterMappings("startDate=${startDate},endDate=${endDate}")), "");
		dsd.addColumn("2All20-24", "PECG: Currently on ART: Patients between 20 and 24 years", new Mapped<CohortIndicator>(Indicators.getPatientBetween20And24YearsIndicator(), ParameterizableUtil.createParameterMappings("startDate=${startDate},endDate=${endDate}")), "");
		dsd.addColumn("2All25-49", "PECG: Currently on ART: Patients between 25 and 49 years", new Mapped<CohortIndicator>(Indicators.getPatientBetween25And49YearsIndicator(), ParameterizableUtil.createParameterMappings("startDate=${startDate},endDate=${endDate}")), "");
		dsd.addColumn("2All50-Above", "PECG: Currently on ART: Patients between 50 years and above", new Mapped<CohortIndicator>(Indicators.getPatientBetween50AndAboveIndicator(), ParameterizableUtil.createParameterMappings("startDate=${startDate},endDate=${endDate}")), "");
		
		// Male
		int i = 0;
		for (AgeCohortDefinition ageCohort : Cohorts.getAllAgeRanges()) {
			dsd.addColumn("2M" + i, "Males:PECG: Currently on ART by age and sex: " + ageCohort.getName(), new Mapped<CohortIndicator>(Indicators.getPatientInYearRangeIndicator(ageCohort, Cohorts.getMales()), ParameterizableUtil.createParameterMappings("startDate=${startDate},endDate=${endDate}")), "");
			i++;
		}
		
		// Females
		int j = 0;
		for (AgeCohortDefinition ageCohort : Cohorts.getAllAgeRanges()) {
			dsd.addColumn("2F" + j, "Females:PECG: Currently on ART by age and sex: " + ageCohort.getName(), new Mapped<CohortIndicator>(Indicators.getPatientInYearRangeIndicator(ageCohort, Cohorts.getFemales()), ParameterizableUtil.createParameterMappings("startDate=${startDate},endDate=${endDate}")), "");
			j++;
		}
		dsd.addColumn("2All", "PECG: Currently on ART", new Mapped<CohortIndicator>(Indicators.getAllPatientsIndicator(), ParameterizableUtil.createParameterMappings("startDate=${startDate},endDate=${endDate}")), "");
		
	}
	
	private void setUpProperties() {
		HIVProgram = Context.getProgramWorkflowService().getProgram(1);
		HIVPrograms.add(HIVProgram);
	}
	
}
