package com.dashboard.personal.service;

import com.dashboard.personal.entity.Task;
import com.dashboard.personal.entity.TaskStatus;
import com.dashboard.personal.entity.User;
import com.dashboard.personal.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAllByOrderByPriorityDesc();
    }

    public List<Task> getAllTasksByUser(User user) {
        return taskRepository.findByUserOrderByPriorityDesc(user);
    }

    public List<Task> getTasksByStatus(TaskStatus status) {
        return taskRepository.findByStatusOrderByPriorityDesc(status);
    }

    public List<Task> getTasksByUserAndStatus(User user, TaskStatus status) {
        return taskRepository.findByUserAndStatusOrderByPriorityDesc(user, status);
    }

    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public Task updateTaskStatus(Long id, TaskStatus status) {
        Optional<Task> taskOpt = taskRepository.findById(id);
        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            task.setStatus(status);
            return taskRepository.save(task);
        }
        return null;
    }
}