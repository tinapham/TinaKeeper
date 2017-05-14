package com.example.tina.doanmang_tinakeeper.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tina.doanmang_tinakeeper.ExpenseDetailActivity;
import com.example.tina.doanmang_tinakeeper.R;
import com.example.tina.doanmang_tinakeeper.model.Expense;
import com.google.gson.Gson;

import java.util.List;

public class RecyclerDataAdapter extends RecyclerView.Adapter<RecyclerDataAdapter.DataViewHolder>  {

    private List<Expense> expense;
    private Context context;

    private static ClickListener clickListener;
    private static final int MENU_ITEM_VIEW = 111;
    private static final int MENU_ITEM_EDIT = 222;
    private static final int MENU_ITEM_CREATE = 333;
    private static final int MENU_ITEM_DELETE = 444;
    private static final int MY_REQUEST_CODE = 1000;

    public RecyclerDataAdapter(Context context, List<Expense> expense) {
        this.context = context;
        this.expense = expense;
    }

    /**
     * Data ViewHolder class.
     */
    public static class DataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CardView cardView;
        private TextView tvExpense;
        private TextView tvCategory;
        private TextView tvNote;
        //private ImageView ivPic;

        public DataViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cv);
            tvCategory = (TextView) itemView.findViewById(R.id.category);
            tvNote = (TextView) itemView.findViewById(R.id.notes);
            tvExpense = (TextView) itemView.findViewById(R.id.cost_value);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    @Override
    public int getItemCount() {
        return expense == null ? 0 : expense.size();
    }

    @Override
    public RecyclerDataAdapter.DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;
        RecyclerDataAdapter.DataViewHolder viewHolder = null;
        switch (viewType) {
            case 0:
                view = View.inflate(parent.getContext(), R.layout.cardview_row_item, null);
                viewHolder = new DataViewHolder(view);
                break;
            case 1:
                view = View.inflate(parent.getContext(), R.layout.cardview_row_item_expense, null);
                viewHolder = new DataViewHolder(view);
                break;
        }
        return viewHolder;
    }
    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        RecyclerDataAdapter.clickListener = clickListener;
    }

    @Override
    public int getItemViewType(int position) {
        String category = expense.get(position).getCategory();
        if (category.equals("Deposits")||category.equals("Salary")||category.equals("Savings")){
            return 0;
        }
        return 1;
    }

    @Override
    public void onBindViewHolder(RecyclerDataAdapter.DataViewHolder holder, int position) {
        String category = expense.get(position).getCategory();
        holder.tvCategory.setText(expense.get(position).getCategory());
        holder.tvNote.setText(expense.get(position).getNotes());
        if (category.equals("Deposits")||category.equals("Salary")||category.equals("Savings")){
            holder.tvExpense.setText("+$"+String.valueOf(expense.get(position).getMoney()));
        } else {
            holder.tvExpense.setText("-$"+String.valueOf(expense.get(position).getMoney()));
        }
        final int pos=position;
    }



    public void addItem(Expense item) {
        expense.add(item);
        //notify tại vị trí mà bạn add item.
        notifyItemInserted(expense.size() - 1);
    }

    public void addItem(int position, Expense item) {
        expense.add(position, item);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        expense.remove(position);
        notifyItemRemoved(position);
    }

    public void removeItem(Expense item) {
        int index = expense.indexOf(item);
        if (index < 0)
            return;
        expense.remove(index);
        notifyItemRemoved(index);
    }

    public void replaceItem(int postion, Expense item) {
        expense.remove(postion);
        expense.add(postion, item);
        notifyItemChanged(postion);
    }

    public Expense getItem(int position){
        return expense.get(position);
    }
}
