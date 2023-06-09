package com.ktds.IaCOps.api.swconfigmanagement.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ktds.IaCOps.api.inventory.service.InventoryService;
import com.ktds.IaCOps.common.parsing.yaml.component.YamlComponent;
import com.ktds.IaCOps.iacengine.ansible.component.AnsibleComponent;

@Service
public class SwConfigManagementService {

    private static final Logger logger = LoggerFactory.getLogger(SwConfigManagementService.class);

    @Autowired
    AnsibleComponent ansibleComponent;

    @Autowired
    InventoryService inventoryService;

    @Autowired
    YamlComponent yamlComponent;

    @Value("${filePath.infra_code.sw_config.path}")
    private String sw_config_path;

    public Map<String, List<String>> dryRun(String id) {

        Map<String, List<String>> result = new HashMap<>();

        /*
         * VCS을 어떤 걸 사용하고 있는 지
         * 체크 이후 해당 VCS에 맞는 객체를 사용해야 할 수 있음 (gitlab or github)
         * 데모버전은 gitlab을 사용하므로 gitlab 객체를 이용
         */

        String fileName = id + ".yaml";
        Map<String, Object> itemMap = yamlComponent.getAllItemsFromYaml(sw_config_path, fileName);

        // Target Host 지정
        String ip = itemMap.get("ip").toString();
        ansibleComponent.setHost(ip);

        // 수행할 playbook을 찾음
        Map<String, Object> sw = (Map<String, Object>) itemMap.get("sw");
        Map<String, Object> os = (Map<String, Object>) itemMap.get("os");

        /*
         * sw 
         */
        if (sw.containsKey("db")) {
            Map<String, Object> db = (Map<String, Object>) sw.get("db");

            List<String> dbRunPlaybookNames = getRunPlaybookNames(db);
            for (String pbName : dbRunPlaybookNames) {
    
                pbName = pbName+".yaml";
                // 수행할 playbook 지정
                ansibleComponent.selectPlaybook(pbName);
                // dryDiffRun 수행
                List<String> log = ansibleComponent.dryDiffRunPlaybook();
                result.put(pbName, log);
            }
        }
        
        if (sw.containsKey("mw")) {
        Map<String, Object> mw = (Map<String, Object>) sw.get("mw");
        List<String> mwRunPlaybookNames = getRunPlaybookNames(mw);
        for (String pbName : mwRunPlaybookNames) {

            pbName = pbName+".yaml";
            
            // 수행할 playbook 지정
            ansibleComponent.selectPlaybook(pbName);
            // dryDiffRun 수행
            List<String> log = ansibleComponent.dryDiffRunPlaybook();
            result.put(pbName, log);
        }
    }

        /*
         * os 
         */

        String extraVars = "";
         if (os instanceof Map) {
            for (Map.Entry<String, Object> entry : ((Map<String, Object>)os).entrySet()) {
                String key = entry.getKey(); 
                String pbName = key + ".yaml";
                ansibleComponent.selectPlaybook(pbName);
                Map<String, Object> valueMap = (Map<String, Object>) entry.getValue();
    
                // 플레이북에서 실행할 파라미터 값을 가져온다
                for (Map.Entry<String, Object> valueEntry : valueMap.entrySet()) {
                    extraVars = extraVars + valueEntry.getKey() + "=" + valueEntry.getValue() +" ";
                }
                List<String> log = ansibleComponent.dryDiffRunPlaybook(extraVars);
                result.put(pbName, log);
                        }
        }

        // for (Map.Entry<String, Object> entry : os.entrySet()) {
        //     String key = entry.getKey(); 
        //     String pbName = key + ".yaml";
        //     ansibleComponent.selectPlaybook(pbName);
        //     Map<String, Object> valueMap = (Map<String, Object>) entry.getValue();

        //     // 플레이북에서 실행할 파라미터 값을 가져온다
        //     for (Map.Entry<String, Object> valueEntry : valueMap.entrySet()) {
        //         extraVars = extraVars + valueEntry.getKey() + "=" + valueEntry.getValue() +" ";
        //     }
        //     List<String> log = ansibleComponent.dryDiffRunPlaybook(extraVars);
        //     result.put(pbName, log);
        
        // }

        return result;
    }

    public Map<String, List<String>> run(String id) {

        Map<String, List<String>> result = new HashMap<>();

        /*
         * VCS을 어떤 걸 사용하고 있는 지
         * 체크 이후 해당 VCS에 맞는 객체를 사용해야 할 수 있음 (gitlab or github)
         * 데모버전은 gitlab을 사용하므로 gitlab 객체를 이용
         */

        String fileName = id + ".yaml";
        Map<String, Object> itemMap = yamlComponent.getAllItemsFromYaml(sw_config_path, fileName);

        // Target Host 지정
        String ip = itemMap.get("ip").toString();
        ansibleComponent.setHost(ip);

        // 수행할 playbook을 찾음
        Map<String, Object> sw = (Map<String, Object>) itemMap.get("sw");
        Map<String, Object> os = (Map<String, Object>) itemMap.get("os");

        /*
         * sw 
         */
        if (sw.containsKey("db")) {
            Map<String, Object> db = (Map<String, Object>) sw.get("db");

            List<String> dbRunPlaybookNames = getRunPlaybookNames(db);
            for (String pbName : dbRunPlaybookNames) {
    
                pbName = pbName+".yaml";
                // 수행할 playbook 지정
                ansibleComponent.selectPlaybook(pbName);
                // dryDiffRun 수행
                List<String> log = ansibleComponent.runPlaybook();
                result.put(pbName, log);
            }
        }
        
        if (sw.containsKey("mw")) {
        Map<String, Object> mw = (Map<String, Object>) sw.get("mw");
        List<String> mwRunPlaybookNames = getRunPlaybookNames(mw);
        for (String pbName : mwRunPlaybookNames) {

            pbName = pbName+".yaml";
            
            // 수행할 playbook 지정
            ansibleComponent.selectPlaybook(pbName);
            // dryDiffRun 수행
            List<String> log = ansibleComponent.runPlaybook();
            result.put(pbName, log);
        }
    }

        /*
         * os 
         */

        String extraVars = "";
         if (os instanceof Map) {
            for (Map.Entry<String, Object> entry : ((Map<String, Object>)os).entrySet()) {
                String key = entry.getKey(); 
                String pbName = key + ".yaml";
                ansibleComponent.selectPlaybook(pbName);
                Map<String, Object> valueMap = (Map<String, Object>) entry.getValue();
    
                // 플레이북에서 실행할 파라미터 값을 가져온다
                for (Map.Entry<String, Object> valueEntry : valueMap.entrySet()) {
                    extraVars = extraVars + valueEntry.getKey() + "=" + valueEntry.getValue() +" ";
                }
                List<String> log = ansibleComponent.runPlaybook(extraVars);
                result.put(pbName, log);
                        }
        }

        return result;
    }

    // hostid를 기준으로 sw형상정보 yaml을 관리
    public boolean setSwConfigFile(String hostid) {
        try {
            Map<String, Object> swConfig = yamlComponent.getAllItemsFromYaml(sw_config_path, hostid);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // hostid를 기준으로 sw형상정보 yaml을 관리
    public Map<String, Object> getHostSwConfig(String hostid) {
        try {
            Map<String, Object> swConfig = yamlComponent.getAllItemsFromYaml(sw_config_path, hostid);
            return swConfig;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean setHostSwConfig(String hostid, Map<String, Object> yamlData) {

        boolean result = false;
        try {
            result = yamlComponent.overwriteFromYaml(sw_config_path, hostid, yamlData);
            return result;
        } catch (Exception e) {
            return result;
        }
    }

    private List<String> getRunPlaybookNames(Map<String, Object> services) {
        List<String> runPlaybookNames = new ArrayList<>();
        for (Map.Entry<String, Object> entry : services.entrySet()) {
            StringBuilder runPlaybookName = new StringBuilder();
            runPlaybookName.append(entry.getKey()); // 서비스 이름 출력 ("nginx", "apache", ...)
            Map<String, Object> serviceConfig = (Map<String, Object>) entry.getValue();
            runPlaybookName.append("_");
            runPlaybookName.append(serviceConfig.get("version"));
            runPlaybookName.append("_");
            runPlaybookName.append(serviceConfig.get("state"));
            runPlaybookName.append("_");
            runPlaybookName.append(serviceConfig.get("install"));
            runPlaybookNames.add(runPlaybookName.toString());
        }
        return runPlaybookNames;
    }

}
