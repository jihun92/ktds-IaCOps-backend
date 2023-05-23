package com.ktds.IaCOps.api.provisioning.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ktds.IaCOps.api.inventory.model.Inventory;
import com.ktds.IaCOps.api.inventory.service.InventoryService;
import com.ktds.IaCOps.common.file.component.FileManagementComponent;
import com.ktds.IaCOps.iacengine.terraform.TerraformComponent;

@Service
public class ProvisioningService {

    private static final Logger logger = LoggerFactory.getLogger(ProvisioningService.class);

    @Autowired
    TerraformComponent terraformComponent;

    @Autowired
    InventoryService inventoryService;

    @Autowired
    FileManagementComponent fileManagementComponent;

    @Value("${filePath.infra_code.sw_config.path}")
    String sw_config_path;

    public List<String> plan() {
        return terraformComponent.plan();
    }

    public List<String> apply() {
        List<String> log = terraformComponent.apply();
        createHostSwConfigFile();
        return log;
    }

    public void createHostSwConfigFile() {

        // apply 완료되면 인벤토리별 sw형상관리 파일을 생성
        List<Inventory> inventories = inventoryService.getAllInventory();
        for (Inventory inventory : inventories) {
            logger.info(sw_config_path);
            logger.info(inventory.getId());
            logger.info(sw_config_path+inventory.getId());
            fileManagementComponent.createFile(sw_config_path + inventory.getId() + ".yaml", "create!!");
        }

    }
}
