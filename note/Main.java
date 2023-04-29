package note;

import note.control.FactoryControlMenu;
import note.interfaces.*;
import note.model.FactoryNotes;
import note.model.FactoryNote;
import note.util.ExceptionProg;
import note.util.FactoryUtil;
import note.util.MyLog;
import note.view.FactoryView;

import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static final String DB_FILE = "notes.csv"; // Файл с базой данных.
    private static final String CHARSET = "UTF-8"; // Кодировка файла с базой данных.

    public static void main(String[] args) throws ExceptionProg {
        MyLog.loggerInit(MyLog.LOG_FILE); // логирование

        // Фабрика для объектов утилит. Для доступа к классу для работы с файлами csv
        IFactoryUtil fUtil = new FactoryUtil();
        ICSV csv = fUtil.create();
        String[][] ss = csv.readFileCSV(DB_FILE, CHARSET);

        // Фабрика для получения объекта Заметки - модель (MCV)
        IFactoryNotes fNotes = new FactoryNotes();
        INotes notes = fNotes.create(ss);
        // Фабрика для получения объекта ЗаметкА
        // отделена, для того чтобы передать ее в контроллер (MCV)
        // Контроллер будет создават записи через фабрику.
        IFactoryNote fNote = new FactoryNote(notes.getGenerator());

        // Фабрика для получения объекта визуализации (MCV)
        IFactoryView fv = new FactoryView();
        // Выход из программы по кнопке q или из меню контроллера.
        IView view = fv.create("", MyLog.CHARSET_CONSOLE, new char[]{'Q', 'q'});

        // Фабрика для получения объекта контроллер (MCV)
        IFactoryControl fc = new FactoryControlMenu();
        IControl control = fc.create(view, notes, fNote, csv, DB_FILE, CHARSET);

        control.run(); // запуск цикла работы
    }
}