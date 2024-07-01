package com.sdu.pandevbot.service;

import com.sdu.pandevbot.repository.CategoryRepository;
import com.sdu.pandevbot.repository.entity.Category;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepository;

	@Autowired
	public CategoryServiceImpl(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	@Transactional
	public String viewTree(Long userId) {
		List<Category> categories = categoryRepository.findAllByUserId(userId);
		StringBuilder treeView = new StringBuilder();
		for (Category category : categories) {
			if (category.getParent() == null) {
				buildTreeView(category, treeView, 0, new ArrayList<>());
			}
		}

		if (!treeView.isEmpty()) {
			treeView.insert(0, "<pre>");
			treeView.append("</pre>");
		}

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
	public String addElement(Long userId, String name) {
		if (categoryRepository.findByName(name).isPresent()) {
			return "Элемент с таким именем уже существует.";
		}
		Category category = new Category(name);
		category.setUserId(userId);
		categoryRepository.save(category);
		return "Элемент успешно добавлен.";
	}

	@Transactional
	public String addElement(Long userId, String parentName, String childName) {
		Optional<Category> parentOpt = categoryRepository.findByName(parentName);
		if (parentOpt.isEmpty()) {
			return "Родительский элемент не найден.";
		}
		if (categoryRepository.findByName(childName).isPresent()) {
			return "Элемент с таким именем уже существует.";
		}
		Category parent = parentOpt.get();
		Category child = new Category(childName);
		child.setUserId(userId);
		child.setParent(parent);
		parent.addChild(child);
		categoryRepository.save(parent);
		return "Элемент успешно добавлен.";
	}

	@Transactional
	public String removeElement(Long userId, String name) {
		Optional<Category> categoryOpt = categoryRepository.findByNameAndUserId(name, userId);
		if (categoryOpt.isEmpty()) {
			return "Элемент не найден.";
		}
		Category category = categoryOpt.get();
		categoryRepository.delete(category);
		return "Элемент успешно удален.";
	}

	@Transactional
	public byte[] downloadTree(Long userId) throws IOException {
		List<Category> categories = categoryRepository.findAllByUserId(userId);

		categories = categories.stream()
				.sorted(Comparator.comparing(Category::getId))
				.toList();

		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Tree");

			Row headerRow = sheet.createRow(0);
			headerRow.createCell(0).setCellValue("id");
			headerRow.createCell(1).setCellValue("name");
			headerRow.createCell(2).setCellValue("parent_id");

			int rowIndex = 1;
			for (Category category : categories) {
				Row row = sheet.createRow(rowIndex++);
				row.createCell(0).setCellValue(category.getId());
				row.createCell(1).setCellValue(category.getName());
				Cell parentIdCell = row.createCell(2);
				if (category.getParent() != null) {
					parentIdCell.setCellValue(category.getParent().getId());
				} else {
					parentIdCell.setBlank();
				}
			}

			try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
				workbook.write(outputStream);
				return outputStream.toByteArray();
			}
		}
	}
}
