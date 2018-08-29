#### Activiti  
----
1. 获取流程引擎
* 数据库连接配置信息
```xml
<?xml version="1.0" encoding="UTF-8"?>

<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:context="http://www.springframework.org/schema/context" xmlns:tx="http://www.springframework.org/schema/tx"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-2.5.xsd
       http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-3.0.xsd">
       
    <!-- 配置 ProcessEngineConfiguration  -->
    <bean id="processEngineConfiguration" class="org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration">
    
        <!-- 配置数据库连接 -->
        <property name="jdbcDriver" value="com.mysql.jdbc.Driver"></property>
        <property name="jdbcUrl" value="jdbc:mysql://localhost:3306/activitiDB?createDatabaseIfNotExist=true&amp;useSSL=false&amp;useUnicode=true&amp;characterEncoding=utf8"></property>
        <property name="jdbcUsername" value="root"></property>
        <property name="jdbcPassword" value="123456"></property>
        
        <!-- * DB_SCHEMA_UPDATE_FALSE = "false" 不自动创建表结构
         * DB_SCHEMA_UPDATE_TRUE = "true" 若表不存在，自动创建表
         * DB_SCHEMA_UPDATE_CTEATE_DROP = "create-drop" 先删除表再重新创建表结构 -->
        <!-- 配置创建表策略 :没有表时，自动创建 -->
        <property name="databaseSchemaUpdate" value="true"></property>
    </bean>
</beans>
```
* 加载配置信息创建流程引擎对象  
````java
// 默认从 Classpath路径加载 activiti.cfg.xml 配置文件
ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
````

2. 部署流程定义  
* 部署方式一：单文件部署  
````java
public void deploy() {
    Deployment deployment = repositoryService.createDeployment() // 创建一个部署构建器
              .addClasspathResource("processes/vacation.bpmn") // 加载流程定义规则
              .addClasspathResource("processes/vacation.png")
              .name("请假流程") // 设置流程部署名称
              .category("教务管理") // 设置部署类别
              .deploy();
}
````
* 部署方式二：zip压缩文件部署  
````java
public void deployZip() {
    InputStream in = this.getClass().getClassLoader().getResourceAsStream("processes/vacation.zip");
    ZipInputStream zipInputStream = new ZipInputStream(in);
    Deployment deployment = processEngine.getRepositoryService()
              .createDeployment()
              .name("请假流程") // 配置部署规则的显示别名
              .addZipInputStream(zipInputStream)
              .deploy();
}
````
* 查询流程定义  
````java
public void queryProcessDefinition() {
    List<ProcessDefinition> processDefinitionList = processEngine.getRepositoryService()
              .createProcessDefinitionQuery()
           // .processDefinitionCategory("教务管理")  // 筛选条件
           // .processDefinitionCategoryLike("教务")
           // .processDefinitionId("2501")
           // .processDefinitionName("请假")
              .processDefinitionKey("vacation")
              .orderByProcessDefinitionVersion().asc()
           // .count() // 结果集数量
           // .listPage(firstResult, maxResults) // 分页查询
           // .singleResult(); // 唯一结果
              .list(); // 总的结果集数量
}
````
* 查询最新版本的流程定义  
````java
public void queryAllLatestVersions() {
    List<ProcessDefinition> list = processEngine.getRepositoryService()
                 .createProcessDefinitionQuery()
                 .orderByProcessDefinitionVersion().asc()
                 .list();
            Map<String, ProcessDefinition> map = new LinkedHashMap<>();
            for (ProcessDefinition pd : list) {
                map.put(pd.getKey(), pd);
            }
}
````
* 删除流程定义  
````java
public void deleteDeployment() {
    String deploymentId = "";
        processEngine.getRepositoryService()
             .deleteDeployment(deploymentId); // 普通删除，若当前规则下有正在执行的流程，抛异常
          // .deleteDeployment(deploymentId, true); // 级联删除，删除与当前规则相关的所有信息，正在执行及历史信息
}
````
3. 执行流程  
* 开始流程 (*设置流程实例中的流程变量*)  
````java
public void startProcess() {
        // 指定执行已部署的工作流程，key为流程定义的ID
        String processDefikey = "vacation";
        RuntimeService runtimeService = processEngine.getRuntimeService();
        Map<String, Object> variables = new HashMap<>();
        variables.put("student", "小明");
        variables.put("day", "2");
        variables.put("grade", "13");
        variables.put("reason", "家中有事");
        ProcessInstance pi = runtimeService.startProcessInstanceByKey(processDefikey, variables);
}
````
* 查询指定办理人的任务列表  
````java
public void queryTask() {
        // 指定任务办理人
        String assignee = "小明";
        
        List<Task> list = processEngine.getTaskService()  // 获取流程引擎的任务服务
                .createTaskQuery()  // 创建任务查询对象
                .taskAssignee(assignee)  // 使用查询对象获取办理人的任务列表
                .list();
}
````
* 办理任务  
````java
public void completeTask() {
        String taskId = "2509";
        // 设置下一环节的任务执行人
        Map<String, Object> variables = new HashMap<>();
        variables.put("teacher", "宋老师");
        // 完成该环节任务
        processEngine.getTaskService().complete(taskId, variables);
}
````
* 查询历史任务  
```java
public void queryHistoryTask() {
        String taskAssignee = "小明";
        List<HistoricTaskInstance> list = processEngine.getHistoryService()
                .createHistoricTaskInstanceQuery() // 创建历史任务查询
                .taskAssignee(taskAssignee) // 指定任务办理人
                .list();
}
```
* 获取历史流程变量  
````java
public void queryTeacherHistoryTask() {
        String processInstanceId = "2501";
        List<HistoricVariableInstance> list = processEngine.getHistoryService()
                .createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId)
                .list();
}
````
* 查询流程实例状态  
```java
public void queryProcessState() {

        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        String processInstanceId = "2501";
        ProcessInstance pi = processEngine.getRuntimeService()
                .createProcessInstanceQuery() // 创建流程实例查询，查询正在执行的流程实例
                .processInstanceId(processInstanceId) // 按流程实例 ID 查询
                .singleResult(); // 返回唯一结果集

        if (pi != null) {
            System.out.println("**ActivitiTest.queryProcessState.348==> 当前流程执行到：" + pi.getActivityId());
        } else {
            System.out.println("**ActivitiTest.queryProcessState.350==> " + "流程已结束！");
        }
}
```