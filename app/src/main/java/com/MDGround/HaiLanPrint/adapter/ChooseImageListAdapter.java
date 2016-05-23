package com.MDGround.HaiLanPrint.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.models.MDImage;
import com.MDGround.HaiLanPrint.utils.GlideUtil;
import com.MDGround.HaiLanPrint.utils.SelectImageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoghourt on 16/5/16.
 */
public class ChooseImageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final static int MODE_MULTIPLE = 1;
    public final static int MODE_SINGLE = 2;

    public static final int TYPE_CAMERA = 3;
    public static final int TYPE_PICTURE = 4;

    private Context context;
    private boolean showCamera = false;
    private boolean enablePreview = false;
    private int mMaxSelectNum = Integer.MAX_VALUE;
    private int selectMode = MODE_MULTIPLE;
    private boolean mIsSelectable = false;

    private List<MDImage> mImages = new ArrayList<MDImage>();
    private List<MDImage> mSelectImages = new ArrayList<MDImage>();

    private OnImageSelectChangedListener imageSelectChangedListener;

    public ChooseImageListAdapter(Context context, int maxSelectNum, boolean isSelectable) {
        this.context = context;
        this.mMaxSelectNum = maxSelectNum;
        this.mIsSelectable = isSelectable;
    }

    public void setSelectable(boolean isSelectable) {
        mIsSelectable = isSelectable;
        if (!mIsSelectable) {
            mSelectImages.clear();
            notifyDataSetChanged();
        }
    }

    public void selectAllImage(boolean mIsSelectAll) {
        if (mIsSelectAll) {
            mSelectImages.clear();
            for (MDImage localMedia : mImages) {
                mSelectImages.add(localMedia);
            }
        } else {
            mSelectImages.clear();
        }
        notifyDataSetChanged();
    }

    public void bindImages(List<MDImage> images) {
        this.mImages = images;
        notifyDataSetChanged();
    }

    public void bindSelectImages(List<MDImage> images) {
        this.mSelectImages = images;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (showCamera && position == 0) {
            return TYPE_CAMERA;
        } else {
            return TYPE_PICTURE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_CAMERA) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_camera, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picture, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == TYPE_CAMERA) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            headerHolder.headerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (imageSelectChangedListener != null) {
                        imageSelectChangedListener.onTakePhoto();
                    }
                }
            });
        } else {
            final ViewHolder contentHolder = (ViewHolder) holder;
            final MDImage mdImage = mImages.get(showCamera ? position - 1 : position);

            GlideUtil.loadImageByMDImage(contentHolder.picture, mdImage);

            if (selectMode == MODE_SINGLE) {
                contentHolder.check.setVisibility(View.GONE);
            }

            selectImage(contentHolder, isSelected(mdImage));

            if (enablePreview) {
                contentHolder.check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeCheckboxState(contentHolder, mdImage);
                    }
                });
            }

            contentHolder.contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((selectMode == MODE_SINGLE || enablePreview) && imageSelectChangedListener != null) {
                        imageSelectChangedListener.onPictureClick(mdImage, showCamera ? position - 1 : position);
                    } else {
                        if (mIsSelectable) {
                            changeCheckboxState(contentHolder, mdImage);
                        }
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return showCamera ? mImages.size() + 1 : mImages.size();
    }

    private void changeCheckboxState(ViewHolder contentHolder, MDImage image) {
        boolean isChecked = contentHolder.check.isSelected();
        if (mSelectImages.size() >= mMaxSelectNum && !isChecked) {
            Toast.makeText(context, context.getString(R.string.message_max_num, mMaxSelectNum), Toast.LENGTH_LONG).show();
            return;
        }
        if (isChecked) {
            for (MDImage media : mSelectImages) {
                if (SelectImageUtil.isSameImage(media, image)) {
                    mSelectImages.remove(media);
                    if (imageSelectChangedListener != null) {
                        imageSelectChangedListener.onUnSelectImage(image);
                    }
                    break;
                }
            }
        } else {
            mSelectImages.add(image);
            if (imageSelectChangedListener != null) {
                imageSelectChangedListener.onSelectImage(image);
            }
        }
        selectImage(contentHolder, !isChecked);
    }

    public List<MDImage> getSelectedImages() {
        return mSelectImages;
    }

    public List<MDImage> getmImages() {
        return mImages;
    }

    public boolean isSelected(MDImage image) {
        for (MDImage media : mSelectImages) {
            if (SelectImageUtil.isSameImage(media, image)) {
                return true;
            }
        }
        return false;
    }

    public void selectImage(ViewHolder holder, boolean isChecked) {
        holder.check.setSelected(isChecked);
        if (isChecked) {
            holder.picture.setColorFilter(context.getResources().getColor(R.color.image_overlay2), PorterDuff.Mode.SRC_ATOP);
        } else {
            holder.picture.setColorFilter(context.getResources().getColor(R.color.image_overlay), PorterDuff.Mode.SRC_ATOP);
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        View headerView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            headerView = itemView;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView picture;
        ImageView check;

        View contentView;

        public ViewHolder(View itemView) {
            super(itemView);
            contentView = itemView;
            picture = (ImageView) itemView.findViewById(R.id.picture);
            check = (ImageView) itemView.findViewById(R.id.check);
        }

    }

    public interface OnImageSelectChangedListener {

        void onSelectImage(MDImage selectImage);

        void onUnSelectImage(MDImage unselectImage);

        void onTakePhoto();

        void onPictureClick(MDImage media, int position);
    }

    public void setOnImageSelectChangedListener(OnImageSelectChangedListener imageSelectChangedListener) {
        this.imageSelectChangedListener = imageSelectChangedListener;
    }
}
