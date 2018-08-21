package edu.hubu.utils;

import java.util.UUID;

/**
 * @BelongsProject: demoDrools
 * @BelongsPackage: edu.hubu.utils
 * @Author: Deson
 * @CreateTime: 2018-08-14 14:55
 * @Description:
 */
public class UUIDGenerate {

    public static void main(String[] args) {
        for (int i= 0; i< 44;i++){
            System.out.println(UUID.randomUUID().toString());
        }
    }
}
