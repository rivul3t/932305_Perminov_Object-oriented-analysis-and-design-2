using System;

namespace NotesApp
{
    public abstract class Note
    {
        public string Id { get; private set; } = Guid.NewGuid().ToString();
        public string Title { get; set; }
        public Image AttachedImage { get; set; }

        protected Note(string title)
        {
            Title = title;
        }

        public abstract Note Clone();

        public override string ToString()
        {
            return Title;
        }
    }

    public class TextNote : Note
    {
        public string Content { get; set; }

        public TextNote(string title, string content) : base(title)
        {
            Content = content;
        }

        public override Note Clone()
        {
            var clone = new TextNote(Title + " (Copy)", Content);

            if (AttachedImage != null)
                clone.AttachedImage = new Bitmap(AttachedImage);

            return clone;
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
    
            public ChecklistItem Clone()
            {
                return new ChecklistItem(Text, IsCompleted);
            }
        }
    
        private List<ChecklistItem> _items = new();
    
        public ChecklistNote(string title) : base(title) { }
    
        public void AddItem(string text)
        {
            _items.Add(new ChecklistItem(text));
        }
    
        public void ClearItems()
        {
            _items.Clear();
        }
    
        public void SetItemCompleted(int index, bool value)
        {
            if (index >= 0 && index < _items.Count)
                _items[index].IsCompleted = value;
        }
    
        public List<ChecklistItem> GetItems()
        {
            return _items;
        }
    
        public override Note Clone()
        {
            var clone = new ChecklistNote(Title + " (Copy)");
    
            foreach (var item in _items)
                clone._items.Add(item.Clone());
    
            if (AttachedImage != null)
                clone.AttachedImage = new Bitmap(AttachedImage);
    
            return clone;
        }
    }
}