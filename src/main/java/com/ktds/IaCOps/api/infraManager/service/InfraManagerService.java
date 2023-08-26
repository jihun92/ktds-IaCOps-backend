package com.ktds.IaCOps.api.infraManager.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ktds.IaCOps.iacengine.ansible.component.AnsibleComponent;

@Service
public class InfraManagerService {

    @Autowired
    AnsibleComponent ansibleComponent;

    public List<String> run(String ip, String action) {

        ansibleComponent.setHost(ip);
        
        String pbName = "";
        if(action.equals("reboot")){
            pbName = "reboot.yaml";
        }
        else if(action.equals("l4_mw_root")){
            pbName = "l4_mw_root.yaml";
        }
        else if(action.equals("db_check")){
            pbName = "db_check.yaml";
        }
        else if(action.equals("os_mw_root")){
            pbName = "os_mw_root.yaml";
        }

        ansibleComponent.selectPlaybook(pbName);
        // List<String> log = ansibleComponent.runPlaybook();
        List<String> log = ansibleComponent.runActionPlaybook();
        return log;
    }

}
