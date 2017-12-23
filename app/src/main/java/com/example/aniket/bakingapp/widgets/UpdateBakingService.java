package com.example.aniket.bakingapp.widgets;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.aniket.bakingapp.data.CakeWidgetConstants;

import java.util.ArrayList;


public class UpdateBakingService extends IntentService {

    public UpdateBakingService() {
        super("UpdateBakingService");
    }

    public static void startBakingService(Context context, ArrayList<String> fromActivityIngredientsList) {
        Intent intent = new Intent(context, UpdateBakingService.class);
        intent.putExtra(CakeWidgetConstants.FROM_ACTIVITY_INGREDIENTS_LIST, fromActivityIngredientsList);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            ArrayList<String> fromActivityIngredientsList =
                    intent.getExtras().getStringArrayList(
                            CakeWidgetConstants.FROM_ACTIVITY_INGREDIENTS_LIST);
            handleActionUpdateBakingWidgets(fromActivityIngredientsList);

        }
    }

    private void handleActionUpdateBakingWidgets(ArrayList<String> fromActivityIngredientsList) {
        System.out.println("Aniket3, here: " + fromActivityIngredientsList.get(0));

        Intent intent = new Intent("android.appwidget.action.APPWIDGET_UPDATE2");
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE2");
        intent.putExtra(CakeWidgetConstants.FROM_ACTIVITY_INGREDIENTS_LIST, fromActivityIngredientsList);
        sendBroadcast(intent);
    }
}
