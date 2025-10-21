package com.dashboard.personal.controller;

import com.dashboard.personal.entity.Note;
import com.dashboard.personal.entity.Task;
import com.dashboard.personal.entity.TaskStatus;
import com.dashboard.personal.service.NoteService;
import com.dashboard.personal.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class DashboardController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private NoteService noteService;

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("tasks", taskService.getAllTasks());
        model.addAttribute("pendingTasks", taskService.getTasksByStatus(TaskStatus.PENDING));
        model.addAttribute("inProgressTasks", taskService.getTasksByStatus(TaskStatus.IN_PROGRESS));
        model.addAttribute("completedTasks", taskService.getTasksByStatus(TaskStatus.COMPLETED));
        model.addAttribute("notes", noteService.getAllNotes());
        model.addAttribute("newTask", new Task());
        model.addAttribute("newNote", new Note());
        return "dashboard";
    }

    @PostMapping("/tasks")
    public String addTask(@ModelAttribute Task task) {
        taskService.saveTask(task);
        return "redirect:/";
    }

    @PostMapping("/tasks/{id}/status")
    public String updateTaskStatus(@PathVariable Long id, @RequestParam TaskStatus status) {
        taskService.updateTaskStatus(id, status);
        return "redirect:/";
    }

    @PostMapping("/tasks/{id}/delete")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/";
    }

    @PostMapping("/notes")
    public String addNote(@ModelAttribute Note note) {
        noteService.saveNote(note);
        return "redirect:/";
    }

    @PostMapping("/notes/{id}/delete")
    public String deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);
        return "redirect:/";
    }
}