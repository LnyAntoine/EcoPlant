package com.launay.ecoplant;

public class utils {
    public interface AuthCallback {
        void onComplete(boolean success);
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
