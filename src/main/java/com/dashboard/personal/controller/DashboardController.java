package com.dashboard.personal.controller;

import com.dashboard.personal.entity.Note;
import com.dashboard.personal.entity.Task;
import com.dashboard.personal.entity.TaskStatus;
import com.dashboard.personal.entity.User;
import com.dashboard.personal.service.NoteService;
import com.dashboard.personal.service.TaskService;
import com.dashboard.personal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class DashboardController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private NoteService noteService;

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        try {
            // Check if username already exists
            if (userService.existsByUsername(user.getUsername())) {
                model.addAttribute("error", "Username already exists");
                return "register";
            }

            // Check if email already exists
            if (userService.existsByEmail(user.getEmail())) {
                model.addAttribute("error", "Email already exists");
                return "register";
            }

            // Validate password length
            if (user.getPassword().length() < 6) {
                model.addAttribute("error", "Password must be at least 6 characters");
                return "register";
            }

            userService.saveUser(user);
            return "redirect:/login?success";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "register";
        }
    }

    @GetMapping("/")
    public String dashboard(Model model, Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName()).orElseThrow();
        model.addAttribute("tasks", taskService.getAllTasksByUser(currentUser));
        model.addAttribute("pendingTasks", taskService.getTasksByUserAndStatus(currentUser, TaskStatus.PENDING));
        model.addAttribute("inProgressTasks", taskService.getTasksByUserAndStatus(currentUser, TaskStatus.IN_PROGRESS));
        model.addAttribute("completedTasks", taskService.getTasksByUserAndStatus(currentUser, TaskStatus.COMPLETED));
        model.addAttribute("notes", noteService.getAllNotesByUser(currentUser));
        model.addAttribute("newTask", new Task());
        model.addAttribute("newNote", new Note());
        model.addAttribute("currentUser", currentUser);
        return "dashboard";
    }

    @PostMapping("/tasks")
    public String addTask(@ModelAttribute Task task, Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName()).orElseThrow();
        task.setUser(currentUser);
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
    public String addNote(@ModelAttribute Note note, Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName()).orElseThrow();
        note.setUser(currentUser);
        noteService.saveNote(note);
        return "redirect:/";
    }

    @PostMapping("/notes/{id}/delete")
    public String deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);
        return "redirect:/";
    }
}