package com.example.service.activiti;

import com.alibaba.druid.sql.parser.Lexer;
import org.activiti.engine.*;
import org.activiti.engine.identity.Group;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class ActivitiService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private RepositoryService repositoryService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 依据流程定义Key启动流程
     *
     * @param processDefinitionKey 流程定义Key
     * @return
     */
    public String startProcessInstanceByKey(String processDefinitionKey) {
        String processInstanceId = runtimeService
                .startProcessInstanceByKey(processDefinitionKey)
                .getProcessInstanceId();
        logger.info("*申请流程启动成功，流程ID：{}", processInstanceId);
        return processInstanceId;
    }

    /**
     * 依据流程定义Key及流程变量启动流程
     *
     * @param processDefinitionKey 流程定义Key
     * @param variables 流程变量
     * @return
     */
    public String startProcessInstanceByKeyWithVariables(String processDefinitionKey, Map<String, Object> variables) {
        String processInstanceId = runtimeService
                .startProcessInstanceByKey(processDefinitionKey, variables)
                .getProcessInstanceId();
        logger.info("*申请流程启动成功，流程ID：{}", processInstanceId);
        return processInstanceId;
    }

    /**
     * 依据任务ID查询任务详情
     *
     * @param taskId 任务ID
     * @return
     */
    public Task queryTaskByTaskId(String taskId) {
        List<Task> list = taskService.createTaskQuery().taskId(taskId).list();
        // list != null 判断是否存在瓶子(即集合对象)
        // list.isEmpty() 判断瓶子中是否有水(即集合元素)
        // 若没有瓶子，直接判断瓶里的水，会报nullException
        // list.add(null)会造成list.isEmpty() 为false,但是list.size()结果为1
        if (list == null || list.isEmpty() || list.size() != 1) {
            return null;
        } else {
            return list.get(0);
        }
    }

    /**
     * 查询用户已领取的任务
     *
     * @param user 用户名
     * @return
     */
    public List<Task> queryReceivedTask(String user) {
        List<Task> tasksOfUser = taskService.createTaskQuery().taskAssignee(user).list();
        return tasksOfUser;
    }

    /**
     * 依据用户ID查询用户已领取任务和所在组待分配任务
     *
     * @param userId
     * @return
     */
    public List<Task> queryTaskByUserId(String userId) {
        // 查出用户所在用户组
        List<Group> groups = identityService.createGroupQuery().groupMember(userId).list();
        List<Task> tasksOfUser = taskService.createTaskQuery().taskCandidateUser(userId).list();
        List<Task> tasksOfGroup;
        if (!groups.isEmpty()) {
            tasksOfGroup = taskService.createTaskQuery().taskCandidateGroup(groups.get(0).getId()).list();
        } else {
            throw new ActivitiException("未查询到用户分组信息");
        }

        // TODO   I don't know how to do this now.
        return null;
    }

    /**
     * 查询分组待分配任务
     *
     * @param groupId 分组ID
     * @return
     */
    public List<Task> queryTaskByCandidateGroup(String groupId) {
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup(groupId).list();
        return tasks;
    }

    /**
     * 查询多个分组待分配任务
     *
     * @param groupIds 分组ID集合
     * @return
     */
    public List<Task> queryTaskByCandidateGroups(List<String> groupIds) {
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroupIn(groupIds).list();
        return tasks;
    }

    /**
     * 认领任务
     *
     * @param taskId 任务ID
     * @param userId 用户ID
     */
    public void claimTask(String taskId, String userId) {
        taskService.claim(taskId, userId);
    }

    /**
     * 完成任务
     *
     * @param taskId 任务ID
     * @param variables 流程变量
     */
    public void completeTask(String taskId, Map<String, Object> variables) {
        taskService.complete(taskId, variables, false);
    }

    /**
     * 查询任务候选组
     *
     * @param task
     * @return
     */
    public IdentityLink queryIdentityLinkByTaskId(Task task) {
        List<IdentityLink> identityLinks = taskService.getIdentityLinksForTask(task.getId());
        return identityLinks.get(0);
    }

    /**
     * 依据任务实例ID查询任务
     *
     * @param processInstancedId
     * @return
     */
    public List<Task> queryTaskByProcessInstancedId(String processInstancedId) {
        return taskService.createTaskQuery().processInstanceId(processInstancedId).list();
    }

    /**
     * 查询所有流程组
     *
     * @return
     */
    public List<Group> queryAllGroups() {
        return  identityService.createGroupQuery().list();
    }

    /**
     * 依据类型查看流程组
     *
     * @param type
     * @return
     */
    public List<Group> queryGroupsByType(String type) {
        return identityService.createGroupQuery().groupType(type).list();
    }


}
