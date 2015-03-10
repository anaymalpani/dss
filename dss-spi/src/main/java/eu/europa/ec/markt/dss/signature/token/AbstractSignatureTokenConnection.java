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
package eu.europa.ec.markt.dss.signature.token;

import java.security.Signature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.markt.dss.DigestAlgorithm;
import eu.europa.ec.markt.dss.EncryptionAlgorithm;
import eu.europa.ec.markt.dss.SignatureAlgorithm;
import eu.europa.ec.markt.dss.SignatureValue;
import eu.europa.ec.markt.dss.ToBeSigned;
import eu.europa.ec.markt.dss.exception.DSSException;

/**
 *
 */
public abstract class AbstractSignatureTokenConnection implements SignatureTokenConnection {

	protected static final Logger LOG = LoggerFactory.getLogger(AbstractSignatureTokenConnection.class);

	@Override
	@Deprecated
	public byte[] sign(final byte[] bytes, final DigestAlgorithm digestAlgorithm, final DSSPrivateKeyEntry keyEntry) throws DSSException {

		ToBeSigned tbs = new ToBeSigned();
		tbs.setBytes(bytes);
		return sign(tbs, digestAlgorithm, keyEntry).getValue();
	}

	@Override
	public SignatureValue sign(ToBeSigned toBeSigned, DigestAlgorithm digestAlgorithm, DSSPrivateKeyEntry keyEntry) throws DSSException {

		final EncryptionAlgorithm encryptionAlgorithm = keyEntry.getEncryptionAlgorithm();
		LOG.info("Signature algorithm: " + encryptionAlgorithm + "/" + digestAlgorithm);
		final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.getAlgorithm(encryptionAlgorithm, digestAlgorithm);
		final String javaSignatureAlgorithm = signatureAlgorithm.getJCEId();

		try {
			final Signature signature = Signature.getInstance(javaSignatureAlgorithm);
			signature.initSign(keyEntry.getPrivateKey());
			signature.update(toBeSigned.getBytes());
			final byte[] signatureValue = signature.sign();
			SignatureValue value = new SignatureValue();
			value.setAlgorithm(signatureAlgorithm);
			value.setValue(signatureValue);
			return value;
		} catch(Exception e) {
			throw new DSSException(e);
		}

	}

}