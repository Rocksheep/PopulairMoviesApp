package nl.codesheep.android.popularmoviesapp.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import nl.codesheep.android.popularmoviesapp.R;

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    public static final int SYNC_INTERVAL = 10;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;
    private static final String LOG_TAG = SyncAdapter.class.getSimpleName();

    ContentResolver mContentResolver;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        //TODO: Get data from server
        Log.d(LOG_TAG, "Syncing from server");
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }

    public static Account getSyncAccount(Context context) {
        AccountManager accountManager = AccountManager.get(context);

        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.account_type)
        );

        if (accountManager.getPassword(newAccount) == null) {
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }

            onAccountCreated(context, newAccount);
        }
        return newAccount;
    }

    private static void onAccountCreated(Context context, Account account) {
        Log.d(LOG_TAG, "Account created");
        SyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);
        ContentResolver.setSyncAutomatically(
                account,
                context.getString(R.string.content_authority),
                true
        );
        syncImmediately(context);
    }

    public static void syncImmediately(Context context) {
        Log.d(LOG_TAG, "Send sync request");
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(
                getSyncAccount(context),
                context.getString(R.string.content_authority),
                bundle
        );
    }

    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SyncRequest syncRequest = new SyncRequest.Builder()
                    .syncPeriodic(syncInterval, flexTime)
                    .setSyncAdapter(account, authority)
                    .setExtras(new Bundle())
                    .build();
            ContentResolver.requestSync(syncRequest);
        }
        else {
            ContentResolver.addPeriodicSync(account, authority, new Bundle(), syncInterval);
        }
    }
}
