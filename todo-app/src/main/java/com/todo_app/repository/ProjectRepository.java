package com.todo_app.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.todo_app.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}

