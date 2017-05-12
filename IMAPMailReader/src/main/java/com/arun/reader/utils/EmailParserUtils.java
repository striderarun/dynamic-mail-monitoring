
package com.arun.reader.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import com.sun.mail.util.BASE64DecoderStream;

/**
 * Utility Class for parsing mail messages.
 */
public final class EmailParserUtils {

	private static final Logger LOGGER = Logger.getLogger(EmailParserUtils.class);
	private static final List<String> excelFormats = Arrays.asList("application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet","application/octet-stream");

	/** Prevent instantiation. */
	private EmailParserUtils() {
		throw new AssertionError();
	}

	public static void handleMessage(final File directory,
										final javax.mail.Message mailMessage,
										final List<EmailFragment> emailFragments) {

		final Object content;
		final String subject;
		try {
			content = mailMessage.getContent();
			subject = mailMessage.getSubject();
		} catch (IOException e) {
			throw new IllegalStateException("Error while retrieving the email contents.", e);
		} catch (MessagingException e) {
			throw new IllegalStateException("Error while retrieving the email contents.", e);
		}

		final File directoryToUse;

		if (directory == null) {
			directoryToUse = new File(subject);
		} else {
			directoryToUse = new File(directory, subject);
		}
		
		if (content instanceof Multipart) {
			Multipart multipart = (Multipart) content;
			handleMultipart(directoryToUse, multipart, mailMessage, emailFragments);
		} else if (content instanceof String || content instanceof BASE64DecoderStream) {
			LOGGER.info("No valid attachment found in email");
		}
		else {
			throw new IllegalStateException("This content type is not handled - " + content.getClass().getSimpleName());
		}

	}

	
	public static void handleMultipart(File directory, Multipart multipart, javax.mail.Message mailMessage, List<EmailFragment> emailFragments) {
		final Integer count;
		try {
			count = multipart.getCount();
			LOGGER.info(String.format("Number of enclosed BodyPart objects: %s.", count));
			for (int i = 0; i < count; i++) {
				final BodyPart bp;
				final String contentTypeString;
				String contentType = null;
				final String subject;
				final Object content;
				
				String filename;
				bp = multipart.getBodyPart(i);				
				contentTypeString = bp.getContentType();
				if (StringUtils.isNotBlank(contentTypeString) && contentTypeString.contains(";")) {
					contentType = contentTypeString.substring(0,contentTypeString.indexOf(";")).trim();
				} else {
					contentType = bp.getContentType();
				}
				LOGGER.info(String.format("Content Type of attachment: %s.", contentType));
				subject  = mailMessage.getSubject();
				content = bp.getContent();					
				if (content instanceof InputStream && excelFormats.contains(contentType)) {
					String filenameExtension = null;
					if (StringUtils.isNotBlank(bp.getFileName())) {
						filenameExtension = "." + FilenameUtils.getExtension(bp.getFileName());					
					}
					filename = subject + filenameExtension;
					final InputStream inputStream = (InputStream) content;
					final ByteArrayOutputStream bis = new ByteArrayOutputStream();
					IOUtils.copy(inputStream, bis);
					LOGGER.info(String.format("Handling Input Stream '%s', type: '%s'", filename, contentType));
					emailFragments.add(new EmailFragment(directory, filename, bis.toByteArray()));
				} else if (content instanceof javax.mail.Message)  {
					LOGGER.info(String.format("Handling Message type '%s'", contentType));
					handleMessage(directory, (javax.mail.Message) content, emailFragments);
				} else if (content instanceof Multipart) {
					LOGGER.info(String.format("Handling Multipart type '%s'", contentType));
					final Multipart mp2 = (Multipart) content;
					handleMultipart(directory, mp2, mailMessage, emailFragments);
				} else {
					LOGGER.info("No valid attachment found in email");
				}			
			}
		} catch (MessagingException e) {
			throw new IllegalStateException("Error while retrieving body part.", e);
		} catch (IOException e) {
			throw new IllegalStateException("Error while retrieving the email contents.", e);
		}		
	
	
	}
}
