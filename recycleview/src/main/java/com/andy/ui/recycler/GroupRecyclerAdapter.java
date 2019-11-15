package com.andy.ui.recycler;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分组的RecyclerViewAdapter
 * 头， 尾
 */
public abstract class GroupRecyclerAdapter<G, GVH extends RecyclerView.ViewHolder, CVH extends RecyclerView.ViewHolder>
        extends HeaderFooterAdapter {

    public static final int INVALID_POSITION = -1;

    private static final int TYPE_GROUP = TYPE_DATA;
    private static final int TYPE_CHILD = 16;

    protected List<G> mGroups;

    public GroupRecyclerAdapter(List<G> groups) {
        mGroups = groups == null ? new ArrayList<G>() : groups;
        updateItemCount();
    }

    public void addGroupList(List<G> groups) {
        int position = getHeaderCount() + getDataItemCount();
        addGroupGroupList(groups, false);
        updateItemCount();
        int offset = getHeaderCount() + getDataItemCount() - position;
        notifyItemRangeInserted(position, offset);
    }

    public void updateGroupList(List<G> groups) {
        mGroups.clear();
        updateItemCount();
        notifyDataSetChanged();
        addGroupGroupList(groups, false);
        updateItemCount();
        notifyDataSetChanged();
    }

    public void clearGroupList() {
        if (mGroups.isEmpty()) return;
        mGroups.clear();
        updateItemCount();
        notifyDataSetChanged();
    }

    public void addGroupGroupList(List<G> groups, boolean notify) {
        if (groups != null) {
            mGroups.addAll(groups);
            if (notify) {
                updateItemCount();
                // 有待优化
                notifyDataSetChanged();
            }
        }
    }

    public void addGroup(@NonNull G groupItem) {
        int insertPosition = getHeaderCount() + getDataItemCount();
        mGroups.add(groupItem);
        notifyItemRangeInserted(insertPosition, getChildCount(groupItem));
    }

    public List<G> getGroups() {
        return mGroups;
    }

    public synchronized Map<G, Integer> getGroupsPosition() {
        Map<G, Integer> groupPositionMap = new HashMap<>();
        if (null == mGroups || mGroups.isEmpty()) return groupPositionMap;

        for (G group : mGroups) {
            groupPositionMap.put(group, getGroupItemPositionInAdapter(group));
        }

        return groupPositionMap;
    }

    @Override
    protected RecyclerView.ViewHolder createDataHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_GROUP) {
            return onCreateGroupViewHolder(parent);
        }
        return onCreateChildViewHolder(parent);
    }

    protected abstract GVH onCreateGroupViewHolder(ViewGroup parent);
    protected abstract CVH onCreateChildViewHolder(ViewGroup parent);
    protected abstract void onBindGroupViewHolder(GVH holder, int groupPosition);
    protected abstract void onBindChildViewHolder(CVH holder, int groupPosition, int childPosition);
    protected abstract int getChildCount(G group);

    @SuppressWarnings("unchecked")
    @Override
    protected void bindDataHolder(RecyclerView.ViewHolder holder, int itemPosition, int dataPosition) {
        Position position = getGroupChildPosition(itemPosition);
        if (position.child == INVALID_POSITION) {
            onBindGroupViewHolder((GVH) holder, position.group);
        } else {
            onBindChildViewHolder((CVH) holder, position.group, position.child);
        }
    }

    @Override
    protected int getDataItemType(int position) {
        return getItemType(position) == ItemType.GROUP_TITLE ? TYPE_GROUP : TYPE_CHILD;
    }

    @Override
    public int getDataItemCount() {
        return getGroupAndChildCount();
    }

    public void clearGroups() {
        if (null != mGroups) {
            mGroups.clear();
            updateItemCount();
        }
    }

    public int getGroupCount() {
        return mGroups.size();
    }

    public G getGroup(int groupPosition) {
        return mGroups.get(groupPosition);
    }

    /**
     * @param itemPosition item of adapter
     * @return group index in group collection, child index in group
     */
    public Position getGroupChildPosition(int itemPosition) {
        int itemCount = getHeaderCount();
        int childCount;
        final Position position = new Position();
        for (G g : mGroups) {
            // item index(itemPosition) is group
            if (itemPosition == itemCount) {
                position.child = INVALID_POSITION;
                return position;
            }

            // item child
            itemCount++;
            childCount = getChildCount(g);
            if (childCount > 0) {
                position.child = itemPosition - itemCount;
                if (position.child < childCount) {
                    return position;
                }
                itemCount += childCount;
            }
            position.group++;
        }
        return position;
    }

    private int getGroupAndChildCount() {
        int count = 0;
        for (G group : mGroups) {
            count += getChildCount(group) + 1;
        }
        return count;
    }

    private ItemType getItemType(final int itemPosition) {
        int count = getHeaderCount();
        int childCount;
        for (G g : mGroups) {
            if (itemPosition == count) {
                return ItemType.GROUP_TITLE;
            }
            childCount = getChildCount(g);
            count += 1;
            if (itemPosition == count && childCount != 0) {
                return ItemType.FIRST_CHILD;
            }
            count += childCount;
            if (itemPosition < count) {
                return ItemType.NOT_FIRST_CHILD;
            }
        }
        throw new IllegalStateException("Could not find item type for item position " + itemPosition);
    }

    public int getGroupItemPositionInAdapter(@NonNull G g) {
        int positionInGroupList = mGroups.indexOf(g);
        if (-1 == positionInGroupList) {
            return -1;
        }
        // 排在前面的Group，
        int countGroupAndChildBefore = 0;
        for (int i=0; i< positionInGroupList; i++) {
            countGroupAndChildBefore += (getChildCount(mGroups.get(i)) + 1);
        }
        int position = getHeaderCount() + countGroupAndChildBefore;
        return position;
    }

    public enum ItemType {
        GROUP_TITLE,
        FIRST_CHILD,
        NOT_FIRST_CHILD
    }

    public static class Position {
        public int group;
        public int child = INVALID_POSITION;
    }
}