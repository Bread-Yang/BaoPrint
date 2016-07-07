package com.MDGround.HaiLanPrint.activity.messagecenter;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.databinding.ActivityMessageCenterBinding;
import com.MDGround.HaiLanPrint.databinding.ItemMessageCenterBinding;
import com.MDGround.HaiLanPrint.models.Message;
import com.MDGround.HaiLanPrint.restfuls.GlobalRestful;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;
import com.MDGround.HaiLanPrint.utils.StringUtil;
import com.google.gson.reflect.TypeToken;
import com.malinskiy.superrecyclerview.OnMoreListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by PC on 2016-06-17.
 */

public class MessageCenterActivity extends ToolbarActivity<ActivityMessageCenterBinding> {

    private MessageAdapter mAdapter;

    private List<Message> mMessageList = new ArrayList<>();

    private int mPageIndex = 0;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_message_center;
    }

    @Override
    protected void initData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mDataBinding.recyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new MessageAdapter();
        mDataBinding.recyclerView.setAdapter(mAdapter);

        GetUserMessageListRequset();
    }

    @Override
    protected void setListener() {
        mDataBinding.recyclerView.setupMoreListener(new OnMoreListener() {
            @Override
            public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
                GetUserMessageListRequset();
            }
        }, Constants.ITEM_LEFT_TO_LOAD_MORE);
    }

    //region SERVER
    public void GetUserMessageListRequset() {
        GlobalRestful.getInstance().GetUserMessageList(mPageIndex, new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                mPageIndex++;

                if (StringUtil.isEmpty(response.body().getContent())) {
                    mDataBinding.recyclerView.setLoadingMore(false);
                    mDataBinding.recyclerView.setupMoreListener(null, 0);
                } else {
                    List<Message> list = response.body().getContent(new TypeToken<List<Message>>() {
                    });

                    mMessageList.addAll(list);
                    mAdapter.notifyDataSetChanged();
                }

                if (mMessageList.size() == 0) {
                    mDataBinding.lltNofind.setVisibility(View.VISIBLE);
                    mDataBinding.recyclerView.setVisibility(View.GONE);
                } else {
                    mDataBinding.lltNofind.setVisibility(View.GONE);
                    mDataBinding.recyclerView.setVisibility(View.VISIBLE);
                }

                mDataBinding.recyclerView.hideMoreProgress();
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {

            }
        });
    }

    //endregion

    //region ADAPTER
    public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MessageCenterActivity.this).inflate(R.layout.item_message_center, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final Message message = mMessageList.get(position);
            holder.itemMessageCenterBinding.tvCreateTime.setText(message.getCreatedTime());
            holder.itemMessageCenterBinding.tvMessage.setText(message.getMessage());
            if (position == 0) {
                holder.itemMessageCenterBinding.viewCircle.setBackgroundResource(R.drawable.shape_oragne_circle);
            } else {
                holder.itemMessageCenterBinding.viewCircle.setBackgroundResource(R.drawable.shape_seekbar_thumb);
            }

            holder.itemMessageCenterBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MessageCenterActivity.this, MessageDetailActivity.class);
                    intent.putExtra(Constants.KEY_MESSAGE, message);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mMessageList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public ItemMessageCenterBinding itemMessageCenterBinding;

            public MyViewHolder(View itemView) {
                super(itemView);
                itemMessageCenterBinding = DataBindingUtil.bind(itemView);
            }
        }
    }
    //endregion
}
