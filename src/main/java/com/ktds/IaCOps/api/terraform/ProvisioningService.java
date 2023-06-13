package com.ktds.IaCOps.api.terraform;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ktds.IaCOps.api.inventory.model.Inventory;
import com.ktds.IaCOps.api.inventory.service.InventoryService;
import com.ktds.IaCOps.common.file.component.FileManagementComponent;
import com.ktds.IaCOps.common.parsing.json.component.JsonComponent;
import com.ktds.IaCOps.iacengine.terraform.component.TerraformComponent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProvisioningService {

    @Value("${filePath.infra_code.infra_config.path}")
    private String infraConfigPath;

    @Value("${filePath.infra_code.sw_config.path}")
    String swConfigPath;

    @Autowired
    TerraformComponent terraformComponent;

    @Autowired
    InventoryService inventoryService;

    @Autowired
    FileManagementComponent fileManagementComponent;

    @Autowired
    JsonComponent jsonComponent;


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
            log.info(swConfigPath);
            log.info(inventory.getId());
            log.info(swConfigPath+inventory.getId());
            fileManagementComponent.createFile(swConfigPath + inventory.getId() + ".yaml", "create!!");
        }

    }

    public List<String> getTfStatus() {
        try {
            return jsonComponent.readJsonFileToList(infraConfigPath+"terraform.tfstate");
        } catch (IOException e) {
            log.debug(e.toString());
            return null;
        }
    }
}
