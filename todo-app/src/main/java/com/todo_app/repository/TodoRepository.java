package com.todo_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.todo_app.entity.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long> {
}
