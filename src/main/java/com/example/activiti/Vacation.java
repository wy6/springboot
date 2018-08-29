package com.example.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;

/**
 * @Description:
 * @Author: WangY
 * @Date: Created in 2018-08-25 16:25
 * @Modified By：
 */

public class Vacation {

    // 通过调用 classpath 路径下的 activiti.cfg.xml 文件创建流程引擎
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    /**
     * 部署流程定义
     * 流程部署属于仓库服务，需调用 RepositoryService 执行操作
     */
    @Test
    public void deploymentProcessDefinition() {
        Deployment deployment = processEngine.getRepositoryService()  // 调用流程定义和对象部署相关的 service
                .createDeployment()  // 创建一个部署对象
                .name("请假流程")  // 添加部署名称
                .addClasspathResource("processes/vacation.bpmn")  // 从 classpath 路径加载，一次加载一个文件
                .addClasspathResource("processes/vacation.png")
                .deploy();  // 完成部署操作
        System.out.println("**Student.deploymentProcessDefinition.32==> 部署ID：" + deployment.getId() + ", 部署名称：" + deployment.getName());
    }

    /**
     * 启动流程实例
     * 启动流程数据运行时服务，需调用 RuntimeServcie 执行操作
     */
    @Test
    public void startProcessInstance() {
        // 流程定义的 key （key 对应bpmn文件中，画图时定义的整图的Id）
        String processDefinitionKey = "vacation";
        ProcessInstance processInstance = processEngine.getRuntimeService()  //调用流程运行和执行对象相关的 service
                .startProcessInstanceByKey(processDefinitionKey);  // 使用流程定义的 key 启动流程实例，自动获取流程定义的最新版本
        System.out.println("**Student.startProcessInstance.46==> 流程实例ID：" + processInstance.getId() + ", 流程定义ID：" + processInstance.getProcessDefinitionId());
    }
}
