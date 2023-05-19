package com.ktds.IaCOps.api.inventory.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ktds.IaCOps.api.inventory.controller.InventoryController;
import com.ktds.IaCOps.api.inventory.model.Inventory;
import com.ktds.IaCOps.common.parsing.json.component.JsonComponent;
import com.ktds.IaCOps.common.parsing.yaml.component.YamlComponent;

@Service
public class InventoryService {

    private static final Logger logger = LoggerFactory.getLogger(InventoryController.class);

    @Autowired
    YamlComponent yamlComponent;

    @Autowired
    JsonComponent jsonComponent;

    @Value("${filePath.infra_code.infra_config.path}")
    private String infraConfigPath;

    @Value("${filePath.infra_code.infra_config.inventory_infofile}")
    private String inventoryInfofile;

    public List<Inventory> getAllInventory() {

        String jsonString;
        try {
            jsonString = jsonComponent.readJsonFile(infraConfigPath + inventoryInfofile);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error("Not Read JsonFile: " + infraConfigPath + inventoryInfofile, e.toString(), e);
            return null;
        }

        List<Inventory> inventories = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray resources = jsonObject.getJSONArray("resources");

        for (int i = 0; i < resources.length(); i++) {
            JSONObject resource = resources.getJSONObject(i);
            JSONArray instances = resource.getJSONArray("instances");

            for (int j = 0; j < instances.length(); j++) {
                JSONObject instance = instances.getJSONObject(j);
                JSONObject attributes = instance.getJSONObject("attributes");

                String id = attributes.optString("id");
                String publicIp = attributes.optString("public_ip");
                String name = resource.optString("name");

                Inventory inventory = new Inventory(id, publicIp, name);
                inventories.add(inventory);
            }
        }

        return inventories;

    }

}
