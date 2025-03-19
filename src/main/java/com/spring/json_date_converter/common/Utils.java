package com.spring.json_date_converter.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.time.DateUtils;

public class Utils {

	private Pattern DATE_PATTERN;

	private String[] formats;

	private String TARGET_FORMAT;

	public Utils(String[] formats, String targetFormat) {
		this.formats = formats;
		this.DATE_PATTERN = Pattern.compile(DateRegexFormationByDatePatterns.convertDateFormatToRegex(formats));
		this.TARGET_FORMAT = targetFormat;
	}

	public Map<String, String> extractDateFields(String jsonString) {
		Map<String, String> dateFields = new LinkedHashMap<>();
		Matcher matcher = DATE_PATTERN.matcher(jsonString);
		while (matcher.find()) {
			dateFields.put(matcher.group(1), matcher.group(2));
		}
		return dateFields;
	}

	public String getDateString(String dateString) {
		try {
			if (dateString.isBlank()) {
				return "";
			} else {
				Date date = DateUtils.parseDateStrictly(dateString, formats);
				return new SimpleDateFormat(TARGET_FORMAT).format(date);
			}
		} catch (ParseException e) {
			return dateString;
		}
	}
}
