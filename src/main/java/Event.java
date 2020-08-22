package main.java;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Event extends Task {

    private LocalDate date;
    private LocalTime time;

    public Event(String msg, String date, String time){
        super(msg);
        this.date = LocalDate.parse(date);

        if (!time.equals("") && !time.equals("NA")) {
            this.time = LocalTime.parse(time);
        }
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    @Override
    public String toString(){
        return "[E]" + super.toString() + " (at: " +  date.format(DateTimeFormatter.ofPattern("MMM d yyyy")) +
                ((time != null) ? " " + time.format(DateTimeFormatter.ofPattern("HH:mm")) : "") + ")";
    }
}
