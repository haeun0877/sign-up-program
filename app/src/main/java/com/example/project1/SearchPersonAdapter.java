package com.example.project1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchPersonAdapter extends RecyclerView.Adapter<SearchPersonAdapter.MyViewHolder> {
    ArrayList<Person> person;

    public SearchPersonAdapter(ArrayList<Person> person){
        this.person = person;
    }

    @NonNull
    @Override
    public SearchPersonAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.select, parent, false);
        return new SearchPersonAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchPersonAdapter.MyViewHolder holder, int position) {
        Person person = this.person.get(position);
        holder.setMessage(person);

    }

    public interface OnItemClickListener{
        void onItemClick(View v, int position);
    }

    private static SearchPersonAdapter.OnItemClickListener mListener = null;

    public void setOnItemClickListener(SearchPersonAdapter.OnItemClickListener listener){
        this.mListener = listener;
    }

    @Override
    public int getItemCount() {
        return person.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView receiver;

        public MyViewHolder(View itemView) {
            super(itemView);
            receiver=itemView.findViewById(R.id.receiver);

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

        public void setMessage(Person person) {
            receiver.setText(person.getId()+" ("+person.getName()+")");
        }

    }

}
