package com.spring.json_date_converter.getconvert;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import com.spring.json_date_converter.common.DateRegexFormationByDatePatterns;
import com.spring.json_date_converter.common.Utils;

public class ConvertDateByPattern {

	static Utils utils;

	private final static Pattern JSON_OBJECT_PATTERN = Pattern.compile("\"([^\"]+)\"\\s*:\\s*\\{");

	public ConvertDateByPattern(String[] formats, String targetFormat) {
		utils = new Utils(formats, targetFormat);
	}

	public String GetConvertDateByPattern(String jsonString) {
		return updateJson(jsonString);
	}

	public String updateJson(String jsonString) {
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			Map<String, JSONObject> jsonKeys = new LinkedHashMap<>();
			updateDte(jsonObject, utils.extractDateFields(jsonObject.toString()));
			extractJsonObjects(jsonObject, "", jsonKeys);
			for (String path : jsonKeys.keySet()) {
				updateJsonValue(jsonObject, path);
			}

			return jsonObject.toString();
		} catch (Exception e) {
			return e.toString();
		}
	}

	public void extractJsonObjects(JSONObject jsonObject, String parentPath, Map<String, JSONObject> keys) {
		Matcher matcher = JSON_OBJECT_PATTERN.matcher(jsonObject.toString());
		while (matcher.find()) {
			String key = matcher.group(1);
			String fullPath = parentPath.isEmpty() ? key : parentPath + "." + key;

			if (jsonObject.has(key) && jsonObject.get(key) instanceof JSONObject) {
				keys.put(fullPath, jsonObject.getJSONObject(key));
				extractJsonObjects(jsonObject.getJSONObject(key), fullPath, keys);
			}
		}
	}

	public void updateJsonValue(JSONObject jsonObject, String jsonPath) {
		String[] keys = jsonPath.split("\\.");
		JSONObject currentObject = jsonObject;
		for (int i = 0; i < keys.length; i++) {
			String key = keys[i];
			currentObject = currentObject.getJSONObject(key);
		}
		Map<String, String> datestr = utils.extractDateFields(currentObject.toString());
		updateDte(currentObject, datestr);
	}

	public void updateDte(JSONObject currentObject, Map<String, String> datestr) {
		for (String datestr1 : datestr.keySet()) {
			if (!currentObject.optString(datestr1).isBlank()) {
				currentObject.put(datestr1, utils.getDateString(currentObject.optString(datestr1)));
			}
		}

	}

	public void updateJsonValues(JSONObject jsonObject) {
		for (String key : jsonObject.keySet()) {
			Object value = jsonObject.get(key);

			if (value instanceof String) {
				String strValue = (String) value;
				for (String regex : DateRegexFormationByDatePatterns.formatToRegexMap.values()) {
					if (strValue.matches(regex)) {
						jsonObject.put(key, utils.getDateString(strValue));
					}
				}
			} else if (value instanceof JSONObject) {
				updateJsonValues((JSONObject) value);
			}
		}
	}

}
