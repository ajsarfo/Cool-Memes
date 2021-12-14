package com.sarftec.coolmemes.view.manager

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.android.billingclient.api.*
import com.sarftec.coolmemes.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BillingManager(
    private val activity: AppCompatActivity
) {

   private var callback: (suspend (Resource<Unit>) -> Unit)? = null

    private var billingClient = BillingClient.newBuilder(activity)
        .setListener(getPurchaseUpdateListener())
        .enablePendingPurchases()
        .build()

    fun launchFlow(callback: suspend (Resource<Unit>) -> Unit) {
        this.callback = callback
        connectToGooglePlayBilling()
    }

    private fun launchBillingFlow(skuDetails: SkuDetails): Result<Unit> {
        val flowParams = BillingFlowParams.newBuilder()
            .setSkuDetails(skuDetails)
            .build()
        val responseCode = billingClient.launchBillingFlow(activity, flowParams).responseCode
        return if (responseCode == BillingClient.BillingResponseCode.OK) Result.success(Unit)
        else Result.failure(Exception("Failed to launch flow"))
    }

    private fun getPurchaseUpdateListener(): PurchasesUpdatedListener {
        return PurchasesUpdatedListener { billingResult, purchases ->
            // To be implemented in a later section.
            if (
                billingResult.responseCode == BillingClient.BillingResponseCode.OK
                && purchases != null
            ) {
                purchases.firstOrNull()?.let {
                    if (
                        it.purchaseState == Purchase.PurchaseState.PURCHASED
                        && !it.isAcknowledged
                    ) {
                        activity.lifecycleScope.launch {
                            callback?.invoke(Resource.success(Unit))
                        }
                        //Do some success thing here
                    }
                }
            }
        }
    }

    private fun connectToGooglePlayBilling() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    activity.lifecycleScope.launch {
                        getProductDetails()?.let {
                            launchBillingFlow(it).onFailure { throwable ->
                                callback?.invoke(Resource.error(throwable.message))
                                Log.v("TAM", "Purchase failed => ${throwable.message}")
                            }.onSuccess {
                                Log.v("TAM", "Purchase Success!")
                            }
                        }
                    }
                }
            }

            override fun onBillingServiceDisconnected() {
                    connectToGooglePlayBilling()
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })
    }

    private suspend fun getProductDetails(): SkuDetails? {
        val productIds = listOf("review_billing")
        val params = SkuDetailsParams.newBuilder()
            .setSkusList(productIds)
            .setType(BillingClient.SkuType.INAPP)

        // leverage querySkuDetails Kotlin extension function
        val skuDetailsResult = withContext(Dispatchers.IO) {
            billingClient.querySkuDetails(params.build())
        }
        return skuDetailsResult.takeIf {
            it.billingResult.responseCode == BillingClient.BillingResponseCode.OK
                    && it.skuDetailsList != null
        }?.let {
            skuDetailsResult.skuDetailsList!![0]
        }
    }
}