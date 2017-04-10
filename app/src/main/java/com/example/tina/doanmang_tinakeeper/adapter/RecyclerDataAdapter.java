package com.example.tina.doanmang_tinakeeper.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tina.doanmang_tinakeeper.R;
import com.example.tina.doanmang_tinakeeper.model.Expense;

import java.util.List;

public class RecyclerDataAdapter extends RecyclerView.Adapter<RecyclerDataAdapter.DataViewHolder> {

    private List<Expense> expense;
    private Context context;
    private static final int MENU_ITEM_VIEW = 111;
    private static final int MENU_ITEM_EDIT = 222;
    private static final int MENU_ITEM_CREATE = 333;
    private static final int MENU_ITEM_DELETE = 444;
    private static final int MY_REQUEST_CODE = 1000;


    public RecyclerDataAdapter(Context context, List<Expense> expense) {
        this.context = context;
        this.expense = expense;
    }

    @Override
    public int getItemCount() {
        return expense == null ? 0 : expense.size();
    }

    @Override
    public RecyclerDataAdapter.DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_row_item, parent, false);
        return new DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerDataAdapter.DataViewHolder holder, int position) {
        holder.tvCategory.setText(expense.get(position).getCategory());
        holder.tvNote.setText(expense.get(position).getNotes());
        //holder.ivPic.setImageResource(expense.get(position).getPhotoID());
        holder.tvExpense.setText(String.valueOf(expense.get(position).getMoney()));

        final int pos=position;

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context,"clicked"+pos, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Data ViewHolder class.
     */
    public static class DataViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
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
            //ivPic = (ImageView) itemView.findViewById(R.id.category_pic);
            tvExpense = (TextView) itemView.findViewById(R.id.cost_value);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View view,
                                        ContextMenu.ContextMenuInfo menuInfo)    {

            menu.setHeaderTitle("Select The Action");

            // groupId, itemId, order, title
            menu.add(0, MENU_ITEM_VIEW , 0, "View Expense");
            menu.add(0, MENU_ITEM_CREATE , 1, "Create Expense");
            menu.add(0, MENU_ITEM_EDIT , 2, "Edit Expense");
            menu.add(0, MENU_ITEM_DELETE, 3, "Delete Expense");
        }
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
