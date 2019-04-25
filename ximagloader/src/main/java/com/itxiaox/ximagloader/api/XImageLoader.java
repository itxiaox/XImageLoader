package com.itxiaox.ximagloader.api;

import android.content.Context;


import com.itxiaox.ximagloader.utils.PreconditionsUtils;
import com.itxiaox.ximagloader.core.XImageActionBase;
import com.itxiaox.ximagloader.core.XImageConfig;
import com.itxiaox.ximagloader.sdk.fresco.FrescoImageAction;
import com.itxiaox.ximagloader.sdk.glide.GlideImageAction;
import com.itxiaox.ximagloader.sdk.picasso.PicassoImageAction;

import java.util.HashMap;

/**
 * Created by SkySeraph on 2016/8/8.
 */
public class XImageLoader {

    private static XImageLoader INSTANCE = new XImageLoader();



    private static HashMap<Integer, XImageActionBase> MAP = new HashMap<>();
    private int curImageAction = XImageConfig.IMAGE_GLIDE;

    static {
        MAP.put(XImageConfig.IMAGE_GLIDE, new GlideImageAction());
        MAP.put(XImageConfig.IMAGE_FRESCO, new FrescoImageAction());
        MAP.put(XImageConfig.IMAGE_PICASSO, new PicassoImageAction());
    }

    private XImageLoader() {
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static XImageLoader getInstance() {
        return INSTANCE;
    }

    /**
     * Sets cur image action.
     *
     * @param curImageAction the cur image action
     */
    public void setCurImageAction(int curImageAction) {
        this.curImageAction = curImageAction;
    }

    /**
     * Load image.
     *
     * @param context the context
     * @param img     the img
     */
    public void loadImage(Context context, XImageView img) {
        PreconditionsUtils.checkNotNull(context, "context is null");
        PreconditionsUtils.checkNotNull(img, "img  is null");
        if (MAP.containsKey(curImageAction)) {
            MAP.get(curImageAction).loadImage(context, img);
        }
    }

    /**
     * Load image with thumbnail.
     *
     * @param context the context
     * @param img     the img
     */
    public void loadImageWithThumbnail(Context context, XImageView img) {
        PreconditionsUtils.checkNotNull(context, "context is null");
        PreconditionsUtils.checkNotNull(img, "img  is null");
        PreconditionsUtils.checkNotNull(img.getThumbnailOption(), "img.getThumbnailOption()  is null");
        if (MAP.containsKey(curImageAction)) {
            MAP.get(curImageAction).loadImageWithThumbnail(context, img);
        }
    }

    /**
     * Load image size.
     *
     * @param context the context
     * @param img     the img
     */
    public void loadImageSize(Context context, XImageView img) {
        PreconditionsUtils.checkNotNull(context, "context is null");
        PreconditionsUtils.checkNotNull(img, "img  is null");
        PreconditionsUtils.checkNotNull(img.getImageSize(), "img.getImageSize()  is null");
//        if (context == null || img == null || img.getImageSize() == null) {
//            throw new IllegalArgumentException("context or img or imageSize is null");
//        }
        if (MAP.containsKey(curImageAction)) {
            MAP.get(curImageAction).loadImageWithSize(context, img);
        }
    }

    /**
     * Load image listener.
     *
     * @param context  the context
     * @param img      the img
     * @param listener the listener
     */
    public void loadImageListener(Context context, XImageView img, XImageLoadListener listener) {
        PreconditionsUtils.checkNotNull(context, "context is null");
        PreconditionsUtils.checkNotNull(img, "img  is null");
        if (MAP.containsKey(curImageAction)) {
            MAP.get(curImageAction).loadImageListener(context, img, listener);
        }
    }
}
