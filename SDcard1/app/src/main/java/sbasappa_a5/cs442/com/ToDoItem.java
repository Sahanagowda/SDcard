package sbasappa_a5.cs442.com;

import android.text.format.Time;

import java.text.SimpleDateFormat;
import java.util.Date;



public class ToDoItem {

  String task;
  Date created;
  int counter;
  Time today;
  String dateView = "";

  public int getNum() {
    return counter;
  }

  public String getTask() {
    return task;
  }

  public boolean isGetCreated() {
    return created!=null;
  }

  public Date getCreated() {
    return created;
  }
  public String getTimeString() {return today.format("%k:%M:%S");}

  public String getDateView() {
    return dateView;
  }

  public boolean isDateView() {
    return dateView!=null;
  }

  public ToDoItem(String a, String b, String c) {
    counter = Integer.parseInt(a);
    this.task = b;
    this.dateView = c;
  }

  public ToDoItem(String _task, int numLast) {
    this(_task, new Date(java.lang.System.currentTimeMillis()), numLast);
  }

  public ToDoItem(String _task, Date _created, int numLast) {
    task = _task;
    created = _created;
    counter = numLast;

    today = new Time(Time.getCurrentTimezone());
    today.setToNow();

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
    String dateString = sdf.format(created);
    dateView = dateString + " " + getTimeString();
  }

  @Override
  public String toString() {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
    String dateString = sdf.format(created);
    String timeString = today.format("%k:%M:%S");
    return "(" + timeString +") " + task;
  }
}