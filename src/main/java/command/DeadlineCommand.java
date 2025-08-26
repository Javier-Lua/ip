package command;

import storage.Storage;
import model.TaskList;
import ui.Ui;
import model.Deadline;
import model.Task;

import java.time.LocalDateTime;

public class DeadlineCommand extends Command {

    private final String desc;
    private final LocalDateTime date;

    public DeadlineCommand(String desc, LocalDateTime date) {
        this.desc = desc;
        this.date = date;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        Task t = new Deadline(this.desc, this.date);
        tasks.add(t);
        storage.saveTasks();
        ui.showAddTask(t, tasks.getCount());
    }

}
