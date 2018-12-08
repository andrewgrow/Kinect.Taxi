package pro.kinect.taxi.activities.activity_main;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pro.kinect.taxi.R;
import pro.kinect.taxi.db.EntityAuto;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    private OnClickListener clickListener;

    public EntityAuto getAuto(int adapterPosition) {
        return items.get(adapterPosition);
    }

    interface OnClickListener {
        void onItemClicked(int adapterPosition);
    }

    private List<EntityAuto> items = new ArrayList<>();

    public void replaceItems(@NonNull List<EntityAuto> newList) {
        items.clear();
        items = newList;
        notifyDataSetChanged();
    }

    public void setOnClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_result, parent, false);
        return new ViewHolder(view, adapterPosition -> {
            if (clickListener != null) {
                clickListener.onItemClicked(adapterPosition);
            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.tvInfo.setText(""); // default

        EntityAuto auto = items.get(position);
        String result = auto.getStateNumber()
                + " " + auto.getDriverName()
                + ", " + auto.getDriverPhone()
                + ", " + auto.getCarBrand()
                + " " + auto.getCarColor()
                ;
        viewHolder.tvInfo.setText(result); // result
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvInfo;

        public ViewHolder(@NonNull View itemView, final OnClickListener clickListener) {
            super(itemView);
            tvInfo = itemView.findViewById(R.id.tvInfo);
            itemView.setOnClickListener(v -> clickListener.onItemClicked(getAdapterPosition()));
        }
    }
}
