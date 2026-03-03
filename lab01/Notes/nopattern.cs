namespace NoPattern
{
    public abstract class Note
    {
        public string Id { get; private set; } = Guid.NewGuid().ToString();
        public string Title { get; set; }

        protected Note(string title)
        {
            Title = title;
        }

        public override string ToString()
        {
            return Title;
        }
    }

    public class TextNote : Note
    {
        public string Content { get; set; }
        public Image AttachedImage { get; set; }

        public TextNote(string title, string content) : base(title)
        {
            Content = content;
        }
    }

    public class ChecklistNote : Note
    {
        public class ChecklistItem
        {
            public string Text { get; set; }
            public bool IsCompleted { get; set; }

            public ChecklistItem(string text, bool completed = false)
            {
                Text = text;
                IsCompleted = completed;
            }
        }

        private List<ChecklistItem> _items = new();
        public Image AttachedImage { get; set; }

        public ChecklistNote(string title) : base(title) { }

        public void AddItem(string text)
        {
            _items.Add(new ChecklistItem(text));
        }

        public List<ChecklistItem> GetItems()
        {
            return _items;
        }
    }
// ------------- NoteService ------------------
    public class NoteService
    {
        public readonly List<Note> _notes = new();

        public IReadOnlyList<Note> GetAll() => _notes;

        public void Add(Note note)
        {
            _notes.Add(note);
        }

        public void Remove(Note note)
        {
            _notes.Remove(note);
        }

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
    }
}