/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.cmrreports.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.cmrreports.api.reporting.SetupPECGReport;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class CMRReportsController {
	
	/** Logger for this class and subclasses */
	protected final Log log = LogFactory.getLog(getClass());
	
	@RequestMapping(value = "/module/cmrreports/cmrreports", method = RequestMethod.GET)
	public void manage() {
	}
	
	@RequestMapping("/module/cmrreports/register_PECG")
	public ModelAndView registerPECG() throws Exception {
		new SetupPECGReport().setup();
		return new ModelAndView(new RedirectView("cmrreports.form"));
	}
	
	@RequestMapping("/module/cmrreports/remove_PECG")
	public ModelAndView removePECG() throws Exception {
		new SetupPECGReport().delete();
		return new ModelAndView(new RedirectView("cmrreports.form"));
	}
	
}
