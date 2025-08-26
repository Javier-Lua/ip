package command;

import storage.Storage;
import model.TaskList;
import ui.Ui;
import model.Event;
import model.Task;

import java.time.LocalDateTime;

public class EventCommand extends Command {

    private final String desc;
    private final LocalDateTime from;
    private final LocalDateTime to;

    public EventCommand(String desc, LocalDateTime from, LocalDateTime to) {
        this.desc = desc;
        this.from = from;
        this.to = to;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        Task t = new Event(this.desc, this.from, this.to);
        tasks.add(t);
        storage.saveTasks();
        ui.showAddTask(t, tasks.getCount());
    }

}
