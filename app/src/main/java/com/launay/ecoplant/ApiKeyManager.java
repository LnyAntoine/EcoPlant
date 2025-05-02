package com.launay.ecoplant;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class ApiKeyManager {
    private static ApiKeyManager instance;
    private final FirebaseRemoteConfig mFirebaseRemoteConfig;
    private ApiKeyManager() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
    }
    public static synchronized ApiKeyManager getInstance() {
        if (instance == null) {
            instance = new ApiKeyManager();
        }
        return instance;
    }
    public void fetchAndGetApiKey(final ApiKeyCallback callback) {
        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String apiKey = mFirebaseRemoteConfig.getString("api_key");
                        if (callback != null) {
                            callback.onApiKeyFetched(apiKey);
                        }
                    } else {
                        if (callback != null) {
                            callback.onApiKeyFetched(null);
                        }
                    }
                });
    }
    public interface ApiKeyCallback {
        void onApiKeyFetched(String apiKey);
    }
}
