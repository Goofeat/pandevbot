package com.sdu.pandevbot;

import org.springframework.beans.factory.annotation.Autowired;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class Service {

	private final Repository categoryRepository;

	@Autowired
	public Service(Repository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	@Transactional
	public String viewTree() {
		List<Category> categories = categoryRepository.findAllWithChildren();
		StringBuilder treeView = new StringBuilder();
		treeView.append("<pre>");
		for (Category category : categories) {
			if (category.getParent() == null) {
				buildTreeView(category, treeView, 0, new ArrayList<>());
			}
		}
		treeView.append("</pre>");
		return treeView.toString();
	}

	private void buildTreeView(Category category, StringBuilder builder, int level, List<Boolean> lastNodes) {
		for (int i = 0; i < level; i++) {
			if (i < level - 1) {
				builder.append(lastNodes.get(i) ? "    " : "│   ");
			} else {
				builder.append(lastNodes.get(i) ? "└── " : "├── ");
			}
		}
		builder.append(category.getName()).append("\n");

		List<Category> children = category.getChildren();
		for (int i = 0; i < children.size(); i++) {
			lastNodes.add(i == children.size() - 1);
			buildTreeView(children.get(i), builder, level + 1, lastNodes);
			lastNodes.removeLast();
		}
	}

	@Transactional
	public String addElement(String name) {
		if (categoryRepository.findByName(name).isPresent()) {
			return "Элемент с таким именем уже существует.";
		}
		Category category = new Category();
		category.setName(name);
		categoryRepository.save(category);
		return "Элемент успешно добавлен.";
	}

	@Transactional
	public String addElement(String parentName, String childName) {
		Optional<Category> parentOpt = categoryRepository.findByName(parentName);
		if (parentOpt.isEmpty()) {
			return "Родительский элемент не найден.";
		}
		if (categoryRepository.findByName(childName).isPresent()) {
			return "Элемент с таким именем уже существует.";
		}
		Category parent = parentOpt.get();
		Category child = new Category();
		child.setName(childName);
		child.setParent(parent);
		parent.getChildren().add(child);
		categoryRepository.save(parent);
		return "Элемент успешно добавлен.";
	}

	@Transactional
	public String removeElement(String name) {
		Optional<Category> categoryOpt = categoryRepository.findByName(name);
		if (categoryOpt.isEmpty()) {
			return "Элемент не найден.";
		}
		Category category = categoryOpt.get();
		categoryRepository.delete(category);
		return "Элемент успешно удален.";
	}
}
