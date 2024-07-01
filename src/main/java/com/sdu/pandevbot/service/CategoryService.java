package com.sdu.pandevbot.service;

public interface CategoryService {

	String viewTree(Long userId);

	String addElement(Long userId, String name);

	String addElement(Long userId, String parentName, String childName);

	String removeElement(Long userId, String name);

}
