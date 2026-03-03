using System.Collections.Generic;

namespace NotesApp
{
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
            _notes.Add(note.Clone());
        }
    }
}