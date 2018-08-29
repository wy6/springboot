package com.example.activiti;

import jdk.internal.util.xml.impl.Input;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 * @Description:
 * @Author: WangY
 * @Date: Created in 2018-08-25 15:26
 * @Modified By：
 */

public class ActivitiTest {

    /**
     * ######################### 创建 Activiti 表结构 #######################
     */

    /**
     * 使用代码创建工作流需要的23张表结构
     */
    @Test
    public void createTable() {
        // 创建流程引擎配置信息对象
        ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();

        // 配置数据库连接信息
        processEngineConfiguration.setJdbcDriver("com.mysql.jdbc.Driver");
        processEngineConfiguration.setJdbcUrl("jdbc:mysql://localhost:3306/activitiDB?useUnicode=true&characterEncoding=utf8");
        processEngineConfiguration.setJdbcUsername("root");
        processEngineConfiguration.setJdbcPassword("123456");

        /**
         * 配置创建表的规则
         * DB_SCHEMA_UPDATE_FALSE = "false" 不自动创建表结构
         * DB_SCHEMA_UPDATE_TRUE = "true" 若表不存在，自动创建表
         * DB_SCHEMA_UPDATE_CTEATE_DROP = "create-drop" 先删除表再重新创建表结构
         */
        processEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        // 由上面流程引擎配置对象的属性信息生成工作流核心对象-ProcessEngine对象
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        System.out.println("ActivitiTest.createTable.39:" + processEngine.toString());
    }

    /**
     * 使用配置文件创建表结构
     */
    @Test
    public void createTable2() {
        // 从配置文件加载配置信息
        ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        System.out.println("**ActivitiTest.createTable2.49 ==>" + processEngine);
    }

    /**
     * ######################### 流程定义管理CRUD #########################
     */

    /**
     * 单文件部署流程定义
     */
    @Test
    public void deploy() {
        // 从默认配置文件 activiti.cfg.xml 文件加载配置信息
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment() // 创建一个部署构建器
                .addClasspathResource("processes/vacation.bpmn") // 加载流程定义规则
                .addClasspathResource("processes/vacation.png")
                .name("请假流程") // 设置流程部署名称
                .category("教务管理") // 设置部署类别
                .deploy();
        
        System.out.println("**ActivitiTest.deploy.69==> 流程部署ID：" + deployment.getId());
        System.out.println("**ActivitiTest.deploy.70==> 流程部署名称：" + deployment.getName());

        // act_ge_bytearray ---> 文件类型及文件路径
        // act_ge_property ---> 添加了两条流程定义规则资源记录（包含资源文件路径）
        // act_re_deployment ---> 添加了一条部署记录（流程部署别名、类别）
        // act_re_procdef ---> 添加了一条资源记录（流程定义ID，版本号、资源路径、流程名称、流程KEY、部署ID等信息）
    }

    /**
     * zip 压缩文件部署流程定义
     */
    public void deployZip() {

        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        InputStream in = this.getClass().getClassLoader().getResourceAsStream("processes/vacation.zip");
        ZipInputStream zipInputStream = new ZipInputStream(in);
        Deployment deployment = processEngine.getRepositoryService()
                .createDeployment()
                .name("请假流程") // 配置部署规则的显示别名
                .addZipInputStream(zipInputStream)
                .deploy();
        System.out.println("**ActivitiTest.deployZip.108==> ID:" + deployment.getId());
        System.out.println("**ActivitiTest.deployZip.109==> Name:" + deployment.getName());
    }

    /**
     * 查询流程定义
     */
    @Test
    public void queryProcessDefinition() {

        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        List<ProcessDefinition> processDefinitionList = processEngine.getRepositoryService()
                .createProcessDefinitionQuery()
//                .processDefinitionCategory("教务管理")
//                .processDefinitionCategoryLike("教务")
//                .processDefinitionId("2501")
//                .processDefinitionName("请假")
                .processDefinitionKey("vacation")
                .orderByProcessDefinitionVersion().asc()
//                .count() // 结果集数量
//                .listPage(firstResult, maxResults) // 分页查询
//                .singleResult(); // 唯一结果
                .list(); // 总的结果集数量

        for (ProcessDefinition pd : processDefinitionList) {
            System.out.println("**ActivitiTest.queryProcessDefinition.103==> " + pd.getId());
            System.out.println("**ActivitiTest.queryProcessDefinition.104==> " + pd.getName());
            System.out.println("**ActivitiTest.queryProcessDefinition.105==> " + pd.getKey());
            System.out.println("**ActivitiTest.queryProcessDefinition.106==> " + pd.getVersion());
        }
    }

    /**
     * 删除流程定义
     */
    @Test
    public void deleteDeployment() {

        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        String deploymentId = "";
        processEngine.getRepositoryService()
                .deleteDeployment(deploymentId); // 普通删除，若当前规则下有正在执行的流程，抛异常
//                .deleteDeployment(deploymentId, true); // 级联删除，删除与当前规则相关的所有信息，正在执行及历史信息
    }

    /**
     * 查看流程定义的流程图
     */
    @Test
    public void showView() {

        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        String deploymentId = "";
        List<String> names = processEngine.getRepositoryService()
                .getDeploymentResourceNames(deploymentId);
        String imagesName = "";
        for (String name : names) {
            System.out.println("**ActivitiTest.showView.142==> name:" + name);
            if (name.indexOf(".png") >= 0) {
                imagesName = name;
            }
        }
        System.out.println("**ActivitiTest.showView.147==> imageName:" + imagesName);
        if (imagesName != null) {
            File f = new File("/imageName");
            InputStream in = processEngine.getRepositoryService()
                    .getResourceAsStream(deploymentId, imagesName);
            // FileUtils.copyInputStreamToFile(in, f);
        }
    }

    /**
     * 查询所有最新版本的流程定义
     */
    @Test
    public void queryAllLatestVersions() {

        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        List<ProcessDefinition> list = processEngine.getRepositoryService()
                .createProcessDefinitionQuery()
                .orderByProcessDefinitionVersion().asc()
                .list();
        Map<String, ProcessDefinition> map = new LinkedHashMap<>();
        for (ProcessDefinition pd : list) {
            map.put(pd.getKey(), pd);
        }

        for (ProcessDefinition pd : map.values()) {
            System.out.println("**ActivitiTest.queryAllLatestVersions.179==> ID:" + pd.getId());
            System.out.println("**ActivitiTest.queryAllLatestVersions.180==> name:" + pd.getName());
            System.out.println("**ActivitiTest.queryAllLatestVersions.181==> key:" + pd.getKey());
        }
    }

    /**
     * 删除流程定义，删除 key 的所有版本
     */
    public void deleteByKey() {

        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        String processDefinitionKey = "";
        List<ProcessDefinition> list = processEngine.getRepositoryService()
                .createProcessDefinitionQuery()
                .processDefinitionKey(processDefinitionKey) // 指定流程定义的 key 进行查询
                .list();

        for (ProcessDefinition pd : list) {
            processEngine.getRepositoryService()
                    .deleteDeployment(pd.getDeploymentId(), true);
        }
        System.out.println("**ActivitiTest.deleteByKey.202==> " + "删除成功！");
    }

    /**
     *
     * ***小结
     * 流程定义中涉及到的 Activiti 表
     * act_re_deployment -----> 部署对象表
     * act_re_procdef -----> 流程定义表
     * act_ge_bytearray -----> 资源文件表
     * act_ge_property -----> 主键生成策略表
     *
     */

    /**
     * ####################### 流程实例的执行 #########################
     */

    /**
     * 启动流程，并设置流程变量（如申请人、请假天数、班级、原因等）
     */
    @Test
    public void startProcess() {

        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        // 指定执行已部署的工作流程，key为流程定义的ID
        String processDefikey = "vacation";

        RuntimeService runtimeService = processEngine.getRuntimeService();
        Map<String, Object> variables = new HashMap<>();
        variables.put("student", "小明");
        variables.put("day", "2");
        variables.put("grade", "13");
        variables.put("reason", "家中有事");
        ProcessInstance pi = runtimeService.startProcessInstanceByKey(processDefikey, variables);
        
        System.out.println("**ActivitiTest.startProcess.90==> 流程实例ID：" + pi.getId());
        System.out.println("**ActivitiTest.startProcess.91==> 流程定义ID：" + pi.getProcessDefinitionId());

        // act_hi_actinst ---> 流程实例执行记录（任务ID、任务办理人、开始/结束时间）
        // act_hi_identitylink ---> 任务办理人记录及所在流程实例ID
        // act_hi_procinst ---> 历史指令记录
        // act_hi_taskinst ---> 历史任务指令记录
        // act_hi_varinst ---> 历史流程变量记录
        // act_re_execution ---> 当前执行中的任务信息
        // act_ru_identitylink ----> 当前执行中的流程实例中的已知任务办理人
        // act_ru_task ---> 当前执行中的任务中的代办理任务节点信息
        // act_ru_variable ---> 当前执行中的任务流程变量

    }

    /**
     * 查询指定办理人的任务列表
     */
    @Test
    public void queryTask() {

        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        // 指定任务办理人
        String assignee = "小明";
        // 获取流程引擎的任务服务
        TaskService taskService = processEngine.getTaskService();
        // 创建任务查询对象
        TaskQuery taskQuery = taskService.createTaskQuery();
        // 使用查询对象获取办理人的任务列表
        List<Task> list = taskQuery.taskAssignee(assignee)
                .list();

        if (list != null && list.size() > 0) {
            for (Task task : list) {
                System.out.println("**ActivitiTest.queryTask.121==> 任务的办理人：" + task.getAssignee());
                System.out.println("**ActivitiTest.queryTask.122==> 任务ID：" + task.getId());
                System.out.println("**ActivitiTest.queryTask.123==> 任务名称：" + task.getName());
            }
        }
    }

    /**
     * 办理任务
     */
    @Test
    public void completeTask() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        String taskId = "2509";
        // 设置下一环节的任务执行人
        Map<String, Object> variables = new HashMap<>();
        variables.put("teacher", "宋老师");
        // 完成该环节任务
        processEngine.getTaskService().complete(taskId, variables);
        
        System.out.println("**ActivitiTest.completeTask.142==> " + "请假申请提交成功！交与宋老师审批。");

        // act_ru_task ---> 当前执行中的任务中的代办理任务节点信息
        // act_ru_variable ---> 当前执行中的任务流程变量

    }

    /**
     * 查询指定办理人的任务列表
     */
    @Test
    public void queryTeacherTask() {

        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        // 指定任务办理人
        String assignee = "宋老师";
        // 获取流程引擎的任务服务创建任务查询实例，指定办理人获取任务列表
        List<Task> list = processEngine.getTaskService()
                .createTaskQuery()
                .taskAssignee(assignee)
                .list();

        if (list != null && list.size() > 0) {
            for (Task task : list) {
                System.out.println("**ActivitiTest.queryTask.165==> 任务的办理人：" + task.getAssignee());
                System.out.println("**ActivitiTest.queryTask.166==> 任务ID：" + task.getId());
                System.out.println("**ActivitiTest.queryTask.167==> 任务名称：" + task.getName());
            }
        }
    }

    /**
     * 获取流程变量（宋老师查看详情）
     */
    @Test
    public void getVariables() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        String assignee = "宋老师";
        String processInstanceId = "2501";
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService
                .createTaskQuery()
                .taskAssignee(assignee)
                .processInstanceId(processInstanceId)
                .singleResult();

        if (task != null) {
            String student = (String) taskService.getVariable(task.getId(), "student");
            String reason = (String) taskService.getVariable(task.getId(), "reason");
            String grade = (String) taskService.getVariable(task.getId(), "grade");
            String day = (String) taskService.getVariable(task.getId(), "day");
            System.out.println("**ActivitiTest.getVariables.352==> 当前任务执行人：" + assignee);
            System.out.println("**ActivitiTest.getVariables.355==> 请假人：" + student);
            System.out.println("**ActivitiTest.getVariables.353==> 请假原因：" + reason);
            System.out.println("**ActivitiTest.getVariables.354==> 所在班级：" + grade);
            System.out.println("**ActivitiTest.getVariables.355==> 请假天数：" + day);
        }
    }

    @Test
    public void completeTeacherTask() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        String taskId = "5003";
        Map<String, Object> variables = new HashMap<>();
        variables.put("dean", "李主任");
        variables.put("status", 2);

        TaskService taskService = processEngine.getTaskService();
        taskService.setVariableLocal(taskId, "opinion", "情况属实，我同意！");
        taskService.complete(taskId, variables);
        System.out.println("**ActivitiTest.completeTeacherTask.393==> " + "宋老师审批完成！");

    }

    /**
     * 查询历史任务
     */
    @Test
    public void queryHistoryTask() {

        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        String taskAssignee = "小明";
        List<HistoricTaskInstance> list = processEngine.getHistoryService()
                .createHistoricTaskInstanceQuery() // 创建历史任务查询
                .taskAssignee(taskAssignee)
                .list();
        if (list != null && list.size() > 0) {
            for (HistoricTaskInstance task : list) {
                System.out.println("**ActivitiTest.queryHistoryTask.349==> 任务ID：" + task.getId());
                System.out.println("**ActivitiTest.queryHistoryTask.350==> 流程实例ID：" + task.getProcessInstanceId());
                System.out.println("**ActivitiTest.queryHistoryTask.351==> 任务办理人：" + task.getAssignee());
                System.out.println("**ActivitiTest.queryHistoryTask.415==> " + task.getProcessVariables());
            }
        }
    }

    /**
     * 获取历史流程变量(宋老师查询已办理的任务列表)
     */
    @Test
    public void queryTeacherHistoryTask() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        String taskAssignee = "宋老师";
        String processInstanceId = "2501";
        List<HistoricVariableInstance> list = processEngine.getHistoryService()
                .createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId)
                .list();

        if (list != null && list.size() > 0) {
            for (HistoricVariableInstance hvi : list) {
                System.out.println("**ActivitiTest.queryTeacherHistoryTask.428==> 任务ID：" + hvi.getProcessInstanceId());
                System.out.println("**ActivitiTest.queryTeacherHistoryTask.437==> " + hvi.getVariableName() + ":" + hvi.getValue());
            }
        }
    }

    @Test
    public void queryBeanTask() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        String taskAssignee = "李主任";
        List<Task> list = processEngine.getTaskService()
                .createTaskQuery()
                .taskAssignee(taskAssignee)
                .list();
        if (list != null && list.size() > 0) {
            TaskService taskService = processEngine.getTaskService();
            for (Task task : list) {
                System.out.println("**ActivitiTest.queryBeanTask.448==> " + task.getId());
                System.out.println("**ActivitiTest.queryBeanTask.449==> " + task.getProcessInstanceId());
                System.out.println("**ActivitiTest.queryBeanTask.451==> " + taskService.getVariables(task.getId()));
            }
        }
    }

    @Test
    public void completeBeanTask() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        String taskId = "7506";
        Map<String, Object> variables = new HashMap<>();
        variables.put("headmaster", "王校长");
        variables.put("status", 2);
        TaskService taskService = processEngine.getTaskService();
        taskService.setVariableLocal(taskId, "opinion", "年级主任审批通过！");
        taskService.complete(taskId, variables);
        System.out.println("**ActivitiTest.completeBeanTask.472==> " + "年级主任审批通过！");
    }

    @Test
    public void queryHistoryBeanTask() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        String assignee = "李主任";
        String processInstanceId = "7506";
        List<HistoricTaskInstance> list = processEngine.getHistoryService()
                .createHistoricTaskInstanceQuery()
                .taskAssignee(assignee)
                .list();
        if (list != null && list.size() > 0) {
            for (HistoricTaskInstance historicTaskInstance : list) {
                System.out.println("**ActivitiTest.queryHistoryBeanTask.485==> " + historicTaskInstance.getId());
                System.out.println("**ActivitiTest.queryHistoryBeanTask.486==> " + historicTaskInstance.getAssignee());
                System.out.println("**ActivitiTest.queryHistoryBeanTask.487==> " + historicTaskInstance.getProcessInstanceId());
                List<HistoricVariableInstance> list1 = processEngine.getHistoryService()
                        .createHistoricVariableInstanceQuery()
                        .processInstanceId(historicTaskInstance.getProcessInstanceId())
                        .list();
                if (list1 != null && list1.size() > 0) {
                    for (HistoricVariableInstance hisv : list1) {
                        System.out.println("**ActivitiTest.queryHistoryBeanTask.495==> " + hisv.getVariableName() + ", " + hisv.getValue());
                    }
                }
            }
        }
    }

    @Test
    public void queryHeadmasterTask() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        String assignee = "王校长";
        List<Task> list = processEngine.getTaskService()
                .createTaskQuery()
                .taskAssignee(assignee)
                .list();
        if (list != null && list.size() > 0) {
            for (Task task : list) {
                System.out.println("**ActivitiTest.queryHeadmasterTask.510==> " + task.getId());
                System.out.println("**ActivitiTest.queryHeadmasterTask.511==> " + task.getProcessInstanceId());
                System.out.println("**ActivitiTest.queryHeadmasterTask.512==> " + task.getProcessVariables());
            }
        }
    }

    @Test
    public void completeHeadmasterTask() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        String assignee = "王校长";
        String taskId = "10005";
        Map<String, Object> variables = new HashMap<>();
        variables.put("status", 6);
        TaskService taskService = processEngine.getTaskService();
        taskService.setVariableLocal(taskId, "opinion", "校长审批通过");
        taskService.complete(taskId, variables);
    }

    @Test
    public void studentBack() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        String assignee = "小明";
        String taskId = "15003";
        processEngine.getTaskService()
                .complete(taskId);
    }

    /**
     * 查询历史流程实例
     */
    @Test
    public void queryHistoryProcessInstance() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        String processInstanceId = "2501";
        HistoricProcessInstance hpi = processEngine.getHistoryService()
                .createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        System.out.println("**ActivitiTest.queryHistoryProcessInstance.369==> 流程定义ID：" + hpi.getProcessDefinitionId());
        System.out.println("**ActivitiTest.queryHistoryProcessInstance.371==> 流程实例ID：" + hpi.getId());
    }

    /**
     * 查看流程状态（判断流程是执行还是结束）
     */
    @Test
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

    /**
     * ****小结
     * act_ru_execution ----> 正在执行的信息
     * act_hi_procinst ----> 已经执行完的历史流程实例信息
     * act_hi_actinst ----> 存放历史所有完成的活动
     *
     * act_ru_task -----> 正在执行的任务信息
     * act_hi_taskinst ----> 已经执行完的历史任务信息
     */

    /**
     * #######################
     */

}
