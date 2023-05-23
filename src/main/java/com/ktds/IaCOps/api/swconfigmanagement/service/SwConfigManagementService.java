package com.ktds.IaCOps.api.swconfigmanagement.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ktds.IaCOps.api.inventory.service.InventoryService;
import com.ktds.IaCOps.common.parsing.yaml.component.YamlComponent;
import com.ktds.IaCOps.iacengine.ansible.component.AnsibleComponent;

@Service
public class SwConfigManagementService {

    @Autowired
    AnsibleComponent ansibleComponent;

    @Autowired
    InventoryService inventoryService;

    @Autowired
    YamlComponent yamlComponent;

    @Value("${filePath.infra_code.sw_config.path}")
    private String sw_config_path;

    public List<String> DryRun(String hostip) {

        List<String> result = new ArrayList<>();

        /*
         * VCS을 어떤 걸 사용하고 있는 지
         * 체크 이후 해당 VCS에 맞는 객체를 사용해야 할 수 있음 (gitlab or github)
         * 데모버전은 gitlab을 사용하므로 gitlab 객체를 이용
         */

        // Target Host 지정
        ansibleComponent.setHost(hostip);

        // 수행할 playbook을 찾음
        ansibleComponent.selectPlaybook(null);

        // dryDiffRun 수행
        result = ansibleComponent.dryDiffRunPlaybook();

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

}
