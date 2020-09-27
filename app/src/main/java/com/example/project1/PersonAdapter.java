package com.example.project1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.MyViewHolder> {
    ArrayList<Mail> messages;

    public PersonAdapter(ArrayList<Mail> messages){
        this.messages = messages;
    }

    @NonNull
    @Override
    public PersonAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.mail, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Mail mail = messages.get(position);
        holder.setMessage(mail);

    }

    public interface OnItemClickListener{
        void onItemClick(View v, int position);
    }

    private static OnItemClickListener mListener = null;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, sendId, time;

        public MyViewHolder(View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            sendId=itemView.findViewById(R.id.sendId);
            time=itemView.findViewById(R.id.time);

            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(pos!=RecyclerView.NO_POSITION){
                        if(mListener!=null){
                            mListener.onItemClick(view, pos);
                        }
                    }
                }
            });
        }

        public void setMessage(Mail mail) {
            title.setText(mail.gettitle());
            time.setText(mail.getTime());
            sendId.setText(mail.getSendid());
        }

    }

}
