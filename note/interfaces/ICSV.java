package note.interfaces;

import note.util.ExceptionProg;

public interface ICSV {
    String[][] readFileCSV(String fileName, String charset) throws ExceptionProg;
    void writeFileCSV(String[][] ss, String fileName, String charset) throws ExceptionProg;
}
