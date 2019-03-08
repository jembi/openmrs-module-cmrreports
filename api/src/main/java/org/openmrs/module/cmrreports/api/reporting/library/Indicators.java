/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.cmrreports.api.reporting.library;

import java.util.Date;
import java.util.Map;

import org.openmrs.module.reporting.cohort.definition.AgeCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.GenderCohortDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.evaluation.parameter.ParameterizableUtil;
import org.openmrs.module.reporting.indicator.CohortIndicator;

public class Indicators {
	
	public static CohortIndicator newCohortIndicator(String name, CohortDefinition cohort, Map<String, Object> map) {
		CohortIndicator i = new CohortIndicator();
		i.setName(name);
		i.setCohortDefinition(new Mapped<CohortDefinition>(cohort, map));
		i.addParameter(new Parameter("startDate", "Start date", Date.class));
		i.addParameter(new Parameter("endDate", "End date", Date.class));
		
		return i;
	}
	
	public static CohortIndicator getPatientBetween50AndAboveIndicator() {
		return newCohortIndicator("patientBetween50AndAboveIndicator", Cohorts.patientWithAgeAbove(50), ParameterizableUtil.createParameterMappings("effectiveDate=${endDate}"));
	}
	
	public static CohortIndicator getPatientBetween25And49YearsIndicator() {
		return newCohortIndicator("patientBetween25And49YearsIndicator", Cohorts.createXtoYAgeCohort("PatientBetween25And49Years", 25, 49), ParameterizableUtil.createParameterMappings("effectiveDate=${endDate}"));
	}
	
	public static CohortIndicator getPatientBetween20And24YearsIndicator() {
		return newCohortIndicator("patientBetween20And24YearsIndicator", Cohorts.createXtoYAgeCohort("PatientBetween20And24Years", 20, 24), ParameterizableUtil.createParameterMappings("effectiveDate=${endDate}"));
	}
	
	public static CohortIndicator getPatientBetween15And19YearsIndicator() {
		return newCohortIndicator("patientBetween15And19YearsIndicator", Cohorts.createXtoYAgeCohort("PatientBetween15And19Years", 15, 19), ParameterizableUtil.createParameterMappings("effectiveDate=${endDate}"));
	}
	
	public static CohortIndicator getPatientBetween10And14YearsIndicator() {
		return newCohortIndicator("patientBetween10And14YearsIndicator", Cohorts.createXtoYAgeCohort("PatientBetween10And14Years", 10, 14), ParameterizableUtil.createParameterMappings("effectiveDate=${endDate}"));
	}
	
	public static CohortIndicator getPatientBetween5And9YearsIndicator() {
		return newCohortIndicator("patientBetween5And9YearsIndicator", Cohorts.createXtoYAgeCohort("PatientBetween5And9Years", 5, 9), ParameterizableUtil.createParameterMappings("effectiveDate=${endDate}"));
	}
	
	public static CohortIndicator getPatientBetween1And4YearsIndicator() {
		return newCohortIndicator("patientBetween1And4YearsIndicator", Cohorts.createXtoYAgeCohort("PatientBetween1And4Years", 1, 4), ParameterizableUtil.createParameterMappings("effectiveDate=${endDate}"));
	}
	
	public static CohortIndicator getPatientBelow1YearIndicator() {
		return newCohortIndicator("patientBelow1YearIndicator", Cohorts.patientWithAgeBelow(1), ParameterizableUtil.createParameterMappings("effectiveDate=${endDate}"));
	}
	
	public static CohortIndicator getPatientInYearRangeIndicator(AgeCohortDefinition ageCohort, GenderCohortDefinition genderCohort) {
		return newCohortIndicator("patientInYearRangeIndicator", Cohorts.getpatientInYearRange(ageCohort, genderCohort), ParameterizableUtil.createParameterMappings("effectiveDate=${endDate}"));
	}
	
	public static CohortIndicator getAllPatientsIndicator() {
		CompositionCohortDefinition allPatients = Cohorts.getAllPatientsByGender(Cohorts.getMales(), Cohorts.getFemales());
		return newCohortIndicator("patientInYearRangeIndicator", allPatients, ParameterizableUtil.createParameterMappings("effectiveDate=${endDate}"));
	}
}
