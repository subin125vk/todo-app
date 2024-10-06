package com.todo_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.todo_app.entity.Project;
import com.todo_app.entity.Todo;
import com.todo_app.service.ProjectService;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping
    public String listProjects(Model model) {
        model.addAttribute("projects", projectService.getAllProjects());
        return "projects";
    }

    @GetMapping("/{id}")
    public String viewProject(@PathVariable Long id, Model model) {
        Project project = projectService.getProjectById(id);
        model.addAttribute("project", project);
        return "project-view";
    }

    @PostMapping
    public String createProject(@ModelAttribute Project project) {
        projectService.saveProject(project);
        return "redirect:/projects";
    }

    @PostMapping("/{id}/todos")
    public String addTodoToProject(@PathVariable Long id, 
                                    @RequestParam String description,
                                    @RequestParam String todoDescription) {
        Todo todo = new Todo();
        todo.setDescription(description);
        todo.setTodoDescription(todoDescription);
        projectService.addTodoToProject(id, todo);
        return "redirect:/projects/" + id;
    }

    @PostMapping("/delete/{id}")
    public String deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return "redirect:/projects";
    }

    @PostMapping("/{projectId}/todos/delete/{todoId}")
    public String deleteTodo(@PathVariable Long projectId, @PathVariable Long todoId) {
        projectService.deleteTodoById(todoId);
        return "redirect:/projects/" + projectId;
    }

    @PostMapping("/{projectId}/todos/{todoId}/complete")
    public String markTodoAsComplete(@PathVariable Long projectId, @PathVariable Long todoId) {
        projectService.markTodoAsComplete(projectId, todoId);
        return "redirect:/projects/" + projectId;
    }

    @GetMapping("/{projectId}/todos/{todoId}/edit")
    public String editTodo(@PathVariable Long projectId, @PathVariable Long todoId, Model model) {
        Todo todo = projectService.getTodoById(todoId); 
        model.addAttribute("todo", todo);
        model.addAttribute("projectId", projectId);
        return "edit-todo";
    }

    @PostMapping("/{projectId}/todos/{todoId}/edit")
    public String updateTodo(@PathVariable Long projectId, @PathVariable Long todoId,
                             @RequestParam String description, 
                             @RequestParam String todoDescription) {
        projectService.updateTodoDescription(todoId, description, todoDescription);
        return "redirect:/projects/" + projectId;
    }

    @PostMapping("/{projectId}/edit")
    public String updateProjectTitle(@PathVariable Long projectId, @RequestParam String title) {
        projectService.updateProjectTitle(projectId, title); 
        return "redirect:/projects/" + projectId;
    }

    @PostMapping("/{projectId}/export")
    public String exportProjectAsMarkdown(@PathVariable Long projectId) {
        projectService.exportProjectAsMarkdown(projectId); 
        return "redirect:/projects/" + projectId;
    }
}
