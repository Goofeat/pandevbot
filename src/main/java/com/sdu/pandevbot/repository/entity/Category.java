package com.sdu.pandevbot.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_seq")
	@SequenceGenerator(name = "category_seq", sequenceName = "category_id_seq", allocationSize = 1)
	private Long id;

	private Long userId;

	private String name;

	@ManyToOne
	@JoinColumn(name = "parent_id")
	private Category parent;

	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Category> children = new ArrayList<>();

	// Конструкторы
	public Category() {
	}

	public Category(String name) {
		this.name = name;
	}

	// Метод для добавления дочернего элемента
	public void addChild(Category child) {
		child.setParent(this);
		this.children.add(child);
	}

}