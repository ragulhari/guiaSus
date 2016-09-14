package com.ragulhari.guiasus;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ragulhari.guiasus.listObjects.*;
import com.ragulhari.guiasus.PlaceFragment.OnListFragmentInteractionListener;
import com.ragulhari.guiasus.dummy.DummyContent.DummyItem;

import java.util.List;

/**item
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyplaceRecyclerViewAdapter extends RecyclerView.Adapter<MyplaceRecyclerViewAdapter.ViewHolder> {

    private final List<placeListObjectItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyplaceRecyclerViewAdapter(List<placeListObjectItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_place, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mNomeFantasia.setText(mValues.get(position).strNomeFantasia);
        holder.mTipoUnidade.setText(mValues.get(position).strTipoUnidade);
        holder.mTelefone.setText(mValues.get(position).strTelefone);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNomeFantasia;
        public final TextView mTipoUnidade;
        public final TextView mTelefone;
        public placeListObjectItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNomeFantasia = (TextView) view.findViewById(R.id.nomeFantasia_list);
            mTipoUnidade = (TextView) view.findViewById(R.id.tipoUnidade_list);
            mTelefone = (TextView) view.findViewById(R.id.telefone_list);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNomeFantasia.getText() + "'";
        }
    }
}
