package com.itxiaox.ximagloader.sdk.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.itxiaox.ximagloader.R;
import com.itxiaox.ximagloader.api.XImageLoadListener;
import com.itxiaox.ximagloader.api.XImageView;
import com.itxiaox.ximagloader.core.XImageActionBase;
import com.itxiaox.ximagloader.core.XImageConfig;
import com.itxiaox.ximagloader.core.option.SizeOption;
import com.itxiaox.ximagloader.sdk.glide.transformation.BlurTransformation;
import com.itxiaox.ximagloader.sdk.glide.transformation.CircleTransform;

/**
 * Created by SkySeraph on 2016/8/8.
 */
public class GlideImageAction extends XImageActionBase {

    @Override
    public void loadImage(Context context, XImageView imageView) {
        Glide.with(context)
                .load(imageView.getUrl())
                .into(imageView.getImageView());
    }

    @Override
    public void loadImageWithHolder(Context context, XImageView imageView) {
        Glide.with(context)
                .load(imageView.getUrl())
                .error(imageView.getHolderOption().getErrorHolder())
                .placeholder(imageView.getHolderOption().getPlaceHolder())
                .into(imageView.getImageView());
    }

    @Override
    public void loadImageWithThumbnail(Context context, XImageView imageView) {
        String thumbnailUrl = imageView.getThumbnailOption().getUrl();//获取缩略图的url
        if (thumbnailUrl == null) {
            Glide.with(context)
                    .load(imageView.getUrl())
                    .thumbnail(XImageConfig.SIZE_MULTIPLIER)
                    .into(imageView.getImageView());
        } else {
            Glide.with(context)
                    .load(imageView.getUrl())
                    .thumbnail(Glide.with(context).load(thumbnailUrl))
                    .into(imageView.getImageView());

        }
    }

    @Override
    public void loadImageWithSize(Context context, XImageView imageView) {
        SizeOption size = imageView.getImageSize();
        int width = (size != null ? size.getWidth() : 0);
        int height = (size != null ? size.getHeight() : 0);
        if (width == 0 || height == 0) {
            Glide.with(context)
                    .load(imageView.getUrl())
                    .into(imageView.getImageView());
        } else {
            Glide.with(context)
                    .load(imageView.getUrl())
                    .override(width, height)
                    .into(imageView.getImageView());
        }
    }

    @Override
    public void loadImageWithAnim(Context context, XImageView imageView) {
        Glide.with(context)
                .load(imageView.getUrl())
                .transition(GenericTransitionOptions.with(imageView.getAnimateOption().getAnimate()))
                .into(imageView.getImageView());
    }

    /**
     * all:缓存源资源和转换后的资源
     * none:不作任何磁盘缓存
     * source:缓存源资源
     * result：缓存转换后的资源
     */
    public void loadImageWithCache(Context context, XImageView imageView) {
        Glide.with(context)
                .load(imageView.getUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL) // TODO: 2016/3/10  抽离
                .skipMemoryCache(true) // 跳过内存缓存
                .into(imageView.getImageView());
    }

    @Override
    public void loadCircleImage(Context context, XImageView imageView) {
        Glide.with(context)
                .load(imageView.getUrl())
                .dontAnimate()
                .transform(new CircleTransform(context))
                .into(imageView.getImageView());
    }


    @Override
    public void loadBlurImage(Context context, XImageView imageView) {
        Glide.with(context)
                .load(imageView.getUrl())
                .dontAnimate()
                .transform(new BlurTransformation(context))
                .into(imageView.getImageView());
    }


    @Override
    public void loadGifImage(Context context, XImageView imageView) {
//        Glide.with(context)
//                .load(imageView.getUrl())
//                .asGif()
//                .dontAnimate()
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .into(imageView.getImageView());

        Glide.with(context)
                .asGif()
                .fitCenter()//still not work without fitCenter()
                .load(imageView.getUrl())
                .into(imageView.getImageView());

//        RequestOptions options = new RequestOptions()
//                .centerCrop()
//                //.placeholder(R.mipmap.ic_launcher_round)
//                .error(android.R.drawable.stat_notify_error)
//                .priority(Priority.HIGH)
//                //.skipMemoryCache(true)
//                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
//
//        Glide.with(context)
//                .load(imageView.getUrl())
//                .apply(options)
//                //.thumbnail(Glide.with(this).load(R.mipmap.ic_launcher))
//                .into(imageView.getImageView());
    }

    @Override
    public void loadImageListener(Context context, final XImageView imageView, final XImageLoadListener listener) {
        Glide.with(context)
                .load(imageView.getUrl())
                .error(imageView.getHolderOption().getErrorHolder())
                .placeholder(imageView.getHolderOption().getPlaceHolder())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (listener != null) {
                            listener.onLoadingError(target, e);
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (null != listener) {
                            if (imageView.getImageView() != null) {
                                listener.onLoadingComplete(target, imageView.getImageView(), resource);
                            } else {
                                listener.onLoadingComplete(target, null, resource);
                            }
                        }

                        return false;
                    }
                })
                .into(imageView.getImageView());
    }

    /**
     * 设置动态转换
     * centerCrop():均衡缩放头像,保持图像原始比例，图像位于视图中心
     * fitCenter():缩放头像，在视图中使图像居中
     *
     * @param mContext   the m context
     * @param path       the path
     * @param mImageView the m image view
     */
    public void loadImageViewCrop(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).centerCrop().into(mImageView);
    }

    /**
     * Load image view fit.
     *
     * @param mContext   the m context
     * @param path       the path
     * @param mImageView the m image view
     */
    public void loadImageViewFit(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).fitCenter().into(mImageView);
    }

    @Override
    protected void cancelAllTasks(Context context) {
        Glide.with(context).pauseRequests();
    }

    @Override
    protected void resumeAllTasks(Context context) {
        Glide.with(context).resumeRequests();
    }

    @Override
    protected void clearAllCache(Context context) {
        clearDiskCache(context);
        clearMemory(context);
    }

    /**
     * 清除磁盘缓存
     */
    private void clearDiskCache(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(context).clearDiskCache();
            }
        }).start();
    }

    /**
     * 释放内存
     */
    private void clearMemory(Context context) {
        Glide.get(context).clearMemory();
    }
}
