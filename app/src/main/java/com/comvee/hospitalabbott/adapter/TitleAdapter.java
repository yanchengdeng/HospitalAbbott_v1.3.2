package com.comvee.hospitalabbott.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;


import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.widget.magicindicator.buildins.UIUtil;
import com.comvee.hospitalabbott.widget.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.comvee.hospitalabbott.widget.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.comvee.hospitalabbott.widget.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.comvee.hospitalabbott.widget.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import com.comvee.hospitalabbott.widget.magicindicator.buildins.commonnavigator.model.TitleData;
import com.comvee.hospitalabbott.widget.magicindicator.buildins.commonnavigator.title.ClipPagerTitleView;

import java.util.List;

/**
 * Created by F011512088 on 2017/12/14.
 */

public class TitleAdapter extends CommonNavigatorAdapter {

    private List<TitleData> mData;
    private TitleAdapterClickListener onClickListener ;

    public TitleAdapter(List<TitleData> mData, TitleAdapterClickListener onClickListener) {
        this.mData = mData;
        this.onClickListener = onClickListener;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public IPagerTitleView getTitleView(Context context, final int index) {
        ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context);
//                clipPagerTitleView.setText(screeList.get(index).getTitle());
        clipPagerTitleView.setTitleData(mData.get(index));
        clipPagerTitleView.setTextColor(Color.parseColor("#000000"));
        clipPagerTitleView.setClipColor(Color.parseColor("#0099ff")); //#0099ff
        clipPagerTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener!=null)
                    onClickListener.onTitleClick(index);
            }
        });
        return clipPagerTitleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        LinePagerIndicator indicator = new LinePagerIndicator(context);
        indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
        float navigatorHeight = context.getResources().getDimension(R.dimen.common_navigator_height);
        float borderWidth = UIUtil.dip2px(context, 1);
        float lineHeight = navigatorHeight - 2 * borderWidth;
//                indicator.setLineHeight(lineHeight);
//                indicator.setRoundRadius(lineHeight / 2);
//                indicator.setYOffset(borderWidth);
        indicator.setLineHeight(UIUtil.dip2px(context, 1));
        indicator.setYOffset(UIUtil.dip2px(context, 3));
        indicator.setColors(context.getResources().getColor(R.color.blue));
//                LinePagerIndicator indicator = new LinePagerIndicator(context);
//                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
//                indicator.setYOffset(UIUtil.dip2px(context, 3));
//                indicator.setColors(getResources().getColor(R.color.blue));
        return indicator;
    }

    public void setData(List<TitleData> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }

    public interface TitleAdapterClickListener {
       void onTitleClick(int index);
    }

    public void setOnClickListener(TitleAdapterClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
