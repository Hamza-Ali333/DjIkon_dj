package com.Ikonholdings.ikoniconnects_subscriber.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.Ikonholdings.ikoniconnects_subscriber.R;
import com.Ikonholdings.ikoniconnects_subscriber.ResponseModels.Transaction;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

public class RecyclerTransactionDetail extends RecyclerView.Adapter<RecyclerTransactionDetail.ViewHolder>{

    private List<Transaction> transactionList;

    //view holder class
    public static class ViewHolder extends  RecyclerView.ViewHolder{

        public CircularImageView status;
        public TextView txt_Amount;
        public TextView txt_Date;
        public TextView txt_Status;

        public ViewHolder(View itemView){
            super(itemView);
            status = itemView.findViewById(R.id.img_profile);

            txt_Amount = itemView.findViewById(R.id.txt_User_name);
            txt_Date = itemView.findViewById(R.id.time);
            txt_Status = itemView.findViewById(R.id.status);
        }

    }

//constructor
    public RecyclerTransactionDetail(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
       final Transaction currentItem = transactionList.get(position);

       holder.txt_Amount.setText("$"+String.valueOf(currentItem.getAmount()));
       holder.txt_Date.setText(String.valueOf(currentItem.getCreated_at()));

            switch (currentItem.getStatus()){
                case 0:
                    holder.status.setImageResource(R.drawable.ic_process);
                    holder.txt_Status.setText("In Process");
                    break;
                case 1:
                    holder.status.setImageResource(R.drawable.ic_check);
                    holder.txt_Status.setText("Accepted");
                    break;
                case 2:
                    holder.status.setImageResource(R.drawable.ic_cancel_red);
                    holder.txt_Status.setText("Rejected");
                    break;
            }
}
    public void filterList(List<Transaction> list) {
        transactionList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }
}
