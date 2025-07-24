package com.crmw.CRM_BE.service;

import com.crmw.CRM_BE.entity.AssignedTask;
import com.crmw.CRM_BE.entity.Users;
import com.crmw.CRM_BE.enums.TasksEnums;
import com.crmw.CRM_BE.enums.UserTypes;
import com.crmw.CRM_BE.repository.IAssignedTaskRepository;
import com.crmw.CRM_BE.repository.IUsersRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class AssignedTaskService {

    private final IAssignedTaskRepository assignedTaskRepository;
    private final IUsersRepository usersRepository;

    public AssignedTaskService(IAssignedTaskRepository assignedTaskRepository, IUsersRepository usersRepository) {
        this.assignedTaskRepository = assignedTaskRepository;
        this.usersRepository = usersRepository;
    }

    public List<AssignedTask> getAllTasks() {
        return assignedTaskRepository.findAll();
    }


    public Optional<AssignedTask> getTaskById(Long id) {
        return assignedTaskRepository.findById(id);
    }

    public AssignedTask createTask(AssignedTask task) {
        task.setStatus(TasksEnums.Status.PENDING.toString());
        return assignedTaskRepository.save(task);
    }

    public AssignedTask updateTask(Long id, AssignedTask updatedTask) {
        AssignedTask existingTask = assignedTaskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));

        if (updatedTask.getTitle() != null) {
            existingTask.setTitle(updatedTask.getTitle());
        }
        if (updatedTask.getDescription() != null) {
            existingTask.setDescription(updatedTask.getDescription());
        }
        if (updatedTask.getPriority() != null) {
            existingTask.setPriority(updatedTask.getPriority());
        }
        if (updatedTask.getStatus() != null) {
            existingTask.setStatus(updatedTask.getStatus());
        }
        if (updatedTask.getDueDate() != null) {
            existingTask.setDueDate(updatedTask.getDueDate());
        }
        if (updatedTask.getCategory() != null) {
            existingTask.setCategory(updatedTask.getCategory());
        }

        return assignedTaskRepository.save(existingTask);
    }


    public AssignedTask updateTaskStatus(Long id, String newStatus) {
        AssignedTask task = assignedTaskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));
        task.setStatus(newStatus);
        return assignedTaskRepository.save(task);
    }

    public AssignedTask createTask(AssignedTask task, Integer assignedById, Integer assignedToId) {
        Users assignedByUser = usersRepository.findById(assignedById)
                .orElseThrow(() -> new RuntimeException("Assigned By user not found with id: " + assignedById));

        Users assignedToUser = usersRepository.findById(assignedToId)
                .orElseThrow(() -> new RuntimeException("Assigned To user not found with id: " + assignedToId));

        task.setAssignedById(assignedByUser.getId());
        task.setAssignedByName(assignedByUser.getUsername());

        task.setAssignedToId(assignedToUser.getId());
        task.setAssignedToName(assignedToUser.getUsername());

        return assignedTaskRepository.save(task);
    }

    public void deleteTask(Long id) {
        assignedTaskRepository.deleteById(id);
    }

    public List<AssignedTask> getTasksByStatusandUserId(String status, Long userId) {
        return assignedTaskRepository.findByStatusAndAssignedToId(status, userId);
    }

    public List<AssignedTask> getTasksByUserId(Long userId) {
        return assignedTaskRepository.findByAssignedToId(userId);
    }

    public Page<AssignedTask> searchTasks(String query,String status, boolean myTasks, Pageable pageable) {

        Users currentUser = getCurrentUser();

        if (status != null && !status.isEmpty()) {
            try {
                TasksEnums.Status.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status value: " + status);
            }
        } else {
            status = null;
        }

        if (myTasks) {
            return assignedTaskRepository.searchMyTasks(query, status, currentUser.getUsername(), pageable);
        } else {
            if (!currentUser.getRole().equals(UserTypes.ADMIN.toString())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Only admins can access all tasks.");
            }
            return assignedTaskRepository.searchAllTasks(query, status, pageable);
        }
    }

    public Users getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return usersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
