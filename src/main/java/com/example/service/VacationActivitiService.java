package com.example.service;

import java.util.List;
import java.util.Map;

public interface VacationActivitiService {

    /**
     * 启动请假流程
     *
     * @param UID 流水号
     * @param student 学生姓名
     * @param Class 班级
     * @param days 请假天数
     * @return
     */
    String startProcess(String UID, String student, String Class, Integer days);

    /**
     * 启动请假流程
     *
     * @param UID 流水号
     * @param student 学生姓名
     * @param variables 流程变量
     * @return
     */
    String startProcess(String UID, String student, Map<String, Object> variables);

    /**
     * 依据用户查询任务
     *
     * @param user 用户标识
     * @return
     */
    List userTask(String user);

    /**
     * 查询用户已办任务
     *
     * @param user 用户标识
     * @return
     */
    List userFinishedTask(String user);

    /**
     * 查询用户所有任务(待办 + 已办)
     *
     * @param user
     * @return
     */
    List userAllTasks(String user);

    /**
     * 认领任务
     *
     * @param user
     * @param groupId
     */
    void claimTask(String user, String groupId);

    /**
     * 完成任务
     *
     * @param UID
     * @param user
     */
    void completeTask(String UID, String user);

    /**
     * 变更当前任务办理人
     *
     * @param UID
     * @param user
     */
    void changeCurrentTaskAssignee(String UID, String user);

    /**
     * 查询该组待办任务
     *
     * @param groupId
     * @return
     */
    List queryTaskByGroup(String groupId);

    /**
     * 查询流程组
     *
     * @param type
     * @return
     */
    List queryGroups(String type);
}
