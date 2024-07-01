package com.sdu.pandevbot;

public class Constants {

	public static final String START_MESSAGE = """
			Привет!
			
			Я бот для управления деревом категорий. Ты можешь создавать, просматривать и удалять категории с помощью команд.
			
			Используйте команду /help для получения списка доступных команд.
			""";

	public static final String HELP_MESSAGE = """
			/viewTree (/vt) – Показать дерево категорий
			/addElement &lt;название элемента&gt; (/add) – Добавить корневой элемент
			/addElement &lt;родительский элемент&gt; &lt;дочерний элемент&gt; – Добавить дочерний элемент
			/removeElement &lt;название элемента&gt; (/rm) – Удалить элемент
			/help – Показать это сообщение
			""";
}
