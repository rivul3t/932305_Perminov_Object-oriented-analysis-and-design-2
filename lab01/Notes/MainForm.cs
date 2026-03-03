using System;
using System.Drawing;
using System.Windows.Forms;

namespace NotesApp
{
    public class MainForm : Form
    {
        private readonly NoteService _noteService = new();

        private ListBox notesList = new();
        private TextBox titleBox = new();
        private TextBox textEditor = new();
        private CheckedListBox checklistEditor = new();
        private PictureBox imageBox = new();

        private TextBox checklistItemBox = new();
        private Button addItemButton = new();
        private Button editItemButton = new();
        private Button removeItemButton = new();
        private TableLayoutPanel checklistControls;

        private Button saveButton = new();
        private Button addTextButton = new();
        private Button addChecklistButton = new();
        private Button duplicateButton = new();
        private Button deleteButton = new();
        private Button loadImageButton = new();

        public MainForm()
        {
            Text = "Notes App";
            Width = 1000;
            Height = 600;

            InitializeLayout();
        }

        private void InitializeLayout()
        {
            var mainLayout = new TableLayoutPanel();
            mainLayout.Dock = DockStyle.Fill;
            mainLayout.ColumnCount = 2;
            mainLayout.ColumnStyles.Add(new ColumnStyle(SizeType.Absolute, 250));
            mainLayout.ColumnStyles.Add(new ColumnStyle(SizeType.Percent, 100));

            checklistItemBox.Dock = DockStyle.Fill;

            addItemButton.Text = "Add Item";
            addItemButton.Dock = DockStyle.Fill;
            addItemButton.Click += AddItemButton_Click;

            editItemButton.Text = "Edit Item";
            editItemButton.Dock = DockStyle.Fill;
            editItemButton.Click += EditItemButton_Click;

            removeItemButton.Text = "Remove Item";
            removeItemButton.Dock = DockStyle.Fill;
            removeItemButton.Click += RemoveItemButton_Click;

            Controls.Add(mainLayout);

            var leftPanel = new TableLayoutPanel();
            leftPanel.Dock = DockStyle.Fill;
            leftPanel.RowCount = 6;
            leftPanel.RowStyles.Add(new RowStyle(SizeType.Percent, 100));
            leftPanel.RowStyles.Add(new RowStyle(SizeType.Absolute, 40));
            leftPanel.RowStyles.Add(new RowStyle(SizeType.Absolute, 40));
            leftPanel.RowStyles.Add(new RowStyle(SizeType.Absolute, 40));
            leftPanel.RowStyles.Add(new RowStyle(SizeType.Absolute, 40));
            leftPanel.RowStyles.Add(new RowStyle(SizeType.Absolute, 10));

            notesList.Dock = DockStyle.Fill;
            notesList.SelectedIndexChanged += NotesList_SelectedIndexChanged;

            addTextButton.Text = "Add Text Note";
            addTextButton.Dock = DockStyle.Fill;
            addTextButton.Click += (s, e) =>
            {
                var note = new TextNote("New Text Note", "");
                _noteService.Add(note);
                RefreshList();
            };

            addChecklistButton.Text = "Add Checklist";
            addChecklistButton.Dock = DockStyle.Fill;
            addChecklistButton.Click += (s, e) =>
            {
                var note = new ChecklistNote("New Checklist");
                _noteService.Add(note);
                RefreshList();
            };

            duplicateButton.Text = "Duplicate";
            duplicateButton.Dock = DockStyle.Fill;
            duplicateButton.Click += (s, e) =>
            {
                if (notesList.SelectedItem is Note note)
                {
                    _noteService.Duplicate(note);
                    RefreshList();
                }
            };

            deleteButton.Text = "Delete";
            deleteButton.Dock = DockStyle.Fill;
            deleteButton.Click += (s, e) =>
            {
                if (notesList.SelectedItem is Note note)
                {
                    _noteService.Remove(note);
                    RefreshList();
                }
            };

            leftPanel.Controls.Add(notesList, 0, 0);
            leftPanel.Controls.Add(addTextButton, 0, 1);
            leftPanel.Controls.Add(addChecklistButton, 0, 2);
            leftPanel.Controls.Add(duplicateButton, 0, 3);
            leftPanel.Controls.Add(deleteButton, 0, 4);

            mainLayout.Controls.Add(leftPanel, 0, 0);

            var rightPanel = new TableLayoutPanel();
            rightPanel.Dock = DockStyle.Fill;
            rightPanel.RowCount = 5;
            rightPanel.ColumnCount = 2;

            rightPanel.ColumnStyles.Add(new ColumnStyle(SizeType.Percent, 70));
            rightPanel.ColumnStyles.Add(new ColumnStyle(SizeType.Percent, 30));
            rightPanel.RowStyles.Clear();

            rightPanel.RowStyles.Add(new RowStyle(SizeType.Absolute, 40));
            rightPanel.RowStyles.Add(new RowStyle(SizeType.Percent, 100));
            rightPanel.RowStyles.Add(new RowStyle(SizeType.Absolute, 40));
            rightPanel.RowStyles.Add(new RowStyle(SizeType.Absolute, 50));
            rightPanel.RowStyles.Add(new RowStyle(SizeType.Absolute, 10));

            titleBox.Dock = DockStyle.Fill;

            textEditor.Multiline = true;
            textEditor.Dock = DockStyle.Fill;

            checklistEditor.Dock = DockStyle.Fill;

            imageBox.Dock = DockStyle.Fill;
            imageBox.SizeMode = PictureBoxSizeMode.Zoom;

            loadImageButton.Text = "Load Image";
            loadImageButton.Dock = DockStyle.Fill;
            loadImageButton.Click += LoadImageButton_Click;

            saveButton.Text = "Save";
            saveButton.Dock = DockStyle.Fill;
            saveButton.Click += SaveButton_Click;

            rightPanel.Controls.Add(titleBox, 0, 0);
            rightPanel.SetColumnSpan(titleBox, 2);

            rightPanel.Controls.Add(textEditor, 0, 1);
            rightPanel.Controls.Add(checklistEditor, 0, 1);
            rightPanel.Controls.Add(imageBox, 1, 1);

            rightPanel.Controls.Add(saveButton, 0, 3);
            rightPanel.Controls.Add(loadImageButton, 1, 3);

            checklistControls = new TableLayoutPanel();
            checklistControls.Dock = DockStyle.Bottom;
            checklistControls.Height = 35;
            checklistControls.ColumnCount = 4;
            checklistControls.ColumnStyles.Add(new ColumnStyle(SizeType.Percent, 40));
            checklistControls.ColumnStyles.Add(new ColumnStyle(SizeType.Percent, 20));
            checklistControls.ColumnStyles.Add(new ColumnStyle(SizeType.Percent, 20));
            checklistControls.ColumnStyles.Add(new ColumnStyle(SizeType.Percent, 20));

            checklistControls.Controls.Add(checklistItemBox, 0, 0);
            checklistControls.Controls.Add(addItemButton, 1, 0);
            checklistControls.Controls.Add(editItemButton, 2, 0);
            checklistControls.Controls.Add(removeItemButton, 3, 0);

            rightPanel.Controls.Add(checklistControls, 0, 2);
            rightPanel.SetColumnSpan(checklistControls, 2);

            checklistControls.Visible = false;

            mainLayout.Controls.Add(rightPanel, 1, 0);

            checklistEditor.Visible = false;
            imageBox.Visible = false;
            loadImageButton.Visible = false;
        }

        private void NotesList_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (notesList.SelectedItem is not Note note)
                return;

            titleBox.Text = note.Title;

            if (note is TextNote textNote)
            {
                textEditor.Visible = true;
                checklistEditor.Visible = false;
                checklistControls.Visible = false;

                imageBox.Visible = true;
                loadImageButton.Visible = true;

                textEditor.Text = textNote.Content;
                imageBox.Image = textNote.AttachedImage;
            }
            else if (note is ChecklistNote checklistNote)
            {
                textEditor.Visible = false;
                checklistEditor.Visible = true;
                imageBox.Visible = true;
                loadImageButton.Visible = true;
                checklistControls.Visible = true;

                checklistEditor.Items.Clear();

                foreach (var item in checklistNote.GetItems())
                    checklistEditor.Items.Add(item.Text, item.IsCompleted);

                imageBox.Image = checklistNote.AttachedImage;
            }
        }

        private void SaveButton_Click(object sender, EventArgs e)
        {
            if (notesList.SelectedItem is not Note note)
                return;

            note.Title = titleBox.Text;

            if (note is TextNote textNote)
            {
                textNote.Content = textEditor.Text;
            }
            else if (note is ChecklistNote checklistNote)
            {
                checklistNote.ClearItems();

                for (int i = 0; i < checklistEditor.Items.Count; i++)
                {
                    checklistNote.AddItem(checklistEditor.Items[i].ToString());
                    checklistNote.SetItemCompleted(i, checklistEditor.GetItemChecked(i));
                }
            }

            RefreshList();
        }

        private void AddItemButton_Click(object sender, EventArgs e)
        {
            if (string.IsNullOrWhiteSpace(checklistItemBox.Text))
                return;

            checklistEditor.Items.Add(checklistItemBox.Text, false);
            checklistItemBox.Clear();
        }

        private void EditItemButton_Click(object sender, EventArgs e)
        {
            int index = checklistEditor.SelectedIndex;
            if (index < 0) return;

            checklistEditor.Items[index] = checklistItemBox.Text;
        }

        private void RemoveItemButton_Click(object sender, EventArgs e)
        {
            int index = checklistEditor.SelectedIndex;
            if (index < 0) return;

            checklistEditor.Items.RemoveAt(index);
        }

        private void LoadImageButton_Click(object sender, EventArgs e)
        {
            if (notesList.SelectedItem is not Note note)
                return;

            using OpenFileDialog dialog = new();
            dialog.Filter = "Images|*.png;*.jpg;*.jpeg;*.bmp";

            if (dialog.ShowDialog() == DialogResult.OK)
            {
                var img = Image.FromFile(dialog.FileName);

                if (note is TextNote textNote)
                    textNote.AttachedImage = img;

                if (note is ChecklistNote checklistNote)
                    checklistNote.AttachedImage = img;

                imageBox.Image = img;
            }
        }

        private void RefreshList()
        {
            notesList.DataSource = null;
            notesList.DataSource = _noteService.GetAll();
        }
    }
}