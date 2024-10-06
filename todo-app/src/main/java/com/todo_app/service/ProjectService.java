package com.todo_app.service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.todo_app.entity.Project;
import com.todo_app.entity.Todo;
import com.todo_app.repository.ProjectRepository;
import com.todo_app.repository.TodoRepository;

@Service
public class ProjectService {

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private TodoRepository todoRepository;

	public List<Project> getAllProjects() {
		return projectRepository.findAll();
	}

	public Project getProjectById(Long id) {
		return projectRepository.findById(id).orElse(null);
	}

	public Project saveProject(Project project) {
		return projectRepository.save(project);
	}

	public void deleteProject(Long id) {
		projectRepository.deleteById(id);
	}

	public Todo addTodoToProject(Long projectId, Todo todo) {
		Project project = projectRepository.findById(projectId).orElse(null);
		if (project != null) {
			todo.setProject(project); 
			return todoRepository.save(todo); 
		}
		return null; 
	}

	public void deleteTodoById(Long id) {
		todoRepository.deleteById(id);
	}

	public void markTodoAsComplete(Long projectId, Long todoId) {
		Project project = projectRepository.findById(projectId).orElse(null);
		if (project != null) {
			for (Todo todo : project.getTodos()) {
				if (todo.getId().equals(todoId)) {
					todo.setCompleted(true); 
					todo.setUpdatedDate(LocalDateTime.now()); 
					todoRepository.save(todo); 
					break;
				}
			}
		}
	}

	public void exportProjectAsMarkdown(Long projectId) {
		Project project = getProjectById(projectId);
		if (project != null) {
			String markdownContent = generateMarkdownForProject(project);
			saveMarkdownToFile(markdownContent, project.getTitle());
		}
	}

	private String generateMarkdownForProject(Project project) {
		StringBuilder markdown = new StringBuilder();

		markdown.append("##Project: ").append(project.getTitle()).append("\n\n");

		long completedTodos = 0;
		long totalTodos = project.getTodos().size();

		for (Todo todo : project.getTodos()) {
			if (todo.getCompleted()) {
				completedTodos++;
			}
		}

		markdown.append("##Summary: ").append(completedTodos).append(" / ").append(totalTodos)
				.append(" todos completed.\n\n");

		markdown.append("##Pending Todos\n");
		for (Todo todo : project.getTodos()) {
			if (!todo.getCompleted()) {
				markdown.append("- [ ] ").append(todo.getDescription()).append("\n");
			}
		}

		markdown.append("\n##Completed Todos\n");
		for (Todo todo : project.getTodos()) {
			if (todo.getCompleted()) {
				markdown.append("- [x] ").append(todo.getDescription()).append("\n");
			}
		}

		return markdown.toString();
	}

	private void saveMarkdownToFile(String markdownContent, String projectTitle) {
		String fileName = projectTitle.replaceAll(" ", "_") + ".md"; // File name

		try (FileWriter fileWriter = new FileWriter(fileName)) {
			fileWriter.write(markdownContent);
			System.out.println("Markdown file saved as: " + fileName);
		} catch (IOException e) {
			System.err.println("Error saving markdown file: " + e.getMessage());
		}
	}

	public Todo getTodoById(Long todoId) {
		return todoRepository.findById(todoId).orElse(null);
	}

	public void updateTodoDescription(Long todoId, String description, String todoDescription) {
		Todo todo = todoRepository.findById(todoId).orElse(null);
		if (todo != null) {
			todo.setDescription(description);
			todo.setTodoDescription(todoDescription); // Update todoDescription
			todo.setUpdatedDate(LocalDateTime.now());
			todoRepository.save(todo);
		}
	}

	public void updateProjectTitle(Long projectId, String title) {
		Project project = getProjectById(projectId);
		if (project != null) {
			project.setTitle(title);
			projectRepository.save(project);
		}
	}
}
