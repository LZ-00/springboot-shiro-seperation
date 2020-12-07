package com.lz.util;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 乐。
 */
@Slf4j
@Component
public class AutoGenerateCodeUtil {

    private static final String PROJECT_PATH;
    private static final String CLASS_PATH;

    @Value("${spring.datasource.username}")
    private  String username;
    @Value("${spring.datasource.password}")
    private  String password;
    @Value("${spring.datasource.url}")
    private  String url;
    @Value("${spring.datasource.driver-class-name}")
    private  String driver;
    @Value("${spring.project.parent-path}")
    private  String parentPath;


    static {
        PROJECT_PATH = System.getProperty("user.dir");
        CLASS_PATH = PROJECT_PATH+"/src/main/java";
    }

    public GlobalConfig globalConfig(){
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(CLASS_PATH);
        gc.setAuthor("lz");
        gc.setOpen(false);
        gc.setFileOverride(false);
        gc.setServiceName("%sService");
        gc.setIdType(IdType.NONE);
        gc.setDateType(DateType.ONLY_DATE);
        gc.setSwagger2(true);
        gc.setBaseResultMap(true);
        gc.setBaseColumnList(true);
        return gc;

    }
    public DataSourceConfig dataSourceConfig(){

        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUsername(username);
        dsc.setPassword(password);
        dsc.setDriverName(driver);
        dsc.setDbType(DbType.MYSQL);
        dsc.setUrl(url);

        log.info("params:username======>{}",username);
        log.info("params:password======>{}",password);
        log.info("params:driver======>{}",driver);
        log.info("params:url======>{}",url);
        return dsc;

    }
    public PackageConfig packageConfig(){
        PackageConfig pc = new PackageConfig();
        pc.setMapper("mapper");
        pc.setService("service");
        pc.setServiceImpl("service.impl");
        pc.setController("controller");
        pc.setEntity("entity");
        pc.setXml(null);
        pc.setParent(parentPath);
        log.info("param:parentPath=====>",parentPath);
        return pc;
    }
    public StrategyConfig strategyConfig(String... tables){
        StrategyConfig strategy = new StrategyConfig();
        strategy.setInclude(tables);
        //驼峰命名
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        //添加lombok支持
        strategy.setEntityLombokModel(true);
        //logicDelete
        strategy.setLogicDeleteFieldName("deleted");
        //自动填充配置
        List<TableFill> tableFills = new ArrayList<>();
        TableFill gmtCreate= new TableFill("gmtCreate", FieldFill.INSERT);
        TableFill gmtModify= new TableFill("gmtModify", FieldFill.INSERT_UPDATE);
        tableFills.add(gmtCreate);
        tableFills.add(gmtModify);
        strategy.setTableFillList(tableFills);
        //乐观锁
        strategy.setVersionFieldName("version");
        //Controller策略
        strategy.setRestControllerStyle(true);
        strategy.setControllerMappingHyphenStyle(true);
        return  strategy;
    }

    public InjectionConfig customizeConfigOfXMLPath() {
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        PackageConfig pc = this.packageConfig();
        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig("/templates/mapper.xml.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 + pc.getModuleName()
                if(StringUtils.isEmpty(pc.getModuleName())){
                    return PROJECT_PATH + "/src/main/resources/mapper/" + tableInfo.getXmlName() + StringPool.DOT_XML;
                } else {
                    return PROJECT_PATH + "/src/main/resources/mapper/" + pc.getModuleName() + "/" + tableInfo.getXmlName() + StringPool.DOT_XML;
                }
            }
        });

        cfg.setFileOutConfigList(focList);
        return cfg;
    }

}
