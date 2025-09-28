import java.util.*;

// ===== Task Model =====
class Task {
    private String title;
    private String start;
    private String end;
    private String priority;
    private boolean completed;

    public Task(String title, String start, String end, String priority) {
        this.title = title; this.start = start; this.end = end; this.priority = priority;
        this.completed = false;
    }
    public String getTitle() { return title; }
    public String getStart() { return start; }
    public String getEnd() { return end; }
    public String getPriority() { return priority; }
    public boolean isCompleted() { return completed; }
    public void markComplete() { completed = true; }
    public void update(String t, String s, String e, String p) { title=t; start=s; end=e; priority=p; }
    public String toString() {
        return start + "-" + end + " | " + title + " [" + priority + "] " + (completed ? "✓" : "");
    }
}

// ===== Factory Pattern =====
class TaskFactory {
    public static Task create(String title, String start, String end, String priority) {
        return new Task(title, start, end, priority);
    }
}

// ===== Observer Pattern =====
interface Notifier { void notify(String msg); }
class ConsoleNotifier implements Notifier {
    public void notify(String msg) { System.out.println("[NOTIFY] " + msg); }
}

// ===== Singleton Manager =====
class ScheduleManager {
    private static ScheduleManager instance;
    private List<Task> tasks = new ArrayList<>();
    private List<Notifier> notifiers = new ArrayList<>();
    private ScheduleManager() {}
    public static ScheduleManager getInstance() {
        if (instance == null) instance = new ScheduleManager();
        return instance;
    }
    public void addNotifier(Notifier n) { notifiers.add(n); }
    private void notifyAll(String msg) { for (Notifier n : notifiers) n.notify(msg); }

    private boolean overlaps(Task a, Task b) {
        return a.getStart().compareTo(b.getEnd()) < 0 && b.getStart().compareTo(a.getEnd()) < 0;
    }

    public void addTask(Task t) {
        for (Task existing : tasks) {
            if (overlaps(existing, t)) {
                notifyAll("Conflict: " + t.getTitle() + " overlaps with " + existing.getTitle());
                return;
            }
        }
        tasks.add(t);
        notifyAll("Task added: " + t.getTitle());
    }
    public void removeTask(String title) {
        Task t = findTask(title);
        if (t!=null) { tasks.remove(t); notifyAll("Task removed: " + title); }
        else System.out.println("Task not found.");
    }
    public void editTask(String oldTitle, String newTitle, String start, String end, String pr) {
        Task t = findTask(oldTitle);
        if (t!=null) { t.update(newTitle,start,end,pr); notifyAll("Task updated: " + newTitle); }
        else System.out.println("Task not found.");
    }
    public void completeTask(String title) {
        Task t = findTask(title);
        if (t!=null) { t.markComplete(); notifyAll("Task completed: " + title); }
        else System.out.println("Task not found.");
    }
    public void viewTasks() {
        if (tasks.isEmpty()) { System.out.println("No tasks."); return; }
        tasks.sort(Comparator.comparing(Task::getStart));
        for (Task t: tasks) System.out.println(t);
    }
    private Task findTask(String title) {
        for (Task t: tasks) if (t.getTitle().equalsIgnoreCase(title)) return t;
        return null;
    }
}

// ===== Main Program =====
public class AstronautScheduleOrganizer {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ScheduleManager mgr = ScheduleManager.getInstance();
        mgr.addNotifier(new ConsoleNotifier());

        System.out.println("=== Astronaut Daily Schedule Organizer ===");
        while (true) {
            System.out.println("\nCommands: add, remove, edit, complete, view, exit");
            System.out.print("Enter: ");
            String cmd = sc.nextLine().toLowerCase();
            switch (cmd) {
                case "add":
                    System.out.print("Title: "); String title = sc.nextLine();
                    System.out.print("Start (HH:mm): "); String s = sc.nextLine();
                    System.out.print("End (HH:mm): "); String e = sc.nextLine();
                    System.out.print("Priority: "); String p = sc.nextLine();
                    mgr.addTask(TaskFactory.create(title,s,e,p));
                    break;
                case "remove":
                    System.out.print("Title: "); mgr.removeTask(sc.nextLine()); break;
                case "edit":
                    System.out.print("Old title: "); String oldT = sc.nextLine();
                    System.out.print("New title: "); String newT = sc.nextLine();
                    System.out.print("Start: "); String ns = sc.nextLine();
                    System.out.print("End: "); String ne = sc.nextLine();
                    System.out.print("Priority: "); String np = sc.nextLine();
                    mgr.editTask(oldT,newT,ns,ne,np);
                    break;
                case "complete":
                    System.out.print("Title: "); mgr.completeTask(sc.nextLine()); break;
                case "view":
                    mgr.viewTasks(); break;
                case "exit":
                    return;
                default:
                    System.out.println("Invalid command.");
            }
        }
    }
}
