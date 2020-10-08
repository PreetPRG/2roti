package com.example.a2roti_v0.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a2roti_v0.Model.Donation;
import com.example.a2roti_v0.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class DonationsAdapter extends FirestoreRecyclerAdapter<Donation, DonationsAdapter.DonationHolder> {

    private OnDonationclickListener mOnDonationclickListener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public DonationsAdapter(@NonNull FirestoreRecyclerOptions<Donation> options,OnDonationclickListener onDonationclickListener) {
        super(options);
        this.mOnDonationclickListener=onDonationclickListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull DonationHolder holder, int position, @NonNull Donation model) {
        holder.user_name.setText(model.getUser_name());
        holder.user_city.setText(String.valueOf(model.getCity_id()));
        holder.Landmark.setText(model.getLandmark());
        holder.category.setText(model.getCategory());
        holder.organiztion.setText(model.getOrganization_name());
    }

    @NonNull
    @Override
    public DonationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_donation_detail,parent,false);

        return new DonationHolder(v,mOnDonationclickListener);
    }

    class DonationHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView user_name;
        TextView user_city;
        TextView category;
        TextView Landmark;
        TextView organiztion;
        OnDonationclickListener onDonationclickListener;
        public DonationHolder(@NonNull View itemView,OnDonationclickListener onDonationclickListener) {
            super(itemView);
            user_name = itemView.findViewById(R.id.user_name);
            user_city= itemView.findViewById(R.id.item_city);
            category=itemView.findViewById(R.id.item_category);
            Landmark=itemView.findViewById(R.id.item_landmark);
            organiztion=itemView.findViewById(R.id.item_organization);
            this.onDonationclickListener=onDonationclickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position=getAdapterPosition();
            if(position!=RecyclerView.NO_POSITION )
            onDonationclickListener.OnDonationClick(getSnapshots().getSnapshot(position),position);
        }
    }

    public interface OnDonationclickListener{
        void OnDonationClick(DocumentSnapshot documentSnapshot,int position);
    }
}
