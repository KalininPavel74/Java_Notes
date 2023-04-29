package note.interfaces;

import note.view.ExceptionExit;

public interface IView {
    void viewText(String text);
    String request(String description) throws ExceptionExit;
    String requestMenu(String description, String[] symbols) throws ExceptionExit;
    void clearScreen();
}
