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

    private List<group_list.GroupData> groupDataList;

    public GroupAdapter(List<group_list.GroupData> groupDataList) {
        this.groupDataList = groupDataList;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group, parent, false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        group_list.GroupData groupData = groupDataList.get(position);
        Group group = groupData.getGroup();
        List<User> students = groupData.getStudents();

        // Bind the group data
        holder.textViewGroupName.setText(group.getGroupName());  // Assuming Group has a method getGroupName()
        holder.textViewGroupDetails.setText("Group Name: " + group.getGroupName()); // Assuming Group has a method getGroupId()

        // Display the student names (or any other details)
        StringBuilder studentDetails = new StringBuilder();
        for (User student : students) {
            studentDetails.append(student.getName()).append("\n"); // Assuming User has a method getName()
        }
        holder.textViewStudentNames.setText(studentDetails.toString());
    }

    @Override
    public int getItemCount() {
        return groupDataList.size();
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder {

        TextView textViewGroupName;
        TextView textViewGroupDetails;
        TextView textViewStudentNames;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewGroupName = itemView.findViewById(R.id.textViewGroupName);
            textViewGroupDetails = itemView.findViewById(R.id.textViewGroupDetails);
            textViewStudentNames = itemView.findViewById(R.id.textViewStudentNames);
        }
    }
}
