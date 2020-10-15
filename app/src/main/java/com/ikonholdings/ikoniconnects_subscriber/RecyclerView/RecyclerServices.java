package com.ikonholdings.ikoniconnects_subscriber.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ikonholdings.ikoniconnects_subscriber.ApiHadlers.ApiClient;
import com.ikonholdings.ikoniconnects_subscriber.R;
import com.ikonholdings.ikoniconnects_subscriber.Activities.ServiceDetailActivity;
import com.ikonholdings.ikoniconnects_subscriber.ServicesModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerServices extends RecyclerView.Adapter<RecyclerServices.ViewHolder> {

    private List<ServicesModel> mServices;

    //view holder class
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView img_service_image;
        public TextView txt_Title;
        public TextView txt_Discription;
        public TextView txt_Charges;
        public TextView txt_PriceType;

        public RatingBar ratingBar;

        public ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            img_service_image = itemView.findViewById(R.id.img_servic_image);

            txt_Title = itemView.findViewById(R.id.txt_servic_name);
            txt_Discription = itemView.findViewById(R.id.txt_servic_description);
            txt_Charges = itemView.findViewById(R.id.txt_service_prize);
            txt_PriceType = itemView.findViewById(R.id.priceType);

            ratingBar = itemView.findViewById(R.id.ratbar);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    //constructor
    public RecyclerServices(List<ServicesModel> services_modelArrayList) {
        this.mServices = services_modelArrayList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ServicesModel currentItem = mServices.get(position);

        // holder.img_service_image.setImageResource(currentItem.getService_image());
        holder.txt_Title.setText(currentItem.getName());
        holder.txt_Discription.setText(currentItem.getDetails());
        holder.txt_Charges.setText("$" + currentItem.getPrice());
        holder.txt_PriceType.setText(currentItem.getPrice_type());

        holder.ratingBar.setRating(currentItem.getRating());

        if (!currentItem.getFeature_image().isEmpty()) {
            holder.progressBar.setVisibility(View.VISIBLE);
            Picasso.get().load((ApiClient.Base_Url + currentItem.getFeature_image()))
                    .into(holder.img_service_image, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            holder.progressBar.setVisibility(View.GONE);
                        }
                    });
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sending Featured Image only
                Intent i = new Intent(v.getContext(), ServiceDetailActivity.class);
                i.putExtra("serviceId", currentItem.getId());
                v.getContext().startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mServices.size();
    }
}