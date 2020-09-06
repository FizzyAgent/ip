package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import task.Deadline;
import task.Event;
import task.Task;
import task.ToDo;

/**
 * Data reading and storage manager.
 */
public class Storage {

    private String filePath;

    /**
     * Creates new storage object.
     *
     * @param filePath Location of file for data reading and writing.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Reads the data file for stored tasks.
     *
     * @return List of tasks stored in data file.
     */
    public List<Task> readFile() {

        File file = new File(filePath);
        List<Task> output = new ArrayList<Task>();

        try {

            if (!file.exists()) {
                return output;
            }
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line;
            while ((line = br.readLine()) != null) {

                String[] lineArr = line.split("\\s(\\|)\\s");

                switch (lineArr[0]) {
                case "T":
                    output.add(new ToDo(lineArr[2]));
                    break;
                case "D":
                    output.add(new Deadline(lineArr[2], lineArr[3], lineArr[4]));
                    break;
                case "E":
                    output.add(new Event(lineArr[2], lineArr[3], lineArr[4]));
                    break;
                default:
                }

                if (lineArr[1].equals("1")) {
                    output.get(output.size() - 1).completeTask();
                }
            }

            br.close();

        } catch (IOException e) {
            System.out.println("Error occurred while reading data");
        }

        return output;
    }

    /**
     * Writes tasks to the data file for storage.
     *
     * @param tasks List of tasks to be stored in data file.
     */
    public void saveFile(List<Task> tasks) {

        File file = new File(filePath);

        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            DateTimeFormatter format = DateTimeFormatter.ofPattern("YYYY-MM-dd");

            tasks.forEach(task -> {
                int completed = task.isCompleted() ? 1 : 0;

                String output = "";

                if (task instanceof ToDo) {

                    output = String.format("T | %d | %s\n", completed, task.getMsg());

                } else if (task instanceof Deadline) {

                    String time = (((Deadline) task).getTime() != null)
                            ? ((Deadline) task).getTime().format(DateTimeFormatter.ofPattern("HH:mm")) : "NA";
                    output = String.format("D | %d | %s | %s | %s\n", completed,
                            task.getMsg(), ((Deadline) task).getDate().format(format),
                            time);

                } else if (task instanceof Event) {

                    String time = (((Event) task).getTime() != null)
                            ? ((Event) task).getTime().format(DateTimeFormatter.ofPattern("HH:mm")) : "NA";
                    output = String.format("E | %d | %s | %s | %s\n", completed,
                            task.getMsg(), ((Event) task).getDate().format(format),
                            time);
                }

                try {
                    bw.write(output);
                } catch (IOException e) {
                    System.out.println("Error occurred while saving data");
                }
            });

            bw.close();

        } catch (IOException e) {
            System.out.println("Error occurred while saving data");
        }
    }
}
