package com.dashboard.personal.repository;

import com.dashboard.personal.entity.Task;
import com.dashboard.personal.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatusOrderByPriorityDesc(TaskStatus status);
    List<Task> findAllByOrderByPriorityDesc();
}