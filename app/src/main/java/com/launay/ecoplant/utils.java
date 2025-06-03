package com.launay.ecoplant;

import android.content.Context;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class utils {
    public interface AuthCallback {
        void onComplete(boolean success);
    }


    public static byte[] uriToBytes(Context context, Uri imageUri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
            if (inputStream == null) {
                Log.e("Utils", "InputStream is null for Uri: " + imageUri);
                return null;
            }

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int nRead;
            byte[] data = new byte[16384]; // 16KB buffer

            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();
            inputStream.close();

            Log.d("Utils", "Conversion uriToBytes réussie, taille: " + buffer.size());
            return buffer.toByteArray();

        } catch (IOException e) {
            Log.e("Utils", "Erreur lors de la conversion uriToBytes", e);
            return null;
        }
    }

    public static void callIdentifyPlantFunction(byte[] imageBytes, Consumer<Map<String,Object>> callback) {
        if (imageBytes == null) {
            Log.e("PlantNetFunction", "ImageBytes est null, impossible d'appeler la fonction");
            callback.accept(null);
            return;
        }
        String imageBase64 = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
        Log.d("PlantNetFunction", "Image encodée en Base64, longueur: " + imageBase64.length());

        Map<String, Object> data = new HashMap<>();
        data.put("imageBase64", imageBase64);
        data.put("organs", "leaf");


        FirebaseFunctions functions = FirebaseFunctions.getInstance();

        functions
                .getHttpsCallable("identifyPlant")
                .call(data)
                .addOnSuccessListener(httpsCallableResult -> {
                    Object rawData = httpsCallableResult.getData();
                    if (rawData == null) {
                        Log.e("PlantNetFunction", "La réponse de la fonction est null");
                        callback.accept(null);
                        return;
                    }

                    if (!(rawData instanceof Map)) {
                        Log.e("PlantNetFunction", "La réponse n'est pas une Map mais de type: " + rawData.getClass().getSimpleName());
                        callback.accept(null);
                        return;
                    }

                    Map<String, Object> result = (Map<String, Object>) rawData;
                    Log.d("PlantNetFunction", "Result reçu: " + result.toString());

                    callback.accept(result);
                })
                .addOnFailureListener(e -> {
                    Log.e("PlantNetFunction", "Erreur lors de l'appel de la fonction Firebase", e);
                    callback.accept(null);
                });
    }


    public static void uploadImage(String path,Uri obsUri, Consumer<String> callback) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        String fileName = "images/"+path+ UUID.randomUUID().toString() + ".jpg";
        StorageReference imageRef = storageRef.child(fileName);

        imageRef.putFile(obsUri)
                .addOnSuccessListener(taskSnapshot -> {
                    imageRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                        String url = downloadUri.toString();
                        Log.d("FirebaseStorage", "Image URL: " + url);
                        callback.accept(url);
                    }).addOnFailureListener(e -> {
                        Log.e("FirebaseStorage", "Échec de l'upload", e);

                        callback.accept("");
                    });
                })
                .addOnFailureListener(e -> {
                    Log.e("FirebaseStorage", "Échec de l'upload", e);
                    callback.accept("");
                });
    }

}
