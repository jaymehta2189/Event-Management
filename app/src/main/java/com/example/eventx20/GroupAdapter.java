package com.example.eventx20;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.eventx20.Database.DataModel.Group;
import com.example.eventx20.Database.DataModel.User;
import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

    private List<User> studentsList;
    private Group group;

    public GroupAdapter(List<User> studentsList) {
        this.studentsList = studentsList;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group, parent, false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        User student = studentsList.get(position);

        // Display the group information once
        if (position == 0 && group != null) {
            holder.textViewGroupName.setText(group.getGroupName());
            holder.textViewGroupDetails.setText("Group ID: " + group.getGroupName());
        }

        // Display student information
        holder.textViewStudentName.setText(student.getName());
    }

    @Override
    public int getItemCount() {
        return studentsList.size();
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder {

        TextView textViewGroupName;
        TextView textViewGroupDetails;
        TextView textViewStudentName;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewGroupName = itemView.findViewById(R.id.textViewGroupName);
            textViewGroupDetails = itemView.findViewById(R.id.textViewGroupDetails);
            textViewStudentName = itemView.findViewById(R.id.textViewStudentNames);
        }
    }
}
