package sbasappa_a5.cs442.com;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paad.todolist.R;

import java.util.List;

public class ToDoItemAdapter extends ArrayAdapter<ToDoItem> {

  int resource;

  public ToDoItemAdapter(Context context,
                         int resource,
                         List<ToDoItem> items) {
    super(context, resource, items);
    this.resource = resource;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LinearLayout todoView;
    ToDoItem item = getItem(position);
    String taskString = item.getTask();
    String createdDate = item.getDateView();

    int num = item.getNum();
    if (convertView == null) {
      todoView = new LinearLayout(getContext());
      String inflater = Context.LAYOUT_INFLATER_SERVICE;
      LayoutInflater li;
      li = (LayoutInflater)getContext().getSystemService(inflater);
      li.inflate(resource, todoView, true);
    } else {
      todoView = (LinearLayout) convertView;
    }

    TextView dateView = (TextView)todoView.findViewById(R.id.rowDate);
    TextView taskView = (TextView)todoView.findViewById(R.id.row);
    TextView NumView = (TextView)todoView.findViewById(R.id.rowNum);
    dateView.setText(createdDate);
    taskView.setText(taskString);
    StringBuilder aStr = new StringBuilder();
    aStr.append(num);
    NumView.setText(aStr.toString());

    return todoView;
  }
}