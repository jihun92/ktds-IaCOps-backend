package com.ktds.IaCOps.common.file.component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.springframework.stereotype.Component;

@Component
public class FileManagementComponent {

    public void createFile(String filePath, String content) {
        File file = new File(filePath);
        if(file.exists()){
            return;
        } else {
            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter(file));
                writer.write(content);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (writer != null) {
                        writer.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean deleteFile(String filePath) {
        File file = new File(filePath);
        return file.delete();
    }
    
}
