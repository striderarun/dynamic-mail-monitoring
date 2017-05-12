package com.arun.reader.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.integration.annotation.Transformer;

/**
 * Parses the E-mail Message and converts each containing message and/or attachment into
 * a {@link List} of {@link EmailFragment}s.
 *
 */
public class EmailTransformer {

	private static final Logger LOGGER = Logger.getLogger(EmailTransformer.class);

	@Transformer
	public List<EmailFragment> transformit(javax.mail.Message mailMessage) {

		final List<EmailFragment> emailFragments = new ArrayList<EmailFragment>();

		EmailParserUtils.handleMessage(null, mailMessage, emailFragments);

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(String.format("Email contains %s fragments.", emailFragments.size()));
		}

		return emailFragments;
	}

}
