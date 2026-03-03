# Лабораторная работа №1 — Паттерн Прототип (Prototype)
## Предметная область и описание проблемы
В данном проекте реализуется приложение для ведения заметок разной структуры. В данной реализации присутствуют заметки двух типов:
1. TextNote - обычная текстовая заметка
2. ChecklistNote - текстовая заметка со списком задач с возможностью отмечать прогресс
Для заметок есть возможность загрузить одно изображение, а также создать полную копию заметки.

Проблема, появляющаяся при разработке, - при добавлении новых заметок необходимо добавлять в метод Duplicate еще одну if ветку с новой реализацией. Во-первых, это приводит к дублированию - каждый раз нужно писать одинаковый код
для копирования аттрибутов объектов. На данном этапе атрибутов всего несколько, но с расширением функционала это будет становиться хуже. Во-вторых, такой подход нарушает принципы ООП - инкапсуляцию
и полиморфизм. NoteService должен знать внутреннюю реализацию заметок, а его поведение регулируется не классом заметки, а набором if/else. В такой ситуации какое то небольшое изменение в
родительском классе Note приведет к необходимости модифицировать весь метод Duplicate.

## 2. Решение: применение паттерна Прототип
Паттерн позволяет вынести внутеннюю реализацию в конкретные классы и сократить метод Duplicate до
```csharp
public void Duplicate(Note note)
{
    _notes.Add(note.Clone());
}
```
Заместо
```csharp
public void Duplicate(Note note)
{
    if (note is TextNote textNote)
    {
        var copy = new TextNote(
            textNote.Title + " (Copy)",
            textNote.Content
        );

        if (textNote.AttachedImage != null)
            copy.AttachedImage = new Bitmap(textNote.AttachedImage);

        _notes.Add(copy);
    }
    else if (note is ChecklistNote checklistNote)
    {
        var copy = new ChecklistNote(
            checklistNote.Title + " (Copy)"
        );

        foreach (var item in checklistNote.GetItems())
        {
            copy.AddItem(item.Text);
            copy.GetItems().Last().IsCompleted = item.IsCompleted;
        }

        if (checklistNote.AttachedImage != null)
            copy.AttachedImage = new Bitmap(checklistNote.AttachedImage);

        _notes.Add(copy);
    }
}
```

## 3. Диаграмма классов
Диаграмма представлена в файле `klassi.drawio.png`.
![alt text](klassi.drawio.png)

## 4. Вывод
Внедрение паттерна дало следующие результаты:
1. Убрано дублирование - в классе NoteService не нужно создавать однотипный код с копированием аттрибутов
2. Возможность простого расширения - достаточно создать наследника класса Note с реализацией Clone() без необходимости менять класс NoteService