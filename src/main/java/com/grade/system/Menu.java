package com.grade.system;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

@SuppressWarnings("all")
public class Menu extends Printer {

    public static final String[] USER_MENU = { "LIST STUDENTS", "ADD STUDENTS", "REMOVE STUDENT", "EDIT STUDENT" };

    public MongoCollection<Document> u;
    public MongoCollection<Document> s;
    public String ID = null;
    public StudentManager sm;

    public Menu(MongoCollection<Document> user, MongoCollection<Document> students) {
        u = user;
        s = students;
    }

    public void Start() {
        clear();
        line();
        newLine();
        line("WELCOME TO THE STUDENT GRADE SYSTEM VER1.0\n");
        line();
        String pass = new String(c.readPassword("Enter your ID: "));
        print(ANSI_RESET);
        verifyUser(pass);
    }

    private void verifyUser(String pass) {
        Bson filter = Filters.in("user_id", pass);
        Document f = (Document) u.find(filter).first();
        if (f != null) {
            ID = f.getString("user_id");
            sm = new StudentManager(ID, s);
            userMenu();
        } else {
            Start();
        }
    }

    private void userMenu() {
        clear();
        line();
        newLine();
        line("WELCOME " + ANSI_CYAN + ID + ANSI_RESET + "!");
        newLine();
        int i = 1;
        for (String msg : USER_MENU) {
            line("[" + ANSI_YELLOW + i + ANSI_RESET + "] " + ANSI_GREEN + msg + ANSI_RESET);
            newLine();
            i++;
        }
        printExit();
        Runnable returnToMenu = () -> userMenu();
        int pr = prompt(returnToMenu);
        switch (pr) {
            case 1:
                sm.listStudents(returnToMenu);
                break;
            case 2:
                sm.addStudents(returnToMenu);
                break;
            case 3:
                sm.removeStudent(returnToMenu);
                break;
            case 4:
                sm.editStudent(returnToMenu);
                break;
            case 0:
                System.exit(0);
                break;
        }
    }
}
