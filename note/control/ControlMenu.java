package note.control;

import note.interfaces.*;
import note.util.ExceptionProg;
import note.util.ExceptionUser;
import note.view.ExceptionExit;
import note.interfaces.IView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import java.util.logging.Logger;

public class ControlMenu implements IControl {
    private static final Logger logger = Logger.getLogger(ControlMenu.class.getName());
    static private final String messageStart = "\nПрограмма \"Заметки\".";
    static private final String messageFinal = "Работа программы завершена.";
    static final String DATE_FORMAT = "dd.MM.yyyy HH:mm";
    static private final String CREATE = "1"; // TODO надо будет переделать в enum и меню в нем же реализовать
    static private final String EDIT = "2";
    static private final String SEARCH = "3";
    static private final String VIEW = "4";
    static private final String DELETE = "5";
    static private final String SAVE = "6";
    static private final String SHOW_ALL = "7";
    static private final String SEARCH_BY_ID = "8";
    static private final String SEARCH_BY_DATE = "9";
    static private final String EXIT = "0";

    static private final String[] MENU_SYMBOLS = {CREATE, EDIT, SEARCH, VIEW, DELETE, SAVE, SHOW_ALL, EXIT};
    static private final String MENU = """
            Меню:
            1 - создать
            2 - редактировать
            3 - поиск
            4 - детали
            5 - удалить
            6 - сохранить все изменения
            7 - все заметки
            0 - выход из программы
            """;

    static private final String[] SUBMENU_SYMBOLS = {SEARCH_BY_ID, SEARCH_BY_DATE, EXIT};
    static private final String SUBMENU = """
            Меню:
            8 - поиск по номеру
            9 - поиск по дате
            0 - вернуться в главное меню
            """;


    private final IView view; // визуализатор (MCV)
    private final INotes notes; // модель  (MCV)
    private final IFactoryNote fNote; // фабрика для создания заметки
    private final ICSV csv; // для работы с файлами csv
    private final String dbFile; // имя файла с базой данных
    private final String charset; // кодировка файла с базой данных

    public ControlMenu(IView view, INotes notes, IFactoryNote fNote, ICSV csv, String dbFile, String charset) {
        this.view = view;
        this.notes = notes;
        this.fNote = fNote;
        this.csv = csv;
        this.dbFile = dbFile;
        this.charset = charset;
    }

    @Override
    public void run() throws ExceptionProg {
        String result = "";
        try {
            while (true) {
                // рисую меню, потом раздел с ответной информаацией
                view.clearScreen();
                view.viewText(messageStart);
                String s = MENU + "------------------------------\n" + result + "\n------------------------------";
                String symbol = view.requestMenu(s, MENU_SYMBOLS);
                result = "";
                switch (symbol) {
                    case CREATE -> { // создать запись
                        String head = view.request("Заголовок записи.");
                        String body = view.request("Текст записи.");
                        INote note = fNote.cteate(head, body);
                        notes.add(note);
                        result = "Добавлена запись: " + note.toString();
                    }
                    case EDIT -> {  // редактировать текст записи, если запись помечена к удалению - восстановится
                        String idS = view.request("Идентификатор записи.");
                        int id = -1;
                        try {
                            id = Integer.valueOf(idS);
                        } catch (Exception e) {
                            result = "Ошибка. Требуется ввести число.";
                            continue;
                        }
                        INote note = notes.search(id);
                        if (note == null) {
                            result = "Ошибка. Запись не найдена.";
                            continue;
                        }
                        String body = view.request("Текст записи.");
                        note.setBody(body);
                        result = "Запись отредактирована: " + note.toString();
                    }
                    case SEARCH -> { // отдельное меню для поисковых функций
                        runSubMenu();
                    }
                    case VIEW -> { // вывести полные данные по одной записи, которую нужно указать по номеру
                        String idS = view.request("Идентификатор записи.");
                        int id = -1;
                        try {
                            id = Integer.valueOf(idS);
                        } catch (Exception e) {
                            result = "Ошибка. Требуется ввести число.";
                            continue;
                        }
                        INote note = notes.search(id);
                        if (note == null) {
                            result = "Ошибка. Запись не найдена.";
                            continue;
                        }
                        result = note.view();
                    }
                    case DELETE -> { // пометить запись на уделение, при сохранении она не добавится в файл,
                                     // но будет доступна для возвращение в рабочую, до перезапуска программы.
                        String idS = view.request("Идентификатор записи.");
                        int id = -1;
                        try { id = Integer.valueOf(idS);
                        } catch (Exception e) {
                            result = "Ошибка. Требуется ввести число.";
                            continue;
                        }
                        INote note = notes.search(id);
                        if(note == null) {
                            result = "Ошибка. Запись не найдена.";
                            continue;
                        }
                        note.setDelete();
                        result = "Запись удалена. " + note.toString();
                    }
                    case SAVE -> {  // сохранить записи в файл, кроме помеченых на удаление
                        csv.writeFileCSV(notes.getArrayNotes(), dbFile, charset);
                        notes.setSaveDate(new Date().getTime());
                        result = "Данные сохранены в файл " + dbFile
                        +"\n\nПоследние заметки:\n"+notes.getLastNotes(10);
                    }
                    case SHOW_ALL -> {  // вывести полную информацию по всем записям
                        List<INote> list = notes.getListNotes();
                        StringBuilder sb = new StringBuilder();
                        for(INote note: list)
                            sb.append(note.view()).append("\n");
                        result = sb.toString();
                    }
                    case EXIT -> {  // выход из программы
                        view.viewText(messageFinal);
                        return;
                    }
                    default -> {
                        logger.warning("Не обработанный пункт меню " + symbol);
                    }
                }
            }
        } catch (ExceptionExit e) {
            view.viewText(messageFinal);
            return;
        }
    }

    // подменю, устроено по аналогии с основным
    private void runSubMenu() throws ExceptionProg, ExceptionExit {
        String result = "";
        try {
            while (true) {
                view.clearScreen();
                view.viewText(messageStart);
                String s = SUBMENU + "------------------------------\n" + result + "\n------------------------------";
                String symbol = view.requestMenu(s, SUBMENU_SYMBOLS);
                result = "";
                switch (symbol) {
                    case SEARCH_BY_ID -> { // поиск записи по номеру и вывод полной информации по указанной записи
                        String idS = view.request("Идентификатор записи.");
                        int id = -1;
                        try {
                            id = Integer.valueOf(idS);
                        } catch (Exception e) {
                            result = "Ошибка. Требуется ввести число.";
                            continue;
                        }
                        INote note = notes.search(id);
                        if (note == null) {
                            result = "Ошибка. Запись не найдена.";
                            continue;
                        }
                        result = note.view();
                    }
                    case SEARCH_BY_DATE -> { // вывод полной информации по всем записям созданным в указанном диапазоне дат

                        String beginDateS = "";
                        long beginDate = -1;
                        int i=3; // чтобы не мучать пользователя - дается несколько попыток, чтобы указать дату в правильном формате
                        while(i>=0) {
                            beginDateS = view.request("Диапазон дат создания. Дата НАЧАЛА в формате 01.02.2023 01:59");
                            try {
                                beginDate = (new SimpleDateFormat(DATE_FORMAT)).parse(beginDateS).getTime();
                                break;
                            } catch (ParseException e) {
                                logger.info("Введенная пользователем дата не распозналась"
                                    + beginDateS + e.getMessage());
                                view.viewText("Ошибка. Неправильный формат даты.");
                            }
                            i--;
                        }
                        if (beginDate == -1) {
                            result = "Ошибка. Неправильный формат даты.";
                            continue;
                        }

                        String endDateS = "";
                        long endDate = -1;
                        i=3; // чтобы не мучать пользователя - дается несколько попыток, чтобы указать дату в правильном формате
                        while(i>=0) {
                            endDateS = view.request("Диапазон дат создания. Дата ОКОНЧАНИЯ в формате 01.02.2023 01:59");
                            try {
                                endDate = (new SimpleDateFormat(DATE_FORMAT)).parse(endDateS).getTime();
                                break;
                            } catch (ParseException e) {
                                logger.info("Введенная пользователем дата не распозналась"
                                        + endDateS + e.getMessage());
                                view.viewText("Ошибка. Неправильный формат даты.");
                            }
                            i--;
                        }
                        if (endDate == -1) {
                            result = "Ошибка. Неправильный формат даты.";
                            continue;
                        }

                        String title = "Записи из диапазона дат: с "+ (new SimpleDateFormat(DATE_FORMAT)).format(beginDate)
                                +" по "+(new SimpleDateFormat(DATE_FORMAT)).format(endDate);

                        String info = notes.search2(beginDate, endDate);
                        if (info == null || info.isBlank()) {
                            result = "В выбранном диапазоне записей нет";
                            continue;
                        }
                        result = title  +"\n\n"+ info;
                    }
                    case EXIT -> { // выход из подменю в основное меню
                        return;
                    }
                    default -> {
                        logger.warning("Не обработанный пункт меню " + symbol);
                    }
                }
            }
        } catch (ExceptionExit e) {
            throw new ExceptionExit(e.getMessage());
        }
    }



}
