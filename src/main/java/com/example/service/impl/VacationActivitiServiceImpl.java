package com.example.service.impl;

import com.example.service.VacationActivitiService;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: WangY
 * @Date: Created in 2018-08-08 9:10
 * @Modified By：
 */

public class VacationActivitiServiceImpl implements VacationActivitiService {

    @Override
    public String startProcess(String UID, String student, String Class, Integer days) {
        return null;
    }

    @Override
    public String startProcess(String UID, String student, Map<String, Object> variables) {
        return null;
    }

    @Override
    public List userTask(String user) {
        return null;
    }

    @Override
    public List userFinishedTask(String user) {
        return null;
    }

    @Override
    public List userAllTasks(String user) {
        return null;
    }

    @Override
    public void claimTask(String user, String groupId) {

    }

    @Override
    public void completeTask(String UID, String user) {

    }

    @Override
    public void changeCurrentTaskAssignee(String UID, String user) {

    }

    @Override
    public List queryTaskByGroup(String groupId) {
        return null;
    }

    @Override
    public List queryGroups(String type) {
        return null;
    }
}
