package com.samsung.android.sdk.iap.sample.adapter;

import android.util.Log;

import com.samsung.android.sdk.iap.lib.helper.IapHelper;
import com.samsung.android.sdk.iap.lib.listener.OnConsumePurchasedItemsListener;
import com.samsung.android.sdk.iap.lib.listener.OnGetOwnedListListener;
import com.samsung.android.sdk.iap.lib.vo.ConsumeVo;
import com.samsung.android.sdk.iap.lib.vo.ErrorVo;
import com.samsung.android.sdk.iap.lib.vo.OwnedProductVo;
import com.samsung.android.sdk.iap.sample.constants.ItemDefine;
import com.samsung.android.sdk.iap.sample.activity.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sangbum7.kim on 2017-08-17.
 */

public class OwnedListAdapter extends ItemDefine implements OnGetOwnedListListener, OnConsumePurchasedItemsListener
{
    private final String TAG = OwnedListAdapter.class.getSimpleName();

    private MainActivity        mMainActivity = null;
    private IapHelper    mIapHelper = null;

    private String              mConsumablePurchaseIDs = "";
    private Map<String, String> consumeItemMap = new HashMap<String, String>();

    public OwnedListAdapter
            (
                    MainActivity _activity,
                    IapHelper _iapHelper
            )
    {
        mMainActivity = _activity;
        mIapHelper = _iapHelper;
    }
    @Override
    public void onGetOwnedProducts(ErrorVo _errorVo, ArrayList<OwnedProductVo> _ownedList )
    {
        Log.v(TAG, "onGetOwnedProducts");
        if( _errorVo != null &&
                _errorVo.getErrorCode() == IapHelper.IAP_ERROR_NONE )
        {
            Log.d(TAG, "onGetOwnedProducts: ");
            int gunLevel = 0;
            boolean infiniteBullet = false;
            if(_ownedList != null) {
                for (int i = 0; i < _ownedList.size(); i++) {
                    _ownedList.get(i).dump();
                    OwnedProductVo product = _ownedList.get(i);

                    if(product.getIsConsumable()) {
                        try{
                            if(consumeItemMap.get(product.getPurchaseId()) == null)
                            {
                                consumeItemMap.put(product.getPurchaseId(), product.getItemId());
                                if (mConsumablePurchaseIDs.length() == 0)
                                    mConsumablePurchaseIDs = product.getPurchaseId();
                                else
                                    mConsumablePurchaseIDs = mConsumablePurchaseIDs + "," + product.getPurchaseId();
                            }
                        }catch (Exception e)
                        {
                            Log.e(TAG, "exception" + e );
                        }
                    }

                    if (product.getItemId().equals(ITEM_ID_NONCONSUMABLE)) {
                        gunLevel = 1;
                    }
                    else if (product.getItemId().equals(ITEM_ID_SUBSCRIPTION)) {
                        infiniteBullet = true;
                    }
                    else if (product.getItemId().equals(ITEM_ID_CONSUMABLE)) {
                        Log.d(TAG, "onGetOwnedProducts: consumePurchasedItems" + product.getPurchaseId());
                    }
                }
            }

            /* ----------------------------------------------------- */
            mMainActivity.setGunLevel(gunLevel);
            mMainActivity.setInfiniteBullet(infiniteBullet);

            if (mConsumablePurchaseIDs.length() > 0) {
                mIapHelper.consumePurchasedItems(mConsumablePurchaseIDs, OwnedListAdapter.this);
                mConsumablePurchaseIDs = "";
            }
        }
    }

    @Override
    public void onConsumePurchasedItems(ErrorVo _errorVO, ArrayList<ConsumeVo> _consumeList) {
        if (_errorVO.getErrorCode() == IapHelper.IAP_ERROR_NONE) {
            try {
                if (_consumeList != null) {
                    for (ConsumeVo consumeVo : _consumeList) {
                        if (consumeVo.getStatusCode().equals(CONSUME_STATUS_SUCCESS)) {
                            String itemId = consumeItemMap.get(consumeVo.getPurchaseId());
                            if (itemId != null) {
                                if (itemId.equals(ITEM_ID_CONSUMABLE))
                                    mMainActivity.plusBullet();
                                consumeItemMap.remove(consumeVo.getPurchaseId());
                            }
                        } else
                            Log.e(TAG, "onConsumePurchasedItems: statuscode " + consumeVo.getStatusCode());
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "onConsumePurchasedItems: Exception :" + e);
            }
        } else {
            Log.d(TAG, "onConsumePurchasedItem: IAP_ERROR code[" + _errorVO.getErrorCode() + "], message[" + _errorVO.getErrorString() + "]");
        }
        consumeItemMap.clear();
    }
}
