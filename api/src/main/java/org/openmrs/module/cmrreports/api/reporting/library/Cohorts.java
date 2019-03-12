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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.Program;
import org.openmrs.VisitType;
import org.openmrs.module.reporting.cohort.definition.AgeCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.GenderCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.common.DurationUnit;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.evaluation.parameter.ParameterizableUtil;

public class Cohorts {
	
	public Log log = LogFactory.getLog(getClass());
	
	public static GenderCohortDefinition getMales() {
		GenderCohortDefinition males = new GenderCohortDefinition();
		males.setName("male Patients");
		males.setMaleIncluded(true);
		males.setFemaleIncluded(false);
		return males;
	}
	
	public static GenderCohortDefinition getFemales() {
		GenderCohortDefinition females = new GenderCohortDefinition();
		females.setName("female Patients");
		females.setMaleIncluded(false);
		females.setFemaleIncluded(true);
		return females;
	}
	
	public static AgeCohortDefinition patientWithAgeBelow(int age) {
		AgeCohortDefinition patientsWithAgebelow = new AgeCohortDefinition();
		patientsWithAgebelow.setName("patientsWithAgebelow");
		patientsWithAgebelow.addParameter(new Parameter("effectiveDate", "effectiveDate", Date.class));
		patientsWithAgebelow.setMaxAge(age - 1);
		patientsWithAgebelow.setMaxAgeUnit(DurationUnit.YEARS);
		return patientsWithAgebelow;
	}
	
	public static AgeCohortDefinition patientWithAgeAbove(int age) {
		AgeCohortDefinition patientsWithAge = new AgeCohortDefinition();
		patientsWithAge.setName("patientsWithAge");
		patientsWithAge.addParameter(new Parameter("effectiveDate", "effectiveDate", Date.class));
		patientsWithAge.setMinAge(age);
		patientsWithAge.setMinAgeUnit(DurationUnit.YEARS);
		return patientsWithAge;
	}
	
	public static AgeCohortDefinition createXtoYAgeCohort(String name, int minAge, int maxAge) {
		AgeCohortDefinition xToYCohort = new AgeCohortDefinition();
		xToYCohort.setName(name);
		xToYCohort.setMaxAge(new Integer(maxAge));
		xToYCohort.setMinAge(new Integer(minAge));
		xToYCohort.addParameter(new Parameter("effectiveDate", "endDate", Date.class));
		return xToYCohort;
	}
	
	public static AgeCohortDefinition createOverXAgeCohort(String name, int minAge) {
		AgeCohortDefinition overXCohort = new AgeCohortDefinition();
		overXCohort.setName(name);
		overXCohort.setMinAge(new Integer(minAge));
		overXCohort.addParameter(new Parameter("effectiveDate", "endDate", Date.class));
		return overXCohort;
	}
	
	public static CompositionCohortDefinition getpatientInYearRange(AgeCohortDefinition ageCohort, GenderCohortDefinition genderCohort) {
		
		CompositionCohortDefinition patientInYearRange = new CompositionCohortDefinition();
		patientInYearRange.setName("patientInYearRangeEnrolledInHIVStarted");
		patientInYearRange.addParameter(new Parameter("effectiveDate", "effectiveDate", Date.class));
		patientInYearRange.getSearches().put("1", new Mapped<CohortDefinition>(ageCohort, ParameterizableUtil.createParameterMappings("effectiveDate=${effectiveDate}")));
		patientInYearRange.getSearches().put("2", new Mapped<CohortDefinition>(genderCohort, null));
		patientInYearRange.setCompositionString("1 and 2");
		return patientInYearRange;
		
	}
	
	public static CompositionCohortDefinition getAllPatientsByGender(GenderCohortDefinition males, GenderCohortDefinition females) {
		
		CompositionCohortDefinition allPatients = new CompositionCohortDefinition();
		allPatients.setName("patientInYearRangeEnrolledInHIVStarted");
		allPatients.addParameter(new Parameter("effectiveDate", "effectiveDate", Date.class));
		allPatients.getSearches().put("1", new Mapped<CohortDefinition>(males, null));
		allPatients.getSearches().put("2", new Mapped<CohortDefinition>(females, null));
		allPatients.setCompositionString("1 or 2");
		return allPatients;
	}
	
	public static List<AgeCohortDefinition> getAllAgeRanges() {
		
		AgeCohortDefinition PatientBelow1Year = Cohorts.patientWithAgeBelow(1);
		PatientBelow1Year.setName("PatientBelow1Year");
		AgeCohortDefinition PatientBetween1And4Years = Cohorts.createXtoYAgeCohort("PatientBetween1And4Years", 1, 4);
		AgeCohortDefinition PatientBetween5And9Years = Cohorts.createXtoYAgeCohort("PatientBetween5And9Years", 5, 9);
		AgeCohortDefinition PatientBetween10And14Years = Cohorts.createXtoYAgeCohort("PatientBetween10And14Years", 10, 14);
		AgeCohortDefinition PatientBetween15And19Years = Cohorts.createXtoYAgeCohort("PatientBetween15And19Years", 15, 19);
		AgeCohortDefinition PatientBetween20And24Years = Cohorts.createXtoYAgeCohort("PatientBetween20And24Years", 20, 24);
		AgeCohortDefinition PatientBetween25And49Years = Cohorts.createXtoYAgeCohort("PatientBetween25And49Years", 25, 49);
		AgeCohortDefinition PatientBetween50YearsAndAbove = Cohorts.patientWithAgeAbove(50);
		PatientBetween50YearsAndAbove.setName("PatientBetween50YearsAndAbove");
		
		ArrayList<AgeCohortDefinition> allAgeRanges = new ArrayList<AgeCohortDefinition>();
		allAgeRanges.add(PatientBelow1Year);
		allAgeRanges.add(PatientBetween1And4Years);
		allAgeRanges.add(PatientBetween5And9Years);
		allAgeRanges.add(PatientBetween10And14Years);
		allAgeRanges.add(PatientBetween15And19Years);
		allAgeRanges.add(PatientBetween20And24Years);
		allAgeRanges.add(PatientBetween25And49Years);
		allAgeRanges.add(PatientBetween50YearsAndAbove);
		return allAgeRanges;
	}
	
	public static SqlCohortDefinition getPatientsCurrentlyOnRegimenBasedOnEndDate(String name, Concept concept) {
		SqlCohortDefinition patientOnRegimen = new SqlCohortDefinition();
		
		StringBuilder query = new StringBuilder("select distinct patient_id from orders where concept_id in (");
		query.append(concept.getId());
		query.append(") and voided=0 and start_date <= :endDate and (discontinued=0 or discontinued_date > :endDate)");
		patientOnRegimen.setQuery(query.toString());
		patientOnRegimen.addParameter(new Parameter("endDate", "endDate", Date.class));
		patientOnRegimen.setName(name);
		
		return patientOnRegimen;
	}
	
	public static SqlCohortDefinition getPatientsOnRegimenBeforStartDate(String name, Concept concept) {
		SqlCohortDefinition patientOnRegimen = new SqlCohortDefinition();
		
		StringBuilder query = new StringBuilder("select distinct patient_id from orders where concept_id in (");
		query.append(concept.getId());
		query.append(") and voided=0 and start_date <= :startDate");
		patientOnRegimen.setQuery(query.toString());
		patientOnRegimen.addParameter(new Parameter("startDate", "startDate", Date.class));
		patientOnRegimen.setName(name);
		
		return patientOnRegimen;
	}
	
	
	/*
	 * Just to demo that you can use SQL queries for cohort definitions. You can
	 * achieve the same here using a Program Enrollment Query and a
	 * VisitCohortDefinition from the reporting module
	 */
	public static SqlCohortDefinition getPatientEnrolledInProgramWithUPECVisit(Program HIVProgram, VisitType UPECVisitType) {
		SqlCohortDefinition patientEnrolledInProgramWithUPECVisit = new SqlCohortDefinition();
		patientEnrolledInProgramWithUPECVisit.setName("patientWithHIVOutcomes");
		patientEnrolledInProgramWithUPECVisit.setQuery("select v.patient_id from visit v,patient_program pp where v.visit_type_id=" + UPECVisitType.getVisitTypeId() + " and v.patient_id=pp.patient_id and pp.program_id=" + HIVProgram.getProgramId() + " and pp.voided=0 and pp.date_enrolled <= :onOrBefore" + " and (pp.date_completed >= :onOrAfter or pp.date_completed is null)" + " and v.voided=0 and v.date_started >= :onOrAfter and v.date_started <= :onOrBefore");
		
		patientEnrolledInProgramWithUPECVisit.addParameter(new Parameter("onOrAfter", "onOrAfter", Date.class));
		patientEnrolledInProgramWithUPECVisit.addParameter(new Parameter("onOrBefore", "onOrBefore", Date.class));
		return patientEnrolledInProgramWithUPECVisit;
	}
	
}
