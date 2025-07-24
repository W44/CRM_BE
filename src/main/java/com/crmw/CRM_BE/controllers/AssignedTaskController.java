package com.crmw.CRM_BE.controllers;

import com.crmw.CRM_BE.entity.AssignedTask;
import com.crmw.CRM_BE.service.AssignedTaskService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@CrossOrigin(origins = "*") // Adjust CORS as needed for frontend deployment
public class AssignedTaskController {

    @Autowired
    private AssignedTaskService assignedTaskService;

    @GetMapping
    public ResponseEntity<List<AssignedTask>> getAllTasks() {
        List<AssignedTask> tasks = assignedTaskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssignedTask> getTaskById(@PathVariable Long id) {
        return assignedTaskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));
    }

    @PostMapping
    public ResponseEntity<AssignedTask> createTask(@RequestBody AssignedTask task) {
        AssignedTask createdTask = assignedTaskService.createTask(task);
        return ResponseEntity.ok(createdTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssignedTask> updateTask(@PathVariable Long id, @RequestBody AssignedTask task) {
        AssignedTask updatedTask = assignedTaskService.updateTask(id, task);
        return ResponseEntity.ok(updatedTask);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<AssignedTask> updateTaskStatus(@PathVariable Long id, @RequestParam String status) {
        AssignedTask updatedTask = assignedTaskService.updateTaskStatus(id, status);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        assignedTaskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<AssignedTask>> searchTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "false") boolean myTasks) {

        Sort sortOrder = Sort.by(Sort.Direction.fromString(sort[1]), sort[0]);
        Pageable pageable = PageRequest.of(page, size, sortOrder);

        Page<AssignedTask> tasks = assignedTaskService.searchTasks(query, status, myTasks, pageable);

        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/status/{status}/user/{userId}")
    public ResponseEntity<List<AssignedTask>> getTasksByStatusAndUserId(
            @PathVariable String status,
            @PathVariable Long userId) {

        List<AssignedTask> tasks = assignedTaskService.getTasksByStatusandUserId(status, userId);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AssignedTask>> getTasksByUserId(
            @PathVariable Long userId) {

        List<AssignedTask> tasks = assignedTaskService.getTasksByUserId(userId);
        return ResponseEntity.ok(tasks);
    }
}

