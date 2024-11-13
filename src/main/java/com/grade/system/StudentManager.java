package com.grade.system;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;

@SuppressWarnings("all")
public class StudentManager extends Printer {
    public MongoCollection<Document> s;
    public String id;

    public StudentManager(String user_id, MongoCollection<Document> students) {
        s = students;
        id = user_id;
    }

    public void listStudents(Runnable callback) {
        clear();
        FindIterable students = s.find(Filters.in("user_id", id));
        students.forEach(doc -> printStudent((Document) doc));
        cont();
        callback.run();
    }

    private void printStudent(Document student) {
        char middle = student.getString("middleName").charAt(0);
        String fullName = student.getString("lastName") + ", " + student.getString("firstName") + " " + middle + ".";
        fullName = ANSI_RED + fullName + ANSI_RESET;
        String course = ANSI_YELLOW + student.getString("Course") + ANSI_RESET;
        line(fullName + " - " + course);
        newLine();
    }

    public void addStudents(Runnable callback) {
        clear();
        line(ANSI_BLUE + "ADD A NEW STUDENT:\n" + ANSI_RESET);
        Document doc = new Document("user_id", id)
                .append("lastName", prompt(ANSI_GREEN + "Last name: "
                        + ANSI_RESET).toUpperCase())
                .append("firstName", prompt(ANSI_GREEN + "First name: "
                        + ANSI_RESET)
                        .toUpperCase())
                .append("middleName", prompt(ANSI_GREEN + "Middle name: "
                        + ANSI_RESET)
                        .toUpperCase())
                .append("Course", prompt(ANSI_GREEN + "Course: " + ANSI_RESET).toUpperCase());
        InsertOneResult res = s.insertOne(doc);
        if (res.wasAcknowledged()) {
            char initial = doc.getString("middleName").toCharArray()[0];
            String fullName = doc.getString("firstName") + " " + initial + ". " + doc.getString("lastName");
            line(ANSI_RED + fullName + ANSI_RESET + " is added to your student list!");
            newLine();
        }
        cont();
        callback.run();
    }

    public void removeStudent(Runnable callback) {
        clear();
        line();
        newLine();
        line(ANSI_RESET + ANSI_RED + "REMOVE A STUDENT:\n" + ANSI_RESET);
        String text = prompt("-> ");
        List<Document> students = new ArrayList<Document>();
        s.find(Filters.text(text)).forEach(doc -> students.add(doc));
        int i = 1;
        if (students.isEmpty()) {
            removeStudent(callback);
        }
        clear();
        for (Document stud : students) {
            String fullName = stud.getString("lastName") + ", " + stud.getString("firstName") + " "
                    + stud.getString("middleName").charAt(0);
            line("[" + ANSI_YELLOW + i + ANSI_RESET + "] " + ANSI_GREEN + fullName + ANSI_RESET);
            newLine();
            i++;
        }
        int num = prompt();
        try {
            Document stud = students.get(num - 1);
            String fullName = stud.getString("lastName") + ", " + stud.getString("firstName") + " "
                    + stud.getString("middleName").charAt(0);
            clear();
            line("Delete " + ANSI_RED + fullName + ANSI_RESET + "?");
            newLine();
            choices(new String[] { "Yes", "No" });
            printExit();
            num = prompt();

            switch (num) {
                case 1:
                    delete(stud, callback);
                    break;
                case 2:
                    callback.run();
                    break;
                default:
                    System.exit(0);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
            cont();
            callback.run();
        }
    }

    private void delete(Document st, Runnable callback) {
        clear();
        DeleteResult res = s.deleteOne(Filters.eq("_id", st.getObjectId("_id")));
        if (res.wasAcknowledged() && res.getDeletedCount() > 0) {
            line("STUDENT SUCCESSFULLY DELETED!");
            newLine();
        }
        cont();
        callback.run();
    }

    public void editStudent(Runnable callback) {
        clear();
        line();
        newLine();
        line(ANSI_RESET + ANSI_GREEN + "EDIT A STUDENT:\n" + ANSI_RESET);
        String text = prompt("-> ");
        List<Document> students = new ArrayList<Document>();
        s.find(Filters.text(text)).forEach(doc -> students.add(doc));
        int i = 1;
        if (students.isEmpty()) {
            editStudent(callback);
        }
        clear();
        for (Document stud : students) {
            String fullName = stud.getString("lastName") + ", " + stud.getString("firstName") + " "
                    + stud.getString("middleName").charAt(0) + ".";
            line("[" + ANSI_YELLOW + i + ANSI_RESET + "] " + ANSI_GREEN + fullName + ANSI_RESET);
            newLine();
            i++;
        }
        int num = prompt();
        try {
            Document stud = students.get(num - 1);
            String fullName = stud.getString("lastName") + ", " + stud.getString("firstName") + " "
                    + stud.getString("middleName").charAt(0) + ".";
            clear();
            line("EDIT " + ANSI_CYAN + fullName + ANSI_RESET + "'s INFO");
            newLine();
            String[] ch = { "lastName", "firstName", "middleName", "Course" };
            choices(ch);
            printExit();
            num = prompt();

            if (num > 0 && num < 5) {
                update(stud, ch[num - 1], callback);
            } else {
                System.exit(0);
            }

        } catch (Exception e) {
            cont();
            callback.run();
        }
    }

    private void update(Document stud, String key, Runnable callback) {
        clear();
        line();
        newLine();
        line("Edit " + ANSI_GREEN + key + ANSI_RESET);
        newLine();
        String text = prompt("-> ");
        UpdateResult res = s.updateOne(Filters.eq("_id", stud.getObjectId("_id")), Updates.set(key, text));
        if (res.wasAcknowledged() && res.getModifiedCount() > 0) {
            line("STUDENT'S " + key.toUpperCase() + " SUCCESSFULLY UPDATED!");
            newLine();
        }
        cont();
        callback.run();
    }
}