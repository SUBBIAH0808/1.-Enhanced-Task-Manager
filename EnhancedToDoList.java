package sce_code;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class EnhancedToDoList {
    private static final String FILE_NAME = "tasks.txt";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static ArrayList<Task> tasks = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        loadTasks();
        while (true) {
            System.out.println("\n1. Add Task");
            System.out.println("2. View Tasks");
            System.out.println("3. Update Task");
            System.out.println("4. Delete Task");
            System.out.println("5. View Tasks by Deadline");
            System.out.println("6. Search Tasks");
            System.out.println("7. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addTask();
                    break;
                case 2:
                    viewTasks();
                    break;
                case 3:
                    updateTask();
                    break;
                case 4:
                    deleteTask();
                    break;
                case 5:
                    viewTasksByDeadline();
                    break;
                case 6:
                    searchTasks();
                    break;
                case 7:
                    saveTasks();
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void addTask() {
        System.out.print("Enter task description: ");
        String description = scanner.nextLine();
        System.out.print("Enter priority (high/medium/low): ");
        String priority = scanner.nextLine();
        System.out.print("Enter category: ");
        String category = scanner.nextLine();
        System.out.print("Enter deadline (YYYY-MM-DD): ");
        LocalDate deadline = LocalDate.parse(scanner.nextLine(), DATE_FORMATTER);
        System.out.print("Enter status (pending/in progress/completed): ");
        String status = scanner.nextLine();
        tasks.add(new Task(description, priority, category, deadline, status));
    }

    private static void viewTasks() {
        // Define priority levels with correct order
        final List<String> PRIORITY_ORDER = Arrays.asList("high", "medium", "low");

        // Sort tasks by priority
        tasks.sort((t1, t2) -> {
            int priority1 = PRIORITY_ORDER.indexOf(t1.getPriority());
            int priority2 = PRIORITY_ORDER.indexOf(t2.getPriority());
            return Integer.compare(priority1, priority2);
        });

        System.out.println("Tasks:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
    }

    private static void updateTask() {
        viewTasks();
        System.out.print("Enter the number of the task to update: ");
        int index = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        if (index > 0 && index <= tasks.size()) {
            Task task = tasks.get(index - 1);
            System.out.print("Enter new description (or press Enter to keep current): ");
            String description = scanner.nextLine();
            if (!description.isEmpty()) {
                task.setDescription(description);
            }
            System.out.print("Enter new priority (or press Enter to keep current): ");
            String priority = scanner.nextLine();
            if (!priority.isEmpty()) {
                task.setPriority(priority);
            }
            System.out.print("Enter new category (or press Enter to keep current): ");
            String category = scanner.nextLine();
            if (!category.isEmpty()) {
                task.setCategory(category);
            }
            System.out.print("Enter new deadline (YYYY-MM-DD) (or press Enter to keep current): ");
            String deadlineStr = scanner.nextLine();
            if (!deadlineStr.isEmpty()) {
                task.setDeadline(LocalDate.parse(deadlineStr, DATE_FORMATTER));
            }
            System.out.print("Enter new status (or press Enter to keep current): ");
            String status = scanner.nextLine();
            if (!status.isEmpty()) {
                task.setStatus(status);
            }
        } else {
            System.out.println("Invalid index.");
        }
    }

    private static void deleteTask() {
        viewTasks();
        System.out.print("Enter the number of the task to delete: ");
        int index = scanner.nextInt();
        if (index > 0 && index <= tasks.size()) {
            tasks.remove(index - 1);
            System.out.println("Task deleted.");
        } else {
            System.out.println("Invalid index.");
        }
    }

    private static void viewTasksByDeadline() {
        System.out.print("Enter the deadline to filter by (YYYY-MM-DD): ");
        LocalDate deadline = LocalDate.parse(scanner.nextLine(), DATE_FORMATTER);
        System.out.println("Tasks with deadline on or before " + deadline + ":");
        int taskCount=0;
        for (Task task : tasks) {
        if (task.getDeadline().isBefore(deadline) || task.getDeadline().isEqual(deadline)) {
        		taskCount++;
                System.out.println(task);
            }
        }
        if(taskCount==0) {
        	System.out.println("no tasks found");
        }
    }

    private static void searchTasks() {
        System.out.print("Enter search term: ");
        String term = scanner.nextLine().toLowerCase();
        System.out.println("Tasks containing \"" + term + "\":");
        for (Task task : tasks) {
            if (task.getDescription().toLowerCase().contains(term) ||
                task.getCategory().toLowerCase().contains(term) ||
                task.getPriority().toLowerCase().contains(term) ||
                task.getStatus().toLowerCase().contains(term)) {
                System.out.println(task);
            }
        }
    }

    private static void saveTasks() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Task task : tasks) {
                writer.write(task.getDescription() + ";" + task.getPriority() + ";" + task.getCategory() + ";" + task.getDeadline() + ";" + task.getStatus());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("An error occurred while saving tasks.");
        }
    }

    private static void loadTasks() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 5) {
                    tasks.add(new Task(parts[0], parts[1], parts[2], LocalDate.parse(parts[3], DATE_FORMATTER), parts[4]));
                }
            }
        } catch (IOException e) {
            System.out.println(" ");
        }
    }
}

class Task {
    private String description;
    private String priority;
    private String category;
    private LocalDate deadline;
    private String status;

    public Task(String description, String priority, String category, LocalDate deadline, String status) {
        this.description = description;
        this.priority = priority;
        this.category = category;
        this.deadline = deadline;
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return description + " - " + priority + " - " + category + " - " + deadline + " - " + status;
    }
}