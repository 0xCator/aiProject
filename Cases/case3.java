import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    public static Admin admin;
    public static Student student;
    public static Instructor instructor;
    public static Scanner input;
    public static Console console = System.console();

    public static void main(String[] args) {
        input = new Scanner(System.in);
        while (true) {
            int selection = loginSelector();
            int panel = login(selection);
            switch (panel) {
                case 1:
                    //Admin panel
                    adminPanel();
                    break;
                case 2:
                    //Instructor panel
                    instructorPanel();
                    break;
                case 3:
                    //Student panel
                    studentPanel();
                    break;
            }
        }
    }

    //This method allows the user to select the type of user he/she wants to login as
    public static int loginSelector() {
        clearConsole();
        System.out.println("Select a login type:");
        printList("Login as an admin", "Login as an instructor", "Login as a student");
        while (true) {
            System.out.print("Enter your selection: ");
            try {
                int selection = input.nextInt();
                if (selection >= 1 && selection <= 3) {
                    return selection;
                } else {
                    System.out.println("Error: Incorrect choice, please try again");
                }
            } catch (Exception e) {
                System.out.println("Error: enter an actual number");
                input.next(); //Disregarding the entered letter
            }
        }
    }

    //Login function, takes a username and password
    public static int login(int selection) {
        clearConsole();
        while (true) {
            System.out.print("Username: ");
            String username = input.next();
            String password;
            if(console == null)
            {
                System.out.print("Password: ");
                password = input.next();
            }
            else password = readPassword();
            try {
                switch (selection) {
                    case 1:
                        admin = new Admin();
                        admin.login(username, password);
                        return selection;
                    case 2:
                        instructor = new Instructor();
                        instructor.login(username, password);
                        return selection;
                    case 3:
                        student = new Student();
                        student.login(username, password);
                        return selection;
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    //Start of admin panel methods
    public static void adminPanel() {
        boolean clear = true;
        while (true) {
            if (clear) {
                clearConsole();
                System.out.println("Welcome, " + admin.getUsername());
                printList("Manage parent courses", "Manage instructors", "Manage students",
                        "Create a new course", "Create a report", "Log out");
            }
            System.out.print("Enter your selection: ");
            try {
                int selection = input.nextInt();
                switch (selection) {
                    case 1:
                        parentCourseMenu();
                        clear = true;
                        break;
                    case 2:
                        instructorMenu();
                        clear = true;
                        break;
                    case 3:
                        studentMenu();
                        clear = true;
                        break;
                    case 4:
                        createCourse();
                        clear = true;
                        break;
                    case 5:
                        createReport();
                        clear = true;
                        break;
                    case 6:
                        return;
                    default:
                        System.out.println("Error: Incorrect choice, please try again");
                        clear = false;
                }
            } catch (Exception e) {
                System.out.println("Error: enter an actual number");
                input.next(); //Disregarding the entered letter
                clear = false;
            }
        }
    }

    public static void parentCourseMenu() {
        boolean clear = true;
        while (true) {
            if (clear) {
                clearConsole();
                System.out.println("Manage parent courses");
                printList("Add a parent course", "Update a parent course", "Delete a parent course",
                        "View parent courses", "Main panel");
            }
            System.out.print("Enter your selection: ");
            try {
                int selection = input.nextInt();
                switch (selection) {
                    case 1:
                        createParentCourse();
                        clear = true;
                        break;
                    case 2:
                        updateParentCourse();
                        clear = true;
                        break;
                    case 3:
                        deleteParentCourse();
                        clear = true;
                        break;
                    case 4:
                        listParentCourses();
                        clear = true;
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Error: Incorrect choice, please try again");
                        clear = false;
                }
            } catch (Exception e) {
                System.out.println("Error: enter an actual number");
                input.next(); //Disregarding the entered letter
                clear = false;
            }
        }
    }

    public static void createParentCourse() {
        clearConsole();
        System.out.println("Create a parent course:");
        boolean error = false;
        String code, name;
        ArrayList<String> existingParentCourses = Global.getDirectoryList(Global.ParentCourseFolder);
        do {
            System.out.print("Enter parent course's code: ");
            code = input.next();
            if (existingParentCourses.contains(code.toUpperCase())) {
                System.out.println("Error: This parent course already exists.");
                error = true;
            } else {
                error = false;
            }
        } while (error);
        input.nextLine();
        do {
            System.out.print("Enter parent course's name: ");
            name = input.nextLine();
            if (name.isEmpty()) {
                System.out.println("Error: No name was given for this parent course, please try again");
                error = true;
            } else {
                error = false;
            }
        } while (error);
        admin.createParentCourse(name, code.toUpperCase());
        System.out.println("Parent course created successfully!");
        Pause();
    }

    public static void updateParentCourse() {
        ArrayList<String> existingParentCourses = Global.getDirectoryList(Global.ParentCourseFolder);
        boolean clear = true;
        if (existingParentCourses.size() > 0) {
            while (true) {
                if (clear) {
                    clearConsole();
                    System.out.println("Update a parent course");
                    System.out.println("Available parent courses: ");
                    printList(existingParentCourses);
                    System.out.println(existingParentCourses.size() + 1 + " - Main panel");
                }
                System.out.print("Enter your selection: ");
                try
                {
                    int selection = input.nextInt();
                    if(selection >=1 && selection <= existingParentCourses.size())
                    {
                        updateParentCourseDetails(existingParentCourses.get(selection-1));
                        clear = true;
                    }
                    else if(selection == existingParentCourses.size()+1) break;
                    else {
                        System.out.println("Incorrect choice, please try again");
                        clear = false;
                    }
                }
                catch (Exception e)
                {
                    System.out.println("Error: enter an actual number");
                    input.next();
                    clear = false;
                }
            }
        } else {
            clearConsole();
            System.out.println("There are 0 parent courses");
            Pause();
        }
    }

    public static void updateParentCourseDetails(String parentCourseCode) {
        ParentCourse parentCourse = new ParentCourse(parentCourseCode);
        boolean clear = true;
        while (true) {
            if (clear) {
                clearConsole();
                System.out.println("Update " + parentCourse.getName() + ": ");
                System.out.println("Available details: ");
                printList("Name: " + parentCourse.getName(), "Main panel");
            }
            System.out.print("Enter your selection: ");
            try {
                int selection = input.nextInt();
                switch (selection) {
                    case 1:
                        System.out.print("Enter a new name: ");
                        input.nextLine();
                        String newName = input.nextLine();
                        if (!newName.isEmpty()) {
                            parentCourse.setName(newName);
                        } else {
                            System.out.println("Error: New name cannot be empty");
                            Pause();
                        }
                        clear = true;
                        break;
                    case 2:
                        return;
                    default:
                        System.out.println("Incorrect choice, please try again");
                        clear = false;
                        break;
                }
            } catch (Exception e) {
                System.out.println("Error: enter an actual number");
                input.next();
                clear = false;
            }
        }
    }

    public static void deleteParentCourse() {
        ArrayList<String> parentCourses = Global.getDirectoryList(Global.ParentCourseFolder);
        clearConsole();
        System.out.println("Delete a parent course: ");
        if (parentCourses.size() > 0) {
            System.out.println("Select parent course to delete: ");
            printList(parentCourses);
            System.out.println(parentCourses.size() + 1 + " - Main panel");
            while (true) {
                System.out.print("Enter your selection: ");
                try {
                    int selection = input.nextInt();
                    if (selection >= 1 && selection <= parentCourses.size()) {
                        admin.deleteParentCourse(parentCourses.get(selection - 1));
                        System.out.println("Parent course deleted successfully!");
                        Pause();
                        return;
                    } else if (selection == parentCourses.size() + 1) {
                        return;
                    } else {
                        System.out.println("Error: Incorrect choice, please try again");
                    }
                } catch (Exception e) {
                    System.out.println("Error: enter an actual number");
                    input.next(); //Disregarding the entered letter
                }
            }
        } else {
            System.out.println("No parent courses to delete");
            Pause();
        }
    }

    public static void listParentCourses() {
        clearConsole();
        ArrayList<String> parentCourses = Global.getDirectoryList(Global.ParentCourseFolder);
        System.out.println("Parent courses: " + parentCourses.size());
        for (String parentCourseCode : parentCourses) {
            ParentCourse parentCourse = new ParentCourse(parentCourseCode);
            System.out.println(parentCourse.toString());
        }
        Pause();
    }

    public static void instructorMenu() {
        boolean clear = true;
        while (true) {
            if (clear) {
                clearConsole();
                System.out.println("Manage instructors");
                printList("Add an instructor", "Update an instructor", "Delete an instructor",
                        "View instructors", "Main panel");
            }
            System.out.print("Enter your selection: ");
            try {
                int selection = input.nextInt();
                switch (selection) {
                    case 1:
                        createInstructor();
                        clear = true;
                        break;
                    case 2:
                        modifyInstructor();
                        clear = true;
                        break;
                    case 3:
                        deleteInstructor();
                        clear = true;
                        break;
                    case 4:
                        listInstructors();
                        Pause();
                        clear = true;
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Error: Incorrect choice, please try again");
                        clear = false;
                }
            } catch (Exception e) {
                System.out.println("Error: enter an actual number");
                input.next(); //Disregarding the entered letter
                clear = false;
            }
        }
    }

    public static void createInstructor() {
        clearConsole();
        System.out.println("Add an instructor: ");
        boolean error = false;
        String username, password, name, phoneNumber;
        int salary = 0;
        ArrayList<String> existingInstructors = Global.getUsernameList(Global.InstructorLogin);
        //Get username
        do {
            System.out.print("Enter the instructor's username: ");
            username = input.next();
            if (existingInstructors.contains(username.toLowerCase())) {
                System.out.println("Error: This instructor already exists.");
                error = true;
            } else {
                error = false;
            }
        } while (error);
        //Get password
        System.out.print("Enter the instructor's password: ");
        password = input.next();
        input.nextLine();
        //Get name
        do {
            System.out.print("Enter the instructor's name: ");
            name = input.nextLine();
            if (name.isEmpty()) {
                System.out.println("Error: No name was given for this instructor, please try again");
                error = true;
            } else {
                error = false;
            }
        } while (error);
        //Get phonenumber
        do {
            System.out.print("Enter the instructor's phone number: ");
            phoneNumber = input.next();
            if (!isInteger(phoneNumber)) {
                error = true;
            } else {
                error = false;
            }
        } while (error);
        //Get salary
        do {
            try {
                System.out.print("Enter the instructor's salary: ");
                salary = input.nextInt();
                if (salary < 0) {
                    System.out.println("Error: the salary couldn't be a negative number");
                    error = true;
                } else {
                    error = false;
                }
            } catch (Exception e) {
                System.out.println("Error: enter an actual salary number");
                input.next(); //Disregarding the entered letter
                error = true;
            }
        } while (error);
        admin.addInstructor(username.toLowerCase(), password, name, phoneNumber, salary + "");
        System.out.println("Instructor successfully added!");
        Pause();
    }

    public static void modifyInstructor() {
        ArrayList<String> existingInstructors = Global.getUsernameList(Global.InstructorLogin);
        boolean clear = true;
        if (existingInstructors.size() > 0) {
            while (true) {
                if (clear) {
                    clearConsole();
                    System.out.println("Update an instructor: ");
                    System.out.println("Available instructors: ");
                    printList(existingInstructors);
                    System.out.println(existingInstructors.size() + 1 + " - Main panel");
                }
                System.out.print("Enter your selection: ");
                try
                {
                    int selection = input.nextInt();
                    if(selection >=1 && selection <= existingInstructors.size())
                    {
                        modifyInstructorDetails(existingInstructors.get(selection-1));
                        clear = true;
                    }
                    else if(selection == existingInstructors.size()+1) break;
                    else {
                        System.out.println("Incorrect choice, please try again");
                        clear = false;
                    }
                }
                catch (Exception e)
                {
                    System.out.println("Error: enter an actual number");
                    input.next();
                    clear = false;
                }
            }
        } else {
            clearConsole();
            System.out.println("Update an instructor: ");
            System.out.println("There are 0 instructors");
            Pause();
        }
    }

    public static void modifyInstructorDetails(String instructorUsername) {
        Instructor currentInstructor = new Instructor(instructorUsername);
        boolean clear = true;
        while (true) {
            if (clear) {
                clearConsole();
                System.out.println("Update " + currentInstructor.getName() + ": ");
                System.out.println("Available details: ");
                printList("Name: " + currentInstructor.getName(), "Phone number: " + currentInstructor.getPhoneNumber(),
                        "Salary: " + currentInstructor.getSalary(), "Main panel");
            }
            System.out.print("Enter your selection: ");
            try {
                int selection = input.nextInt();
                switch (selection) {
                    case 1:
                        System.out.print("Enter a new name: ");
                        input.nextLine();
                        String newName = input.nextLine();
                        if (newName.isEmpty()) {
                            System.out.println("Error: empty name");
                            Pause();
                        } else currentInstructor.setName(newName);
                        clear = true;
                        break;
                    case 2:
                        System.out.print("Enter a new phone number: ");
                        input.nextLine();
                        String newPhone = input.nextLine();
                        if (isInteger(newPhone)) {
                            currentInstructor.setPhoneNumber(newPhone);
                        }
                        clear = true;
                        break;
                    case 3:
                        System.out.print("Enter a new salary: ");
                        input.nextLine();
                        String newSalary = input.nextLine();
                        if (isInteger(newSalary)) {
                            currentInstructor.setSalary(newSalary);
                        }
                        clear = true;
                        break;
                    case 4:
                        return;
                    default:
                        System.out.println("Incorrect choice, please try again");
                        clear = false;
                        break;
                }
            } catch (Exception e) {
                System.out.println("Error: enter an actual number");
                input.next();
                clear = false;
            }
        }
    }

    public static void deleteInstructor() {
        ArrayList<String> instructorUsernames = Global.getUsernameList(Global.InstructorLogin);
        clearConsole();
        System.out.println("Delete an instructor: ");
        if (instructorUsernames.size() > 0) {
            System.out.println("Select instructor to delete: ");
            printList(instructorUsernames);
            System.out.println(instructorUsernames.size() + 1 + " - Main panel");
            while (true) {
                System.out.print("Enter your selection: ");
                try {
                    int selection = input.nextInt();
                    if (selection >= 1 && selection <= instructorUsernames.size()) {
                        admin.deleteInstructor(instructorUsernames.get(selection - 1));
                        System.out.println("Instructor deleted successfully!");
                        Pause();
                        return;
                    } else if (selection == instructorUsernames.size() + 1) {
                        return;
                    } else {
                        System.out.println("Error: Incorrect choice, please try again");
                    }
                } catch (Exception e) {
                    System.out.println("Error: enter an actual number");
                    //e.printStackTrace();
                    input.next(); //Disregarding the entered letter
                }
            }
        } else {
            System.out.println("No instructors to delete");
            Pause();
        }
    }

    public static void listInstructors() {
        clearConsole();
        ArrayList<String> instructorList = Global.getUsernameList(Global.InstructorLogin);
        System.out.println("Instructors: " + instructorList.size());
        for (String instructorUsername : instructorList) {
            Instructor listableInstructor = new Instructor(instructorUsername);
            System.out.println(listableInstructor.toString());
        }
    }

    public static void studentMenu() {
        boolean clear = true;
        while (true) {
            if (clear) {
                clearConsole();
                System.out.println("Manage students:");
                printList("Add a student", "Update a student", "Delete a student",
                        "View students", "Main panel");
            }
            System.out.print("Enter your selection: ");
            try {
                int selection = input.nextInt();
                switch (selection) {
                    case 1:
                        createStudent();
                        clear = true;
                        break;
                    case 2:
                        modifyStudent();
                        clear = true;
                        break;
                    case 3:
                        deleteStudent();
                        clear = true;
                        break;
                    case 4:
                        listStudents();
                        Pause();
                        clear = true;
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Error: Incorrect choice, please try again");
                        clear = false;
                }
            } catch (Exception e) {
                System.out.println("Error: enter an actual number");
                input.next(); //Disregarding the entered letter
                clear = false;
            }
        }
    }

    public static void createStudent() {
        boolean error = false;
        String username, password, name, phoneNumber;
        int age = 0;
        ArrayList<String> existingStudents = Global.getUsernameList(Global.StudentLogin);
        clearConsole();
        System.out.println("Add a student: ");
        //Get username
        do {
            System.out.print("Enter the student's username: ");
            username = input.next();
            if (existingStudents.contains(username.toLowerCase())) {
                System.out.println("Error: This student already exists.");
                error = true;
            } else {
                error = false;
            }
        } while (error);
        //Get password
        System.out.print("Enter the student's password: ");
        password = input.next();
        input.nextLine();
        //Get name
        do {
            System.out.print("Enter the student's name: ");
            name = input.nextLine();
            if (name.isEmpty()) {
                System.out.println("Error: No name was given for this student, please try again");
                error = true;
            } else {
                error = false;
            }
        } while (error);
        //Get phonenumber

        do {
            System.out.print("Enter the student's phone number: ");
            phoneNumber = input.next();
            if (!isInteger(phoneNumber)) {
                error = true;
                input.nextLine();
            } else {
                error = false;
            }
        }while (error);
        //Get age
        do {
            try {
                System.out.print("Enter the student's age: ");
                age = input.nextInt();
                if (age <= 0) {
                    System.out.println("Error: invalid age number");
                    error = true;
                } else {
                    error = false;
                }
            } catch (Exception e) {
                System.out.println("Error: enter an actual age");
                input.next(); //Disregarding the entered letter
                error = true;
            }
        } while (error);
        admin.addStudent(username.toLowerCase(), password, name, phoneNumber, age + "");
        System.out.println("Student successfully added!");
        Pause();
    }

    public static void modifyStudent() {
        ArrayList<String> existingStudents = Global.getUsernameList(Global.StudentLogin);
        clearConsole();
        boolean clear = true;
        if (existingStudents.size() > 0) {
            while (true) {
                if (clear) {
                    clearConsole();
                    System.out.println("Available students: ");
                    printList(existingStudents);
                    System.out.println(existingStudents.size() + 1 + " - Main panel");
                }
                System.out.print("Enter your selection: ");
                try
                {
                    int selection = input.nextInt();
                    if(selection >=1 && selection <= existingStudents.size())
                    {
                        modifyStudentDetails(existingStudents.get(selection-1));
                        clear = true;
                    }
                    else if(selection == existingStudents.size()+1) break;
                    else {
                        System.out.println("Incorrect choice, please try again");
                        clear = false;
                    }
                }
                catch (Exception e)
                {
                    System.out.println("Error: enter an actual number");
                    input.next();
                    clear = false;
                }
            }
        } else {
            System.out.println("There are 0 students");
            Pause();
        }
    }

    public static void modifyStudentDetails(String studentUsername) {
        Student currentStudent = new Student(studentUsername);
        boolean clear = true;
        while (true) {
            if (clear) {
                clearConsole();
                System.out.println("Update " + currentStudent.getName() + ": ");
                System.out.println("Available details: ");
                printList("Name: " + currentStudent.getName(), "Age: " + currentStudent.getAge(),
                        "Phone number: " + currentStudent.getPhone(), "Main panel");
            }
            System.out.print("Enter your selection: ");
            try {
                int selection = input.nextInt();
                switch (selection) {
                    case 1:
                        System.out.print("Enter a new name: ");
                        input.nextLine();
                        String newName = input.nextLine();
                        if (newName.isEmpty()) {
                            System.out.println("Error: empty name");
                            Pause();
                        } else currentStudent.setName(newName);
                        clear = true;
                        break;
                    case 2:
                        System.out.print("Enter a new age: ");
                        input.nextLine();
                        String newAge = input.nextLine();
                        if (isInteger(newAge)) {
                            currentStudent.setAge(newAge);
                        }
                        clear = true;
                        break;
                    case 3:
                        System.out.print("Enter a new phone number: ");
                        input.nextLine();
                        String newPhone = input.nextLine();
                        if (isInteger(newPhone)) {
                            currentStudent.setPhone(newPhone);
                        }
                        clear = true;
                        break;
                    case 4:
                        return;
                    default:
                        System.out.println("Incorrect choice, please try again");
                        clear = false;
                        break;
                }
            } catch (Exception e) {
                System.out.println("Error: enter an actual number");
                input.next();
                clear = false;
            }
        }
    }

    public static void deleteStudent() {
        ArrayList<String> studentList = Global.getUsernameList(Global.StudentLogin);
        clearConsole();
        System.out.println("Delete an student: ");
        if (studentList.size() > 0) {
            System.out.println("Select student to delete: ");
            printList(studentList);
            System.out.println(studentList.size() + 1 + " - Main panel");
            while (true) {
                System.out.print("Enter your selection: ");
                try {
                    int selection = input.nextInt();
                    if (selection >= 1 && selection <= studentList.size()) {
                        admin.deleteStudent(studentList.get(selection - 1));
                        System.out.println("Student deleted successfully!");
                        Pause();
                        return;
                    } else if (selection == studentList.size() + 1) {
                        return;
                    } else {
                        System.out.println("Error: Incorrect choice, please try again");
                    }
                } catch (Exception e) {
                    System.out.println("Error: enter an actual number");
                    input.next(); //Disregarding the entered letter
                }
            }
        } else {
            System.out.println("No students to delete");
            Pause();
        }
    }

    public static void listStudents() {
        clearConsole();
        ArrayList<String> studentList = Global.getUsernameList(Global.StudentLogin);
        System.out.println("Students: " + studentList.size());
        for (String studentUsername : studentList) {
            Student listableStudent = new Student(studentUsername);
            System.out.println(listableStudent.toString());
        }
    }

    public static void createCourse() {
        ArrayList<String> existingCourses = Global.getDirectoryList(Global.CourseFolder);
        ArrayList<String> parentCourses = Global.getDirectoryList(Global.ParentCourseFolder);
        ArrayList<String> availableInstructors = Global.getUsernameList(Global.InstructorLogin);
        ArrayList<String> existingStudents = Global.getUsernameList(Global.StudentLogin);

        clearConsole();

        if (parentCourses.size() == 0 || availableInstructors.size() == 0 || existingStudents.size() == 0) {
            System.out.println("You cannot create a course due to a lack in " +
                    "parent courses, instructors or students");
            Pause();
            return;
        }

        boolean error = false;
        String ID, name, parentCourseCode, instructorUsername, room, price;
        int days = 0, grade = 0;
        Date startDate = new Date(), endDate = new Date();

        //Step 1 - Set course ID, compare to existing courses
        System.out.println("Create a course");
        do {
            System.out.print("Enter course ID: ");
            ID = input.next();
            if (existingCourses.contains(ID.toUpperCase())) {
                System.out.println("Error: This course already exists.");
                error = true;
            } else {
                error = false;
                input.nextLine();
            }
        } while (error);

        //Step 2 - Set course name
        clearConsole();
        System.out.println("Create a course");
        System.out.println("Course ID: " + ID);
        do {
            System.out.print("Enter course name: ");
            name = input.nextLine();
            if (name.isEmpty()) {
                System.out.println("Error: Empty course name");
                error = true;
            } else {
                error = false;
            }
        } while (error);

        //Step 3 - Select from parent courses
        clearConsole();
        System.out.println("Create a course");
        System.out.println("Course ID: " + ID);
        System.out.println("Course name: " + name);
        System.out.println("Select parent course: ");
        printList(parentCourses);
        while (true) {
            System.out.print("Enter your selection: ");
            try {
                int selection = input.nextInt();
                if (selection >= 1 && selection <= parentCourses.size()) {
                    parentCourseCode = parentCourses.get(selection - 1);
                    break;
                } else {
                    System.out.println("Error: Incorrect choice, please try again");
                }
            } catch (Exception e) {
                System.out.println("Error: enter an actual number");
                input.next(); //Disregarding the entered letter
            }
        }

        //Step 4 - Select from instructors
        clearConsole();
        System.out.println("Create a course");
        System.out.println("Course ID: " + ID);
        System.out.println("Course name: " + name);
        System.out.println("Parent course: " + parentCourseCode);
        System.out.println("Select instructor: ");
        printList(availableInstructors);
        while (true) {
            System.out.print("Enter your selection: ");
            try {
                int selection = input.nextInt();
                if (selection >= 1 && selection <= availableInstructors.size()) {
                    instructorUsername = availableInstructors.get(selection - 1);
                    break;
                } else {
                    System.out.println("Error: Incorrect choice, please try again");
                }
            } catch (Exception e) {
                System.out.println("Error: enter an actual number");
                input.next(); //Disregarding the entered letter
            }
        }

        //Step 5 - Set room
        clearConsole();
        System.out.println("Create a course");
        System.out.println("Course ID: " + ID);
        System.out.println("Course name: " + name);
        System.out.println("Parent course: " + parentCourseCode);
        System.out.println("Instructor: " + instructorUsername);
        System.out.print("Enter room: ");
        room = input.next();

        //Step 6 - Set price
        clearConsole();
        System.out.println("Create a course");
        System.out.println("Course ID: " + ID);
        System.out.println("Course name: " + name);
        System.out.println("Parent course: " + parentCourseCode);
        System.out.println("Instructor: " + instructorUsername);
        System.out.println("Room: " + room);
        System.out.print("Enter price: ");
        price = input.next();

        //Step 7 - Set days
        clearConsole();
        System.out.println("Create a course");
        System.out.println("Course ID: " + ID);
        System.out.println("Course name: " + name);
        System.out.println("Parent course: " + parentCourseCode);
        System.out.println("Instructor: " + instructorUsername);
        System.out.println("Room: " + room);
        System.out.println("Price: " + price);
        do {
            try {
                System.out.print("Enter course days: ");
                days = input.nextInt();
                if (days < 1) {
                    System.out.println("Error: days should be greater than 0");
                    error = true;
                } else {
                    error = false;
                }
            } catch (Exception e) {
                System.out.println("Error: enter an actual number");
                input.next(); //Disregarding the entered letter
                error = true;
            }
        } while (error);

        //Step 8 - Set grades
        clearConsole();
        System.out.println("Create a course");
        System.out.println("Course ID: " + ID);
        System.out.println("Course name: " + name);
        System.out.println("Parent course: " + parentCourseCode);
        System.out.println("Instructor: " + instructorUsername);
        System.out.println("Room: " + room);
        System.out.println("Price: " + price);
        System.out.println("Days: " + days);
        do {
            try {
                System.out.print("Enter maximum grade: ");
                grade = input.nextInt();
                if (grade <= 0) {
                    System.out.println("Error: grade should be greater than 0");
                    error = true;
                } else {
                    error = false;
                }
            } catch (Exception e) {
                System.out.println("Error: enter an actual number");
                input.next(); //Disregarding the entered letter
                error = true;
            }
        } while (error);

        //Step 9 - Set start date
        clearConsole();
        System.out.println("Create a course");
        System.out.println("Course ID: " + ID);
        System.out.println("Course name: " + name);
        System.out.println("Parent course: " + parentCourseCode);
        System.out.println("Instructor: " + instructorUsername);
        System.out.println("Room: " + room);
        System.out.println("Price: " + price);
        System.out.println("Days: " + days);
        System.out.println("Max. grade: " + grade);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        do {
            System.out.print("Enter starting date (in a dd-mm-yyyy format): ");
            String date = input.next();
            if (!date.matches("\\d{2}-\\d{2}-\\d{4}")) {
                System.out.println("Error: the date should follow the format");
                error = true;
            } else {
                error = false;
                try {
                    startDate = format.parse(date);
                } catch (ParseException e) {
                    System.out.println(e.getMessage());
                }
            }
        } while (error);

        //Step 10 - Set end date
        clearConsole();
        System.out.println("Create a course");
        System.out.println("Course ID: " + ID);
        System.out.println("Course name: " + name);
        System.out.println("Parent course: " + parentCourseCode);
        System.out.println("Instructor: " + instructorUsername);
        System.out.println("Room: " + room);
        System.out.println("Price: " + price);
        System.out.println("Days: " + days);
        System.out.println("Max. grade: " + grade);
        System.out.println("Start date: " + format.format(startDate));
        //1- Get start date + days
        long dateSum = startDate.getTime() + (days * 24 * 60 * 60 * 1000);
        //2- Convert summation to days
        endDate = new Date(dateSum);

        //Step 11 - Select students
        ArrayList<String> assignedStudents = new ArrayList<>();

        while (true) {
            if (existingStudents.size() > 0) {
                clearConsole();
                System.out.println("Create a course");
                System.out.println("Course ID: " + ID);
                System.out.println("Course name: " + name);
                System.out.println("Parent course: " + parentCourseCode);
                System.out.println("Instructor: " + instructorUsername);
                System.out.println("Room: " + room);
                System.out.println("Price: " + price);
                System.out.println("Days: " + days);
                System.out.println("Max. grade: " + grade);
                System.out.println("Start date: " + format.format(startDate));
                System.out.println("End date: " + format.format(endDate));
                System.out.println("Select students: ");
                printList(existingStudents);
                System.out.println(existingStudents.size() + 1 + " - End selection");
                System.out.println("Currently selected students: " + String.valueOf(assignedStudents));
                System.out.print("Enter your selection: ");
                try {
                    int selection = input.nextInt();
                    if (selection >= 1 && selection <= existingStudents.size()) {
                        //Add a student to the assigned list, remove from the existing list
                        assignedStudents.add(existingStudents.get(selection - 1));
                        existingStudents.remove(selection - 1);
                    } else if (selection == existingStudents.size() + 1) {
                        if (assignedStudents.size() != 0) {
                            break;
                        } else {
                            System.out.println("Error: Cannot create course with zero students");
                            Pause();
                        }
                    } else {
                        System.out.println("Error: Incorrect choice, please try again");
                        Pause();
                    }
                } catch (Exception e) {
                    System.out.println("Error: enter an actual number");
                    input.next(); //Disregarding the entered letter
                    Pause();
                }
            } else {
                System.out.println("All students are registered in this course");
                Pause();
                break;
            }
        }

        //Step 12 - Create course
        admin.createCourse(ID.toUpperCase(), name, parentCourseCode, instructorUsername, room, price, days, grade, startDate, endDate, assignedStudents);
        System.out.println("Course successfully created!");
        Pause();
    }

    public static void createReport() {
        String show;
        boolean clear = true;
        while (true) {
            if (clear) {
                clearConsole();
                System.out.println("Create a report");
                System.out.println("Select report type: ");
                printList("Courses near to start", "Courses near to end", "Return to main panel");
            }
            System.out.print("Enter your selection: ");
            try {
                int selection = input.nextInt();
                switch (selection) {
                    case 1:
                        admin.createReport(true);
                        System.out.println("The report is saved in Reports/starting.txt");
                        System.out.println("Press S if you wish to read it");
                        input.nextLine();
                        show = input.nextLine();
                        if (show.equalsIgnoreCase("S")) {
                            admin.viewReport(true);
                        }
                        Pause();
                        return;
                    case 2:
                        admin.createReport(false);
                        System.out.println("The report is saved in Reports/ending.txt");
                        System.out.println("Press S if you wish to read it");
                        input.nextLine();
                        show = input.nextLine();
                        if (show.equalsIgnoreCase("S")) {
                            admin.viewReport(false);
                        }
                        Pause();
                        return;
                    case 3:
                        return;
                    default:
                        System.out.println("Error: Incorrect choice, please try again");
                        clear = false;
                }
            } catch (Exception e) {
                System.out.println("Error: enter an actual number");
                input.next(); //Disregarding the entered letter
                clear = false;
            }
        }
    }

    //Instructor methods
    public static void instructorPanel()
    {
        boolean clear = true;
        while (true) {
            if (clear) {
                clearConsole();
                System.out.println("Welcome, " + instructor.getName());
                printList("Publish grades", "View courses", "View survey", "Log out");
            }
            System.out.print("Enter your selection: ");
            try {
                int selection = input.nextInt();
                switch (selection) {
                    case 1:
                        publishGrades();
                        clear = true;
                        break;
                    case 2:
                        viewCourses();
                        clear = true;
                        break;
                    case 3:
                        viewSurvey();
                        clear = true;
                        break;
                    case 4:
                        return;
                    default:
                        System.out.println("Error: Incorrect choice, please try again");
                        clear = false;
                }
            } catch (Exception e) {
                System.out.println("Error: enter an actual number");
                input.next(); //Disregarding the entered letter
                clear = false;
            }
        }
    }

    public static void publishGrades()
    {
        ArrayList<String> courses = instructor.getCourseID();
        //list courses
        if(courses.isEmpty())
        {
            clearConsole();
            System.out.println("You have zero courses");
            Pause();
        }
        else
        {
            boolean clear = true;
            while (true)
            {
                if (clear) {
                    clearConsole();
                    System.out.println("Available courses: ");
                    printList(courses);
                    System.out.println(courses.size() + 1 + " - Main panel");
                }
                System.out.print("Enter your selection: ");

                try
                {
                    int selection = input.nextInt();
                    if(selection >=1 && selection <=courses.size())
                    {
                        setGrades(courses.get(selection-1));
                        clear = true;
                    }
                    else if(selection ==instructor.getCourseID().size()+1) break;
                    else {
                        System.out.println("Incorrect choice, please try again");
                        clear = false;
                    }
                }
                catch (Exception e)
                {
                    System.out.println("Error: enter an actual number");
                    input.next();
                    clear = false;
                }

            }
        }
    }

    public static void setGrades(String courseID)
    {
        Course course = new Course(courseID);
        clearConsole();
        System.out.println("Setting grades for " + courseID + ": ");
        for(String student : course.getStudentUsernames())
        {
            boolean errComp = false;
            while (!errComp)
            {
                try {
                    System.out.print("Set grade for " + student + ": ");
                    int grade = input.nextInt();
                    if(grade >=0 && grade<= course.getGrade()) {
                        instructor.setGrade(courseID,student,grade+"");
                        errComp = true;
                    }
                    else
                    {
                        System.out.println("Grade is not in specified range ");
                    }

                }
                catch (Exception e)
                {
                    System.out.println("Error: enter an actual number");
                    input.next();
                }
            }
        }
        System.out.println("All grades are set and published!");
        Pause();

    }

    public static void viewCourses()
    {
        ArrayList<String> courseIDs = instructor.getCourseID();
        if(courseIDs.isEmpty())
        {
            clearConsole();
            System.out.println("You have zero courses.");
        }
        else
        {
            clearConsole();
            System.out.println("Courses number : "+courseIDs.size());
            for(String id : courseIDs)
            {
                Course course = new Course(id);
                System.out.println(course.toString());
            }
        }
        Pause();
    }

    public static void viewSurvey()
    {
        ArrayList<String> courses = instructor.getCourseID();
        if(instructor.getCourseID().isEmpty())
        {
            clearConsole();
            System.out.println("You have zero courses");
            Pause();
        }
        else
        {
            boolean clear = true;
            while (true)
            {
                if (clear) {
                    clearConsole();
                    System.out.println("Available courses: ");
                    printList(courses);
                    System.out.println(courses.size() + 1 + " - Main panel");
                }
                System.out.print("Enter your selection: ");
                try
                {
                    int selection = input.nextInt();
                    if(selection >=1 && selection <=instructor.getCourseID().size())
                    {
                        clearConsole();
                        instructor.readSurvey(instructor.getCourseID().get(selection-1));
                        Pause();
                        clear = true;
                    }
                    else if(selection ==instructor.getCourseID().size()+1) break;
                    else {
                        System.out.println("Incorrect choice, please try again");
                        clear = false;
                    }
                }
                catch (Exception e)
                {
                    System.out.println("Error: enter an actual number");
                    input.next();
                    clear = false;
                }

            }
        }

    }

    //Start of student panel methods
    public static void studentPanel() {
        boolean clear = true;
        while (true) {
            if (clear) {
                clearConsole();
                System.out.println("Welcome, " + student.getName());
                printList("View grades", "View courses", "View all courses", "Create survey", "Update information", "Log out");
            }
            System.out.print("Enter your selection: ");
            try {
                int selection = input.nextInt();
                clearConsole();
                switch (selection) {
                    case 1:
                        showGrades();
                        Pause();
                        clear = true;
                        break;
                    case 2:
                        listCourses();
                        Pause();
                        clear = true;
                        break;
                    case 3:
                        listAllCourses();
                        Pause();
                        clear = true;
                        break;
                    case 4:
                        createSurvey();
                        clear = true;
                        break;
                    case 5:
                        updateStudent();
                        clear = true;
                        break;
                    case 6:
                        return;
                    default:
                        System.out.println("Incorrect choice, please try again");
                        clear = false;
                        break;
                }
            } catch (Exception e) {
                System.out.println("Error: enter an actual number");
                input.next();
                clear = false;
            }
        }
    }

    public static void showGrades() {
        if (!student.getCourses().isEmpty()) {
            System.out.println("Your grades ");
            System.out.println("--------------------");

            for (int i = 0; i < student.getGrades().size(); i++) {
                if (!student.getGrades().get(i).equals(""))
                    System.out.println(student.getCourses().get(i) + ": " + student.getGrades().get(i));
                else System.out.println(student.getCourses().get(i) + ": " + "unset grade");
            }
        } else System.out.println(student.getName() + " didn't register any course.");
    }

    public static boolean listCourses() {
        if (!student.getCourses().isEmpty()) {
            System.out.println("Your courses ");
            System.out.println("--------------------");
            int index = 1;
            for (String course : student.getCourses()) {
                System.out.println(index + "- " + course);
                index++;
            }
        } else
        {
            System.out.println(student.getName() + " didn't register any course.");
            return false;
        }
        return true;
    }

    public static void listAllCourses() {
        ArrayList<String> courses = Global.getDirectoryList(Global.CourseFolder);
        System.out.println("All courses: " + courses.size());
        for (String courseID: courses) {
            Course course = new Course(courseID);
            System.out.println(course.toString());
        }
    }

    public static void createSurvey() {
        if (student.getCourses().size() == 0) {
            clearConsole();
            System.out.println(student.getName() + " didn't register any course.");
            Pause();
            return;
        }
        while (true) {
            clearConsole();
            if (listCourses())
            {
                System.out.println(student.getCourses().size() + 1 + "- Main panel");
                System.out.print("Enter your selection: ");
                try {
                    int selection = input.nextInt();
                    if (selection >= 1 && selection <= student.getCourses().size()) {
                        input.nextLine();
                        boolean err = false;
                        String comment;
                        do {
                            System.out.print("Enter your comment: ");
                            comment = input.nextLine();
                            if (comment.isEmpty()) {
                                System.out.println("Error: empty comment");
                                err = true;
                            } else {
                                err = false;
                            }
                        } while (err);
                        student.createSurvey(student.getCourses().get(selection - 1), comment);
                        System.out.println("Survey set successfully!");
                        Pause();

                    } else if (selection == student.getCourses().size() + 1) {
                        return;
                    } else {
                        System.out.println("Incorrect choice, please try again");
                        Pause();
                    }
                } catch (Exception e) {
                    System.out.println("Error: enter an actual number");
                    input.next();
                    Pause();
                }
            }
        }

    }

    public static void updateStudent() {
        boolean err = true;
        String newInfo;
        do {
            clearConsole();
            System.out.println("Update panel");
            System.out.println("--------------------");
            printList("Name: " + student.getName(), "Age: " + student.getAge(), "Phone number: " + student.getPhone(),
                    "Main panel");
            System.out.print("Enter your selection: ");
            try {
                int selection = input.nextInt();
                switch (selection) {
                    case 1:
                        System.out.print("Enter a new name: ");
                        input.nextLine();
                        newInfo = input.nextLine();
                        if (!newInfo.isEmpty()) {
                            student.setName(newInfo);
                        } else {
                            System.out.println("Error: empty name");
                            Pause();
                        }
                        break;
                    case 2:
                        System.out.print("Enter a new age : ");
                        input.nextLine();
                        newInfo = input.nextLine();
                        if(isInteger(newInfo))
                            student.setAge(newInfo);
                        break;
                    case 3:
                        System.out.print("Enter an new phone number : ");
                        input.nextLine();
                        newInfo = input.nextLine();
                        if(isInteger(newInfo))
                            student.setPhone(newInfo);
                        break;
                    case 4:
                        err = false;
                        break;
                    default:
                        System.out.println("Incorrect choice, please try again");
                        Pause();
                        break;
                }
            } catch (Exception e) {
                System.out.println("Error: enter an actual number");
                input.next();
                Pause();
            }
        } while (err);
    }

    //Helper functions
    public static void printList(String... list) {
        for (int i = 0; i < list.length; i++) {
            System.out.println(i+1 + " - " + list[i]);
        }
    }

    public static void printList(ArrayList<String> list) {
        for (int i = 0; i < list.size(); i++) {
            System.out.println(i+1 + " - " + list.get(i));
        }
    }

    public static void Pause() {
        Scanner input = new Scanner(System.in);
        System.out.print("Press Enter to return ");
        try {
            input.nextLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clearConsole()  {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }
            else {
                new ProcessBuilder("cmd", "/c", "clear").inheritIO().start().waitFor();
            }
        } catch (IOException | InterruptedException ignored) {}
    }

    public static boolean isInteger(String input)
    {
        try
        {
            Integer.parseInt(input);
            return true;
        }
        catch (Exception e)
        {
            System.out.println("Error: Not a valid input.");
            Pause();
            return false;
        }
    }

    public static String readPassword()
    {
         try
         {
             if(console != null)
             {
                 char[] pass = console.readPassword("Password: ");
                 return new String(pass);
             }
         } catch (Exception e) {};
        return "NAN";
    }
}