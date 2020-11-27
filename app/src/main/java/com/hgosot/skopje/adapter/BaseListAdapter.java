package com.hgosot.skopje.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hgosot.skopje.ItemLoadMore;
import com.hgosot.skopje.ItemNoResults;
import com.hgosot.skopje.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BaseListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected final List<Object> items;
    protected ItemListener itemListener;
    protected LoadMoreListener loadMoreListener;

    public static final int ITEM_LOAD_MORE = 70;
    private final int ITEM_NO_RESULTS = 71;

    public BaseListAdapter() {
        this.items = new ArrayList<>();
    }

    @Deprecated
    public BaseListAdapter(ItemListener listener) {
        this.items = new ArrayList<>();
        this.itemListener = listener;
    }

    public BaseListAdapter(LoadMoreListener loadMoreListener) {
        this.items = new ArrayList<>();
        this.loadMoreListener = loadMoreListener;
    }

    /*
        @Override
        public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
            super.onViewRecycled(holder);
            Glide.clear(holder.imageView);
        }
    */

    @Override
    public int getItemViewType(int position) {
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        switch (viewType) {
            case ITEM_LOAD_MORE:
                view = inflater.inflate(R.layout.item_load_more, parent, false);
                viewHolder = new LoadMoreViewHolder(view, loadMoreListener);
                break;
            case ITEM_NO_RESULTS:
                view = inflater.inflate(R.layout.item_load_more, parent, false);
                viewHolder = new NoResultsViewHolder(view);
                break;
            default:
                // TODO: 13-Jun-17 add empty view holder
                view = inflater.inflate(R.layout.item_load_more, parent, false);
                viewHolder = new RecyclerView.ViewHolder(view) {
                };
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        switch (viewHolder.getItemViewType()) {
            case ITEM_LOAD_MORE:
                LoadMoreViewHolder loadingViewHolder = (LoadMoreViewHolder) viewHolder;
                loadingViewHolder.bind(position);
                break;
            case ITEM_NO_RESULTS:
                NoResultsViewHolder noResultsViewHolder = (NoResultsViewHolder) viewHolder;
                noResultsViewHolder.bind(position);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void add(Object item) {
        this.items.add(item);
    }

    public void add(int index, Object item) {
        this.items.add(index, item);
    }

    public void addAll(List items) {
        this.items.addAll(items);
    }

    public void addAll(Object[] items) {
        this.items.addAll(Arrays.asList(items));
    }

    public void addAll(int index, List items) {
        this.items.addAll(index, items);
    }

    public void addAll(int index, Object[] items) {
        this.items.addAll(index, Arrays.asList(items));
    }

    public void remove(int index) {
        items.remove(index);
    }

    public void removeAndNotify(int index) {
        items.remove(index);
        notifyItemRemoved(index);
    }

    public Object get(int index) {
        return items.get(index);
    }

    public void clear() {
        items.clear();
    }

    public void clearInternal() {
        clear();
        //this is called to prevent  java.lang.IndexOutOfBoundsException: Inconsistency detected. Invalid item position 13(offset:13).state:17 androidx.recyclerview.widget.RecyclerView
        //while scrolling!Because the adapter was not notified about the data change!
        notifyDataSetChanged();
    }

    public List getItems() {
        return items;
    }

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }

    /**
     * Replaces the object at a given position.
     */
    public boolean replace(@NonNull Object object, int position) {
        if (position >= 0 && position < getItemCount()) {
            items.set(position, object);
            return true;
        }
        return false;
    }

    /**
     * Replaces an object in a horizontal list.
     */
    public void replace(@NonNull Object object, int rowPosition, int columnPosition) {
        if (columnPosition >= 0) {
            List list = (List) get(rowPosition);
            list.set(columnPosition, object);
        } else {
            replace(object, rowPosition);
        }
    }

    /**
     * Replaces the object at a given position and notifies.
     */
    public void replaceAndNotify(@NonNull Object object, int position) {
        if (replace(object, position)) {
            notifyItemChanged(position);
        }
    }

    /**
     * Replaces an object in a horizontal list and notifies.
     */
    public void replaceAndNotify(@NonNull Object object, int rowPosition, int columnPosition) {
        if (columnPosition >= 0) {
            List list = (List) get(rowPosition);
            list.set(columnPosition, object);
            notifyItemChanged(rowPosition);
        } else {
            replaceAndNotify(object, rowPosition);
        }
    }

    @Deprecated
    public interface ItemListener {
        void onClick(int position);

        void onLongClick(int position);

        void loadMore();
    }

    public interface LoadMoreListener {
        void onLoadMore();
    }

    public class LoadMoreViewHolder extends RecyclerView.ViewHolder {

        public final View mainView;
        public final TextView loadMoreText;
        public final ProgressBar progressBar;

        private ItemLoadMore itemLoadMore;

        public LoadMoreViewHolder(View view, final LoadMoreListener loadMoreListener) {
            super(view);
            mainView = view;
            progressBar = view.findViewById(R.id.progress_bar);
            loadMoreText = view.findViewById(R.id.load_more_text);
            loadMoreText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemLoadMore.setLoading(true);
                    loadMoreText.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    loadMoreListener.onLoadMore();
                }
            });
        }

        public void bind(int position) {
            itemLoadMore = (ItemLoadMore) items.get(position);
            if (itemLoadMore.getType() == ItemLoadMore.TYPE_LOADER || itemLoadMore.isLoading()) {
                loadMoreText.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
            } else {
                loadMoreText.setVisibility(View.VISIBLE);
                loadMoreText.setText(itemLoadMore.getText());
                progressBar.setVisibility(View.GONE);
            }
        }

    }

    public class NoResultsViewHolder extends RecyclerView.ViewHolder {

//        public final TextView description;

        public NoResultsViewHolder(View view) {
            super(view);
//            description = view.findViewById(R.id.description);
        }

        public void bind(int position) {
            ItemNoResults itemNoResults = (ItemNoResults) items.get(position);

            String fullDescription = null;
            String itemText = null;

            switch (itemNoResults.getType()) {
                case ItemNoResults.TYPE_GUIDE:
//                    fullDescription = description.getResources().getString(R.string.no_results_description_guides);
                    break;
                case ItemNoResults.TYPE_CONTACTS:
//                    fullDescription = description.getResources().getString(R.string.no_results_description_contacts);
                    break;
                case ItemNoResults.TYPE_CONVERSATION:
//                    fullDescription = description.getResources().getString(R.string.no_results_description_messages);
                    break;
                case ItemNoResults.TYPE_EVENT:
                    itemText = "events";
                    break;
                case ItemNoResults.TYPE_PRIVILEGE:
                    itemText = "privileges";
                    break;
                case ItemNoResults.TYPE_DISCUSSION:
                    itemText = "discussions";
                    break;
                default:
                    itemText = "items";
                    break;
            }

            if (fullDescription == null) {
//                fullDescription = description.getResources().getString(R.string.no_results_description, itemText);
            }

//            description.setText(fullDescription);
        }

    }

}
