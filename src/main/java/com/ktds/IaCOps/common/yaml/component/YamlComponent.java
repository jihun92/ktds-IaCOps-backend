package com.ktds.IaCOps.common.yaml.component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

@Component
public class YamlComponent {

	public Map<String, Object> readYaml(String filePath, String fileName) {

		InputStream inputStream = null;

		try {
			inputStream = new FileInputStream(new File(filePath+fileName));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		Yaml yaml = new Yaml();
		Map<String, Object> yamlData = yaml.load(inputStream);
		return yamlData;

	}

	public void writeYaml(String filePath, String fileName, String key, Object value, Map<String, Object> yamlData) {

		setValueFromNestedMap(yamlData, value, key.split("\\."));

		PrintWriter writer = null;
	    try {
	        writer = new PrintWriter(new File(filePath + fileName));
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }

	    DumperOptions options = new DumperOptions();
	    options.setIndent(2);
	    options.setPrettyFlow(true);
	    options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
	    Yaml yaml = new Yaml(options);
	    yaml.dump(yamlData, writer);
	}

	@SuppressWarnings("unchecked")
	public Object getValueFromNestedMap(Map<String, Object> map, String... keys) {
	    Object value = map;
	    for (String key : keys) {
	        if (!(value instanceof Map)) {
	            return null; // 잘못된 구조이므로 null을 반환
	        }
	        value = ((Map<String, Object>) value).get(key);
	    }
	    return value;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> setValueFromNestedMap(Map<String, Object> map, Object value, String... keys) {
	    if (keys.length == 1) {
	        map.put(keys[0], value);
	        return null;
	    }
	    Map<String, Object> nestedMap = (Map<String, Object>) map.get(keys[0]);
	    if (nestedMap == null) {
	        nestedMap = new HashMap<>();
	        map.put(keys[0], nestedMap);
	    }
	    setValueFromNestedMap(nestedMap, value, Arrays.copyOfRange(keys, 1, keys.length));

	   return nestedMap;

	}

}
