/**
 * DSS - Digital Signature Services
 * Copyright (C) 2015 European Commission, provided under the CEF programme
 * 
 * This file is part of the "DSS - Digital Signature Services" project.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package eu.europa.esig.dss.validation.process.bbb.xcv.rfc.checks;

import java.util.Date;

import eu.europa.esig.dss.jaxb.detailedreport.XmlRFC;
import eu.europa.esig.dss.validation.policy.rules.Indication;
import eu.europa.esig.dss.validation.policy.rules.SubIndication;
import eu.europa.esig.dss.validation.process.ChainItem;
import eu.europa.esig.dss.validation.process.MessageTag;
import eu.europa.esig.dss.validation.reports.wrapper.RevocationWrapper;
import eu.europa.esig.jaxb.policy.LevelConstraint;

public class NextUpdateCheck extends ChainItem<XmlRFC> {

	private final RevocationWrapper revocationData;

	public NextUpdateCheck(XmlRFC result, RevocationWrapper revocationData, LevelConstraint constraint) {
		super(result, constraint);

		this.revocationData = revocationData;
	}

	@Override
	protected boolean process() {
		if (revocationData != null) {
			Date nextUpdate = revocationData.getNextUpdate();
			return nextUpdate != null;
		}
		return false;
	}

	@Override
	protected MessageTag getMessageTag() {
		return MessageTag.BBB_RFC_NUP;
	}

	@Override
	protected MessageTag getErrorMessageTag() {
		return MessageTag.BBB_RFC_NUP_ANS;
	}

	@Override
	protected Indication getFailedIndicationForConclusion() {
		return Indication.INDETERMINATE;
	}

	@Override
	protected SubIndication getFailedSubIndicationForConclusion() {
		return SubIndication.TRY_LATER;
	}

}
