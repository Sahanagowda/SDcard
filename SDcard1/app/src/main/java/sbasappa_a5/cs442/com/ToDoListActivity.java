package sbasappa_a5.cs442.com;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.paad.todolist.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ToDoListActivity extends Activity implements NewItemFragment.OnNewItemAddedListener {
  private ArrayList<ToDoItem> todoItems;
  private ToDoItemAdapter aa;
  private sbasappa_a5.cs442.com.ToDoListFragment todoListFragment;
  private static int numLast = 0;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Inflate your view
    setContentView(R.layout.main);

    // Get references to the Fragments
    FragmentManager fm = getFragmentManager();
    todoListFragment = (sbasappa_a5.cs442.com.ToDoListFragment) fm.findFragmentById(R.id.TodoListFragment);
    loadActivity();
  }

  public void loadActivity() {
    // Create the array list of to do items
    todoItems = new ArrayList<ToDoItem>();
    SharedPreferences keyValues = getApplicationContext().getSharedPreferences("icons_list", Context.MODE_PRIVATE);
    Map<String, String> toDoList;
    toDoList = (Map<String, String>) keyValues.getAll();
    Set set = toDoList.entrySet();
    Iterator i = set.iterator();
    while (i.hasNext()) {
      Map.Entry e = (Map.Entry) i.next();
      String[] str = e.getValue().toString().split(";");
      ToDoItem tdi = new ToDoItem(str[1].substring(0, str[1].indexOf(".")), str[1], str[2]);
      todoItems.add(0, tdi);
      int numTmp = Integer.parseInt(str[1].substring(0, str[1].indexOf(".")));
      if (numTmp > numLast) {
        numLast = numTmp;
      }
    }

    // Create the array adapter to bind the array to the ListView
    int resID = R.layout.todolist_item;
    aa = new ToDoItemAdapter(this, resID, todoItems);
    // Bind the array adapter to the ListView.
    todoListFragment.setListAdapter(aa);
  }

  public void onNewItemAdded(String newItem) {
    numLast++;
    newItem = numLast + newItem.substring(1);
    ToDoItem newTodoItem = new ToDoItem(newItem, numLast);

    todoItems.add(0, newTodoItem);
    aa.notifyDataSetChanged();

    SharedPreferences keyValues = getApplicationContext().getSharedPreferences("icons_list", Context.MODE_PRIVATE);
    SharedPreferences.Editor keyValuesEditor = keyValues.edit();

    for (int i = 0; i < todoItems.size(); i++) {
      keyValuesEditor.putString(Integer.toString(i), Integer.toString(todoItems.get(i).getNum()) + ";" + todoItems.get(i).getTask() + ";" + todoItems.get(i).getDateView());
    }
    keyValuesEditor.commit();
  }

  /**
   * Called when the user clicks the Reset button
   */
  public void resetInfo(View view) {
    SharedPreferences settings = getApplicationContext().getSharedPreferences("icons_list", Context.MODE_PRIVATE);
    settings.edit().clear().commit();
    numLast = 0;
    todoItems.clear();
    aa.notifyDataSetChanged();
    Toast toast = Toast.makeText(getApplicationContext(), "To Do List Cleared!", Toast.LENGTH_SHORT);
    toast.show();
  }

  /**
   * Called when the user clicks the Export button
   */
  public void saveInfo(View view) {
    if (!isExternalStorageWritable()) {
      Toast toast = Toast.makeText(getApplicationContext(), "External SD Card Not Found!", Toast.LENGTH_SHORT);
      toast.show();
    } else {
      File file = new File(getApplicationContext().getExternalFilesDir(null), "myfile");
      try {
        // get external storage file reference
        FileOutputStream f = new FileOutputStream(file);
        PrintWriter pw = new PrintWriter(f);
        for (int i = 0; i < todoItems.size(); i++) {
          pw.println(Integer.toString(todoItems.get(i).getNum()) + ";" + todoItems.get(i).getTask() + ";" + todoItems.get(i).getDateView());
        }
        pw.flush();
        pw.close();
        f.close();
        Toast toast = Toast.makeText(getApplicationContext(), "Data File Exported Successfully!", Toast.LENGTH_SHORT);
        toast.show();
      } catch (FileNotFoundException e) {
        Toast toast = Toast.makeText(getApplicationContext(), "Data File Export Failed!", Toast.LENGTH_SHORT);
        toast.show();
        e.printStackTrace();
      } catch (IOException e) {
        Toast toast = Toast.makeText(getApplicationContext(), "Data File Export Failed!", Toast.LENGTH_SHORT);
        toast.show();
        e.printStackTrace();
      }
    }
  }

  /**
   * Called when the user clicks the Import button
   */
  public void loadInfo(View view) {
    if (!isExternalStorageWritable()) {
      Toast toast = Toast.makeText(getApplicationContext(), "External SD Card Not Found!", Toast.LENGTH_SHORT);
      toast.show();
    } else {
      //Find the directory for the SD Card using the API
      File file = new File(getApplicationContext().getExternalFilesDir(null), "myfile");
      //Read text from file
      StringBuilder text = new StringBuilder();
      try {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;

        SharedPreferences keyValues = getApplicationContext().getSharedPreferences("icons_list", Context.MODE_PRIVATE);
        keyValues.edit().clear().apply();
        numLast = 0;
        todoItems.clear();
        aa.notifyDataSetChanged();
        SharedPreferences.Editor keyValuesEditor = keyValues.edit();

        while ((line = br.readLine()) != null) {
          String[] str = line.split(";");
          keyValuesEditor.putString(str[0], str[0] + ";" + str[1] + ";" + str[2]);
          numLast++;
        }
        keyValuesEditor.commit();
        br.close();
        aa.notifyDataSetChanged();
        loadActivity();
        if (numLast != 0) {
          Toast toast = Toast.makeText(getApplicationContext(), "Data File Imported Successfully!", Toast.LENGTH_SHORT);
          toast.show();
        } else {
          Toast toast = Toast.makeText(getApplicationContext(), "Data File Empty!", Toast.LENGTH_SHORT);
          toast.show();
        }
      } catch (IOException e) {
        Toast toast = Toast.makeText(getApplicationContext(), "Data File Not Found!", Toast.LENGTH_SHORT);
        toast.show();
      }

    }
  }

  /* Checks if external storage is available for read and write */
  public boolean isExternalStorageWritable() {
    String state = Environment.getExternalStorageState();
    if (Environment.MEDIA_MOUNTED.equals(state)) {
      return true;
    }
    return false;
  }
}