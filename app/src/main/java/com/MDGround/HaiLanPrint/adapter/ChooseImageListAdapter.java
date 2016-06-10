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
import com.MDGround.HaiLanPrint.utils.SnapViewPicture;
import com.MDGround.HaiLanPrint.views.dialog.ShareDialog;

import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.onekeyshare.OnekeyShare;

import static com.MDGround.HaiLanPrint.utils.ViewUtils.getString;

/**
 * Created by yoghourt on 16/5/16.
 */
public class ChooseImageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final static int MODE_MULTIPLE = 1;
    public final static int MODE_SINGLE = 2;

    public static final int TYPE_CAMERA = 3;
    public static final int TYPE_PICTURE = 4;

    private Context mContext;

    private boolean showCamera = false;
    private boolean enablePreview = false;
    private int mMaxSelectNum = Integer.MAX_VALUE;
    private int selectMode = MODE_MULTIPLE;
    private boolean mIsSelectable = false;
    private boolean mIsShareble = false;

    private List<MDImage> mImages = new ArrayList<MDImage>();
    private List<MDImage> mSelectImages = new ArrayList<MDImage>();

    private OnImageSelectChangedListener imageSelectChangedListener;

    private ShareDialog mShareDialog;

    public ChooseImageListAdapter(Context context, int maxSelectNum, boolean isSelectable, boolean isShareble) {
        this.mContext = context;
        this.mMaxSelectNum = maxSelectNum;
        this.mIsSelectable = isSelectable;
        this.mIsShareble = isShareble;
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

            if (imageSelectChangedListener != null) {
                imageSelectChangedListener.onSelectImage(null, mSelectImages.size());
            }
        } else {
            mSelectImages.clear();

            if (imageSelectChangedListener != null) {
                imageSelectChangedListener.onUnSelectImage(null, 0);
            }
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

            GlideUtil.loadImageByMDImage(contentHolder.ivImage, mdImage);

            if (selectMode == MODE_SINGLE) {
                contentHolder.ivCheck.setVisibility(View.GONE);
            }

            selectImage(contentHolder, isSelected(mdImage));

            if (enablePreview) {
                contentHolder.ivCheck.setOnClickListener(new View.OnClickListener() {
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

            if (mIsShareble) {
                contentHolder.contentView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        String imagePath = SnapViewPicture.snapView(contentHolder.ivImage);

//                    OnekeyShare oks = new OnekeyShare();
//                    // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
//                    //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
//                    // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
//                    oks.setTitle("分享");
//                    // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//                    oks.setTitleUrl("http://sharesdk.cn");
//                    // text是分享文本，所有平台都需要这个字段
//                    oks.setText("我是分享文本");
//                    // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//                    oks.setImagePath(imagePath);//确保SDcard下面存在此张图片
//                    // url仅在微信（包括好友和朋友圈）中使用
//                    oks.setUrl("http://sharesdk.cn");
//                    // site是分享此内容的网站名称，仅在QQ空间使用
//                    oks.setSite(getString(R.string.app_name));
//                    // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//                    oks.setSiteUrl("http://sharesdk.cn");
//                    // 启动分享GUI
//                    oks.show(mContext);

                        if (mShareDialog == null) {
                            mShareDialog = new ShareDialog(mContext);
                        }
                        mShareDialog.initShareParams(imagePath);
                        mShareDialog.show();
                        return true;
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return showCamera ? mImages.size() + 1 : mImages.size();
    }

    private void changeCheckboxState(ViewHolder contentHolder, MDImage image) {
        boolean isChecked = contentHolder.ivCheck.isSelected();
        if (mSelectImages.size() >= mMaxSelectNum && !isChecked) {
            Toast.makeText(mContext, mContext.getString(R.string.message_max_num, mMaxSelectNum), Toast.LENGTH_LONG).show();
            return;
        }
        if (isChecked) {
            for (MDImage media : mSelectImages) {
                if (SelectImageUtil.isSameImage(media, image)) {
                    mSelectImages.remove(media);
                    if (imageSelectChangedListener != null) {
                        imageSelectChangedListener.onUnSelectImage(image, mSelectImages.size());
                        if (mSelectImages.size() == mImages.size() - 1) {
                            imageSelectChangedListener.onIsSelectAllImage(false);
                        }

                    }
                    break;
                }
            }
        } else {
            mSelectImages.add(image);
            if (imageSelectChangedListener != null) {
                imageSelectChangedListener.onSelectImage(image, mSelectImages.size());
                if (mSelectImages.size() == mImages.size()) {
                    imageSelectChangedListener.onIsSelectAllImage(true);
                }
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
        holder.ivCheck.setSelected(isChecked);
        if (isChecked) {
            holder.ivImage.setColorFilter(mContext.getResources().getColor(R.color.image_overlay2), PorterDuff.Mode.SRC_ATOP);
        } else {
            holder.ivImage.setColorFilter(mContext.getResources().getColor(R.color.image_overlay), PorterDuff.Mode.SRC_ATOP);
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
        ImageView ivImage;
        ImageView ivCheck;

        View contentView;

        public ViewHolder(View itemView) {
            super(itemView);
            contentView = itemView;
            ivImage = (ImageView) itemView.findViewById(R.id.picture);
            ivCheck = (ImageView) itemView.findViewById(R.id.check);
        }

    }

    public interface OnImageSelectChangedListener {

        void onSelectImage(MDImage selectImage, int selectNum);

        void onUnSelectImage(MDImage unselectImage, int selectNum);

        void onTakePhoto();

        void onPictureClick(MDImage media, int position);

        void onIsSelectAllImage(boolean isSelectAll);
    }

    public void setOnImageSelectChangedListener(OnImageSelectChangedListener imageSelectChangedListener) {
        this.imageSelectChangedListener = imageSelectChangedListener;
    }
}
