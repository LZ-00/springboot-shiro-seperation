package com.lz;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.lz.util.AutoGenerateCodeUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AutoGenerateCode {

    @Autowired
    AutoGenerateCodeUtil autoGenerateCodeUtil;

    @Test
    void autoGenerateCode(){
        AutoGenerator mpg = new AutoGenerator();
        mpg.setGlobalConfig(autoGenerateCodeUtil.globalConfig())
                .setDataSource(autoGenerateCodeUtil.dataSourceConfig())
                .setPackageInfo(autoGenerateCodeUtil.packageConfig())
                .setStrategy(autoGenerateCodeUtil.strategyConfig("user"))
                .setCfg(autoGenerateCodeUtil.customizeConfigOfXMLPath())
                .execute();
    }

}
