package com.ktds.IaCOps.common.inventory.component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ktds.IaCOps.common.JsonComponent;
import com.ktds.IaCOps.common.inventory.model.Inventory;
import com.ktds.IaCOps.common.yaml.component.YamlComponent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class InventoryComponent {

    @Autowired
    YamlComponent yamlComponent;

    @Autowired
    JsonComponent jsonComponent;

    @Value("${filePath.infra_code.infra_config.path}")
    private String infraConfigPath;

    @Value("${filePath.infra_code.infra_config.inventory_infofile}")
    private String inventoryInfofile;

    public List<Inventory> getInventoryAllList() {

        String jsonString;
        try {
            jsonString = jsonComponent.readJsonFile(infraConfigPath+inventoryInfofile);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            log.error("Not Read JsonFile: "+infraConfigPath+inventoryInfofile,e.toString());
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
