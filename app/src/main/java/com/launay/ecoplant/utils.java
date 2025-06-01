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

        FirebaseFunctions functions = FirebaseFunctions.getInstance();

        String imageBase64 = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
        Log.d("PlantNetFunction", "Image encodée en Base64, longueur: " + imageBase64.length());

        Map<String, Object> data = new HashMap<>();
        data.put("imageBase64", imageBase64);
        data.put("organs", "leaf");

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

    public static String requestAPIPlantNet(){
        String jsonresponse = "{\n" +
                "  \"query\": {\n" +
                "    \"project\": \"all\",\n" +
                "    \"images\": [\n" +
                "      \"ac09dcf8b9e111ac5fd3e8ea06bd8f82\"\n" +
                "    ],\n" +
                "    \"organs\": [\n" +
                "      \"auto\"\n" +
                "    ],\n" +
                "    \"includeRelatedImages\": false,\n" +
                "    \"noReject\": false,\n" +
                "    \"type\": null\n" +
                "  },\n" +
                "  \"predictedOrgans\": [\n" +
                "    {\n" +
                "      \"image\": \"ac09dcf8b9e111ac5fd3e8ea06bd8f82\",\n" +
                "      \"filename\": \"pissenlit.PNG\",\n" +
                "      \"organ\": \"flower\",\n" +
                "      \"score\": 0.75909\n" +
                "    }\n" +
                "  ],\n" +
                "  \"language\": \"en\",\n" +
                "  \"preferedReferential\": \"k-world-flora\",\n" +
                "  \"bestMatch\": \"Taraxacum campylodes G.E.Haglund\",\n" +
                "  \"results\": [\n" +
                "    {\n" +
                "      \"score\": 0.31602,\n" +
                "      \"species\": {\n" +
                "        \"scientificNameWithoutAuthor\": \"Taraxacum campylodes\",\n" +
                "        \"scientificNameAuthorship\": \"G.E.Haglund\",\n" +
                "        \"genus\": {\n" +
                "          \"scientificNameWithoutAuthor\": \"Taraxacum\",\n" +
                "          \"scientificNameAuthorship\": \"\",\n" +
                "          \"scientificName\": \"Taraxacum\"\n" +
                "        },\n" +
                "        \"family\": {\n" +
                "          \"scientificNameWithoutAuthor\": \"Asteraceae\",\n" +
                "          \"scientificNameAuthorship\": \"\",\n" +
                "          \"scientificName\": \"Asteraceae\"\n" +
                "        },\n" +
                "        \"commonNames\": [\n" +
                "          \"Dandelion\",\n" +
                "          \"قاصدک\",\n" +
                "          \"Garden Dandelion\"\n" +
                "        ],\n" +
                "        \"scientificName\": \"Taraxacum campylodes G.E.Haglund\"\n" +
                "      },\n" +
                "      \"gbif\": {\n" +
                "        \"id\": \"5394016\"\n" +
                "      },\n" +
                "      \"powo\": {\n" +
                "        \"id\": \"252973-1\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"score\": 0.23014,\n" +
                "      \"species\": {\n" +
                "        \"scientificNameWithoutAuthor\": \"Taraxacum sect. Taraxacum\",\n" +
                "        \"scientificNameAuthorship\": \"F.H.Wigg.\",\n" +
                "        \"genus\": {\n" +
                "          \"scientificNameWithoutAuthor\": \"Taraxacum\",\n" +
                "          \"scientificNameAuthorship\": \"\",\n" +
                "          \"scientificName\": \"Taraxacum\"\n" +
                "        },\n" +
                "        \"family\": {\n" +
                "          \"scientificNameWithoutAuthor\": \"Asteraceae\",\n" +
                "          \"scientificNameAuthorship\": \"\",\n" +
                "          \"scientificName\": \"Asteraceae\"\n" +
                "        },\n" +
                "        \"commonNames\": [\n" +
                "          \"Common dandelion\",\n" +
                "          \"Wild Dandelion\",\n" +
                "          \"Dandelion\"\n" +
                "        ],\n" +
                "        \"scientificName\": \"Taraxacum sect. Taraxacum F.H.Wigg.\"\n" +
                "      },\n" +
                "      \"gbif\": null,\n" +
                "      \"powo\": {\n" +
                "        \"id\": \"254151-1\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"score\": 0.04043,\n" +
                "      \"species\": {\n" +
                "        \"scientificNameWithoutAuthor\": \"Taraxacum mattmarkense\",\n" +
                "        \"scientificNameAuthorship\": \"Soest\",\n" +
                "        \"genus\": {\n" +
                "          \"scientificNameWithoutAuthor\": \"Taraxacum\",\n" +
                "          \"scientificNameAuthorship\": \"\",\n" +
                "          \"scientificName\": \"Taraxacum\"\n" +
                "        },\n" +
                "        \"family\": {\n" +
                "          \"scientificNameWithoutAuthor\": \"Asteraceae\",\n" +
                "          \"scientificNameAuthorship\": \"\",\n" +
                "          \"scientificName\": \"Asteraceae\"\n" +
                "        },\n" +
                "        \"commonNames\": [\n" +
                "          \"Dandelion\",\n" +
                "          \"Taraxacum Officinale\",\n" +
                "          \"قاصدک\"\n" +
                "        ],\n" +
                "        \"scientificName\": \"Taraxacum mattmarkense Soest\"\n" +
                "      },\n" +
                "      \"gbif\": {\n" +
                "        \"id\": \"5697122\"\n" +
                "      },\n" +
                "      \"powo\": {\n" +
                "        \"id\": \"253957-1\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"score\": 0.02255,\n" +
                "      \"species\": {\n" +
                "        \"scientificNameWithoutAuthor\": \"Taraxacum dissectum\",\n" +
                "        \"scientificNameAuthorship\": \"(Ledeb.) Ledeb.\",\n" +
                "        \"genus\": {\n" +
                "          \"scientificNameWithoutAuthor\": \"Taraxacum\",\n" +
                "          \"scientificNameAuthorship\": \"\",\n" +
                "          \"scientificName\": \"Taraxacum\"\n" +
                "        },\n" +
                "        \"family\": {\n" +
                "          \"scientificNameWithoutAuthor\": \"Asteraceae\",\n" +
                "          \"scientificNameAuthorship\": \"\",\n" +
                "          \"scientificName\": \"Asteraceae\"\n" +
                "        },\n" +
                "        \"commonNames\": [\n" +
                "          \"Cut-leaved Dandelion\",\n" +
                "          \"Dandelion\",\n" +
                "          \"Diente de león\"\n" +
                "        ],\n" +
                "        \"scientificName\": \"Taraxacum dissectum (Ledeb.) Ledeb.\"\n" +
                "      },\n" +
                "      \"gbif\": {\n" +
                "        \"id\": \"5394003\"\n" +
                "      },\n" +
                "      \"powo\": {\n" +
                "        \"id\": \"253216-1\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"score\": 0.02132,\n" +
                "      \"species\": {\n" +
                "        \"scientificNameWithoutAuthor\": \"Taraxacum palustre\",\n" +
                "        \"scientificNameAuthorship\": \"(Lyons) Symons\",\n" +
                "        \"genus\": {\n" +
                "          \"scientificNameWithoutAuthor\": \"Taraxacum\",\n" +
                "          \"scientificNameAuthorship\": \"\",\n" +
                "          \"scientificName\": \"Taraxacum\"\n" +
                "        },\n" +
                "        \"family\": {\n" +
                "          \"scientificNameWithoutAuthor\": \"Asteraceae\",\n" +
                "          \"scientificNameAuthorship\": \"\",\n" +
                "          \"scientificName\": \"Asteraceae\"\n" +
                "        },\n" +
                "        \"commonNames\": [\n" +
                "          \"Marsh dandelion\",\n" +
                "          \"Fen Dandelion\",\n" +
                "          \"Wild Dandelion\"\n" +
                "        ],\n" +
                "        \"scientificName\": \"Taraxacum palustre (Lyons) Symons\"\n" +
                "      },\n" +
                "      \"gbif\": {\n" +
                "        \"id\": \"5394278\"\n" +
                "      },\n" +
                "      \"powo\": {\n" +
                "        \"id\": \"30320466-2\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"score\": 0.00768,\n" +
                "      \"species\": {\n" +
                "        \"scientificNameWithoutAuthor\": \"Taraxacum parnassicum\",\n" +
                "        \"scientificNameAuthorship\": \"Dahlst.\",\n" +
                "        \"genus\": {\n" +
                "          \"scientificNameWithoutAuthor\": \"Taraxacum\",\n" +
                "          \"scientificNameAuthorship\": \"\",\n" +
                "          \"scientificName\": \"Taraxacum\"\n" +
                "        },\n" +
                "        \"family\": {\n" +
                "          \"scientificNameWithoutAuthor\": \"Asteraceae\",\n" +
                "          \"scientificNameAuthorship\": \"\",\n" +
                "          \"scientificName\": \"Asteraceae\"\n" +
                "        },\n" +
                "        \"commonNames\": [\n" +
                "          \"Parnassus Dandelion\"\n" +
                "        ],\n" +
                "        \"scientificName\": \"Taraxacum parnassicum Dahlst.\"\n" +
                "      },\n" +
                "      \"gbif\": {\n" +
                "        \"id\": \"5696522\"\n" +
                "      },\n" +
                "      \"powo\": {\n" +
                "        \"id\": \"254229-1\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"score\": 0.00594,\n" +
                "      \"species\": {\n" +
                "        \"scientificNameWithoutAuthor\": \"Taraxacum mongolicum\",\n" +
                "        \"scientificNameAuthorship\": \"Hand.-Mazz.\",\n" +
                "        \"genus\": {\n" +
                "          \"scientificNameWithoutAuthor\": \"Taraxacum\",\n" +
                "          \"scientificNameAuthorship\": \"\",\n" +
                "          \"scientificName\": \"Taraxacum\"\n" +
                "        },\n" +
                "        \"family\": {\n" +
                "          \"scientificNameWithoutAuthor\": \"Asteraceae\",\n" +
                "          \"scientificNameAuthorship\": \"\",\n" +
                "          \"scientificName\": \"Asteraceae\"\n" +
                "        },\n" +
                "        \"commonNames\": [\n" +
                "          \"Mongolian dandelion\"\n" +
                "        ],\n" +
                "        \"scientificName\": \"Taraxacum mongolicum Hand.-Mazz.\"\n" +
                "      },\n" +
                "      \"gbif\": {\n" +
                "        \"id\": \"5394645\"\n" +
                "      },\n" +
                "      \"powo\": {\n" +
                "        \"id\": \"254031-1\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"score\": 0.00475,\n" +
                "      \"species\": {\n" +
                "        \"scientificNameWithoutAuthor\": \"Sonchus arvensis\",\n" +
                "        \"scientificNameAuthorship\": \"L.\",\n" +
                "        \"genus\": {\n" +
                "          \"scientificNameWithoutAuthor\": \"Sonchus\",\n" +
                "          \"scientificNameAuthorship\": \"\",\n" +
                "          \"scientificName\": \"Sonchus\"\n" +
                "        },\n" +
                "        \"family\": {\n" +
                "          \"scientificNameWithoutAuthor\": \"Asteraceae\",\n" +
                "          \"scientificNameAuthorship\": \"\",\n" +
                "          \"scientificName\": \"Asteraceae\"\n" +
                "        },\n" +
                "        \"commonNames\": [\n" +
                "          \"Field sowthistle\",\n" +
                "          \"Sowthistle\",\n" +
                "          \"Field Milk-thistle\"\n" +
                "        ],\n" +
                "        \"scientificName\": \"Sonchus arvensis L.\"\n" +
                "      },\n" +
                "      \"gbif\": {\n" +
                "        \"id\": \"3105813\"\n" +
                "      },\n" +
                "      \"powo\": {\n" +
                "        \"id\": \"250029-1\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"score\": 0.00376,\n" +
                "      \"species\": {\n" +
                "        \"scientificNameWithoutAuthor\": \"Taraxacum erythrospermum\",\n" +
                "        \"scientificNameAuthorship\": \"Andrz. ex Besser\",\n" +
                "        \"genus\": {\n" +
                "          \"scientificNameWithoutAuthor\": \"Taraxacum\",\n" +
                "          \"scientificNameAuthorship\": \"\",\n" +
                "          \"scientificName\": \"Taraxacum\"\n" +
                "        },\n" +
                "        \"family\": {\n" +
                "          \"scientificNameWithoutAuthor\": \"Asteraceae\",\n" +
                "          \"scientificNameAuthorship\": \"\",\n" +
                "          \"scientificName\": \"Asteraceae\"\n" +
                "        },\n" +
                "        \"commonNames\": [\n" +
                "          \"Rock dandelion\",\n" +
                "          \"Red-seed Dandelion\",\n" +
                "          \"Red-seeded Dandelion\"\n" +
                "        ],\n" +
                "        \"scientificName\": \"Taraxacum erythrospermum Andrz. ex Besser\"\n" +
                "      },\n" +
                "      \"gbif\": {\n" +
                "        \"id\": \"5393872\"\n" +
                "      },\n" +
                "      \"powo\": {\n" +
                "        \"id\": \"249536-2\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"score\": 0.00329,\n" +
                "      \"species\": {\n" +
                "        \"scientificNameWithoutAuthor\": \"Taraxacum platycarpum\",\n" +
                "        \"scientificNameAuthorship\": \"Dahlst.\",\n" +
                "        \"genus\": {\n" +
                "          \"scientificNameWithoutAuthor\": \"Taraxacum\",\n" +
                "          \"scientificNameAuthorship\": \"\",\n" +
                "          \"scientificName\": \"Taraxacum\"\n" +
                "        },\n" +
                "        \"family\": {\n" +
                "          \"scientificNameWithoutAuthor\": \"Asteraceae\",\n" +
                "          \"scientificNameAuthorship\": \"\",\n" +
                "          \"scientificName\": \"Asteraceae\"\n" +
                "        },\n" +
                "        \"commonNames\": [\n" +
                "          \"Japanese dandelion\"\n" +
                "        ],\n" +
                "        \"scientificName\": \"Taraxacum platycarpum Dahlst.\"\n" +
                "      },\n" +
                "      \"gbif\": {\n" +
                "        \"id\": \"5393953\"\n" +
                "      },\n" +
                "      \"powo\": {\n" +
                "        \"id\": \"254346-1\"\n" +
                "      }\n" +
                "    }\n" +
                "  ],\n" +
                "  \"version\": \"2025-01-17 (7.3)\"\n" +
                "}";
        return jsonresponse;
    }
}
